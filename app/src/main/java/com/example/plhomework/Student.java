package com.example.plhomework;

import java.util.ArrayList;

public class Student extends User {
    ArrayList<Course> coursesStudentIn;

    public Student(String name, String surname, String email, ArrayList<Course> coursesStudentIn) {
        super(name, surname, email);
        this.coursesStudentIn = coursesStudentIn;
    }

    public Student(String name, String surname, String email) {
        super(name, surname, email);
    }
    public ArrayList<Course> getCoursesStudentIn() {
        return coursesStudentIn;
    }

    public void setCoursesStudentIn(ArrayList<Course> coursesStudentIn) {
        this.coursesStudentIn = coursesStudentIn;
    }

}
