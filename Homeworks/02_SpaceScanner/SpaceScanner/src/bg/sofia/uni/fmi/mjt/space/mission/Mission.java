package bg.sofia.uni.fmi.mjt.space.mission;

import java.time.LocalDate;
import java.util.Optional;

import bg.sofia.uni.fmi.mjt.space.rocket.RocketStatus;

public record Mission(String id, String company, String location,
                      LocalDate date, Detail detail, RocketStatus rocketStatus,
                      Optional<Double> cost, MissionStatus missionStatus) {
}
