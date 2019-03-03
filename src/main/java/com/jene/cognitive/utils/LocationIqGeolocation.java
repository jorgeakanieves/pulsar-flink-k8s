package com.jene.cognitive.utils;

import com.jene.cognitive.model.GeoLoc;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.*;

/**
 * @uthor Jorge Nieves
 */

public class LocationIqGeolocation {

    public static String urlService = "https://eu1.locationiq.com/v1/search.php?key=";
    public static String APIkey = "";

    public static GeoLoc getGeo (String address) throws IOException, org.json.simple.parser.ParseException{
        String jsonResponse = "";
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
//            System.out.println(urlService+APIkey+"&q="+ URLEncoder.encode(address)+"&format=json");
            HttpGet getRequest = new HttpGet(
                    urlService+APIkey+"&q="+ URLEncoder.encode(address)+"&format=json");
            getRequest.addHeader("accept", "application/json");

            HttpResponse response = httpClient.execute(getRequest);

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output = "";
//            System.out.println("Output from Server .... \n");
            while ((output = br.readLine()) != null) {
//                System.out.println(output);
                jsonResponse = output;
            }

            httpClient.getConnectionManager().shutdown();

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }

        JSONParser parser = new JSONParser();
        GeoLoc geoLoc = new GeoLoc();
        try {
            JSONArray places = ((JSONArray) parser.parse(jsonResponse));
            JSONObject item = (JSONObject) places.get(0);
            geoLoc.setLat(Double.parseDouble(item.get("lat").toString()));
            geoLoc.setLon(Double.parseDouble(item.get("lon").toString()));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return geoLoc;
    }
}