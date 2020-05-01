package com.example.plhomework;

import java.util.ArrayList;

public class Teacher extends User {
    ArrayList<Course> coursesTeacherTeach;

    public Teacher(String name, String surname, String email, ArrayList<Course> coursesTeacherTeach) {
        super(name, surname, email);
        this.coursesTeacherTeach = coursesTeacherTeach;
    }

    public Teacher(String name, String surname, String email) {
        super(name, surname, email);
    }

    public ArrayList<Course> getCoursesTeacherTeach() {
        return coursesTeacherTeach;
    }

    public void setCoursesTeacherTeach(ArrayList<Course> coursesTeacherTeach) {
        this.coursesTeacherTeach = coursesTeacherTeach;
    }
}
