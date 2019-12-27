package com.specialteam.urce;

public class DataFromAdaptor {
    private String CName;
    private String PhotoID;
    //private String CommentID;
    private String Liked;
    private String ID;
    private String Desc;

    public DataFromAdaptor(){

    }

    /*public DataFromAdaptor(String CName,String PhotoID,String CommentID){
        this.CName=CName;
        this.PhotoID=PhotoID;
        this.CommentID=CommentID;
    }*/

    public DataFromAdaptor(String CName,String PhotoID,String Liked,String ID,String Desc){
        this.CName=CName;
        this.PhotoID=PhotoID;
        this.Liked=Liked;
        this.ID=ID;
        this.Desc=Desc;
    }

    public String getCName() {
        return CName;
    }

    public String getPhotoID() {
        return PhotoID;
    }

    //To enable comments uncomment
    /*public String getCommentID() {
        return CommentID;
    }*/

    public String getLiked(){return Liked;}

    public String getID(){return ID;}

    public String getDesc(){return Desc;}
}
