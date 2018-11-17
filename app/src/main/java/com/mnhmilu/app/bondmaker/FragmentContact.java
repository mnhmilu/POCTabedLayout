package com.mnhmilu.app.bondmaker;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mnhmilu.app.bondmaker.entity.ContactModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link FragmentContact#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentContact extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{

    private CustomAdapter customAdapter;
    private ArrayList<ContactModel> contactModelArrayList;
    private ArrayList<ContactModel> contactModelArrayNeverCalled;


    private ListView listView;
    private View mLayout;
    public static final String TAG = "MainActivity";
    private  TextView progressView;

   // private ProgressBar progressBar;



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
        setHasOptionsMenu(true);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_refresh)
        {
            new GetContactAsycTask().execute(progressView);
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_frag2, container, false);
        mLayout = rootView.findViewById(R.id.main_content);
        progressView=(TextView) rootView.findViewById(R.id.processStatus);


        customAdapter = new CustomAdapter(getContext(), contactModelArrayList) ;

        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(customAdapter);
        listView.setVisibility(View.INVISIBLE);

        TextView empty = new TextView(getContext());
        empty.setHeight(150);
        listView.addFooterView(empty);

        new GetContactAsycTask().execute(progressView);
        return rootView;
    }


    @Override
    public void onResume() {

        super.onResume();

        customAdapter.notifyDataSetChanged();

    }

    @Override
    public void onRefresh() {
        new GetContactAsycTask().execute(progressView);
    }

    class GetContactAsycTask extends AsyncTask<TextView, String, Boolean> {

        int count;

        @Override
        protected void onPreExecute() {
            count = 0;
            progressView.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(TextView... textViews) {

            Boolean returnValue = false;

            if (textViews.length > 0) {
                progressView=textViews[0];
                int permission_all = 1;
                String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG};
                publishProgress("Checking Permission....");

                    contactModelArrayList = new ArrayList<>();
                    contactModelArrayNeverCalled = new ArrayList<>();


                    //TODO: add check permission to avoid crash

                    publishProgress("Getting your coantact data");
                    Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.RawContacts.ACCOUNT_TYPE+"=?", new String[]{"com.google"}, "UPPER(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");


                    while (phones.moveToNext()) {
                        String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String identity = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));

                        ContactModel contactModel = new ContactModel();
                        contactModel.setName(name);
                        contactModel.setNumber(phoneNumber.replace(" ","").replace("-",""));
                       // contactModel.setNumber(phoneNumber);
                        contactModel.setIdentity(identity);
                        contactModelArrayList.add(contactModel);
                        publishProgress("Getting your conatact information.......");

                        //Log.d("name>>", name + "  " + phoneNumber);
                    }


                    phones.close();

                    int listSize=contactModelArrayList.size();
                    int counter=0;


                    for (ContactModel item : contactModelArrayList) {

                        counter++;
                        /////
                        Cursor cursorLastCall = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI,
                                new String[]{CallLog.Calls.DATE, CallLog.Calls.DURATION,
                                        CallLog.Calls.NUMBER, CallLog.Calls._ID},
                                CallLog.Calls.NUMBER + "=?",
                                new String[]{item.getNumber()},
                                CallLog.Calls.DATE + " DESC limit 1");


                        if (cursorLastCall != null && cursorLastCall.getCount() == 0) {
                            contactModelArrayNeverCalled.add(item);
                        }


                        cursorLastCall.close();
                        //cursorLastCall = null;
                      //  publishProgress("Analyzing your call history..........");
                        publishProgress("Analyzing your call history, "+counter+" of "+listSize+" contacts");

                    }

                    //contactModelArrayNeverCalled.removeAll(contactModelArrayList);
                    // getting only never call item
                    contactModelArrayList =contactModelArrayNeverCalled;

                }
                returnValue = true;

            return returnValue;
            //  return "Task done,All item are populated in the list";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // super.onProgressUpdate(values);
            count++;
            progressView.setText(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean isDone) {

            if (isDone) {

                progressView.setText("Processing Done!");

               // Toast.makeText(getContext(), " !", Toast.LENGTH_LONG).show();
                customAdapter = new CustomAdapter(getContext(), contactModelArrayList);
                ListView listView = (ListView) rootView.findViewById(R.id.listView);
                listView.setAdapter(customAdapter);
                listView.setVisibility(View.VISIBLE);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                        ContactModel contact = (ContactModel) adapterView.getItemAtPosition(position);
                        Intent in = new Intent(getActivity(),MakeCallActivity.class);
                        in.putExtra("callerNumber",contact.getNumber());
                        in.putExtra("callerName",contact.getName());

                        startActivity(in);
                      //  Toast.makeText(getContext(), "You Selected " + contact.getIdentity() + " as identity", Toast.LENGTH_SHORT).show();
                    }
                });
                progressView.setVisibility(View.GONE);
            }

        }
    }


}
