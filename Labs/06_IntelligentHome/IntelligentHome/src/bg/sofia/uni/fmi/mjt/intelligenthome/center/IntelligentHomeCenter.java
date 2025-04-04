package bg.sofia.uni.fmi.mjt.intelligenthome.center;

import bg.sofia.uni.fmi.mjt.intelligenthome.device.DeviceType;
import bg.sofia.uni.fmi.mjt.intelligenthome.device.IoTDevice;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.comparator.KWhComparator;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.comparator.RegistrationComparator;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceAlreadyRegisteredException;
import bg.sofia.uni.fmi.mjt.intelligenthome.center.exceptions.DeviceNotFoundException;
import bg.sofia.uni.fmi.mjt.intelligenthome.storage.DeviceStorage;

import java.time.LocalDateTime;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class IntelligentHomeCenter {
    DeviceStorage storage;

    public IntelligentHomeCenter(DeviceStorage storage) {
        this.storage = storage;
    }

    /**
     * Adds a @device to the IntelligentHomeCenter.
     *
     * @throws IllegalArgumentException         in case @device is null.
     * @throws DeviceAlreadyRegisteredException in case the @device is already
     *                                          registered.
     */
    public void register(IoTDevice device) throws DeviceAlreadyRegisteredException {
        if (device == null) {
            throw new IllegalArgumentException("device cannot be null");
        }

        if (storage.exists(device.getId())) {
            throw new DeviceAlreadyRegisteredException("Device with id : " + device.getId() + " already exists");
        }

        device.setRegistration(LocalDateTime.now());
        storage.store(device.getId(), device);
    }

    /**
     * Removes the @device from the IntelligentHomeCenter.
     *
     * @throws IllegalArgumentException in case null is passed.
     * @throws DeviceNotFoundException  in case the @device is not found.
     */
    public void unregister(IoTDevice device) throws DeviceNotFoundException {
        if (device == null) {
            throw new IllegalArgumentException("device cannot be null");
        }

        if (!storage.exists(device.getId())) {
            throw new DeviceNotFoundException("device doesn't exist");
        }

        storage.delete(device.getId());
    }

    /**
     * Returns a IoTDevice with an ID @id if found.
     *
     * @throws IllegalArgumentException in case @id is null or empty.
     * @throws DeviceNotFoundException  in case device with ID @id is not found.
     */
    public IoTDevice getDeviceById(String id) throws DeviceNotFoundException {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("device cannot be null or empty");
        }
        if (!storage.exists(id)) {
            throw new DeviceNotFoundException("device with id : " + id + " can't be found");
        }

        return storage.get(id);
    }

    /**
     * Returns the total number of devices with type @type registered in
     * SmartCityHub.
     *
     * @throws IllegalArgumentException in case @type is null.
     */
    public int getDeviceQuantityPerType(DeviceType type) {
        if (type == null) {
            throw new IllegalArgumentException("Device type can't be null");
        }

        int quantity = 0;

        for (IoTDevice value : storage.listAll()) {
            if (value.getType().getShortName().equals(type.getShortName())) {
                quantity++;
            }
        }

        return quantity;
    }

    /**
     * Returns a collection of IDs of the top @n devices which consumed
     * the most power from the time of their installation until now.
     *
     * The total power consumption of a device is calculated by the hours elapsed
     * between the two LocalDateTime-s: the installation time and the current time (now)
     * multiplied by the stated nominal hourly power consumption of the device.
     *
     * If @n exceeds the total number of devices, return all devices available sorted by the given criterion.
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public Collection<String> getTopNDevicesByPowerConsumption(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n can't be < 0");
        }

        List<IoTDevice> list = new ArrayList<>(storage.listAll());

        KWhComparator compareKWh = new KWhComparator();
        Collections.sort(list, compareKWh);

        if (n >= list.size()) {
            n = list.size();
        }

        List<String> arrList = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            arrList.add(list.get(i).getId());
        }

        return arrList;
    }

    /**
     * Returns a collection of the first @n registered devices, i.e the first @n that were added
     * in the IntelligentHomeCenter (registration != installation).
     *
     * If @n exceeds the total number of devices, return all devices available sorted by the given criterion.
     *
     * @throws IllegalArgumentException in case @n is a negative number.
     */
    public Collection<IoTDevice> getFirstNDevicesByRegistration(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n can't be < 0");
        }

        List<IoTDevice> list = new ArrayList<>();

        for (IoTDevice value : storage.listAll()) {
            list.add(value);
        }

        RegistrationComparator compareReg = new RegistrationComparator();
        Collections.sort(list, compareReg);

        List<IoTDevice> arrList = new ArrayList<>();

        if (n >= list.size()) {
            n = list.size();
        }

        for (int i = 0; i < n; i++) {
            arrList.add(list.get(i));
        }

        return arrList;
    }
}
