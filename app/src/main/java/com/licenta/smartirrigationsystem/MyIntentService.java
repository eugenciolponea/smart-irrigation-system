package com.licenta.smartirrigationsystem;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class MyIntentService extends IntentService {
    public MyIntentService() {
        super("Notification Service");
    }
    ValueEventListener valueEventListener;
    DatabaseReference mPostReference;
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        mPostReference = FirebaseDatabase.getInstance().getReference();
        Query lastQuery = mPostReference.child("distancesensor").orderByKey().limitToLast(1);
        mPostReference.addListenerForSingleValueEvent(valueEventListener = new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String SV = ds.child("distance").getValue().toString();
                    Float SensorValue = Float.parseFloat(SV);
                    if(SensorValue > 10) {
                      //show_Notification();
                   }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @TargetApi(Build.VERSION_CODES.O)
    public void show_Notification() {

        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        String CHANNEL_ID = "MYCHANNEL";
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "name", NotificationManager.IMPORTANCE_LOW);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
        Notification notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentText("Water Level LOW")
                .setContentTitle("There is no water in tank!")
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.sym_action_chat, "Title", pendingIntent)
                .setChannelId(CHANNEL_ID)
                .setSmallIcon(android.R.drawable.sym_action_chat)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);
        notificationManager.notify(1, notification);
    }
}