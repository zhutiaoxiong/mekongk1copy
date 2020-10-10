package com.mani.car.mekongk1.ui.personcenter.carmanager.voice;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kulala.staticsview.OnClickListenerMy;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.model.ManagerVoiceSet;

public class VoiceSetRecycleViewAdapter extends RecyclerView.Adapter<VoiceSetRecycleViewAdapter.ViewHolder> {
    private  String[]  data;
    public OnItemClickListener itemClickListener;

    public VoiceSetRecycleViewAdapter(String[] data) {
        this.data = data;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onVoiceNameSelect(View view, int position);

        void onVoicePlayClick(View view, int position);

        void onVoiceHornClick(View view, int position);

        void onVoiceSelectClick(View view, int position);

        void onVoiceDefaultClick(View view, int position);
    }

    public void setData(String[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voiceset_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.voiceName.setOnClickListener(new OnClickListenerMy() {
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
        viewHolder.img_voice_horn.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    itemClickListener.onVoiceHornClick(v, position);
                }
            }
        });
        viewHolder.img_voice_select.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    itemClickListener.onVoiceSelectClick(v, position);
                }
            }
        });
        viewHolder.img_default.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    itemClickListener.onVoiceDefaultClick(v, position);
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.voiceName.setText(data[position]);

        switch (position){
            case 0:
              int isOpenStartVoice= ManagerVoiceSet.getInstance().getIsOpenStartVoice();
              if(isOpenStartVoice==-1){
                  holder.img_voice_horn.setImageResource(R.drawable.voice_horn);
              }else{
                  holder.img_voice_horn.setImageResource(R.drawable.voice_silent);
              }
                Log.e("aaaaaa", "position: "+position+"isOpenStartVoice"+isOpenStartVoice );
                break;
            case 1:
                int isOpenLockUpVoice=ManagerVoiceSet.getInstance().getIsOpenLockUpVoice();
                if(isOpenLockUpVoice==-1){
                    holder.img_voice_horn.setImageResource(R.drawable.voice_horn);
                }else{
                    holder.img_voice_horn.setImageResource(R.drawable.voice_silent);
                }
                Log.e("aaaaaa", "position: "+position+"isOpenLockUpVoice"+isOpenLockUpVoice );
                  break;
            case 2:
                int isOpenAlertVoice=  ManagerVoiceSet.getInstance().getIsOpenAlertVoice();
                if(isOpenAlertVoice==-1){
                    holder.img_voice_horn.setImageResource(R.drawable.voice_horn);
                }else{
                    holder.img_voice_horn.setImageResource(R.drawable.voice_silent);
                }
                break;
            case 3:
                int isOpenClickCarVoice=  ManagerVoiceSet.getInstance().getIsOpenClickCarVoice();
                if(isOpenClickCarVoice==-1){
                    holder.img_voice_horn.setImageResource(R.drawable.voice_horn);
                }else{
                    holder.img_voice_horn.setImageResource(R.drawable.voice_silent);
                }
                break;
            case 4:
                int isOpenScrollButtonVoice=  ManagerVoiceSet.getInstance().getIsOpenScrollButtonVoice();
                if(isOpenScrollButtonVoice==-1){
                    holder.img_voice_horn.setImageResource(R.drawable.voice_horn);
                }else{
                    holder.img_voice_horn.setImageResource(R.drawable.voice_silent);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView voiceName;
        public ImageView img_voice_play;
        public ImageView img_voice_horn;
        public ImageView img_voice_select;
        public ImageView img_default;

        public ViewHolder(View itemView) {
            super(itemView);
            voiceName = itemView.findViewById(R.id.txt_left);
            img_voice_play = itemView.findViewById(R.id.img_voice_play);
            img_voice_horn = itemView.findViewById(R.id.img_voice_horn);
            img_voice_select = itemView.findViewById(R.id.img_voice_select);
            img_default = itemView.findViewById(R.id.img_default);
        }
    }
}
