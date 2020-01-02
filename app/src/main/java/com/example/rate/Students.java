package com.example.rate;

public class Students {
    private String studentName;

    Students() {
        studentName = "";
    }

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
