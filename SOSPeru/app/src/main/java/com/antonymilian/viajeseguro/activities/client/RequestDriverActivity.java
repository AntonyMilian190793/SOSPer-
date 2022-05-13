package com.antonymilian.viajeseguro.activities.client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.antonymilian.viajeseguro.R;
import com.antonymilian.viajeseguro.models.ClientBooking;
import com.antonymilian.viajeseguro.models.FCMBody;
import com.antonymilian.viajeseguro.models.FCMResponse;
import com.antonymilian.viajeseguro.providers.AuthProvider;
import com.antonymilian.viajeseguro.providers.ClientBookingProvider;
import com.antonymilian.viajeseguro.providers.GeofireProvider;
import com.antonymilian.viajeseguro.providers.GoogleApiProvider;
import com.antonymilian.viajeseguro.providers.NotificationProvieder;
import com.antonymilian.viajeseguro.providers.TokenProvider;
import com.antonymilian.viajeseguro.utils.DecodePoints;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class RequestDriverActivity extends AppCompatActivity {

    private LottieAnimationView mAnimation;
    private TextView mTextViewLokkingFor;
    private Button btnCancelRequest;
    private GeofireProvider mGeofireProvider;

    private String mExtraOrigin;
    private String mExtraDestination;
    private double mExtraOriginLat;
    private double mExtraOriginLng;
    private double mExtraDestinationLat;
    private double mExtraDestinationLng;
    private LatLng mOriginLatLng;
    private LatLng mDestinationLatLng;



    private double mRadius = 0.1;
    private boolean mDriverFound = false;
    private String mIdDriverFound = "";
    private LatLng mDriverFoundLatLng;

    private NotificationProvieder mNotificationProvider;
    private TokenProvider mTokenProvider;

    private ClientBookingProvider mClientBookingProvider;
    private AuthProvider mAuthProvider;

    private GoogleApiProvider mGoogleApiProvider;

    private ValueEventListener mListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_driver);

        mAnimation = findViewById(R.id.animation);
        mTextViewLokkingFor = findViewById(R.id.textViewLookingFor);
        btnCancelRequest = findViewById(R.id.btnCancelRequest);

        mAnimation.playAnimation();

        mExtraOrigin = getIntent().getStringExtra("origin");
        mExtraDestination = getIntent().getStringExtra("destination");
        mExtraOriginLat = getIntent().getDoubleExtra("origin_lat", 0);
        mExtraOriginLng = getIntent().getDoubleExtra("origin_lng", 0);
        mExtraDestinationLat = getIntent().getDoubleExtra("destination_lat", 0);
        mExtraDestinationLng = getIntent().getDoubleExtra("destination_lng", 0);
        mOriginLatLng = new LatLng(mExtraOriginLat, mExtraOriginLng);
        mDestinationLatLng = new LatLng(mExtraDestinationLat, mExtraDestinationLng);

        mGeofireProvider = new GeofireProvider("drivers_activos");

        mNotificationProvider = new NotificationProvieder();
        mTokenProvider = new TokenProvider();

        mClientBookingProvider = new ClientBookingProvider();
        mAuthProvider = new AuthProvider();

        mGoogleApiProvider = new GoogleApiProvider(RequestDriverActivity.this);

        getCloseDriver();
    }

    private void getCloseDriver(){
        mGeofireProvider.getActivesDrivers(mOriginLatLng, mRadius).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if(!mDriverFound){
                    mDriverFound = true;
                    mIdDriverFound = key;
                    mDriverFoundLatLng = new LatLng(location.latitude, location.longitude);
                    mTextViewLokkingFor.setText("CONDUCTOR ENCONTRADO\nESPERANDO RESPUESTA");
                    createClientBooking();
                    Log.d("DRIVER", "ID: "+ mIdDriverFound);
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                //ingresa cuando termina la busqueda del conductor en un radio de 0.1km
                if(!mDriverFound){
                    //se incrementa el radio
                    mRadius = mRadius + 0.1f;
                    //no se encontro ningun conductor
                    if(mRadius > 5){
                        mTextViewLokkingFor.setText("NO SE ENCONTRO CONDUCTOR");
                        Toast.makeText(RequestDriverActivity.this, "No se encontró un conductor", Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        getCloseDriver();
                    }
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void createClientBooking(){
        mGoogleApiProvider.getDirections(mOriginLatLng, mDriverFoundLatLng).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //se recibe la respuesta del servidor
                try {

                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);
                    JSONObject polylines = route.getJSONObject("overview_polyline");
                    String points = polylines.getString("points");

                    JSONArray legs = route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);
                    JSONObject distance = leg.getJSONObject("distance");
                    JSONObject duration = leg.getJSONObject("duration"); //duration_in_traffic para que sea sin trafico
                    String distanceText = distance.getString("text");
                    String durationText = duration.getString("text");

                    sendNotification(durationText, distanceText);


                }catch (Exception e){
                    Log.d("Error", "Error encontrado" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                //en caso falle la peticion al servidor
            }
        });

    }


    private void sendNotification(final String time, final String km) {
        mTokenProvider.getToken(mIdDriverFound).addListenerForSingleValueEvent(new ValueEventListener() {
            //el dataSnapshot consigue el valor del token del usuario
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String token = dataSnapshot.child("token").getValue().toString();
                    Map<String, String> map = new HashMap<>();
                    map.put("title", "SOLICITUD DE AYUDA A " + time + " DE TU POSICIÓN");
                    map.put("body",
                            "Están solicitando su ayuda a una distancia de " + km + "\n" +
                                    "Recoger en: " + mExtraOrigin + "\n" +
                                    "Destino: " + mExtraDestination
                    );
                    map.put("idClient", mAuthProvider.getId());
                    FCMBody fcmBody = new FCMBody(token, "high", "4500s", map);
                    mNotificationProvider.sendNotification(fcmBody).enqueue(new Callback<FCMResponse>() {
                        @Override
                        public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                            if(response.body() != null){
                                if(response.body().getSuccess() == 1){
                                    ClientBooking clientBooking = new ClientBooking(
                                            mAuthProvider.getId(),
                                            mIdDriverFound,
                                            mExtraDestination,
                                            mExtraOrigin,
                                            time,
                                            km,
                                            "create",
                                            mExtraOriginLat,
                                            mExtraOriginLng,
                                            mExtraDestinationLat,
                                            mExtraDestinationLng
                                    );
                                    mClientBookingProvider.create(clientBooking).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            checkStatusClientBooking();
                                           //Toast.makeText(RequestDriverActivity.this, "La petición se creó correctamente!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    //Toast.makeText(RequestDriverActivity.this, "La notificación se envió correctamente!", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(RequestDriverActivity.this, "No se pudo enviar su notificación!", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(RequestDriverActivity.this, "No se pudo enviar su notificación!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResponse> call, Throwable t) {
                            Log.d("Error", "Error" +  t.getMessage());
                        }
                    });
                }else{
                    Toast.makeText(RequestDriverActivity.this, "No se pudo enviar su notificación porque el conductor no tiene un token de sesión!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkStatusClientBooking() {
        mListener = mClientBookingProvider.getStatus(mAuthProvider.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String status = dataSnapshot.getValue().toString();
                    if(status.equals("accept")){
                        Intent intent = new Intent(RequestDriverActivity.this, MapClientBookingActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(status.equals("cancel")){
                        Toast.makeText(RequestDriverActivity.this, "No se aceptó su ayuda", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RequestDriverActivity.this, MapClientActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mListener != null){
            mClientBookingProvider.getStatus(mAuthProvider.getId()).removeEventListener(mListener);
        }

    }
}