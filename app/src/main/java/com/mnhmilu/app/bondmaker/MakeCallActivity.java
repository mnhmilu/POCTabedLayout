package com.mnhmilu.app.bondmaker;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MakeCallActivity extends AppCompatActivity {

    TextView textViewNumber,textViewName;
    Button btnSubmit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_call);
        btnSubmit = (Button) findViewById(R.id.btnCallNow);
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

    }

    private  void callNow(String number)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }





}
