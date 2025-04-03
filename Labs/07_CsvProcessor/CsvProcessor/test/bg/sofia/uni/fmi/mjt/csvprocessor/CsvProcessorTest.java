package bg.sofia.uni.fmi.mjt.csvprocessor;

import bg.sofia.uni.fmi.mjt.csvprocessor.exceptions.CsvDataNotCorrectException;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.BaseTable;
import bg.sofia.uni.fmi.mjt.csvprocessor.table.Table;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CsvProcessorTest {
    private static Reader reader;
    private static Writer writer;

    @BeforeAll
    static void openStreams() {
        reader = new StringReader("Name,Last name \nIvan,Ivanov \nDragan,Draganov");
        writer = new StringWriter();
    }

    @AfterAll
    static void closeStreams() throws IOException {
        reader.close();
        writer.close();
    }

    @Test
    void testReadCsvNullReader() {
        CsvProcessorAPI processor = new CsvProcessor();

        assertThrows(IllegalArgumentException.class,
                () -> processor.readCsv(null, ","),
                "IllegalArgumentException was expected but was not thrown");
    }

    @Test
    void testReadCsvNullDelimiter() {
        CsvProcessorAPI processor = new CsvProcessor();

        assertThrows(IllegalArgumentException.class,
                () -> processor.readCsv(reader, null),
                "IllegalArgumentException was expected but was not thrown");
    }

    @Test
    void testReadCsvEmptyDelimiter() {
        CsvProcessorAPI processor = new CsvProcessor();

        assertThrows(IllegalArgumentException.class,
                () -> processor.readCsv(reader, ""),
                "IllegalArgumentException was expected but was not thrown");
    }

    @Test
    void testReadCsvIsCorrect() {
        CsvProcessorAPI processor = new CsvProcessor();

        assertDoesNotThrow(() -> processor.readCsv(reader, ","),
                "Doesn't expect to throw CsvDataNotCorrectException but it was thown");
    }

    @Test
    void testWriteTableNullWriter() {
        CsvProcessorAPI processor = new CsvProcessor();

        assertThrows(IllegalArgumentException.class,
                () -> processor.writeTable(null),
                "IllegalArgumentException was expected but was not thrown");
    }

    @Test
    void testWriteTableIsCorrect() throws CsvDataNotCorrectException {
        Table table = new BaseTable();
        table.addData(new String[]{"Name", "Last name"});
        table.addData(new String[]{"Ivo", "Kirov"});
        table.addData(new String[]{"Elvis", "Presley"});

        CsvProcessorAPI processor = new CsvProcessor(table);
        processor.writeTable(writer);

        String expected = "| Name  | Last name |\n" +
                          "| ----- | --------- |\n" +
                          "| Ivo   | Kirov     |\n" +
                          "| Elvis | Presley   |";

        assertEquals(expected, writer.toString(),
                "Expected : " + expected + " but it was : " + writer.toString());
    }

}
