package com.example.anytimeanywhere;

import java.io.File;
import com.example.anytimeanywhere.manager.UserManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class GetPaceActivity extends Activity {

	MediaPlayer mPlayer;
	int mState;
	public static final int STATE_IDLE = 1;
	public static final int STATE_INITIALIZED = 2;
	public static final int STATE_PREPARED = 3;
	public static final int STATE_STARTED = 4;
	public static final int STATE_PAUSED = 5;
	public static final int STATE_COMPLETED = 6;
	public static final int STATE_STOPPED = 7;
	public static final int STATE_ERROR = 8;
	Handler mHandler = new Handler();
	AudioManager audioManager;
	File mSavedFile;
	int times = 0;
	double speed;
	Location locList[] = new Location[12];
	String weatherInfo; // store weather information 
	
	ImageView startFont;
	Button btn;
	Button skipBtn;
	ProgressBar musicBar;
	boolean musicPlayed = false;
	boolean isProgressPressed = false;
	CountDownTimer cntr_aCounter;
	GPSTracker gps;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_pace);
		
		skipBtn = (Button)findViewById(R.id.btn_skipWalking);
		btn = (Button)findViewById(R.id.btn_startWalking);
		startFont = (ImageView)findViewById(R.id.btn_start_font);
		startFont.setImageResource(R.drawable.start);

		audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		musicBar = (ProgressBar)findViewById(R.id.progressBar_sampleMusic);

		File sdPathFile = Environment.getExternalStorageDirectory();
		String filePath = sdPathFile.getAbsolutePath() + "/Music/Roar.mp3";
		Uri uri = Uri.parse(filePath);
		mState = STATE_IDLE;

		try {
			mPlayer = MediaPlayer.create(this, uri);
			mState = STATE_PREPARED;
			musicBar.setMax(120000);
			musicBar.setProgress(0);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} 

		if(fromStartOrSetting() == 1){
			UserManager.getInstance().setPaceData(0);

			skipBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(GetPaceActivity.this, MainActivity.class));
					finish();
				}
			});

		}else{	//reset pace
			skipBtn.setVisibility(View.INVISIBLE);
		}
		
		cntr_aCounter = new CountDownTimer(120100, 10000) {

			@Override
			public void onTick(long millisUntilFinished) {
				gps.getLocation();
				locList[times]=gps.location;
				if(times>=1)
					Toast.makeText(getApplicationContext(), "Current Speed : "+locList[times-1].distanceTo(locList[times]), Toast.LENGTH_LONG).show();
				times++;
			}

			@Override
			public void onFinish() {
			    Toast.makeText(getApplicationContext(),"speed : "+gps.getSpeed(locList),Toast.LENGTH_LONG).show();
				UserManager.getInstance().setPaceData(gps.getSpeed(locList)); 	//set pace as int num
				mPlayer.stop();
				mState = STATE_STOPPED;
				speed=gps.getSpeed(locList);
				locList = null;
				startActivity(new Intent(GetPaceActivity.this, MainActivity.class));
				finish();
			}
		};
		
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				gps = new GPSTracker(GetPaceActivity.this);
				if(gps.isCanGetLocation()){
				skipBtn.setBackgroundColor(R.drawable.deactivated_btn);
				skipBtn.setEnabled(false);
				startFont.setImageResource(R.drawable.starting);
				btn.setEnabled(false);
				
				musicPlayed = true;
				mPlayer.seekTo(musicBar.getProgress());
				mPlayer.start();
				mState = STATE_STARTED;
				mHandler.post(progressRunnable);
				cntr_aCounter.start();
				
				//TODO:temporary Code for SungSoo				
	/*		 	convertToXY p = new convertToXY(37.4782662,126.8819707);
				parsingWeather parsing = new parsingWeather(p.getX(), p.getY());
				weatherInfo = parsing.getWeatherInfo(); // weatherInfo Stored.
			 
				Toast.makeText(getApplicationContext(), weatherInfo, Toast.LENGTH_LONG).show();
	*/
				}
				else{
					gps.showSettingsAlert();
				}
			}
		});

	}

	private int fromStartOrSetting(){
		SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences(this);

		int defaultValue=0;
		int keepcheck=sharedPref.getInt(getString(R.string.paceFrom),defaultValue);
		if(keepcheck==defaultValue){ //ó�� ���� �� ��
			return 1;
		}
		else{
			return 0;

		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mState == STATE_STARTED){
			
		}
	}
	

	public static final int INTERVAL = 1000;
	Runnable progressRunnable = new Runnable() {

		@Override
		public void run() {
			if (mState == STATE_STARTED) {
				if (!isProgressPressed) {
					musicBar.setProgress(mPlayer.getCurrentPosition());
				}
				mHandler.postDelayed(this, INTERVAL);
			}

		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	@Override
	public void onBackPressed() {
		if(fromStartOrSetting() == 0){
			
			if(mPlayer != null){
				mPlayer.stop();
				mState = STATE_STOPPED;
				mPlayer.release();
				startActivity(new Intent(GetPaceActivity.this, MainActivity.class));
				finish();
			}
			
			super.onBackPressed();
		}else{
			
		}
	}
	
}