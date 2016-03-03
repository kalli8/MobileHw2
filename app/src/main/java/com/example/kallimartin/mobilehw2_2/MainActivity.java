package com.example.kallimartin.mobilehw2_2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ControlFragment.OnFragmentInteractionListener, LapFragment.OnFragmentInteractionListener {

    boolean running = false;
    Timer timer;
    int currentTime;
    TimerAsyncTask timerAsyncTask;

    ControlFragment controlFragment;

    public ControlFragment getControlFragment()
    {
        return controlFragment = (ControlFragment) getSupportFragmentManager().findFragmentById(R.id.controlFrag);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void start() {
        if (timerAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            timerAsyncTask.cancel(true);
        }
        running = !running;

        if(getControlFragment() != null) {
            controlFragment.toggleStartButtonText(running);
        }

        timerAsyncTask = new TimerAsyncTask();
        timerAsyncTask.execute(currentTime);
    }

    @Override
    public void reset() {

        if (timerAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            timerAsyncTask.cancel(true);
        }
        running = false;
        timer.reset();
        currentTime = 0;

        if(getControlFragment() != null) {
            controlFragment.setTimeTextView(timer.getTime());
        }
    }

    @Override
    public void lap() {
        timer.lapTime();

    }

    @Override
    public void viewLapList() {

        Intent intent = new Intent(this, ListActivity.class);
        intent.putStringArrayListExtra("LIST", timer.getLappedTimes());
        startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            getControlFragment();
            timer = new Timer();
            timerAsyncTask = new TimerAsyncTask();
            running = false;
        } else
        {
            running = savedInstanceState.getBoolean("THREAD_RUNNING");
            int curTime = savedInstanceState.getInt("CURRENT_TIME");
            ArrayList<String> lapTimes = savedInstanceState.getStringArrayList("LAP_TIMES");
            timer = new Timer(curTime, lapTimes);

            if (getControlFragment() != null) {
                controlFragment.toggleStartButtonText(running);
                controlFragment.setTimeTextView(timer.getTime());
            }

            timerAsyncTask = new TimerAsyncTask();
            timerAsyncTask.execute(curTime);

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        if (timerAsyncTask != null && timerAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            outState.putBoolean("THREAD_RUNNING", true);
        } else {
            outState.putBoolean("THREAD_RUNNING", false);
        }

        outState.putStringArrayList("LAP_TIMES", timer.getLappedTimes());
        outState.putInt("CURRENT_TIME", timer.getTimeInSeconds());

        Log.d("Saved CT", "" + timer.getTimeInSeconds());

//        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        running = savedInstanceState.getBoolean("THREAD_RUNNING");
        int curTime = savedInstanceState.getInt("CURRENT_TIME");
        Log.d("Reinstated CT", "" + curTime);
        ArrayList<String> lapTimes = savedInstanceState.getStringArrayList("LAP_TIMES");
        timer = new Timer(curTime, lapTimes);

        if (getControlFragment() != null) {
            controlFragment.toggleStartButtonText(running);
            controlFragment.setTimeTextView(timer.getTime());
        }

        timerAsyncTask = new TimerAsyncTask();
        timerAsyncTask.execute(curTime);

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override protected void onDestroy() {
        if(timerAsyncTask!=null&&timerAsyncTask.getStatus()== AsyncTask.Status.RUNNING)
        {
            timerAsyncTask.cancel(true);
            timerAsyncTask=null;
        }
        super.onDestroy();
    }


    private class TimerAsyncTask extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... values) {

            int count = values[0];

            while (running) {
                try {
                    Thread.sleep(1000); // one second
                    count++;
                    Log.d("TIME FROM THREAD: ", "" + count);
                    publishProgress(count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            currentTime = values[0];
            timer.setTime(currentTime);
            Log.d("PROGRESS UPDATE: ", "" + currentTime);
            if(getControlFragment() != null) {
                controlFragment.setTimeTextView(timer.getTime());
            }
            super.onProgressUpdate(values);
        }


    }
}