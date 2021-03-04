package edu.ranken.prsmith.droidcafe.model;

import androidx.annotation.DrawableRes;

import java.io.Serializable;

public class Product implements Serializable {
    private String id;
    private String name;
    private String description;
    private float price;
    private int imageResId;

    // private int imageResId; bundled with app
    // private String imageUrl; stored online
    // private Drawable imageDrawable; provided by user

    public Product(
        String id,
        String name,
        String description,
        float price,
        @DrawableRes int imageResId) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public float getPrice() { return price; }

    @DrawableRes
    public int getImageResId() { return imageResId; }

    public void setId(String value) { id = value; }
    public void setName(String value) { name = value; }
    public void setDescription(String value) { description = value; }
    public void setPrice(float value) { price = value; }

    public void setImageResId(@DrawableRes int imageResId) {
        this.imageResId = imageResId;
    }
}
