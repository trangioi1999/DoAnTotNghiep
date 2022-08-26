package com.example.appthanhlong.receiver;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class KhuMotReceiver extends BroadcastReceiver {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference khu1 = database.getReference("DieuKien/Khu1");
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("anhvan","Khu1");
        khu1.setValue(1);
    }
}
