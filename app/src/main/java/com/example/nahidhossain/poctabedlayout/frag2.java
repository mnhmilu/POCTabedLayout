package com.example.nahidhossain.poctabedlayout;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the

 * to handle interaction events.
 * Use the {@link frag2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag2 extends Fragment  {

    private CustomAdapter customAdapter;
    private ArrayList<ContactModel> contactModelArrayList;
    private ListView listView;
    private View mLayout;
    private static final int REQUEST_CONTACTS = 1;

    public static final String TAG = "MainActivity";

    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_CONTACTS };


///////////////////

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;

    String[] NAMES ={"Name A","Name B"};

    String[] DESCRIPTIONS ={"Description 1", "Description 2"};

    public frag2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Frag1.
     */
    // TODO: Rename and change types and number of parameters
    public static frag2 newInstance(String param1, String param2) {
        frag2 fragment = new frag2();
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

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView =inflater.inflate(R.layout.fragment_frag2, container, false);
        mLayout=  rootView.findViewById(R.id.main_content);

        checkPermission();

        return rootView;
    }


    private void checkPermission()
    {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED
                ) {
            // Contacts permissions have not been granted.
            Log.i("MainActivity", "Contact permissions has NOT been granted. Requesting permissions.");
            requestContactsPermissions();
        }


    }

    @Override
    public void onResume() {

        //checkPermission();

        super.onResume();
        getContracts();
    }

    private void requestContactsPermissions() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                Manifest.permission.READ_CONTACTS)
                ) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
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

                            requestPermissions(new String[]{ Manifest.permission.READ_CONTACTS},REQUEST_CONTACTS);

                        }
                    })
                    .show();
        } else {
            // Contact permissions have not been granted yet. Request them directly.
           /// ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_CONTACT, REQUEST_CONTACTS);
            requestPermissions(new String[]{ Manifest.permission.READ_CONTACTS},REQUEST_CONTACTS);
        }
        // END_INCLUDE(contacts_permission_request)
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CONTACTS) {
            Log.i(TAG, "Received response for contact permissions request.");

            // We have requested multiple permissions for contacts, so all of them need to be
            // checked.
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
              //  Snackbar.make(mLayout, R.string.permision_available_contacts,
                     //   Snackbar.LENGTH_SHORT)
                      //  .show();

                getContracts();

            } else {
                Log.i(TAG, "Contacts permissions were NOT granted.");
                Snackbar.make(mLayout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void getContracts()
    {

        contactModelArrayList = new ArrayList<>();
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
            Log.d("name>>", name + "  " + phoneNumber);
        }
        phones.close();

        customAdapter = new CustomAdapter(getContext(), contactModelArrayList);
        ListView listView=(ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(customAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                ContactModel contact = (ContactModel) adapterView.getItemAtPosition(position);

                Toast.makeText(getContext(), "You Selected " + contact.getIdentity()+ " as identity", Toast.LENGTH_SHORT).show();
            }
        });



    }


    /*

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView =inflater.inflate(R.layout.fragment_frag2, container, false);
        ListView listView=(ListView) rootView.findViewById(R.id.listView);
        CustomeAdapter customeAdapter = new CustomeAdapter();
        listView.setAdapter(customeAdapter);

        return rootView;
    }



    class  CustomeAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.customlayout,null);

            TextView name = (TextView) convertView.findViewById(R.id.textView_name);
            TextView description = (TextView) convertView.findViewById(R.id.textView_desc);

            name.setText(NAMES[position]);
            description.setText(DESCRIPTIONS[position]);

            return  convertView;
        }
    }

    */

}
