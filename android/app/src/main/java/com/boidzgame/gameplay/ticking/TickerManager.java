package com.boidzgame.gameplay.ticking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TickerManager {
    private final TickerComparator mTickerComparator = new TickerComparator();
    private boolean mSorted = false;
    // Ticker
    private List<ITicker> mTickers = new ArrayList<ITicker>();
    private int mIndex;

    public void register(ITicker ticker) {
        register(ticker, 0);
    }

    public void register(ITicker ticker, int priority) {
        ticker.setPriority(priority);
        mTickers.add(ticker);
        mSorted = false;
    }

    public void unregister(ITicker ticker) {
        int index = mTickers.indexOf(ticker);
        if (index <= mIndex) {
            // in case it was before the currently ticking ticker, adjust the
            // index to avoid
            // ticking twice
            mIndex--;
        }
        mTickers.remove(index);
    }

    public void tick(double delay) {
        if (!mSorted) {
            Collections.sort(mTickers, mTickerComparator);
            mSorted = true;
        }
        for (mIndex = mTickers.size() - 1; mIndex >= 0; mIndex--) {
            mTickers.get(mIndex).tick(delay);
        }
    }

    public void setup() {
    }

    public void clean() {
    }

    private class TickerComparator implements Comparator<ITicker> {
        public int compare(ITicker lhs, ITicker rhs) {
            return rhs.getPriority() - lhs.getPriority();
        }
    }
}
