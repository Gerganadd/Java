package bg.sofia.uni.fmi.mjt.gym.workout;

import bg.sofia.uni.fmi.mjt.gym.messages.ExceptionMessages;

import java.util.SequencedCollection;

public record Workout(SequencedCollection<Exercise> exercises) {
    public void add(Exercise exercise) {
        if (exercise == null) {
            throw new IllegalArgumentException(ExceptionMessages.EXERCISE_NULL);
        }

        exercises.addLast(exercise);
    }

    public void addAll(SequencedCollection<Exercise> exercises) {
        if (exercises == null || exercises.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessages.EXERCISES_NULL_OR_EMPTY);
        }

        this.exercises.addAll(exercises);
    }

    public Exercise getLastWorkout() {
        return exercises.getLast();
    }

    public boolean conatins(Exercise exercise) {
        if (exercise == null) {
            throw new IllegalArgumentException(ExceptionMessages.EXERCISE_NULL);
        }

        return exercises.contains(exercise);
    }
}
