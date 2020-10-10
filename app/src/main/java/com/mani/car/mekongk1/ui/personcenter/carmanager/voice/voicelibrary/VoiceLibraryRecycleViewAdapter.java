package com.mani.car.mekongk1.ui.personcenter.carmanager.voice.voicelibrary;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kulala.staticsview.OnClickListenerMy;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.ui.personcenter.carmanager.voice.MyVoiceSelect;

import java.util.List;

public class VoiceLibraryRecycleViewAdapter extends RecyclerView.Adapter<VoiceLibraryRecycleViewAdapter.ViewHolder> {
    private List<MyVoice> data;
    public OnItemClickListener itemClickListener;
    private int clickPosition;

    public VoiceLibraryRecycleViewAdapter(List<MyVoice> data,String voiceType) {
        this.data = data;
        switch (voiceType){
            case "启动":
               String startVoice= MyVoiceSelect.getInstance().getStartVoice();
              int position1=   getDeaufaltPosition(startVoice,data);
              clickPosition=position1;
                break;
            case "上落锁":
                String lockUpVoice= MyVoiceSelect.getInstance().getLockUpVoice();
               int position2= getDeaufaltPosition(lockUpVoice,data);
                clickPosition=position2;
                break;
            case "警报":
                String alertVoice= MyVoiceSelect.getInstance().getAlertVoice();
                int position3=  getDeaufaltPosition(alertVoice,data);
                clickPosition=position3;
                break;
            case "点击车音效":
                String clickCarVoice= MyVoiceSelect.getInstance().getClickCarVoice();
                int position4=  getDeaufaltPosition(clickCarVoice,data);
                clickPosition=position4;
                break;
            case "滑动按钮音效":
                String scrollButtonVoice= MyVoiceSelect.getInstance().getScrollButtonVoice();
                int position5=  getDeaufaltPosition(scrollButtonVoice,data);
                clickPosition=position5;
                break;
        }
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onVoiceNameSelect(View view, int  position);
    }

    public void setData(List<MyVoice> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voice_library_list, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.voiceName.setOnClickListener(new OnClickListenerMy() {
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    clickPosition=position;
                    itemClickListener.onVoiceNameSelect(v, position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.voiceName.setText(data.get(position).voiceName);
        if(clickPosition==position){
            holder.img_gou.setVisibility(View.VISIBLE);
        }else{
            holder.img_gou.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView voiceName;
        public ImageView img_gou;

        public ViewHolder(View itemView) {
            super(itemView);
            voiceName = itemView.findViewById(R.id.txt_left);
            img_gou = itemView.findViewById(R.id.img_gou);
        }
    }
    public int getDeaufaltPosition(String voiceName,List<MyVoice> data){
        int position=0;
       for (int i=0;i<data.size();i++){
           if(voiceName.equals(data.get(i).voiceName)){
               position=i;
           }
       }
       return position;
    }
}
