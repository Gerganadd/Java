package bg.sofia.uni.fmi.mjt.gym;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;
import bg.sofia.uni.fmi.mjt.gym.member.comparators.GymMemberNameComparator;
import bg.sofia.uni.fmi.mjt.gym.member.comparators.GymMemberProximityComparator;
import bg.sofia.uni.fmi.mjt.gym.messages.ExceptionMessages;
import bg.sofia.uni.fmi.mjt.gym.workout.Exercise;

import java.time.DayOfWeek;

import java.util.Collections;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Gym implements GymAPI {
    private int capacity;
    private Address address;
    private SortedSet<GymMember> members;
    private Map<DayOfWeek, Set<Exercise>> program;

    public Gym(int capacity, Address address) {
        setCapacity(capacity);
        setAddress(address);

        members = new TreeSet<>(new GymMemberNameComparator());
        program = new HashMap<>();
    }

    private void setCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException(ExceptionMessages.NEGATIVE_CAPACITY);
        }

        this.capacity = capacity;
    }

    private void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public SortedSet<GymMember> getMembers() {
        if (members.isEmpty())
            return Collections.emptySortedSet();

        return Collections.unmodifiableSortedSet(members);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByName() {
        if (members.isEmpty())
            return Collections.emptySortedSet();

        return Collections.unmodifiableSortedSet(members);
    }

    @Override
    public SortedSet<GymMember> getMembersSortedByProximityToGym() {
        if (members.isEmpty())
            return Collections.emptySortedSet();

        SortedSet<GymMember> membersSortedByProximity = new TreeSet<>(new GymMemberProximityComparator(address));
        membersSortedByProximity.addAll(members);

        return Collections.unmodifiableSortedSet(membersSortedByProximity);
    }

    @Override
    public void addMember(GymMember member) throws GymCapacityExceededException {
        validateMember(member);
        checkCapacity(1); // because we want to add only one member

        this.members.add(member);
    }

    @Override
    public void addMembers(Collection<GymMember> members) throws GymCapacityExceededException {
        validateMembers(members);
        checkCapacity(members.size());

        this.members.addAll(members);
    }

    @Override
    public boolean isMember(GymMember member) {
        validateMember(member);

        return members.contains(member);
    }

    @Override
    public boolean isExerciseTrainedOnDay(String exerciseName, DayOfWeek day) {
        validateExerciseName(exerciseName);
        validateDay(day);

        Map<DayOfWeek, List<String>> allMembers = getDailyListOfMembersForExercise(exerciseName);

        return allMembers.containsKey(day);
    }

    @Override
    public Map<DayOfWeek, List<String>> getDailyListOfMembersForExercise(String exerciseName) {
        validateExerciseName(exerciseName);

        Map<DayOfWeek, List<String>> matches = new HashMap<>();

        for (GymMember member : members) {
            Collection<DayOfWeek> days = member.getDaysFinishingWith(exerciseName);

            for (DayOfWeek day : days) {
                if (!matches.containsKey(day)) {
                    matches.put(day, new ArrayList<>());
                }

                matches.get(day).add(member.getName());
            }
        }

        return matches;
    }

    private void validateMember(GymMember member) {
        if (member == null) {
            throw new IllegalArgumentException(ExceptionMessages.GYM_MEMBER_NULL);
        }
    }

    private void validateMembers(Collection<GymMember> members) {
        if (members == null || members.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessages.MEMBERS_NULL_OR_EMPTY);
        }
    }

    private void checkCapacity(int newMembers) throws GymCapacityExceededException {
        if (members.size() + newMembers >= capacity) {
            throw new GymCapacityExceededException(ExceptionMessages.GYM_FULL);
        }
    }

    private void validateExerciseName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessages.EXERCISE_NAME_NULL_OR_EMPTY);
        }
    }

    private void validateDay(DayOfWeek day) {
        if (day == null) {
            throw new IllegalArgumentException(ExceptionMessages.DAY_OF_WEEK_NULL);
        }
    }

}
