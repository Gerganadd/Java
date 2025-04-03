package bg.sofia.uni.fmi.mjt.itinerary;

public record City(String name, Location location) {
    private static final int METERS_TO_KILOMETERS = 1000;

    public static long getDistanceInMetres(City start, City end) {
        long dx = Math.abs(start.location.x() - end.location.x());
        long dy = Math.abs(start.location.y() - end.location.y());

        return dx + dy;
    }

    public static double getDistanceInKilometres(City start, City end) {
        return (double) getDistanceInMetres(start, end) / METERS_TO_KILOMETERS;
    }
}
