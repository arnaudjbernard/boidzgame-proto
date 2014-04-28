package com.boidzgame.gameplay;

import android.view.MotionEvent;

import com.boidzgame.gameplay.boidz.component.Coordinates;
import com.boidzgame.gameplay.boidz.component.renderer.CircleRenderer;
import com.boidzgame.gameplay.boidz.level.Level;
import com.boidzgame.gameplay.ticking.Ticker;
import com.boidzgame.gameplay.ticking.TickerManager;

import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class TouchManager extends Ticker {
    @SuppressWarnings("unused")
    private static final String TAG = "TouchManager";
    private static final boolean DEBUG_CIRCLE = false;
    private static CircleRenderer circleRenderer;
    private static Coordinates coordinates;

    public List<Finger> fingersList;
    public List<Finger> newFingersList;
    public List<Finger> removedFingersList;

    /**
     * Mutex to protect thread safety when editing the finger list
     */
    private Object mUpdateMutex;

    private TickerManager tickerManager;

    public TouchManager(TickerManager tickerManager) {
        super();
        mUpdateMutex = new Object();
        fingersList = new LinkedList<Finger>();
        newFingersList = new LinkedList<Finger>();
        removedFingersList = new LinkedList<Finger>();
        this.tickerManager = tickerManager;
    }

    public void setup(Level level) {
        if(DEBUG_CIRCLE) {
            coordinates = new Coordinates();
            coordinates.width = 50;
            circleRenderer = new CircleRenderer(0xffff00ff, 10);
            circleRenderer.setup(level, coordinates);
        }
        tickerManager.register(this, 0);
    }

    public void clean() {
        tickerManager.unregister(this);
    }

    public void reset() {
        fingersList.clear();
        newFingersList.clear();
        removedFingersList.clear();
    }

    public boolean addTouch(MotionEvent event, double offsetX, double offsetY, double scaleX,
                            double scaleY) {
        // loop over the pointers
        int actionIndex = event.getActionIndex();
        int action = event.getActionMasked();
        Finger finger;
        synchronized (mUpdateMutex) {

            if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) {
                // create new finger
                finger = new Finger();// TODO: Finger pool
                finger.id = event.getPointerId(actionIndex);
                finger.firstX = (event.getX(actionIndex) + offsetX) / scaleX;
                finger.firstY = (event.getY(actionIndex) + offsetY) / scaleY;
                newFingersList.add(finger);
                // Log.i(TAG, "finger event add " + finger.id);
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
                // remove finger
                finger = getFingerById(event.getPointerId(actionIndex));
                if (finger != null) {
                    finger.toRemove = true;
                    finger.lastX = (event.getX(actionIndex) + offsetX) / scaleX;
                    finger.lastY = (event.getY(actionIndex) + offsetY) / scaleY;
                }
                // Log.i(TAG, "finger event remove " + finger.id);
            }

            // update the current fingers
            for (int i = 0; i < event.getPointerCount(); i++) {
                int id = event.getPointerId(i);
                finger = getFingerById(id);
                if (finger != null && !finger.toRemove) {
                    finger.nextX = (event.getX(i) + offsetX) / scaleX;
                    finger.nextY = (event.getY(i) + offsetY) / scaleY;
                    // Log.i(TAG, "finger event update " + finger.id + "    " +
                    // finger.nextX + "    "
                    // + finger.nextY);
                }
                if(DEBUG_CIRCLE && i == 0 && finger != null) {
                    coordinates.positionX = finger.nextX;
                    coordinates.positionY = finger.nextY;
                }
            }
        }
        return true;
    }

    /**
     * Returns the finger which attribute id is id from the fingersList and
     * newFingersList.
     *
     * @param id the id of the finger to retrieve
     * @return null if no finger have this id, the correct finger otherwise
     */
    private Finger getFingerById(int id) {
        Finger result = null;
        Finger f;
        for (int i = fingersList.size() - 1; i >= 0; i--) {
            f = fingersList.get(i);
            if (f.id == id) {
                result = f;
                break;
            }
        }
        if (result == null) {
            for (int i = newFingersList.size() - 1; i >= 0; i--) {
                f = newFingersList.get(i);
                if (f.id == id) {
                    result = f;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Update the fingers according
     *
     * @see com.boidzgame.gameplay.ticking.ITicker#tick(double)
     */
    @Override
    public void tick(double delay) {
        synchronized (mUpdateMutex) {
            // clear the removed list
            if (!removedFingersList.isEmpty()) {
                removedFingersList.clear();
            }

            if (!fingersList.isEmpty()) {
                for (Finger f : fingersList) {
                    f.age += delay;
                    if (f.toRemove) {
                        // remove the ones gone
                        removedFingersList.add(f);
                        // Log.i(TAG, "remove " + f.id);
                    } else {
                        // update the positions
                        f.x = f.nextX;
                        f.y = f.nextY;
                    }
                }
                fingersList.removeAll(removedFingersList);
            }

            // add the new ones
            if (!newFingersList.isEmpty()) {
                for (Finger f : newFingersList) {
                    f.x = f.firstX;
                    f.y = f.firstY;
                    fingersList.add(f);
                    // Log.i(TAG, "add " + f.id);
                }
                newFingersList.clear();
            }
            // Log.i(TAG, "finger count" + fingersList.size());
        }
    }
}
