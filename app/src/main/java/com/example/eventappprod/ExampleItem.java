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
    private String mCreator;


    public ExampleItem(String cardName, String cardStartTime, String cardEndTime, String cardDate, String cardCreator, String imgfirestore){
        mName = cardName;
        mStartTime = cardStartTime;
        mEndTime = cardEndTime;
        mDate = cardDate;
        img_firestore=imgfirestore;
        mCreator = cardCreator;
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

    public String getCreator() { return mCreator; }

    public String getImg_firestore(){
        return  img_firestore;
    }
}
