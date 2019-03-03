package com.jene.cognitive.utils;

import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.jene.cognitive.model.GeoLoc;
import com.google.maps.GeoApiContext;

import java.io.IOException;

/**
 * @uthor Jorge Nieves
 */

public class GoogleGeolocation {

    public static String APIkey = "";

    public static GeoLoc getGeo (String address) throws InterruptedException, ApiException{
        GeoLoc geoLoc = null;
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(APIkey)
                .build();
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context, address).await();
            geoLoc.setLat(results[0].geometry.location.lat);
            geoLoc.setLon(results[0].geometry.location.lng);
        } catch (IOException e) {
            // In case of error with Google Maps API we'll return empty value
            System.out.println(e.getMessage());
        }
        return geoLoc;

    }
}
