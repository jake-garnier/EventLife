package com.example.eventappprod;
import java.io.Serializable;

public class User implements Serializable{
    // mandatory fields for an user created

    private static User instance;

    private String name;
    private String email;
    private String password;
    private String biointro;
    private String userId;

    // optinal field, user will go to profile to edit
    private String profileImage;
    private String backgroundImage;


    // filled during user's action on app
    private String rsvpevents;
    private String createdEvents;
    private String friendList;

    public User() {
        name = "";
        email =  "";
        password = "";
        biointro =  "";
        profileImage =  "";
        backgroundImage =  "";
        rsvpevents =  "";
        createdEvents =  "";
        friendList  =  "";
        userId = "";

    }


    public User(String aname, String aemail, String userID, String apass, String abio,
                String aima, String back, String acol, String agroup,
                String arsvp, String acreated, String anotified, String afriendlist) {
        name = aname;
        email = aemail;
        userId = "";
        password = apass;
        biointro = abio;
        profileImage = aima;
        backgroundImage = back;
        rsvpevents = arsvp;
        createdEvents = acreated;
        friendList  = afriendlist;
    }

    public static synchronized User getInstance(){
        if(instance==null){
            instance=new User();
        }
        return instance;
    }

    public User copy(User another){
        name = another.name;
        email = another.email;
        userId = another.userId;
        password = another.password;
        biointro = another.biointro;
        profileImage = another.profileImage;
        backgroundImage = another.backgroundImage;
        rsvpevents = another.rsvpevents;
        createdEvents = another.createdEvents;
        friendList  = another.friendList ;
        return instance;
    }



    public String getName(){
        return name;
    }
    public void setName(String n){
        name = n;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail(String e){
        email = e;
    }

    public String getUserId(){return userId;}
    public void setUserId(String set){userId = set;}

    public String getPassword(){ return password; }
    public void setPassword(String p){ password = p; }

    public String getBiointro(){ return biointro; }
    public void setBiointro(String bio){ biointro = bio; }

    public String getProfileImage(){ return profileImage; }
    public void setProfileImage(String i){ profileImage = i; }

    public String getBackgroundImage(){ return backgroundImage; }
    public void setBackgroundImage(String i){ backgroundImage = i; }


    public String getCreatedEvents(){ return createdEvents; }
    public void addCreatedEvents(String favo){ createdEvents = createdEvents.concat(favo+ "$"); // use '$' to parse the event since
        // Real-time Firebase database can store data under String objects only
    }

    /*public void removeEvent(String e){
        FavoriteEvent = FavoriteEvent.replace(e, "");
    }*/

    public String getRSVPEvents(){ return rsvpevents; }
    public void addRSVPEvent(String past){
        rsvpevents = past + "," + rsvpevents; // use '$' to parse the event since
        // Real-time Firebase database can store data under String objects only
    }
    public void removeRSVPEvent(String past){
        rsvpevents = rsvpevents.replace(past+",", ""); // use '$' to parse the event since
        // Real-time Firebase database can store data under String objects only
    }

    public String getFriendList(){ return friendList; }
    public void addFriend(String f){
       friendList = f + "," + friendList;// use '$' to parse the friends since
        // Real-time Firebase database can store data under String objects only
        //numberofFriend++;
    }

    public void removeFriend(String f){
       friendList = friendList.replace(f+",", "");
        // numberofFriend--;
    }
}
