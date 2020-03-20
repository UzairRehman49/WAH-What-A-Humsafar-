package com.example.wah;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.kwabenaberko.openweathermaplib.constants.Units;
import com.kwabenaberko.openweathermaplib.implementation.OpenWeatherMapHelper;
import com.kwabenaberko.openweathermaplib.implementation.callbacks.CurrentWeatherCallback;
import com.kwabenaberko.openweathermaplib.models.currentweather.CurrentWeather;

import java.text.NumberFormat;

public class Weather extends AppCompatActivity {
    TextView CityName;
    TextView Description;
    TextView CurrentTemp;
    TextView MinTemp;
    TextView MaxTemp;
    TextView Humidity;
    TextView Wind;
    TextView Cloud;
    TextView Pressure;
    NumberFormat nf=NumberFormat.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        CityName = (TextView) findViewById(R.id.textViewCardCityName);
        Description =(TextView) findViewById(R.id.textViewCardWeatherDescription);
        CurrentTemp = (TextView)findViewById(R.id.textViewCardCurrentTemp);
        MinTemp = (TextView)findViewById(R.id.textViewCardMinTemp);
        MaxTemp = (TextView)findViewById(R.id.textViewCardMaxTemp);
        Humidity = (TextView) findViewById(R.id.textViewHumidity);
        Wind = (TextView)findViewById(R.id.textViewWind);
        Cloud = (TextView)findViewById(R.id.textViewCloudiness);
        Pressure = (TextView)findViewById(R.id.textViewPressure);
        OpenWeatherMapHelper helper = new OpenWeatherMapHelper(getString(R.string.OPEN_WEATHER_MAP_API_KEY));
        helper.setUnits(Units.METRIC );
        helper.getCurrentWeatherByGeoCoordinates(31.5204, 74.3587, new CurrentWeatherCallback() {
            @Override
            public void onSuccess(CurrentWeather currentWeather) {
                Log.v("The Weather API", "Coordinates: " + currentWeather.getCoord().getLat() + ", "+currentWeather.getCoord().getLon() +"\n"
                );
                Toast.makeText(getApplicationContext(),"Weather Call Successful",Toast.LENGTH_LONG).show();
                Description.setText(currentWeather.getWeather().get(0).getDescription());
                Humidity.setText(nf.format( currentWeather.getMain().getHumidity()));
                Pressure.setText(nf.format(currentWeather.getMain().getPressure()));
                //Cloud.setText(currentWeather.getClouds().toString());
                CurrentTemp.setText(nf.format(currentWeather.getMain().getTemp()));
                MaxTemp.setText((nf.format(currentWeather.getMain().getTempMax())));
                MinTemp.setText(nf.format(currentWeather.getMain().getTempMin()));
                CityName.setText(currentWeather.getName() + ", " + currentWeather.getSys().getCountry() );


            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.v("The Weather Failed", throwable.getMessage());
            }
        });

    }
}
