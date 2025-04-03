package bg.sofia.uni.fmi.mjt.order.server.repository;

import bg.sofia.uni.fmi.mjt.order.server.Response;
import bg.sofia.uni.fmi.mjt.order.server.destination.Destination;
import bg.sofia.uni.fmi.mjt.order.server.order.Order;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Color;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.Size;
import bg.sofia.uni.fmi.mjt.order.server.tshirt.TShirt;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MJTOrderRepositoryTest {
    private static OrderRepository repository = new MJTOrderRepository();

    @BeforeAll
    static void addOrdersInRepository() {
        repository.request("S", "BLACK", "EUROPE");
    }

    @Test
    void testRequestWithNullSize() {
        assertThrows(IllegalArgumentException.class ,
                () -> repository.request(null, "BLACK", "EUROPE"),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testRequestWithEmptySize() {
        assertThrows(IllegalArgumentException.class ,
                () -> repository.request("", "BLACK", "EUROPE"),
                "IllegalArgumentException was expected but wasn't thrown");

        assertThrows(IllegalArgumentException.class ,
                () -> repository.request("  ", "BLACK", "EUROPE"),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testRequestWithNullColor() {
        assertThrows(IllegalArgumentException.class ,
                () -> repository.request("S", null, "EUROPE"),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testRequestWithEmptyColor() {
        assertThrows(IllegalArgumentException.class ,
                () -> repository.request("S", "", "EUROPE"),
                "IllegalArgumentException was expected but wasn't thrown");

        assertThrows(IllegalArgumentException.class ,
                () -> repository.request("S", "  ", "EUROPE"),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testRequestWithNullDestination() {
        assertThrows(IllegalArgumentException.class ,
                () -> repository.request("S", "BLACK", null),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testRequestWithEmptyDestination() {
        assertThrows(IllegalArgumentException.class ,
                () -> repository.request("S", "BLACK", ""),
                "IllegalArgumentException was expected but wasn't thrown");

        assertThrows(IllegalArgumentException.class ,
                () -> repository.request("S", "BLACK", "  "),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testRequestWithInvalidSize() {
        String expectedAdditionalInfo = "invalid: size";
        Response actual = repository.request("K", "BLACK", "EUROPE");

        assertEquals(expectedAdditionalInfo, actual.additionalInfo(),
                "Expected additionalInfo : " + expectedAdditionalInfo +
                        ", but it was : " + actual.additionalInfo());
        assertTrue(actual.orders().isEmpty(),
                "Expected orders to be empty, but it was : " + actual.orders());
    }

    @Test
    void testRequestWithInvalidColor() {
        String expectedAdditionalInfo = "invalid: color";
        Response actual = repository.request("S", "GREEN", "EUROPE");

        assertEquals(expectedAdditionalInfo, actual.additionalInfo(),
                "Expected additionalInfo : " + expectedAdditionalInfo +
                        ", but it was : " + actual.additionalInfo());
        assertTrue(actual.orders().isEmpty(),
                "Expected orders to be empty, but it was : " + actual.orders());
    }

    @Test
    void testRequestWithInvalidDestination() {
        String expectedAdditionalInfo = "invalid: destination";
        Response actual = repository.request("S", "RED", "CHINA");

        assertEquals(expectedAdditionalInfo, actual.additionalInfo(),
                "Expected additionalInfo : " + expectedAdditionalInfo +
                        ", but it was : " + actual.additionalInfo());
        assertTrue(actual.orders().isEmpty(),
                "Expected orders to be empty, but it was : " + actual.orders());
    }

    @Test
    void testRequestWithInvalidColorAndDestination() {
        String expectedAdditionalInfo = "invalid: color,destination";
        Response actual = repository.request("S", "GREEN", "CHINA");

        assertEquals(expectedAdditionalInfo, actual.additionalInfo(),
                "Expected additionalInfo : " + expectedAdditionalInfo +
                        ", but it was : " + actual.additionalInfo());
        assertTrue(actual.orders().isEmpty(),
                "Expected orders to be empty, but it was : " + actual.orders());
    }

    @Test
    void testRequestWithInvalidSizeAndDestination() {
        String expectedAdditionalInfo = "invalid: size,destination";
        Response actual = repository.request("K", "RED", "CHINA");

        assertEquals(expectedAdditionalInfo, actual.additionalInfo(),
                "Expected additionalInfo : " + expectedAdditionalInfo +
                        ", but it was : " + actual.additionalInfo());
        assertTrue(actual.orders().isEmpty(),
                "Expected orders to be empty, but it was : " + actual.orders());
    }

    @Test
    void testRequestWithInvalidSizeAndColor() {
        String expectedAdditionalInfo = "invalid: size,color";
        Response actual = repository.request("K", "GREEN", "EUROPE");

        assertEquals(expectedAdditionalInfo, actual.additionalInfo(),
                "Expected additionalInfo : " + expectedAdditionalInfo +
                        ", but it was : " + actual.additionalInfo());
        assertTrue(actual.orders().isEmpty(),
                "Expected orders to be empty, but it was : " + actual.orders());
    }

    @Test
    void testRequestWithInvalidSizeColorAndDestination() {
        String expectedAdditionalInfo = "invalid: size,color,destination";
        Response actual = repository.request("K", "GREEN", "CHINA");

        assertEquals(expectedAdditionalInfo, actual.additionalInfo(),
                "Expected additionalInfo : " + expectedAdditionalInfo +
                        ", but it was : " + actual.additionalInfo());
        assertTrue(actual.orders().isEmpty(),
                "Expected orders to be empty, but it was : " + actual.orders());
    }

    @Test
    void testRequestWithCorrectData() {
        String expectedAdditionalInfo = "ORDER_ID=2";
        Response actual = repository.request("S", "BLACK", "EUROPE");

        assertEquals(expectedAdditionalInfo, actual.additionalInfo(),
                "Expected additionalInfo : " + expectedAdditionalInfo +
                        ", but it was : " + actual.additionalInfo());
        assertTrue(actual.orders().isEmpty(),
                "Expected orders to be empty, but it was : " + actual.orders());
    }

    @Test
    void testGetOrderByIdWithInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> repository.getOrderById(-1),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> repository.getOrderById(-7),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> repository.getOrderById(-12),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> repository.getOrderById(0),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testGetOrderByIdWithIncorrectId() {
        String expectedAdditionalInfo = "Order with id = 4 does not exist.";
        Response actual = repository.getOrderById(4);

        assertEquals(expectedAdditionalInfo, actual.additionalInfo(),
                "Expected additionalInfo : " + expectedAdditionalInfo +
                        ", but it was : " + actual.additionalInfo());
        assertTrue(actual.orders().isEmpty(),
                "Expected orders to be empty, but it was : " + actual.orders());
    }

    @Test
    void testGetOrderByIdIsCorrect() {
        Response actual = repository.getOrderById(1);

        Order expectedOrder = new Order(1, new TShirt(Size.S, Color.BLACK), Destination.EUROPE);
        Order actualOrder = actual.orders().iterator().next();

        assertTrue(actual.additionalInfo().isBlank(),
                "Expected additionalInfo to be blank, but it was : " + actual.additionalInfo());
        assertEquals(1, actual.orders().size(),
                "Expected orders size to be 1, but it was : " + actual.orders().size());
        assertEquals(expectedOrder, actualOrder,
                "Expected order : " + expectedOrder +
                        ", but it was : " + actualOrder);
    }

    @Test
    void testGetAllOrdersWithTwoInvalidSizes() {
        OrderRepository repo = new MJTOrderRepository();
        repo.request("S", "RED", "AUSTRALIA");
        repo.request("XLL", "BLACK", "NORTH_AMERICA");
        repo.request("XS", "WHITE", "NORTH_AMERICA");

        List<Order> expected = List.of(
                new Order(1, new TShirt(Size.S, Color.RED), Destination.AUSTRALIA),
                new Order(-1, new TShirt(Size.UNKNOWN, Color.BLACK), Destination.NORTH_AMERICA),
                new Order(-1, new TShirt(Size.UNKNOWN, Color.WHITE), Destination.NORTH_AMERICA)
        );

        Collection<Order> actual = repo.getAllOrders().orders();

        assertEquals(expected.size(), actual.size(),
                "Expected size : " + expected.size() + ", but it was : " + actual.size());
        assertTrue(actual.containsAll(expected),
                "Expected : " + expected + ", but it was : " + actual);
    }
}
