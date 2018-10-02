package com.example.nahidhossain.poctabedlayout;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    private ArrayList<TagModel> contactModelArrayList;
    private ListView listView;
    public static final String TAG = "MainActivity2";
    private CustomerAdapterForTagList customAdapter;

    SQLiteDatabaseHandler db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        contactModelArrayList = new ArrayList<TagModel>();

        db = new SQLiteDatabaseHandler(this);

        /*
        TagModel test = new TagModel();
        test.setTag_name("Test 1");
        db.addTag(test);

        TagModel test2 = new TagModel();
        test2.setTag_name("Test 2");
        db.addTag(test2);
        */


        contactModelArrayList = (ArrayList) db.getAllTagModels();

        customAdapter = new CustomerAdapterForTagList(this, contactModelArrayList);
        ListView listView=(ListView) findViewById(R.id.listViewTag);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                TagModel item = (TagModel) adapterView.getItemAtPosition(position);
                Toast.makeText(getBaseContext(), "You Selected " + item.getTag_name()+ " as identity", Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
