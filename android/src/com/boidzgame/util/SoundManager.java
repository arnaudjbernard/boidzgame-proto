package com.boidzgame.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.SparseIntArray;

public class SoundManager {

	private static SoundManager mInstance;
	private static SoundPool mSoundPool;
	private static SparseIntArray mSoundPoolMap;
	private static AudioManager mAudioManager;
	private static Context mContext;
	private static MediaPlayer musicMediaPlayer;

	private static Boolean mMusicEnabled = true;
	private static Boolean mSfxEnabled = true;

	private SoundManager() {
	}

	/**
	 * Requests the instance of the Sound Manager and creates it if it does not
	 * exist.
	 * 
	 * @return Returns the single instance of the SoundManager
	 */
	static synchronized public SoundManager getInstance() {
		if (mInstance == null)
			mInstance = new SoundManager();
		return mInstance;
	}

	/**
	 * Initializes the storage for the sounds
	 * 
	 * @param theContext The Application context
	 */
	public static void setup(Context theContext) {
		mContext = theContext;
		mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new SparseIntArray();
		mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
	}

	/**
	 * Add a new Sound to the SoundPool
	 * 
	 * @param SoundID - The Android ID for the Sound asset.
	 */
	public static void addSound(int soundID) {
		if (mSoundPoolMap.get(soundID) == 0) {
			int i = mSoundPool.load(mContext, soundID, 1);
			mSoundPoolMap.put(soundID, i);
		}
	}

	/**
	 * Plays a Sound
	 * 
	 * @param index - The Index of the Sound to be played
	 * @param speed - The Speed to play not, not currently used but included for
	 *        compatibility
	 */
	public static void playSound(int index, int loop) {
		playSound(index, 1, loop, 1.0f);
	}

	public static void playSound(int index, int priority, int loop) {
		playSound(index, priority, loop, 1.0f);
	}

	public static void playSound(int index, int priority, int loop, float speed) {
		if (!mSfxEnabled)
			return;

		mSoundPool.play(mSoundPoolMap.get(index), 1.0f, 1.0f, priority, loop, speed);
	}

	/**
	 * Stop a Sound
	 * 
	 * @param index - index of the sound to be stopped
	 */
	public static void stopSound(int index) {
		mSoundPool.stop(mSoundPoolMap.get(index));
	}

	/**
	 * Start a Music.
	 * 
	 * @param soundID - The Android ID for the Sound asset.
	 */
	public static void playMusic(int soundID) {
		if (musicMediaPlayer != null) {
			musicMediaPlayer.release();
		}

		musicMediaPlayer = MediaPlayer.create(mContext, soundID);

		if (mMusicEnabled) {
			musicMediaPlayer.setVolume(0, 0);
		} else {
			musicMediaPlayer.setVolume(1, 1);
		}

		musicMediaPlayer.start();
	}

	public static void stopMusic() {
		if (musicMediaPlayer != null) {
			musicMediaPlayer.pause();
		}
	}

	/**
	 * Deallocates the resources and Instance of SoundManager
	 */
	public static void clean() {
		mSoundPool.release();
		mSoundPool = null;
		mSoundPoolMap.clear();
		mAudioManager.unloadSoundEffects();
		if (musicMediaPlayer != null) {
			musicMediaPlayer.release();
		}
		mInstance = null;
	}

	public static void setMusicEnabled(Boolean value) {
		if (mMusicEnabled == value) {
			return;
		}
		mMusicEnabled = value;
		if (musicMediaPlayer != null) {
			if (mMusicEnabled) {
				musicMediaPlayer.setVolume(0, 0);
			} else {
				musicMediaPlayer.setVolume(1, 1);
			}
		}
	}

	public static void setSfxEnabled(Boolean value) {
		mSfxEnabled = value;
	}
}
