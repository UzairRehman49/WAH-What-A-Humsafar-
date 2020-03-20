package com.example.wah;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.android.PolyUtil;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Object;

import java.util.HashMap;
import java.util.List;

import static com.example.wah.DataParserPumps.nextPageToken;


public class MyFragmentMaps extends Fragment implements OnMapReadyCallback, TaskLoadedCallback, NotifyPumpsFetched {

//    private Context context = getActivity();


    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;


    //vars
    private boolean mLocationPermissionGranted = false;
    private GoogleMap mMap;
    int PROXIMITY_RADIUS= 10000;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LatLng userLocation;
    private Address searchedLocation;
    private Polyline currentPolyline;
    private Context mycontext;
    private Button btn_getDirection;
    private Button btn_reCenter;
    private Button btn_getPetrolPumps;
    private LatLng searchedLatLong;
    ArrayList points;
    ArrayList route_line;
   // private String nextPageToken;

   // public PolyUtil polyUtil;
    PolylineOptions polylineOptions =null;


    //widgets
    private EditText mSearchText;
    AutocompleteSupportFragment a;
    PlacesClient placesClient;

    LocationManager locationManager;
    LocationListener locationListener;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        mycontext= container.getContext();
        View view = inflater.inflate(R.layout.fragment_my_fragment_maps, container, false);
        btn_getDirection= view.findViewById(R.id.btn_getDirection);
        btn_reCenter= view.findViewById(R.id.btn_reCenter);
        btn_getPetrolPumps= view.findViewById(R.id.btn_getPetrolPumps);

        btn_getDirection.setVisibility(View.GONE);
        btn_getPetrolPumps.setVisibility(View.GONE);
        //  btn_reCenter.setVisibility(View.GONE);
        getLocationPermission();

        route_line = new ArrayList();
        mSearchText = view.findViewById(R.id.edttxt_searchBar);
       // initMap();
        String apiKey= "AIzaSyClejO43YfA2PIdP0OANYrgq2CmyupGFak";


        if(!Places.isInitialized())
        {
            Places.initialize(getActivity(), apiKey);
        }

        placesClient= Places.createClient(getActivity());



        final AutocompleteSupportFragment autocompleteSupportFragment =  (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);


        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        autocompleteSupportFragment.getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int actionId, KeyEvent keyEvent) {
                if ((actionId == EditorInfo.IME_ACTION_SEARCH)
                        || (actionId == EditorInfo.IME_ACTION_DONE)
                        || (keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                        || (keyEvent.getAction() == KeyEvent.KEYCODE_ENTER))
                {
                    // execute searching
                    btn_getDirection.setVisibility(View.VISIBLE);
                    geoLocate();
                    hideSoftKeyboard();
                    //   return true;
                }


                return false;
            }
        });
        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                final LatLng newlatLng= place.getLatLng();


                moveCamera(newlatLng,DEFAULT_ZOOM, place.getName());
                searchedLatLong= newlatLng;
                btn_getDirection.setVisibility(View.VISIBLE);





            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });


        a = autocompleteSupportFragment;




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
                    btn_getDirection.setVisibility(View.VISIBLE);
                    geoLocate();
                    hideSoftKeyboard();
                    //   return true;
                }


                return false;
            }
        });


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

                //       btn_getDirection.setVisibility(View.GONE);



                //        }

            }
        });


