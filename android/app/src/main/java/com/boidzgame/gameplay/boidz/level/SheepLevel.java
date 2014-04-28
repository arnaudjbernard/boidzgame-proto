package com.boidzgame.gameplay.boidz.level;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.boidzgame.R;
import com.boidzgame.activity.level.LevelActivity;
import com.boidzgame.gameplay.boidz.component.container.DogContainer;
import com.boidzgame.gameplay.boidz.component.container.SheepContainer;
import com.boidzgame.gameplay.boidz.component.ticker.MeadowTicker;
import com.boidzgame.gameplay.boidz.entity.Dog;
import com.boidzgame.gameplay.boidz.entity.Meadow;
import com.boidzgame.gameplay.boidz.entity.Sheep;
import com.boidzgame.gameplay.rendering.LevelView;
import com.boidzgame.util.SoundManager;

import java.util.Random;

public class SheepLevel extends Level {
    public static final int MESSAGE_WHAT_INCREMENT_SCORE = 0;
    private static final String TAG = "SheepLevel";
    private static final int SHEEP_COUNT = 40;
    private static final double SHEEP_SPREAD = 100.0d;
    private static final int DOG_COUNT = 2;
    private static final int MEADOW_RADIUS = 30;
    public SheepContainer sheepContainer;
    public DogContainer dogContainer;
    public Meadow meadow;
    public int score;

    @Override
    public void setup(LevelActivity levelActivity) {
        super.setup(levelActivity);

        LevelView view = (LevelView) levelActivity.findViewById(R.id.scene);
        view.color = 0xff009900;

        Random rand = new Random();
        sheepContainer = new SheepContainer();
        dogContainer = new DogContainer();
        meadow = new Meadow();
        meadow.radius = MEADOW_RADIUS;

        sheepContainer.setup(this);
        dogContainer.setup(this);

        for (int i = 0; i < SHEEP_COUNT; i++) {
            sheepContainer.add(new Sheep());
        }

        for (int i = 0; i < DOG_COUNT; i++) {
            dogContainer.add(new Dog());
        }
        sheepContainer.setupSheeps(sheepContainer, dogContainer);
        dogContainer.setupDogs();
        meadow.setup(this, sheepContainer);

        meadow.coordinates.positionX = 10 + MEADOW_RADIUS;
        meadow.coordinates.positionY = 10 + MEADOW_RADIUS;

        for (Sheep sheep : sheepContainer.boids) {
            sheep.coordinates.positionX = (rand.nextDouble() - 0.5) * SHEEP_SPREAD + this.width
                    * 0.5;
            sheep.coordinates.positionY = (rand.nextDouble() - 0.5) * SHEEP_SPREAD + this.height
                    * 0.5;
        }

        handler = new SheepLevelHandler();
    }

    @Override
    public void clean() {
        pause();

        handler = null;
        dogContainer.cleanDogs();
        sheepContainer.cleanSheeps();
        sheepContainer.clean();
        dogContainer.clean();
        dogContainer = null;
        sheepContainer = null;

        super.clean();
    }

    @Override
    public void play() {
        super.play();

        SoundManager.playMusic(R.raw.sheeps_background_music);
    }

    @Override
    public void pause() {
        super.pause();

        SoundManager.stopMusic();
    }

    private void incrementScore() {
        score++;
        if (score >= SHEEP_COUNT && !gameOver) {
            Log.d(TAG, "Game Won: " + score);
            gameOver = true;
            levelActivity.onWin();
        }
    }

    // no delayed message
    @SuppressLint("HandlerLeak")
    public class SheepLevelHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MeadowTicker.MESSAGE_SHEEP_IN) {
                incrementScore();
            }
        }
    }
}
