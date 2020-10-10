package com.mani.car.mekongk1.ui.personcenter.carmanager.manager.carbrand.brand;

public class SortModel {

    private String name;
    private String letters;//显示拼音的首字母


    private String imageUrl;//图片url

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {

        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetters() {
        return letters;
    }

    public void setLetters(String letters) {
        this.letters = letters;
    }
}
