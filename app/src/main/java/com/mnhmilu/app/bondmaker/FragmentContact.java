package com.mnhmilu.app.bondmaker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link FragmentContact#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentContact extends Fragment {

    private CustomAdapter customAdapter;
    private ArrayList<ContactModel> contactModelArrayList;
    private ListView listView;
    private View mLayout;
    private static final int REQUEST_CONTACTS = 1;
    private static final int REQUEST_READ_CALLLOG = 2;

    public static final String TAG = "MainActivity";

    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS};


///////////////////

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;

    String[] NAMES = {"Name A", "Name B"};

    String[] DESCRIPTIONS = {"Description 1", "Description 2"};

    public FragmentContact() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentContact newInstance(String param1, String param2) {
        FragmentContact fragment = new FragmentContact();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        int permission_all =1;
        String[] permissions={Manifest.permission.READ_CONTACTS,Manifest.permission.READ_CALL_LOG};

        if(!hasPermission(this.getContext(),permissions))
        {
            ActivityCompat.requestPermissions(this.getActivity(),permissions,permission_all);
        }


    }



    public static boolean hasPermission(Context context, String... permissions)
    {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && context!=null && permissions!=null ){
            for(String permission:permissions)
            {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
                        ) {
                    return  false;
                }
                }
            }
            return  true;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_frag2, container, false);
        mLayout = rootView.findViewById(R.id.main_content);

      //  checkPermission();

        return rootView;
    }

/*
    private void checkPermission() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED
                ) {
            // Contacts permissions have not been granted.
            Log.i("MainActivity", "Contact permissions has NOT been granted. Requesting permissions.");
            requestContactsPermissions();
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED
                ) {
            // Contacts permissions have not been granted.
            Log.i("MainActivity", "Contact permissions has NOT been granted. Requesting permissions.");
            requestCallLogPermissions();
        }


    }
    */

    @Override
    public void onResume() {

        //checkPermission();

        super.onResume();
        try {
            getContracts();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void requestContactsPermissions() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                Manifest.permission.READ_CONTACTS)
                ) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For mnhmilu, if the request has been denied previously.
            Log.i("xxxx",
                    "Displaying contacts permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(mLayout, R.string.permission_contacts_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //   ActivityCompat
                            //   .requestPermissions(getActivity(), PERMISSIONS_CONTACT,
                            //     REQUEST_CONTACTS);

                            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACTS);

                        }
                    })
                    .show();
        } else {
            // Contact permissions have not been granted yet. Request them directly.
            /// ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_CONTACT, REQUEST_CONTACTS);
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACTS);
        }
        // END_INCLUDE(contacts_permission_request)
    }

    private void requestCallLogPermissions() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                Manifest.permission.READ_CALL_LOG)
                ) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For mnhmilu, if the request has been denied previously.
            Log.i("xxxx",
                    "Displaying call history permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(mLayout, R.string.permission_contacts_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_READ_CALLLOG);

                        }
                    })
                    .show();
        } else {

            requestPermissions(new String[]{Manifest.permission.READ_CALL_LOG}, REQUEST_READ_CALLLOG);
        }
        // END_INCLUDE(contacts_permission_request)
    }

/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CONTACTS) {
            Log.i(TAG, "Received response for contact permissions request.");


            if (PermissionUtil.verifyPermissions(grantResults)) {

                try {
                    getContracts();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else {
                Log.i(TAG, "Contacts permissions were NOT granted.");
                Snackbar.make(mLayout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }

        } else if (requestCode == REQUEST_READ_CALLLOG) {

            Log.i(TAG, "Received response for read log permissions request.");
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // getContracts();

            } else {
                Log.i(TAG, "Read log permissions were NOT granted.");
                Snackbar.make(mLayout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    */

    public void getContracts() throws ParseException {
        // if (PermissionUtil.verifyPermissions(grantResults)) {

        contactModelArrayList = new ArrayList<>();
        //TODO: add check permission to avoid crash
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String identity = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));

            ContactModel contactModel = new ContactModel();
            contactModel.setName(name);
            contactModel.setNumber(phoneNumber);
            contactModel.setIdentity(identity);
            contactModelArrayList.add(contactModel);
            //Log.d("name>>", name + "  " + phoneNumber);
        }
        phones.close();


        for (ContactModel item : contactModelArrayList) {
            /////
            Cursor cursorLastCall = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI,
                    new String[]{CallLog.Calls.DATE, CallLog.Calls.DURATION,
                            CallLog.Calls.NUMBER, CallLog.Calls._ID},
                    CallLog.Calls.NUMBER + "=?",
                    new String[]{item.getNumber()},
                    CallLog.Calls.DATE + " DESC limit 1");


            if (cursorLastCall != null && cursorLastCall.getCount() > 0) {
                cursorLastCall.moveToLast();

                int date = cursorLastCall.getColumnIndex(CallLog.Calls.DATE);
                //  String testDate = cursorLastCall.getString(cursorLastCall.getColumnIndex(CallLog.Calls.DATE));

                String callDate = cursorLastCall.getString(date);
                Date callDayTime = new Date(Long.valueOf(callDate));

                DateFormat dt = android.text.format.DateFormat.getDateFormat(this.getContext());
                String formattedDate = dt.format(callDayTime);

                Log.d("Last Call date>>", item.getNumber() + "  " + dt.format(callDayTime) + "  Month: " + callDayTime.getMonth());
                item.setLastCallDate(callDayTime);


                long diff = new Date().getTime()-callDayTime.getTime();
                long seconds = diff/1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;

                long l = days;
                int i = (int) l;

                item.setDayElapsed(i);

              /*  if (callDayTime.getMonth() > 0) {
                    DateTime prevDay = new DateTime(callDayTime.getYear(), callDayTime.getMonth(), callDayTime.getDay(), 0, 0, 0, 0);
                    DateTime today = new DateTime();

                    item.setDayElapsed(Days.daysBetween(prevDay, today).getDays());

                }*/
            }
            cursorLastCall = null;
            // cursorLastCall.close();
            //  Date  test =     int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            //  item.setLastCallDate();
            ////
        }


        customAdapter = new CustomAdapter(getContext(), contactModelArrayList);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(customAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                ContactModel contact = (ContactModel) adapterView.getItemAtPosition(position);

                Toast.makeText(getContext(), "You Selected " + contact.getIdentity() + " as identity", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
