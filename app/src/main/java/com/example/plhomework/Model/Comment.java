package com.example.plhomework.Model;

public class Comment {
    String commenterEmail, commentContext,commentDate ;

    public Comment(String commenterEmail, String commentContext, String commentDate) {
        this.commenterEmail = commenterEmail;
        this.commentContext = commentContext;
        this.commentDate = commentDate;
    }

    public String getCommenterEmail() {
        return commenterEmail;
    }

    public void setCommenterEmail(String commenterEmail) {
        this.commenterEmail = commenterEmail;
    }

    public String getCommentContext() {
        return commentContext;
    }

    public void setCommentContext(String commentContext) {
        this.commentContext = commentContext;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }
}
