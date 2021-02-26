package edu.ranken.prsmith.droidcafe.model;

import java.util.ArrayList;
import java.util.List;

import edu.ranken.prsmith.droidcafe.R;

public class ProductDataSource {

    private ArrayList<Product> products;

    public ProductDataSource() {
        products = new ArrayList<>();
        products.add(new Product(
            "1",
            "Donut",
            "Donuts are glazed and sprinkled with candy.",
            3.99f,
            R.drawable.donut_circle
        ));
        products.add(new Product(
            "2",
            "Ice Cream Sandwich",
            "Ice cream sandwiches have chocolate wafers and vanilla filling.",
            2.50f,
            R.drawable.icecream_circle
        ));
        products.add(new Product(
            "3",
            "FroYo",
            "FroYo is premium self-serve frozen yogurt.",
            9.00f,
            R.drawable.froyo_circle
        ));
        products.add(new Product(
            "4",
            "Donut 2",
            "Donuts are glazed and sprinkled with candy.",
            3.99f,
            R.drawable.donut_circle
        ));
        products.add(new Product(
            "5",
            "Ice Cream Sandwich 2",
            "Ice cream sandwiches have chocolate wafers and vanilla filling.",
            2.50f,
            R.drawable.icecream_circle
        ));
        products.add(new Product(
            "6",
            "FroYo 2",
            "FroYo is premium self-serve frozen yogurt.",
            9.00f,
            R.drawable.froyo_circle
        ));
    }

    public List<Product> getAll() {
        return products;
    }

    public Product getById(String id) {
        for (int i = 0; i < products.size(); ++i) {
            if (products.get(i).getId().equals(id)) {
                return products.get(i);
            }
        }
        return null;
    }
}
