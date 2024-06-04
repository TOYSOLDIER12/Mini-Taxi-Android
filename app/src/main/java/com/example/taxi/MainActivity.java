package com.example.taxi;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap gMap;
    private FirebaseFirestore db;
    private Button requestTaxiButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.mapView);
        requestTaxiButton = findViewById(R.id.requestTaxiButton);
        db = FirebaseFirestore.getInstance();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        requestTaxiButton.setOnClickListener(view -> requestTaxi());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        LatLng initialLocation = new LatLng(-34, 151);
        gMap.moveCamera(CameraUpdateFactory.newLatLng(initialLocation));
        fetchTaxis();
    }

    private void fetchTaxis() {
        db.collection("taxis").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot result = task.getResult();
                if (result != null) {
                    for (DocumentSnapshot document : result) {
                        LatLng taxiLocation = new LatLng(document.getGeoPoint("currentLocation").getLatitude(),
                                document.getGeoPoint("currentLocation").getLongitude());
                        gMap.addMarker(new MarkerOptions().position(taxiLocation).title(document.getString("driverName")));
                    }
                }
            }
        });
    }

    private void requestTaxi() {
        // Handle taxi request logic here
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
