package com.example.eventappprod;

import java.util.ArrayList;

public class Event {
    private String ID;
    private String Name;

    private String Owner;
    private String Date;
    private String Location;

    private String StartTime;
    private String EndTime;
    private String Tag;
    private String Description;

    private String Image;
    public String userGoing;
    public String userMaybeGoing;
    public String userNotGoing;

    public Event() {

    }

    public Event(String id,String name,String owner, String date, String loca,
                 String stime, String etime, String tag, String des,
                 String usergo, String usermaybe, String usernot) {
        // mandatory fields for an event object
        ID = id;
        Name = name;
        Owner = owner;
        Date = date;
        Location = loca;
        Tag =tag;
        StartTime =stime;
        EndTime = etime;
        Description = des;

        // field filled by user's action
        userGoing = usergo;
        userMaybeGoing = usermaybe;
        userNotGoing = usernot;
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

    public String getOwner(){
        return Owner;
    }
    public void setOwner(String o){Owner= o; }

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

    public String getUserGoing(){
        return userGoing;
    }
    public void addUserGoing(String g){
        userGoing = userGoing.concat(g+ "$");// use '$' to parse the users since
        // Real-time Firebase database can store data under String objects only
    }

    public String getUserMaybeGoing(){
        return userMaybeGoing;
    }
    public void addUserMaybeGoing(String m){
        userMaybeGoing = userMaybeGoing.concat(m+ "$");// use '$' to parse the users since
        // Real-time Firebase database can store data under String objects only
    }

    public String getUserNotGoing(){
        return userNotGoing;
    }
    public void addUserNotGoing(String n){
        userNotGoing = userNotGoing.concat(n+ "$");// use '$' to parse the users since
        // Real-time Firebase database can store data under String objects only
    }

}
