package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment.NOALIGNMENT;

public class MarkdownTablePrinter implements TablePrinter {
    private static final int DEFAULT_COLUMN_LENGTH = 3;
    private static final String EMPTY_SYMBOL = " ";
    private static final String ALIGNMENT_SYMBOL1 = "-";
    private static final String ALIGNMENT_SYMBOL2 = ":";
    private static final String COLUMN_SEPARATOR_SYMBOL = "|";

    private List<Integer> maxColumnLengths = new ArrayList<>();

    @Override
    public Collection<String> printTable(Table table, ColumnAlignment... alignments) {
        if (table == null) {
            throw new IllegalArgumentException("Table can't be null");
        }

        getMaxColumnLengths(table);

        List<StringBuilder> result = new ArrayList<>();

        result.add(row(table.getColumnNames().toArray(new String[0])));
        result.add(row(alignments));

        for (int i = 0; i < table.getRowsCount() - 1; i++) {
            result.add(new StringBuilder(COLUMN_SEPARATOR_SYMBOL));
        }

        int column = 0;
        for (String title : table.getColumnNames()) {
            addColumn(result, table.getColumnData(title), maxColumnLengths.get(column));

            column++;
        }

        return convertCollection(result);
    }

    private void addColumn(List<StringBuilder> collection, Collection<String> columnValues, int maxColumnLength) {
        int emptySpaces = 0;
        int index = 2;

        for (String value : columnValues) {
            emptySpaces = maxColumnLength - value.length();

            collection.get(index).append(EMPTY_SYMBOL);
            collection.get(index).append(value);
            collection.get(index).append(EMPTY_SYMBOL.repeat(Math.max(0, emptySpaces)));
            collection.get(index).append(EMPTY_SYMBOL);
            collection.get(index).append(COLUMN_SEPARATOR_SYMBOL);

            index++;
        }
    }

    private List<String> convertCollection(List<StringBuilder> list) {
        List<String> result = new ArrayList<>();

        for (StringBuilder sb : list) {
            result.add(sb.toString());
        }

        return Collections.unmodifiableList(result);
    }

    private StringBuilder row(String[] values) {
        StringBuilder result = new StringBuilder(COLUMN_SEPARATOR_SYMBOL);
        int emptySpaces = 0;
        int columnIndex = 0;

        for (String s : values) {
            emptySpaces = maxColumnLengths.get(columnIndex) - s.length();

            result.append(EMPTY_SYMBOL);
            result.append(s);
            result.append(EMPTY_SYMBOL.repeat(Math.max(0, emptySpaces)));
            result.append(EMPTY_SYMBOL);
            result.append(COLUMN_SEPARATOR_SYMBOL);

            columnIndex++;
        }

        return result;
    }

    private StringBuilder row(ColumnAlignment... alignments) {
        StringBuilder result = new StringBuilder(COLUMN_SEPARATOR_SYMBOL);

        for (int columnIndex = 0; columnIndex < maxColumnLengths.size(); columnIndex++) {
            result.append(EMPTY_SYMBOL);

            if (columnIndex < alignments.length) {
                result.append(getAlignment(alignments[columnIndex], maxColumnLengths.get(columnIndex)));
            } else {
                result.append(getAlignment(NOALIGNMENT, maxColumnLengths.get(columnIndex)));
            }

            result.append(EMPTY_SYMBOL);
            result.append(COLUMN_SEPARATOR_SYMBOL);
        }

        return result;
    }

    private String getAlignment(ColumnAlignment alignment, int totalSize) {
        if (alignment == NOALIGNMENT) {
            return ALIGNMENT_SYMBOL1.repeat(Math.max(0, totalSize));
        }

        StringBuilder result = new StringBuilder();

        if (alignment == ColumnAlignment.LEFT || alignment == ColumnAlignment.CENTER) {
            result.append(ALIGNMENT_SYMBOL2);
        } else {
            result.append(ALIGNMENT_SYMBOL1);
        }

        result.append(ALIGNMENT_SYMBOL1.repeat(Math.max(0, totalSize - 2)));

        if (alignment == ColumnAlignment.RIGHT || alignment == ColumnAlignment.CENTER) {
            result.append(ALIGNMENT_SYMBOL2);
        } else {
            result.append(ALIGNMENT_SYMBOL1);
        }

        return result.toString();
    }

    private void getMaxColumnLengths(Table table) {
        for (String columnName : table.getColumnNames()) {
            int maxLength = Math.max(columnName.length(), DEFAULT_COLUMN_LENGTH);

            for (String column : table.getColumnData(columnName)) {
                maxLength = Math.max(maxLength, column.length());
            }

            maxColumnLengths.add(maxLength);
        }
    }

}
