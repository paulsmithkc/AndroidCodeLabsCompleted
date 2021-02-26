package edu.ranken.prsmith.droidcafe.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable {
    private ArrayList<CartItem> items;
    private float subtotal;
    private float taxAmount;
    private float taxPercent;
    private float total;

    public Cart() {
        items = new ArrayList<>();
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public void addItem(CartItem newItem) {
        boolean found = false;

        for (int i = 0; i < items.size(); ++i) {
            CartItem item = items.get(i);
            if (item.getProductId().equals(newItem.getProductId())) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                found = true;
                break;
            }
        }

        if (!found) {
            items.add(newItem);
        }

        subtotal += newItem.getProductPrice() * newItem.getQuantity();
        taxAmount = subtotal * taxPercent;
        total = subtotal + taxAmount;
    }

    public boolean updateItem(String productId, int quantity) {
        for (int i = 0; i < items.size(); ++i) {
            CartItem item = items.get(i);
            if (item.getProductId().equals(productId)) {
                int changeInQuantity = quantity - item.getQuantity();
                item.setQuantity(quantity);
                subtotal += item.getProductPrice() * changeInQuantity;
                taxAmount = subtotal * taxPercent;
                total = subtotal + taxAmount;
                return true;
            }
        }
        return false;
    }

    public void removeItem(String productId) {
        for (int i = items.size() - 1; i >= 0; --i) {
            CartItem item = items.get(i);
            if (item.getProductId().equals(productId)) {
                items.remove(i);
                subtotal -= item.getProductPrice() * item.getQuantity();
                taxAmount = subtotal * taxPercent;
                total = subtotal + taxAmount;
            }
        }
    }

    public void setTaxPercent(float percent) {
        taxPercent = percent;
        taxAmount = subtotal * taxPercent;
        total = subtotal + taxAmount;
    }

    public float getSubtotal() {
        return subtotal;
    }

    public float getTaxAmount() {
        return taxAmount;
    }

    public float getTaxPercent() {
        return taxPercent;
    }

    public float getTotal() {
        return total;
    }
}
