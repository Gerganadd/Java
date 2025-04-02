package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class IoTDeviceBaseTest {
    @Test
    void testConstructWithEmptyName() {
        assertThrows(IllegalArgumentException.class,
                () -> new AmazonAlexa(" ", 1.0, LocalDateTime.now()),
                "IllegalArgumentException was expected but was not throw");
    }

    @Test
    void testConstructWithNullName() {
        assertThrows(IllegalArgumentException.class,
                () -> new AmazonAlexa(null, 1.0, LocalDateTime.now()),
                "IllegalArgumentException was expected but was not throw");
    }

    @Test
    void testConstructWithNegativePowerConsumption() {
        assertThrows(IllegalArgumentException.class,
                () -> new AmazonAlexa("Alexa", -1, LocalDateTime.now()),
                "IllegalArgumentException was expected but was not throw");
    }

    @Test
    void testConstructWithNullInstallationDateTime() {
        assertThrows(IllegalArgumentException.class,
                () -> new AmazonAlexa("Alexa", 1.3, null),
                "IllegalArgumentException was expected but was not throw");
    }

}
