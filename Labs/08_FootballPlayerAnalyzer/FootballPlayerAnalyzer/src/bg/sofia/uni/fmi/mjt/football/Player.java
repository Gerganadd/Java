package bg.sofia.uni.fmi.mjt.football;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public record Player(String name, String fullName, LocalDate birthDate,
                     int age, double heightCm, double weightKg,
                     List<Position> positions, String nationality, int overallRating,
                     int potential, long valueEuro, long wageEuro, Foot preferredFoot) {

    public static Player of(String line) {
        if (line == null || line.isEmpty()) {
            throw new IllegalArgumentException("Line can't be null or empty");
        }

        String[] elements = line.split(";");
        int index = 0;

        String name = elements[index++];
        String fullName = elements[index++];
        LocalDate birthDate = parseDate(elements[index++]);
        int age = Integer.parseInt(elements[index++]);
        double heightCm = Double.parseDouble(elements[index++]);
        double weightKg = Double.parseDouble(elements[index++]);
        List<Position> positions = parsePositions(elements[index++]);
        String nationality = elements[index++];
        int overallRating = Integer.parseInt(elements[index++]);
        int potential = Integer.parseInt(elements[index++]);
        long valueEuro = Long.parseLong(elements[index++]);
        long wageEuro = Long.parseLong(elements[index++]);
        Foot preferredFoot = parseFoot(elements[index++]);

        return new Player(name, fullName, birthDate, age, heightCm,
                weightKg, positions, nationality, overallRating,
                potential, valueEuro, wageEuro, preferredFoot);
    }

    private static LocalDate parseDate(String date) {
        // format - 6/24/1987
        String[] info = date.split("/");

        int year = Integer.parseInt(info[2]);
        int month = Integer.parseInt(info[0]);
        int day = Integer.parseInt(info[1]);

        return LocalDate.of(year, month, day);
    }

    private static List<Position> parsePositions(String positions) {
        List<Position> result = new ArrayList<>();

        // format - CM,CAM
        String[] info = positions.split(",");

        for (String position : info) {
            result.add(parsePostion(position));
        }

        return result;
    }

    private static Position parsePostion(String position) {
        return switch (position) {
            case "ST" -> Position.ST;
            case "LM" -> Position.LM;
            case "CF" -> Position.CF;
            case "GK" -> Position.GK;
            case "RW" -> Position.RW;
            case "CM" -> Position.CM;
            case "LW" -> Position.LW;
            case "CDM" -> Position.CDM;
            case "CAM" -> Position.CAM;
            case "RB" -> Position.RB;
            case "LB" -> Position.LB;
            case "LWB" -> Position.LWB;
            case "RM" -> Position.RM;
            case "RWB" -> Position.RWB;
            case "CB" -> Position.CB;

            default -> throw new IllegalArgumentException("Invalid position");
        };
    }

    private static Foot parseFoot(String foot) {
        if (foot.charAt(0) == 'L') {
            return Foot.LEFT;
        }

        return Foot.RIGHT;
    }

}
