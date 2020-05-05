package com.example.plhomework;

public abstract class User {
    protected String name,surname,email;
    protected boolean isStudent;

    public User(String name, String surname, String email) {
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public User(String name, String surname, String email, boolean isStudent) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.isStudent = isStudent;
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
}
