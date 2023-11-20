package bg.sofia.uni.fmi.mjt.udemy.course.duration;

public record ResourceDuration(int minutes)
{
    public ResourceDuration
    {
        if (minutes < 0)
            throw new IllegalArgumentException("Minutes must be bigger than 0");
        if (minutes > 60)
            throw new IllegalArgumentException("Minutes must be smaller than 60");
    }
}
