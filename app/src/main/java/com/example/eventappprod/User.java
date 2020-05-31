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
    private String College;
    private String Group;

    // filled during user's action on app
    private String RSVPEvents;
    private String CreatedEvents;
    private String NotifiedEvent;
    private String friendList;

    public User() {
        Name = "";
        Email =  "";
        Password = "";
        Biointro =  "";
        ProfileImage =  "";
        BackgroundImage =  "";
        College  =  "";
        Group =  "";
        RSVPEvents =  "";
        CreatedEvents =  "";
        NotifiedEvent =  "";
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
        College  = acol;
        Group = agroup;
        RSVPEvents = arsvp;
        CreatedEvents = acreated;
        NotifiedEvent = anotified;
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
        College  = another.College;
        Group = another.Group;
        RSVPEvents = another. RSVPEvents;
        CreatedEvents = another.CreatedEvents;
        NotifiedEvent = another.NotifiedEvent;
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

    public String getCollege(){ return College; }
    public void setCollege(String col){ College =col; }

    public String getGroup(){
        return Group;
    }
    public void setGroup(String g){ Group = g; }

    public String getCreatedEvents(){ return CreatedEvents; }
    public void addCreatedEvents(String favo){ CreatedEvents = CreatedEvents.concat(favo+ "$"); // use '$' to parse the event since
        // Real-time Firebase database can store data under String objects only
    }

    /*public void removeEvent(String e){
        FavoriteEvent = FavoriteEvent.replace(e, "");
    }*/

    public String getRSVPEvents(){ return RSVPEvents; }
    public void addRSVPEvent(String past){
        RSVPEvents = RSVPEvents.concat(past+ ","); // use '$' to parse the event since
        // Real-time Firebase database can store data under String objects only
    }

    public String getNotifiedEvent(){ return NotifiedEvent; }
    public void addNotifiedEvent(String noti){
        NotifiedEvent = NotifiedEvent.concat(noti+ ",");// use '$' to parse the events since
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
