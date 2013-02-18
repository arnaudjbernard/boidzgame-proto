package com.boidzgame.activity.level;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.boidzgame.R;
import com.boidzgame.activity.LevelMenuActivity;
import com.boidzgame.activity.WinActivity;
import com.boidzgame.gameplay.LevelManager;
import com.boidzgame.gameplay.level.Level;

public class LevelActivity extends Activity {
	private static final String TAG = "LevelActivity";

	public static final String LEVEL_ID_KEY = "com.boidzgame.activity.LEVEL_ID_KEY";
	public static final String LEVEL_TIME_TO_WIN = "com.boidzgame.activity.LEVEL_TIME_TO_WIN";
	private static final int LEVEL_MENU_REQUEST_CODE = 1;

	protected Level mLevel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "View added");

		setContentView(R.layout.level);

		Intent intent = getIntent();
		int levelId = intent.getIntExtra(LevelActivity.LEVEL_ID_KEY, -1);
		if (levelId == -1)
			throw new RuntimeException("No level name id given in intent");
		mLevel = LevelManager.getLevel(levelId);
		mLevel.setup(this);
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

		// Debug.startMethodTracing();
	}

	@Override
	public void onBackPressed() {
		pauseGameAndShowMenu();
	}

	public void onPauseButtonClick(View v) {
		pauseGameAndShowMenu();
	}

	private void pauseGameAndShowMenu() {
		mLevel.pause();

		// show the pause menu
		Intent intent = new Intent(this, LevelMenuActivity.class);
		intent.putExtra(LEVEL_ID_KEY, mLevel.levelId);
		startActivityForResult(intent, LEVEL_MENU_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == LEVEL_MENU_REQUEST_CODE) {
			if (resultCode == LevelMenuActivity.RESUME_RESULT_CODE) {
				mLevel.play();
			} else if (resultCode == LevelMenuActivity.GIVE_UP_RESULT_CODE) {
				mLevel.clean();
				finish();
			} else {
				// pressed back again take it as a give up
				mLevel.clean();
				finish();
			}
		}
	}

	public void onWin() {
		Log.d(TAG, "win called");
		mLevel.pause();

		// show the win screen
		Intent intent = new Intent(this, WinActivity.class);
		intent.putExtra(LEVEL_ID_KEY, mLevel.levelId);
		intent.putExtra(LEVEL_TIME_TO_WIN, mLevel.timeToWin);
		startActivity(intent);

		mLevel.clean();
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

		if (mLevel.paused) {
			// the level is gonna be on screen, we need to make it play
			mLevel.play();
		}
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause called");
		super.onPause();

		if (!mLevel.paused) {
			// we got destroyed by something else than give up, pause the game
			pauseGameAndShowMenu();
		}
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

		// Debug.stopMethodTracing();
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
