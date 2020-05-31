package com.example.eventappprod;
import java.io.Serializable;

public class User implements Serializable{
    // mandatory fields for an user created

    private static User instance;

    private String Name;
    private String Email;
    private String Password;
    private String Biointro;
    private String UserId;

    // optinal field, user will go to profile to edit
    private String ProfileImage;
    private String BackgroundImage;

    // filled during user's action on app
    private String rsvpevents;
    private String createdEvents;
    private String friendList;

    public User() {
        Name = "";
        Email =  "";
        Password = "";
        Biointro =  "";
        ProfileImage =  "";
        BackgroundImage =  "";
        rsvpevents =  "";
        createdEvents =  "";
        friendList  =  "";
        UserId = "";

    }


    public User(String aname, String aemail, String userID, String apass, String abio,
                String aima, String back, String acol, String agroup,
                String arsvp, String acreated, String anotified, String afriendlist) {
        Name = aname;
        Email = aemail;
        UserId = "";
        Password = apass;
        Biointro = abio;
        ProfileImage = aima;
        BackgroundImage = back;
        rsvpevents = arsvp;
        createdEvents = acreated;
        friendList  = afriendlist;
        //numberofFriend =0;
    }

    public static synchronized User getInstance(){
        if(instance==null){
            instance=new User();
        }
        return instance;
    }

    public User copy(User another){
        Name = another.Name;
        Email = another.Email;
        UserId = another.UserId;
        Password = another.Password;
        Biointro = another.Biointro;
        ProfileImage = another.ProfileImage;
        BackgroundImage = another.BackgroundImage;
        rsvpevents = another. rsvpevents;
        createdEvents = another.createdEvents;
        friendList  = another.friendList ;
        return instance;
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

    public String getUserId(){return UserId;}
    public void setUserId(String set){UserId = set;}

    public String getPassword(){ return Password; }
    public void setPassword(String p){ Password = p; }

    public String getBiointro(){ return Biointro; }
    public void setBiointro(String bio){ Biointro = bio; }

    public String getProfileImage(){ return ProfileImage; }
    public void setProfileImage(String i){ ProfileImage = i; }

    public String getBackgroundImage(){ return BackgroundImage; }
    public void setBackgroundImage(String i){ BackgroundImage = i; }

    public String getCreatedEvents(){ return createdEvents; }
    public void addCreatedEvents(String favo){ createdEvents = favo + ", " + createdEvents; // use '$' to parse the event since
        // Real-time Firebase database can store data under String objects only
    }

    /*public void removeEvent(String e){
        FavoriteEvent = FavoriteEvent.replace(e, "");
    }*/

    public String getRSVPEvents(){ return rsvpevents; }
    public void addRSVPEvent(String past){
        rsvpevents = past + "," + rsvpevents;
                //RSVPEvents.concat(past+ ","); // use '$' to parse the event since
        // Real-time Firebase database can store data under String objects only
    }


    public String getFriendList(){ return friendList; }
    public void addFriend(String f){
       friendList = friendList + "," + f;// use '$' to parse the friends since
        // Real-time Firebase database can store data under String objects only
        //numberofFriend++;
    }

    public void removeFriend(String f){
       friendList = friendList.replace(f+",", "");
        // numberofFriend--;
    }
}
