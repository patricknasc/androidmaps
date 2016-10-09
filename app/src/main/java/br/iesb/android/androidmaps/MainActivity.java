package br.iesb.android.androidmaps;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final int MAP_PERMISSION_ACCESS_COURSE_LOCATION = 9999;

    private GoogleMap mMap;
    LocationManager locationManager;
    private Marker yourMarker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(googleServicesAvailable()){
            Toast.makeText(this, "Carregando...", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_main);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if(isAvailable == ConnectionResult.SUCCESS){
            return true;
        } else if(api.isUserResolvableError(isAvailable)){ //Deu erro mas pode ser consertado pelo usuário
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Não foi possível se conectar ao Play Services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MAP_PERMISSION_ACCESS_COURSE_LOCATION);
        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 1, new LocationListener(){


                @Override
                public void onLocationChanged(Location location) {


                    if(yourMarker != null) { yourMarker.remove(); }
                    LatLng newPosition = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions newMarkerOptions = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Você");
                    yourMarker = mMap.addMarker(newMarkerOptions);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 30));

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });
        }

        LatLng iesbSul = new LatLng(-15.7571194, -47.8788442);
        mMap.addMarker(new MarkerOptions().position(iesbSul).title("IESB Sul"));

    }



}
