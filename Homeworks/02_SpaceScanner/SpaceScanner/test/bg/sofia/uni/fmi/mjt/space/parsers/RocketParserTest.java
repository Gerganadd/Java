package bg.sofia.uni.fmi.mjt.space.parsers;

import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RocketParserTest {
    @Test
    void testParseRocketsFromWithNullReader() {
        assertThrows(IllegalArgumentException.class, () -> RocketParser.parseRocketsFrom(null),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseRocketsFromIsCorrect() {
        StringReader reader = new StringReader("\"\",Name,Wiki,Rocket Height\n" +
                "0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m\n" +
                "1,Tsyklon-4M,,38.7 m\n" +
                "2,Unha-2,https://en.wikipedia.org/wiki/Unha, \n" +
                "3,Unha-3,,\n");
        Set<Rocket> actual = RocketParser.parseRocketsFrom(reader);
        reader.close();

        Set<Rocket> expected = getExpected();

        assertEquals(4, actual.size());

        assertTrue(actual.containsAll(expected),
                "Expected : " + expected + ", but it was : " + actual);
    }

    private static Set<Rocket> getExpected() {
        Rocket r1 = new Rocket("0", "Tsyklon-3",
                Optional.of("https://en.wikipedia.org/wiki/Tsyklon-3"), Optional.of(39.0));
        Rocket r2 = new Rocket("1", "Tsyklon-4M",
                Optional.empty(), Optional.of(38.7));
        Rocket r3 = new Rocket("2", "Unha-2",
                Optional.of("https://en.wikipedia.org/wiki/Unha"), Optional.empty());
        Rocket r4 = new Rocket("3", "Unha-3",
                Optional.empty(), Optional.empty());

        return Set.of(r1, r2, r3, r4);
    }

    @Test
    void testParseRocketWithNullRow() {
        assertThrows(IllegalArgumentException.class, () -> RocketParser.parseRocket(null),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseRocketWithEmptyRow() {
        assertThrows(IllegalArgumentException.class, () -> RocketParser.parseRocket(""),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class, () -> RocketParser.parseRocket("     "),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseRocketWithFullCorrectData() {
        String rocketInfo = ("4,Vanguard,https://en.wikipedia.org/wiki/Vanguard,23.0 m");

        Rocket actualRocket = RocketParser.parseRocket(rocketInfo);

        assertEquals("4", actualRocket.id());
        assertEquals("Vanguard", actualRocket.name());
        assertEquals(Optional.of("https://en.wikipedia.org/wiki/Vanguard"), actualRocket.wiki());
        assertEquals(Optional.of(23.0), actualRocket.height());
    }

    @Test
    void testParseRocketWithoutWiki() {
        String rocketInfo = "4,Vanguard,,23.0 m";

        Rocket actualRocket = RocketParser.parseRocket(rocketInfo);

        assertEquals("4", actualRocket.id());
        assertEquals("Vanguard", actualRocket.name());
        assertEquals(Optional.empty(), actualRocket.wiki());
        assertEquals(Optional.of(23.0), actualRocket.height());
    }

    @Test
    void testParseRocketWithoutHeight() {
        String rocketInfo = "4,Vanguard,https://en.wikipedia.org/wiki/Vanguard,";

        Rocket actualRocket = RocketParser.parseRocket(rocketInfo);

        assertEquals("4", actualRocket.id());
        assertEquals("Vanguard", actualRocket.name());
        assertEquals(Optional.of("https://en.wikipedia.org/wiki/Vanguard"), actualRocket.wiki());
        assertEquals(Optional.empty(), actualRocket.height());
    }

    @Test
    void testParseRocketWithoutWikiAndHeight() {
        String rocketInfo = "4,Vanguard,,";

        Rocket actualRocket = RocketParser.parseRocket(rocketInfo);

        assertEquals("4", actualRocket.id());
        assertEquals("Vanguard", actualRocket.name());
        assertEquals(Optional.empty(), actualRocket.wiki());
        assertEquals(Optional.empty(), actualRocket.height());
    }

    @Test
    void testParseRocketWithoutId() {
        String rocketInfo = ",Vanguard,https://en.wikipedia.org/wiki/Vanguard_(rocket),25.0 m";

        assertThrows(IllegalArgumentException.class, () -> RocketParser.parseRocket(rocketInfo),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testParseRocketWithoutName() {
        String rocketInfo = "4,,https://en.wikipedia.org/wiki/Vanguard_(rocket),25.0 m";

        assertThrows(IllegalArgumentException.class, () -> RocketParser.parseRocket(rocketInfo),
                "IllegalArgumentException was expected but wasn't thrown");
    }
}
