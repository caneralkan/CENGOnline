package com.example.plhomework.Model;

import java.util.ArrayList;

public class Teacher extends User {

    public Teacher(String name, String surname, String email,boolean isStudent) {
        super(name, surname, email,isStudent);
    }

    public Teacher(String name, String surname, String email, boolean isStudent, ArrayList<Course> courses) {
        super(name, surname, email, isStudent, courses);
    }

    @Override
    protected void enrollCourse(Course course) {
        courses.add(course);
        course.setTeacher(this);
    }

    @Override
    protected void removeFromCourse(Course course) {
        courses.remove(course);
    }

    @Override
    protected boolean isEnrolled(Course course) {

        return courses.contains(course);
    }

    public Teacher(String name, String surname, String email) {
        super(name, surname, email,false);
    }

}
