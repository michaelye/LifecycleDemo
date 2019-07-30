package com.michael.lifecycledemo;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity2 extends AppCompatActivity
{

    private TextView tvTitle;
    private MyLocationListener myLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTitle = findViewById(R.id.tvTitle);
        myLocationListener = new MyLocationListener(this, new MyLocationListener.OnLocationChangedListener()
        {
            @Override
            public void onChanged(double latitude, double longitude)
            {
                tvTitle.setText(latitude+":"+longitude);
            }
        });
        getLifecycle().addObserver(myLocationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MyLocationListener.PERMISSIONS_REQUEST_LOCATION:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    myLocationListener.getLocation();
                }
                else
                {
                    Toast.makeText(MainActivity2.this, "用户拒绝授权", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


}
