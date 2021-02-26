package edu.ranken.prsmith.droidcafe;

import android.app.Application;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import edu.ranken.prsmith.droidcafe.model.Cart;
import edu.ranken.prsmith.droidcafe.model.ProductDataSource;

public class DroidCafe extends Application {

    private ProductDataSource products;
    private Cart cart;

    @Override
    public void onCreate() {
        super.onCreate();

        // Switch between Light and Dark themes based on system setting
        // https://medium.com/androiddevelopers/appcompat-v23-2-daynight-d10f90c83e94
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

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
