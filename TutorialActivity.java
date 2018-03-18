package com.example.anytimeanywhere;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class TutorialActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		
		newOrNot();
		
		Button btn = (Button)findViewById(R.id.btn_endTutorial);
		
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(TutorialActivity.this,GetPaceActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	private void newOrNot(){
		SharedPreferences sharedPref=PreferenceManager.getDefaultSharedPreferences(this);

		int defaultValue=0;
		int keepcheck=sharedPref.getInt(getString(R.string.savedSetting),defaultValue);
		if(keepcheck==defaultValue){ //아직 한번도 안써졌을때

		}
		else{
			Intent intent=new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}
}
