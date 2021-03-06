package edu.ranken.prsmith.droidcafe.adapter;

import edu.ranken.prsmith.droidcafe.model.CartItem;

public interface OnQuantityChangeListener {
    void onChange(CartItem item, int newQuantity);
}

// interfaces
// - similar to abstract class
// - all of the methods have be abstract

// dependency inversion
// - change the direction of dependency
