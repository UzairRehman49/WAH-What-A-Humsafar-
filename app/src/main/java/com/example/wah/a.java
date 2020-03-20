package com.example.wah;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.KeyEvent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class a extends Fragment implements OnMapReadyCallback, TaskLoadedCallback {

//    private Context context = getActivity();


   private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
   private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
   private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
   private static final float DEFAULT_ZOOM = 15f;


   //vars
   private boolean mLocationPermissionGranted = false;
   private GoogleMap mMap;
   private FusedLocationProviderClient mFusedLocationProviderClient;
   private LatLng userLocation;
   private Address searchedLocation;
    private Polyline currentPolyline;
   private Context mycontext;
   private Button btn_getDirection;
   private Button btn_reCenter;
   private LatLng searchedLatLong;

   //widgets
    private EditText mSearchText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        mycontext= container.getContext();
        View view = inflater.inflate(R.layout.fragment_my_fragment_maps, container, false);
        btn_getDirection= view.findViewById(R.id.btn_getDirection);
        btn_reCenter= view.findViewById(R.id.btn_reCenter);
        btn_getDirection.setVisibility(View.GONE);
        btn_reCenter.setVisibility(View.GONE);
        getLocationPermission();
        mSearchText = view.findViewById(R.id.edttxt_searchBar);

        initMap();
       // onMapReady(mMap);
        //init();
        return view;
    }

    private void init ()
    {
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent keyEvent) {
                if ((actionId == EditorInfo.IME_ACTION_SEARCH)
                        || (actionId == EditorInfo.IME_ACTION_DONE)
                        || (keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                        || (keyEvent.getAction() == KeyEvent.KEYCODE_ENTER))
                {
                    // execute searching
                    geoLocate();
                    hideSoftKeyboard();
                 //   return true;
                }


                return false;
            }
        });



    }

    private void geoLocate ()
    {
        String searchString = mSearchText.getText().toString();


        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> list = new ArrayList<>();

        try{
            list = geocoder.getFromLocationName(searchString,1);
        }
        catch (IOException e)
        {

        }

        if (list.size()>0)
        {
            mMap.clear();
            Address address = list.get(0);
            searchedLocation = address;
            InputMethodManager inputManager = (InputMethodManager) mycontext.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            moveCamera(new LatLng(address.getLatitude(),address.getLongitude()),DEFAULT_ZOOM, address.getAddressLine(0));
            searchedLatLong = new LatLng(address.getLatitude(), address.getLongitude());

            btn_getDirection.setVisibility(View.VISIBLE);

            btn_getDirection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    //               if(userLocation==null)
                    //              {
                    //                  Toast toast=   Toast.makeText(mycontext, "userLocation null while getting direction", Toast.LENGTH_LONG);
                    //             }
                    //               else if (searchedLatLong==null)
                    //              {
                    //                Toast toast=   Toast.makeText(mycontext, "searchedLocation null while getting direction", Toast.LENGTH_LONG);

                    //           }

                    //          else {
                    String url = getURL(userLocation, searchedLatLong, "driving");
                    TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                    taskRequestDirections.execute(url);

                    btn_getDirection.setVisibility(View.GONE);



                    //        }

                }
            });


            btn_reCenter.setVisibility(View.VISIBLE);

            btn_reCenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    moveCamera(new LatLng(userLocation.latitude, userLocation.longitude), DEFAULT_ZOOM, "My Location" );

                }
            });




        }

    }

    @Override
    public void onMapReady (GoogleMap googleMap)
    {
        mMap = googleMap;

        if (mLocationPermissionGranted)
        {
             getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }
            mMap.setMyLocationEnabled(true);
            //    mMap.getUiSettings().setMyLocationButtonEnabled(false);

            init();

        }
    }


    private void getDeviceLocation ()
    {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try{
            if (mLocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();

                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful())
                        {
                            Location currentLocation = (Location) task.getResult();
                            userLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } catch (SecurityException e)
        {

        }
    }

    private void moveCamera (LatLng latLng, float zoom, String title)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        if (!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }

        hideSoftKeyboard();
    }

    private void hideSoftKeyboard ()
    {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void initMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                        .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission ()
    {
        String [] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),FINE_LOCATION )== PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),COURSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
            {
                mLocationPermissionGranted = true;
            }
            else
            {
                ActivityCompat.requestPermissions(getActivity(),permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else
        {
            ActivityCompat.requestPermissions(getActivity(),permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int [] grantResults)
    {
        mLocationPermissionGranted = false;

        switch (requestCode)
        {
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0 )
                {
                    for (int i=0; i<grantResults.length; i++)
                    {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                        {
                            mLocationPermissionGranted =true;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    //initialize our map

                    initMap();
                }

            }
        }
    }


//
//    public boolean isServicesOK ()
//    {
//        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());
//
//        if (available == ConnectionResult.SUCCESS)
//        {
//            // user an make requests
//            return true;
//        }
//        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available))
//        {
//            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
//            dialog.show();
//        }
//        else
//        {
//            Toast.makeText(getActivity(),"You can't make map requests", Toast.LENGTH_SHORT).show();
//        }
//
//        return false;
///    }



    public String getURL(LatLng origin, LatLng dest, String directionMode)
    {

        String str_origin = "origin=" + Double.toString(origin.latitude) + "," + Double.toString(origin.longitude);
        //String str_origin= "origin=31.4760,74.3045";
        String str_dest = "destination=" + Double.toString(dest.latitude) + "," + Double.toString(dest.longitude);
        //String str_dest = "destination=31.5102,74.3441";


        String sensor= "sensor=false";

        String mode = "mode=" + directionMode;

        String parameters= str_origin + "&" + str_dest + "&key=" + getString(R.string.google_maps_key);

        String output = "json";


        String url= "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        // String url= "https://maps.googleapis.com/maps/api/directions/json?origin=31.5102,74.3441&destination=31.5102,74.3441&sensor=false&mode=driving&key=AIzaSyClejO43YfA2PIdP0OANYrgq2CmyupGFak";// + output + "?" + parameters;

        // Toast.makeText(mycontext, url, Toast.LENGTH_SHORT).show();
        //https://maps.googleapis.com/maps/api/directions/json?origin=31.4760,74.3045&destination=lat,long&sensor=false&mode=driving&key=YourKey

        return url;



    }

    private String requestDirection  (String reqUrl) throws IOException
    {
        String responseString = "";
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            // Get the response result
            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();
            String line = "";
            while ((line=bufferedReader.readLine())!= null)
            {
                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (inputStream!=null)
            {
                inputStream.close();
            }
            httpURLConnection.disconnect();
        }
        return responseString;
    }

    @Override
    public void onTaskDone(Object... values) {

        if(currentPolyline!=null)
            currentPolyline.remove();

        currentPolyline= mMap.addPolyline((PolylineOptions) values[0]);



    }

    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String responseString = "";
            try{
                responseString = requestDirection(strings[0]);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TaskParser taskParser= new TaskParser();
            taskParser.execute(s);



        }
    }

    public  class TaskParser extends  AsyncTask<String, Void, List<List<HashMap<String, String>>>>
    {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {

            JSONObject jsonObject=null;
            List<List<HashMap<String, String>>> routes=null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser= new DirectionsParser();
                routes= directionsParser.parse(jsonObject);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {

            ArrayList points = null;
            PolylineOptions polylineOptions =null;

            for(List<HashMap<String, String>> path: lists)
            {
                points = new ArrayList();
                polylineOptions= new PolylineOptions();

                for (HashMap<String, String> point: path)
                {
                    double lat= Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat, lon));

                }
                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);



            }

            if(polylineOptions!=null)
            {
                mMap.addPolyline(polylineOptions);


            }
            else
            {
                Toast.makeText(mycontext, "Direction not found", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
