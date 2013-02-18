package com.boidzgame.gameplay.rendering;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.boidzgame.gameplay.level.Level;

public class LevelView extends SurfaceView implements IRenderer, SurfaceHolder.Callback {
	private static final String TAG = "LevelActivity";

	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 480;

	public int color = 0xff00ff;
	private Level level;
	private boolean rendering;
	private boolean mListening = false;

	private int mWidth = DEFAULT_WIDTH;
	private int mHeight = DEFAULT_HEIGHT;
	private double mScaleX = 1.0d;
	private double mScaleY = 1.0d;
	private double mTouchOffsetY = 0.0d;
	private double mTouchOffsetX = 0.0d;

	public LevelView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(this);
	}

	public void setup(Level level) {
		this.level = level;
		level.rendererManager.register(this, -10);
		this.mListening = true;
	}

	public void clean() {
		mListening = false;
		level.rendererManager.unregister(this);
		this.level = null;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (rendering && mListening) {
			return level.touchManager.addTouch(event, mTouchOffsetX, mTouchOffsetY,
					mScaleX, mScaleY);
		}
		return false;
	}

	private void updateActivitySize() {
		if (level != null) {
			level.width = mWidth;
			level.height = mHeight;

			level.rendererManager.setScaleX(mScaleX);
			level.rendererManager.setScaleY(mScaleY);
		}
	}

	// ////////////////////////////////////////////////////////////////
	// IRenderer implementation
	// ////////////////////////////////////////////////////////////////
	private int mLayer;

	public int getLayer() {
		return mLayer;
	}

	public void setLayer(int layer) {
		mLayer = layer;
	}

	public void draw(int delay, Canvas canvas, double scaleX, double scaleY) {
		canvas.drawColor(color);
	}

	// ////////////////////////////////////////////////////////////////
	// SurfaceHolder.Callback implementation
	// ////////////////////////////////////////////////////////////////
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		mWidth = width;
		mHeight = height;

		mScaleX = (double) mWidth / DEFAULT_WIDTH;
		mScaleY = (double) mHeight / DEFAULT_HEIGHT;

		mTouchOffsetX = -mWidth * 0.5d;
		mTouchOffsetY = -mHeight * 0.5d;

		updateActivitySize();

		Log.d(TAG, "Update screen size: " + mWidth + "x" + mHeight);
		Log.d(TAG, "scale: " + mScaleX + "x" + mScaleY);
	}

	public void surfaceCreated(SurfaceHolder holder) {
		rendering = true;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		rendering = false;
	}
}