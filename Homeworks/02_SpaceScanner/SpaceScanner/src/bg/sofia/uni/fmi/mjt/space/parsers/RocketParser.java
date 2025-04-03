package bg.sofia.uni.fmi.mjt.space.parsers;

import bg.sofia.uni.fmi.mjt.space.exception.ExceptionMessages;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Optional;

public class RocketParser {
    private RocketParser() {
        //don't want to make instances of this class
    }

    public static Set<Rocket> parseRocketsFrom(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException(ExceptionMessages.READER_NULL);
        }

        Set<Rocket> result = new LinkedHashSet<>();

        try (BufferedReader fileReader = new BufferedReader(reader)) {
            String columnNames = fileReader.readLine();
            String line = fileReader.readLine();

            while (line != null) {
                result.add(parseRocket(line));
                line = fileReader.readLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(ExceptionMessages.PROBLEM_WITH_FILE_PARSING, e.getCause());
        }

        return result;
    }

    public static Rocket parseRocket(String row) {
        validateText(row, ExceptionMessages.ROW_NULL_OR_EMPTY);

        String[] values = row.split(RegularExpressions.MATCH_COMMA);

        String id = values[RocketParserIndexes.ID];
        String name = values[RocketParserIndexes.NAME];
        Optional<String> wiki = Optional.empty();
        Optional<Double> height = Optional.empty();

        if (values.length > RocketParserIndexes.WIKI) {
            wiki = parseWiki(values[RocketParserIndexes.WIKI]);
        }
        if (values.length > RocketParserIndexes.HEIGHT) {
            height = parseHeight(values[RocketParserIndexes.HEIGHT]);
        }

        validateText(id, ExceptionMessages.ID_NULL_OR_EMPTY);
        validateText(name, ExceptionMessages.NAME_NULL_OR_EMPTY);

        return new Rocket(id, name, wiki, height);
    }

    private static Optional<String> parseWiki(String text) {
        if (text == null || text.isBlank()) {
            return Optional.empty();
        }

        return Optional.of(text);
    }

    private static Optional<Double> parseHeight(String text) { //23.0 m
        if (text == null || text.isBlank()) {
            return Optional.empty();
        }

        String number = text
                .trim()
                .split(RegularExpressions.MATCH_SPACE)[0];

        return Optional.of(Double.parseDouble(number));
    }

    private static void validateText(String text, String exceptionMessage) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
}
