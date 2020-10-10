package com.mani.car.mekongk1.ui.personcenter.carmanager.carblue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mani.car.mekongk1.R;
import com.kulala.staticsview.OnClickListenerMy;

import java.util.List;

public class BlueRecycleViewAdapter extends RecyclerView.Adapter<BlueRecycleViewAdapter.ViewHolder> {
    private List<String> data;
    public OnItemClickListener itemClickListener;
    private Context context;

    public BlueRecycleViewAdapter(List<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onBlueNameSelect(View view, int position);
    }

    public void setData(List<String> data) {
        this.data = data;
    }
    public String getData(int pos){
        if(data == null || pos >= data.size())return "";
        return data.get(pos);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blue_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.blueName.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    itemClickListener.onBlueNameSelect(v, position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.blueName.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        if(data == null)return 0;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView blueName;

        public ViewHolder(View itemView) {
            super(itemView);
            blueName = itemView.findViewById(R.id.txt_left);
        }
    }
}
