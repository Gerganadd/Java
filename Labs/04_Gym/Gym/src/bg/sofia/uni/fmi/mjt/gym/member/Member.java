package bg.sofia.uni.fmi.mjt.gym.member;

import bg.sofia.uni.fmi.mjt.gym.messages.ExceptionMessages;
import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;
import bg.sofia.uni.fmi.mjt.gym.workout.Workout;

import java.time.DayOfWeek;

import java.util.Collections;
import java.util.Collection;
import java.util.SequencedCollection;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Member implements GymMember {
    private String name;
    private String personalIdNumber;
    private Address address;
    private Gender gender;
    private int age;

    private Map<DayOfWeek, Workout> trainingProgram;

    public Member(Address address, String name, int age, String personalIdNumber, Gender gender) {
        setAddress(address);
        setName(name);
        setAge(age);
        setPersonalIdNumber(personalIdNumber);

        this.gender = gender;
        this.trainingProgram = new HashMap<>();
    }

    private void setPersonalIdNumber(String personalIdNumber) {
        this.personalIdNumber = personalIdNumber;
    }

    private void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException(ExceptionMessages.NEGATIVE_AGE);
        }

        this.age = age;
    }

    private void setName(String name) {
        this.name = name;
    }

    private void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Member other)) {
            return false;
        }

        return personalIdNumber.equals(other.personalIdNumber);
    }

    @Override
    public int hashCode() {
        return personalIdNumber.hashCode();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public String getPersonalIdNumber() {
        return personalIdNumber;
    }

    @Override
    public Gender getGender() {
        return gender;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public Map<DayOfWeek, Workout> getTrainingProgram() {
        return Collections.unmodifiableMap(trainingProgram);
    }

    @Override
    public void setWorkout(DayOfWeek day, Workout workout) {
        validateWorkout(workout);
        validateDay(day);

        trainingProgram.put(day, workout);
    }

    @Override
    public Collection<DayOfWeek> getDaysFinishingWith(String exerciseName) {
        validateExerciseName(exerciseName);

        Collection<DayOfWeek> matches = new ArrayList<>();

        for (Map.Entry<DayOfWeek, Workout> entry : trainingProgram.entrySet()) {

            String currentWorkoutName = entry.getValue().getLastWorkout().name();

            if (currentWorkoutName.equals(exerciseName)) {
                matches.add(entry.getKey());
            }
        }

        return Collections.unmodifiableCollection(matches);
    }

    @Override
    public void addExercise(DayOfWeek day, Exercise exercise) {
        validateDay(day);
        validateExercise(exercise);
        validateKey(day);

        trainingProgram.get(day).add(exercise);
    }

    @Override
    public void addExercises(DayOfWeek day, List<Exercise> exercises) {
        validateDay(day);
        validateExercises(exercises);
        validateKey(day);

        trainingProgram.get(day).addAll(exercises);
    }

    private void validateDay(DayOfWeek day) {
        if (day == null) {
            throw new IllegalArgumentException(ExceptionMessages.DAY_OF_WEEK_NULL);
        }
    }

    private void validateExercises(SequencedCollection<Exercise> exercises) {
        if (exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessages.EXERCISES_NULL_OR_EMPTY);
        }
    }

    private void validateKey(DayOfWeek day) {
        if (!trainingProgram.containsKey(day)) {
            throw new DayOffException(ExceptionMessages.DAY_OFF);
        }
    }

    private void validateExercise(Exercise exercise) {
        if (exercise == null) {
            throw new IllegalArgumentException(ExceptionMessages.EXERCISE_NULL);
        }
    }

    private void validateExerciseName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessages.EXERCISE_NAME_NULL_OR_EMPTY);
        }
    }

    private void validateWorkout(Workout workout) {
        if (workout == null) {
            throw new IllegalArgumentException(ExceptionMessages.WORKOUT_NULL);
        }
    }
}
