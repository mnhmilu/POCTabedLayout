package com.mnhmilu.app.bondmaker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.mnhmilu.app.bondmaker.entity.TagModel;

import java.util.ArrayList;

public class TagIndexActivity extends AppCompatActivity {

    private ArrayList<TagModel> tagModelArrayList;
    private ListView listView;
    public static final String TAG = "MainActivity2";
    private CustomeAdapterForTagList customAdapter;
    PopupWindow pwindo;
    Button btnSubmit;
    Activity activity;

    SQLiteDatabaseHandler db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        activity =this;

        tagModelArrayList = new ArrayList<TagModel>();

        db = new SQLiteDatabaseHandler(this);

/*
        TagModel test = new TagModel();
        test.setTag_name("Test 1");
        db.addTag(test);

        TagModel test2 = new TagModel();
        test2.setTag_name("Test 2");
        db.addTag(test2);
*/
        tagModelArrayList = (ArrayList) db.getAllTagModels();

        customAdapter = new CustomeAdapterForTagList(this, tagModelArrayList,db);
        listView=(ListView) findViewById(R.id.listViewTag);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                TagModel item = (TagModel) adapterView.getItemAtPosition(position);
                Toast.makeText(getBaseContext(), "You Selected " + item.getTag_name()+ " as identity", Toast.LENGTH_SHORT).show();
            }
        });

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPopUp();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

   }

    public void addPopUp() {
        LayoutInflater inflater = this.getLayoutInflater();
        View layout = inflater.inflate(R.layout.layout_popup_tagedit,
                (ViewGroup) this.findViewById(R.id.popup_element_tagentry));
        pwindo = new PopupWindow(layout, 600, 670, true);
        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
        final EditText tagNameEdit = (EditText) layout.findViewById(R.id.editTextTagName);

        Button save = (Button) layout.findViewById(R.id.save_popup);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countryStr = tagNameEdit.getText().toString();

                TagModel country = new TagModel(countryStr);
                db.addTag(country);
                if (customAdapter == null) {
                    customAdapter = new CustomeAdapterForTagList(activity, tagModelArrayList, db);
                    listView.setAdapter(customAdapter);
                }
                customAdapter.tagModelArrayList = (ArrayList) db.getAllTagModels();
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
                for (TagModel item : tagModelArrayList) {
                    String log = "Id: " + item.getTag_id() + " ,Name: " + item.getTag_name();
                    // Writing Countries to log
                    Log.d("Tag Name: ", log);
                }
                pwindo.dismiss();
            }
        });
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
