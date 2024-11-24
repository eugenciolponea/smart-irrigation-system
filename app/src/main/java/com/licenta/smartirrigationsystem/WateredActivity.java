package com.licenta.smartirrigationsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class WateredActivity extends AppCompatActivity {

    private ListView list;
    Button refresh;
    DatabaseReference reff, reff2;
    ValueEventListener valueEventListener;
    String millis = "No data";
    ArrayList<String> listt = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watered);
        list = findViewById(R.id.list);
        refresh = (Button) findViewById(R.id.refresh);

        final ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.list_item,listt);
        list.setAdapter(adapter);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reff = FirebaseDatabase.getInstance().getReference("timeon");
                reff.addValueEventListener(valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listt.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            millis = ds.child("lastwatered").getValue().toString();
                            Long mil = Long.parseLong(millis);

                            String duration = ds.child("duration").getValue().toString();
                            Float durationFloat = Float.parseFloat(duration);
                            String durationtt = String.format("%.1f", durationFloat);

                            String text = "The pump was started for : ";
                            text += durationtt;

                            text += " sec | Water Quantity : ";
                            float flow = 25 * durationFloat;
                            String flowString = String.format("%.1f", flow);
                            flowString += " mL";
                            text += flowString;

                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");
                            Date resultdate = new Date(mil);
                            String x = "Date : ";
                            String rd = sdf.format(resultdate);
                            x += rd;

                            String[] strs = {x, text};
                            listt.addAll(Arrays.asList(strs));


                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
