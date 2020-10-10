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
import com.mani.car.mekongk1.model.pushmessage.AlertInfos;

import java.util.List;

public class MessageSwitchRecycleViewAdapter extends RecyclerView.Adapter<MessageSwitchRecycleViewAdapter.ViewHolder> {
    private List<AlertInfos> data;
    public OnItemClickListener itemClickListener;

    public MessageSwitchRecycleViewAdapter(List<AlertInfos> data) {
        this.data = data;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onMessageButtonStusChange(View view, int position);
    }

    public void setData(List<AlertInfos> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.message_set_item,null);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.messageIsOpen.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    itemClickListener.onMessageButtonStusChange(v, position);
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.messageName.setText(data.get(position).name);
        if(data.get(position).isOpen==0){
            holder.messageIsOpen.setImageResource(R.drawable.gesture_close);
        }else  if(data.get(position).isOpen==1){
            holder.messageIsOpen.setImageResource(R.drawable.gesture_open);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView messageName;
        public ImageView messageIsOpen;

        public ViewHolder(View itemView) {
            super(itemView);
            messageName=itemView.findViewById(R.id.txt_left);
            messageIsOpen=itemView.findViewById(R.id.right_img);
        }
    }
}
