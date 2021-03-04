package edu.ranken.prsmith.droidcafe.model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private String productId;
    private String productName;
    private int productThumbnailResId;
    private float productPrice;
    private int quantity;

    public CartItem(
        String productId,
        String productName,
        int productThumbnailResId,
        float productPrice,
        int quantity) {

        this.productId = productId;
        this.productName = productName;
        this.productThumbnailResId = productThumbnailResId;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public CartItem(Product product, int quantity) {
        this.productId = product.getId();
        this.productName = product.getName();
        this.productThumbnailResId = product.getImageResId();
        this.productPrice = product.getPrice();
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductThumbnailResId() {
        return productThumbnailResId;
    }

    public void setProductThumbnailResId(int productThumbnailResId) {
        this.productThumbnailResId = productThumbnailResId;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
