package com.example.eventappprod;

import android.media.Image;
import android.net.Uri;

public class ExampleItem {
    private int mImageResource;
    //private Uri mImageResource;
    //private Image mImageResource;
    private String mText1;
    private String mText2;
    private String img_firestore;


    public ExampleItem(int imageResource, String text1, String text2,String imgfirestore){
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        img_firestore=imgfirestore;
    }

    public int getImageResource(){
        return mImageResource;
    }

    public String getText1(){
        return mText1;
    }

    public String getText2(){
        return mText2;
    }

    public String getImg_firestore2(){
        return  img_firestore;
    }
}
