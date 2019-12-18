package com.example.ejerciciointegradormercadoesclavo.view;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ejerciciointegradormercadoesclavo.R;
import com.example.ejerciciointegradormercadoesclavo.model.Articulo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public static final String LONGITUD = "longitud";
    public static final String LATITUD = "latitud";

    private GoogleMap mMap;
    private String longitud, latitud;
    private Double lon, lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        lon = Double.valueOf(longitud = bundle.getString(LONGITUD));
        lat = Double.valueOf(latitud = bundle.getString(LATITUD));
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng vendedor = new LatLng(lat,lon);
        mMap.addMarker(new MarkerOptions().position(vendedor).title("Aqui se encuentra el vendedor!!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(vendedor));
    }
}
