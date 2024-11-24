package com.licenta.smartirrigationsystem;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Yasir on 02/06/16.
 */
public class HourAxisValueFormatter implements AxisValueFormatter
{

    private ArrayList<Long> dateList;
    private Long reference_timestamp;
    private DateFormat mDataFormat;
    private Date mDate;

    public HourAxisValueFormatter(ArrayList<Long> dateList, Long reference_timestamp) {
        this.reference_timestamp = reference_timestamp;
        this.dateList = dateList;
        this.mDataFormat = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        this.mDate = new Date();
    }


    /**
     * Called when a value from an axis is to be formatted
     * before being drawn. For performance reasons, avoid excessive calculations
     * and memory allocations inside this method.
     *
     * @param value the value to be formatted
     * @param axis  the axis the value belongs to
     * @return
     */


    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        if((int)value < 0) {
            return "";
        }

        // Retrieve original timestamp
        long originalTimestamp = dateList.get((int)value) + reference_timestamp;

        return getHour(originalTimestamp);
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }

    private String getHour(long timestamp){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            Date resultdate = new Date(timestamp);
            return sdf.format(resultdate);
            /*

            mDate.setTime(timestamp*1000);
            return mDataFormat.format(mDate);

             */
        }
        catch(Exception ex){
            return "xx";
        }
    }
}