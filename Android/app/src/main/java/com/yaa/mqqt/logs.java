package com.yaa.mqqt;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class logs extends AppCompatActivity {
//    int[] images = [R., R.drawable.megan, R.drawable.will,R.drawable.tom];
    private static List<String> dataList = new ArrayList<>();
    private static MyCustomAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        try {
            MqttService mqttService = new MqttService(this);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }

        listView = findViewById(R.id.listview);
        adapter = new MyCustomAdapter(this, R.layout.single_item, dataList);
        listView.setAdapter(adapter);

        loadDataFromFile();
    }

    private void loadDataFromFile() {
        try {
            FileInputStream fis = openFileInput("data.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = reader.readLine()) != null) {
                dataList.add(line);
            }
            reader.close();
            adapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void listUpdater(String newData){
        dataList.add(newData);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
