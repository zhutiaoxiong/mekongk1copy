package com.mani.car.mekongk1.ui.personcenter.carmanager.message;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kulala.staticsview.OnClickListenerMy;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.model.ManagerDownloadVoice;
import com.mani.car.mekongk1.model.common.DataVoice;

import java.util.List;

public class MessageAlertLibrayAdapter extends RecyclerView.Adapter<MessageAlertLibrayAdapter.ViewHolder> {
    private List<DataVoice> data;
    public MessageAlertLibrayAdapter.OnItemClickListener itemClickListener;
    private String select= ManagerDownloadVoice.getInstance().getUseVoiceId();

    public MessageAlertLibrayAdapter(List<DataVoice> data) {
        this.data = data;
    }

    public void setOnItemClickListener(MessageAlertLibrayAdapter.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public void setSelect(String select){
        this.select=select;
    }
    public interface OnItemClickListener {
        void onVoiceNameSelect(View view, int position);
        void onVoicePlayClick(View view, int position);
    }

    public void setData( List<DataVoice> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MessageAlertLibrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voice_new_list, parent, false);
        final MessageAlertLibrayAdapter.ViewHolder viewHolder = new MessageAlertLibrayAdapter.ViewHolder(view);
        viewHolder.txtState.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    itemClickListener.onVoiceNameSelect(v, position);
                }
            }
        });
        viewHolder.img_voice_play.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    itemClickListener.onVoicePlayClick(v, position);
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAlertLibrayAdapter.ViewHolder holder, final int position) {
        if(data==null||data.get(position)==null)return;
        holder.txtPeople.setText(data.get(position).name);
        holder.txtSize.setText(data.get(position).size);
        if(select.equals("")||select.equals("1")){
            if(position==0){
                holder.txtState.setText("使用中");
            }else{
                holder.txtState.setText("使用");
            }
        }else{
            if(position!=0){
                holder.txtState.setText("使用中");
            }else{
                holder.txtState.setText("使用");
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txtPeople;
        public ImageView img_voice_play;
        public TextView txtSize;
        public TextView txtState;



        public ViewHolder(View itemView) {
            super(itemView);
            txtPeople = itemView.findViewById(R.id.txt_people);
            txtSize = itemView.findViewById(R.id.txt_size);
            txtState = itemView.findViewById(R.id.txt_state);
            img_voice_play = itemView.findViewById(R.id.img_voice_play);
        }
    }
}
