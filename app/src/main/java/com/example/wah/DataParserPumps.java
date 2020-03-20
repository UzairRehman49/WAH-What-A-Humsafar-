package com.example.wah;

import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParserPumps  {

    SendNextPageToken sendNextPageToken;
    public static String nextPageToken;

    private HashMap<String, String> getPlace(JSONObject googlePlaceJSON)
    {
        HashMap<String, String> googlePlacesMap= new HashMap<>();
        String placeName= "-NA-";
        String vicinity= "-NA-";
        String latitude= "";
        String longitude="";
        String reference="";

        try {
         if(!googlePlaceJSON.isNull("name"))
         {

                 placeName= googlePlaceJSON.getString("name");
             }

         if(!googlePlaceJSON.isNull("vicinity"))
         {
            vicinity= googlePlaceJSON.getString("vicinity");
         }
            latitude= googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude= googlePlaceJSON.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference= googlePlaceJSON.getString("reference");

            googlePlacesMap.put("place_name", placeName);
            googlePlacesMap.put("vicinity", vicinity);
            googlePlacesMap.put("lat", latitude);
            googlePlacesMap.put("lng", longitude);
            googlePlacesMap.put("reference", reference);




        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        return googlePlacesMap;
    }

    private List<HashMap<String, String>>getPlaces(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String, String>> placelist = new ArrayList<>();
        HashMap<String, String> placeMap = null;

        for(int i = 0; i<count;i++)
        {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placelist.add(placeMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return placelist;
    }



    public List<HashMap<String, String>> parse(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        String next_token="";
        Log.d("json data", jsonData);

        try {

            jsonObject = new JSONObject(jsonData);

            if(jsonObject.has("next_page_token")) {
                next_token = jsonObject.getString("next_page_token");
                nextPageToken= next_token;
            }
           else
            {
               nextPageToken= null;
            }
            //sendNextPageToken.sendToken(next_token);

            Log.d("NEXT_PAGE_KA_TOKEN", next_token);
            jsonArray = jsonObject.getJSONArray("results");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }



    void setListener(SendNextPageToken obj)
    {
        sendNextPageToken=obj;
    }




}
