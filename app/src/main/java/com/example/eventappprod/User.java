package com.example.eventappprod;

public class User {
    private String UserName;
    private String Email;
    private String Password;
    //private String Image;
    private String College;
    private String Group;


    public User(String name, String email, String pass) {
        UserName = name;
        Email = email;
        Password = pass;
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

    public String getPassword(){
        return Password;
    }
    public void setPassword(String p){
        Password = p;
    }

}
