package com.boidzgame.gameplay;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import com.boidzgame.gameplay.rendering.RendererManager;
import com.boidzgame.gameplay.ticking.TickerManager;

public class GameThread extends Thread {
	private static final String TAG = "GameThread";

	public boolean running = false;
	public boolean ticking = true;

	private final int mFramerate = 40;
	private final int mMaxTicks = 10;
	private final int mFramedelay;

	private SurfaceHolder holder;
	private TickerManager tickerManager;
	private RendererManager rendererManager;

	public GameThread(SurfaceHolder holder, TickerManager tickerManager,
			RendererManager rendererManager) {
		super();
		this.holder = holder;
		this.tickerManager = tickerManager;
		this.rendererManager = rendererManager;
		mFramedelay = 1000000000 / mFramerate;
	}

	private long mStartTime;
	private long mEndTime;
	private long nexStartTime;

	@Override
	public void run() {
		nexStartTime = System.nanoTime();
		Canvas canvas = null;
		int ticksCount;
		while (running) {
			mStartTime = nexStartTime;
			// tick
			if (ticking) {
				tickerManager.tick(mFramedelay / 1000000000.0d);
			}
			ticksCount = 1;
			canvas = holder.lockCanvas();
			// draw
			if (canvas != null) {
				try {
					synchronized (holder) {
						rendererManager.draw(mFramedelay, canvas);
					}
				} finally {
					holder.unlockCanvasAndPost(canvas);
				}
				// rendererManager.onDraw(mFramedelay, canvas);
				// holder.unlockCanvasAndPost(canvas);
			}

			mEndTime = System.nanoTime();
			// Log.d(TAG, "Time taken: " + ((mEndTime - mStartTime) /
			// 1000000000.0f));
			if (mEndTime - mStartTime < mFramedelay) {
				// sleep if computation faster than framerate
				try {

					sleep((mFramedelay - mEndTime + mStartTime) / 1000000);
					// Log.d(TAG, "sleep: " + ((mFramedelay - mEndTime +
					// mStartTime) / 1000000000.0f));
				} catch (InterruptedException e) {
				}
			} else if (ticking) {
				// catch up ticks if computation slower than framerate
				while (ticksCount < mMaxTicks && mEndTime - mStartTime > mFramedelay * ticksCount) {
					tickerManager.tick(mFramedelay / 1000000000.0d);
					ticksCount++;
				}
				Log.w(TAG, "Skipped: " + (ticksCount - 1));
			}
			if (ticking) {
				// try to achieve a certain amount of ticks per second
				if (ticksCount == mMaxTicks) {
					Log.w(TAG, "Running under minimum tps");
					nexStartTime = mEndTime;
				} else {
					nexStartTime = mStartTime + mFramedelay * ticksCount;
				}
			} else {
				// don't stack delay on ticks
				nexStartTime = mEndTime;
			}
			// mEndTime = System.nanoTime();
			// Log.d(TAG, "framedelay: " + (mEndTime - mStartTime) /
			// 1000000000.0f + " skipped: "
			// + (ticksCount - 1));
		}
	}
}
