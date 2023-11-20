package bg.sofia.uni.fmi.mjt.udemy.course;

import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.ResourceNotFoundException;

import java.util.Arrays;

public class Course implements Completable, Purchasable
{
    private String name;
    private String description;
    private Category category;
    private double price;
    private Resource[] content;
    private CourseDuration totalTime;
    private boolean isPurchased;
    private double completionPercentage;
    private double percentagePerResource;

    public Course(String name, String description, double price, Resource[] content, Category category)
    {
        setName(name);
        setDescription(description);
        setPrice(price);
        setContent(content);
        this.category = category;
        this.totalTime = CourseDuration.of(this.content);
    }

    private void setName(String name)
    {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name can't be null or blank");

        this.name = name;
    }

    private void setDescription(String description)
    {
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Description can't be null or blank");

        this.description = description;
    }

    private void setPrice(double price)
    {
        if (price < 0)
            throw new IllegalArgumentException("Price can't be negative");

        this.price = price;
    }

    private Resource[] getOnlyNotNullElementsOf(Resource[] resources)
    {
        int index = 0;
        Resource[] result = new Resource[resources.length];

        for (Resource res : resources)
        {
            if (res != null)
            {
                result[index] = res;
                index++;
            }
        }

        return Arrays.copyOfRange(result, 0, index);
    }

    private void setContent(Resource[] content)
    {
        if (content == null)
            throw new IllegalArgumentException("Content can't be null or empty");

        // check if all resources are != null
        this.content = getOnlyNotNullElementsOf(content);

        //calculate percentagePerResource
        if (this.content.length == 0)
        {
            this.percentagePerResource = 100.0;
            return;
        }

        this.percentagePerResource = 100.0 / this.content.length;

        //calculate completionPercentage
        for (Resource res : this.content)
        {
            if (res.isCompleted())
                this.completionPercentage += percentagePerResource;
        }
    }
    private boolean containsResource(Resource resource)
    {
        for (Resource r: content)
        {
            if (r == null)
                break;

            if (r == resource)
                return true;
        }

        return false;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public double getPrice()
    {
        return price;
    }

    public Category getCategory()
    {
        return category;
    }

    public Resource[] getContent()
    {
        return content;
    }

    public CourseDuration getTotalTime()
    {
        return totalTime;
    }

    public void completeResource(Resource resourceToComplete) throws ResourceNotFoundException
    {
        if (resourceToComplete == null)
            throw new IllegalArgumentException("Resource can't be null");
        if (!containsResource(resourceToComplete))
            throw new ResourceNotFoundException("The course doesn't contain this resource");
        if (resourceToComplete.isCompleted())
            return;

        resourceToComplete.complete();
        completionPercentage += percentagePerResource;
    }

    // return 2  - if this == other
    // return 1  - if this > other
    // return -1 - if this < other
    public int compareDuration(CourseDuration otherDuration)
    {
        CourseDuration currentDuration = getTotalTime();

        if (currentDuration.hours() == otherDuration.hours()
                && currentDuration.minutes() == otherDuration.minutes())
        {
            return 2;
        }

        if (currentDuration.hours() >= otherDuration.hours()
                && currentDuration.minutes() >= otherDuration.minutes())
        {
            return 1;
        }

        return -1;
    }

    @Override
    public boolean isCompleted()
    {
        int percentage = (int) Math.round(completionPercentage);
        return percentage > (100 - (int) Math.round(percentagePerResource));
    }

    @Override
    public int getCompletionPercentage()
    {
        return isCompleted() ? 100 : (int) Math.round(completionPercentage);
    }

    @Override
    public void purchase() {isPurchased = true;}

    @Override
    public boolean isPurchased() {return isPurchased;}
}
