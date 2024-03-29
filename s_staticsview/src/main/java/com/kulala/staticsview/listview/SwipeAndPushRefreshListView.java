
package com.kulala.staticsview.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ListView;

import com.kulala.staticsview.R;
/**
 * 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓━━━━┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　　┃　＞　　　＜　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃...　⌒　...　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃　Code is far away from bug with the animal protecting
 * 　　　　　　　　　┃　史　┃   神兽保佑,代码无bug
 * 　　　　　　　　　┃　诗　┃
 * 　　　　　　　　　┃　之　┃
 * 　　　　　　　　　┃　宠　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┗━━━┓
 * 　　　　　　　　　┃BUG天敌　　　┣┓┣┓┣┓┣┓┣┓
 * 　　　　　　　　　┃　　　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 */
/**
 * 侧滑删除功能,详情看sample
 *
 * swap功能
 * list_states.setOnLoadBottomListener
 * list_states.setonLoadHeaderListener
 //变动查下最近消息
 ManagerWarnings.getInstance().DBClearDataAll();
 list_states.setSelection(0);
 list_states.loadStart();
 OCtrlCar.getInstance().ccmd1221_getWarninglist(0,0,2,0,20,selectCarId);
 // 为了显示效果，采用延迟加载
 new Handler().postDelayed(new Runnable() {
@Override
public void run() {
list_states.loadComplete();
}
}, 2000);
 */
public class SwipeAndPushRefreshListView extends ListView implements AbsListView.OnScrollListener {
    private Boolean mIsHorizontal;
    private View mPreItemView;
    private View mCurrentItemView;
    private float mFirstX;
    private float mFirstY;
    private int mRightViewWidth = 135;
    // private boolean mIsInAnimation = false;
    private final int mDuration = 100;
    private final int mDurationStep = 10;
    private boolean mIsShown;
    /**
     * 是否允许footer or Header Swipe
     */
    private boolean mIsFooterCanSwipe = false;
    private boolean mIsHeaderCanSwipe = false;


    //swap
    private View footerView;//底部View
    private View headerView;//顶部View

