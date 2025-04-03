package bg.sofia.uni.fmi.mjt.csvprocessor.table;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseTableTest {
    @Test
    void testAddDataWithNullData() {
        Table table = new BaseTable();

        assertThrows(IllegalArgumentException.class,
                () -> table.addData(null),
                "IllegalArgumentExpected but was not thrown");
    }

    @Test
    void testAddDataWithLowerCountColumns() throws CsvDataNotCorrectException {
        Table table = new BaseTable();
        table.addData(new String[]{"Ivan", "Gogo"});

        assertThrows(CsvDataNotCorrectException.class,
                () -> table.addData(new String[]{"Pesho"}),
                "CsvDataNotCorrectException but was not thrown");
    }

    @Test
    void testAddDataWithBiggerCountColumns() throws CsvDataNotCorrectException {
        Table table = new BaseTable();
        table.addData(new String[]{"Ivan", "Gogo"});

        assertThrows(CsvDataNotCorrectException.class,
                () -> table.addData(new String[]{"Pesho", "Gogo", "Ema"}),
                "CsvDataNotCorrectException but was not thrown");
    }

    @Test
    void testGetColumnDataWithNullColumnName() {
        Table table = new BaseTable();

        assertThrows(IllegalArgumentException.class,
                () -> table.getColumnData(null),
                "IllegalArgumentExpected but was not thrown");
    }

    @Test
    void testGetColumnDataWithEmptyColumnName() {
        Table table = new BaseTable();

        assertThrows(IllegalArgumentException.class,
                () -> table.getColumnData(""),
                "IllegalArgumentExpected but was not thrown");
    }

    @Test
    void testGetColumnDataWithNotExistingColumnName() {
        Table table = new BaseTable();

        assertThrows(IllegalArgumentException.class,
                () -> table.getColumnData("Ivan"),
                "IllegalArgumentExpected but was not thrown");
    }

    @Test
    void testGetRowsCountEmptyTable() {
        Table table = new BaseTable();

        assertEquals(0, table.getRowsCount());
    }

    @Test
    void testGetRowsCountIsCorrect() throws CsvDataNotCorrectException {
        Table table = new BaseTable();
        table.addData(new String[]{"Ivan", "Dragan"});

        assertEquals(1, table.getRowsCount());
    }
}
