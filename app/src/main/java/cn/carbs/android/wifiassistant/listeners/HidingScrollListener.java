package cn.carbs.android.wifiassistant.listeners;

import android.support.v7.widget.RecyclerView;
import android.util.Log;

/*
* This class is a ScrollListener for RecyclerView that allows to show/hide
* views when list is scrolled.
* */
public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {

    private static final float HIDE_THRESHOLD = 10;
    private static final float SHOW_THRESHOLD = 70;

    private int mViewOffset = 0;
    private int mTotalScrolledDistance;
    private int mViewMoveDistance;
    private boolean mControlsVisible = true;
    private boolean mDirectionUp = true;

    public HidingScrollListener(int distance, boolean directionUp){
        mViewMoveDistance = distance;
        mDirectionUp = directionUp;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if(newState == RecyclerView.SCROLL_STATE_IDLE) {
            Log.d("7155","onScrollStateChanged  newState == RecyclerView.SCROLL_STATE_IDLE");
            if(mDirectionUp) {
                if(mTotalScrolledDistance < mViewMoveDistance) {
                    setVisible();
                } else {
                    if (mControlsVisible) {
                        if (mViewOffset > HIDE_THRESHOLD) {
                            setInvisible();
                        } else {
                            setVisible();
                        }
                    } else {
                        if ((mViewMoveDistance - mViewOffset) > SHOW_THRESHOLD) {
                            setVisible();
                        } else {
                            setInvisible();
                        }
                    }
                }
            }else{
                if (mTotalScrolledDistance > -mViewMoveDistance) {
                    Log.d("7155", "onScrollStateChanged  31");
                    setVisible();
                } else {
                    Log.d("7155", "onScrollStateChanged  34");
                    if (mControlsVisible) {
                        Log.d("7155", "onScrollStateChanged  36");
                        if (mViewOffset < -HIDE_THRESHOLD) {
                            Log.d("7155", "onScrollStateChanged  38");
                            setInvisible();
                        } else {
                            Log.d("7155", "onScrollStateChanged  41");
                            setVisible();
                        }
                    } else {
                        Log.d("7155", "onScrollStateChanged  45");
                        if ((mViewMoveDistance + mViewOffset) > SHOW_THRESHOLD) {
                            Log.d("7155", "onScrollStateChanged  47");
                            setVisible();
                        } else {
                            Log.d("7155", "onScrollStateChanged  49");
                            setInvisible();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        clipViewOffset();
        Log.d("777","onMoved mViewOffset:" + mViewOffset + " dy:" + dy);//手指向上滑dy>0
        onMoved(mViewOffset);
        if(mDirectionUp) {
            if((mViewOffset <mViewMoveDistance && dy>0) || (mViewOffset >0 && dy<0)) {
                mViewOffset += dy;
            }
            if (mTotalScrolledDistance < 0) {
                mTotalScrolledDistance = 0;
            } else {
                mTotalScrolledDistance += dy;
            }
        }else{
            Log.d("777","1 mViewOffset: " + mViewOffset + " dy :" + dy);
            if ((mViewOffset > -mViewMoveDistance && dy > 0) || (mViewOffset < 0 && dy < 0)) {
                mViewOffset += (-dy);
                Log.d("777","2 mViewOffset: " + mViewOffset + " dy :" + dy);
            }
            if (mTotalScrolledDistance > 0) {
                mTotalScrolledDistance = 0;
            } else {
                mTotalScrolledDistance += (-dy);
            }
        }
    }

    //TODO
    private void clipViewOffset() {
        //以原先所在的位置mViewOffset=0，向上mViewOffset>0
        if(mDirectionUp) {
            if (mViewOffset > mViewMoveDistance) {
                mViewOffset = mViewMoveDistance;
            } else if (mViewOffset < 0) {
                mViewOffset = 0;
            }
        }else{
            if (mViewOffset < -mViewMoveDistance) {
                mViewOffset = -mViewMoveDistance;
            } else if (mViewOffset > 0) {
                mViewOffset = 0;
            }
        }
    }

    private void setVisible() {
        Log.d("7155","setVisible() mViewOffset:" + mViewOffset );
        if(mDirectionUp) {
            if (mViewOffset >= 0) {//change > to >=
                Log.d("7155","onShow");
                onShow();
                mViewOffset = 0;
            }
        }else{
            if (mViewOffset <= 0) {
                Log.d("7155","onShow");
                onShow();
                mViewOffset = 0;
            }
        }
        mControlsVisible = true;
    }

    private void setInvisible() {
        Log.d("7155","setInvisible() mViewOffset:" + mViewOffset + " mViewMoveDistance: " + mViewMoveDistance );
        if(mDirectionUp) {
            if (mViewOffset <= mViewMoveDistance) {//change < to <=
                Log.d("7155","onHide");
                onHide();
                mViewOffset = mViewMoveDistance;
            }
        }else{
            if (mViewOffset >= -mViewMoveDistance) {//change > to >=
                Log.d("7155","onHide");
                onHide();
                mViewOffset = -mViewMoveDistance;
            }
        }
        mControlsVisible = false;
    }

    public abstract void onMoved(int distance);
    public abstract void onShow();
    public abstract void onHide();
}