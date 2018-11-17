package com.mnhmilu.app.bondmaker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.mnhmilu.app.bondmaker.entity.TagContactSettingModel;
import com.mnhmilu.app.bondmaker.entity.TagModel;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

        // All Static variables
        // Database Version
        private static final int DATABASE_VERSION = 1;

        // Database Name
        private static final String DATABASE_NAME = "KeepInTouchData";

        // tags table name
        private static final String TABLE_TAGS = "tags";

        // tags Table Columns names
        private static final String KEY_ID = "tag_id";
        private static final String TAG_NAME = "tag_name";


        //taga_contact_settings
        // tags table name
        private static final String TABLE_TAGS_CONTACT_SETTINGS = "contact_tags_settings";

       // tags Table Columns names

        private static final String KEY_ID_FK = "tag_id_fk";
        private static final String KEY_ID_TAGS_CONTACT_SETTINGS = "contact_tags_settings_id";
        private static final String DAYS = "days";
        private static  final  String CONTACT_IDENTITY="contact_identity";


    //    db.execSQL("CREATE TABLE IF NOT EXISTS contact_tags_settings ( contact_tags_settings_id
        // INTEGER PRIMARY KEY,tag_id INTEGER,days INT NOT NULL,FOREIGN KEY(tag_setting_id) REFERENCES tags(tag_id))");


        public SQLiteDatabaseHandler(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        // Creating Tables
        @Override
        public void onCreate(SQLiteDatabase db) {

            String CREATE_TAG_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TAGS + "("
                    + KEY_ID + " INTEGER PRIMARY KEY," + TAG_NAME + " TEXT" + ")";
            db.execSQL(CREATE_TAG_TABLE);

            String CREATE_TAG_CONTACT_SETTINGS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_TAGS_CONTACT_SETTINGS + "("
                    + KEY_ID_TAGS_CONTACT_SETTINGS
                    + " INTEGER PRIMARY KEY,"
                    + DAYS + " INTEGER,"
                    + KEY_ID_FK + " INTEGER,"
                    + CONTACT_IDENTITY+ " VARCHAR,"
                    +" FOREIGN KEY("+KEY_ID_FK+") REFERENCES "+TABLE_TAGS+"("+KEY_ID+")"
                    + ")";

            db.execSQL(CREATE_TAG_CONTACT_SETTINGS_TABLE);

        }

        // Upgrading database
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TAGS_CONTACT_SETTINGS);

            // Create tables again
            onCreate(db);
        }

        /**
         * All CRUD(Create, Read, Update, Delete) Operations
         */

        // Adding new country
        void addTag(TagModel entity) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put(TAG_NAME, entity.getTag_name()); // tag Name

                      // Inserting Row
            db.insert(TABLE_TAGS, null, values);
            db.close(); // Closing database connection
        }


        void addTagSettings(TagContactSettingModel entity) {
           SQLiteDatabase db = this.getWritableDatabase();
           ContentValues values = new ContentValues();
           values.put(KEY_ID, entity.getTag_id()); // tag Name
           values.put(KEY_ID_TAGS_CONTACT_SETTINGS, entity.getContact_tags_settings_id()); // tag Name
           values.put(DAYS, entity.getDays()); // tag Name
            values.put(CONTACT_IDENTITY,entity.getIdentity());

          // Inserting Row
          db.insert(TABLE_TAGS_CONTACT_SETTINGS, null, values);
          db.close(); // Closing database connection
    }


        // Getting single
        TagModel getTag(int id) {
            SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.query(TABLE_TAGS, new String[] { KEY_ID,
                            TAG_NAME }, KEY_ID + "=?",
                    new String[] { String.valueOf(id) }, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();

            TagModel tagmodel = new TagModel(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1));
            // return country
            return tagmodel;
        }

    TagContactSettingModel getTagContactSettingModelbyIdentity(String identity) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TAGS_CONTACT_SETTINGS, new String[] { CONTACT_IDENTITY }, CONTACT_IDENTITY + "=?",
                new String[] { String.valueOf(identity) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        TagContactSettingModel model = new TagContactSettingModel(Integer.parseInt(cursor.getString(0)),
                                                                  Integer.parseInt(cursor.getString(1)),
                                                                  Integer.parseInt(cursor.getString(2)),
                                                                  cursor.getString(3));

        return model;
    }




    // Getting All Countries
        public List<TagModel> getAllTagModels() {
            List<TagModel> tagmodels = new ArrayList<TagModel>();
            // Select All Query
            String selectQuery = "SELECT  * FROM " + TABLE_TAGS;

            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    TagModel tagModel = new TagModel();
                    tagModel.setTag_id(Integer.parseInt(cursor.getString(0)));
                    tagModel.setTag_name(cursor.getString(1));
                    tagmodels.add(tagModel);
                } while (cursor.moveToNext());
            }

            return tagmodels;
        }

        // Updating single country
        public int updateTagModel(TagModel tagModel) {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(TAG_NAME, tagModel.getTag_name());
           // values.put(POPULATION, tagModel.getPopulation());

            // updating row
            return db.update(TABLE_TAGS, values, KEY_ID + " = ?",
                    new String[] { String.valueOf(tagModel.getTag_id()) });
        }

    public int updateTagContactSettingsModel(TagContactSettingModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DAYS, model.getDays());
        // values.put(POPULATION, tagModel.getPopulation());

        // updating row
        return db.update(TABLE_TAGS_CONTACT_SETTINGS, values, CONTACT_IDENTITY + " = ?",
                new String[] { String.valueOf(model.getIdentity()) });
    }

    // Deleting single country
        public void deleteTagModel(TagModel tagModel) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_TAGS, KEY_ID + " = ?",
                    new String[] { String.valueOf(tagModel.getTag_id()) });
            db.close();
        }

    public void deleteTagContactSettingModel(TagContactSettingModel tagContactSettingModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TAGS_CONTACT_SETTINGS, CONTACT_IDENTITY + " = ?",
                new String[] { String.valueOf(tagContactSettingModel.getIdentity()) });
        db.close();
    }


    // Deleting all countries
        public void deleteAllTagModels() {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_TAGS,null,null);
            db.close();
        }

        // Getting countries Count
        public int getTagModelCount() {
            String countQuery = "SELECT  * FROM " + TABLE_TAGS;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            cursor.close();

            // return count
            return cursor.getCount();
        }


}
