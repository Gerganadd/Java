package bg.sofia.uni.fmi.mjt.space.parsers;

import bg.sofia.uni.fmi.mjt.space.exception.ExceptionMessages;
import bg.sofia.uni.fmi.mjt.space.mission.Detail;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import java.time.LocalDate;
import java.time.Month;

import java.util.List;
import java.util.Arrays;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Optional;

public class MissionParser {
    private MissionParser() {
        //don't want to make instances of this class
    }

    public static Set<Mission> parseMissionsFrom(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException(ExceptionMessages.READER_NULL);
        }

        Set<Mission> result = new LinkedHashSet<>();

        try (BufferedReader fileReader = new BufferedReader(reader)) {
            String columnNames = fileReader.readLine();
            String line = fileReader.readLine();

            while (line != null) {
                result.add(parseMission(line));
                line = fileReader.readLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(ExceptionMessages.PROBLEM_WITH_FILE_PARSING, e.getCause());
        }

        return result;
    }

    public static Mission parseMission(String row) {
        validateText(row, ExceptionMessages.ROW_NULL_OR_EMPTY);

        List<String> values = Arrays
                .stream(row.splitWithDelimiters(RegularExpressions.MATCH_ALL_COMPONENTS, 9)) // to-do 9
                .filter(x -> !x.equals(","))
                .filter(x -> !x.isBlank())
                .toList();

        if (values.size() <= MissionParserIndexes.MISSION_STATUS) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_MISSION_FORMAT);
        }

        String id = formatId(values.get(MissionParserIndexes.ID));
        String companyName = formatCompanyName(values.get(MissionParserIndexes.COMPANY_NAME));
        String location = formatLocation(values.get(MissionParserIndexes.LOCATION));
        LocalDate date = parseDate(values.get(MissionParserIndexes.DATE));
        Detail detail = parseDetail(values.get(MissionParserIndexes.DETAIL));
        RocketStatus rocketStatus = parseRocketStatus(values.get(MissionParserIndexes.ROCKET_STATUS));
        Optional<Double> cost = parseCost(values.get(MissionParserIndexes.COST));
        MissionStatus missionStatus = parseMissionStatus(values.get(MissionParserIndexes.MISSION_STATUS));

        return new Mission(id, companyName, location, date, detail, rocketStatus, cost, missionStatus);
    }

    private static String formatId(String text) {
        validateText(text, ExceptionMessages.ID_NULL_OR_EMPTY);
        return removeSurroundingQuotationMarks(text);
    }

    private static String formatCompanyName(String text) {
        validateText(text, ExceptionMessages.NAME_NULL_OR_EMPTY);
        return removeSurroundingQuotationMarks(text);
    }

    private static String formatLocation(String text) {
        validateText(text, ExceptionMessages.LOCATION_NULL_OR_EMPTY);
        return removeSurroundingQuotationMarks(text);
    }

    private static LocalDate parseDate(String text) { //"Fri Aug 07, 2020"
        validateText(text, ExceptionMessages.DATE_NULL_OR_EMPTY);
        text = removeSurroundingQuotationMarks(text);

        String[] values = text.split(RegularExpressions.MATCH_SPACE_OR_COMMA_SPACE);

        if (values.length <= MissionParserIndexes.YEAR) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_DATE_FORMAT);
        }

        int day = Integer.parseInt(values[MissionParserIndexes.DAY]);
        Month month = parseMonth(values[MissionParserIndexes.MONTH]);
        int year = Integer.parseInt(values[MissionParserIndexes.YEAR]);

        return LocalDate.of(year, month, day);
    }

    private static Month parseMonth(String text) {
        validateText(text, ExceptionMessages.MONTH_NULL_OR_EMPTY);

        return switch (text) {
            case "Jan" -> Month.JANUARY;
            case "Feb" -> Month.FEBRUARY;
            case "Mar" -> Month.MARCH;
            case "Apr" -> Month.APRIL;
            case "May" -> Month.MAY;
            case "Jun" -> Month.JUNE;
            case "Jul" -> Month.JULY;
            case "Aug" -> Month.AUGUST;
            case "Sep" -> Month.SEPTEMBER;
            case "Oct" -> Month.OCTOBER;
            case "Nov" -> Month.NOVEMBER;
            case "Dec" -> Month.DECEMBER;

            default -> throw new IllegalArgumentException(ExceptionMessages.UNKNOWN_MONTH);
        };
    }

    private static Detail parseDetail(String text) {
        validateText(text, ExceptionMessages.DETAIL_NULL_OR_EMPTY);
        text = removeSurroundingQuotationMarks(text);

        String[] values = text.split(RegularExpressions.MATCH_VERTICAL_BAR);

        if (values.length <= MissionParserIndexes.ROCKET_PAYLOAD) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_DETAIL_FORMAT);
        }

        String rocketName = values[MissionParserIndexes.ROCKET_NAME];
        String payload = values[MissionParserIndexes.ROCKET_PAYLOAD];

        validateText(rocketName, ExceptionMessages.NAME_NULL_OR_EMPTY);
        validateText(payload, ExceptionMessages.PAYLOAD_NULL_OR_EMPTY);

        return new Detail(rocketName, payload);
    }

    private static RocketStatus parseRocketStatus(String text) {
        validateText(text, ExceptionMessages.ROCKET_STATUS_NULL_OR_EMPTY);
        text = removeSurroundingQuotationMarks(text);

        return switch (text) {
            case "StatusActive" -> RocketStatus.STATUS_ACTIVE;
            case "StatusRetired" -> RocketStatus.STATUS_RETIRED;

            default -> throw new IllegalArgumentException(ExceptionMessages.UNKNOWN_ROCKET_STATUS);
        };
    }

    private static Optional<Double> parseCost(String text) {
        if (text == null || text.isBlank()) {
            return Optional.empty();
        }

        text = text.replaceAll(RegularExpressions.MATCH_COMMA, "");
        text = removeSurroundingQuotationMarks(text);

        if (text.isBlank()) {
            return Optional.empty();
        }

        return Optional.of(Double.parseDouble(text));
    }

    private static MissionStatus parseMissionStatus(String text) {
        validateText(text, ExceptionMessages.MISSION_STATUS_NULL_OR_EMPTY);
        text = removeSurroundingQuotationMarks(text);

        return switch (text) {
            case "Success" -> MissionStatus.SUCCESS;
            case "Failure" -> MissionStatus.FAILURE;
            case "Partial Failure" -> MissionStatus.PARTIAL_FAILURE;
            case "Prelaunch Failure" -> MissionStatus.PRELAUNCH_FAILURE;

            default -> throw new IllegalArgumentException(ExceptionMessages.UNKNOWN_MISSION_STATUS);
        };
    }

    private static void validateText(String text, String exceptionMessage) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(exceptionMessage);
        }

        String formattedText = text.replaceAll(RegularExpressions.MATCH_COMMA, "");
        if (formattedText.isBlank()) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    private static String removeSurroundingQuotationMarks(String text) {
        return text.replaceAll(RegularExpressions.MATCH_QUOTATION_MARK, "");
    }
}
