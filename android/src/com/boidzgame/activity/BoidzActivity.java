package com.boidzgame.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public abstract class BoidzActivity extends Activity {
	private static final String TAG = "BoidzActivity";

	public BoidzActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate called");

		// no title
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// make the application full screen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart called");
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume called");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause called");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.d(TAG, "onRestart called");
		super.onRestart();
	}

	@Override
	protected void onStop() {
		Log.d(TAG, "onStop called");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy called");
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "onSaveInstanceState called");
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		Log.d(TAG, "onRestoreInstanceState called");
		super.onRestoreInstanceState(savedInstanceState);
	}
}
