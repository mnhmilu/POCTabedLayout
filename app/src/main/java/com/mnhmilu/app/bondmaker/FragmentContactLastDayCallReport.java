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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

       // Spinner spinner =new Spinner(getContext());
// Create an ArrayAdapter using the string array and a default spinner layout
    //    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
      //          R.array.filtercontact, android.R.layout.simple_expandable_list_item_1);
// Specify the layout to use when the list of choices appears
       // adapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
// Apply the adapter to the spinner
        //spinner.setAdapter(adapter);

     /*   final Spinner spinner = new Spinner(getContext());
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.filtercontact)); //selected item will look like a spinner set from XML
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinner.setAdapter(spinnerArrayAdapter);
*/
       /* spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });*/

        setHasOptionsMenu(true);//enable menu refresh


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
        db = new SQLiteDatabaseHandlerForStoreContacts(getActivity());

        mLayout = rootView.findViewById(R.id.main_content);
        progressView = (TextView) rootView.findViewById(R.id.processStatus);

        ArrayList<ContactModel> dbList=db.getAllContactsModels();

        customAdapterLastCall = new CustomAdapterLastCall(getContext(),contactModelArrayList ) {
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
        }

        @Override
        protected Boolean doInBackground(TextView... textViews) {

            Boolean returnValue = false;

            if (textViews.length > 0) {
                progressView = textViews[0];
                int permission_all = 1;
                String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG};
                publishProgress("Checking Permission....");
                    contactModelArrayList = new ArrayList<>();


                    //TODO: add check permission to avoid crash

                    publishProgress("Getting your coantact data");


                contactModelArrayList=db.getAllContactsModels();


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

              //  if(contactModelArrayList.get(position).getContact_tag().equalsIgnoreCase("NEVER_CALLED"))
              //  {
               //     textViewDays.setBackgroundColor(Color.GRAY);
              //      textViewDaysFixed.setBackgroundColor(Color.GRAY);
              //  }else {

                    if (contactModelArrayList.get(position).getDayElapsed() >= 0 && contactModelArrayList.get(position).getDayElapsed() <= days) {
                        textViewDays.setBackgroundColor(Color.GREEN);
                        textViewDaysFixed.setBackgroundColor(Color.GREEN);
                    } else if (contactModelArrayList.get(position).getDayElapsed() >= (days + 1) && contactModelArrayList.get(position).getDayElapsed() < secondLevel) {
                        textViewDays.setBackgroundColor(Color.YELLOW);
                        textViewDaysFixed.setBackgroundColor(Color.YELLOW);
                    } else if (contactModelArrayList.get(position).getDayElapsed() >= secondLevel) {
                        textViewDays.setBackgroundColor(Color.RED);
                        textViewDaysFixed.setBackgroundColor(Color.RED);
                    }
                //}

            }
        }
    }

}
