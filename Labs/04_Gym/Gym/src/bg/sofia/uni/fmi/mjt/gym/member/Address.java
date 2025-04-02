package bg.sofia.uni.fmi.mjt.gym.member;

public record Address(double longitude, double latitude) {
    public double getDistanceTo(Address other) {
        double diffX = Math.abs(longitude - other.longitude());
        double diffY = Math.abs(latitude - other.latitude());

        return Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffY, 2)); // to-do round it
    }
}
