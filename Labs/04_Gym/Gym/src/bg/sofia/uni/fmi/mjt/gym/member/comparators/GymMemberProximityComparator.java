package bg.sofia.uni.fmi.mjt.gym.member.comparators;

import bg.sofia.uni.fmi.mjt.gym.member.Address;
import bg.sofia.uni.fmi.mjt.gym.member.GymMember;
import bg.sofia.uni.fmi.mjt.gym.messages.ExceptionMessages;

import java.util.Comparator;

public class GymMemberProximityComparator implements Comparator<GymMember> {

    private Address startPosition;

    public GymMemberProximityComparator(Address startPosition) {
        this.startPosition = startPosition;
    }

    @Override
    public int compare(GymMember o1, GymMember o2) {
        if (o1 == null || o2 == null) {
            throw new IllegalArgumentException(ExceptionMessages.GYM_MEMBER_NULL);
        }

        double firstDistance = o1.getAddress().getDistanceTo(startPosition);
        double secondDistance = o2.getAddress().getDistanceTo(startPosition);

        return Double.compare(firstDistance, secondDistance);
    }
}
