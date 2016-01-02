package com.shirishkadam.currencyconverter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by felix on 28/12/15.
 */

public class GetExchageRates extends AsyncTask<String,Void,String>{

    Activity mactivity;

    public GetExchageRates(Activity mactivity) {
        this.mactivity = mactivity;
    }

    @Override
    protected String doInBackground(String... str_url) {

        InputStream is = null;
        int len = 5000;
        String json_data = "";

        try {

            URL url = new URL(str_url[0]);
            HttpURLConnection huconn = (HttpURLConnection)url.openConnection();
            huconn.setReadTimeout(10000);
            huconn.setConnectTimeout(15000);
            huconn.setRequestMethod("GET");
            huconn.setDoInput(true);

            huconn.connect();
            int response = huconn.getResponseCode();
            Log.d("Http Response: ",""+response);

            is = huconn.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            json_data = sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return json_data;
    }

    protected void onPostExecute(String exchage_rates){

        SharedPreferences sf = (SharedPreferences) mactivity.getSharedPreferences("Exchange_Rates", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();

        JSONParser parser = new JSONParser();
        try {

            JSONObject jb = (JSONObject) parser.parse(exchage_rates);
            JSONObject jb_query = (JSONObject) parser.parse(jb.get("query").toString());
            JSONObject jb_results = (JSONObject) parser.parse(jb_query.get("results").toString());
            JSONArray jb_rates = (JSONArray) parser.parse(jb_results.get("rate").toString());

            Iterator<JSONObject> it = jb_rates.iterator();

            while (it.hasNext()){

                JSONObject jb_value = it.next();

                String currency = jb_value.get("Name").toString();
                String rate = jb_value.get("Rate").toString();

                editor.putFloat(currency, Float.parseFloat(rate));
            }

            editor.commit();

            Toast.makeText(mactivity,"Exchange Rates Updated...!",Toast.LENGTH_SHORT).show();
            Log.d("Rates : ","Updated successfully");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
