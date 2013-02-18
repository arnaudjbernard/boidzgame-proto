package com.boidzgame.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.boidzgame.R;

public class HomeScreenActivity extends Activity {
	private static final String TAG = "HomeScreenActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "View added");
		setContentView(R.layout.home);

		SettingsActivity.setupSettings(this);
	}

	public void onPlayClick(View v) {
		Log.d(TAG, "onPlayClick");
		Intent intent = new Intent(this, LevelSelectScreenActivity.class);
		startActivity(intent);
	}

	public void onHighscoreClick(View v) {
		Log.d(TAG, "onHighscoreClick");

	}

	public void onSettingsClick(View v) {
		Log.d(TAG, "onSettingsClick");

		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	public void onQuitClick(View v) {
		Log.d(TAG, "onQuitClick");
		finish();
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
