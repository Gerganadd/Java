package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.BaseColumn;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.column.Column;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.Collections;

public class BaseTable implements Table {
    private Map<String, Column> table;

    public BaseTable() {
        table = new LinkedHashMap<>();
    }

    @Override
    public void addData(String[] data) throws CsvDataNotCorrectException {
        if (data == null) {
            throw new IllegalArgumentException("Data can't be null");
        }

        checkData(data);

        if (table.isEmpty()) {
            for (String title : data) {
                table.put(title, new BaseColumn());
            }
        } else {
            int index = 0;
            for (Column col : table.values()) {
                col.addData(data[index++]);
            }
        }
    }

    @Override
    public Collection<String> getColumnNames() {
        return Collections.unmodifiableSet(table.keySet());
    }

    @Override
    public Collection<String> getColumnData(String column) {
        if (column == null || column.isBlank()) {
            throw new IllegalArgumentException("Columns can't be null or empty");
        }
        if (table.containsKey(column)) {
            return table.get(column).getData();
        }

        throw new IllegalArgumentException("There have no such column");
    }

    @Override
    public int getRowsCount() {
        if (table.isEmpty()) {
            return 0;
        }
        return table.values().iterator().next().getData().size() + 1;
    }

    private void checkData(String[] data) throws CsvDataNotCorrectException {
        if (!table.isEmpty() && data.length != getColumnNames().size()) {
            throw new CsvDataNotCorrectException("Data.length must be the same as columns count");
        }
    }
}
