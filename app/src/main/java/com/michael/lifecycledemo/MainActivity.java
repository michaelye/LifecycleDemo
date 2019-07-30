package com.michael.lifecycledemo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{

    private LocationManager locationManager;
    private LocationListener locationListener;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iniComponent();
        tvTitle = findViewById(R.id.tvTitle);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(checkLocationPermission())
        {
            getLocation();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSIONS_REQUEST_LOCATION:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    getLocation();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "用户拒绝授权", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void iniComponent()
    {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                tvTitle.setText(location.getLatitude()+":"+location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras)
            {

            }

            @Override
            public void onProviderEnabled(String provider)
            {

            }

            @Override
            public void onProviderDisabled(String provider)
            {

            }
        };
    }

    public static final int PERMISSIONS_REQUEST_LOCATION = 10;

    public boolean checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                showExplainDialog();

            } else
            {
                requestLocationPermission();
            }
            return false;
        }
        else
        {
            return true;
        }
    }

    private void requestLocationPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
    }

    private void showExplainDialog()
    {
        new AlertDialog.Builder(this)
                .setTitle("权限确认")
                .setMessage("是否允许获取地理位置？")
                .setPositiveButton("同意", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        requestLocationPermission();
                    }
                })
                .create()
                .show();
    }

    private void getLocation()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, locationListener);
        }
    }
}
