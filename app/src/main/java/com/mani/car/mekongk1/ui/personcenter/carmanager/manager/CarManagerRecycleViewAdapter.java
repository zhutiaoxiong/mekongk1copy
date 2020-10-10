package com.mani.car.mekongk1.ui.personcenter.carmanager.manager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.mani.car.mekongk1.model.carlist.DataCarInfo;

import java.util.List;
import java.util.Map;

public class CarManagerRecycleViewAdapter extends RecyclerView.Adapter<CarManagerRecycleViewAdapter.ViewHolder>{
    private List<DataCarInfo> data;
    public OnItemClickListener itemClickListener;
    private Context context;
    private Map<Integer,Boolean> map;

    public CarManagerRecycleViewAdapter(List<DataCarInfo> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public  void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface OnItemClickListener{
        void onWechat(View view, int position);
        void onPhone(View view, int position);
    }

    public void setData(List<DataCarInfo> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CarmanagerItem carmanagerItem=new CarmanagerItem(context,null);
        final ViewHolder viewHolder=new ViewHolder(carmanagerItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarmanagerItem itemView= (CarmanagerItem) holder.itemView;
        itemView.setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
