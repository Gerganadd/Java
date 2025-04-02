package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AmazonAlexaTest {
    @Test
    void testGetIdIsCorrect() {
        AmazonAlexa alexa = new AmazonAlexa("Alexa", 1.0, LocalDateTime.now());

        assertTrue(alexa.getId().startsWith("SPKR-Alexa-"),
                "Id doesn't match on SPKR-<device name>-<unique number per device type>");

        int uniqueNumberStartIndex = alexa.getId().lastIndexOf('-');
        String uniqueNumberStr = alexa.getId().substring(uniqueNumberStartIndex);
        int uniqueNumber = Integer.parseInt(uniqueNumberStr);
    }

    @Test
    void testGetIdOnDifferentObjects() {
        AmazonAlexa alexa = new AmazonAlexa("Alexa", 1.0, LocalDateTime.now());
        AmazonAlexa alexa1 = new AmazonAlexa("Alexa", 1.3, LocalDateTime.now());

        assertNotEquals(alexa.getId(), alexa1.getId(), "Different objects must have different id");
        assertNotSame(alexa.getId(), alexa1.getId(), "Different objects must have different id");
    }

    @Test
    void testGetIdOnSameObjects() {
        AmazonAlexa alexa = new AmazonAlexa("Alexa", 1.0, LocalDateTime.now());
        AmazonAlexa alexa1 = alexa;

        assertEquals(alexa.getId(), alexa1.getId(), "Same objects must have same id");
        assertSame(alexa.getId(), alexa1.getId(), "Same objects must have same id");
    }

    @Test
    void testGetIdNotNull() {
        AmazonAlexa alexa = new AmazonAlexa("Alexa", 1.0, LocalDateTime.now());
        assertNotNull(alexa.getId(), "Id can't be null");
    }

    @Test
    void testGetNameNotNull() {
        AmazonAlexa alexa = new AmazonAlexa("Alexa", 1.0, LocalDateTime.now());
        assertNotNull(alexa.getName(), "Name can't be null");
    }

    @Test
    void testGetTypeNotNull() {
        AmazonAlexa alexa = new AmazonAlexa("Alexa", 1.0, LocalDateTime.now());
        assertNotNull(alexa.getType(), "Type can't be null");
    }

    @Test
    void testGetTypeCorrect() {
        AmazonAlexa alexa = new AmazonAlexa("Alexa", 1.0, LocalDateTime.now());
        assertEquals(DeviceType.SMART_SPEAKER, alexa.getType(), "Type must be SPKR. Your type is " + alexa.getType());
    }
}
