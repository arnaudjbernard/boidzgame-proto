package com.boidzgame.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.boidzgame.R;
import com.boidzgame.activity.level.LevelActivity;
import com.boidzgame.gameplay.LevelManager;
import com.boidzgame.gameplay.boidz.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LevelSelectScreenActivity extends ListActivity {
    private static final String TAG = "LevelSelectScreenActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "View added");
        setContentView(R.layout.levelselector);

        // build the list of all levels
        List<HashMap<String, String>> levels = new ArrayList<HashMap<String, String>>();

        HashMap<String, String> map;
        Level level;
        Resources resources = getResources();
        for (int i = 0; i < LevelManager.getLevelCount(); i++) {
            level = LevelManager.getLevel(i);
            map = new HashMap<String, String>();
            map.put("name", resources.getString(level.titleResourceId));
            map.put("description", resources.getString(level.descriptionResourceId));
            levels.add(map);
        }

        // create the grid item mapping
        String[] from = new String[]{"name", "description"};
        int[] to = new int[]{R.id.level_name, R.id.level_description};

        SimpleAdapter adapter = new SimpleAdapter(this, levels, R.layout.levelselectoritem, from,
                to);

        setListAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int levelId = arg2;
                // Level level = LevelManager.getLevel(levelId);
                startLevel(levelId);
            }
        });
    }

    private void startLevel(int levelId) {
        Intent intent = new Intent(this, LevelActivity.class);
        intent.putExtra(LevelActivity.LEVEL_ID_KEY, levelId);
        startActivity(intent);
    }

    public void onBackButtonClick(View v) {
        Log.d(TAG, "onBackButtonClick");
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
