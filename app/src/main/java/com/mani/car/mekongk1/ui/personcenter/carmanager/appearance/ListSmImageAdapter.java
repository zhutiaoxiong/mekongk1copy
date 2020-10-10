package com.mani.car.mekongk1.ui.personcenter.carmanager.appearance;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kulala.staticsview.image.smart.SmartImageView;
import com.mani.car.mekongk1.R;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerSkins;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.carlist.DataCarSkin;

import java.util.List;

public class ListSmImageAdapter extends RecyclerView.Adapter<ListSmImageAdapter.ViewHolder>{
    private List<DataCarSkin> data;
    public OnItemClickListener itemClickListener;
    private Context context;
    private long checkCarId;
    private int listShowType;

    public ListSmImageAdapter(List<DataCarSkin> data, Context context,long carId,int currentType) {
        this.data = data;
        this.context = context;
        this.checkCarId = carId;
        this.listShowType = currentType;
    }

    public  void setOnItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    public interface OnItemClickListener{
        void onClickPicture(int position,DataCarSkin skin);
    }

    public void setData(List<DataCarSkin> data,int currentType){
        this.data = data;
        this.listShowType = currentType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_skin_image, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if(itemClickListener!=null)itemClickListener.onClickPicture(position,data.get(position));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        View view= holder.itemView;
        //image
        final SmartImageView imageView= (SmartImageView) view.findViewById(R.id.image_view);
        Drawable cacheDrawable = ManagerSkins.getInstance().getPngImage(data.get(position).smallPic);
        if(cacheDrawable!=null){
            imageView.setImageDrawable(cacheDrawable);
        }else{
            imageView.setImageDrawable(new ColorDrawable(0xFFFFFFFF));
        }
        //check
        DataCarInfo carInfo = ManagerCarList.getInstance().getCarByID(checkCarId);
        SmartImageView imageSelect= (SmartImageView) view.findViewById(R.id.image_select);
        if(listShowType == ActivityAppearance.SHOW_TYPE_STICKER){//sticker
            if(carInfo.getCarStickerId() == data.get(position).ide)imageSelect.setVisibility(View.VISIBLE);
            else imageSelect.setVisibility(View.INVISIBLE);
        }else {//skin
            if (carInfo.getCarTypeId() == data.get(position).ide)imageSelect.setVisibility(View.VISIBLE);
            else   imageSelect.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if(data==null)return 0;
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
