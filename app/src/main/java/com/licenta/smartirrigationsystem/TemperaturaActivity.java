package com.licenta.smartirrigationsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;


public class TemperaturaActivity extends AppCompatActivity {

    private LineChart Temp_linechart;
    ArrayList<Entry> yData;
    ValueEventListener valueEventListener;
    DatabaseReference mPostReference;
    ArrayList<Long> dateList = new ArrayList< Long>();
    Long referenceTimestamp = 1591373801221L;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moisture);
        Temp_linechart = (LineChart) findViewById(R.id.linechart1);

        mPostReference = FirebaseDatabase.getInstance().getReference("dht");
        mPostReference.addValueEventListener(valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                yData = new ArrayList<>();
                int i = 0;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String SV = ds.child("temp").getValue().toString();
                    Float SensorValue = Float.parseFloat(SV);

                    String t = ds.child("time").getValue().toString();

                    Long originalTimestamp = Long.parseLong(t);

                    Long convertedTimestamp = originalTimestamp - referenceTimestamp;
                    dateList.add(convertedTimestamp);
                    yData.add(new Entry(i, SensorValue));
                    i++;
                }

                //Grafic 1
                 LineDataSet lineDataSet = new LineDataSet(yData, "Temperature(℃)");

                lineDataSet.setLineWidth(3);

                lineDataSet.setColor(Color.CYAN);
                lineDataSet.setDrawValues(false);
                lineDataSet.setFillColor(Color.GRAY);
                lineDataSet.setFillColor(Color.RED);


                LineData lineData = new LineData(lineDataSet);
                Temp_linechart.setData(lineData);

                Temp_linechart.getLegend().setTextSize(15f);
                Temp_linechart.getAxisRight().setEnabled(false);
                Temp_linechart.getAxisLeft().setTextColor(Color.CYAN);
                Temp_linechart.getAxisLeft().setTextSize(12f);
                Temp_linechart.getXAxis().setTextColor(Color.CYAN);
                Temp_linechart.getXAxis().setTextSize(12f);
                Temp_linechart.setNoDataText("No data received from raspberry");

                Temp_linechart.notifyDataSetChanged();
                Temp_linechart.invalidate();
                Temp_linechart.setDescription("Temperature(℃)");
                Temp_linechart.setDescriptionTextSize(12);

                AxisValueFormatter xAxisFormatter = new HourAxisValueFormatter(dateList, referenceTimestamp);
                XAxis xAxis = Temp_linechart.getXAxis();
                xAxis.setValueFormatter(xAxisFormatter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}