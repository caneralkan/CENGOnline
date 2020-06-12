package com.example.plhomework.OOPFiles;

import java.util.ArrayList;

public class Course {
    private String courseName,courseID;
    ArrayList<Student> students;
    private Teacher teacher;
    ArrayList<String> announcements;

    public Course(String courseName, String courseID, Teacher teacher) {
        this.courseName = courseName;
        this.courseID = courseID;
        this.teacher = teacher;
        this.announcements=new ArrayList<>();
        this.students=new ArrayList<>();
    }
    public Course(String courseName, String courseID, String teacherName,String teacherSurname,String teacherEmail) {
        this.courseName = courseName;
        this.courseID = courseID;
        this.teacher = new Teacher(teacherName,teacherSurname,teacherEmail);
        this.announcements=new ArrayList<>();
        this.students=new ArrayList<>();
    }

    public Course(String courseName, String courseID, ArrayList<Student> students, Teacher teacher) {
        this.courseName = courseName;
        this.courseID = courseID;
        this.students = students;
        this.teacher = teacher;
        this.announcements=new ArrayList<>();
    }

    public Course(String courseName, String courseID, ArrayList<Student> students, Teacher teacher, ArrayList<String> announcements) {
        this.courseName = courseName;
        this.courseID = courseID;
        this.students = students;
        this.teacher = teacher;
        this.announcements = announcements;
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

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<Student> students) {
        this.students = students;
    }
    public void addStudentToCourse(Student student){
        students.add(student);
    }
    public void removeStudentFromCourse(Student student){
        if(students.contains(student)){
            students.remove(student);
        }
    }
    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
