package bg.sofia.uni.fmi.mjt.space;

import bg.sofia.uni.fmi.mjt.space.algorithm.Rijndael;
import bg.sofia.uni.fmi.mjt.space.algorithm.SymmetricBlockCipher;
import bg.sofia.uni.fmi.mjt.space.exception.CipherException;
import bg.sofia.uni.fmi.mjt.space.exception.ExceptionMessages;
import bg.sofia.uni.fmi.mjt.space.exception.TimeFrameMismatchException;

import bg.sofia.uni.fmi.mjt.space.mission.Mission;
import bg.sofia.uni.fmi.mjt.space.mission.MissionStatus;

import bg.sofia.uni.fmi.mjt.space.parsers.MissionParser;
import bg.sofia.uni.fmi.mjt.space.parsers.RegularExpressions;
import bg.sofia.uni.fmi.mjt.space.parsers.RocketParser;

import bg.sofia.uni.fmi.mjt.space.rocket.Rocket;
import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

import javax.crypto.SecretKey;

import java.io.Reader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.nio.charset.StandardCharsets;

import java.time.LocalDate;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.groupingBy;

public class MJTSpaceScanner implements SpaceScannerAPI {
    private static final int RELIABILITY_COEFFICIENT = 2;
    private Set<Mission> missions;
    private Set<Rocket> rockets;
    private SymmetricBlockCipher cipher;

    public MJTSpaceScanner(Reader missionsReader, Reader rocketsReader, SecretKey secretKey) {
        this.missions = MissionParser.parseMissionsFrom(missionsReader);
        this.rockets = RocketParser.parseRocketsFrom(rocketsReader);
        this.cipher = new Rijndael(secretKey);
    }

    @Override
    public Collection<Mission> getAllMissions() {
        return Set.copyOf(missions);
    }

    @Override
    public Collection<Mission> getAllMissions(MissionStatus missionStatus) {
        validateNotNull(missionStatus, ExceptionMessages.MISSION_STATUS_NULL);

        return missions
                .stream()
                .filter(x -> x.missionStatus().equals(missionStatus))
                .collect(toSet());
    }

    @Override
    public String getCompanyWithMostSuccessfulMissions(LocalDate from, LocalDate to) {
        validateDates(from, to);

        return missions
                .stream()
                .filter(x -> x.missionStatus().equals(MissionStatus.SUCCESS))
                .filter(x -> x.date().isAfter(from))
                .filter(x -> x.date().isBefore(to))
                .collect(Collectors.groupingBy(Mission::company, toSet()))
                .entrySet()
                .stream()
                .max(Comparator.comparingInt((x) -> x.getValue().size()))
                .get()
                .getKey();
    }

    @Override
    public Map<String, Collection<Mission>> getMissionsPerCountry() {
        return missions
                .stream()
                .collect(Collectors.groupingBy(
                        x -> getMissionCountryByLocation(x.location()), toCollection(HashSet::new)));
    }

    @Override
    public List<Mission> getTopNLeastExpensiveMissions(int n,
                                                       MissionStatus missionStatus, RocketStatus rocketStatus) {
        validateN(n);
        validateNotNull(missionStatus, ExceptionMessages.MISSION_STATUS_NULL);
        validateNotNull(rocketStatus, ExceptionMessages.ROCKET_STATUS_NULL);

        return missions
                .stream()
                .filter(x -> x.missionStatus().equals(missionStatus))
                .filter(x -> x.rocketStatus().equals(rocketStatus))
                .filter(x -> x.cost().isPresent())
                .sorted(Comparator.comparing(m -> m.cost().get()))
                .limit(n)
                .toList();
    }

    @Override
    public Map<String, String> getMostDesiredLocationForMissionsPerCompany() {
        Map<String, List<Mission>> map = missions
                .stream()
                .collect(Collectors.groupingBy(Mission::company));

        Map<String, String> result = new HashMap<>();

        map.forEach((key, value) -> result.put(key, getTopLocation(value)));

        return result;
    }

    @Override
    public Map<String, String> getLocationWithMostSuccessfulMissionsPerCompany(LocalDate from, LocalDate to) {
        validateDates(from, to);

        Map<String, List<Mission>> map = missions
                .stream()
                .filter(x -> x.date().isAfter(from))
                .filter(x -> x.date().isBefore(to))
                .filter(x -> x.missionStatus().equals(MissionStatus.SUCCESS))
                .collect(Collectors.groupingBy(Mission::company));

        Map<String, String> result = new HashMap<>();

        map.forEach((key, value) -> result.put(key, getTopLocation(value)));

        return result;
    }

