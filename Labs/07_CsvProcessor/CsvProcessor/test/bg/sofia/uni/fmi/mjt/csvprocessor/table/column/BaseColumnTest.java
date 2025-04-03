package bg.sofia.uni.fmi.mjt.csvprocessor.table.column;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseColumnTest {
    @Test
    void addDataNullData() {
        Column col = new BaseColumn();

        assertThrows(IllegalArgumentException.class,
                () -> col.addData(null),
                "IllegalArgumentException was expected but was not thrown");
    }

    @Test
    void addDataEmptyData() {
        Column col = new BaseColumn();

        assertThrows(IllegalArgumentException.class,
                () -> col.addData("  "),
                "IllegalArgumentException was expected but was not thrown");
    }

    @Test
    void getDataIsCorrectWithTwoElementsWithSameData() {
        Column col = new BaseColumn();
        col.addData("Ivan");
        col.addData("Dragan");
        col.addData("Ivan");

        Collection<String> result = new ArrayList<>();
        result.add("Ivan");
        result.add("Dragan");

        StringBuilder givenResult = new StringBuilder();
        for (String s : col.getData()) {
            givenResult.append(s).append(", ");
        }

        assertIterableEquals(result, col.getData(),
                "Expected collection with content : Ivan, Dragan, but it was : " +
                givenResult.toString());

    }

    @Test
    void addDataIsUnmodifiableCollection() {
        Column col = new BaseColumn();
        col.addData("Ivan");
        col.addData("Dragan");

        assertThrows(UnsupportedOperationException.class,
                () -> col.getData().add("Gogo"),
                "Collection must be unmodifiable");
    }

}
