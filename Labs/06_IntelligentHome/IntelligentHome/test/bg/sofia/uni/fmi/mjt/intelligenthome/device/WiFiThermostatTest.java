package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WiFiThermostatTest {
    @Test
    void testGetIdIsCorrect() {
        WiFiThermostat thermostat = new WiFiThermostat("Thermostat", 1.0, LocalDateTime.now());

        assertTrue(thermostat.getId().startsWith("TMST-Thermostat-"),
                "Id doesn't match on TMST-<device name>-<unique number per device type>");

        int uniqueNumberStartIndex = thermostat.getId().lastIndexOf('-');
        String uniqueNumberStr = thermostat.getId().substring(uniqueNumberStartIndex);
        int uniqueNumber = Integer.parseInt(uniqueNumberStr);
    }

    @Test
    void testGetIdOnDifferentObjects() {
        WiFiThermostat thermostat = new WiFiThermostat("Thermostat", 1.0, LocalDateTime.now());
        WiFiThermostat thermostat1 = new WiFiThermostat("Thermostat", 1.3, LocalDateTime.now());

        assertNotEquals(thermostat.getId(), thermostat1.getId(), "Different objects must have different id");
        assertNotSame(thermostat.getId(), thermostat1.getId(), "Different objects must have different id");
    }

    @Test
    void testGetIdOnSameObjects() {
        WiFiThermostat thermostat = new WiFiThermostat("Thermostat", 1.0, LocalDateTime.now());
        WiFiThermostat thermostat1 = thermostat;

        assertEquals(thermostat.getId(), thermostat1.getId(), "Same objects must have same id");
        assertSame(thermostat.getId(), thermostat1.getId(), "Same objects must have same id");
    }

    @Test
    void testGetIdNotNull() {
        WiFiThermostat thermostat = new WiFiThermostat("Thermostat", 1.0, LocalDateTime.now());

        assertNotNull(thermostat.getId(), "Id can't be null");
    }

    @Test
    void testGetNameNotNull() {
        WiFiThermostat thermostat = new WiFiThermostat("Thermostat", 1.0, LocalDateTime.now());
        assertNotNull(thermostat.getName(), "Name can't be null");
    }

    @Test
    void testGetTypeNotNull() {
        WiFiThermostat thermostat = new WiFiThermostat("Thermostat", 1.0, LocalDateTime.now());
        assertNotNull(thermostat.getType(), "Type can't be null");
    }

    @Test
    void testGetTypeCorrect() {
        WiFiThermostat thermostat = new WiFiThermostat("Thermostat", 1.0, LocalDateTime.now());
        assertEquals(DeviceType.THERMOSTAT, thermostat.getType(),
                "Type must be TMST. Your type is " + thermostat.getType());
    }
}
