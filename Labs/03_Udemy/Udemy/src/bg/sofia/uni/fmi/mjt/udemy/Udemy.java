package bg.sofia.uni.fmi.mjt.udemy;

import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.exception.AccountNotFoundException;
import bg.sofia.uni.fmi.mjt.udemy.exception.CourseNotFoundException;

import java.util.Arrays;

public class Udemy implements LearningPlatform
{
    private Account[] accounts;
    private Course[] courses;
    public Udemy(Account[] accounts, Course[] courses)
    {
        setAccounts(accounts);
        setCourses(courses);
    }

    private void setAccounts(Account[] accounts)
    {
        this.accounts = accounts;
    }
    private void setCourses(Course[] courses)
    {
        this.courses = courses;
    }

    private boolean containsSubstring(String source, String target)
    {
        if (target == null || source == null)
            return false;

        for (int i = 0; i < source.length(); i++)
        {
            if (source.charAt(i) == target.charAt(0))
            {
                for (int k = 0; k < target.length(); k++)
                {
                    if (i + k > source.length())
                        return false;

                    if (source.charAt(i+k) != target.charAt(k))
                        return false;
                }

                return true;
            }
        }

        return false;
    }

    private boolean isValidKeyword(String keyword)
    {
        char[] symbols = keyword.toLowerCase().toCharArray();
        for (char symbol : symbols)
        {
            if (symbol == ' ') continue;

            if (symbol < 'a' || symbol > 'z')
                return false;
        }

        return true;
    }

    @Override
    public Course findByName(String name) throws CourseNotFoundException
    {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name can't be null or blank");

        for (Course course : courses)
        {
            if (course == null)
                break;
            if (course.getName().equals(name))
                return course;
        }

        throw new CourseNotFoundException("Course doesn't exist");
    }

    @Override
    public Course[] findByKeyword(String keyword)
    {
        if (keyword == null || keyword.isBlank() || !isValidKeyword(keyword))
            throw new IllegalArgumentException("Invalid keyword");

        Course[] matches = new Course[courses.length];

        int index = 0;

        for (Course course: courses)
        {
            if (course == null)
                break;

            String description = course.getDescription();
            String title = course.getName();

            if (containsSubstring(title, keyword) || containsSubstring(description, keyword))
            {
                matches[index] = course;
                index++;
            }
        }

        return Arrays.copyOfRange(matches, 0, index);
    }

    @Override
    public Course[] getAllCoursesByCategory(Category category)
    {
        Course[] matches = new Course[courses.length];

        int index = 0;

        for (Course course : courses)
        {
            if (course != null && course.getCategory() == category)
            {
                matches[index] = course;
                index++;
            }
        }

        return Arrays.copyOfRange(matches, 0, index);
    }

    @Override
    public Account getAccount(String name) throws AccountNotFoundException
    {
        for (Account account : accounts)
        {
            if (account != null && account.getUsername().equals(name))
                return account;
        }

        throw new AccountNotFoundException("Account doesn't exist");
    }

    @Override
    public Course getLongestCourse()
    {
        if (courses.length == 0)
            return null;

        Course longestCourse = courses[0];
        int index = 0;

        for (int i = 1; i < courses.length; i++)
        {
            if (courses[i].compareDuration(longestCourse.getTotalTime()) == -1)
            {
                longestCourse = courses[i];
                index = i;
            }
        }

        return courses[index];
    }

    @Override
    public Course getCheapestByCategory(Category category)
    {
        Course[] coursesByCategory = getAllCoursesByCategory(category);

        if (coursesByCategory == null)
            return null;

        double minPrice = coursesByCategory[0].getPrice();
        int minPriceIndex = 0;

        for (int i = 1; i < coursesByCategory.length; i++)
        {
            if (coursesByCategory[i].getPrice() < minPrice)
            {
                minPrice = coursesByCategory[i].getPrice();
                minPriceIndex = i;
            }
        }

        return coursesByCategory[minPriceIndex];
    }
}