package com.mani.car.mekongk1.ui.personcenter.carmanager.manager.carseries;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kulala.staticsview.OnClickListenerMy;
import com.mani.car.mekongk1.R;

public class CarSeiesRecycleViewAdapter extends RecyclerView.Adapter<CarSeiesRecycleViewAdapter.ViewHolder> {
    private String[] data;
    public OnItemClickListener itemClickListener;
    private Context context;

    public CarSeiesRecycleViewAdapter(String[] data, Context context) {
        this.data = data;
        this.context = context;
    }

    public  void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface OnItemClickListener{
        void onClick(View view, int position);
    }

    public void setData(String[] data ){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car_series, parent, false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.txt_left.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null){
                    int position=viewHolder.getAdapterPosition();
                    itemClickListener.onClick(v,position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txt_left.setText(data[position]);
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_left;
        public ViewHolder(View itemView) {
            super(itemView);
            txt_left= itemView.findViewById(R.id.txt_left);
        }
    }
}
