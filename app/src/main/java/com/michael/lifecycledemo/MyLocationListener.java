package com.michael.lifecycledemo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

/**
 * 获取用户的地理位置
 */
public class MyLocationListener implements LifecycleObserver
{
    private String TAG = this.getClass().getName();
    private Activity context;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private OnLocationChangedListener onLocationChangedListener;
    public static final int PERMISSIONS_REQUEST_LOCATION = 10;
    private Dialog dialog;

    public MyLocationListener(Activity context, OnLocationChangedListener onLocationChangedListener)
    {
        this.context = context;
        this.onLocationChangedListener = onLocationChangedListener;
        iniComponent();
    }

    private void iniComponent()
    {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                MyLocationListener.this.onLocationChangedListener.onChanged(location.getLatitude(), location.getLongitude());
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

    /**
     * 使用注解  @OnLifecycleEvent 来表明该方法需要监听指定的生命周期事件
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void startGetLocation()
    {
        Log.d(TAG, "startGetLocation()");
        if(checkLocationPermission())
        {
            getLocation();
        }
    }

    /**
     * 使用注解  @OnLifecycleEvent 来表明该方法需要监听指定的生命周期事件
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void stopGetLocation()
    {
        Log.d(TAG, "stopGetLocation()");
        locationManager.removeUpdates(locationListener);
        dismissDialogIfShowing();
    }

    public boolean checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION))
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

    private void showExplainDialog()
    {
        dialog = new AlertDialog.Builder(context)
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
                .create();
        dialog.show();
    }

    private void dismissDialogIfShowing()
    {
        if(dialog != null && dialog.isShowing())
        {
            dialog.dismiss();
        }
    }

    public void getLocation()
    {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1, locationListener);
        }
    }

    private void requestLocationPermission()
    {
        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
    }

    public interface OnLocationChangedListener
    {
        void onChanged(double latitude, double longitude);
    }
}
