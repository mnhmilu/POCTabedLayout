package com.mnhmilu.app.bondmaker;

import android.Manifest;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mnhmilu.app.bondmaker.entity.ContactModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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


  //  private  Button btnSyncLastCall;
    private ListView listView;
    private View mLayout;
    public static final String TAG = "FragmentContactLastDayCallReport";
    private TextView progressView;
    private Spinner spinner;
    private  String tagKey="";
    private ProgressBar progressBarContactLastDay;

    SQLiteDatabaseHandlerForStoreContacts db;

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


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here

        MenuItem spinnerItem = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner)spinnerItem.getActionView().findViewById(R.id.spinner);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if(position==0) {// do nothing
                    /*Toast toast2 = Toast.makeText(getContext(),
                            "Do Nothing",
                            Toast.LENGTH_SHORT);
                    toast2.show();*/
                }else {

                    String itemselected = parent.getSelectedItem().toString();

                   /* Toast toast = Toast.makeText(getContext(),
                            itemselected,
                            Toast.LENGTH_SHORT);
                    toast.show();*/

                    tagKey=itemselected;

                    new GetContactLastCallAsycTask().execute(progressView);


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       if(id == R.id.action_refresh)
        {
            tagKey="refresh";
            new GetContactLastCallAsycTask().execute(progressView);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_contactlastdayall, container, false);
        db = new SQLiteDatabaseHandlerForStoreContacts(getActivity());

        mLayout = rootView.findViewById(R.id.main_content);
        progressView = (TextView) rootView.findViewById(R.id.processStatus);

        progressBarContactLastDay=(ProgressBar)rootView.findViewById(R.id.progressBarContactLastDay);


        //  ArrayList<ContactModel> dbList=db.getAllContactsModels();
        contactModelArrayList = new ArrayList<>();
        customAdapterLastCall = new CustomAdapterLastCall(getContext(),contactModelArrayList ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                setRowColor(position, view);
                return view;
            }
        };

        listView = (ListView) rootView.findViewById(R.id.listView);
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
        Log.d("Last On Resume>>", "");

        super.onResume();
        customAdapterLastCall.notifyDataSetChanged();

    }


    class GetContactLastCallAsycTask extends AsyncTask<TextView, String, Boolean> {

        int count;

        @Override
        protected void onPreExecute() {

            count = 0;
            progressView.setVisibility(View.VISIBLE);
            progressBarContactLastDay.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Boolean doInBackground(TextView... textViews) {

            Boolean returnValue = false;
            contactModelArrayList = new ArrayList<>();
           // progressBarContactLastDay.setVisibility(View.VISIBLE);
            progressBarContactLastDay.setProgress(10);

            if (textViews.length > 0) {
                progressView = textViews[0];

                publishProgress("Analyzing your call history...............");
                contactModelArrayList.clear();

                if(tagKey.equalsIgnoreCase("")||tagKey.equalsIgnoreCase("All")) {
                    contactModelArrayList = db.getAllContactsModels();
                }
                else if (tagKey.equalsIgnoreCase("refresh"))
                {
                    updateContactwithCallLogs();
                    contactModelArrayList = db.getAllContactsModels();
                }
                else
                {
                    contactModelArrayList=db.getContactsModelsByTag(tagKey);
                }
                tagKey="";

                }
                returnValue = true;
            //}
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

                if(contactModelArrayList.size()>0) {
                    listView.setVisibility(View.VISIBLE);
                }else {
                        Toast toast2 = Toast.makeText(getContext(),
                            "No Data Found",
                            Toast.LENGTH_SHORT);
                    toast2.show();
                }

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
                progressBarContactLastDay.setVisibility(View.GONE);

            }

        }
    }

    private void updateContactwithCallLogs()
    {
        int count=0;
        ArrayList<ContactModel> cantacts =db.getAllContactsModels();

        for (ContactModel item : cantacts) {

            count++;

            progressBarContactLastDay.setProgress((100*count)/cantacts.size());



            //  counter++;
            /////
            Cursor cursorLastCall = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI,
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

                DateFormat dt = android.text.format.DateFormat.getDateFormat(getContext());
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

    private void setRowColor(int position, View view) {
        if(contactModelArrayList != null) {

            //TODO: unclear logic "" temporary fix for worng position then size
            if (contactModelArrayList.size() > 0  && position<contactModelArrayList.size()) {


                Log.d("#####Debug","position: "+position+" Size: "+contactModelArrayList.size());

                SharedPreferences mysettings=this.getActivity().getSharedPreferences(PREFERENCES_FILE_NAME, 0);

                int days = mysettings.getInt(getString(R.string.alarm_first_level),10);
                int secondLevel=days+days+1;
                TextView textViewDays=(TextView) view.findViewById(R.id.textViewDays);
                TextView textViewDaysFixed=(TextView) view.findViewById(R.id.textViewFixedDays);

               if(contactModelArrayList.get(position).getContact_tag().equalsIgnoreCase("Never Called"))
               {
                    textViewDays.setBackgroundColor(Color.GRAY);
                   textViewDaysFixed.setBackgroundColor(Color.GRAY);
                }else {

                    if (contactModelArrayList.get(position).getDayElapsed() >= 0 && contactModelArrayList.get(position).getDayElapsed() <= days) {
                        textViewDays.setBackgroundColor(Color.rgb(66, 244, 155));
                        textViewDaysFixed.setBackgroundColor(Color.rgb(66, 244, 155));
                    } else if (contactModelArrayList.get(position).getDayElapsed() >= (days + 1) && contactModelArrayList.get(position).getDayElapsed() < secondLevel) {
                        textViewDays.setBackgroundColor(Color.rgb(211, 244, 65));
                        textViewDaysFixed.setBackgroundColor(Color.rgb(211, 244, 65));
                    } else if (contactModelArrayList.get(position).getDayElapsed() >= secondLevel) {
                        textViewDays.setBackgroundColor(Color.rgb(255, 114, 114));
                        textViewDaysFixed.setBackgroundColor(Color.rgb(255, 114, 114));
                    }
                }

            }
        }
    }

}
