package com.kulala.staticsview.dragGrid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
/**
 *  <com.kulala.staticsview.dragGrid.DragGridView
 android:id="@+id/dragGridView"
 android:listSelector="@android:color/transparent"
 android:layout_width="match_parent"
 android:layout_height="match_parent"
 android:cacheColorHint="@android:color/transparent"
 android:verticalSpacing="10dip"
 android:horizontalSpacing="10dip"
 android:stretchMode="columnWidth"
 android:gravity="center"
 android:numColumns="3" >


 DragGridView mDragGridView = (DragGridView) findViewById(R.id.dragGridView);
 for (int i = 0; i < 30; i++) {
 HashMap<String, Object> itemHashMap = new HashMap<String, Object>();
 itemHashMap.put("item_image",R.drawable.com_tencent_open_notice_msg_icon_big);
 itemHashMap.put("item_text", "拖拽 " + Integer.toString(i));
 dataSourceList.add(itemHashMap);
 }


 final SimpleAdapter mSimpleAdapter = new SimpleAdapter(this, dataSourceList,
 R.layout.grid_item, new String[] { "item_image", "item_text" },
 new int[] { R.id.item_image, R.id.item_text });

 mDragGridView.setAdapter(mSimpleAdapter);

 mDragGridView.setOnChangeListener(new OnChanageListener() {

@Override
public void onChange(int from, int to) {
HashMap<String, Object> temp = dataSourceList.get(from);
//直接交互item
//				dataSourceList.set(from, dataSourceList.get(to));
//				dataSourceList.set(to, temp);
//				dataSourceList.set(to, temp);


//这里的处理需要注意下
if(from < to){
for(int i=from; i<to; i++){
Collections.swap(dataSourceList, i, i+1);
}
}else if(from > to){
for(int i=from; i>to; i--){
Collections.swap(dataSourceList, i, i-1);
}
}

dataSourceList.set(to, temp);

mSimpleAdapter.notifyDataSetChanged();


}
});
 */
public class DragGridViewZhuAllCanMove extends GridView {
    private long dragResponseMS = 600;//item长按响应的时间， 默认是1000毫秒
    private boolean isDrag = false;//是否可以拖拽，默认不可以

    private int mDownX;
    private int mDownY;
    private int moveX;
    private int moveY;
    private float actionUpY;
    private float actionDownY;
    private int dragPosition,dropPosition;//正在拖拽的position

    private View mStartDragItemView = null;//刚开始拖拽的item对应的View

    /**用于拖拽的镜像，这里直接用一个ImageView*/
    private ImageView mDragImageView;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowLayoutParams;//item镜像的布局参数
    private Bitmap mDragBitmap;//我们拖拽的item对应的Bitmap

    private int mPoint2ItemTop,mPoint2ItemLeft ;//按下的点
    private int mOffset2Top,mOffset2Left;//DragGridView距离屏幕的偏移量
    private int mStatusHeight;//状态栏的高度

    /**DragGridView自动向下滚动的边界值*/
    private int mDownScrollBorder;
    /**DragGridView自动向上滚动的边界值*/
    private int mUpScrollBorder;
    private static final int speed = 20;//DragGridView自动滚动的速度
    private  long actionDownTime;
    private  long actionUpTime;

    public DragGridViewZhuAllCanMove(Context context) {
        this(context, null);
    }

