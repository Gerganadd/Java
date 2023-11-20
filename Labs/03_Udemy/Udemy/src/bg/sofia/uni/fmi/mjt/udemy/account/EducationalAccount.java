package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class EducationalAccount extends AccountBase
{
    private final static int COMPLETED_COURSES_REQUIRED = 5;
    private double coursePrice = 0.0;

    public EducationalAccount(String username, double balance)
    {
        super(username, balance);
    }

    private boolean hasDiscount()
    {
        if (coursesIndex < COMPLETED_COURSES_REQUIRED)
            return false;

        double sum = 0.0;

        //check if last five courses are completed
        for (int i = coursesIndex - COMPLETED_COURSES_REQUIRED; i < coursesIndex; i++)
        {
            if (super.grades[i] != 0)
            {
                sum += super.grades[i];
            }
            else
            {
                return false;
            }
        }

        return (sum / COMPLETED_COURSES_REQUIRED) >= 4.5;
    }
    private void calculateCoursePrice(Course course)
    {
        coursePrice = course.getPrice();

        if (hasDiscount())
        {
            double discount = course.getPrice() * AccountType.EDUCATION.getDiscount();
            coursePrice -= discount;
        }
    }
    private void checkCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException
    {
        if (course == null)
            throw new IllegalArgumentException("Course can't be null");

        if (coursesIndex == MAX_COURSES_COUNT)
            throw new MaxCourseCapacityReachedException("You have reached the maximum courses limit");

        if (super.hasCourse(course.getName()))
            throw new CourseAlreadyPurchasedException("You already have that course");

        calculateCoursePrice(course);

        if (coursePrice > super.getBalance())
            throw new InsufficientBalanceException("You don't have enough money");
    }
    @Override
    public void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException
    {
        checkCourse(course);

        course.purchase();

        super.balance -= coursePrice;

        courses[coursesIndex] = course;
        coursesIndex++;
    }
}
