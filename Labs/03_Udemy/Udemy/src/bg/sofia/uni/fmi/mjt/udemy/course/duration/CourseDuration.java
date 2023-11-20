package bg.sofia.uni.fmi.mjt.udemy.course.duration;

import bg.sofia.uni.fmi.mjt.udemy.course.Resource;

public record CourseDuration(int hours, int minutes)
{
    public CourseDuration
    {
        if (hours < 0)
            throw new IllegalArgumentException("Hours must be bigger than 0");
        if (hours > 24)
            throw new IllegalArgumentException("Hours must be smaller than 24");
        if (minutes < 0)
            throw new IllegalArgumentException("Minutes must be bigger than 0");
        if (minutes > 60)
            throw new IllegalArgumentException("Minutes must be smaller than 60");
    }

    public static CourseDuration of(Resource[] content)
    {
        if (content == null)
            throw new IllegalArgumentException("content is null");

        int minutesValue = 0;
        int hoursValue = 0;

        for (Resource resource : content)
        {
            if (resource == null)
                throw new IllegalArgumentException("Resource is null");

            minutesValue += resource.getDuration().minutes();

            if (minutesValue >= 60)
            {
                minutesValue -= 60;
                hoursValue++;
            }
        }

        return new CourseDuration(hoursValue, minutesValue);
    }
}
