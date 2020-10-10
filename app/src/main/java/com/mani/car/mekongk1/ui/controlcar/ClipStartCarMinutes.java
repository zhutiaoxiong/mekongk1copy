package com.mani.car.mekongk1.ui.controlcar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.kulala.staticsfunc.dbHelper.ODBHelper;
import com.kulala.staticsfunc.static_view_change.ODipToPx;

/**
 * 启动车的时间选择
 */
public class ClipStartCarMinutes extends android.support.v7.widget.AppCompatImageView {
	public static int selectValue = 10;//不是第几个，是几分种,time显示为0-6档
	private int LINEY = 45;//dp
	private int LINEW;//线长
	private int MARGINX = 10;//dp
	private int NUMBERW = 20;//dp
	private Paint paint_gray;
	private Paint paint_green;
	private float moveX;
	private boolean isTouched = false;
	public ClipStartCarMinutes(Context context, AttributeSet attrs) {
		super(context, attrs);
		paint_gray = new Paint();
		paint_gray.setColor(Color.parseColor("#858585"));
//		paint_gray.setAlpha(170);
		paint_gray.setFlags(Paint.ANTI_ALIAS_FLAG);//消除锯齿
		paint_gray.setStrokeWidth(1.0f);
		paint_green = new Paint();
		paint_green.setColor(Color.parseColor("#90CF26"));
		paint_green.setFlags(Paint.ANTI_ALIAS_FLAG);//消除锯齿
		paint_green.setStrokeWidth(1.0f);
	}
	public static int getLevel(int selectValue){
		int level =  selectValue/5-1;
		return level;
	}
//	public void setChooseNum(int time){selectValue = time;}
//	public int getChooseNum(){
//		return selectValue;
//	}
//	public void setLineColor(String color){
//		paint_gray.setColor(Color.parseColor(color));
//		invalidate();
//	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() ==MotionEvent.ACTION_MOVE){
			moveX= event.getX();
			ODBHelper.getInstance(getContext()).changeCommonInfo("moveX",moveX+"");
			isTouched = true;
			invalidate();
		}
		return true;
	}
	private Point sceneSize;
	private int cutMargin,cutFinelText,dp2px;
	@Override
	protected void onDraw(Canvas canvas) {
		//初始化数值
		if(sceneSize == null){
			sceneSize = new Point(canvas.getWidth(), canvas.getHeight());
			dp2px = ODipToPx.dipToPx(getContext(),1f);
			MARGINX *= dp2px;
			NUMBERW *= dp2px;
			LINEY *= dp2px;
			cutMargin = sceneSize.x - MARGINX*2;
			cutFinelText = cutMargin - NUMBERW;
			LINEW = (cutFinelText - NUMBERW*7)/6;
		}
		//首次显示为10分钟
		if(!isTouched){
			String theMoveX=ODBHelper.getInstance(getContext()).queryCommonInfo("moveX");
			if(theMoveX==null||theMoveX.equals("")){
				moveX = MARGINX+(LINEW+NUMBERW)*selectValue/5;
			}else{
				moveX=Float.parseFloat(theMoveX);
			}
		}
		//设定每段,6格7数
		for(int i=1;i<=7;i++){
			int numX = MARGINX+NUMBERW+(LINEW+NUMBERW)*(i-1);//当前颜色最大点
			Paint usePaint;
			if(numX<=moveX || i==1){//在绿色内的
				selectValue = i*5;
				usePaint = paint_green;//12dp文字大小，绿色
				//划线
				if(i>1)canvas.drawLine(numX-(LINEW+NUMBERW), LINEY, numX-NUMBERW, LINEY, usePaint);
			}else{
				usePaint = paint_gray;//12dp文字大小，灰色
				//划线
				canvas.drawLine(numX-(LINEW+NUMBERW), LINEY, numX-NUMBERW, LINEY, usePaint);
			}
			//写数字
			usePaint.setTextSize(12.0f*dp2px);
			canvas.drawText(""+(i*5), numX-NUMBERW+2*dp2px, LINEY+4*dp2px, usePaint);
		}
		int lastNumX = sceneSize.x-MARGINX-NUMBERW;
		if(moveX > lastNumX) {
			canvas.drawText("分", sceneSize.x - MARGINX  - NUMBERW+2*dp2px , LINEY+4*dp2px, paint_green);
		}else{
			canvas.drawText("分", sceneSize.x - MARGINX  - NUMBERW+2*dp2px , LINEY+4*dp2px, paint_gray);
		}
		super.onDraw(canvas);
	}
}
