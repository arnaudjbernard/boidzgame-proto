package com.boidzgame.gameplay;

import java.util.ArrayList;
import java.util.Iterator;

import android.view.MotionEvent;

import com.boidzgame.gameplay.ticking.Ticker;
import com.boidzgame.gameplay.ticking.TickerManager;

/**
 * 
 */
public class TouchManager extends Ticker {
	@SuppressWarnings("unused")
	private static final String TAG = "TouchManager";

	public ArrayList<Finger> fingersList;
	public ArrayList<Finger> newFingersList;

	/** Mutex to protect thread safety when editing the finger list */
	private Object mUpdateMutex;

	private TickerManager tickerManager;

	public TouchManager(TickerManager tickerManager) {
		super();
		mUpdateMutex = new Object();
		fingersList = new ArrayList<Finger>(4);
		newFingersList = new ArrayList<Finger>(4);
		this.tickerManager = tickerManager;
	}

	public void setup() {
		tickerManager.register(this, 0);
	}

	public void clean() {
		tickerManager.unregister(this);
	}

	public void reset() {
		fingersList.clear();
		newFingersList.clear();
	}

	public boolean addTouch(MotionEvent event, double offsetX, double offsetY,
			double scaleX, double scaleY) {
		// loop over the pointers
		int actionIndex = event.getActionIndex();
		int action = event.getActionMasked();
		Finger finger;
		synchronized (mUpdateMutex) {

			if (action == MotionEvent.ACTION_DOWN
					|| action == MotionEvent.ACTION_POINTER_DOWN) {
				// create new finger
				finger = new Finger();// TODO: Finger pool
				finger.id = event.getPointerId(actionIndex);
				finger.nextX = (event.getX(actionIndex) + offsetX) / scaleX;
				finger.nextY = (event.getY(actionIndex) + offsetY) / scaleY;
				newFingersList.add(finger);
				// Log.i(TAG, "a add " + finger.id);
			} else if (action == MotionEvent.ACTION_UP
					|| action == MotionEvent.ACTION_POINTER_UP) {
				// remove finger
				finger = getFingerById(event.getPointerId(actionIndex));
				if (finger != null) {
					finger.toRemove = true;
				}
				// Log.i(TAG, "a remove " + finger.id);
			}

			// update the current fingers
			for (int i = 0; i < event.getPointerCount(); i++) {
				int id = event.getPointerId(i);
				finger = getFingerById(id);
				if (finger != null && !finger.toRemove) {
					finger.nextX = (event.getX(i) + offsetX) / scaleX;
					finger.nextY = (event.getY(i) + offsetY) / scaleY;
					// Log.i(TAG, "finger update " + finger.id + "    " +
					// finger.nextX + "    "
					// + finger.nextY);
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
	 * @see com.boidzgame.gameplay.ticking.ITicker#tick(int)
	 */
	@Override
	public void tick(double delay) {
		Iterator<Finger> it;
		synchronized (mUpdateMutex) {
			// remove the ones gone
			if (fingersList.size() > 0) {
				it = fingersList.iterator();
				while (it.hasNext()) {
					Finger f = it.next();
					if (f.toRemove) {
						it.remove();
						// Log.i(TAG, "remove " + f.id);
					}
				}
			}
			// add the new ones
			if (newFingersList.size() > 0) {
				it = newFingersList.iterator();
				while (it.hasNext()) {
					Finger f = it.next();
					fingersList.add(f);
					it.remove();
					// Log.i(TAG, "add " + f.id);
				}
			}
			// update the positions
			for (int i = fingersList.size() - 1; i >= 0; i--) {
				Finger f = fingersList.get(i);
				f.x = f.nextX;
				f.y = f.nextY;
			}
		}
	}
}
