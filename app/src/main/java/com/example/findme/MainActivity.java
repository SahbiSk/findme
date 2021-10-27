package com.example.findme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
   // private TextView lang,lat,num;
    private EditText langField,latField,numField;
    private Button submit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getting instences
        langField=findViewById(R.id.langitude_field);
        latField=findViewById(R.id.latitude_field);
        numField=findViewById(R.id.numero_field);
        submit=findViewById(R.id.submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager manager =SmsManager.getDefault();
                manager.sendTextMessage(
                        numField.getText().toString(),
                        null,
                        "FindMe#"+langField.getText().toString()+"#"+latField.getText().toString(),
                        null,
                        null
                );
            }
        });
    }
}