    public DragGridViewZhuAllCanMove(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridViewZhuAllCanMove(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mStatusHeight = getStatusHeight(context); //获取状态栏的高度

    }

    private Handler mHandler = new Handler();

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
//                Log.e("GridView","dispatchTouchEvent ACTION_DOWN");
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();

                //根据按下的X,Y坐标获取所点击item的position
                dragPosition = pointToPosition(mDownX, mDownY);


//                if(dragPosition == AdapterView.INVALID_POSITION || dragPosition == 5){//position 5不拖
//                    return super.dispatchTouchEvent(ev);
//                }
                if(dragPosition == AdapterView.INVALID_POSITION ){//position 5不拖
                    return super.dispatchTouchEvent(ev);
                }
                //根据position获取该item所对应的View
                mStartDragItemView = getChildAt(dragPosition - getFirstVisiblePosition());
                if(onChangeListener!=null && onChangeListener.isNoMove(dragPosition - getFirstVisiblePosition())){
                    return super.dispatchTouchEvent(ev);
                }

                //使用Handler延迟dragResponseMS执行mLongClickRunnable
                mHandler.postDelayed(mLongClickRunnable, dragResponseMS);

                //下面这几个距离大家可以参考我的博客上面的图来理解下
                mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
                mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();

                mOffset2Top = (int) (ev.getRawY() - mDownY);
                mOffset2Left = (int) (ev.getRawX() - mDownX);

                //获取DragGridView自动向上滚动的偏移量，小于这个值，DragGridView向下滚动
                mDownScrollBorder = getHeight() /4;
                //获取DragGridView自动向下滚动的偏移量，大于这个值，DragGridView向上滚动
                mUpScrollBorder = getHeight() * 3/4;

                //开启mDragItemView绘图缓存
                mStartDragItemView.setDrawingCacheEnabled(true);
                //获取mDragItemView在缓存中的Bitmap对象
                mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
                //这一步很关键，释放绘图缓存，避免出现重复的镜像
                mStartDragItemView.destroyDrawingCache();
                break;
            case MotionEvent.ACTION_MOVE:
//                Log.e("GridView","dispatchTouchEvent ACTION_MOVE");
                int moveX = (int)ev.getX();
                int moveY = (int) ev.getY();

                //如果我们在按下的item上面移动，只要不超过item的边界我们就不移除mRunnable
                if(!isTouchInItem(mStartDragItemView, moveX, moveY)){
                    mHandler.removeCallbacks(mLongClickRunnable);
                }
                break;
            case MotionEvent.ACTION_UP:
//                Log.e("GridView","dispatchTouchEvent ACTION_UP");
                mHandler.removeCallbacks(mLongClickRunnable);
                mHandler.removeCallbacks(mScrollRunnable);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    //用来处理是否为长按的Runnable
    private Runnable mLongClickRunnable = new Runnable() {

        @Override
        public void run() {
            isDrag = true; //设置可以拖拽
            mStartDragItemView.setVisibility(View.INVISIBLE);//隐藏该item
            //根据我们按下的点显示item镜像
            createDragImage(mDragBitmap, mDownX, mDownY);
        }
    };


    /**
     * 是否点击在GridView的item上面
     */
    private boolean isTouchInItem(View dragView, int x, int y){
        if(dragView == null){
            return false;
        }
        int leftOffset = dragView.getLeft();
        int topOffset = dragView.getTop();
        if(x < leftOffset || x > leftOffset + dragView.getWidth()){
            return false;
        }

        if(y < topOffset || y > topOffset + dragView.getHeight()){
            return false;
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        Log.e("执行循序", "2" );
        if(isDrag && mDragImageView != null){
            switch(ev.getAction()){
                case  MotionEvent.ACTION_DOWN:
                    actionDownY=ev.getRawY();
                    actionDownTime=System.currentTimeMillis();
                    break;

                case MotionEvent.ACTION_MOVE:
//                    Log.e("GridView","onTouchEvent ACTION_MOVE");
                    moveX = (int) ev.getX();
                    moveY = (int) ev.getY();
                    //拖动item
                    onDragItem(moveX, moveY);
                    break;
                case MotionEvent.ACTION_UP:
                    actionUpY=ev.getRawY();
                    actionUpTime=System.currentTimeMillis();
//                    Log.e("GridView","onTouchEvent ACTION_UP");
                     onStopDrag();
                    break;
            }
            return true;
        }else{
            if(runOnTouchEvent!=null)runOnTouchEvent.runOnTouch(ev);
//            Log.e("执行循序", "actionUpTime"+actionUpTime+"-actionDownTime"+actionDownTime+"="+(actionUpTime-actionDownTime)+"actionUpY"+actionUpY+"-"+"actionDownTime"+actionDownY+"="+(actionUpY-actionDownY));
        }
        return super.onTouchEvent(ev);
    }

    /**提供给其它页面事件*/
    public interface OnFlashScrollListner {
        void flashScroll( );//其它页面发出的事件
    }
    private OnFlashScrollListner onFlashScrollListner;
    public void setOnFlashScrollListner(OnFlashScrollListner onFlashScrollListner){
        onFlashScrollListner = onFlashScrollListner;
    }
    /**提供给其它页面事件*/
    public interface RunOnTouchEvent {
        void runOnTouch(MotionEvent event);//其它页面发出的事件
    }
    private RunOnTouchEvent runOnTouchEvent;
    public void setRunOnTouchEvent(RunOnTouchEvent onTouchEvent){
        runOnTouchEvent = onTouchEvent;
    }


    /**
     * 创建拖动的镜像
     * @param bitmap
     * @param downX  按下的点相对父控件的X坐标
     * @param downY 按下的点相对父控件的X坐标
     */
    private void createDragImage(Bitmap bitmap, int downX , int downY){
        removeDragImage();
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; //图片之外的其他地方透明
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
        mWindowLayoutParams.alpha = 0.55f; //透明度
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ;

        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);
        mWindowManager.addView(mDragImageView, mWindowLayoutParams);
    }

    /**
     * 从界面上面移动拖动镜像
     * 移除，如果dismiss
     */
    public void removeDragImage(){
//        Log.e("GridView","removeDragImage");
        if(mDragImageView != null){
            mWindowManager.removeView(mDragImageView);
            mDragImageView = null;
        }
    }

    /**
     * 拖动item，在里面实现了item镜像的位置更新，item的相互交换以及GridView的自行滚动
     */
//     * @param x
//     * @param y
    private void onDragItem(int moveX, int moveY){
//        Log.e("GridView","onDragItem");
        mWindowLayoutParams.x = moveX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = moveY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
        mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); //更新镜像的位置
        itemMove(moveX, moveY);

        //GridView自动滚动
        mHandler.post(mScrollRunnable);
    }
    /**
     * 停止拖拽我们将之前隐藏的item显示出来，并将镜像移除
     */
    private void onStopDrag(){
        isDrag = false;
        clearMoveCache();
        View vDrop = getChildAt(dropPosition - getFirstVisiblePosition());//之前的item显示出来
        View vDrag = getChildAt(dragPosition - getFirstVisiblePosition());//之前的item显示出来
        if(vDrop!=null)vDrop.setVisibility(View.VISIBLE);
        if(vDrag!=null)vDrag.setVisibility(View.VISIBLE);
        if(mStartDragItemView!=null)mStartDragItemView.setVisibility(VISIBLE);
//        ((AdapterGridView)this.getAdapter()).onChange(dragPosition, dragPosition);//这里刷新了页面
        removeDragImage();
//        if(dropPosition == 5)return;
        if(onChangeListener!=null && onChangeListener.isNoMove(dropPosition))return;
        if(onChangeListener!=null)onChangeListener.onChange(dragPosition, dropPosition);
        dragPosition = -1;
        dropPosition = -1;
    }
    public interface OnChangeListener{
        void onChange(int from, int to);
        boolean isNoMove(int pos);
    }
    private OnChangeListener onChangeListener;
    public void setOnChangeListener(OnChangeListener listener){
        this.onChangeListener = listener;
    }


