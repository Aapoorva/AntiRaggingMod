package com.example.apoorva.antiraggingmod;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;
/**
 * Created by Apoorva on 14-Sep-17.
 */

public class AntiRaggingMod extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
