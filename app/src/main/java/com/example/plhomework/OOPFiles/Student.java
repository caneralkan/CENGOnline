package com.example.plhomework.OOPFiles;

import java.util.ArrayList;

public class Student extends User {

    public Student(String name, String surname, String email, ArrayList<Course> courses) {
        super(name, surname, email,false,courses);
    }
    public Student(String name, String surname, String email) {
        super(name, surname, email,false);
    }
    public Student(String name, String surname, String email,boolean isStudent) {
        super(name, surname, email,isStudent);
    }

    @Override
    protected void enrollCourse(Course course) {
        courses.add(course);
        course.addStudentToCourse(this);
    }
    @Override
    protected void removeFromCourse(Course course) {
        courses.remove(course);
        course.removeStudentFromCourse(this);
    }
    @Override
    protected boolean isEnrolled(Course course) {

        return courses.contains(course);
    }
}
