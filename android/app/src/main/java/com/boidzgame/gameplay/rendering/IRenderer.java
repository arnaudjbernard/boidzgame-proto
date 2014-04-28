package com.boidzgame.gameplay.rendering;

import android.graphics.Canvas;

public interface IRenderer {
    public int getLayer();

    public void draw(int delay, Canvas canvas, double scaleX, double scaleY);
}
