package com.mani.car.mekongk1.ui.controlcar.trajectory;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kulala.staticsfunc.static_system.ODateTime;
import com.kulala.staticsview.OnClickListenerMy;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.model.gps.DataGpsPath;

import java.text.DecimalFormat;
import java.util.List;

public class TrajectoryRecycleViewAdapter extends RecyclerView.Adapter<TrajectoryRecycleViewAdapter.ViewHolder> {
    private List<DataGpsPath> data;
    public OnItemClickListener itemClickListener;

    public TrajectoryRecycleViewAdapter(List<DataGpsPath> data, OnItemClickListener itemClickListener) {
        this.data = data;
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void onPathSelect(View view, int position);
    }

    public void setData(List<DataGpsPath> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trajectory, parent, false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.re.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null){
                    int position=viewHolder.getAdapterPosition();
                    itemClickListener.onPathSelect(v,position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(data==null||data.size()==0)return;
        DataGpsPath info = data.get(position);
        if(info==null)return;
        holder.txt_start_place.setText(ODateTime.time2StringHHmm(info.endTime)+"| " + info.endLocation);
        holder.txt_end_place.setText(ODateTime.time2StringHHmm(info.startTime)+"| " + info.startLocation);
        DecimalFormat df = new DecimalFormat("##0.00");
        holder.txt_mileage.setText("里程:" + df.format(info.miles) + "公里");
        holder.txt_time.setText("时长:" + info.intervalTime);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_start_place;
        public TextView txt_end_place;
        public TextView txt_mileage;
        public TextView txt_time;
        public RelativeLayout re;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_start_place= itemView.findViewById(R.id.txt_start_place);
            txt_end_place= itemView.findViewById(R.id.txt_end_place);
            txt_mileage= itemView.findViewById(R.id.txt_mileage);
            txt_time= itemView.findViewById(R.id.txt_time);
            re= itemView.findViewById(R.id.re);
        }
    }
}
