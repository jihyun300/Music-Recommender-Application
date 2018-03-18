package com.example.anytimeanywhere;

//import convertToXY;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.anytimeanywhere.manager.UserManager;
import com.example.anytimeanywhere.manager.UserState;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.localytics.android.*;

public class MainActivity extends Activity {

	ToggleButton BtnPlay;
	ImageView ImgNow;
	ImageView[] ImgNexts;
	ImageView PaceSetting;
	ImageLoader mImageLoader;
	TextView TxtTitle;
	TextView TxtArtist;
	ImageView ImgLike;
	ImageView ImgHate;
	private MediaPlayer mMediaPlayer=new MediaPlayer();
	private LocalyticsAmpSession localyticsSession;
	MusicList mMusicList;
	RecommendMusic mRecommendMusic;
	
	MusicList nowPlayingList;
	MusicList[] nextPlayList;
	
	Music nowPlayingMusic;
	
	
	//now playing
    Uri musicURI;
    Uri albumImgUri;
    int times = 0;
    private long countTime = 120100;
    double speed=0;
    GPSTracker gps2;
    Location locList2[] = new Location[11];
    DisplayImageOptions imageOptionNext;
    int mState;
    public static final int STATE_IDLE = 1;
    public static final int STATE_INITIALIZED = 2;
    public static final int STATE_PREPARED = 3;
    public static final int STATE_STARTED = 4;
    public static final int STATE_PAUSED = 5;
    public static final int STATE_COMPLETED = 6;
    public static final int STATE_STOPPED = 7;
    public static final int STATE_ERROR = 8;
    Cursor musicCursor;
    private int SELECTED_NEXT=0; //selected Music List num
    private int EVALUATE=-1; // if like=0, dislike=1;
    CountDownTimer mTimer;
    int checkForCountDownTimer;
    
    Map<String,String> map_good = new HashMap<String,String>();
    Map<String,String> map_bad = new HashMap<String,String>();
	    
    private final static String COUNT_PLAY_MUSIC = "play music";
	private final static String COUNT_PAUSE_MUSIC = "pause music";
	private final static String GOOD = "GOOD";
	private final static String BAD = "BAD";
	
	SimpleDateFormat sdfNow = new SimpleDateFormat("HH");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 // Instantiate the object
		   this.localyticsSession = new LocalyticsAmpSession(
		            this.getApplicationContext());  // Context used to access device resources

		   // Register LocalyticsActivityLifecycleCallbacks
		   getApplication().registerActivityLifecycleCallbacks(
		         new LocalyticsActivityLifecycleCallbacks(this.localyticsSession));

		   // Activity Creation Code
		ImgNow=(ImageView)findViewById(R.id.imgv_playNow);
		ImgNexts=new ImageView[3];
		
		ImgNexts[0]=(ImageView)findViewById(R.id.imgv_playNext1);
		ImgNexts[1]=(ImageView)findViewById(R.id.imgv_playNext2);
		ImgNexts[2]=(ImageView)findViewById(R.id.imgv_playNext3);
		ImgLike=(ImageView)findViewById(R.id.btn_like);
		ImgHate=(ImageView)findViewById(R.id.btn_dislike);
		BtnPlay=(ToggleButton)findViewById(R.id.btn_playNow);
		
