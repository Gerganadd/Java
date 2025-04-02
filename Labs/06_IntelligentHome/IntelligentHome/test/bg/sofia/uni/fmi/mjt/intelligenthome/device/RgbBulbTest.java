package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

public class RgbBulbTest {
    @Test
    void testGetIdIsCorrect() {
        RgbBulb bulb = new RgbBulb("Bulb", 1.0, LocalDateTime.now());

        assertTrue(bulb.getId().startsWith("BLB-Bulb-"),
                "Id doesn't match on BLB-<device name>-<unique number per device type>");

        int uniqueNumberStartIndex = bulb.getId().lastIndexOf('-');
        String uniqueNumberStr = bulb.getId().substring(uniqueNumberStartIndex);
        int uniqueNumber = Integer.parseInt(uniqueNumberStr);
    }

    @Test
    void testGetIdOnDifferentObjects() {
        RgbBulb bulb = new RgbBulb("Bulb", 1.0, LocalDateTime.now());
        RgbBulb bulb1 = new RgbBulb("Bulb", 1.3, LocalDateTime.now());

        assertNotEquals(bulb.getId(), bulb1.getId(), "Different objects must have different id");
        assertNotSame(bulb.getId(), bulb1.getId(), "Different objects must have different id");
    }

    @Test
    void testGetIdOnSameObjects() {
        RgbBulb bulb = new RgbBulb("Bulb", 1.0, LocalDateTime.now());
        RgbBulb bulb1 = bulb;

        assertEquals(bulb.getId(), bulb1.getId(), "Same objects must have same id");
        assertSame(bulb.getId(), bulb1.getId(), "Same objects must have same id");
    }

    @Test
    void testGetIdNotNull() {
        RgbBulb bulb = new RgbBulb("Bulb", 1.0, LocalDateTime.now());
        assertNotNull(bulb.getId(), "Id can't be null");
    }

    @Test
    void testGetNameNotNull() {
        RgbBulb bulb = new RgbBulb("Bulb", 1.0, LocalDateTime.now());
        assertNotNull(bulb.getName(), "Name can't be null");
    }

    @Test
    void testGetTypeNotNull() {
        RgbBulb bulb = new RgbBulb("Bulb", 1.0, LocalDateTime.now());
        assertNotNull(bulb.getType(), "Type can't be null");
    }

    @Test
    void testGetTypeCorrect() {
        RgbBulb bulb = new RgbBulb("Bulb", 1.0, LocalDateTime.now());
        assertEquals(DeviceType.BULB, bulb.getType(), "Type must be BLB. Your type is " + bulb.getType());
    }
}
