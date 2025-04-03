package bg.sofia.uni.fmi.mjt.football;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import java.util.Set;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FootballPlayerAnalyzerTest {
    private static StringReader input;
    private static FootballPlayerAnalyzer analyzer;

    private static StringReader emptyInput;
    private static FootballPlayerAnalyzer emptyAnalyzer;

    @BeforeAll
    static void openResources() {
        input = new StringReader("name;full_name;birth_date;age;height_cm;weight_kgs;positions;nationality;overall_rating;potential;value_euro;wage_euro;preferred_foot\n" +
                "L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left\n" +
                "C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right\n" +
                "B. Saka;Bukayo Saka;9/5/2001;17;152.4;69.9;LW,RW;England;92;86;1200000;9000;Left\n" +
                "P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right\n" +
                "K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right");

        analyzer = new FootballPlayerAnalyzer(input);

        emptyInput = new StringReader("   ");
        emptyAnalyzer = new FootballPlayerAnalyzer(emptyInput);
    }

    @AfterAll
    static void closeResources() {
        input.close();
        emptyInput.close();
    }

    @Test
    void tesGetAllNationalitiesIsCorrect() {
        Set<String> expected = Set.of("Denmark", "France", "England", "Argentina");
        Set<String> actual = analyzer.getAllNationalities();

        assertIterableEquals(expected, actual,
                "Expected : " + expected + " but it was : " + actual.toString());
    }

    @Test
    void tesGetAllNationalitiesIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class,
                () -> analyzer.getAllNationalities().add("Ivan"),
                "UnsupportedOperationException was expected but wasn't thrown");
    }

    @Test
    void testGetHighestPaidPlayerByNationalityWhenNationalityIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> analyzer.getHighestPaidPlayerByNationality(null),
                "IllegalArgumentException was expected but not thrown");
    }

    @Test
    void testGetHighestPaidPlayerByNationalityWhenNationalityDoesNotExist() {
        assertThrows(NoSuchElementException.class,
                () -> analyzer.getHighestPaidPlayerByNationality("China"),
                "NoSuchElementException was expected but not thrown");
    }

    @Test
    void testGetHighestPaidPlayerByNationalityIsCorrect() {
        Player expected = Player.of("B. Saka;Bukayo Saka;9/5/2001;" +
                "17;152.4;69.9;LW,RW;England;92;86;1200000;9000;Left");
        Player actual = analyzer.getHighestPaidPlayerByNationality("England");

        assertEquals(expected, actual ,
                "Expected " + expected + " but it was " + actual);
    }

    @Test
    void testGroupByPositionIsCorrect() {
        Player Messi = Player.of("L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left");
        Player Eriksen = Player.of("C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right");
        Player Saka = Player.of("B. Saka;Bukayo Saka;9/5/2001;17;152.4;69.9;LW,RW;England;92;86;1200000;9000;Left");
        Player Pogba = Player.of("P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right");
        Player Mbappe = Player.of("K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right");

        Map<Position, Set<Player>> expected = new LinkedHashMap<>();
        expected.put(Position.CF, Set.of(Messi));
        expected.put(Position.RW, Set.of(Messi, Saka, Mbappe));
        expected.put(Position.ST, Set.of(Messi, Mbappe));
        expected.put(Position.CAM, Set.of(Eriksen, Pogba));
        expected.put(Position.RM, Set.of(Eriksen, Mbappe));
        expected.put(Position.CM, Set.of(Eriksen, Pogba));
        expected.put(Position.LW, Set.of(Saka));

        Map<Position, Set<Player>> actual = analyzer.groupByPosition();

        assertIterableEquals(expected.entrySet(), actual.entrySet(),
                "Expected : " + expected + " but it was : " + actual);
    }

    @Test
    void testGroupByPositionWithNoMatches() {
        Map<Position, Set<Player>> expected = Map.of();
        Map<Position, Set<Player>> actual = emptyAnalyzer.groupByPosition();

        assertIterableEquals(expected.entrySet(), actual.entrySet(),
                "Expected : " + expected + " but it was : " + actual);
    }

    @Test
    void testGetTopProspectPlayerForPositionInBudgetWithNullPosition() {
        assertThrows(IllegalArgumentException.class,
                () -> analyzer.getTopProspectPlayerForPositionInBudget(null, 10L),
                "IllegalArgumentException was expected but not thrown");
    }

    @Test
    void testGetTopProspectPlayerForPositionInBudgetWithNegativeBudget() {
        assertThrows(IllegalArgumentException.class,
                () -> analyzer.getTopProspectPlayerForPositionInBudget(Position.CDM, -10L),
                "IllegalArgumentException was expected but not thrown");
    }

    @Test
    void testGetTopProspectPlayerForPositionInBudgetWithZeroBudget() {
        assertTrue(analyzer.getTopProspectPlayerForPositionInBudget(Position.CDM, 0).isEmpty(),
                "Expected result to be empty but it wasn't");
    }

    @Test
    void testGetTopProspectPlayerForPositionInBudgetIsCorrect() {
        Player expected = Player.of("B. Saka;Bukayo Saka;9/5/2001;17;152.4;69.9;LW,RW;England;92;86;1200000;9000;Left");
        Optional<Player> actual = analyzer.getTopProspectPlayerForPositionInBudget(Position.RW, 20000000L);

        assertEquals(expected, actual.get(),
                "Expected " + expected + " but it was : " + actual.get());
    }

    @Test
    void testGetSimilarPlayersWithNullPlayer() {
        assertThrows(IllegalArgumentException.class,
                () -> analyzer.getSimilarPlayers(null),
                "IllegalArgumentException was expected but not thrown");
    }

    @Test
    void testGetSimilarPlayersIsCorrectWithSameFoot() {
        Player Messi = Player.of("L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left");
        Player Saka = Player.of("B. Saka;Bukayo Saka;9/5/2001;17;152.4;69.9;LW,RW;England;92;86;1200000;9000;Left");

        // Messi <-> Saka

        Set<Player> actual = analyzer.getSimilarPlayers(Messi);
        assertIterableEquals(Set.of(Saka), analyzer.getSimilarPlayers(Messi),
                "Expected : " + Messi + " but it was : " + actual);

        actual = analyzer.getSimilarPlayers(Saka);
        assertIterableEquals(Set.of(Messi), analyzer.getSimilarPlayers(Saka),
                "Expected : " + Saka + " but it was : " + actual);
    }

    @Test
    void testGetSimilarPlayersIsCorrect() {
        Player Eriksen = Player.of("C. Eriksen;Christian  Dannemann Eriksen;2/14/1992;27;154.94;76.2;CAM,RM,CM;Denmark;88;89;69500000;205000;Right");
        Player Pogba = Player.of("P. Pogba;Paul Pogba;3/15/1993;25;190.5;83.9;CM,CAM;France;88;91;73000000;255000;Right");
        Player Mbappe = Player.of("K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right");

        // Eriksen -> expect {Pogba, Mbappe}
        Set<Player> expected = Set.of(Pogba, Mbappe);
        Set<Player> actual = analyzer.getSimilarPlayers(Eriksen);
        assertTrue(expected.containsAll(actual) && expected.size() == actual.size(),
                "Expected : " + expected + " but it was : " + actual);

        // Mbappe -> expect {Eriksen}
        expected = Set.of(Eriksen);
        actual = analyzer.getSimilarPlayers(Mbappe);
        assertIterableEquals(expected, actual,
                "Expected : " + expected + " but it was : " + actual);

        // Pogba -> expect {Eriksen}
        actual = analyzer.getSimilarPlayers(Pogba);
        assertIterableEquals(expected, actual,
                "Expected : " + expected + " but it was : " + actual);
    }

    @Test
    void testGetPlayersByFullNameKeywordWithNullKeyword() {
        assertThrows(IllegalArgumentException.class,
                () -> analyzer.getPlayersByFullNameKeyword(null),
                "IllegalArgumentException was expected but not thrown");
    }

    @Test
    void testGetPlayersByFullNameKeywordWithEmptyKeyword() {
        Set<Player> result = analyzer.getPlayersByFullNameKeyword("");

        assertFalse(result.isEmpty(),
                "Wasn't expected empty collection but it was");
        assertTrue(result.size() == 5,
                "Expected size 5 but it was " + result.size());
    }

    @Test
    void testGetPlayersByFullNameKeywordIsCorrect() {
        Player Messi = Player.of("L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left");
        Player Mbappe = Player.of("K. Mbappé;Kylian Mbappé;12/20/1998;20;152.4;73;RW,ST,RM;France;88;95;81000000;100000;Right");

        Set<Player> expected = Set.of(Messi, Mbappe);
        Set<Player> actual = analyzer.getPlayersByFullNameKeyword(" M");

        assertEquals(expected, actual,
                "Expected : " + expected + " but it was : " + actual);
    }

    @Test
    void testGetPlayersByFullNameKeywordIsCaseSensitive() {
        Player expected = Player.of("L. Messi;Lionel Andrés Messi Cuccittini;6/24/1987;31;170.18;72.1;CF,RW,ST;Argentina;94;94;110500000;565000;Left");

        assertEquals(Set.of(expected), analyzer.getPlayersByFullNameKeyword("Messi"),
                "Must have case sensitive match");
        assertEquals(Set.of(), analyzer.getPlayersByFullNameKeyword("messi"),
                "Must have case sensitive match");
    }

}
