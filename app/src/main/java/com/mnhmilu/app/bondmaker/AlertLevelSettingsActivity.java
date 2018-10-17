package com.mnhmilu.app.bondmaker;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AlertLevelSettingsActivity extends AppCompatActivity {

    private EditText editText1;
    Button btnSubmit;
    public static final String PREFERENCES_FILE_NAME = "bondmakerprefrerence";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_level_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editText1 =(EditText) findViewById(R.id.tbxAlert1);

        btnSubmit = (Button) findViewById(R.id.btnSubmitAlertLevel);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

      //  saveData();
        
        loadData();

    }

    private void saveData() {

        SharedPreferences settingsfile= getSharedPreferences(PREFERENCES_FILE_NAME,0);

        SharedPreferences.Editor myeditor = settingsfile.edit();
        myeditor.putInt(getString(R.string.alarm_first_level),Integer.valueOf(editText1.getText().toString()));
      //  myeditor.putInt(getString(R.string.alarm_second_level),Integer.valueOf(editText2.getText().toString()));

        myeditor.apply();
        super.onBackPressed();
        this.finish();
    }

    public void loadData()
    {

        SharedPreferences mysettings= getSharedPreferences(PREFERENCES_FILE_NAME, 0);

        int firstLevel = mysettings.getInt(getString(R.string.alarm_first_level),10);
       // int secondLevel = mysettings.getInt(getString(R.string.alarm_second_level),10);

        editText1.setText(String.valueOf(firstLevel));
      //  editText2.setText(String.valueOf(secondLevel));

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