    /**
     * 当moveY的值大于向上滚动的边界值，触发GridView自动向上滚动
     * 当moveY的值小于向下滚动的边界值，触犯GridView自动向下滚动
     * 否则不进行滚动
     */
    private Runnable mScrollRunnable = new Runnable() {

        @Override
        public void run() {
            int scrollY;
            if(moveY > mUpScrollBorder){
                scrollY = speed;
                mHandler.postDelayed(mScrollRunnable, 25);
            }else if(moveY < mDownScrollBorder){
                scrollY = -speed;
                mHandler.postDelayed(mScrollRunnable, 25);
            }else{
                scrollY = 0;
                mHandler.removeCallbacks(mScrollRunnable);
            }

            //当我们的手指到达GridView向上或者向下滚动的偏移量的时候，可能我们手指没有移动，但是DragGridView在自动的滚动
            //所以我们在这里调用下onSwapItem()方法来交换item
            itemMove(moveX, moveY);


            smoothScrollBy(scrollY, 10);
        }
    };
    /**排除5和不可选*/
    //lowPos:1 highPos:4  to lowPos:1 highPos:3
    private void moveForward(int lowPos, int highPos,int durationMS,boolean isRecove){
        Log.e("GridView","Anim moveForward: lowPos:"+lowPos+" highPos:"+highPos);
        for (int i = lowPos; i <= highPos; i++) {
//            if(i == 5)i = 6;
            View view = getChildAt(i);
//            View nextView = getChildAt(i+1 ==5 ? i+2 : i + 1);
            View nextView = getChildAt( i + 1);
            if(view == null || nextView == null)return;
            float xValue = (nextView.getLeft() - view.getLeft()) * 1f / view.getWidth();
            float yValue = (nextView.getTop() - view.getTop()) * 1f / view.getHeight();
            TranslateAnimation translateAnimation;
            if(isRecove) translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -xValue, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -yValue, Animation.RELATIVE_TO_SELF, 0f);
            else translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, xValue, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, yValue);
            translateAnimation.setInterpolator(new LinearInterpolator());
            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(durationMS);
//            if (i == fromPos - 1) translateAnimation.setAnimationListener(animationListener);
            if(isRecove) nextView.startAnimation(translateAnimation);
            else view.startAnimation(translateAnimation);
        }
    }
    //moveBackward: lowPos:1 highPos:4 to lowPos:2 highPos:4
    private void moveBackward(int lowPos, int highPos,int durationMS,boolean isRecove){
        Log.e("GridView","Anim moveBackward: lowPos:"+lowPos+" highPos:"+highPos);
        for (int i = lowPos; i <= highPos; i++) {
//            if(i == 5)i = 6;
            View view = getChildAt(i);
//            View prevView = getChildAt(i-1==5 ? i-2 : i - 1);
            View prevView = getChildAt( i - 1);
            if(view == null || prevView == null)return;
            float xValue = (prevView.getLeft() - view.getLeft()) * 1f / view.getWidth();
            float yValue = (prevView.getTop() - view.getTop()) * 1f / view.getHeight();
            TranslateAnimation translateAnimation;
            if(isRecove) translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -xValue, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -yValue, Animation.RELATIVE_TO_SELF, 0f);
            else translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, xValue, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, yValue);
            translateAnimation.setInterpolator(new LinearInterpolator());
            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(durationMS);
