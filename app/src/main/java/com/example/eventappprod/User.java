package com.example.eventappprod;
import java.io.Serializable;

public class User implements Serializable{
    // mandatory fields for an user created
    private String UserName;
    private String Email;
    private String Password;
    private String Biointro;

    // optinal field, user will go to profile to edit
    private String Image;
    private String College;
    private String Group;

    // filled during user's action on app
    private String FavoriteEvent;
    private String PastEvent;
    private String NotifiedEvent;
    private String FriendList;



    public User(String name, String email, String pass, String bio,
                String ima, String col, String group,
                String favorite, String past, String notified, String friendlist) {
        UserName = name;
        Email = email;
        Password = pass;
        Biointro = bio;
        Image = ima;
        College  = col;
        Group = group;
        FavoriteEvent = favorite;
        PastEvent = past;
        NotifiedEvent = notified;
        FriendList  = friendlist;
    }

    public String getName(){
        return UserName;
    }
    public void setName(String n){
        UserName = n;
    }

    public String getEmail(){
        return Email;
    }
    public void setEmail(String e){
        Email = e;
    }

    public String getPassword(){ return Password; }
    public void setPassword(String p){ Password = p; }

    public String getBiointro(){ return Biointro; }
    public void setBiointro(String bio){ Biointro = bio; }

    public String getImage(){ return Image; }
    public void setImage(String i){ Image = i; }

    public String getCollege(){ return College; }
    public void setCollege(String col){ College =col; }

    public String getGroup(){
        return Group;
    }
    public void setGroup(String g){ Group = g; }

    public String getFavoriteEvent(){ return FavoriteEvent; }
    public void addFavoriteEvent(String favo){ FavoriteEvent = FavoriteEvent.concat(favo+ "$"); // use '$' to parse the event since
        // Real-time Firebase database can store data under String objects only
    }

    public String getPastEvent(){ return PastEvent; }
    public void addPastEvent(String past){
        PastEvent = PastEvent.concat(past+ "$"); // use '$' to parse the event since
        // Real-time Firebase database can store data under String objects only
    }

    public String getNotifiedEvent(){ return NotifiedEvent; }
    public void addNotifiedEvent(String noti){
        NotifiedEvent = NotifiedEvent.concat(noti+ "$");// use '$' to parse the events since
        // Real-time Firebase database can store data under String objects only
    }

    public String getFriendList(){ return FriendList; }
    public void addFriend(String f){
        FriendList = FriendList.concat(f+ "$");// use '$' to parse the friends since
        // Real-time Firebase database can store data under String objects only
    }


}
