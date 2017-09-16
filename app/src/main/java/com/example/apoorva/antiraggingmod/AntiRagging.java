package com.example.apoorva.antiraggingmod;

import android.app.AlertDialog.Builder;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class AntiRagging extends AppCompatActivity implements ChildEventListener, LocationListener {

    DatabaseReference databaseReference;
    HashMap<String, String> contactList = new HashMap<>();
    HashMap<String, Location> locationList = new HashMap<>();

    private static final int PERMISSION_REQUEST_SEND_SMS_CODE = 1;

    LocationManager locationManager;
    Location mLastLocation, campusLoc;
    Builder builder;
    Button help;
    Set set;
    String w, message, provider;
    Iterator iter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anti_ragging);

        help = (Button) findViewById(R.id.help_button);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        builder = new Builder(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("friendcontact").addChildEventListener(this);
        databaseReference.child("authorities").addChildEventListener(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_REQUEST_SEND_SMS_CODE);
        }

        databaseReference.child("campuslocations").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String key;
                int min;
                String locName = dataSnapshot.child("name").getValue(String.class);
                Toast.makeText(AntiRagging.this,"Location = " + locName, Toast.LENGTH_SHORT).show();
                Double lat = Double.parseDouble(dataSnapshot.child("lat").getValue().toString());
                Double lng = Double.parseDouble(dataSnapshot.child("lng").getValue().toString());
                mLastLocation.

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                campusLoc = new Location(dataSnapshot.getValue().toString());
                locationList.put(dataSnapshot.getKey(),campusLoc);

                Toast.makeText(AntiRagging.this, "Loc = "+dataSnapshot.getKey()+"lat = "+campusLoc.getLatitude()+"long = "+campusLoc.getLongitude(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                locationList.remove(dataSnapshot.getKey());

                Toast.makeText(AntiRagging.this,"REMOVING"+ "Loc = "+dataSnapshot.getKey()+"lat = "+campusLoc.getLatitude()+"long = "+campusLoc.getLongitude(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        provider = locationManager.getBestProvider(criteria, true);
        locationManager.requestLocationUpdates(provider,60000,1, this);
        Toast.makeText(this, "Provider = "+provider, Toast.LENGTH_SHORT).show();

        mLastLocation = locationManager.getLastKnownLocation(provider);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (contactList.isEmpty()) {
                    Toast.makeText(AntiRagging.this, "Emergency Contact List is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                SmsManager smsManager = SmsManager.getDefault();

                String nearestLoc = getNearestLocation();

                if (mLastLocation != null)
                    message = "Student at " + "https://www.google.co.in/maps/@" + mLastLocation.getLatitude() + "," + mLastLocation.getLongitude() + "," + "16z" + " need your help";
                else
                    message = "Student need your help";

                set = contactList.keySet();
                iter = set.iterator();
                while (iter.hasNext()) {
                    w = iter.next().toString();
                    smsManager.sendTextMessage("+91" + contactList.get(w), null, message, null, null);
                }
                Toast.makeText(AntiRagging.this, "Message sent", Toast.LENGTH_SHORT).show();
                Log.d("CHECKMESSAGE", message);
            }
        });
    }

    public String getNearestLocation(){

        int min,max;
        min =
        for()

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(provider,60000,1, AntiRagging.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        contactList.put(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        contactList.put(dataSnapshot.getKey(), dataSnapshot.getValue().toString());
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        contactList.remove(dataSnapshot.getKey());
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {
        Toast.makeText(AntiRagging.this, "Error in fetching emergency conatacts", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

        builder.setCancelable(false);
        builder.setTitle("SETTINGS");
        builder.setMessage("Enable GPS Provider!\nGo to System Setting?");
        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ActivityCompat.checkSelfPermission(AntiRagging.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AntiRagging.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mLastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        });
        builder.create().show();
    }
}