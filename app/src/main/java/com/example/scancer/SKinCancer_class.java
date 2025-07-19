package com.example.scancer;

import android.graphics.Bitmap;

public class SKinCancer_class {
    String image;
    String label;

    public SKinCancer_class(String image, String label, String id_user) {
        this.image = image;
        this.label = label;
        this.id_user = id_user;
    }

    String id_user;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }



}
