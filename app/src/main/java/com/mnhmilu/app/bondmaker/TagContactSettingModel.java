package com.mnhmilu.app.bondmaker;

public class TagContactSettingModel {

    //    db.execSQL("CREATE TABLE IF NOT EXISTS contact_tags_settings ( contact_tags_settings_id
    // INTEGER PRIMARY KEY,tag_id INTEGER,days INT NOT NULL,FOREIGN KEY(tag_setting_id) REFERENCES tags(tag_id))");

    private int tag_id;

    private int contact_tags_settings_id;

    private int days;

    private  String identity;



    public TagContactSettingModel() {
    }

    public TagContactSettingModel(int tag_id, int contact_tags_settings_id, int days, String identity) {
        this.tag_id = tag_id;
        this.contact_tags_settings_id = contact_tags_settings_id;
        this.days = days;
        this.identity = identity;
    }


    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public int getContact_tags_settings_id() {
        return contact_tags_settings_id;
    }

    public void setContact_tags_settings_id(int contact_tags_settings_id) {
        this.contact_tags_settings_id = contact_tags_settings_id;
    }

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }


}
