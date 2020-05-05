package com.example.plhomework;

import java.util.ArrayList;

public class Teacher extends User {
    ArrayList<Course> coursesTeacherTeach;

    public Teacher(String name, String surname, String email,boolean isStudent, ArrayList<Course> coursesTeacherTeach) {
        super(name, surname, email,isStudent);
        this.coursesTeacherTeach = coursesTeacherTeach;
    }

    public Teacher(String name, String surname, String email,boolean isStudent) {
        super(name, surname, email,isStudent);
    }

    public ArrayList<Course> getCoursesTeacherTeach() {
        return coursesTeacherTeach;
    }

    public void setCoursesTeacherTeach(ArrayList<Course> coursesTeacherTeach) {
        this.coursesTeacherTeach = coursesTeacherTeach;
    }
    public void addCourseToTeacher(Course course){
        coursesTeacherTeach.add(course);
    }
}
