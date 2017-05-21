package com.example.searchfromfb;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class HttpGetRequest extends AsyncTask<String, Void, String> {

    //asyncTask to do in background
    @Override
    protected String doInBackground(String... params){

        String url = params[0];
        String results = null;


        try {

            //Create a URL object holding our url
            URL myUrl = new URL(url);

            //Create a connection
            HttpURLConnection connection=(HttpURLConnection) myUrl.openConnection();

            //Connect to our url
            connection.connect();

            //Create a new InputStreamReader
            InputStreamReader streamReader = new
                    InputStreamReader(connection.getInputStream());

            //Create a new buffered reader and String Builder
            BufferedReader reader = new BufferedReader(streamReader);
            StringBuilder stringBuilder = new StringBuilder();

            //Check if the line we are reading is not null
            String inputLine;
            while((inputLine = reader.readLine()) != null){
                stringBuilder.append(inputLine);
            }

            //Close our InputStream and Buffered reader
            reader.close();
            streamReader.close();

            //Set our result equal to our stringBuilder
            results = stringBuilder.toString();

            //convert to json object
            JSONObject jsonResult = new JSONObject(results);

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return results;
    }

    @Override
    protected void onPostExecute(String result){
        //retrieve the result here
        super.onPostExecute(result);

    }
}
