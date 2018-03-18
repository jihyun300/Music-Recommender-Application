package com.example.anytimeanywhere;

import com.example.anytimeanywhere.manager.*;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class SplashActivity extends Activity {
	private static final int DELAY_TIME = 2000;
	int not_first = 0;
	UserManager uManager= UserManager.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		 new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	               if(fromStart() == 0){
	            	  if(hasPace() == 1)
	            		  startActivity(new Intent(SplashActivity.this, GetPaceActivity.class));
	            	  else
	            		  startActivity(new Intent(SplashActivity.this, MainActivity.class));
	               }else if(fromStart() == 1){	//splash
	            	   startActivity(new Intent(SplashActivity.this, TutorialActivity.class));
	               }
	               else{
	                  startActivity(new Intent(SplashActivity.this, MainActivity.class));
	               }
	               
	               overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	               finish();
	            }
	         }, DELAY_TIME);
	}
	
	
	private int hasPace(){
		SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences(this);
		int defaultValue=0;
		int keepcheck=sharedPref.getInt(getString(R.string.paceFrom),defaultValue);
		if(keepcheck==defaultValue){ //it's the first time
			return 1;
		}
		else{
			return 0;

		}
	}
	
	private int fromStart(){
		SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences(this);
		int defaultValue=0;
		int keepcheck=sharedPref.getInt(getString(R.string.splash),defaultValue);
		if(keepcheck==defaultValue){ //it's the first time
			return 1;
		}
		else{
			return 0;

		}
	}
	
}

