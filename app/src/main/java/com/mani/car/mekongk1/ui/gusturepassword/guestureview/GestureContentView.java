package com.mani.car.mekongk1.ui.gusturepassword.guestureview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kulala.staticsfunc.static_system.AppUtil;
import com.kulala.staticsfunc.static_view_change.ODipToPx;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势密码容器
 */
	public class GestureContentView extends ViewGroup {
		private int baseNum = 4;
		private int[] screenDispaly;
		private int                blockWidth;//每个点区域的宽度
		private List<GesturePoint> list;//坐标集合
		private Context context;
		private boolean            isVerify;
		private GestureDrawline    gestureDrawline;
	    private int dp84=ODipToPx.dipToPx(GlobalContext.getContext(),60);
	    private int dp75=ODipToPx.dipToPx(GlobalContext.getContext(),58);
		private int dp14=ODipToPx.dipToPx(GlobalContext.getContext(),40);
		private int dp98=dp84+dp14;
		/**
		 * 包含9个ImageView的容器，初始化
		 * @param context
		 * @param isVerify 是否为校验手势密码
		 * @param passWord 用户传入密码
		 * @param callBack 手势绘制完毕的回调
		 */
		public GestureContentView(Context context, boolean isVerify, String passWord, GestureDrawline.GestureCallBack callBack) {
			super(context);
			screenDispaly = AppUtil.getScreenDispaly(context);
//			blockWidth = screenDispaly[0]/3;
			this.list = new ArrayList<GesturePoint>();
			this.context = context;
			this.isVerify = isVerify;
			// 添加9个图标
			addChild();
			// 初始化一个可以画线的view
			gestureDrawline = new GestureDrawline(context, list, isVerify, passWord, callBack);
		}

		private void addChild(){

			for (int i = 0; i < 9; i++) {
				ImageView image = new ImageView(context);
				image.setBackgroundResource(R.drawable.gesture_node_normal);
//				LayoutParams params = new LayoutParams(blockWidth/3,blockWidth/3);
				LayoutParams params = new LayoutParams(dp14,dp14);
//				int padding=ODipToPx.dipToPx(GlobalContext.getContext(),6);
//				image.setPadding(padding,padding,padding,padding);
				image.setLayoutParams(params);
				this.addView(image);
				invalidate();
				// 09-19 14:52:58.434 21033-21033/com.mani.car.mekongk1 E/look: row=0col=0
				//    row=0col=1
				//    row=0col=2
				//09-19 14:52:58.435 21033-21033/com.mani.car.mekongk1 E/look: row=1col=0
				//    row=1col=1
				//    row=1col=2
				//09-19 14:52:58.436 21033-21033/com.mani.car.mekongk1 E/look: row=2col=0
				//    row=2col=1
				//    row=2col=2
				// 第几行
				int row = i / 3;
				// 第几列
				int col = i % 3;
//                Log.e("look", "row=" +row+"col="+col);
				// 定义点的每个属性
//				int leftX = col*blockWidth+blockWidth/baseNum;
                int leftX = col*dp98+dp75;
//				int topY = row*blockWidth+blockWidth/baseNum;
                int topY = row*dp98;
//				int rightX = col*blockWidth+blockWidth-blockWidth/baseNum;
				int rightX = col*dp98+dp75+dp14;;
//				int bottomY = row*blockWidth+blockWidth-blockWidth/baseNum;
				int bottomY = row*dp98+dp14;
				GesturePoint p = new GesturePoint(leftX, rightX, topY, bottomY, image,i+1);
				this.list.add(p);
			}
		}

		public void setParentView(ViewGroup parent){
			// 得到屏幕的宽度
			int width = screenDispaly[0];
			LayoutParams layoutParams = new LayoutParams(width, width);
			this.setLayoutParams(layoutParams);
			gestureDrawline.setLayoutParams(layoutParams);
			parent.addView(gestureDrawline);
			parent.addView(this);
		}

		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			for (int i = 0; i < getChildCount(); i++) {
				//第几行
				int row = i/3;
				//第几列
				int  col = i%3;
				View v   = getChildAt(i);
//				v.layout(col*blockWidth+blockWidth/baseNum, row*blockWidth+blockWidth/baseNum,
//						col*blockWidth+blockWidth-blockWidth/baseNum, row*blockWidth+blockWidth-blockWidth/baseNum);
				v.layout(col*dp98+dp75, row*dp98,
						col*dp98+dp75+dp14, row*dp98+dp14);
			}
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			// 遍历设置每个子view的大小
			for (int i = 0; i < getChildCount(); i++) {
				View v = getChildAt(i);
				v.measure(widthMeasureSpec, heightMeasureSpec);
			}
		}

		/**
		 * 保留路径delayTime时间长
		 * @param delayTime
		 */
		public void clearDrawlineState(long delayTime) {
			gestureDrawline.clearDrawlineState(delayTime);
		}

	}
