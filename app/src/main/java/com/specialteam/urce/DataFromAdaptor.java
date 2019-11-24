package com.specialteam.urce;

public class DataFromAdaptor {
    private String CName;
    private String PhotoID;
    private String CommentID;

    public DataFromAdaptor(){

    }

    public DataFromAdaptor(String CName,String PhotoID,String CommentID){
        this.CName=CName;
        this.PhotoID=PhotoID;
        this.CommentID=CommentID;
    }

    public String getCName() {
        return CName;
    }

    public String getPhotoID() {
        return PhotoID;
    }

    public String getCommentID() {
        return CommentID;
    }
}
