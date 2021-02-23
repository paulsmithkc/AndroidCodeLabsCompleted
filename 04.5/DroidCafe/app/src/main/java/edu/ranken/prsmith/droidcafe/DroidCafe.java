package edu.ranken.prsmith.droidcafe;

import android.app.Application;
import android.util.Log;

import edu.ranken.prsmith.droidcafe.model.Cart;
import edu.ranken.prsmith.droidcafe.model.ProductDataSource;

public class DroidCafe extends Application {

    private ProductDataSource products;
    private Cart cart;

    @Override
    public void onCreate() {
        super.onCreate();

        products = new ProductDataSource();
        cart = new Cart();
        cart.setTaxPercent(0.08f);

        Log.i("DroidCafe", "Application Created.");
    }

    public ProductDataSource getProducts() {
        return products;
    }

    public Cart getCart() {
        return cart;
    }
}
