package com.example.wah;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class DashBoard extends AppCompatActivity {

    Post newPost;
    Boolean newPostFlag=false;
 // public static  ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new HomeFragment()).commit();

        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selected = null;

                    switch (menuItem.getItemId()){
                        case R.id.home:
                            selected = new HomeFragment();
                            break;
                        case R.id.favorite:
                            selected = new FavoritesFragment();
                            break;
                        case R.id.map:
                            selected = new MyFragmentMaps();
                            break;
                        case R.id.profile:
                            selected = new ProfileFragment();
                            break;
                        case R.id.budget:
                            selected = new TripPlan();

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selected).commit();
                    return true;
                }
            };

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this,"OnStart called", Toast.LENGTH_LONG).show();
        Intent intent = getIntent();
        newPostFlag=  intent.getBooleanExtra("PostMadeFlag", newPostFlag);

        if(newPostFlag==true) {


            SharedPreferences prefs = getSharedPreferences("PostObj", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = prefs.getString("user", "");
            newPost = gson.fromJson(json, Post.class);
           //

            Toast.makeText(getApplicationContext(), "CALLED AGAIN", Toast.LENGTH_LONG).show();
            Log.d("AGAIN", "CALLED AGAIN");



            //

            String src= newPost.getSource();
            Log.d("SOURCE", src);

            Fragment selected = null;
            selected = new ProfileFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selected).commit();

        }

        //  String objectUsername=obj.getUserName();


    }


}
