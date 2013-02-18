package com.boidzgame.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.boidzgame.R;
import com.boidzgame.activity.level.LevelActivity;
import com.boidzgame.gameplay.LevelManager;

public class LevelMenuActivity extends Activity {
	private static final String TAG = "LevelMenuActivity";
	public static final int RESUME_RESULT_CODE = RESULT_FIRST_USER;
	public static final int GIVE_UP_RESULT_CODE = RESULT_FIRST_USER + 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "View added");
		setContentView(R.layout.levelmenu);

		// show title of level
		Intent intent = getIntent();
		int levelId = intent.getIntExtra(LevelActivity.LEVEL_ID_KEY, -1);
		if (levelId == -1)
			throw new RuntimeException("No level name id given in intent");

		final TextView levelNameTextview = (TextView) findViewById(R.id.level_name);
		levelNameTextview.setText(LevelManager.getLevel(levelId).titleResourceId);
	}

	public void onPlayButtonClick(View v) {
		setResult(RESUME_RESULT_CODE);
		finish();
	}

	public void onSettingsButtonClick(View v) {
		Log.d(TAG, "clicked onSettingsButtonClick");
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	public void onGiveUpButtonClick(View v) {
		setResult(GIVE_UP_RESULT_CODE);
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
