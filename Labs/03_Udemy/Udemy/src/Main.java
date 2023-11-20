import bg.sofia.uni.fmi.mjt.udemy.Udemy;
import bg.sofia.uni.fmi.mjt.udemy.account.Account;
import bg.sofia.uni.fmi.mjt.udemy.account.BusinessAccount;
import bg.sofia.uni.fmi.mjt.udemy.account.EducationalAccount;
import bg.sofia.uni.fmi.mjt.udemy.account.StandardAccount;
import bg.sofia.uni.fmi.mjt.udemy.course.Category;
import bg.sofia.uni.fmi.mjt.udemy.course.Course;
import bg.sofia.uni.fmi.mjt.udemy.course.Resource;
import bg.sofia.uni.fmi.mjt.udemy.course.duration.CourseDuration;
import bg.sofia.uni.fmi.mjt.udemy.course.duration.ResourceDuration;
import bg.sofia.uni.fmi.mjt.udemy.exception.*;

public class Main
{
    public static void main(String[] args) throws InsufficientBalanceException, MaxCourseCapacityReachedException, CourseAlreadyPurchasedException, ResourceNotFoundException, CourseNotCompletedException, CourseNotPurchasedException {
       ResourceDuration duration1 = new ResourceDuration(57);
       ResourceDuration duration2 = new ResourceDuration(58);
       ResourceDuration duration3 = new ResourceDuration(10);

       Resource r1 = new Resource("first", duration1);
       Resource r2 = new Resource("second", duration2);
       Resource r3 = new Resource("third", duration3);
       Resource r4 = new Resource("4", duration2);
       Resource r5 = new Resource("5", duration2);
       Resource r6 = new Resource("6", duration2);
       Resource r7 = new Resource("7", duration3);
       Resource r8 = new Resource("8", duration3);
       Resource r9 = new Resource("9", duration3);
       Resource r10 = new Resource("10", duration3);

       r2.complete();

       Course c1 =
               new Course("SDA1", "Some text 1", 10, new Resource[]{},Category.BUSINESS); // r1, r2, r3
       Course c2 =
               new Course("SDA2", "Some text 2", 11, new Resource[]{r4, r5, r6},Category.FINANCE);
       Course c3 =
               new Course("SDA3", "Some text 3", 12, new Resource[]{r7, r8, r9},Category.DESIGN);
       Course c4 =
               new Course("SDA4", "Some text 4", 13, new Resource[]{r10, r1, r2},Category.DEVELOPMENT);
       Course c5 =
               new Course("SDA5", "Some text 5", 14, new Resource[]{r5, r4, r3},Category.SOFTWARE_ENGINEERING);
       Course c6 =
               new Course("SDA6", "Some text 6", 15, new Resource[]{r5, r4, r3},Category.HEALTH_AND_FITNESS);
       Course c7 =
               new Course("SDA7", "Some text 7", 16, new Resource[]{r6, r7, r8},Category.MARKETING);
       Course c8 =
               new Course("SDA8", "Some text 8", 17, new Resource[]{r9, r10, r1},Category.MUSIC);
       Course c9 =
               new Course("SDA9", "Some text 9", 18, new Resource[]{r9, r10, r1},Category.DESIGN);
       Course c10 =
               new Course("SDA10", "Some text 10", 19, new Resource[]{r9, r10, r1},Category.MARKETING);
       Course c11 =
               new Course("SDA11", "Some text 11", 20, new Resource[]{r9, r10, r1},Category.HEALTH_AND_FITNESS);
       Course c12 =
               new Course("SDA12", "Some text 12", 21, new Resource[]{r9, r10, r1},Category.BUSINESS);

       Account a = new EducationalAccount("Gosho", 200);
       a.buyCourse(c1);

       System.out.println(c1.isCompleted());

       a.completeCourse(c1, 5);

       System.out.println(c1.isCompleted());

       //c1.completeResource(r1);
       //c1.completeResource(r2);
       //c1.completeResource(r3);

       c2.completeResource(r4);
       c2.completeResource(r5);
       c2.completeResource(r6);

       c3.completeResource(r7);
       c3.completeResource(r8);
       c3.completeResource(r9);

       c4.completeResource(r10);
       c4.completeResource(r1);
       c4.completeResource(r2);

       c5.completeResource(r3);
       c5.completeResource(r4);
       c5.completeResource(r5);

       // c6 is not complete

       c7.completeResource(r6);
       c7.completeResource(r7);
       c7.completeResource(r8);

       c8.completeResource(r9);
       c8.completeResource(r10);
       c8.completeResource(r1);

       c9.completeResource(r9);
       c9.completeResource(r10);
       c9.completeResource(r1);

       c10.completeResource(r9);
       c10.completeResource(r10);
       c10.completeResource(r1);

       c11.completeResource(r9);
       c11.completeResource(r10);
       c11.completeResource(r1);

       c12.completeResource(r9);
       c12.completeResource(r10);
       c12.completeResource(r1);


       Account a1 = new EducationalAccount("Ivan1", 1000);
       Account a2 = new EducationalAccount("Ivan2", 1001);
       Account a3 = new EducationalAccount("Ivan3", 1002);
       Account a4 = new EducationalAccount("Ivan4", 1003);
       Account a5 = new EducationalAccount("Ivan5", 1004);
       Account a6 = new EducationalAccount("Ivan6", 1005);
       Account a7 = new EducationalAccount("Ivan7", 1006);
       Account a8 = new EducationalAccount("Ivan8", 1007);
       Account a9 = new EducationalAccount("Ivan9", 1008);

       a1.buyCourse(c1);
       a1.buyCourse(c2);
       a1.buyCourse(c3);
       a1.buyCourse(c4);
       a1.buyCourse(c5);

       a1.completeCourse(c1, 4);
       a1.completeCourse(c2, 5);
       a1.completeCourse(c3, 6);
       a1.completeCourse(c4, 6);
       a1.completeCourse(c5, 6);

       a1.buyCourse(c6);

       a1.buyCourse(c7);
       a1.buyCourse(c8);
       a1.buyCourse(c9);
       a1.buyCourse(c10);
       a1.buyCourse(c11);

       a1.completeCourse(c7, 4);
       a1.completeCourse(c8, 5);
       a1.completeCourse(c9, 6);
       a1.completeCourse(c10, 6);
       a1.completeCourse(c11, 6);

       a1.buyCourse(c12);
       c6.completeResource(r3);
       c6.completeResource(r4);

       System.out.println(c6.getCompletionPercentage());

       //System.out.println(Math.round(66.6));

       //System.out.println(c3.compareDuration(c1.getTotalTime()));
       //System.out.printf(a1.getBalance() + " ");

    }
}