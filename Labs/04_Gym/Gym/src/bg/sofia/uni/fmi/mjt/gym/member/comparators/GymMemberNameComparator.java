package bg.sofia.uni.fmi.mjt.gym.member.comparators;

import bg.sofia.uni.fmi.mjt.gym.member.GymMember;
import bg.sofia.uni.fmi.mjt.gym.messages.ExceptionMessages;

import java.util.Comparator;

public class GymMemberNameComparator implements Comparator<GymMember> {
    @Override
    public int compare(GymMember o1, GymMember o2) {
        if (o1 == null || o2 == null) {
            throw new IllegalArgumentException(ExceptionMessages.GYM_MEMBER_NULL);
        }

        return o1.getName().compareTo(o2.getName());
    }
}
