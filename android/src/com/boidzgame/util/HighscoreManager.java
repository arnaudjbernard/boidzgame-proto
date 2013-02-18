package com.boidzgame.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HighscoreManager {
	private static final String TAG = "HighscoreManager";

	// ////////////////////////////////////////////////////////////////
	// Manager life cycle
	// ////////////////////////////////////////////////////////////////
	private final Context mContext;
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	/**
	 * Constructor
	 * 
	 * @param ctx the Context to access the database
	 */
	public HighscoreManager(Context context) {
		mContext = context;
	}

	public HighscoreManager setup() throws SQLException {
		mDbHelper = new DatabaseHelper(mContext);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void clean() {
		mDbHelper.close();
	}

	// ////////////////////////////////////////////////////////////////
	// Interface
	// ////////////////////////////////////////////////////////////////
	/**
	 * Store a new high score in the database
	 * 
	 * @param levelId level id
	 * @param nickname nickname of the player who got the highscore
	 * @param score
	 * @return rowId or -1 if failed
	 */
	public long storeNewHighscore(int levelId, String nickname, double score) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(KEY_LEVELID, levelId);
		contentValues.put(KEY_NICKNAME, nickname);
		contentValues.put(KEY_SCORE, score);

		return mDb.insert(TABLE_HIGHSCORES, null, contentValues);
	}

	public static final class HighscoreListCursorLoader extends SimpleCursorLoader {
		private int mLevelId;
		private SQLiteDatabase mDb;

		public HighscoreListCursorLoader(Context context, SQLiteDatabase db, int levelId) {
			super(context);
			mLevelId = levelId;
			mDb = db;
		}

		@Override
		public Cursor loadInBackground() {
			Cursor cursor = null;
			try {
				cursor = mDb.query(true, TABLE_HIGHSCORES, new String[] { KEY_ROWID,
						KEY_NICKNAME, KEY_SCORE }, KEY_LEVELID + "=" + mLevelId, null,
						null, null, KEY_SCORE, "100");
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (cursor != null) {
				cursor.moveToFirst();
			}

			return cursor;
		}
	}

	public HighscoreListCursorLoader getHighscoreListLoader(int levelId) {
		return new HighscoreListCursorLoader(mContext, mDb, levelId);
	}

	// private Cursor getHighscoreList(int levelId) throws SQLException {
	// Cursor mCursor = mDb.query(true, TABLE_HIGHSCORES, new String[] {
	// KEY_ROWID,
	// KEY_NICKNAME, KEY_SCORE }, KEY_LEVELID + "=" + levelId, null, null, null,
	// KEY_SCORE, "100");
	// if (mCursor != null) {
	// mCursor.moveToFirst();
	// }
	// return mCursor;
	// }

	// ////////////////////////////////////////////////////////////////
	// SQLite
	// ////////////////////////////////////////////////////////////////
	private static final String DATABASE_NAME = "boidzgame";
	private static final String TABLE_HIGHSCORES = "highscores";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_LEVELID = "levelid";
	public static final String KEY_NICKNAME = "nickname";
	public static final String KEY_SCORE = "score";

	/** Database creation sql statement */
	private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_HIGHSCORES
			+ " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_LEVELID
			+ " INTEGER NOT NULL, " + KEY_NICKNAME + " TEXT NOT NULL, " + KEY_SCORE
			+ " REAL NOT NULL);";

	private static final int DATABASE_VERSION = 3;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIGHSCORES);
			onCreate(db);
		}
	}
}