    int     totalItemCount   = 0;//ListView item个数
    int     firstVisibleItem = 0;//首item号
    int     lastVisibleItem  = 0;//最后可见的Item
    boolean isLoading        = false;//是否加载标示
    private LayoutInflater mInflater;
    private int            measuredHeight;
    public SwipeAndPushRefreshListView(Context context) {
        super(context);
        initView(context);
    }
    public SwipeAndPushRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public SwipeAndPushRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }
    public int getLastVisibleItem() {
        return lastVisibleItem;
    }
    public int getFirstVisibleItem() {
        return firstVisibleItem;
    }
    /**
     * 初始化ListView
     */
    private void initView(Context context) {
        mInflater = LayoutInflater.from(context);
        footerView = mInflater.inflate(R.layout.listview_refresh_header, null);
        headerView = mInflater.inflate(R.layout.listview_refresh_header, null);
        this.addFooterView(footerView);
        this.addHeaderView(headerView);
        headerView.measure(0, 0);//先测量再拿到它的高度
        measuredHeight = headerView.getMeasuredHeight();
        headerView.setPadding(0, -measuredHeight, 0, 0);// 隐藏
        footerView.setPadding(0, -measuredHeight, 0, 0);// 隐藏
        this.setOnScrollListener(this);
        this.setOnDragListener(new OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                event.getAction();
                return false;
            }
        });
    }
    //无效
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //当滑动到底端，并滑动状态为 not scrolling
        if (scrollState != SCROLL_STATE_IDLE) return;
        if (lastVisibleItem == totalItemCount || lastVisibleItem == totalItemCount - 2) {
            if (onLoadBottomListener == null) return;
            if (!isLoading) {
                isLoading = true;
                //设置可见
                footerView.setPadding(0, 0, 0, 0);// 显示
                onLoadBottomListener.onLoad();
            }
        } else if (firstVisibleItem == 0) {//滑至顶部且结束滑动
            if (onLoadHeaderListener == null) return;
            if (!isLoading) {
                isLoading = true;
                //设置可见
                headerView.setPadding(0, 0, 0, 0);// 显示
                //加载数据
                onLoadHeaderListener.onLoad();

            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.firstVisibleItem = firstVisibleItem;
        this.lastVisibleItem = firstVisibleItem + visibleItemCount - 1 - 1;
        this.totalItemCount = totalItemCount;
    }

    /**
     * 加载数据接口
     */

    private OnLoadBottomListener onLoadBottomListener;

    public void setOnLoadBottomListener(OnLoadBottomListener onLoadBottomListener) {
        this.onLoadBottomListener = onLoadBottomListener;
    }
    private OnLoadHeaderListener onLoadHeaderListener;

    public void setonLoadHeaderListener(OnLoadHeaderListener onLoadHeaderListener) {
        this.onLoadHeaderListener = onLoadHeaderListener;
    }
    public interface OnLoadBottomListener {
        void onLoad();
    }

    public interface OnLoadHeaderListener {
        void onLoad();
    }

    /**
     * 数据加载完成
     */
    public void loadComplete() {
        headerView.setPadding(0, -measuredHeight, 0, 0);// 隐藏
        footerView.setPadding(0, -measuredHeight, 0, 0);// 隐藏
        isLoading = false;
    }
    /**
     * 数据初始要显示load
     */
    public void loadStart() {
        headerView.setPadding(0, 0, 0, 0);// 显示
        footerView.setPadding(0, 0, 0, 0);// 显示
        isLoading = true;
    }
    /**
     * return true, deliver to listView. return false, deliver to child. if
     * move, return true
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mIsHorizontal = null;
                mFirstX = lastX;
                mFirstY = lastY;
                int motionPosition = pointToPosition((int)mFirstX, (int)mFirstY);//itemPos
                if (motionPosition >= 0) {
                    View currentItemView = getChildAt(motionPosition - getFirstVisiblePosition());
                    mPreItemView = mCurrentItemView;
                    mCurrentItemView = currentItemView;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;
                if (Math.abs(dx) >= 5 && Math.abs(dy) >= 5) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mIsShown && (mPreItemView != mCurrentItemView || isHitCurItemLeft(lastX))) {
                    System.out.println("1---> hiddenRight");
                    /**
                     * 情况一：
                     * <p>
                     * 一个Item的右边布局已经显示，
                     * <p>
                     * 这时候点击任意一个item, 那么那个右边布局显示的item隐藏其右边布局
                     */
                    hiddenRight(mPreItemView);
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    private boolean isHitCurItemLeft(float x) {
        return x < getWidth() - mRightViewWidth;
    }

    /**
     * @param dx
     * @param dy
     * @return judge if can judge scroll direction
     */
    private boolean judgeScrollDirection(float dx, float dy) {
        boolean canJudge = true;
        if (Math.abs(dx) > 30 && Math.abs(dx) > 2 * Math.abs(dy)) {
            mIsHorizontal = true;
        } else if (Math.abs(dy) > 30 && Math.abs(dy) > 2 * Math.abs(dx)) {
            mIsHorizontal = false;
        } else {
            canJudge = false;
        }
        return canJudge;
    }

    /**
     * @param posX
     * @param posY
     * @return judge if can footer judge
     */
    private boolean judgeFooterView(float posX, float posY) {
        // if footer can swipe
        if (mIsFooterCanSwipe) {
            return true;
        }
        // footer cannot swipe
        int selectPos = pointToPosition((int)posX, (int)posY);
        if (selectPos >= (getCount() - getFooterViewsCount())) {
            // is footer ,can not swipe
            return false;
        }
        // not footer can swipe
        return true;
    }

    /**
     * @param posX
     * @param posY
     * @return judge if can judge scroll direction
     */
    private boolean judgeHeaderView(float posX, float posY) {
        // if header can swipe
        if (mIsHeaderCanSwipe) {
            return true;
        }
        // header cannot swipe
        int selectPos = pointToPosition((int)posX, (int)posY);
        if (selectPos >= 0 && selectPos < getHeaderViewsCount()) {
            // is header ,can not swipe
            return false;
        }
        // not header can swipe
        return true;
    }

    /**
     * return false, can't move any direction. return true, cant't move
     * vertical, can move horizontal. return super.onTouchEvent(ev), can move
     * both.
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        float lastX = ev.getX();
        float lastY = ev.getY();
        // test footer and header
        if (!judgeFooterView(mFirstX, mFirstY) || !judgeHeaderView(mFirstX, mFirstY)) {
            return super.onTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                if(mCurrentItemView == null)return super.onTouchEvent(ev);
                float dx = lastX - mFirstX;
                float dy = lastY - mFirstY;
                // confirm is scroll direction
                if (mIsHorizontal == null) {
                    if (!judgeScrollDirection(dx, dy)) {
                        break;
                    }
                }

                if (mIsHorizontal) {
                    if (mIsShown && mPreItemView != mCurrentItemView) {
                        /**
                         * 情况二：
                         * <p>
                         * 一个Item的右边布局已经显示，
                         * <p>
                         * 这时候左右滑动另外一个item,那个右边布局显示的item隐藏其右边布局
                         * <p>
                         * 向左滑动只触发该情况，向右滑动还会触发情况五
                         */
                        hiddenRight(mPreItemView);
                    }

                    if (mIsShown && mPreItemView == mCurrentItemView) {
                        dx = dx - mRightViewWidth;
                    }

                    // can't move beyond boundary
                    if (dx < 0 && dx > -mRightViewWidth) {
                        mCurrentItemView.scrollTo((int)(-dx), 0);
                    }

                    return true;
                } else {
                    if (mIsShown) {
                        /**
                         * 情况三：
                         * <p>
                         * 一个Item的右边布局已经显示，
                         * <p>
                         * 这时候上下滚动ListView,那么那个右边布局显示的item隐藏其右边布局
                         */
                        hiddenRight(mPreItemView);
                    }
                }

                break;

            case MotionEvent.ACTION_UP:
