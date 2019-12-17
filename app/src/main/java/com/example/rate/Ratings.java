package com.example.rate;

public class Ratings {
    private String courseName;
    private int teacherRating;
    private int courseRating;
    private int testRating;

    public Ratings() {

    }

    public Ratings(String name, int teacher, int course, int test){
        this.courseName=name;
        this.courseRating=course;
        this.teacherRating=teacher;
        this.testRating=test;
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
