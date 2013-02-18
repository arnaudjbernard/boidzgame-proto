package com.boidzgame.activity;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.boidzgame.R;
import com.boidzgame.activity.level.LevelActivity;
import com.boidzgame.gameplay.LevelManager;
import com.boidzgame.util.HighscoreManager;

public class HighscoresActivity extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {
	private static final String TAG = "HighscoresActivity";
	public static final String NICKNAME = "com.boidzgame.activity.NICKNAME";
	public static final String SCORE = "com.boidzgame.activity.SCORE";
	private HighscoreManager mHighscoreManager;
	private int mLevelId;
	private SimpleCursorAdapter mAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate called");
		setContentView(R.layout.highscores);

		// show title of level
		Intent intent = getIntent();
		mLevelId = intent.getIntExtra(LevelActivity.LEVEL_ID_KEY, -1);
		if (mLevelId == -1)
			throw new RuntimeException("No level name id given in intent");

		final TextView levelNameTextview = (TextView) findViewById(R.id.level_name);
		levelNameTextview.setText(LevelManager.getLevel(mLevelId).titleResourceId);

		// highscore databse setup
		mHighscoreManager = new HighscoreManager(this);
		mHighscoreManager.setup();

		// highscore update
		String nickname = intent.getStringExtra(NICKNAME);
		if (nickname != null) {
			long timeToWin = intent.getLongExtra(LevelActivity.LEVEL_TIME_TO_WIN, 0);
			double score = timeToWin / 1000000000.0f;
			mHighscoreManager.storeNewHighscore(mLevelId, nickname, score);
			intent.removeExtra(NICKNAME);
			intent.removeExtra(LevelActivity.LEVEL_TIME_TO_WIN);
		}
	}

	public void onCloseButtonClick(View v) {
		finish();
	}

	private void fillHighscores() {
		// Get all of the rows from the database and create the item list
		// Cursor highscoresCursor =
		// mHighscoreManager.getHighscoreList(mLevelId);
		// startManagingCursor(highscoresCursor);

		// Create an array to specify the fields we want to display in the list
		String[] from = new String[] { HighscoreManager.KEY_NICKNAME,
				HighscoreManager.KEY_SCORE };

		// and an array of the fields we want to bind those fields to (in this
		// case just text1)
		int[] to = new int[] { R.id.nickname, R.id.score };

		// Now create a simple cursor adapter and set it to display
		mAdapter = new SimpleCursorAdapter(this, R.layout.highscoresitem, null, from, to,
				0);
		setListAdapter(mAdapter);

		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	protected void onResume() {
		Log.d(TAG, "onResume called");
		super.onResume();

		fillHighscores();
	}

	@Override
	protected void onPause() {
		Log.d(TAG, "onPause called");
		super.onPause();
	}

	@Override
	protected void onStart() {
		Log.d(TAG, "onStart called");
		super.onStart();
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
		mHighscoreManager.clean();
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

	// /////////////////////////////////////////////////////////////////////////////////////////////
	// LoaderManager.LoaderCallbacks<Cursor> implementation
	// /////////////////////////////////////////////////////////////////////////////////////////////

	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// This is called when a new Loader needs to be created. This
		// sample only has one Loader, so we don't care about the ID.
		return mHighscoreManager.getHighscoreListLoader(mLevelId);
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// Swap the new cursor in. (The framework will take care of closing the
		// old cursor once we return.)
		mAdapter.swapCursor(data);
	}

	public void onLoaderReset(Loader<Cursor> arg0) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed. We need to make sure we are no
		// longer using it.
		mAdapter.swapCursor(null);

	}

}
