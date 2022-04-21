package com.antonymilian.viajeseguro.providers;

import android.content.Context;

import com.antonymilian.viajeseguro.R;
import com.antonymilian.viajeseguro.retrofit.IGoogleApi;
import com.antonymilian.viajeseguro.retrofit.RetrofitClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import retrofit2.Call;

public class GoogleApiProvider {

    private Context contex;

    public GoogleApiProvider(Context contex){
        this.contex = contex;

    }

    public Call<String> getDirections(LatLng originLatLng, LatLng destinationLatLng){
        String baseUrl = "https://maps.googleapis.com";
        String query = "/maps/api/directions/json?mode=driving&transit_routing_preferences=less_driving&"
                        + "origin=" + originLatLng.latitude + "," + originLatLng.longitude + "&"
                        + "destination=" + destinationLatLng.latitude + "," + destinationLatLng.longitude + "&"
                        + "departure_time=" + (new Date().getTime() + (60*60*1000)) + "&"
                        + "traffic_model=best_guess&"
                        + "key=" + contex.getResources().getString(R.string.google_maps_key);
        return RetrofitClient.getClient(baseUrl).create(IGoogleApi.class).getDirections(baseUrl + query);

    }
}