//            if (i == toPos) translateAnimation.setAnimationListener(animationListener);
            if(isRecove) prevView.startAnimation(translateAnimation);
            else view.startAnimation(translateAnimation);
        }
    }
    /**
     * 判断item移动
     * 1.上移moveForward 下移moveBackward
     * 2.移出回退
     */
    private int preDropPosition=AdapterView.INVALID_POSITION;
    private void clearMoveCache(){
        preDropPosition =AdapterView.INVALID_POSITION;
    }
    private void itemMove(int moveX, int moveY){
        //获取我们手指移动到的那个item的position
        dropPosition = pointToPosition(moveX, moveY);
//        if(dropPosition == 5)return;
        if(dropPosition!=-1 && onChangeListener!=null && onChangeListener.isNoMove(dropPosition)){
            Log.e("itemMove","no move: drop:"+dropPosition+ " drag:"+dragPosition);
            return;
        }
        if(preDropPosition == dropPosition)return;//没有改变
        Log.e("GridView", "preDropPosition" +preDropPosition+"dropPosition"+dropPosition+"dragPosition"+dragPosition);
        //如果有执行过交换动画，又移出了位置,就移回去,时间短
        if(dropPosition == AdapterView.INVALID_POSITION && preDropPosition != AdapterView.INVALID_POSITION){
            if(preDropPosition < dragPosition){
                moveBackward(preDropPosition+1,dragPosition,50,true);
            }else if(preDropPosition > dragPosition){
                moveForward(dragPosition,preDropPosition-1,50,true);
            }
            preDropPosition = AdapterView.INVALID_POSITION;
        }else{//是移到了点上,进行交换动画
            preDropPosition = dropPosition;
            if(dropPosition < dragPosition){
                moveForward(dropPosition,dragPosition-1,300,false);
            }else if(dropPosition > dragPosition){
                moveBackward(dragPosition+1,dropPosition,300,false);
            }
        }
    }

    /**
     * 动画监听器
     */
//    Animation.AnimationListener animationListener = new Animation.AnimationListener() {
//        @Override
//        public void onAnimationStart(Animation animation) {
//        }
//        @Override
//        public void onAnimationEnd(Animation animation) {
//            // 在动画完成时将adapter里的数据交换位置
//            removeDragImage();
//            ((AdapterGridView)DragGridView.this.getAdapter()).onChange(dragPosition, dropPosition);
//        }
//        @Override
//        public void onAnimationRepeat(Animation animation) {
//        }
//    };
    /**
     * 获取状态栏的高度
     */
    private static int getStatusHeight(Context context){
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight){
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }


}
