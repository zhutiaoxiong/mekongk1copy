package com.mani.car.mekongk1.model.carlist;

public class DataControlButton {
    public String name;//名称
    public int status = 1;//状态	0：关闭，1：开启
    public int ide;//功能开关id

    public DataControlButton() {
    }

    public int isUse;//0：未使用，1：使用，用于取消授权，取消电子围栏等点击替换的功能

    public DataControlButton(String name, int status, int ide, int isUse) {
        this.name = name;
        this.status = status;
        this.ide = ide;
        this.isUse = isUse;
    }

    public DataControlButton(String name, int isShow, int ide){
        this.name = name;
        this.status = isShow;
        this.ide = ide;
    }

    public DataControlButton(String name, int ide) {
        this.name = name;
        this.ide = ide;
    }
}
