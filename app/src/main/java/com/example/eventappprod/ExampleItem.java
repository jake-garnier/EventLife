package com.example.eventappprod;

import android.media.Image;
import android.net.Uri;

public class ExampleItem {
    private int mImageResource;
    //private Uri mImageResource;
    //private Image mImageResource;
    private String mText1;
    private String mText2;
    private String mText3;
    private String mText4;
    private String img_firestore;


    public ExampleItem(int imageResource, String text1, String text2, String txt3, String txt4, String imgfirestore){
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        mText3 = txt3;
        mText4 = txt4;
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

    public String getText3(){
        return mText3;
    }

    public String getText4(){
        return mText4;
    }

    public String getImg_firestore(){
        return  img_firestore;
    }
}
