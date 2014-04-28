package com.boidzgame.gameplay.rendering;

import android.graphics.Canvas;

public abstract class Renderer implements IRenderer {
    protected int mLayer;

    public int getLayer() {
        return mLayer;
    }

    public abstract void draw(int delay, Canvas canvas, double scaleX, double scaleY);
}
