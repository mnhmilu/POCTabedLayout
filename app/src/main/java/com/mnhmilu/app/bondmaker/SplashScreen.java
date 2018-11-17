package com.mnhmilu.app.bondmaker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mnhmilu.app.bondmaker.entity.ContactModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    TextView textView;
    private ArrayList<ContactModel> cantacts;
    SQLiteDatabaseHandlerForStoreContacts db;

    private TextView progressView;

    private  boolean isFirstTimeLoad=false;

    public static final String PREFERENCES_FILE_NAME_LASTSYNC = "bondmakerprefrerencelastsync";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int permission_all = 1;
        db = new SQLiteDatabaseHandlerForStoreContacts(this);

        String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG};

        if (!hasPermission(this.getBaseContext(), permissions)) {
            ActivityCompat.requestPermissions(this, permissions, permission_all);
            isFirstTimeLoad=true;
        }else
        {
            isFirstTimeLoad=false;
           // loadMainActivity();
            textView =(TextView) findViewById(R.id.textViewStatus);
            setContentView(R.layout.activity_splash_screen);
            new PrepareApplicationDataAsycTask().execute(textView);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
      //  Log.i(TAG, "onRequestPermissionResult");
        loadMainActivity(requestCode, grantResults);
    }

    private void loadMainActivity(int requestCode, @NonNull int[] grantResults) {
        int permission_all = 1;
        if (requestCode == permission_all) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
              //  Log.i(TAG, "User interaction was cancelled.");
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Please give necessary permission to run this application!",
                        Toast.LENGTH_SHORT);

                toast.show();
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted. Kick off the process of building and connecting

                // prepareContactStore();

                textView =(TextView) findViewById(R.id.textViewStatus);

                new PrepareApplicationDataAsycTask().execute(textView);
              //  task.execute();

               // loadMainActivity();

            }
            else {
                // Permission denied.

                              Toast toast = Toast.makeText(getApplicationContext(),
                        "Please give permission to run this application!",
                        Toast.LENGTH_SHORT);

                toast.show();
            }
        }
    }


    class PrepareApplicationDataAsycTask extends AsyncTask<TextView, String, Boolean> {

        int count;

        @Override
        protected void onPreExecute() {
             count = 0;
           // progressView.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(TextView... textViews) {

            Boolean returnValue = false;

            if (textViews.length > 0) {

                cantacts= new ArrayList<>();

                publishProgress("Getting your coantact data");

                if(isFirstTimeLoad) {

                    storeContactToDatabaseForFirstTime();

                    updateContactwithCallLogs();

                }
                else { // it not the first time , only update the call history


                  // updateContactwithCallLogs();
                   //TODO: Sync the contact for new contact entry


                }

                /*List<ContactModel> dblist = db.getAllContactsModels();

                for(ContactModel entity:dblist)
                {
                    Log.d("Database>>>>>>>>", " number:"+entity.getNumber() + " tags:" + entity.getContact_tag() + "  name:" + entity.getName() +" days"+entity.getDayElapsed());
                }*/

            }
            returnValue = true;
            //}
            return returnValue;
            //  return "Task done,All item are populated in the list";
        }


        private void storeContactToDatabaseForFirstTime()
        {
            Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.RawContacts.ACCOUNT_TYPE + "=?", new String[]{"com.google"}, "UPPER(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");

            while (phones.moveToNext()) {
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String identity = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));
                ContactModel contactModel = new ContactModel();
                contactModel.setName(name);
                contactModel.setNumber(phoneNumber.replace(" ", "").replace("-", ""));
                contactModel.setIdentity(identity);
                contactModel.setContact_tag("");
               // contactModel.setDayElapsed(-1);
                db.addContact(contactModel); // saving to database
            }
            phones.close();
        }


        private void updateContactwithCallLogs()
        {
             cantacts =db.getAllContactsModels();

            for (ContactModel item : cantacts) {
              //  counter++;
                /////
                Cursor cursorLastCall = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                        new String[]{CallLog.Calls.DATE, CallLog.Calls.DURATION,
                                CallLog.Calls.NUMBER, CallLog.Calls._ID, CallLog.Calls.TYPE},
                        CallLog.Calls.NUMBER + "=?",
                        new String[]{item.getNumber()},
                        CallLog.Calls.DATE + " DESC limit 1");


                if (cursorLastCall != null && cursorLastCall.getCount() > 0) {

                    cursorLastCall.moveToLast();
                    int callType = cursorLastCall.getColumnIndex(CallLog.Calls.TYPE);
                    String callTypeString = cursorLastCall.getString(callType);

                    int dircode = Integer.parseInt(callTypeString);
                    if (dircode == CallLog.Calls.INCOMING_TYPE) {
                        item.setCallType("Incoming");
                    } else if (dircode == CallLog.Calls.OUTGOING_TYPE) {
                        item.setCallType("Outgoing");
                    } else if (dircode == CallLog.Calls.MISSED_TYPE) {
                        item.setCallType("Missed");
                    } else {
                        item.setCallType("Others");
                    }

                    int date = cursorLastCall.getColumnIndex(CallLog.Calls.DATE);
                    //  String testDate = cursorLastCall.getString(cursorLastCall.getColumnIndex(CallLog.Calls.DATE));

                    String callDate = cursorLastCall.getString(date);
                    Date callDayTime = new Date(Long.valueOf(callDate));

                    DateFormat dt = android.text.format.DateFormat.getDateFormat(getApplicationContext());
                    String formattedDate = dt.format(callDayTime);

                    item.setLastCallDate(formattedDate);


                    long diff = new Date().getTime() - callDayTime.getTime();
                    long seconds = diff / 1000;
                    long minutes = seconds / 60;
                    long hours = minutes / 60;
                    long days = hours / 24;

                    long l = days;
                    int i = (int) l;

                    item.setDayElapsed(i);

                    Log.d("Last Call date>>", item.getNumber() + "  " + dt.format(callDayTime) + "  Month: " + callDayTime.getMonth() + " days" + item.getDayElapsed());


                } else {
                    item.setContact_tag("Never Called");
                }

                db.updateContactModelByIdentity(item); //only update

               // ContactModel entity = db.getContactbyContactIdentity(item.getIdentity());
                //Log.d("Debug day elapsed>>", " dayelapsed "+entity.getDayElapsed());

                cursorLastCall.close();

            }
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // super.onProgressUpdate(values);
            count++;
          //  progressView.setText(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean isDone) {

            if (isDone) {

                loadMainActivity();

            }
        }
    }



    private void loadMainActivity() {


        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);

    /*  //  textView=(TextView)  findViewById(R.id.versionName) ;

        //textView.setText(versionName);


        Thread myThread = new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        myThread.start();*/
    }


    public static boolean hasPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
                        ) {
                    return false;
                }
            }
            // customAdapterLastCall.notifyDataSetChanged();
        }
        return true;
    }
}
