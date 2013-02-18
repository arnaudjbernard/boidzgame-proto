package com.boidzgame.gameplay.component.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.level.Level;
import com.boidzgame.gameplay.rendering.Renderer;

public class CircleRenderer extends Renderer {
	@SuppressWarnings("unused")
	private static final String TAG = "CircleRenderer";
	protected int color;
	protected Coordinates mCoordinates;
	protected Level mLevel;
	protected Bitmap sImage;
	protected int layer = 0;

	public CircleRenderer(int color, int layer) {
		super();
		this.color = color;
		this.layer = layer;
	}

	public void setup(Level level, Coordinates coordinates) {
		this.mCoordinates = coordinates;
		this.mLevel = level;
		mLevel.rendererManager.register(this, layer);
	}

	public void clean() {
		mLevel.rendererManager.unregister(this);
		this.mLevel = null;
		this.mCoordinates = null;
	}

	private Paint paint = new Paint();

	@Override
	public void draw(int delay, Canvas canvas, double scaleX, double scaleY) {
		paint.setColor(color);
		canvas.drawCircle((float) (mCoordinates.positionX * scaleX),
				(float) (mCoordinates.positionY * scaleY),
				(float) (mCoordinates.width * scaleX), paint);

		// Paint paint = new Paint();
		// paint.setColor(0xFFFFFFFF);
		// canvas.drawCircle((float) mCoordinates.positionX, (float)
		// mCoordinates.positionY, 2, paint);
	}
}
