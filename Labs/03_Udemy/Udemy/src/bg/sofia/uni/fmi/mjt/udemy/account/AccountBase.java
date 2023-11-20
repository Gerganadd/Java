package bg.sofia.uni.fmi.mjt.udemy.account;

import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.exception.*;

public abstract class AccountBase implements Account
{
    protected static final int MAX_COURSES_COUNT = 100;
    private String username;
    protected double balance;
    protected Course[] courses;
    protected double[] grades;
    protected int coursesIndex;

    public AccountBase(String username, double balance)
    {
        setUsername(username);
        setBalance(balance);
        grades = new double[MAX_COURSES_COUNT];
        courses = new Course[MAX_COURSES_COUNT];
        coursesIndex = 0;
    }

    private void setUsername(String username)
    {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Username can't be null or blank");

        this.username = username;
    }

    private void setBalance(double balance)
    {
        if (balance < 0)
            throw new IllegalArgumentException("Balance can't be negative");

        this.balance = balance;
    }

    protected boolean hasCourse(String courseName)
    {
        for (Course course : courses)
        {
            if (course == null)
                break;

            if (courseName.equals(course.getName()))
                return true;
        }

        return false;
    }

    protected boolean isCourseUnavailable(Course course)
    {
        for (Course c : courses)
        {
            if (c == null)
                break;

            if (c == course)
                return !c.isPurchased(); // or true
        }

        return true;
    }

    private boolean areAllResourcesCompleted(Course course)
    {
        boolean hasUncomplete = false;

        Resource[] resources = course.getContent();
        for (Resource res : resources)
        {
            if (res == null || !res.isCompleted())
            {
                hasUncomplete = true;
                break;
            }
        }

        return !hasUncomplete;
    }

    private int getCourseIndex(Course course)
    {
        for (int i = 0; i < coursesIndex; i++)
        {
            if (courses[i] == course)
                return i;
        }

        //throw new CourseNotFoundException("Course can't be found");
        return -1;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    @Override
    public void addToBalance(double amount)
    {
        if (amount < 0)
            throw new IllegalArgumentException("Amount can't be negative value");

        this.balance += amount;
    }

    @Override
    public double getBalance()
    {
        return balance;
    }

    public abstract void buyCourse(Course course) throws InsufficientBalanceException, CourseAlreadyPurchasedException, MaxCourseCapacityReachedException;

    @Override
    public void completeResourcesFromCourse(Course course, Resource[] resourcesToComplete) throws CourseNotPurchasedException, ResourceNotFoundException
    {
        if (course == null || resourcesToComplete == null)
            throw new IllegalArgumentException("Course or resources can't be null");
        if (isCourseUnavailable(course))
            throw new CourseNotPurchasedException("Course is not purchased");

        for (Resource resource : resourcesToComplete)
        {
            course.completeResource(resource);
        }
    }

    @Override
    public void completeCourse(Course course, double grade) throws CourseNotPurchasedException, CourseNotCompletedException
    {
        if (course == null)
            throw new IllegalArgumentException("Course can't be null");
        if (grade < 2 || grade > 6)
            throw new IllegalArgumentException("Grade must be in range [2.0 ; 6.0]");
        if (isCourseUnavailable(course))
            throw new CourseNotPurchasedException("Course is not purchased");
        if (!areAllResourcesCompleted(course))
            throw new CourseNotCompletedException("The course has uncompleted resources");

        int index = getCourseIndex(course);
        if (index != -1)
            grades[index] = grade;  //mark course as completed
    }

    @Override
    public Course getLeastCompletedCourse()
    {
        if (coursesIndex == 0)
            return null;

        int leastCompleted = courses[0].getCompletionPercentage();
        int index = 0;

        for (int i = 1; i < coursesIndex; i++)
        {
            if (courses[i].getCompletionPercentage() < leastCompleted)
            {
                leastCompleted = courses[i].getCompletionPercentage();
                index = i;
            }
        }

        return courses[index];
    }
}
