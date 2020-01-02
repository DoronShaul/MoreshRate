package com.example.rate;

/**
 * this class represents a rating of a course.
 */
public class Ratings {
    private String courseID;
    private int teacherRating;
    private int courseRating;
    private int testRating;
    private String comment;
    private String userID;

    public Ratings() {
        this.courseID = "";
        this.teacherRating = 0;
        this.courseRating = 0;
        this.testRating = 0;
        this.comment = "";
        userID="";
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Ratings(String id, String comment, int teacher, int course, int test, String userID) {
        this.courseID = id;
        this.comment = comment;
        this.courseRating = course;
        this.teacherRating = teacher;
        this.testRating = test;
        this.userID = userID;
    }


    public void setCourseID(String courseID) {
        this.courseID = courseID;
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

    public String getCourseID() {
        return courseID;
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