//                boolean nun = mFirstX - lastX>0;
            case MotionEvent.ACTION_CANCEL:
                clearPressedState();
                if (mIsShown) {
                    /**
                     * 情况四：
                     * <p>
                     * 一个Item的右边布局已经显示，
                     * <p>
                     * 这时候左右滑动当前一个item,那个右边布局显示的item隐藏其右边布局
                     */
                    hiddenRight(mPreItemView);
                }

                if (mIsHorizontal != null && mIsHorizontal) {
                    if (mFirstX - lastX > mRightViewWidth / 2) {
                        showRight(mCurrentItemView);
                    } else {
                        /**
                         * 情况五：
                         * <p>
                         * 向右滑动一个item,且滑动的距离超过了右边View的宽度的一半，隐藏之。
                         */
                        hiddenRight(mCurrentItemView);
                    }
                    MotionEvent obtain = MotionEvent.obtain(ev);
				    obtain.setAction(MotionEvent.ACTION_CANCEL);
				    super.onTouchEvent(obtain);
				    return true;
                }

                break;
        }

        return super.onTouchEvent(ev);
    }

    private void clearPressedState() {
        // TODO current item is still has background, issue
        if(mCurrentItemView == null)return;
        mCurrentItemView.setPressed(false);
        setPressed(false);
        refreshDrawableState();
        // invalidate();
    }

    private void showRight(View view) {
        if(view == null)return;
        Message msg = new MoveHandler().obtainMessage();
        msg.obj = view;
        msg.arg1 = view.getScrollX();
        msg.arg2 = mRightViewWidth;
        msg.sendToTarget();

        mIsShown = true;
    }

    private void hiddenRight(View view) {
        if (mCurrentItemView == null || view==null) {
            return;
        }
        Message msg = new MoveHandler().obtainMessage();//
        msg.obj = view;
        msg.arg1 = view.getScrollX();
        msg.arg2 = 0;

        msg.sendToTarget();

        mIsShown = false;
    }

    /**
     * show or hide right layout animation
     */
    @SuppressLint("HandlerLeak")
    class MoveHandler extends Handler {
        int stepX = 0;

        int fromX;

        int toX;

        View view;

        private boolean mIsInAnimation = false;

        private void animatioOver() {
            mIsInAnimation = false;
            stepX = 0;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (stepX == 0) {
                if (mIsInAnimation) {
                    return;
                }
                mIsInAnimation = true;
                view = (View)msg.obj;
                fromX = msg.arg1;
                toX = msg.arg2;
                stepX = (int)((toX - fromX) * mDurationStep * 1.0 / mDuration);
                if (stepX < 0 && stepX > -1) {
                    stepX = -1;
                } else if (stepX > 0 && stepX < 1) {
                    stepX = 1;
                }
                if (Math.abs(toX - fromX) < 10) {
                    view.scrollTo(toX, 0);
                    animatioOver();
                    return;
                }
            }

            fromX += stepX;
            boolean isLastStep = (stepX > 0 && fromX > toX) || (stepX < 0 && fromX < toX);
            if (isLastStep) {
                fromX = toX;
            }

            view.scrollTo(fromX, 0);
            invalidate();

            if (!isLastStep) {
                this.sendEmptyMessageDelayed(0, mDurationStep);
            } else {
                animatioOver();
            }
        }
    }

    public int getRightViewWidth() {
        return mRightViewWidth;
    }

    public void setRightViewWidth(int mRightViewWidth) {
        this.mRightViewWidth = mRightViewWidth;
    }

    /**
     * 设置list的脚是否能够swipe
     * 
     * @param canSwipe
     */
    public void setFooterViewCanSwipe(boolean canSwipe) {
        mIsFooterCanSwipe = canSwipe;
    }

    /**
     * 设置list的头是否能够swipe
     * 
     * @param canSwipe
     */
    public void setHeaderViewCanSwipe(boolean canSwipe) {
        mIsHeaderCanSwipe = canSwipe;
    }

    /**
     * 设置 footer and header can swipe
     * 
     * @param footer
     * @param header
     */
    public void setFooterAndHeaderCanSwipe(boolean footer, boolean header) {
        mIsHeaderCanSwipe = header;
        mIsFooterCanSwipe = footer;
    }
}
