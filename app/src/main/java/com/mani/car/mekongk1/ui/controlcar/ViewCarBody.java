package com.mani.car.mekongk1.ui.controlcar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.util.Log;
import com.mani.car.mekongk1.common.GlobalContext;
import com.mani.car.mekongk1.model.ManagerCarList;
import com.mani.car.mekongk1.model.ManagerSkins;
import com.mani.car.mekongk1.model.carlist.DataCarInfo;
import com.mani.car.mekongk1.model.carlist.DataCarStatus;

/**
 * 车身
 */
public class ViewCarBody extends android.support.v7.widget.AppCompatImageView {
    private Drawable stickerImage;
    private DataCarInfo carInfoUse;
    private static String needReLoadName = "car_lightfind";
    public ViewCarBody(Context context, AttributeSet attrs) {
        super(context, attrs);// this layout for add and edit
    }
    //修改车状态
    public void changeData(){
        carInfoUse = ManagerCarList.getInstance().getCurrentCar();
        if(needReLoadName.length()>0) {
            ManagerSkins.getInstance().loadSkinByIdReal(getContext(), carInfoUse.getCarTypeId(), carInfoUse.carType,needReLoadName, new ManagerSkins.OnLoadPngListener() {
                @Override
                public void loadCompleted(Drawable drawable) {
                    setImage();
                }
                @Override
                public void loadFail(String error) {
                    Log.e("ViewCarBody",error);
                }
            });
            needReLoadName = "";
        }
        ManagerSkins.getInstance().loadStickerById(getContext(), carInfoUse.getCarStickerId(), carInfoUse.carType, new ManagerSkins.OnLoadPngListener(){
            @Override
            public void loadCompleted(Drawable image) {
                stickerImage = image;
            }
            @Override
            public void loadFail(String error) {
                stickerImage = null;
            }
        });
        setImage();
    }
    private Drawable getDrawableFromName(String name,String dir){
        Drawable cache;
        if(name.equals("TRANSPARENT")){
            cache = new PaintDrawable(Color.TRANSPARENT);
        }else {
            cache = ManagerSkins.getInstance().getPngImage(dir+"/"+name);
        }
        if(cache == null)needReLoadName = name;
        return cache;
    }
    private void setImage(){
        if(GlobalContext.getCurrentActivity() == null)return;//重开无UI
        GlobalContext.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DataCarStatus carStatus = ManagerCarList.getInstance().getCurrentCarStatus();
                String carSkinDir = ManagerSkins.getInstance().getSkinZipFolder(getContext())+"/"+ManagerSkins.getInstance().getSkinZipFileName(carInfoUse.getCarTypeId(), carInfoUse.carType)+ ".zip";
                if (carInfoUse == null || carStatus == null) return;
                //light:1+body1+door4+sticker1+backpag1+skylight1
                Drawable[]   array = new Drawable[9];
                //car_lightback
                if (carStatus.lightOpen == 1) {
                    array[0] = getDrawableFromName("car_lightback",carSkinDir);
                } else {
                    array[0] = getDrawableFromName("TRANSPARENT",carSkinDir);
                }
                //body
                array[1] = getDrawableFromName("car_body",carSkinDir);
                //car_lt左上门
                array[2] = getDrawableFromName("car_lt_door"
                        + DataCarStatus.getOpenCloseChar(carStatus.leftFrontOpen)
                        + "_win"
                        + DataCarStatus.getOpenCloseChar(carStatus.leftFrontWindowOpen),carSkinDir);
                //car_lt左下门
                array[3] = getDrawableFromName("car_ld_door"
                        + DataCarStatus.getOpenCloseChar(carStatus.leftBehindOpen)
                        + "_win"
                        + DataCarStatus.getOpenCloseChar(carStatus.leftBehindWindowOpen),carSkinDir);
                //car_lt右上门
                array[4] = getDrawableFromName("car_rt_door"
                        + DataCarStatus.getOpenCloseChar(carStatus.rightFrontOpen)
                        + "_win"
                        + DataCarStatus.getOpenCloseChar(carStatus.rightFrontWindowOpen),carSkinDir);
                //car_lt右下门
                array[5] = getDrawableFromName("car_rd_door"
                        + DataCarStatus.getOpenCloseChar(carStatus.rightBehindOpen)
                        + "_win"
                        + DataCarStatus.getOpenCloseChar(carStatus.rightBehindWindowOpen),carSkinDir);
                //sticker
                if (stickerImage == null) {
                    array[6] = getDrawableFromName("TRANSPARENT","");
                } else {
                    array[6] = stickerImage;
                }
                //backpag
                if (carStatus.afterBehindOpen == 1) {
                    array[7] = getDrawableFromName("car_backpag",carSkinDir);
                } else {
                    array[7] = getDrawableFromName("TRANSPARENT",carSkinDir);
                }
                //skylight
                if (carStatus.skyWindowOpen == 1) {
                    array[8] = getDrawableFromName("car_skylight",carSkinDir);
                } else {
                    array[8] = getDrawableFromName("TRANSPARENT",carSkinDir);
                }
                for(Drawable drawable : array){
                    if(drawable == null)return;
                }
                //再加入层中
                LayerDrawable ldraw = new LayerDrawable(array); //参数为上面的Drawable数组
                ldraw.setLayerInset(0, 0, 0, 0, 0);
                ldraw.setLayerInset(1, 0, 0, 0, 0);
                ldraw.setLayerInset(2, 0, 0, 0, 0);
                ldraw.setLayerInset(3, 0, 0, 0, 0);
                ldraw.setLayerInset(4, 0, 0, 0, 0);
                ldraw.setLayerInset(5, 0, 0, 0, 0);
                ldraw.setLayerInset(6, 0, 0, 0, 0);
                ldraw.setLayerInset(7, 0, 0, 0, 0);
                ldraw.setLayerInset(8, 0, 0, 0, 0);
                //设值
                ViewCarBody.this.setImageDrawable(ldraw);
                if(onImageLoadCompletedListener!=null && array[1] instanceof BitmapDrawable){
                    BitmapDrawable draw = (BitmapDrawable)array[1];
                    onImageLoadCompletedListener.OnImageLoadCompleted(draw.getBitmap().getWidth(),draw.getBitmap().getHeight());
                }
            }
        });
    }
    /**用于解决部分手机车身变小问题**/
    public interface OnImageLoadCompleted{
        public abstract void OnImageLoadCompleted(int bodyW,int bodyH);
    }
    private OnImageLoadCompleted onImageLoadCompletedListener;
    public void setOnImageLoadCompleted(OnImageLoadCompleted listener){
        onImageLoadCompletedListener = listener;
    }
    /**用于解决部分手机车身变小问题 end**/
}
