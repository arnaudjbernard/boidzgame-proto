package com.boidzgame.gameplay.boidz.level;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.boidzgame.R;
import com.boidzgame.activity.level.LevelActivity;
import com.boidzgame.gameplay.boidz.component.container.BacteriaContainer;
import com.boidzgame.gameplay.boidz.component.container.FoodContainer;
import com.boidzgame.gameplay.boidz.entity.Bacteria;
import com.boidzgame.gameplay.rendering.LevelView;
import com.boidzgame.util.SoundManager;

public class BacteriaLevel extends Level {
    private static final String TAG = "BacteriaLevel";

    private static final int GOOD_BACTERIA_FINAL_COUNT = 50;
    private static final int BAD_BACTERIA_FINAL_COUNT = 5;
    private static final int GOOD_BACTERIA_INITIAL_COUNT = 10;
    private static final int BAD_BACTERIA_INITIAL_COUNT = 10;

    public BacteriaContainer bacteriaContainer;
    public FoodContainer foodContainer;

    @Override
    public void setup(LevelActivity levelActivity) {
        super.setup(levelActivity);

        handler = new BacteriaLevelHandler();

        LevelView view = (LevelView) levelActivity.findViewById(R.id.scene);
        view.color = 0xff000000;

        bacteriaContainer = new BacteriaContainer();
        foodContainer = new FoodContainer();
        bacteriaContainer.setup(this);
        foodContainer.setup(this);
        for (int i = 0; i < GOOD_BACTERIA_INITIAL_COUNT + BAD_BACTERIA_INITIAL_COUNT; i++) {
            bacteriaContainer.add(new Bacteria());
        }
        bacteriaContainer.setupBacterias(GOOD_BACTERIA_INITIAL_COUNT, BAD_BACTERIA_INITIAL_COUNT,
                foodContainer, bacteriaContainer);
    }

    @Override
    public void clean() {
        pause();

        foodContainer.cleanFood();
        foodContainer.clean();
        foodContainer = null;

        bacteriaContainer.cleanBacterias();
        bacteriaContainer.clean();
        bacteriaContainer = null;

        handler = null;
        super.clean();
    }

    @Override
    public void play() {
        super.play();

        SoundManager.playMusic(R.raw.shark_background_music);
    }

    @Override
    public void pause() {
        super.pause();

        SoundManager.stopMusic();
    }

    private void checkVictoryContdition() {
        if (!gameOver && bacteriaContainer.getGoodBacteriasCount() >= GOOD_BACTERIA_FINAL_COUNT
                && bacteriaContainer.getBadBacteriasCount() <= BAD_BACTERIA_FINAL_COUNT) {
            Log.d(TAG, "Game Won");
            gameOver = true;
            levelActivity.onWin();
        }
    }

    // no delayed message
    @SuppressLint("HandlerLeak")
    public class BacteriaLevelHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == BacteriaContainer.MESSAGE_BACTERIA_COUNT_UPDATE) {
                checkVictoryContdition();
            }
        }
    }
}