//            btn_reCenter.setVisibility(View.VISIBLE);

        btn_reCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                moveCamera(new LatLng(userLocation.latitude, userLocation.longitude), DEFAULT_ZOOM, "My Location" );

            }
        });

        btn_getPetrolPumps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getPetrolPumps();
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

            //      btn_getDirection.setVisibility(View.VISIBLE);




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

               // getDeviceLocation();
               // mMap.setMyLocationEnabled(true);
                //    mMap.getUiSettings().setMyLocationButtonEnabled(false);

               // init();
                return;
            }
            mMap.setMyLocationEnabled(true);
           // getDeviceLocation();
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
                initMap();
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
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                        {
                            mLocationPermissionGranted =false;
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





            for(List<HashMap<String, String>> path: lists)
            {

                points = new ArrayList();
                polylineOptions= new PolylineOptions();

                for (HashMap<String, String> point: path)

                {
                    double lat= Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    route_line.add(new LatLng(lat, lon));
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
                btn_getPetrolPumps.setVisibility(View.VISIBLE);


            }
            else
            {
                Toast.makeText(mycontext, "Direction not found", Toast.LENGTH_SHORT).show();

            }

        }
    }


    public void getPetrolPumps()
    {

       // mMap.clear();
        String petrolPumps= "gas_station";


        Double latitude= userLocation.latitude;
        Double longitude= userLocation.longitude;
        String url= getURLPumps(latitude, longitude, petrolPumps);
        Object dataTransfer[]= new Object[2];
        dataTransfer[0]= mMap;
        dataTransfer[1]= url;

        GetNearbyPumpsData getNearbyPumpsData= new GetNearbyPumpsData();


        getNearbyPumpsData.setListener(this);

        getNearbyPumpsData.execute(dataTransfer);
        Toast.makeText(getActivity().getApplicationContext(), "Showing Nearby Petrol Pumps", Toast.LENGTH_LONG ).show();



    }

    private String getURLPumps(double latitude, double longitude, String nearbyPlace)
    {

        String key= getString(R.string.google_maps_key);
        StringBuilder googlePlacesURL= new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesURL.append("location="+latitude+","+longitude);
        googlePlacesURL.append("&radius="+PROXIMITY_RADIUS);
        //googlePlacesURL.append("&rankby=distance");
        googlePlacesURL.append("&type="+nearbyPlace);
        googlePlacesURL.append("&sensor=true");
        googlePlacesURL.append("&key="+key);

        Log.d("petrol pumps url", googlePlacesURL.toString());

        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=31.5004498,74.350971&radius=13000&type=gas_station&sensor=true&key=AIzaSyClejO43YfA2PIdP0OANYrgq2CmyupGFak




   //    String x= "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=31.5204,74.3587&radius=10000"
        return googlePlacesURL.toString();
    }



    @Override
    public void notifyPumpsFetched(ArrayList<LatLng> pumps) {


        MarkerOptions markerOptions = new MarkerOptions();
        // Toast.makeText(getActty(), "RUNNING NOTIFY", Toast.LENGTH_LONG).show();

        if(route_line!=null && pumps!=null)
        {
            Log.d("notifyCALLED", "notify called");
            Toast.makeText(getActivity(), "RUNNING NOTIFY", Toast.LENGTH_LONG).show();
            Boolean isOnPath=false;
            int x= pumps.size();
            Log.d("pumps received", String.valueOf(x) );
            for(int i=0; i<pumps.size();i++) {

                //  Log.d("hello", route_line.toString());
                //Toast.makeText(getActivity(), i, Toast.LENGTH_SHORT).show();
                LatLng currLatLng= pumps.get(i);

               // Log.d("notifyCALLED"," "+i);
                isOnPath= PolyUtil.isLocationOnPath(currLatLng, route_line, true, 500);
                //  isOnPath= PolyUtil.isLocationOnEdge(currLatLng, route_line, true, 500);
                if(isOnPath==true)
                {
                    Log.d("notifyCALLED"," "+i);
                    LatLng latLng = new LatLng( currLatLng.latitude, currLatLng.longitude);
                    markerOptions.position(latLng);
                    //   markerOptions.title(placeName + " : "+ vicinity);
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));


                    mMap.addMarker(markerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(10));



                }



            }

        }




        if(nextPageToken!=null && !nextPageToken.isEmpty() && nextPageToken!="" && nextPageToken.length()>4)
        {

            try {



                Thread.sleep(3000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getNextPagePetrolPumps();
        }




    }


    private String getURLPumps2(double latitude, double longitude, String nearbyPlace)
    {

        String key= getString(R.string.google_maps_key);
        StringBuilder googlePlacesURL= new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesURL.append("location="+latitude+","+longitude);
        googlePlacesURL.append("&radius="+PROXIMITY_RADIUS);
        googlePlacesURL.append("&type="+nearbyPlace);
        googlePlacesURL.append("&sensor=true");
        googlePlacesURL.append("&key="+key);
        googlePlacesURL.append("&pagetoken="+nextPageToken);
        googlePlacesURL.append("");

        Log.d("petrol pumps url", googlePlacesURL.toString());

        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=31.4760,74.3045&radius=5000&type=gas_station&sensor=true&key=AIzaSyClejO43YfA2PIdP0OANYrgq2CmyupGFak




        //    String x= "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=31.5204,74.3587&radius=10000"
        return googlePlacesURL.toString();
    }

    public void getNextPagePetrolPumps()
    {

        // mMap.clear();
        String petrolPumps= "gas_station";


        Double latitude= userLocation.latitude;
        Double longitude= userLocation.longitude;
        String url= getURLPumps2(latitude, longitude, petrolPumps);
        Object dataTransfer[]= new Object[2];
        dataTransfer[0]= mMap;
        dataTransfer[1]= url;

        GetNearbyPumpsData getNearbyPumpsData= new GetNearbyPumpsData();
        getNearbyPumpsData.setListener(this);
        getNearbyPumpsData.execute(dataTransfer);
        Toast.makeText(getActivity().getApplicationContext(), "Showing Nearby Petrol Pumps", Toast.LENGTH_LONG ).show();



    }






}

