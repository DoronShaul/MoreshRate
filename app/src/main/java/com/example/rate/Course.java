package com.example.rate;

/**
 * this class represents a course.
 */
public class Course {
    private String teacherName;
    private double teacherAvg;
    private double courseAvg;
    private double testAvg;
    private int numOfRatings;
    private double totalAvg;
    private boolean isMust;

    public Course() {
        this.teacherName = "";
        this.courseAvg = 0;
        this.teacherAvg = 0;
        this.testAvg = 0;
        this.totalAvg = 0;
        this.numOfRatings = 0;
        this.isMust = true;
    }


    public Course(String teacher, boolean must) {
        this.teacherName = teacher;
        this.courseAvg = 0;
        this.teacherAvg = 0;
        this.testAvg = 0;
        this.totalAvg = 0;
        this.numOfRatings = 0;
        this.isMust = must;
    }

    public Course(Course other) {
        this.teacherName = other.getTeacherName();
        this.isMust = other.getIsMust();
        this.testAvg = other.getTestAvg();
        this.teacherAvg = other.getTeacherAvg();
        this.courseAvg = other.getCourseAvg();
        this.totalAvg = other.getTotalAvg();
        this.numOfRatings = other.getNumOfRatings();
    }


    public double getTotalAvg() {
        return totalAvg;
    }

    public void setTotalAvg(double totalAvg) {
        this.totalAvg = totalAvg;
    }

    public double getTeacherAvg() {
        return teacherAvg;
    }

    public void setTeacherAvg(double teacherAvg) {
        this.teacherAvg = teacherAvg;
    }

    public double getCourseAvg() {
        return courseAvg;
    }

    public void setCourseAvg(double courseAvg) {
        this.courseAvg = courseAvg;
    }

    public double getTestAvg() {
        return testAvg;
    }

    public void setTestAvg(double testAvg) {
        this.testAvg = testAvg;
    }

    public int getNumOfRatings() {
        return numOfRatings;
    }

    public void setNumOfRatings(int numOfRatings) {
        this.numOfRatings = numOfRatings;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public boolean getIsMust() {
        return isMust;
    }

    public void setIsMust(boolean must) {
        this.isMust = must;
    }
}
