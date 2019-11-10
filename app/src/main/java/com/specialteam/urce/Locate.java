package com.specialteam.urce;

public class Locate {

    public static double latitude;
    public static double longtitude;

    public double data;

    public Locate(){}

    public Locate(double data){this.data=data;}

    public Locate(double latitude,double longtitude){
        this.latitude=latitude;
        this.longtitude=longtitude;
    }
}
