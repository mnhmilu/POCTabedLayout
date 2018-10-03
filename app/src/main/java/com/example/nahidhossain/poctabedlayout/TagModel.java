package com.example.nahidhossain.poctabedlayout;

import android.nfc.Tag;

public class TagModel {


    public TagModel()
    {

    }
    public  TagModel(String tag_name)
    {

        this.tag_name=tag_name;
    }


    public  TagModel(int tag_id,String tag_name)
    {
        this.tag_id=tag_id;
        this.tag_name=tag_name;
    }

    private int tag_id;

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    private String tag_name;

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }
}
