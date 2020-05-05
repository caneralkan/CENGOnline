package com.example.plhomework;

import java.util.ArrayList;

public class Course {
    private String courseName,courseID;
    private Student[] students;
    private Teacher teacher;
    ArrayList<String> announcements;

    public Course(String courseName, String courseID, Teacher teacher) {
        this.courseName = courseName;
        this.courseID = courseID;
        this.teacher = teacher;
    }

    public Course(String courseName, String courseID, Student[] students, Teacher teacher) {
        this.courseName = courseName;
        this.courseID = courseID;
        this.students = students;
        this.teacher = teacher;
        this.announcements=new ArrayList<>();
    }
    public void addAnnouncement(String string){
        announcements.add(string);
    }
    public ArrayList<String> getAnnouncements() {
        return announcements;
    }

    public void setAnnouncements(ArrayList<String> announcements) {
        this.announcements = announcements;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public Student[] getStudents() {
        return students;
    }

    public void setStudents(Student[] students) {
        this.students = students;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
