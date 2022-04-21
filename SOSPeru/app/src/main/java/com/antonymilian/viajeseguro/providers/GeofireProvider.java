package com.antonymilian.viajeseguro.providers;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeofireProvider {

    private DatabaseReference mDataBase;
    private GeoFire mGiofare;

    public GeofireProvider(String reference){
        mDataBase = FirebaseDatabase.getInstance().getReference().child(reference);
        mGiofare = new GeoFire(mDataBase);
    }
    public void saveLocation(String idDriver, LatLng latLng){

        mGiofare.setLocation(idDriver, new GeoLocation(latLng.latitude, latLng.longitude));
    }

    public void removeLocation(String idDriver){
        mGiofare.removeLocation(idDriver);
    }

    public GeoQuery getActivesDrivers(LatLng latLng, double radius){
        GeoQuery geoQuery = mGiofare.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), radius);
        geoQuery.removeAllListeners();
        return geoQuery;
    }

    public DatabaseReference getDriverLocation(String idDriver){
        return mDataBase.child(idDriver).child("l");
    }

    public DatabaseReference isDriverWorking(String idDriver){
        return FirebaseDatabase.getInstance().getReference().child("drivers_trabajando").child(idDriver);
    }
}
