package bg.sofia.uni.fmi.mjt.intelligenthome.center;

import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.AmazonAlexa;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.RgbBulb;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.WiFiThermostat;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;

import bg.sofia.uni.fmi.mjt.intelligenthome.storage.DeviceStorage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class IntelligentHomeCenterTest {
    @Mock
    private DeviceStorage storage;
    @InjectMocks
    private IntelligentHomeCenter center;

    @Test
    void testRegisterWithNullDevice() {
        assertThrows(IllegalArgumentException.class, () -> center.register(null),
                "IllegalArgumentException was expected but was not throw");

        verify(storage, never()).store(any(), any());
    }

    @Test
    void testRegisterWhenDeviceAlreadyExist() {
        IoTDevice device = new AmazonAlexa("Alexa", 1.0, LocalDateTime.now());

        when(storage.exists(device.getId())).thenReturn(true);

        assertThrows(DeviceAlreadyRegisteredException.class, () -> center.register(device),
                "DeviceAlreadyRegisteredException was expected but was not throw");

        verify(storage, never()).store(any(), any());
    }

    @Test
    void testRegisterWithCorrectData() throws DeviceAlreadyRegisteredException {
        IoTDevice device = new AmazonAlexa("Alexa1", 1.0, LocalDateTime.now());

        when(storage.exists(device.getId())).thenReturn(false);
        center.register(device);

        verify(storage, times(1)).store(device.getId(), device);
    }

    @Test
    void testUnregisterWithNullDevice() {
        assertThrows(IllegalArgumentException.class, () -> center.unregister(null),
                "IllegalArgumentException was expected but was not throw");

        verify(storage, never()).delete(any());
    }

    @Test
    void testUnregisterWithNotExistDevice() {
        IoTDevice device = new AmazonAlexa("Alexa", 1.0, LocalDateTime.now());
        when(storage.exists(device.getId())).thenReturn(false);

        assertThrows(DeviceNotFoundException.class, () -> center.unregister(device),
                "DeviceNotFoundException was expected but was not throw");

        verify(storage, never()).delete(device.getId());
    }

    @Test
    void testUnregisterWithCorrectData() throws DeviceNotFoundException {
        IoTDevice device = new AmazonAlexa("Alexa", 1.0, LocalDateTime.now());
        when(storage.exists(device.getId())).thenReturn(true);

        center.unregister(device);

        verify(storage, times(1)).delete(device.getId());
    }

    @Test
    void testGetDeviceByIdNull() {
        assertThrows(IllegalArgumentException.class, ()->center.getDeviceById(null),
                "IllegalArgumentException was expected but was not throw");

        verify(storage, never()).get(any());
    }

    @Test
    void testGetDeviceByIdWithNotExistID() {
        IoTDevice device = new AmazonAlexa("Alexa", 1.0, LocalDateTime.now());
        when(storage.exists(device.getId())).thenReturn(false);

        assertThrows(DeviceNotFoundException.class, ()-> center.getDeviceById(device.getId()),
                "DeviceNotFoundException was expected but was not throw");

        verify(storage, never()).get(any());
    }

    @Test
    void testGetDeviceByIdWithCorrectData() throws DeviceNotFoundException {
        IoTDevice device = new AmazonAlexa("Alexa", 1.0, LocalDateTime.now());
        when(storage.exists(device.getId())).thenReturn(true);

        center.getDeviceById(device.getId());

        verify(storage, times(1)).get(device.getId());
    }

    @Test
    void testGetDeviceQuantityPerTypeNull() {
        assertThrows(IllegalArgumentException.class, ()-> center.getDeviceQuantityPerType(null),
                "IllegalArgumentException was expected but was not throw");

        verify(storage, never()).listAll();
    }

    @Test
    void testGetDeviceQuantityPerTypeEmptyCollection() {
        when(storage.listAll()).thenReturn(new ArrayList<>());

        assertTrue(center.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER) == 0);
    }

    @Test
    void testGetDeviceQuantityPerTypeIsCorrect() {
        List<IoTDevice> list = new ArrayList<>();
        list.add(new AmazonAlexa("1", 1.1, LocalDateTime.now()));
        list.add(new AmazonAlexa("2", 1.1, LocalDateTime.now()));
        list.add(new AmazonAlexa("3", 1.1, LocalDateTime.now()));
        list.add(new WiFiThermostat("11", 1.1, LocalDateTime.now()));
        list.add(new WiFiThermostat("12", 1.1, LocalDateTime.now()));
        list.add(new RgbBulb("21", 1.1, LocalDateTime.now()));

        when(storage.listAll()).thenReturn(list);

        assertTrue(center.getDeviceQuantityPerType(DeviceType.SMART_SPEAKER) == 3);
        assertTrue(center.getDeviceQuantityPerType(DeviceType.THERMOSTAT) == 2);
        assertTrue(center.getDeviceQuantityPerType(DeviceType.BULB) == 1);
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionNegativeN() {
        assertThrows(IllegalArgumentException.class, ()-> center.getTopNDevicesByPowerConsumption(-17),
                "IllegalArgumentException was expected but was not throw");

        verify(storage, never()).listAll();
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionZeroN() {
        when(storage.listAll()).thenReturn(new ArrayList<>());

        assertTrue(center.getTopNDevicesByPowerConsumption(0).isEmpty());
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionWithBiggerN() {
        LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.of(12,11,10));
        List<IoTDevice> list = new ArrayList<>();
        list.add(new AmazonAlexa("1", 1.1, time));
        list.add(new AmazonAlexa("2", 7.1, time));

        List<String> sortedList = new ArrayList<>();
        sortedList.add(list.get(1).getId());
        sortedList.add(list.get(0).getId());

        when(storage.listAll()).thenReturn(list);

        assertTrue(center.getTopNDevicesByPowerConsumption(123).size() == 2);
        assertIterableEquals(center.getTopNDevicesByPowerConsumption(123), sortedList);
    }

    @Test
    void testGetTopNDevicesByPowerConsumptionIsCorrect() {
        LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.of(12,11,10));
        List<IoTDevice> list = new ArrayList<>();
        list.add(new AmazonAlexa("1", 5.4, time));
        list.add(new RgbBulb("2", 2.2, time));
        list.add(new AmazonAlexa("3", 7, time));
        list.add(new WiFiThermostat("11", 1.1, time));

        List<String> sortedList = new ArrayList<>();
        sortedList.add(list.get(2).getId());
        sortedList.add(list.get(0).getId());

        when(storage.listAll()).thenReturn(list);

        assertTrue(center.getTopNDevicesByPowerConsumption(2).size() == 2);
        assertIterableEquals(center.getTopNDevicesByPowerConsumption(2), sortedList);
    }

    @Test
    void testGetFirstNDevicesByRegistrationNegativeN() {
        assertThrows(IllegalArgumentException.class, ()-> center.getFirstNDevicesByRegistration(-17),
                "IllegalArgumentException was expected but was not throw");

        verify(storage, never()).listAll();
    }

    @Test
    void testGetFirstNDevicesByRegistrationZeroN() {
        when(storage.listAll()).thenReturn(new ArrayList<>());

        assertTrue(center.getFirstNDevicesByRegistration(0).isEmpty());
    }

    @Test
    void testGetFirstNDevicesByRegistrationWithBiggerN() {
        LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        List<IoTDevice> list = new ArrayList<>();
        list.add(new AmazonAlexa("1", 1.1, time));
        list.add(new AmazonAlexa("2", 1.2, time));

        when(storage.exists(any())).thenReturn(false);
        when(storage.listAll()).thenReturn(list);

        assertTrue(center.getFirstNDevicesByRegistration(123).size() == 2);
        assertIterableEquals(center.getFirstNDevicesByRegistration(123), list);
    }

    @Test
    void testGetFirstNDevicesByRegistrationIsCorrect() throws DeviceAlreadyRegisteredException {
        LocalDateTime time = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0));
        List<IoTDevice> list = new ArrayList<>();
        list.add(new AmazonAlexa("1", 1.1, time));
        list.add(new AmazonAlexa("2", 1.2, time));
        list.add(new RgbBulb("2", 2.2, time));
        list.add(new AmazonAlexa("3", 7, time));
        list.add(new WiFiThermostat("11", 1.1, time));

        for (IoTDevice device : list)
        {
            center.register(device);
        }

        List<IoTDevice> sortedList = new ArrayList<>();
        sortedList.add(list.get(0));
        sortedList.add(list.get(1));

        when(storage.listAll()).thenReturn(list);

        assertTrue(center.getFirstNDevicesByRegistration(2).size() == 2);
        assertIterableEquals(center.getFirstNDevicesByRegistration(2), sortedList);
    }

}
