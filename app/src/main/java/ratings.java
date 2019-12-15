public class ratings {
    private String courseID;
    private String courseName;
    private int teacherRating;
    private int courseRating;
    private int testRating;

    public ratings() {

    }

    public ratings(String id, String name, int teacher, int course, int test){
        this.courseID=id;
        this.courseName=name;
        this.courseRating=course;
        this.teacherRating=teacher;
        this.testRating=test;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setTeacherRating(int teacherRating) {
        this.teacherRating = teacherRating;
    }

    public void setCourseRating(int courseRating) {
        this.courseRating = courseRating;
    }

    public void setTestRating(int testRating) {
        this.testRating = testRating;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getTeacherRating() {
        return teacherRating;
    }

    public int getCourseRating() {
        return courseRating;
    }

    public int getTestRating() {
        return testRating;
    }
}
