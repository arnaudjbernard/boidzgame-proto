package com.boidzgame.gameplay.component.renderer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.boidzgame.gameplay.component.Coordinates;
import com.boidzgame.gameplay.level.Level;
import com.boidzgame.gameplay.rendering.Renderer;

public class DrawableRenderer extends Renderer {
	// private static final String TAG = "Renderer";
	protected int drawable;
	protected Coordinates mCoordinates;
	protected Level mLevel;
	protected Bitmap mBitmap;
	protected int imageWidth;
	protected int imageHeight;
	protected Matrix mMatrix = new Matrix();
	protected int layer = 0;

	public DrawableRenderer(int drawable) {
		this.drawable = drawable;
	}

	public DrawableRenderer(int drawable, int layer) {
		this.drawable = drawable;
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

	@Override
	public void draw(int delay, Canvas canvas, double scaleX, double scaleY) {
		if (mBitmap == null) {
			mBitmap = BitmapFactory.decodeResource(mLevel.levelActivity.getResources(),
					drawable);
			imageWidth = (int) (mBitmap.getWidth() * scaleX + 0.5f);
			imageHeight = (int) (mBitmap.getHeight() * scaleY + 0.5f);
			mBitmap = Bitmap.createScaledBitmap(mBitmap, imageWidth, imageHeight, false);
		}

		double sx = mCoordinates.speedX;
		double sy = mCoordinates.speedY;
		mMatrix.reset();
		double speed = Math.sqrt(sx * sx + sy * sy);
		if (speed > 0.001) {
			mMatrix.setSinCos((float) (sx / speed), -(float) (sy / speed),
					imageHeight * 0.5f, imageWidth * 0.5f);
			// Log.d(TAG, "sImage.getHeight(): "+
			// (sImage.getHeight())+" sImage.getWidth(): "+(sImage.getWidth()));
		}
		mMatrix.postTranslate((float) mCoordinates.positionX - imageWidth * 0.5f,
				(float) mCoordinates.positionY - imageHeight * 0.5f);
		mMatrix.postScale((float) scaleX, (float) scaleY);
		mMatrix.postTranslate(canvas.getWidth() * 0.5f, canvas.getHeight() * 0.5f);
		canvas.drawBitmap(mBitmap, mMatrix, null);

		// Paint paint = new Paint();
		// paint.setColor(0xFFFFFFFF);
		// canvas.drawCircle((float) mCoordinates.positionX, (float)
		// mCoordinates.positionY, 2, paint);
	}
}
