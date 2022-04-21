package com.antonymilian.viajeseguro.activities.client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.antonymilian.viajeseguro.R;
import com.antonymilian.viajeseguro.providers.GoogleApiProvider;
import com.antonymilian.viajeseguro.utils.DecodePoints;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailRequestActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;

    private double mExtraOriginLat;
    private double mExtraOriginLng;
    private double mExtraDestinationLat;
    private double mExtraDestinationLng;

    private LatLng mOriginLatLng;
    private LatLng mDesinationLatLng;

    private String mExtraOrigin;
    private String mExtraDestination;

    private GoogleApiProvider mGoogleApiProvider;

    private List<LatLng> mPolylineList;
    private PolylineOptions mPolylineOptions;

    private TextView mTextViewOrigin;
    private TextView mTextViewDestination;
    private TextView mTextViewTime;
    private TextView mTextViewDistance;

    private Button mButtonRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

        mExtraOriginLat = getIntent().getDoubleExtra("origin_lat", 0 );
        mExtraOriginLng = getIntent().getDoubleExtra("origin_lng", 0 );
        mExtraDestinationLat = getIntent().getDoubleExtra("destination_lat", 0 );
        mExtraDestinationLng = getIntent().getDoubleExtra("destination_lng", 0 );

        mExtraOrigin = getIntent().getStringExtra("origin");
        mExtraDestination = getIntent().getStringExtra("destination");

        mOriginLatLng = new LatLng(mExtraOriginLat, mExtraOriginLng);
        mDesinationLatLng = new LatLng(mExtraDestinationLat, mExtraDestinationLng);

        mGoogleApiProvider = new GoogleApiProvider(DetailRequestActivity.this);

        mTextViewOrigin = findViewById(R.id.textViewOrigin);
        mTextViewDestination = findViewById(R.id.textViewDestination);
        mTextViewTime = findViewById(R.id.textViewTime);
        mTextViewDistance = findViewById(R.id.textViewDistance);

        mButtonRequest = findViewById(R.id.btnRequestNow);

        mTextViewOrigin.setText(mExtraOrigin);
        mTextViewDestination.setText(mExtraDestination);

        mButtonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoRequestDriver();
            }
        });
    }

    private void gotoRequestDriver() {
        Intent intent = new Intent(DetailRequestActivity.this, RequestDriverActivity.class );
        intent.putExtra("origin_lat", mOriginLatLng.latitude);
        intent.putExtra("origin_lng", mOriginLatLng.longitude);
        intent.putExtra("origin", mExtraOrigin);
        intent.putExtra("destination", mExtraDestination);
        intent.putExtra("destination_lat", mDesinationLatLng.latitude);
        intent.putExtra("destination_lng", mDesinationLatLng.longitude);

        startActivity(intent);
        finish();
    }

    private void drawRoute(){
        mGoogleApiProvider.getDirections(mOriginLatLng, mDesinationLatLng).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                //se recibe la respuesta del servidor
                try {

                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);
                    JSONObject polylines = route.getJSONObject("overview_polyline");
                    String points = polylines.getString("points");
                    mPolylineList = DecodePoints.decodePoly(points);
                    mPolylineOptions = new PolylineOptions();
                    mPolylineOptions.color(Color.DKGRAY);
                    mPolylineOptions.width(13f);
                    mPolylineOptions.startCap(new SquareCap());
                    mPolylineOptions.jointType(JointType.ROUND);
                    mPolylineOptions.addAll(mPolylineList);
                    mMap.addPolyline(mPolylineOptions);

                    JSONArray legs = route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);
                    JSONObject distance = leg.getJSONObject("distance");
                    JSONObject duration = leg.getJSONObject("duration"); //duration_in_traffic para que sea sin trafico
                    String distanceText = distance.getString("text");
                    String durationText = duration.getString("text");
                    mTextViewTime.setText(durationText);
                    mTextViewDistance.setText(distanceText);


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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.addMarker(new MarkerOptions().position(mOriginLatLng).title("Origen").icon(BitmapDescriptorFactory.fromResource(R.drawable.icons_marcador_red)));
        mMap.addMarker(new MarkerOptions().position(mDesinationLatLng).title("Destino").icon(BitmapDescriptorFactory.fromResource(R.drawable.icons_marcador_blue)));

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                .target(mOriginLatLng)
                .zoom(14f)
                .build()
        ));
        drawRoute();
    }
}