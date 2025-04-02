package bg.sofia.uni.fmi.mjt.intelligenthome.device;

import java.time.Duration;
import java.time.LocalDateTime;

public abstract class IoTDeviceBase implements IoTDevice {
    protected static int uniqueNumberDevice = 0;
    protected String name;
    protected double powerConsumption;
    protected LocalDateTime installationDateTime;
    protected LocalDateTime registration;

    public IoTDeviceBase(String name, double powerConsumption, LocalDateTime installationDateTime) {
        setName(name);
        setPowerConsumption(powerConsumption);
        setInstallationDateTime(installationDateTime);
    }

    @Override
    public abstract String getId();

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPowerConsumption() {
        return powerConsumption;
    }

    @Override
    public LocalDateTime getInstallationDateTime() {
        return installationDateTime;
    }

    @Override
    public abstract DeviceType getType();

    @Override
    public long getRegistration() {
        return Duration.between(registration, LocalDateTime.now()).toHours();
    }

    @Override
    public void setRegistration(LocalDateTime registration) {
        this.registration = registration;
    }

    @Override
    public long getPowerConsumptionKWh() {
        long duration = Math.abs(Duration.between(getInstallationDateTime(), LocalDateTime.now()).toHours());
        return (long) (duration * powerConsumption);
    }

    private void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name can't be null or empty");
        }

        this.name = name;
    }

    private void setPowerConsumption(double powerConsumption) {
        if (powerConsumption < 0.0) {
            throw new IllegalArgumentException("Power consumption can't be negative");
        }

        this.powerConsumption = powerConsumption;
    }

    private void setInstallationDateTime(LocalDateTime installationDateTime) {
        if (installationDateTime == null) {
            throw new IllegalArgumentException("Installation date time can't be null");
        }

        this.installationDateTime = installationDateTime;
    }
}
