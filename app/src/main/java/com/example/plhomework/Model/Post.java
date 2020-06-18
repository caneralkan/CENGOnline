package com.example.plhomework.Model;

import java.util.ArrayList;

public class Post {
    String postID, postTitle, postContext,postDate,teacherEmail;
    ArrayList<Comment> comments;
    public Post(String postID, String postTitle, String postContext, String postDate, String teacherEmail) {
        this.postID = postID;
        this.postTitle = postTitle;
        this.postContext = postContext;
        this.postDate = postDate;
        this.teacherEmail = teacherEmail;
        this.comments=new ArrayList<>();
    }
    public void addCommentToPost(Comment comment){
        this.comments.add(comment);
    }
    public void removeComment(Comment comment){
        if(comments.contains(comment))
        this.comments.remove(comment);
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContext() {
        return postContext;
    }

    public void setPostContext(String postContext) {
        this.postContext = postContext;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getTeacherEmail() {
        return teacherEmail;
    }

    public void setTeacherEmail(String teacherEmail) {
        this.teacherEmail = teacherEmail;
    }
}
