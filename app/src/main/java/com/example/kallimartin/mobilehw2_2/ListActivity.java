package com.example.kallimartin.mobilehw2_2;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            finish(); return;
        }
        setContentView(R.layout.fragment_lap);

        Intent intent = getIntent();
        List<String> list = intent.getStringArrayListExtra("LIST");

        listView = (ListView)findViewById(R.id.lapList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,list);

        listView.setAdapter(adapter);

    }
}
