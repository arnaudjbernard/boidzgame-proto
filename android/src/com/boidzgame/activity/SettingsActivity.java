package com.boidzgame.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.boidzgame.R;
import com.boidzgame.util.SoundManager;

public class SettingsActivity extends Activity implements
		OnSharedPreferenceChangeListener {
	public static final String KEY_SETTINGS_MUSIC_ENABLED = "settings_music_enabled";
	public static final String KEY_SETTINGS_SFX_ENABLED = "settings_sfx_enabled";

	private SettingsFragment mSettingsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Display the fragment as the main content.
		mSettingsFragment = new SettingsFragment();
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, mSettingsFragment).commit();
	}

	public static void setupSettings(Context context) {
		// set the default settings if they are not yet set
		PreferenceManager.setDefaultValues(context, R.xml.preferences, false);

		// apply the current preferences
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);

		Boolean musicEnabled = sharedPref.getBoolean(KEY_SETTINGS_MUSIC_ENABLED, false);
		SoundManager.setMusicEnabled(musicEnabled);
		Boolean sfxEnabled = sharedPref.getBoolean(KEY_SETTINGS_SFX_ENABLED, true);
		SoundManager.setSfxEnabled(sfxEnabled);
	}

	public static class SettingsFragment extends PreferenceFragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.preferences);
		}
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPref, String key) {
		if (key.equals(KEY_SETTINGS_MUSIC_ENABLED)) {
			Boolean musicEnabled = sharedPref.getBoolean(KEY_SETTINGS_MUSIC_ENABLED,
					false);
			SoundManager.setMusicEnabled(musicEnabled);
		} else if (key.equals(KEY_SETTINGS_SFX_ENABLED)) {
			Boolean sfxEnabled = sharedPref.getBoolean(KEY_SETTINGS_SFX_ENABLED, true);
			SoundManager.setSfxEnabled(sfxEnabled);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		mSettingsFragment.getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSettingsFragment.getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}
}
