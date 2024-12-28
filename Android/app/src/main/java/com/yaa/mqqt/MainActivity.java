package com.yaa.mqqt;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button b_exit, b_contacts, b_logs ;
    TextView t_textView;
    ImageView i_imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //textview
        t_textView = findViewById(R.id.textView2);

        //imageview
        i_imageView = findViewById(R.id.imageView);

        // buttons
        b_logs = findViewById(R.id.button);
        b_contacts = findViewById(R.id.button2);
        b_exit = findViewById(R.id.button3);


        b_logs.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, logs.class);
            startActivity(intent);
        });

        b_contacts.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Contacts.class);
            startActivity(intent);
        });

        b_exit.setOnClickListener(view -> {
            finishAffinity();
            System.exit(0);
        });

    }

}
