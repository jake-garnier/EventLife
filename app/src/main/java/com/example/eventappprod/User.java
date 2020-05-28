package com.example.eventappprod;
import java.io.Serializable;

public class User implements Serializable{
    // mandatory fields for an user created

    private String Name;
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

    public User() {

    }


    public User(String aname, String aemail, String apass, String abio,
                String aima, String acol, String agroup,
                String afavorite, String apast, String anotified, String afriendlist) {
        Name = aname;
        Email = aemail;
        Password = apass;
        Biointro = abio;
        Image = aima;
        College  = acol;
        Group = agroup;
        FavoriteEvent = afavorite;
        PastEvent = apast;
        NotifiedEvent = anotified;
        FriendList  = afriendlist;
        //numberofFriend =0;
    }

    public String getName(){
        return Name;
    }
    public void setName(String n){
        Name = n;
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

    public void removeEvent(String e){
        FavoriteEvent = FavoriteEvent.replace(e, "");
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
        //numberofFriend++;
    }

    public void removeFriend(String f){
        FriendList = FriendList.replace(f+"$", "");
        // numberofFriend--;
    }
}
