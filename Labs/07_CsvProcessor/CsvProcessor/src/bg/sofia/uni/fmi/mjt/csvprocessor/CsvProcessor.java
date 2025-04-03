package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.ColumnAlignment;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.printer.MarkdownTablePrinter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Reader;
import java.io.Writer;
import java.io.IOException;

import java.util.Collection;

public class CsvProcessor implements CsvProcessorAPI {
    private Table table;
    public CsvProcessor() {
        this(new BaseTable());
    }

    public CsvProcessor(Table table) {
        this.table = table;
    }

    @Override
    public void readCsv(Reader reader, String delimiter) throws CsvDataNotCorrectException {
        if (reader == null) {
            throw new IllegalArgumentException("Reader can't be null");
        }
        if (delimiter == null || delimiter.isEmpty()) {
            throw new IllegalArgumentException("Delimiter can't be null or empty");
        }

        try (BufferedReader input = new BufferedReader(reader)) {
            String[] titles = input.readLine().split("\\Q" + delimiter + "\\E");
            int columnsCount = titles.length;

            table.addData(titles);

            String line = input.readLine();
            while (line != null && !line.isEmpty()) {
                String[] row = line.split("\\Q" + delimiter + "\\E");

                if (row.length != columnsCount) {
                    throw new CsvDataNotCorrectException("Table must have " + columnsCount
                            + " but there have line with " + row.length + " columns");
                }

                table.addData(row);
                line = input.readLine();
            }
        } catch (IOException e) {
            //to-do throw new exception
        }
    }

    @Override
    public void writeTable(Writer writer, ColumnAlignment... alignments) {
        if (writer == null) {
            throw new IllegalArgumentException("Writer can't be null");
        }

        Collection<String> content = new MarkdownTablePrinter().printTable(table, alignments);
        if (content == null || content.isEmpty()) {
            return; // or do something
        }

        try (BufferedWriter output = new BufferedWriter(writer)) {
            int index = 0;
            for (String row : content) {
                output.write(row);

                if (index < content.size() - 1) {
                    output.write("\n");
                }
                index++;
            }
            output.flush();
        } catch (IOException e) {
            //to-do
        }
    }
}
