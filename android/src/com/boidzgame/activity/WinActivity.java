package com.boidzgame.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.boidzgame.R;
import com.boidzgame.activity.level.LevelActivity;
import com.boidzgame.gameplay.LevelManager;

public class WinActivity extends Activity {
	private static final String TAG = "LevelMenuActivity";
	private int mLevelId;
	private long timeToWin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "View added");
		setContentView(R.layout.win);

		// show title of level
		Intent intent = getIntent();
		mLevelId = intent.getIntExtra(LevelActivity.LEVEL_ID_KEY, -1);
		if (mLevelId == -1)
			throw new RuntimeException("No level name id given in intent");

		final TextView levelNameTextview = (TextView) findViewById(R.id.level_name);
		levelNameTextview.setText(LevelManager.getLevel(mLevelId).titleResourceId);

		// display the time taken to finish the level
		timeToWin = intent.getLongExtra(LevelActivity.LEVEL_TIME_TO_WIN, 0);
		double timeToWinInS = timeToWin / 1000000000.0f;
		final TextView timeToWinTextview = (TextView) findViewById(R.id.time_to_win);
		timeToWinTextview.setText(timeToWinInS + "s");
	}

	public void onOkButtonClick(View v) {
		// show the high scores for this level
		Intent intent = new Intent(this, HighscoresActivity.class);
		intent.putExtra(LevelActivity.LEVEL_ID_KEY, mLevelId);

		final EditText nicknameEditText = (EditText) findViewById(R.id.nickname_input);
		String nickname = nicknameEditText.getText().toString();
		if (nickname.length() > 0) {
			intent.putExtra(HighscoresActivity.NICKNAME, nickname);
			intent.putExtra(LevelActivity.LEVEL_TIME_TO_WIN, timeToWin);
		}

		startActivity(intent);
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
