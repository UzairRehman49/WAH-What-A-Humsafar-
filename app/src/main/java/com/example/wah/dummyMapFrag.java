package com.example.wah;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;

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


public class dummyMapFrag extends Fragment implements  OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    Location lastKnownLocation;
    LatLng userLocation;
    private static final float DEFAULT_ZOOM = 15f;
    private EditText mSerachText;
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40,-168), new LatLng(71,136));
    private Address searchedLocation;
    private Polyline currentPolyline;
    Context mycontext;

    public dummyMapFrag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mycontext= container.getContext();


        View v=  inflater.inflate(R.layout.fragment_my_fragment_maps, container, false);

        //   setContentView(R.layout.activity_maps);
        mSerachText = (EditText) v.findViewById(R.id.edttxt_searchBar);
//        //
//        mGoogleApiClient = new GoogleApiClient
//                .Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this,this)
//                .build();

        //  mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient,LAT_LNG_BOUNDS, null);
        // mSerachText.setAdapter(mPlaceAutocompleteAdapter);
        mSerachText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {



                //mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter()

                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER)
                {
                    // execute searching
                    geoLocate();
                }

                return false;
            }
        });



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        return v;
    }

    private void geoLocate ()
    {
        String searchString = mSerachText.getText().toString();

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
            Address address = list.get(0);
            searchedLocation=address;
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()),DEFAULT_ZOOM, address.getAddressLine(0));

            LatLng searchedLatLong= new LatLng(address.getLatitude(), address.getLongitude());



            String url= getURL(userLocation, searchedLatLong, "driving" );
            TaskRequestDirections taskRequestDirections= new TaskRequestDirections();
            taskRequestDirections.execute(url);


            //     new FetchURL(mycontext).execute(url, "driving");

        }
    }


    private void moveCamera (LatLng latLng, float zoom, String title)
    {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        if (!title.equals("My Location")) {
            mMap.clear();
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
        }
        hideSoftKeyBoard();
    }

    private void hideSoftKeyBoard ()
    {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.clear();
                //mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, DEFAULT_ZOOM));

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {



            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if(Build.VERSION.SDK_INT<23)
        {
            //locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        }
        else
        {

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else
            {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListener);


                lastKnownLocation= locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

//                if(lastKnownLocation==null)
//                {
//                  Toast toast=   Toast.makeText(getApplicationContext(), "NULLLLLLLL", Toast.LENGTH_LONG);
//                  toast.show();
//                }
//
//                else {
                userLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                mMap.clear();
                // mMap.addMarker(new MarkerOptions().position(userLocation).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, DEFAULT_ZOOM));
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                //}
            }
        }

        // Add a marker in Sydney and move the camera

    }


    public String getURL(LatLng origin, LatLng dest, String directionMode)
    {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        //String str_origin= "origin=31.4760,74.3045";
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        //String str_dest = "destination=31.5102,74.3441";


        String sensor= "sensor=false";

        String mode = "mode=" + directionMode;

        String parameters= str_origin + "&" + str_dest + "&key=" + getString(R.string.google_maps_key);

        String output = "json";

        //  String url= "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        String url= "https://maps.googleapis.com/maps/api/directions/json?origin=31.5102,74.3441&destination=31.5102,74.3441&sensor=false&mode=driving&key=AIzaSyClejO43YfA2PIdP0OANYrgq2CmyupGFak";// + output + "?" + parameters;


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

    public class TaskRequestDirections extends AsyncTask <String, Void, String>{

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
