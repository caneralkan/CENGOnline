package com.example.plhomework.OOPFiles;

import java.util.ArrayList;

public abstract class User {
    protected String name,surname,email;
    protected boolean isStudent;
    public  ArrayList<Course> courses;

    public User(String name, String surname, String email, boolean isStudent) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.isStudent = isStudent;

        this.courses=new ArrayList<>();
    }
    public User(String name, String surname, String email, boolean isStudent,ArrayList<Course> courses) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.isStudent = isStudent;
        this.courses=courses;
    }

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean student) {
        isStudent = student;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    protected abstract void enrollCourse(Course course);
    protected abstract void removeFromCourse(Course course);
    protected abstract boolean isEnrolled(Course course);


}
