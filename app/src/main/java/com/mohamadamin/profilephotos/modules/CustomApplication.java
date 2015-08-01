package com.mohamadamin.profilephotos.modules;

import android.graphics.Bitmap;

public class CustomApplication {

    public String titleText, descriptionText, packageName, imageLink;
    public Bitmap iconBitmap;

    public CustomApplication(String titleText, String descriptionText,
                             String packageName, String imageLink) {
        super();
        this.titleText = titleText;
        this.imageLink = imageLink;
        this.packageName = packageName;
        this.descriptionText = descriptionText;
    }

}
