package com.mnhmilu.app.bondmaker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mnhmilu.app.bondmaker.entity.ContactModel;

import java.util.ArrayList;

public class AlertLevelSettingsActivity extends AppCompatActivity {

    private EditText editText1;
    ImageButton btnSubmit;
    Button btnSyncContact;
    public static final String PREFERENCES_FILE_NAME = "bondmakerprefrerence";

    ProgressBar progressBarSettings;

    SQLiteDatabaseHandlerForStoreContacts db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_level_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        editText1 =(EditText) findViewById(R.id.tbxAlert1);

        btnSubmit = (ImageButton) findViewById(R.id.btnSubmitAlertLevel);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });


        btnSyncContact=(Button) findViewById(R.id.btnSyncContact);

        btnSyncContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               new GetContactsAsycTask().execute();
            }
        });

        progressBarSettings=(ProgressBar) findViewById(R.id.progressBarSettings);
        progressBarSettings.setVisibility(View.INVISIBLE);
        //  saveData();
        
        loadData();

    }


    class GetContactsAsycTask extends AsyncTask<TextView, String, Boolean> {

        int count;

        @Override
        protected void onPreExecute() {

            count = 0;

            progressBarSettings.setVisibility(View.VISIBLE);

        }

        @Override
        protected Boolean doInBackground(TextView... textViews) {


            progressBarSettings.setProgress(10);

            syncContactAndCallLog();


            return true;
            //  return "Task done,All item are populated in the list";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // super.onProgressUpdate(values);
            count++;

        }

        @Override
        protected void onPostExecute(Boolean isDone) {

            if (isDone) {

                progressBarSettings.setVisibility(View.GONE);

            }

        }
    }



    private void syncContactAndCallLog()
    {
        progressBarSettings.setProgress(5);
        progressBarSettings.setVisibility(View.VISIBLE);

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.RawContacts.ACCOUNT_TYPE + "=?", new String[]{"com.google"}, "UPPER(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");

        int count=0;

        db = new SQLiteDatabaseHandlerForStoreContacts(this);

        while (phones.moveToNext()) {

            count++;

            progressBarSettings.setProgress((100*count)/phones.getCount());

            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String identity = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));


          ContactModel entity=  db.getContactbyContactIdentity(identity);

          if(entity==null)
          {
            ContactModel contactModel = new ContactModel();
            contactModel.setName(name);
            contactModel.setNumber(phoneNumber.replace(" ", "").replace("-", ""));
            contactModel.setIdentity(identity);
            contactModel.setContact_tag("");
            db.addContact(contactModel); // saving to database
              Log.d("DebugSetings>>","Adding new contact");

         }
         else {
              entity.setName(name);
              entity.setNumber(phoneNumber.replace(" ", "").replace("-", ""));
              db.updateContactModelByIdentity(entity);
              Log.d("DebugSetings>>","Updating contact");
          }




        }
        phones.close();


    }


    private void saveData() {

        SharedPreferences settingsfile= getSharedPreferences(PREFERENCES_FILE_NAME,0);

        SharedPreferences.Editor myeditor = settingsfile.edit();
        myeditor.putInt(getString(R.string.alarm_first_level),Integer.valueOf(editText1.getText().toString()));
      //  myeditor.putInt(getString(R.string.alarm_second_level),Integer.valueOf(editText2.getText().toString()));

        myeditor.apply();
        super.onBackPressed();
        this.finish();
    }

    public void loadData()
    {

        SharedPreferences mysettings= getSharedPreferences(PREFERENCES_FILE_NAME, 0);

        int firstLevel = mysettings.getInt(getString(R.string.alarm_first_level),10);
       // int secondLevel = mysettings.getInt(getString(R.string.alarm_second_level),10);

        editText1.setText(String.valueOf(firstLevel));
      //  editText2.setText(String.valueOf(secondLevel));

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
