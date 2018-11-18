package com.mnhmilu.app.bondmaker;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mnhmilu.app.bondmaker.entity.ContactModel;

public class MakeCallActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView textViewNumber,textViewName,textTagStatus;
    ImageButton btnSubmit,btnAddTag;
    SQLiteDatabaseHandlerForStoreContacts db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_call);

        db = new SQLiteDatabaseHandlerForStoreContacts(this);


       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSubmit = (ImageButton) findViewById(R.id.btnCallNow);
        textViewNumber = (TextView) findViewById(R.id.textViewCallNumber);
        textViewNumber.setText(getIntent().getStringExtra("callerNumber"));

        textViewName = (TextView) findViewById(R.id.textViewCallName);
        textViewName.setText(getIntent().getStringExtra("callerName"));



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNow(getIntent().getStringExtra("callerNumber"));
            }
        });


        Spinner spinner = (Spinner) findViewById(R.id.other_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filtercontact, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        spinner.setSelection(0);

        ContactModel entity = db.getContactbyContactNumber(getIntent().getStringExtra("callerNumber"));

        String currentTagName="No Tag";

        /*Toast toast = Toast.makeText(getApplicationContext(),
                "Debug is tag:"+entity.getContact_tag()+" callType:"+entity.getCallType(),
                Toast.LENGTH_SHORT);
        toast.show();;*/


        if(entity.getContact_tag()!=null && !entity.getContact_tag().equalsIgnoreCase(""))
        {
            currentTagName=entity.getContact_tag();
        }else {
            currentTagName="No Tag!";
        }


        TextView tagStatus =(TextView) findViewById(R.id.tagStatus);
       tagStatus.setText(currentTagName);

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using



        // parent.getItemAtPosition(pos)

        if(pos==0) {
          /*  Toast toast2 = Toast.makeText(getApplicationContext(),
                    "Do Nothing",
                    Toast.LENGTH_SHORT);
            toast2.show();*/
        }else {

            String selectedTag = parent.getSelectedItem().toString();

           /* Toast toast = Toast.makeText(getApplicationContext(),
                    itemselected,
                    Toast.LENGTH_SHORT);
            toast.show();*/

            ContactModel entity = db.getContactbyContactNumber(getIntent().getStringExtra("callerNumber"));


            /*Toast toast3 = Toast.makeText(getApplicationContext(),
                   "tag is: "+entity.getContact_tag(),
                    Toast.LENGTH_SHORT);
            toast3.show();*/

            String debug = "";
            if (entity == null) {
                debug = "No Item Found!";
            } else {
                debug = "Item Found";
                entity.setContact_tag(selectedTag);
                db.updateContactModelByIdentity(entity);

                TextView tagStatus =(TextView) findViewById(R.id.tagStatus);
                tagStatus.setText(selectedTag);

                debug = selectedTag+" Tag added to the contact";
            }
            Toast toast2 = Toast.makeText(getApplicationContext(),
                    debug,
                    Toast.LENGTH_SHORT);
            toast2.show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private  void callNow(String number)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
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
