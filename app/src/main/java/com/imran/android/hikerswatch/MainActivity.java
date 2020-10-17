package com.imran.android.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationInfo(location);
            }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                updateLocationInfo(lastKnownLocation);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    private void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }


    @SuppressLint("SetTextI18n")
    public void updateLocationInfo(Location location) {
        TextView latitudeTextView = findViewById(R.id.latitudeText);
        TextView longitudeTextView = findViewById(R.id.longitudeText);
        TextView accuracyTextView = findViewById(R.id.accuracyText);
        TextView altitudeTextView = findViewById(R.id.altitudeText);
        TextView addressTextView = findViewById(R.id.addressText);

        latitudeTextView.setText("Latitude: " + Double.toString(location.getLatitude()));
        longitudeTextView.setText("Latitude: " + Double.toString(location.getLongitude()));
        accuracyTextView.setText("Latitude: " + Double.toString(location.getAccuracy()));
        altitudeTextView.setText("Latitude: " + Double.toString(location.getAltitude()));

        String address = "\nCould Not find address :(";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List <Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddress != null && listAddress.size() > 0) {
                address = "Address:\n";

                if (listAddress.get(0).getThoroughfare() != null) {
                    address += listAddress.get(0).getThoroughfare() + "\n";
                }

                if (listAddress.get(0).getThoroughfare() != null) {
                    address += listAddress.get(0).getThoroughfare() + "\n";
                }

                if (listAddress.get(0).getLocality() != null) {
                    address += listAddress.get(0).getLocality() + "\n";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}