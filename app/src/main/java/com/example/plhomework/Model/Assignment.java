package com.example.plhomework.Model;

import java.io.Serializable;

public class Assignment implements Serializable {
    String assignmentID, description, courseID,assignmentTitle;
    String startDate, endDate;

    public Assignment(String assignmentID,String assignmentTitle, String description, String courseID,  String startDate, String endDate) {
        this.assignmentID = assignmentID;
        this.description = description;
        this.courseID = courseID;
        this.assignmentTitle = assignmentTitle;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getAssignmentTitle() {
        return assignmentTitle;
    }

    public void setAssignmentTitle(String assignmentTitle) {
        this.assignmentTitle = assignmentTitle;
    }

    public String getAssignmentID() {
        return assignmentID;
    }

    public void setAssignmentID(String assignmentID) {
        this.assignmentID = assignmentID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
