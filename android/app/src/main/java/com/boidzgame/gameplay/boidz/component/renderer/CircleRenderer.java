package com.boidzgame.gameplay.boidz.component.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.boidzgame.gameplay.boidz.component.Coordinates;
import com.boidzgame.gameplay.boidz.level.Level;
import com.boidzgame.gameplay.rendering.Renderer;

public class CircleRenderer extends Renderer {
    @SuppressWarnings("unused")
    private static final String TAG = "CircleRenderer";
    public Paint paint = new Paint();
    protected int mColor;
    protected Coordinates mCoordinates;
    protected Level mLevel;
    protected Bitmap sImage;

    /**
     * @param color argb
     * @param layer superior on top
     */
    public CircleRenderer(int color, int layer) {
        super();
        this.mColor = color;
        this.mLayer = layer;
        paint.setColor(mColor);
    }

    public void setup(Level level, Coordinates coordinates) {
        this.mCoordinates = coordinates;
        this.mLevel = level;
        mLevel.rendererManager.register(this);
    }

    public void clean() {
        mLevel.rendererManager.unregister(this);
        this.mLevel = null;
        this.mCoordinates = null;
    }

    @Override
    public void draw(int delay, Canvas canvas, double scaleX, double scaleY) {
        if (mCoordinates.width == 0)
            Log.w(TAG, "mCoordinates.width should not be 0");
        float cx = canvas.getWidth() * 0.5f + (float) (mCoordinates.positionX * scaleX);
        float cy = canvas.getHeight() * 0.5f + (float) (mCoordinates.positionY * scaleY);
        canvas.drawCircle(cx, cy, (float) (mCoordinates.width * scaleX), paint);

        // Paint paint = new Paint();
        // paint.setColor(0xFFFFFFFF);
        // canvas.drawCircle((float) mCoordinates.positionX, (float)
        // mCoordinates.positionY, 2, paint);
    }
}
