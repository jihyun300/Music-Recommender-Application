package com.example.anytimeanywhere;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

/**
 * Created by Buru on 2014-10-28.
 * I think that this class is used in tutorial and music player
 */
public class GPSTracker extends Service implements LocationListener {

    private final Context mContext;

    //flag for GPS status
    boolean isGPSEnabled = false;

    //flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;
    double speed;

    //the minimum distance to change updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

    //the minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000*1*1;

    //declaring a location Manager
    protected LocationManager locationManager;

    public GPSTracker(Context context){
        this.mContext=context;
        getLocation();
    }

    public Location getLocation(){
        try{
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if(!isGPSEnabled && !isNetworkEnabled){
                //no network provider is enabled
            }
            else{
                this.canGetLocation = true;
                //First get location from Network Provider
                if(isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                    Log.d("Network", "Network");
                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null){
                            latitude=location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if(isGPSEnabled){
                    if(location == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if(location != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location != null){
                                latitude=location.getLatitude();
                                longitude=location.getLongitude();
                            }
                        }
                    }
                }
            }
        }

        catch (Exception e){
            e.printStackTrace();
        }
        return location;
    }

    public double getLatitude(){
        if(location != null){
            latitude=location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if(location != null){
            longitude=location.getLongitude();
        }
        return longitude;
    }
    
    public double getSpeed(Location[] locList){
    	double speed = 0;
    	double distance = 0;
    	for(int i=5;i<10;i++){
    		distance+=locList[i].distanceTo(locList[i+1]);
    	}
    	speed = distance*60/50;
    	return speed;
    }

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSTracker.this);
        }
    }

    public boolean isCanGetLocation(){
        return this.canGetLocation;
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS function is turn off");

        alertDialog.setMessage("Do you want to continue with GPS settings page?");
        
        alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
               dialog.cancel();
            }
        });

        alertDialog.setNegativeButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        alertDialog.show();
    }
    
    /*public double getSpeed(Location loc1, Location loc2){
    	double tempSpeed;
    	
    	return  tempSpeed;
    }*/



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}


