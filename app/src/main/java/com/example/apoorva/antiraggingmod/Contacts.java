package com.example.apoorva.antiraggingmod;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Contacts extends AppCompatActivity {

    EditText text_contact1,text_contact2;
    Button submit;
    String contact1,contact2;

    DatabaseReference databaseReference;
    ConnectivityManager connectivityManager;

    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("friendcontact");

        submit = (Button) findViewById(R.id.Submit_button);
        text_contact1 = (EditText) findViewById(R.id.emergen_contact1);
        text_contact2 = (EditText) findViewById(R.id.emergen_contact2);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED)

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.INTERNET},PERMISSION_REQUEST_CODE);
        }

        connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact1 = text_contact1.getText().toString().trim();
                contact2 = text_contact2.getText().toString().trim();

                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState()== NetworkInfo.State.DISCONNECTED
                        && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState()== NetworkInfo.State.DISCONNECTED){

                    Toast.makeText(Contacts.this, "Internet not available to update contact list", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(contact1.length()!=10 || contact2.length()!=10 || (!TextUtils.isDigitsOnly(contact1)) || (!TextUtils.isDigitsOnly(contact2))) {
                    Toast.makeText(Contacts.this, "INVALID MOBILE NO.", Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String,String> contact_map = new HashMap<String, String>();
                contact_map.put("contact1",contact1);
                contact_map.put("contact2",contact2);

                databaseReference.setValue(contact_map);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(Contacts.this,AntiRagging.class));
                finish();
            }
        });
    }
}
