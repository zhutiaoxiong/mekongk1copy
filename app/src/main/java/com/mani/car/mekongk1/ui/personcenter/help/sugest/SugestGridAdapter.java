package com.mani.car.mekongk1.ui.personcenter.help.sugest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mani.car.mekongk1.R;

import java.io.File;
import java.util.List;

public class SugestGridAdapter extends BaseAdapter {
    private List<String> urlList;
    private Context context;
    private int maxPosition;
    public SugestGridAdapter(List<String> urlList, Context context) {
        this.urlList = urlList;
        this.context = context;
    }

    public void setData(List<String> urlList) {
        this.urlList = urlList;
    }


    @Override
    public int getCount() {
        if(urlList.size()==4){
            maxPosition=4;
            return urlList.size();
        }
        else{
            maxPosition = urlList.size() + 1;
        }
        return maxPosition;
    }

    @Override
    public Object getItem(int position) {
        return urlList == null ? null : urlList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public long getmMaxPosition() {
        return maxPosition;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_app, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.img_add);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(urlList.size()==0||position==urlList.size()){
            holder.iv.setImageResource(R.drawable.person_add);
        }else{
                Glide.with(context)
                        .load(new File(urlList.get(position)))
//////                .placeholder(R.mipmap.default_error)
//////                .error(R.mipmap.default_error)
                .centerCrop()
//////                .crossFade()
//////                .bitmapTransform(new CropCircleTransformation(MainActivity.this))
                        .into(holder.iv);
        }
        return convertView;
    }

   final class ViewHolder {
        ImageView iv;
    }
}
