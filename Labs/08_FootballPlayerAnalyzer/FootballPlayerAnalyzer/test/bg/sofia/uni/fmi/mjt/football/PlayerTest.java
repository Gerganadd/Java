package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlayerTest {
    @Test
    void testConstructPlayerFromEmptyString() {
        assertThrows(IllegalArgumentException.class, () -> Player.of(""),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testConstructPlayerWithNullString() {
        assertThrows(IllegalArgumentException.class, () -> Player.of(null),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testConstructPlayerWithInvalidPosition() {
        String line = "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,STMK;Argentina;94;94;110500000;565000;Left";
        assertThrows(IllegalArgumentException.class, () -> Player.of(line),
                "IllegalArgumentException was expected but wasn't thrown");
    }
}
