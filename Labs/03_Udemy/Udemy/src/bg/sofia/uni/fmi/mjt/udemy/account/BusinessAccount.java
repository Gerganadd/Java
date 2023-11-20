package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.account.type.AccountType;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseAlreadyPurchasedException;
import bg.sofia.uni.fmi.mjt.udemy.exception.InsufficientBalanceException;
import bg.sofia.uni.fmi.mjt.udemy.exception.MaxCourseCapacityReachedException;

public class BusinessAccount extends AccountBase
{
    Category[] categories;
    public BusinessAccount(String username, double balance, Category[] allowedCategories)
    {
        super(username, balance);
        this.categories = allowedCategories;
    }

    private boolean containsCategory(Category category)
    {
        if (category == null)
            return false; // or throw exception

        for (Category c : categories)
        {
            if (c == null)
                break;
            if (c == category)
                return true;
        }

        return false;
    }
    private double discountedCoursePrice(Course course)
    {
        double discount = course.getPrice() * AccountType.BUSINESS.getDiscount();
        return course.getPrice() - discount;
    }

    private void checkCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException
    {
        if (course == null)
            throw new IllegalArgumentException("Course can't be null");

        if (coursesIndex == MAX_COURSES_COUNT)
            throw new MaxCourseCapacityReachedException("You have reached the maximum courses limit");

        if (!containsCategory(course.getCategory()))
            throw new IllegalArgumentException("This account can't by the course with that category");

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
