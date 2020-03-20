package com.example.wah;

import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadURL
        {

    public String readURL(String myURL) throws IOException
    {
        String data="";
        InputStream inputStream =null;

        HttpURLConnection urlConnection=null;
        try {
            URL url = new URL(myURL);
            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream= urlConnection.getInputStream();

            BufferedReader br= new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb= new StringBuffer();

            String line= "";

            while((line=br.readLine())!=null)
            {
                sb.append(line);

            }

            data= sb.toString();
            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
          finally {


            if(inputStream==null)
            {
                Log.d("NOT ALLOWED", "NULL");
            }


           inputStream.close();

           urlConnection.disconnect();
        }

        return data;
    }

}