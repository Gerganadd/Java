package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;

public class Resource implements Completable
{
    private int completionPercentage;
    private String name;
    private ResourceDuration duration;

    public Resource(String name, ResourceDuration duration)
    {
        setName(name);
        setDuration(duration);
        this.completionPercentage = 0;
    }

    private void setName(String name)
    {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Resource name can't be null or blank");

        this.name = name;
    }

    private void setDuration(ResourceDuration duration)
    {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public ResourceDuration getDuration() {
        return duration;
    }

    public void complete()
    {
        completionPercentage = 100;
    }

    @Override
    public boolean isCompleted()
    {
        return completionPercentage == 100;
    }

    @Override
    public int getCompletionPercentage()
    {
        return completionPercentage;
    }
}
