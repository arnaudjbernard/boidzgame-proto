package com.boidzgame.gameplay.level;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.boidzgame.R;
import com.boidzgame.activity.level.LevelActivity;
import com.boidzgame.gameplay.component.container.BacteryContainer;
import com.boidzgame.gameplay.component.container.FoodContainer;
import com.boidzgame.gameplay.entity.Bactery;
import com.boidzgame.gameplay.rendering.LevelView;
import com.boidzgame.util.SoundManager;

public class BacteryLevel extends Level {
	private static final String TAG = "BacteryLevel";

	private static final int GOOD_BACTERY_FINAL_COUNT = 50;
	private static final int BAD_BACTERY_FINAL_COUNT = 5;
	private static final int GOOD_BACTERY_INITIAL_COUNT = 10;
	private static final int BAD_BACTERY_INITIAL_COUNT = 10;

	public BacteryContainer bacteryContainer;
	public FoodContainer foodContainer;

	@Override
	public void setup(LevelActivity levelActivity) {
		super.setup(levelActivity);

		handler = new BacteryLevelHandler();

		LevelView view = (LevelView) levelActivity.findViewById(R.id.scene);
		view.color = 0xff000000;

		bacteryContainer = new BacteryContainer();
		foodContainer = new FoodContainer();
		bacteryContainer.setup(this);
		foodContainer.setup(this);
		for (int i = 0; i < GOOD_BACTERY_INITIAL_COUNT + BAD_BACTERY_INITIAL_COUNT; i++) {
			bacteryContainer.add(new Bactery());
		}
		bacteryContainer.setupBacteries(GOOD_BACTERY_INITIAL_COUNT, BAD_BACTERY_INITIAL_COUNT,
				foodContainer, bacteryContainer);
	}

	@Override
	public void clean() {
		pause();

		foodContainer.cleanFood();
		foodContainer.clean();
		foodContainer = null;

		bacteryContainer.cleanBacteries();
		bacteryContainer.clean();
		bacteryContainer = null;

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

	// no delayed message
	@SuppressLint("HandlerLeak")
	public class BacteryLevelHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == BacteryContainer.MESSAGE_BACTERY_COUNT_UPDATE) {
				checkVictoryContdition();
			}
		}
	}

	private void checkVictoryContdition() {
		if (!gameOver && bacteryContainer.getGoodBacteriesCount() >= GOOD_BACTERY_FINAL_COUNT
				&& bacteryContainer.getBadBacteriesCount() <= BAD_BACTERY_FINAL_COUNT) {
			Log.d(TAG, "Game Won");
			gameOver = true;
			levelActivity.onWin();
		}
	}
}
