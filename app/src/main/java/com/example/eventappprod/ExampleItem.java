package com.example.eventappprod;

public class ExampleItem {
//    private int mImageResource;
    //private Uri mImageResource;
    //private Image mImageResource;
    private String mName;
    private String mStartTime;
    private String mEndTime;
    private String mDate;
    private String img_firestore;


    public ExampleItem(String cardName, String cardStartTime, String cardEndTime, String cardDate, String imgfirestore){
        mName = cardName;
        mStartTime = cardStartTime;
        mEndTime = cardEndTime;
        mDate = cardDate;
        img_firestore=imgfirestore;
    }

//    public int getImageResource(){
//        return mImageResource;
//    }

    public String getName(){
        return mName;
    }

    public String getStartTime(){
        return mStartTime;
    }

    public String getEndTime() { return mEndTime; }

    public String getDate() { return mDate; }

    public String getImg_firestore(){
        return  img_firestore;
    }
}
