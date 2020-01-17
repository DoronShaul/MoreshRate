package com.example.rate;

/**
 * this class represents a student.
 */
public class Students {
    private String studentName;

    /**
     * default constructor.
     */
    Students() {
        studentName = "";
    }

    /**
     * a constructor.
     */
    Students(String studentName){
        this.studentName = studentName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
