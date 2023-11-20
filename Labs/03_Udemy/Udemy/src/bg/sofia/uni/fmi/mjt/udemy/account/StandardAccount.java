package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class StandardAccount extends AccountBase
{
    public StandardAccount(String username, double balance) {
        super(username, balance);
    }

    private double discountedCoursePrice(Course course)
    {
        double discount = course.getPrice() * AccountType.STANDARD.getDiscount();
        return course.getPrice() - discount;
    }
    private void checkCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException
    {
        if (course == null)
            throw new IllegalArgumentException("Course can't be null");

        if (coursesIndex == MAX_COURSES_COUNT)
            throw new MaxCourseCapacityReachedException("You have reached the maximum courses limit");

        if (super.hasCourse(course.getName()))
            throw new CourseAlreadyPurchasedException("You already have that course");

        if (discountedCoursePrice(course)  > super.getBalance())
            throw new InsufficientBalanceException("You don't have enough money");
    }
    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException
    {
        checkCourse(course);

        course.purchase();

        courses[coursesIndex] = course;
        coursesIndex++;

        super.balance -= discountedCoursePrice(course);
    }
}
