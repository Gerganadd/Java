package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;
import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;
import bg.sofia.uni.fmi.mjt.space.parsers.RegularExpressions;
import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import java.io.Reader;
import java.io.StringReader;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.security.NoSuchAlgorithmException;

import java.time.LocalDate;
import java.time.Month;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Collection;
import java.util.Optional;
import java.util.NoSuchElementException;

import java.util.stream.Collectors;

import static bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael.ENCRYPTION_ALGORITHM;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MJTSpaceScannerTest {
    private static final int KEY_SIZE_IN_BITS = 128;
    private static SpaceScannerAPI spaceScanner;
    private static SpaceScannerAPI emptySpaceScanner;
    private static Map<String, Mission> missions;
    private static Map<String, Rocket> rockets;

    @BeforeAll
    static void initialize() throws IOException, NoSuchAlgorithmException {
        Reader missionsReader = new StringReader("""
                Unnamed: 0,Company Name,Location,Datum,Detail,Status Rocket," Rocket",Status Mission
                0,SpaceX,"LC-39A, Kennedy Space Center, Florida, USA","Fri Aug 07, 2020",Tsyklon-3 | Starlink V1 L9 & BlackSky,StatusActive,"50.0 ",Success
                1,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Thu Aug 06, 2020",Unha-2 | Gaofen-9 04 & Q-SAT,StatusActive,"29.75 ",Success
                2,SpaceX,"LC-39A, Kennedy Space Center, Florida, USA","Tue Aug 04, 2020",Unha-2 | 150 Meter Hop,StatusActive,"14.7 ",Failure
                3,Roscosmos,"Site 200/39, Baikonur Cosmodrome, Kazakhstan","Thu Jul 30, 2020",Proton-M/Briz-M | Ekspress-80 & Ekspress-103,StatusActive,"65.0 ",Partial Failure
                4,ULA,"SLC-41, Cape Canaveral AFS, Florida, USA","Thu Jul 30, 2020",Tsyklon-3 | Perseverance,StatusActive,"145.0 ",Failure
                5,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Sat Jul 25, 2020",Long March 4B | Ziyuan-3 03,StatusActive,"64.68 ",Prelaunch Failure
                6,CASC,"LC-101, Wenchang Satellite Launch Center, China","Thu Jul 23, 2020",Vanguard | Tianwen-1,StatusActive,,Success
                7,CASC,"Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China","Thu Jul 09, 2020",Tsyklon-4M | Apstar-6D,StatusActive,"29.15 ",Success
                """);
        Reader rocketsReader = new StringReader("""
                "",Name,Wiki,Rocket Height
                0,Tsyklon-3,https://en.wikipedia.org/wiki/Tsyklon-3,39.0 m
                1,Tsyklon-4M,https://en.wikipedia.org/wiki/Cyclone-4M,38.7 m
                2,Unha-2,,28.0 m
                3,Unha-3,https://en.wikipedia.org/wiki/Unha,32.0 m
                4,Vanguard,,
                """);

        emptySpaceScanner = new MJTSpaceScanner(new StringReader("\n"), new StringReader("\n"), null);
        spaceScanner = new MJTSpaceScanner(missionsReader, rocketsReader, generateSecretKey());

        missions = spaceScanner
                .getAllMissions()
                .stream()
                .collect(Collectors.toMap(Mission::id, m -> m));

        rockets = spaceScanner
                .getAllRockets()
                .stream()
                .collect(Collectors.toMap(Rocket::id, r -> r));

        missionsReader.close();
        rocketsReader.close();
    }

    private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
        keyGenerator.init(KEY_SIZE_IN_BITS);

        return keyGenerator.generateKey();
    }

    /*
     *  Tests for the method : getAllMissions()
     */

    @Test
    void testGetAllMissionsWithZeroMissions() {
        assertEquals(0, emptySpaceScanner.getAllMissions().size(),
                "Expected empty collection but it wasn't");
    }

    @Test
    void testGetAllMissionsIsCorrect() {
        Collection<Mission> actual = spaceScanner.getAllMissions();

        List<Mission> expected = new ArrayList<>(missions.values());

        assertEquals(expected.size(), actual.size(),
                "Expected size " + expected.size() + ", but it was " + actual.size());
        assertTrue(actual.containsAll(expected),
                "Expected : " + expected + ", but it was : " + actual);
    }

    /*
     *  Tests for the method : getAllMissions(MissionStatus status)
     */

    @Test
    void testGetAllMissionsWithNullMissionStatus() {
        assertThrows(IllegalArgumentException.class, () -> spaceScanner.getAllMissions(null),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testGetAllMissionsIsCorrectWithMissionStatusSuccess() {
        Collection<Mission> actual = spaceScanner.getAllMissions(MissionStatus.SUCCESS);

        List<Mission> expected = List.of(
                missions.get("0"),
                missions.get("1"),
                missions.get("6"),
                missions.get("7"));

        assertEquals(expected.size(), actual.size(),
                "Expected size " + expected.size() + ", but it was " + actual.size());
        assertTrue(actual.containsAll(expected),
                "Expected : " + expected + ", but it was : " + actual);
    }

    @Test
    void testGetAllMissionsIsCorrectWithMissionStatusFailure() {
        Collection<Mission> actual = spaceScanner.getAllMissions(MissionStatus.FAILURE);

        List<Mission> expected = List.of(
                missions.get("2"),
                missions.get("4"));

        assertEquals(expected.size(), actual.size(),
                "Expected size " + expected.size() + ", but it was " + actual.size());
        assertTrue(actual.containsAll(expected),
                "Expected : " + expected + ", but it was : " + actual);
    }

    @Test
    void testGetAllMissionsIsCorrectWithMissionStatusPartialFailure() {
        Collection<Mission> actual = spaceScanner.getAllMissions(MissionStatus.PARTIAL_FAILURE);

        List<Mission> expected = List.of(
                missions.get("3"));

        assertIterableEquals(expected, actual,
                "Expected : " + expected + ", but it was : " + actual);
    }

    @Test
    void testGetAllMissionsIsCorrectWithMissionStatusPrelaunchFailure() {
        Collection<Mission> actual = spaceScanner.getAllMissions(MissionStatus.PRELAUNCH_FAILURE);

        List<Mission> expected = List.of(
                missions.get("5"));

        assertIterableEquals(expected, actual,
                "Expected : " + expected + ", but it was : " + actual);
    }

    /*
     *  Tests for the method : getCompanyWithMostSuccessfulMissions(LocalDate from, LocalDate to)
     */

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithNullFrom() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getCompanyWithMostSuccessfulMissions(null, LocalDate.now()),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithNullTo() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getCompanyWithMostSuccessfulMissions(LocalDate.now(), null),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithNullFromAndTo() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getCompanyWithMostSuccessfulMissions(null, null),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithToBeforeFrom() {
        LocalDate start = LocalDate.of(2020, Month.AUGUST, 3);
        LocalDate end = LocalDate.of(2019, Month.AUGUST, 3);

        assertThrows(TimeFrameMismatchException.class,
                () -> spaceScanner.getCompanyWithMostSuccessfulMissions(start, end),
                "TimeFrameMismatchException was expected but wasn't thrown");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithZeroMissions() {
        LocalDate start = LocalDate.of(2020, Month.SEPTEMBER, 3);
        LocalDate end = LocalDate.of(2021, Month.SEPTEMBER, 4);

        assertThrows(NoSuchElementException.class,
                () -> emptySpaceScanner.getCompanyWithMostSuccessfulMissions(start, end),
                "NoSuchElementException was expected but wasn't thrown");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithMissionsInThatRange() {
        LocalDate start = LocalDate.of(2021, Month.SEPTEMBER, 3);
        LocalDate end = LocalDate.of(2021, Month.SEPTEMBER, 4);

        assertThrows(NoSuchElementException.class,
                () -> spaceScanner.getCompanyWithMostSuccessfulMissions(start, end),
                "NoSuchElementException was expected but wasn't thrown");
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsIsCorrect() {
        LocalDate start = LocalDate.of(2020, Month.MAY, 1);
        LocalDate end = LocalDate.of(2020, Month.DECEMBER, 1);

        String expected = "CASC";
        String actual = spaceScanner.getCompanyWithMostSuccessfulMissions(start, end);

        assertEquals(expected, actual,
                "expected company " + expected + ", but it was : " + actual);
    }

    @Test
    void testGetCompanyWithMostSuccessfulMissionsWithTwoOptionsIsCorrect() {
        LocalDate start = LocalDate.of(2020, Month.AUGUST, 1);
        LocalDate end = LocalDate.of(2020, Month.AUGUST, 25);

        String expected1 = "CASC";
        String expected2 = "SpaceX";
        String actual = spaceScanner.getCompanyWithMostSuccessfulMissions(start, end);

        assertTrue(expected1.equals(actual) || expected2.equals(actual),
                "expected company CASC or SpaceX, but it was : " + actual);
    }

    /*
     *  Tests for the method : getMissionsPerCountry()
     */

    @Test
    void testGetMissionsPerCountryWithZeroMissions() {
        assertTrue(emptySpaceScanner.getMissionsPerCountry().isEmpty(),
                "Expected empty collection but it wasn't");
    }

    @Test
    void testGetMissionsPerCountryIsCorrect() {
        Map<String, Collection<Mission>> actual = spaceScanner.getMissionsPerCountry();
        Map<String, Collection<Mission>> expected = new HashMap<>();
        for (Mission m : missions.values()) {
            String country = getCountry(m);

            if (!expected.containsKey(country)) {
                expected.put(country, new HashSet<>());
            }

            expected.get(country).add(m);
        }

        assertEquals(expected.size(), actual.size(),
                "Expected size : " + expected.size() + ", but it was : " + actual.size());
        assertTrue(actual.keySet().containsAll(expected.keySet()),
                "Expected keys : " + expected.values() + ", but it was : " + actual.values());
        assertTrue(actual.values().containsAll(expected.values()),
                "Expected values : " + expected.values() + ", but it was : " + actual.values());
    }

    private String getCountry(Mission mission) {
        int countryStartIndex = mission.location().lastIndexOf(',');
        String country = mission.location().substring(countryStartIndex);

        return country.replaceAll(RegularExpressions.MATCH_COMMA, "").trim();
    }

    /*
     *  Tests for the method : getTopNLeastExpensiveMissions(int n,
     *                              MissionStatus missionStatus, RocketStatus rocketStatus)
     */

    @Test
    void testGetTopNLeastExpensiveMissionsWithNegativeN() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getTopNLeastExpensiveMissions(-1,
                        MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithZeroN() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getTopNLeastExpensiveMissions(0,
                        MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithNullMissionStatus() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getTopNLeastExpensiveMissions(1,
                        null, RocketStatus.STATUS_ACTIVE),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithNullRocketStatus() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getTopNLeastExpensiveMissions(1,
                        MissionStatus.SUCCESS, null),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testGetTopNLeastExpensiveMissionsIsEmpty() {
        List<Mission> actual0 = spaceScanner.getTopNLeastExpensiveMissions(3,
                MissionStatus.PARTIAL_FAILURE, RocketStatus.STATUS_RETIRED);

        assertEquals(0, actual0.size(),
                "Expected size to be 0, but it was : " + actual0.size());
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithBiggerNIsCorrect() {
        List<Mission> expected = List.of(
                missions.get("2"),
                missions.get("4"));

        List<Mission> actual = spaceScanner.getTopNLeastExpensiveMissions(3,
                MissionStatus.FAILURE, RocketStatus.STATUS_ACTIVE);

        assertEquals(expected.size(), actual.size(),
                "Expected size to be " + expected.size() + ", but it was : " + actual.size());
        assertTrue(actual.containsAll(expected),
                "Expected : " + expected + ", but it was : " + actual);
    }

    @Test
    void testGetTopNLeastExpensiveMissionsWithSmallerNIsCorrect() {
        List<Mission> expected = List.of(
                missions.get("2"));

        List<Mission> actual = spaceScanner.getTopNLeastExpensiveMissions(1,
                MissionStatus.FAILURE, RocketStatus.STATUS_ACTIVE);

        assertEquals(expected.size(), actual.size(),
                "Expected size to be " + expected.size() + ", but it was : " + actual.size());
        assertTrue(actual.containsAll(expected),
                "Expected : " + expected + ", but it was : " + actual);
    }

    /*
     *  Tests for the method : getMostDesiredLocationForMissionsPerCompany()
     */

    @Test
    void testGetMostDesiredLocationForMissionsPerCompanyWithZeroMissions() {
        assertTrue(emptySpaceScanner.getMostDesiredLocationForMissionsPerCompany().isEmpty(),
                "Expected empty collection but it wasn't");
    }

    @Test
    void testGetMostDesiredLocationForMissionsPerCompanyIsCorrect() {
        Map<String, String> expected = Map.of(
                "SpaceX", "LC-39A, Kennedy Space Center, Florida, USA",
                "CASC", "Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China",
                "Roscosmos", "Site 200/39, Baikonur Cosmodrome, Kazakhstan",
                "ULA", "SLC-41, Cape Canaveral AFS, Florida, USA");

        Map<String, String> actual = spaceScanner.getMostDesiredLocationForMissionsPerCompany();

        assertEquals(expected.size(), actual.size(),
                "Expected size : " + expected.size() + ", but it was : " + actual.size());
        assertTrue(actual.entrySet().containsAll(expected.entrySet()),
                "Expected : " + expected.entrySet() + ", but it was : " + actual.entrySet());
    }

    /*
     *  Tests for the method : getLocationWithMostSuccessfulMissionsPerCompany(LocalDate from, LocalDate to)
     */

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithNullFrom() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(null, LocalDate.now()),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithNullTo() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(LocalDate.now(), null),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithNullFromAndTo() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(null, null),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithToBeforeFrom() {
        LocalDate start = LocalDate.of(2020, Month.AUGUST, 3);
        LocalDate end = LocalDate.of(2019, Month.AUGUST, 3);

        assertThrows(TimeFrameMismatchException.class,
                () -> spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(start, end),
                "TimeFrameMismatchException was expected but wasn't thrown");
    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyWithZeroMissions() {
        LocalDate start = LocalDate.of(2020, Month.AUGUST, 3);
        LocalDate end = LocalDate.of(2021, Month.AUGUST, 3);

        assertTrue(emptySpaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(start, end).isEmpty(),
                "Expected empty collection but it wasn't");

    }

    @Test
    void testGetLocationWithMostSuccessfulMissionsPerCompanyIsCorrect() {
        LocalDate start = LocalDate.of(2019, Month.AUGUST, 3);
        LocalDate end = LocalDate.of(2021, Month.AUGUST, 3);

        Map<String, String> expected = Map.of(
                "SpaceX", "LC-39A, Kennedy Space Center, Florida, USA",
                "CASC", "Site 9401 (SLS-2), Jiuquan Satellite Launch Center, China");

        Map<String, String> actual = spaceScanner.getLocationWithMostSuccessfulMissionsPerCompany(start, end);

        assertEquals(expected.size(), actual.size(),
                "Expected size : " + expected.size() + ", but it was : " + actual.size());
        assertTrue(actual.entrySet().containsAll(expected.entrySet()),
                "Expected : " + expected + ", but it was : " + actual);
    }

    /*
     *  Tests for the method : getAllRockets()
     */

    @Test
    void testGetAllRocketsWithZeroRockets() {
        assertTrue(emptySpaceScanner.getAllRockets().isEmpty(),
                "Expected empty collection but it wasn't");
    }

    @Test
    void testGetAllRocketsIsCorrect() {
        Collection<Rocket> actual = spaceScanner.getAllRockets();

        assertEquals(rockets.values().size(), actual.size(),
                "Expected size : " + rockets.values().size() + ", but it was : " + actual.size());
        assertTrue(actual.containsAll(rockets.values()),
                "Expected : " + rockets.values() + ", but it was : " + actual);
    }

    /*
     *  Tests for the method : getTopNTallestRockets(int n)
     */

    @Test
    void testGetTopNTallestRocketsWithNegativeN() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getTopNTallestRockets(-1),
                "IllegalArgumentException was expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getTopNTallestRockets(-7),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testGetTopNTallestRocketsWithZeroN() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getTopNTallestRockets(0),
                "IllegalArgumentException was expected but wasn't thrown");
    }

    @Test
    void testGetTopNTallestRocketsWithZeroRockets() {
        assertTrue(emptySpaceScanner.getTopNTallestRockets(12).isEmpty(),
                "Expected empty collection but it wasn't");
    }

    @Test
    void testGetTopNTallestRocketsIsCorrect() {
        List<Rocket> expected = List.of(
                rockets.get("0"), // 39.0
                rockets.get("1"), // 38.7
                rockets.get("3")); // 32.0

        Collection<Rocket> actual = spaceScanner.getTopNTallestRockets(3);
        assertIterableEquals(expected, actual,
                "Expected : " + expected + ", but it was : " + actual);
    }

    /*
     *  Tests for the method : getWikiPageForRocket()
     */

    @Test
    void testGetWikiPageForRocketWithZeroRockets() {
        assertTrue(emptySpaceScanner.getWikiPageForRocket().isEmpty(),
                "Expected empty collection but it wasn't");
    }

    @Test
    void testGetWikiPageForRocketIsCorrect() {
        Map<String, Optional<String>> actual = spaceScanner.getWikiPageForRocket();

        Map<String, Optional<String>> expected = new HashMap<>();
        for (Rocket r : rockets.values()) {
            expected.put(r.name(), r.wiki());
        }

        assertEquals(expected.size(), actual.size(),
                "Expected size : " + expected.size() + ", but it was : " + actual.size());


        assertTrue(actual.entrySet().containsAll(expected.entrySet()),
                "Expected : " + expected.entrySet() + ", but it was : " + actual.entrySet());
    }

    /*
     *  Tests for the method : getWikiPagesForRocketsUsedInMostExpensiveMissions(int n,
     *                              MissionStatus missionStatus, RocketStatus rocketStatus)
     */

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithNegativeN() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(-1,
                        MissionStatus.FAILURE, RocketStatus.STATUS_ACTIVE),
                "IllegalArgumentException expected but wasn't thrown");
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(-8,
                        MissionStatus.FAILURE, RocketStatus.STATUS_ACTIVE),
                "IllegalArgumentException expected but wasn't thrown");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithZeroN() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(0,
                        MissionStatus.FAILURE, RocketStatus.STATUS_ACTIVE),
                "IllegalArgumentException expected but wasn't thrown");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithNullMissionStatus() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(10,
                        null, RocketStatus.STATUS_ACTIVE),
                "IllegalArgumentException expected but wasn't thrown");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithNullRocketStatus() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(10,
                        MissionStatus.FAILURE, null),
                "IllegalArgumentException expected but wasn't thrown");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsWithZeroRockets() {
        assertTrue(spaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(2,
                        MissionStatus.FAILURE, RocketStatus.STATUS_RETIRED).isEmpty(),
                "Expected empty collection but it wasn't");
    }

    @Test
    void testGetWikiPagesForRocketsUsedInMostExpensiveMissionsIsCorrect() {
        List<String> expected = List.of(
                "https://en.wikipedia.org/wiki/Tsyklon-3",
                "https://en.wikipedia.org/wiki/Cyclone-4M");

        List<String> actual = spaceScanner.getWikiPagesForRocketsUsedInMostExpensiveMissions(3,
                MissionStatus.SUCCESS, RocketStatus.STATUS_ACTIVE);

        assertEquals(expected.size(), actual.size(),
                "Expected size : " + expected.size() + ", but it was : " + actual.size());
        assertTrue(actual.containsAll(expected),
                "Expected : " + expected + ", but it was : " + actual);
        assertIterableEquals(expected, actual,
                "Expected : " + expected + ", but it was : " + actual);
    }

    /*
     *  Tests for the method : saveMostReliableRocket(OutputStream stream,
     *                               LocalDate from, LocalDate to)
     */

    @Test
    void testSaveMostReliableRocketWithNullOutputStream() {
        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.saveMostReliableRocket(null,
                        LocalDate.now(), LocalDate.now()),
                "IllegalArgumentException expected but wasn't thrown");
    }

    @Test
    void testSaveMostReliableRocketWithNullFrom() throws IOException {
        OutputStream stream = new ByteArrayOutputStream();

        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.saveMostReliableRocket(stream,
                        null, LocalDate.now()),
                "IllegalArgumentException expected but wasn't thrown");

        stream.close();
    }

    @Test
    void testSaveMostReliableRocketWithNullTo() throws IOException {
        OutputStream stream = new ByteArrayOutputStream();

        assertThrows(IllegalArgumentException.class,
                () -> spaceScanner.saveMostReliableRocket(stream,
                        LocalDate.now(), null),
                "IllegalArgumentException expected but wasn't thrown");

        stream.close();
    }

    @Test
    void testSaveMostReliableRocketWithToBeforeFrom() throws IOException {
        LocalDate start = LocalDate.of(2020, Month.APRIL, 12);
        LocalDate end = LocalDate.of(2020, Month.JANUARY, 3);
        OutputStream stream = new ByteArrayOutputStream();

        assertThrows(TimeFrameMismatchException.class,
                () -> spaceScanner.saveMostReliableRocket(stream,
                        start, end),
                "TimeFrameMismatchException expected but wasn't thrown");

        stream.close();
    }

    @Test
    void testSaveMostReliableRocketIsCorrect() throws IOException, CipherException {
        LocalDate start = LocalDate.of(2020, Month.APRIL, 1);
        LocalDate end = LocalDate.of(2020, Month.DECEMBER, 1);
        OutputStream stream = new ByteArrayOutputStream();

        spaceScanner.saveMostReliableRocket(stream, start, end);

        assertFalse(stream.toString().isBlank(),
                "Expected not empty outputStream, but it was");

        stream.close();
    }
}
