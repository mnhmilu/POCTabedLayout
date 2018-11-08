package com.mnhmilu.app.bondmaker;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int permission_all = 1;
        String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_CALL_LOG};




        if (!hasPermission(this.getBaseContext(), permissions)) {
            ActivityCompat.requestPermissions(this, permissions, permission_all);
        }else
        {
            loadMainActivity();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
      //  Log.i(TAG, "onRequestPermissionResult");
        loadMainActivity(requestCode, grantResults);
    }

    private void loadMainActivity(int requestCode, @NonNull int[] grantResults) {
        int permission_all = 1;
        if (requestCode == permission_all) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
              //  Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted. Kick off the process of building and connecting

                loadMainActivity();

            }
            else {
                // Permission denied.

                Toast toast = Toast.makeText(getApplicationContext(),
                        "Please give permission to run this application!",
                        Toast.LENGTH_SHORT);

                toast.show();
            }
        }
    }

    private void loadMainActivity() {



        setContentView(R.layout.activity_splash_screen);

        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;

      //  textView=(TextView)  findViewById(R.id.versionName) ;

        //textView.setText(versionName);


        Thread myThread = new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        myThread.start();
    }

    public static boolean hasPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED
                        ) {
                    return false;
                }
            }
            // customAdapterLastCall.notifyDataSetChanged();
        }
        return true;
    }
}
