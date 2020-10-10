package com.mani.car.mekongk1.ui.controlcar;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.kulala.staticsfunc.static_assistant.ArrayHelper;
import com.kulala.staticsview.image.smart.SmartImageView;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.carlist.DataCarStatus;

import java.util.List;

public class AdapterSideState extends RecyclerView.Adapter<AdapterSideState.ViewHolder>{
    private List<String> data;
    public OnItemClickListener itemClickListener;
    private Context context;
    private Animation anmiRotate;

    public AdapterSideState(List<String> data, Context context) {
        this.data = data;
        this.context = context;
        anmiRotate = AnimationUtils.loadAnimation(context, R.anim.rotate_animation);
        Log.i("动画", "创建一个动画对象");
        LinearInterpolator lir = new LinearInterpolator();
        anmiRotate.setInterpolator(lir);//必设不然无法均速
        anmiRotate.setFillAfter(true);
        anmiRotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }
    private boolean currentFanOn = true;
    public void superOnResume(){
        currentFanOn = true;
        GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if(anmiRotate!=null)anmiRotate.start();
            }
        });
    }
    public void superOnStop(){
        currentFanOn = false;
        GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                if(anmiRotate!=null)anmiRotate.cancel();
            }
        });
    }

    public  void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface OnItemClickListener{
        void onClickPicture(int position, String name);
    }

    private int preVoltage = 0;//前次电压,有变动就要刷新
    public void setData(List<String> data1){
        final int result = ArrayHelper.checkListStrDiff(data1,data,"锁");
        //-1 相同的数据 -2 size同值不同 0-999新增的点 1000-1999 减少的点
        //不能直接换数据，有可能同时改变二个以上的值
        DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
        DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
        if(carInfo.isActive != 1)carStatus.voltage = 11;//演示,没激活的车,假车，直接显示电压
        int cacheVoltage = (int)(carStatus.voltage);
        if(result == -2){
            this.data = data1;
            GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }else if(result>=0 && result<1000){
            this.data.add(result,data1.get(result));
            GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyItemInserted(result);
                }
            });
        }else if(result>=1000 && result<2000){
            this.data.remove(result-1000);
            GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyItemRemoved(result-1000);
                }
            });
        }else if(cacheVoltage!=preVoltage){//电压值有变动
            Log.e("SideState","电压变动："+cacheVoltage);
            this.data = data1;
            GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });
        }
    }
//    public List<String> getData(){
//        return this.data;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_side_state, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if(itemClickListener!=null)itemClickListener.onClickPicture(position,data.get(position));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
        View view= holder.itemView;
        //image "警告", "无连接", "警戒中", "开锁", "启动"
        SmartImageView imageView= (SmartImageView) view.findViewById(R.id.image_view);
        TextView txt_face= (TextView) view.findViewById(R.id.txt_face);
        imageView.clearAnimation();
        txt_face.setVisibility(View.INVISIBLE);
        String name = data.get(position);
        if(name.equals(ViewSideState.NAMES[0])) {
            imageView.setImageResource(R.drawable.state_warning);
        }else if(name.equals(ViewSideState.NAMES[1])) {
            imageView.setImageResource(R.drawable.state_connfail);
        }else if(name.equals(ViewSideState.NAMES[2])) {
            imageView.setImageResource(R.drawable.state_shelling);
        }else if(name.equals("开锁")) {
            imageView.setImageResource(R.drawable.state_unlock);
        }else if(name.equals("关锁")) {
            imageView.setImageResource(R.drawable.state_lock);
        }else if(name.equals(ViewSideState.NAMES[4])) {//电压
            imageView.setImageResource(R.drawable.state_voltage);
            txt_face.setVisibility(View.VISIBLE);
            DataCarInfo carInfo = ManagerCarList.getInstance().getCurrentCar();
            if(carInfo.isActive != 1) {//演示,没激活的车,假车，直接显示电压
                carStatus.voltage = 11;
            }
            preVoltage = (int)(carStatus.voltage);
            txt_face.setText(""+preVoltage);
            if(preVoltage>=11){
                txt_face.setTextColor(Color.parseColor("#000000"));//正常电压白色
            }else{
                txt_face.setTextColor(Color.parseColor("#FF0000"));//低电压红色
            }
//            if(carInfo.status.voltageStatus == 2)txt_face.setTextColor(Color.parseColor("#FF0000"));//低电压红色
//            else txt_face.setTextColor(Color.parseColor("#FFFFFF"));//正常电压白色
        }else if(name.equals(ViewSideState.NAMES[5])) {
            imageView.setImageResource(R.drawable.state_running);
//            if(currentFanOn)
            imageView.startAnimation(anmiRotate);
        }else if(name.equals(ViewSideState.NAMES[6])) {//电源
            imageView.setImageResource(R.drawable.state_on);
        }else if(name.equals(ViewSideState.NAMES[7])) {
            imageView.setImageResource(R.drawable.state_blue);
        }
    }

    @Override
    public int getItemCount() {
        if(data==null)return 0;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
