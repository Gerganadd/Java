package bg.sofia.uni.fmi.mjt.csvprocessor.table.printer;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MarkdownTablePrinterTest {
    @Test
    void testPrintTableNullTable() {
        assertThrows(IllegalArgumentException.class,
                () -> new MarkdownTablePrinter().printTable(null),
                "IllegalArgumentException was expected but was not thrown");
    }

    @Test
    void testPrintTableIsUnmodifiableCollection() throws CsvDataNotCorrectException {
        Table table = new BaseTable();
        table.addData(new String[]{"Ivan", "Dragan"}); // 2 columns

        assertThrows(UnsupportedOperationException.class,
                () -> new MarkdownTablePrinter()
                        .printTable(table, ColumnAlignment.LEFT, ColumnAlignment.NOALIGNMENT)
                        .add("Gogo"),
                "Collection must be unmodifiable");
    }

    @Test
    void testPrintTableIsCorrect() throws CsvDataNotCorrectException {
        Table table = new BaseTable();
        table.addData(new String[]{"Name", "Last name"}); // 2 columns
        table.addData(new String[]{"Ivo", "Andonov"});

        List<String> expectedResult = new ArrayList<>();
        expectedResult.add("| Name | Last name |");
        expectedResult.add("| :--- | --------: |");
        expectedResult.add("| Ivo  | Andonov   |");

        StringBuilder expectedResultStr = new StringBuilder();
        for (String s : expectedResult) {
            expectedResultStr.append(s).append(", ");
        }

        Collection<String> result = new MarkdownTablePrinter()
                .printTable(table, ColumnAlignment.LEFT, ColumnAlignment.RIGHT);

        StringBuilder givenResultStr = new StringBuilder();
        for (String s : result) {
            givenResultStr.append(s).append(", ");
        }

        assertIterableEquals(expectedResult, result,
                "Expected collection with content : " + expectedResultStr +
                ", but it was : " + givenResultStr);
    }

}
