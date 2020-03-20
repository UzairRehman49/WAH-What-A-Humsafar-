package com.example.wah;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SubFormsActivity extends AppCompatActivity {

    String source;
    String finalDest;
    String numOfStops;
    String numOfdays;
    String startdate;
    String dest_transport;
    String dest_stay;
    int dest_cost;
    int numOfStopsVal;
    int startDate;
    int numOfDays;
    TextView edttext_stopNum;
    TextView edttext_numDays;
    TextView edttext_dateStart;
    RecyclerView recyclerView;
    TextView dest_edttext_transport;
    TextView dest_edttext_stay;
    TextView dest_edttext_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_forms);


        edttext_stopNum= findViewById(R.id.stopNum);
        edttext_numDays=findViewById(R.id.postForm_numOfDays);
        edttext_dateStart=findViewById(R.id.postForm_startDate);

        dest_edttext_transport = findViewById(R.id.dest_transport_txtview);
        dest_edttext_stay= findViewById(R.id.dest_stay_txtview);
        dest_edttext_cost= findViewById(R.id.dest_price_txtview);


        source = getIntent().getStringExtra("Source");
        finalDest = getIntent().getStringExtra("Final Destination");
        numOfStops= getIntent().getStringExtra("Num of Stops");
        //Toast.makeText(this,numOfStops,Toast.LENGTH_LONG).show();
        numOfStopsVal= Integer.parseInt(numOfStops);
        numOfdays=getIntent().getStringExtra("Num of days");
        numOfDays=Integer.parseInt(numOfdays);
        startdate=getIntent().getStringExtra("Start date");
        startDate=Integer.parseInt(startdate);






        TextView finaldestname;
        finaldestname = findViewById(R.id.final_dest_name);
        finaldestname.setText(finalDest);

        recyclerView = findViewById(R.id.Recycler_view_subforms);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));

        ArrayList<ProfilePosts2> allPosts = new ArrayList<ProfilePosts2>();

        // ProfilePosts2 post2 = new ProfilePosts2("Naran", "Jeep", "Fairy Meadows", "Rs.20,000");
        //ProfilePosts2 post3 = new ProfilePosts2("Swat", "Bike", "HotelIn", "Rs.15,000");
        //ProfilePosts2 post4 = new ProfilePosts2("Murree", "Car", "Pine Hotel", "Rs.13,000");
        //ProfilePosts2 [] posts = {post1,post2,post3,post4};


        //   numOfStopsVal=2;
        for(int i=0; i<numOfStopsVal; i++) {

            //   edttext_stopNum.setText(i);
            ProfilePosts2 post1 = new ProfilePosts2(i+1,"Enter Stop Name", " Enter Transport Type (Car, Jeep, Bus or OnFoot)", "Where did you stay?", "Enter Amount spent on Transport + Stay");
            allPosts.add(post1);

        }
        // allPosts.add(post2);
        // allPosts.add(post3);
        // allPosts.add(post4);

        recyclerView.setAdapter(new Form2PostAdapter(allPosts, numOfStopsVal, source, finalDest));

        //saving post data

        Button makePostBtn= findViewById(R.id.MakePostButton);

        makePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ArrayList<Stop> stops = new ArrayList<Stop>();
                Boolean postMaker = true;




                dest_transport= dest_edttext_transport.getText().toString();
                dest_stay= dest_edttext_stay.getText().toString();
                String dummy_cost=dest_edttext_cost.getText().toString();
                dest_cost=Integer.parseInt(dummy_cost);


                EditText dest_name, transport, stay, costOfTransport;



                for (int i=0; i<numOfStopsVal; i++)
                {
                    View v = recyclerView.getChildAt(i);

                    dest_name = v.findViewById(R.id.dest_name);
                    transport = v.findViewById(R.id.transport_txtview);
                    stay = v.findViewById(R.id.stay_txtview);
                    costOfTransport = v.findViewById(R.id.price_txtview);

                    String strDest= dest_name.getText().toString();
                    String strTrans= transport.getText().toString();
                    String strStay = stay.getText().toString();
                    String dumCostOfTransport = costOfTransport.getText().toString();



                    if (strDest.equals(null )|| strTrans.equals(null )|| strStay.equals(null )|| dumCostOfTransport.equals(null ))
                    {
                        // show error message
                        postMaker = false;
                        break;
                    }
                    else
                    {
                        int strCostOfTransport= Integer.parseInt(dumCostOfTransport);
                        Stop stop = new Stop(strDest,strTrans, strStay,0, strCostOfTransport );
                        stops.add(stop);
                    }

                }
                if (postMaker == true) {
                    Stop fDest = new Stop(finalDest, dest_transport, dest_stay,dest_cost,0);
                    Post post = new Post(source, fDest, stops, numOfDays, startDate);

                    SharedPreferences.Editor editor = getSharedPreferences("PostObj", MODE_PRIVATE).edit();

                    Gson gson = new Gson();
                    String json = gson.toJson(post);
                    editor.putString("user", json);
                    editor.commit();

                    Intent intent = new Intent ( getApplicationContext() , DashBoard.class );
                    intent.addFlags ( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    intent.putExtra("PostMadeFlag", true);
                    startActivity ( intent );

                }



            }
        });




    }

}
