package com.specialteam.urce;

public class CommentsData {

    String Oname;
    String Comment;

    public CommentsData(){

    }

    public CommentsData(String Oname,String Comment){
        this.Oname = Oname;
        this.Comment = Comment;
    }

    public String getOname() {
        return Oname;
    }

    public String getComment() {
        return Comment;
    }
}
