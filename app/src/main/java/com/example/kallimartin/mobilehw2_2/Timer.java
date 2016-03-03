package com.example.kallimartin.mobilehw2_2;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kallimartin on 3/2/16.
 */
public class Timer {

    private int time;
    private int seconds;
    private int minutes;
    private int hours;
    ArrayList<String> lappedTimes;

    public Timer() {
        seconds = 0;
        minutes = 0;
        hours = 0;
        lappedTimes = new ArrayList<String>();
    }
    public Timer(int time, ArrayList<String> list)
    {
        setTime(time);
        lappedTimes = list;
    }
    public void setTime(int s)
    {
        time = s;
        hours = s / 3600;
        minutes = (s % 3600 ) / 60;
        seconds = (s % 3600 ) % 60;
    }

    public String getTime()
    {
        String time = "";
        if(hours < 10)
            time += "0";
        time += hours + ":";
        if(minutes < 10)
            time += "0";
        time += minutes + ":";
        if(seconds < 10)
            time += "0";
        time += seconds;
        return time;
    }

    public int getTimeInSeconds()
    {
        return time;
    }
    public void lapTime()
    {
        lappedTimes.add(getTime());
    }
    public ArrayList<String> getLappedTimes()
    {
        return lappedTimes;
    }

    public void reset()
    {
        setTime(0);
        lappedTimes.clear();
    }


}
