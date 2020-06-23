package com.example.plhomework.Model;

public class Announcement {
    String context,title,courseID,date,ID;

    public Announcement(String context, String title, String courseID,String date,String ID) {
        this.context = context;
        this.title = title;
        this.courseID = courseID;
        this.date=date;
        this.ID=ID;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getDate() {
        return date;
    }

    public String getID() {
        return ID;
    }

}
