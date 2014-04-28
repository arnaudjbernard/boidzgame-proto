package com.boidzgame.gameplay.boidz.level;

import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.Display;

import com.boidzgame.R;
import com.boidzgame.activity.level.LevelActivity;
import com.boidzgame.gameplay.GameThread;
import com.boidzgame.gameplay.TouchManager;
import com.boidzgame.gameplay.rendering.LevelView;
import com.boidzgame.gameplay.rendering.RendererManager;
import com.boidzgame.gameplay.ticking.TickerManager;
import com.boidzgame.util.SoundManager;

public abstract class Level {
    private static final String TAG = "Level";
    public int levelId;
    public int titleResourceId;
    public int descriptionResourceId;

    public int width;
    public int height;

    // public int levelId;
    public long timeToWin;
    public boolean paused = true;
    public LevelActivity levelActivity;
    public TickerManager tickerManager;
    public RendererManager rendererManager;
    public TouchManager touchManager;
    public SoundManager soundManager;
    public Handler handler;
    protected long timeLatUnpause;
    protected boolean gameOver = false;
    protected GameThread mThread;

    public void setup(LevelActivity activity) {

        this.levelActivity = activity;
        // setup initial size
        Display display = levelActivity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        tickerManager = new TickerManager();
        rendererManager = new RendererManager();

        tickerManager.setup();
        rendererManager.setup();

        touchManager = new TouchManager(tickerManager);
        touchManager.setup(this);

        final LevelView view = (LevelView) levelActivity.findViewById(R.id.scene);
        view.setup(this);

        SoundManager.setup(levelActivity.getBaseContext());
    }

    public void clean() {
        pause();

        SoundManager.clean();

        final LevelView view = (LevelView) levelActivity.findViewById(R.id.scene);
        view.clean();

        touchManager.clean();
        touchManager = null;

        tickerManager.clean();
        rendererManager.clean();

        tickerManager = null;
        rendererManager = null;
    }

    public void play() {
        Log.d(TAG, "start");

        final LevelView view = (LevelView) levelActivity.findViewById(R.id.scene);
        mThread = new GameThread(view.getHolder(), tickerManager, rendererManager);

        paused = false;
        mThread.running = true;
        mThread.start();

        timeLatUnpause = System.nanoTime();
    }

    public void pause() {
        Log.d(TAG, "stop");
        if (mThread != null) {
            // stop the game thread
            paused = true;
            mThread.running = false;
            boolean retry = true;
            while (retry) {
                try {
                    mThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
            mThread = null;

            timeToWin += System.nanoTime() - timeLatUnpause;
        }
        touchManager.reset();
    }

}
