package com.example.rate;

public class Ratings {
    private String courseName;
    private int teacherRating;
    private int courseRating;
    private int testRating;
    private String comment;

    public Ratings() {
        this.courseName = "";
        this.teacherRating = 0;
        this.courseRating = 0;
        this.testRating = 0;
        this.comment = "";
    }

    public Ratings(String name, String comment, int teacher, int course, int test) {
        this.courseName = name;
        this.comment = comment;
        this.courseRating = course;
        this.teacherRating = teacher;
        this.testRating = test;
    }


    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getComment() {
        return comment;
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
