package com.mnhmilu.app.bondmaker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Spinner;

import com.mnhmilu.app.bondmaker.entity.ContactModel;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHandlerForStoreContacts extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "BondMakerData";

    // tags table name
    private static final String TABLE_BONDMAKER_DATA = "bondmakerdata";

    // tags Table Columns names
    private static final String KEY_ID = "id";
    private static final String CONTACT_NAME = "contact_name";
    private static final String CONTACT_NUMBER = "contact_number";
    private static final String CONTACT_TAG = "contact_tag";
    private static final String CONTACT_IDENTITY = "contact_identity";
    private static final String CALL_TYPE = "call_type";
    private static final String DAY_ELAPSED = "day_elapsed";
    private static final String LAST_CALL_DATETIME = "last_call_date";
    private static final String ACCOUNT_TYPE = "account_type";
    //private String name, number,identity,accountType,callType,contact_tag;

    // private Date lastCallDate;

    // private int dayElapsed;


    public SQLiteDatabaseHandlerForStoreContacts(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTACT_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_BONDMAKER_DATA
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + CONTACT_NAME + " TEXT,"
                + CONTACT_NUMBER + " TEXT,"
                + CONTACT_IDENTITY + " TEXT,"
                + CONTACT_TAG + " TEXT,"
                + CALL_TYPE + " TEXT,"
                + LAST_CALL_DATETIME + " TEXT,"
                + DAY_ELAPSED + " INTEGER"
                + ")";
        db.execSQL(CREATE_CONTACT_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BONDMAKER_DATA);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new country
    void addContact(ContactModel entity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, entity.getName());
        values.put(CONTACT_NUMBER, entity.getNumber());
        values.put(CONTACT_TAG, entity.getContact_tag());
        values.put(CONTACT_IDENTITY, entity.getIdentity());
        values.put(CALL_TYPE, entity.getCallType());
        values.put(DAY_ELAPSED, entity.getDayElapsed());
        if (entity.getLastCallDate() != null) {
            values.put(LAST_CALL_DATETIME, entity.getLastCallDate().toString());
        }
        // Inserting Row
        db.insert(TABLE_BONDMAKER_DATA, null, values);
        db.close(); // Closing database connection
    }



    ContactModel getContactbyContactNumber(String number) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_BONDMAKER_DATA, new String[]{
                        KEY_ID,
                        CONTACT_NAME,
                        CONTACT_NUMBER,
                        CONTACT_IDENTITY,
                        CONTACT_TAG,
                        CALL_TYPE,
                        LAST_CALL_DATETIME,
                        DAY_ELAPSED}, CONTACT_NUMBER + "=?",
                new String[]{number}, null, null, null, null);
        ContactModel model = null;
        if (cursor != null && cursor.moveToFirst()) {

            model = new ContactModel();
            model.setId(Integer.parseInt(cursor.getString(0)));
            model.setName(cursor.getString(1));//name
            model.setNumber(cursor.getString(2));//number
            model.setIdentity(cursor.getString(3));//identity
            model.setContact_tag(cursor.getString(4));//contact_tag
            model.setCallType(cursor.getString(5));//calltype
            model.setLastCallDate(cursor.getString(6));//lastCallDate
            model.setDayElapsed(cursor.getInt(7));// dayElapsed

        }

        Log.d("Debug>>>>>>", "Call Type: " + model.getCallType() + " tag" + model.getContact_tag());

        return model;
    }

    public ArrayList<ContactModel> getAllContactsModels() {
        ArrayList<ContactModel> models = new ArrayList<ContactModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BONDMAKER_DATA + " WHERE contact_tag!='Never Called'";
        ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactModel model = new ContactModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setName(cursor.getString(1));//name
                model.setNumber(cursor.getString(2));//number
                model.setIdentity(cursor.getString(3));//identity
                model.setContact_tag(cursor.getString(4));//contact_tag
                model.setCallType(cursor.getString(5));//calltype
                model.setLastCallDate(cursor.getString(6));//lastcallDate
                model.setDayElapsed(cursor.getInt(7));//dayelapshes

                models.add(model);
            } while (cursor.moveToNext());
        }

        return models;
    }

    public ArrayList<ContactModel> getContactsModelsByTag(String tag) {
        ArrayList<ContactModel> models = new ArrayList<ContactModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BONDMAKER_DATA + " WHERE contact_tag='" + tag.trim() + "'";

        Log.d(">>>>Query", selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContactModel model = new ContactModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setName(cursor.getString(1));//name
                model.setNumber(cursor.getString(2));//number
                model.setIdentity(cursor.getString(3));//identity
                model.setContact_tag(cursor.getString(4));//contact_tag
                model.setCallType(cursor.getString(5));//calltype
                model.setLastCallDate(cursor.getString(6));//lastcallDate
                model.setDayElapsed(cursor.getInt(7));//dayelapshes

                models.add(model);
            } while (cursor.moveToNext());
        }

        return models;
    }

    // Updating single country
    public int updateContactModelByIdentity(ContactModel model) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, model.getName());
        values.put(CONTACT_NUMBER, model.getNumber());
        values.put(CONTACT_TAG, model.getContact_tag());
        values.put(CALL_TYPE, model.getCallType());
        values.put(LAST_CALL_DATETIME, model.getLastCallDate());
        values.put(DAY_ELAPSED, model.getDayElapsed());
        // updating row
        return db.update(TABLE_BONDMAKER_DATA, values, CONTACT_IDENTITY + " = ?",
                new String[]{String.valueOf(model.getIdentity())});
    }


    // Deleting single country
    public void deleteContactModelById(ContactModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BONDMAKER_DATA, KEY_ID + " = ?",
                new String[]{String.valueOf(model.getId())});
        db.close();
    }


    // Deleting all countries
    public void deleteAllContactModels() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BONDMAKER_DATA, null, null);
        db.close();
    }


}