		TxtTitle=(TextView)findViewById(R.id.txt_title);
		TxtArtist=(TextView)findViewById(R.id.txt_artist);
	
		
		
	
        String[] proj = {
				MediaStore.Audio.Media._ID,
				MediaStore.Audio.Media.ALBUM_ID,
				MediaStore.Audio.Media.TITLE,
				MediaStore.Audio.Media.ARTIST,
				MediaStore.Audio.Media.DATA
		};
        
		
		musicCursor=getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,proj,MediaStore.Audio.Media.IS_MUSIC+"=1",null,null);

		mMusicList=new MusicList(musicCursor);
		
		mRecommendMusic= new RecommendMusic(mMusicList);
		
		//nowPlayingList=new MusicList();
		nextPlayList=new MusicList[3];
		
		//at first
		nowPlayingList=mMusicList; 
		
		
		nowPlayingMusic=nowPlayingList.musiclist.get(nowPlayingList.index);
		musicURI=nowPlayingMusic.musicUri;
		albumImgUri=nowPlayingMusic.albumImgUri;
		
		TxtTitle.setText(nowPlayingMusic.Title);
		TxtArtist.setText(nowPlayingMusic.Artist);
			
		//at first
		for(int i=0;i<3;i++){
		nextPlayList[i]=mRecommendMusic.getRecommList(i, nowPlayingList);
		}
		
	
		mState=STATE_INITIALIZED;
	
	
		
		PaceSetting=(ImageView)findViewById(R.id.imgv_setpace);
		//mTimer.


		mTimer=new CountDownTimer(countTime,10000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				countTime = (int) millisUntilFinished;
				gps2.getLocation();
				locList2[times]=gps2.location;
				
				if(times>=1)
					Toast.makeText(getApplicationContext(), "Current Speed : "+locList2[times-1].distanceTo(locList2[times]), Toast.LENGTH_LONG).show();
				times++;
				//Toast.makeText(getApplicationContext(), "��", Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				countTime = 120100;
				times = 0;
				Toast.makeText(getApplicationContext(), "weather : "+getWeather(locList2[1].getLatitude(),locList2[1].getLongitude()) , Toast.LENGTH_LONG).show();
			//	Toast.makeText(getApplicationContext(), "timer Finished\nspeed : "+gps2.getSpeed(locList2), Toast.LENGTH_LONG).show();
				UserState.getInstance().setPaceData(gps2.getSpeed(locList2));
				UserState.getInstance().setWeather(WeatherToInt(getWeather(37.4782662,126.8819707)));
			}
		};

		
		
		imageOptionNext=new DisplayImageOptions.Builder()
		.showImageOnFail(R.drawable.ic_default)
		.showImageOnLoading(R.drawable.ic_launcher)
		.cacheInMemory(true)
		.resetViewBeforeLoading(true)
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.displayer(new RoundedBitmapDisplayer(500))
		.build();
		
		
		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
		this).defaultDisplayImageOptions(imageOptionNext).memoryCache(
		new WeakMemoryCache());
	
		ImageLoaderConfiguration config=builder.build();
		mImageLoader=ImageLoader.getInstance();
		mImageLoader.init(config);
		
				
		ImageLoader.getInstance().displayImage(""+albumImgUri, ImgNow,imageOptionNext);
		for(int j=0;j<3;j++){
			ImageLoader.getInstance().displayImage(""+nextPlayList[j].musiclist.get(nextPlayList[j].index+1).albumImgUri, ImgNexts[j],imageOptionNext);
			ImgNexts[j].setOnClickListener(btnClickListener);
		}
		ImgLike.setOnClickListener(btnClickListener);
		ImgHate.setOnClickListener(btnClickListener);
		
		
		
	
		BtnPlay.setOnClickListener(new View.OnClickListener() {
			
			
			@Override
			public void onClick(View v) {
				gps2=new GPSTracker(MainActivity.this);
				
				//======================THIs part is weather info====================
				
				
				if(BtnPlay.isChecked()){
					if(gps2.isCanGetLocation())
					{
					//Play
					Log.d("aa", "music2 : " + musicURI);
					startCountdowntimer();
					playMusic(musicURI);
					BtnPlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_pause));
					}
					else
					{	
						BtnPlay.setChecked(false);
						gps2.showSettingsAlert();
					}
				}
				else{
					//Pause
					checkForCountDownTimer = STATE_PAUSED;
					mTimer.cancel();
					Toast.makeText(getApplicationContext(), Long.toString(countTime), Toast.LENGTH_LONG).show();
					pauseMusic(musicURI);
					BtnPlay.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_play));
				}
			}
		});
		
		PaceSetting.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mMediaPlayer != null){
					onDestroy();
				}
				Intent i = new Intent(MainActivity.this, GetPaceActivity.class);
				startActivityForResult(i, 0);
				finish();
			}
		});
		
		//write that user has already started app before
		SharedPreferences sharedPref =PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(getString(R.string.savedSetting), 1);
		editor.commit();
		
		
	}

	private View.OnClickListener btnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.imgv_playNext1:
				SELECTED_NEXT=0;
				ImgNexts[0].setBackgroundResource(R.drawable.ic_red);
				ImgNexts[1].setBackgroundResource(R.drawable.img_background);
				ImgNexts[2].setBackgroundResource(R.drawable.img_background);
				break;
			case R.id.imgv_playNext2:
				SELECTED_NEXT=1;
				ImgNexts[1].setBackgroundResource(R.drawable.ic_orange);
				ImgNexts[0].setBackgroundResource(R.drawable.img_background);
				ImgNexts[2].setBackgroundResource(R.drawable.img_background);
				break;
			case R.id.imgv_playNext3:
				SELECTED_NEXT=2;
				ImgNexts[2].setBackgroundResource(R.drawable.ic_green);
				ImgNexts[0].setBackgroundResource(R.drawable.img_background);
				ImgNexts[1].setBackgroundResource(R.drawable.img_background);
				break;
			case R.id.btn_like:
				EVALUATE=0;			
			//	ImgLike.setBackgroundResource(R.drawable.yellow_circle);
				ImgLike.setBackgroundResource(R.drawable.circle_64);
				ImgHate.setBackgroundResource(0);
					break;
			case R.id.btn_dislike:
				EVALUATE=1;
				ImgLike.setBackgroundResource(0);
			//	ImgHate.setBackgroundResource(R.drawable.yellow_circle);
				ImgHate.setBackgroundResource(R.drawable.circle_64);
				break;
			}
		}
	};

	
	public void playMusic(Uri uri) {
	    try {
	    
	    	if(mState==STATE_INITIALIZED){
	    		mMediaPlayer.reset();
	    		  mMediaPlayer.setDataSource(this, uri);    
		        	mMediaPlayer.prepare();
	    	}
	    	else if(mState==STATE_PAUSED){ 	        	      
	    		int current=mMediaPlayer.getCurrentPosition();	    		
	    		mMediaPlayer.seekTo(current);
	        
	    	}
	        mMediaPlayer.start();

	        this.localyticsSession.tagEvent(COUNT_PLAY_MUSIC);			////When playing, 'play music'event operated.

	        mState=STATE_STARTED;
	        
	        //start Timer
	       // mTimer.start();      
	        
	        mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
	             public void onCompletion(MediaPlayer mp) {
	                 //when music is end
	            	mState=STATE_INITIALIZED;
	            	
	            	if(EVALUATE == 0){
	        	        map_good.put("Weather",getWeather(locList2[1].getLatitude(),locList2[1].getLongitude()));
	        	        map_good.put("Time",sdfNow.format(new Date(System.currentTimeMillis())));
	        	        map_good.put("Pace",String.valueOf(UserState.getInstance().getPaceData()));
	        	        map_good.put("Title",nowPlayingMusic.Title);
	        	        localyticsSession.tagEvent(GOOD, map_good);
	        	        map_good.clear();
	            	}
	            	else if(EVALUATE == 1){
	            		map_bad.put("Weather",getWeather(locList2[1].getLatitude(),locList2[1].getLongitude()));
	            		map_bad.put("Time",sdfNow.format(new Date(System.currentTimeMillis())));
	            		map_bad.put("Pace",String.valueOf(UserState.getInstance().getPaceData()));
	            		map_bad.put("Title",nowPlayingMusic.Title);
	            		localyticsSession.tagEvent(BAD, map_bad);
	            		map_bad.clear();
	            	}
	            	

	            	//change nowplayinglist to selected list
	            	nowPlayingList=nextPlayList[SELECTED_NEXT];
	            	nowPlayingMusic=nowPlayingList.musiclist.get(++nowPlayingList.index);
	            	
	            	albumImgUri=nowPlayingMusic.albumImgUri;
	            	musicURI=nowPlayingMusic.musicUri;
	         	            	
	            	TxtTitle.setText(nowPlayingMusic.Title);
		        	TxtArtist.setText(nowPlayingMusic.Artist);
		            	
	            	ImageLoader.getInstance().displayImage(""+albumImgUri, ImgNow,imageOptionNext); 
	            //	nextPlayList[SELECTED_NEXT].index+=1;
	            	//make next play list
	            	for(int i=0;i<3;i++){
	            		
	            		nextPlayList[i]=mRecommendMusic.getRecommList(i, nowPlayingList);
	            		ImageLoader.getInstance().displayImage(""+nextPlayList[i].musiclist.get(nextPlayList[i].index+1).albumImgUri, ImgNexts[i],imageOptionNext);
	            	}
	            	//initialize
	            	EVALUATE=-1;
					ImgLike.setBackgroundResource(0);
					ImgHate.setBackgroundResource(0);
	            	playMusic(musicURI);
	            	//change 0 to ex)"nowPlayingMusic->(float)starRate"
	            	
	  	            	
	             }
	        });
	          
	    } catch (IOException e) {
	        Log.v("SimplePlayer", e.getMessage());
	    }
	}
	
	public void pauseMusic(Uri uri){
		if(mMediaPlayer.isPlaying()){
			mMediaPlayer.pause();
			this.localyticsSession.tagEvent(COUNT_PAUSE_MUSIC);			//When pausing, 'pause music'event operated.
			mState=STATE_PAUSED;
		//	mMediaPlayer.release();
		}
		else
			mMediaPlayer.start();
	}
	@Override
	protected void onDestroy(){
		mMusicList.musiclist.clear();
		if(!musicCursor.isClosed())
			musicCursor.close();
		
		if(mMediaPlayer!=null)
			mMediaPlayer.release();
		super.onDestroy();
	}
	
	public void startCountdowntimer()
	{
mTimer=new CountDownTimer(countTime,10000) {
			@Override
			public void onTick(long millisUntilFinished) {
				countTime = (int) millisUntilFinished;
				if(checkForCountDownTimer!=STATE_PAUSED)
				{
					gps2.getLocation();
					locList2[times]=gps2.location;
					if(times>=1)
						Toast.makeText(getApplicationContext(), "Current Speed : "+locList2[times-1].distanceTo(locList2[times]), Toast.LENGTH_LONG).show();
					times++;
				}
				else
					checkForCountDownTimer = STATE_STARTED;
			  
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				countTime = 120100;
				times = 0;
				Toast.makeText(getApplicationContext(), "timer Finished\nspeed : "+gps2.getSpeed(locList2), Toast.LENGTH_LONG).show();
				UserState.getInstance().setPaceData(gps2.getSpeed(locList2));
			}
		}.start();
	
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
			
		}
	
	}
	
	String getWeather(double x, double y)
	{
		//==============testing=================================
		convertToXY p = new convertToXY(x,y);
		p.start();
		while(p.isAlive());
		parsingWeather weather = new parsingWeather(p.getX(),p.getY());
		weather.start();
		while(weather.isAlive());
		
		return weather.getWeatherInfo();
	}
	
	int WeatherToInt(String weather){
	      if(weather.matches(".*맑음.*")){
	         return 0;
	      }
	      else if(weather.matches(".*흐림.*")||weather.matches(".*구름.*")){
	         return 1;
	      }
	      else if(weather.matches(".*비.*")){
	         return 2;
	      }
	      else{
	         return 3;
	      }
	   }
}
