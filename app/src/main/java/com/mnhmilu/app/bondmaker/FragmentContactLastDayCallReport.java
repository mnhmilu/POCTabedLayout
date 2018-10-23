package com.mnhmilu.app.bondmaker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * <p>
 * to handle interaction events.
 * Use the {@link FragmentContactLastDayCallReport#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentContactLastDayCallReport extends Fragment {

    private CustomAdapterLastCall customAdapterLastCall;
    private ArrayList<ContactModel> contactModelArrayList;
    private ArrayList<ContactModel> contactModelArrayListForRemove;

  //  private  Button btnSyncLastCall;
    private ListView listView;
    private View mLayout;
    public static final String TAG = "FragmentContactLastDayCallReport";
    private TextView progressView;

   // private ProgressBar progressBar;

    public static final String PREFERENCES_FILE_NAME = "bondmakerprefrerence";



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

    public FragmentContactLastDayCallReport() {
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
    public static FragmentContactLastDayCallReport newInstance(String param1, String param2) {
        FragmentContactLastDayCallReport fragment = new FragmentContactLastDayCallReport();
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

        setHasOptionsMenu(true);//enable menu refresh


    }


    public static boolean hasPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
                        ) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       if(id == R.id.action_refresh)
        {
            new GetContactLastCallAsycTask().execute(progressView);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_contactlastdayall, container, false);

        mLayout = rootView.findViewById(R.id.main_content);
        progressView = (TextView) rootView.findViewById(R.id.processStatus);


        customAdapterLastCall = new CustomAdapterLastCall(getContext(), contactModelArrayList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                setRowColor(position, view);
                return view;
            }
        };


        ListView listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setAdapter(customAdapterLastCall);
        listView.setVisibility(View.INVISIBLE);

        TextView empty = new TextView(getContext());
        empty.setHeight(150);
        listView.addFooterView(empty);
        progressView.setText("");
        new GetContactLastCallAsycTask().execute(progressView);
        return rootView;
    }


    @Override
    public void onResume() {

        super.onResume();
        customAdapterLastCall.notifyDataSetChanged();

    }


    class GetContactLastCallAsycTask extends AsyncTask<TextView, String, Boolean> {

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
                progressView = textViews[0];
                int permission_all = 1;
                String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG};
                publishProgress("Checking Permission....");
                if (!hasPermission(getContext(), permissions)) {
                    ActivityCompat.requestPermissions(getActivity(), permissions, permission_all);
                } else {

                    contactModelArrayList = new ArrayList<>();
                    contactModelArrayListForRemove = new ArrayList<>();


                    //TODO: add check permission to avoid crash

                    publishProgress("Getting your coantact data");
                    Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.RawContacts.ACCOUNT_TYPE+"=?", new String[]{"com.google"}, "UPPER(" + ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");


                    while (phones.moveToNext()) {
                        String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String identity = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));
                       // String accountType = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.));


                        ContactModel contactModel = new ContactModel();
                        contactModel.setName(name);
                        contactModel.setNumber(phoneNumber.replace(" ","").replace("-",""));
                        contactModel.setIdentity(identity);
                        contactModelArrayList.add(contactModel);
                        publishProgress("Getting your conatact information.......");

                        //Log.d("name>>", name + "  " + phoneNumber);
                    }


                    phones.close();
                    for (ContactModel item : contactModelArrayList) {
                        /////
                        Cursor cursorLastCall = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI,
                                new String[]{CallLog.Calls.DATE, CallLog.Calls.DURATION,
                                        CallLog.Calls.NUMBER, CallLog.Calls._ID,CallLog.Calls.TYPE},
                                CallLog.Calls.NUMBER + "=?",
                                new String[]{item.getNumber()},
                                CallLog.Calls.DATE + " DESC limit 1");


                        if (cursorLastCall != null && cursorLastCall.getCount() >0) {
                            cursorLastCall.moveToLast();

                            int callType = cursorLastCall.getColumnIndex(CallLog.Calls.TYPE);
                            String callTypeString =cursorLastCall.getString(callType);

                            int dircode = Integer.parseInt(callTypeString);
                            if(dircode==CallLog.Calls.INCOMING_TYPE)
                            {
                                item.setCallType("Incoming");
                            }else if(dircode==CallLog.Calls.OUTGOING_TYPE)
                            {
                                item.setCallType("Outgoing");
                            }
                            else if(dircode==CallLog.Calls.MISSED_TYPE)
                            {
                                item.setCallType("Missed");
                            } else
                            {
                                item.setCallType("Others");
                            }


                            int date = cursorLastCall.getColumnIndex(CallLog.Calls.DATE);
                            //  String testDate = cursorLastCall.getString(cursorLastCall.getColumnIndex(CallLog.Calls.DATE));

                            String callDate = cursorLastCall.getString(date);
                            Date callDayTime = new Date(Long.valueOf(callDate));

                            DateFormat dt = android.text.format.DateFormat.getDateFormat(getContext());
                            String formattedDate = dt.format(callDayTime);

                            Log.d("Last Call date>>", item.getNumber() + "  " + dt.format(callDayTime) + "  Month: " + callDayTime.getMonth() +" "+dircode);
                            item.setLastCallDate(callDayTime);


                            long diff = new Date().getTime() - callDayTime.getTime();
                            long seconds = diff / 1000;
                            long minutes = seconds / 60;
                            long hours = minutes / 60;
                            long days = hours / 24;

                            long l = days;
                            int i = (int) l;

                            item.setDayElapsed(i);

                        } else {
                            contactModelArrayListForRemove.add(item);
                        }

                        cursorLastCall.close();
                        //cursorLastCall = null;
                        publishProgress("Analyzing your call history..........");
                    }
                    contactModelArrayList.removeAll(contactModelArrayListForRemove);
                }
                returnValue = true;
            }
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

                customAdapterLastCall = new CustomAdapterLastCall(getContext(), contactModelArrayList) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        setRowColor(position, view);
                        return view;
                    }
                };
                ListView listView = (ListView) rootView.findViewById(R.id.listView);
                listView.setAdapter(customAdapterLastCall);
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

    private void setRowColor(int position, View view) {
        if(contactModelArrayList != null) {
            if (contactModelArrayList.size() > 0 ) {

                SharedPreferences mysettings=this.getActivity().getSharedPreferences(PREFERENCES_FILE_NAME, 0);

                int days = mysettings.getInt(getString(R.string.alarm_first_level),10);
                int secondLevel=days+days+1;
                TextView textViewDays=(TextView) view.findViewById(R.id.textViewDays);
                TextView textViewDaysFixed=(TextView) view.findViewById(R.id.textViewFixedDays);

                if (contactModelArrayList.get(position).getDayElapsed() >=0 && contactModelArrayList.get(position).getDayElapsed() <= days) {
                    textViewDays.setBackgroundColor(Color.GREEN);
                    textViewDaysFixed.setBackgroundColor(Color.GREEN);
                } else if (contactModelArrayList.get(position).getDayElapsed() >= (days+1) && contactModelArrayList.get(position).getDayElapsed() < secondLevel) {
                    textViewDays.setBackgroundColor(Color.YELLOW);
                    textViewDaysFixed.setBackgroundColor(Color.YELLOW);
                } else if (contactModelArrayList.get(position).getDayElapsed() >= secondLevel) {
                    textViewDays.setBackgroundColor(Color.RED);
                    textViewDaysFixed.setBackgroundColor(Color.RED);
                }

            }
        }
    }





}
