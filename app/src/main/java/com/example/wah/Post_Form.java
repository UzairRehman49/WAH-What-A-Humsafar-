package com.example.wah;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Post_Form extends AppCompatActivity {

    String source;
    String finalDest;
    String numOfStops;
    String daysOfTrip;
    String dateOfTrip;
    EditText edttxt_source;
    EditText edttxt_dest;
    EditText edttxt_numOfStops;
    EditText edttxt_numOfDays;
    EditText edttxt_startDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__form);

         edttxt_source= (EditText) findViewById(R.id.postForm_source);
         edttxt_dest=  (EditText) findViewById(R.id.postForm_destination);
         edttxt_numOfStops= (EditText)findViewById(R.id.postForm_numOfStops);
         edttxt_startDate= (EditText)findViewById(R.id.postForm_startDate);
         edttxt_numOfDays= (EditText)findViewById(R.id.postForm_numOfDays);






        Button nxt_btn;

        nxt_btn =(Button) findViewById(R.id.next_btn);


        nxt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), SubFormsActivity.class );
                source= edttxt_source.getText().toString();
                finalDest= edttxt_dest.getText().toString();
                numOfStops = edttxt_numOfStops.getText().toString();
                daysOfTrip = edttxt_numOfDays.getText().toString();
                dateOfTrip = edttxt_startDate.getText().toString();
                i.putExtra("Source", source);
                i.putExtra("Final Destination", finalDest);
                i.putExtra("Num of Stops", numOfStops);
                i.putExtra("Num of days", daysOfTrip);
                i.putExtra("Start date", dateOfTrip);
                startActivity(i);

            }
        });
    }
}
