package com.boidzgame.gameplay.boidz.level;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.boidzgame.R;
import com.boidzgame.activity.level.LevelActivity;
import com.boidzgame.gameplay.boidz.component.container.GoldfishContainer;
import com.boidzgame.gameplay.boidz.component.ticker.SharkTicker;
import com.boidzgame.gameplay.boidz.entity.Goldfish;
import com.boidzgame.gameplay.boidz.entity.Shark;
import com.boidzgame.gameplay.rendering.LevelView;
import com.boidzgame.util.SoundManager;

public class SharkLevel extends Level {
    private static final String TAG = "SharkLevel";

    private static final int GOLDFISH_COUNT = 50;
    public GoldfishContainer goldfishContainer;
    public Shark shark;
    public int score;

    @Override
    public void setup(LevelActivity levelActivity) {
        super.setup(levelActivity);

        handler = new SharkLevelHandler();

        LevelView view = (LevelView) levelActivity.findViewById(R.id.scene);
        view.color = 0xff0000aa;

        goldfishContainer = new GoldfishContainer();
        goldfishContainer.setup(this);
        shark = new Shark();
        for (int i = 0; i < GOLDFISH_COUNT; i++) {
            goldfishContainer.add(new Goldfish());
        }
        goldfishContainer.setupParticles(goldfishContainer, shark);
        shark.setup(this, goldfishContainer);

    }

    @Override
    public void clean() {
        pause();

        goldfishContainer.cleanParticles();
        goldfishContainer.clean();
        shark.clean();
        goldfishContainer = null;
        shark = null;

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

    private void incrementScore() {
        score++;
        if (score >= GOLDFISH_COUNT && !gameOver) {
            Log.d(TAG, "Game Won: " + score);
            gameOver = true;
            levelActivity.onWin();
        }
    }

    // no delayed message
    @SuppressLint("HandlerLeak")
    public class SharkLevelHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == SharkTicker.MESSAGE_GOLDFISH_EATEN) {
                incrementScore();
            }
        }
    }

}
