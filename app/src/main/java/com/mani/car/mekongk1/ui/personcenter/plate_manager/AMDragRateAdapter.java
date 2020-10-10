package com.mani.car.mekongk1.ui.personcenter.plate_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mani.car.mekongk1.R;

import java.util.List;

public class AMDragRateAdapter extends BaseAdapter {
    private Context context;
    List<ActivityPlateManager.DataPlateManager> items;//适配器的数据源

    public AMDragRateAdapter(Context context, List<ActivityPlateManager.DataPlateManager> list) {
        this.context = context;
        this.items = list;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    public void remove(int arg0) {//删除指定位置的item
        items.remove(arg0);
        this.notifyDataSetChanged();//不要忘记更改适配器对象的数据源
    }

    public void insert(ActivityPlateManager.DataPlateManager item, int arg0) {
//        在指定位置插入item
        items.add(arg0, item);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ActivityPlateManager.DataPlateManager item = (ActivityPlateManager.DataPlateManager) getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.am_rate_drag_item, null);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.ivDelete = (ImageView) convertView.findViewById(R.id.click_remove);
            viewHolder.ivDragHandle = (ImageView) convertView.findViewById(R.id.drag_handle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (items.get(position).src == 0) {

            viewHolder.ivDelete.setImageResource(R.drawable.close_eyes);
        } else if (items.get(position).src == 1) {
            viewHolder.ivDelete.setImageResource(R.drawable.open_eyes);
        }
        viewHolder.tvTitle.setText(item.title);
        //点眼睛，暂不改变
//        viewHolder.ivDelete.setOnClickListener(new OnClickListenerMy() {
//            @Override
//            public void onClickNoFast(View v) {
//                if (items.get(position).src == 0) {
//                    items.get(position).src = 1;
//                    viewHolder.ivDelete.setImageResource(R.drawable.open_eyes);
//                } else if (items.get(position).src == 1) {
//                    items.get(position).src = 0;
//                    viewHolder.ivDelete.setImageResource(R.drawable.close_eyes);
//                }
//            }
//        });
        return convertView;
    }

    class ViewHolder {
        TextView tvTitle;
        ImageView ivDelete;
        ImageView ivDragHandle;
    }
}
