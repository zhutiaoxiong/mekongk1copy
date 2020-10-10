package com.mani.car.mekongk1.ui.personcenter.help.service;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.kulala.staticsview.OnClickListenerMy;
import com.kulala.staticsview.image.CircleImg;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.model.loginreg.DataUser;

import java.util.List;

public class PersonServiceRecycleViewAdapter extends RecyclerView.Adapter<PersonServiceRecycleViewAdapter.ViewHolder> {
    private List<DataUser> data;
    public OnItemClickListener itemClickListener;
    private Context context;

    public PersonServiceRecycleViewAdapter(List<DataUser> data, Context context) {
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

    public void setData(List<DataUser> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_sevice_list, parent, false);
        final ViewHolder viewHolder=new ViewHolder(view);
        viewHolder.wechat.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null){
                    int position=viewHolder.getAdapterPosition();
                    itemClickListener.onWechat(v,position);
                }
            }
        });
        viewHolder.phone.setOnClickListener(new OnClickListenerMy(){
            @Override
            public void onClickNoFast(View v) {
                if (itemClickListener != null){
                    int position=viewHolder.getAdapterPosition();
                    itemClickListener.onPhone(v,position);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        holder.username.setText(data.get(position).phoneNum);
        String url= data.get(position).avatarUrl;
        if(url==null||url.equals(""))return;
        Glide.with(context).load(url)
                .placeholder(R.drawable.headpic_nomal)
                .into(new SimpleTarget<GlideDrawable>() {
                    @Override
                    public void onResourceReady(GlideDrawable resource,
                                                GlideAnimation<? super GlideDrawable> glideAnimation) {
                        holder.headpic.setImageDrawable(resource);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CircleImg headpic;
        public ImageView wechat ;
        public TextView username;
        public ImageView phone;
        public ViewHolder(View itemView) {
            super(itemView);
            phone= itemView.findViewById(R.id.img_phone);
            wechat= itemView.findViewById(R.id.img_wechat);
           username= itemView.findViewById(R.id.txt_left);
            headpic= itemView.findViewById(R.id.img_left);
        }
    }
}
