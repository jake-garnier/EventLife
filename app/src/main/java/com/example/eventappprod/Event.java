package com.example.eventappprod;

import java.util.ArrayList;

public class Event {
    private String ID;
    private String Name;
    private String Date;
    private String Location;
    private String StartTime;
    private String EndTime;
    private String Tag;
    private String Description;
    private String Image;
    public String UserGoing;
    public String UserMaybeGoing;
    public String UserNotGoing;

    public Event() {

    }

    public Event(String id, String des){
        ID=id;
        Description =des;
    }

    public Event(String id,String name, String date, String loca, String stime, String etime, String tag, String des) {
        ID = id;
        Name = name;
        Date = date;
        Location = loca;
        Tag =tag;
        StartTime =stime;
        EndTime = etime;
        Description = des;
        UserGoing = null;
        UserMaybeGoing = null;
        UserNotGoing = null;
    }
    public String getId(){
        return ID;
    }
    public void setId(String id){ ID = id; }

    public String getName(){
        return Name;
    }
    public void setName(String n){
        Name = n;
    }

    public String getDate(){
        return Date;
    }
    public void setDate(String d){
        Date = d;
    }

    public String getLocation(){
        return Location;
    }
    public void setLocation(String l){ Location = l; }

    public String getStartTime(){
        return StartTime;
    }
    public void setStartTime(String t){
        StartTime = t;
    }

    public String getEndTime(){
        return EndTime;
    }
    public void setEndTime(String t){
        EndTime = t;
    }

    public String getTag(){
        return Tag;
    }
    public void setTag(String t){
        Tag =t;
    }

    public String getDescription(){
        return Description;
    }
    public void setDescription(String d){
        Description =d;
    }

    public String getImage(){
        return Image;
    }
    public void setImage(String i){
        Image = i;
    }

}