    @Override
    public Collection<Rocket> getAllRockets() {
        return Set.copyOf(rockets);
    }

    @Override
    public List<Rocket> getTopNTallestRockets(int n) {
        validateN(n);

        return rockets
                .stream()
                .filter(x -> x.height().isPresent())
                .sorted((r1, r2) -> r2.height().get().compareTo(r1.height().get()))
                .limit(n)
                .toList();
    }

    @Override
    public Map<String, Optional<String>> getWikiPageForRocket() {
        return rockets
                .stream()
                .collect(Collectors.toMap(Rocket::name, Rocket::wiki));
    }

    @Override
    public List<String> getWikiPagesForRocketsUsedInMostExpensiveMissions(int n, MissionStatus missionStatus,
                                                                          RocketStatus rocketStatus) {
        validateN(n);
        validateNotNull(missionStatus, ExceptionMessages.MISSION_STATUS_NULL);
        validateNotNull(rocketStatus, ExceptionMessages.ROCKET_STATUS_NULL);

        return missions
                .stream()
                .filter(x -> x.missionStatus().equals(missionStatus))
                .filter(x -> x.rocketStatus().equals(rocketStatus))
                .filter(x -> x.cost().isPresent())
                .sorted((m1, m2) -> m2.cost().get().compareTo(m1.cost().get()))
                .limit(n)
                .map(x -> getRocketWikiByName(x.detail().rocketName()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public void saveMostReliableRocket(OutputStream outputStream, LocalDate from, LocalDate to)
            throws CipherException {
        validateNotNull(outputStream, ExceptionMessages.STREAM_NULL);
        validateDates(from, to);

        Map<String, Integer> sortedMap = rockets
                .stream()
                .collect(toMap(Rocket::name, this::getReliability))
                .entrySet()
                .stream()
                .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        if (sortedMap.isEmpty()) {
            return;
        }

        String mostReliableRocket = sortedMap.keySet().iterator().next();
        byte[] bytes = mostReliableRocket.getBytes(StandardCharsets.UTF_8);

        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            this.cipher.encrypt(inputStream, outputStream);
        } catch (IOException e) {
            throw new CipherException(ExceptionMessages.PROBLEM_WITH_ENCRYPTING, e);
        }
    }

    private int getReliability(Rocket rocket) {
        int successfulMissions = getMissionsCountOf(rocket.name(), MissionStatus.SUCCESS);
        int failedMissions = getMissionsCountOf(rocket.name(), MissionStatus.FAILURE);
        int partialFailedMissions = getMissionsCountOf(rocket.name(), MissionStatus.PARTIAL_FAILURE);
        int prelaunchFailedMissions = getMissionsCountOf(rocket.name(), MissionStatus.PRELAUNCH_FAILURE);

        int failed = failedMissions + partialFailedMissions + prelaunchFailedMissions;
        int total = successfulMissions + failed;

        if (total == 0) {
            return 0;
        }

        return (RELIABILITY_COEFFICIENT * successfulMissions + failed) / (RELIABILITY_COEFFICIENT * total);
    }

    private int getMissionsCountOf(String rocketName, MissionStatus status) {
        return missions
                .stream()
                .filter(x -> x.detail().rocketName().equals(rocketName))
                .filter(x -> x.missionStatus().equals(status))
                .toList()
                .size();
    }

    private String getTopLocation(Collection<Mission> collection) {
        Map<String, Long> map = collection
                .stream()
                .collect(groupingBy(Mission::location, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return map.entrySet().iterator().next().getKey();
    }

    private Optional<String> getRocketWikiByName(String rocketName) {
        return rockets
                .stream()
                .filter(x -> x.name().equals(rocketName))
                .toList()
                .getFirst()
                .wiki();
    }

    private String getMissionCountryByLocation(String location) {
        String[] values = location.split(RegularExpressions.MATCH_COMMA);
        return values[values.length - 1].trim();
    }

    private void validateNotNull(Object object, String exceptionMessage) {
        if (object == null) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }

    private void validateN(int n) {
        if (n < 1) {
            throw new IllegalArgumentException(ExceptionMessages.INVALID_N);
        }
    }

    private void validateDates(LocalDate start, LocalDate end) {
        validateNotNull(start, ExceptionMessages.DATE_NULL);
        validateNotNull(end, ExceptionMessages.DATE_NULL);

        if (end.isBefore(start)) {
            throw new TimeFrameMismatchException(ExceptionMessages.TIME_MISMATCH);
        }
    }
}
