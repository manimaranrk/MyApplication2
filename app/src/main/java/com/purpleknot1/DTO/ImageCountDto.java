package com.purpleknot1.DTO;


import android.app.Application;

import java.util.ArrayList;

public class ImageCountDto  extends Application {

    int imageCount;

    public ArrayList<String> imagePath = new ArrayList<String>();

    public ArrayList<String> getImagePath() {
        return imagePath;
    }

    public void setImagePath(ArrayList<String> imagePath) {
        this.imagePath = imagePath;
    }





    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }
}
