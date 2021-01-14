package edu.ranken.paul_smith;

import java.util.Objects;

public class Customer extends Person implements Purchaser {
    protected short age;
    protected double height;
    protected boolean isSingle;

//    public Customer() {
//    }

    public Customer(int id, String name, short age, double height, boolean isSingle) {
        super(id, name);
        this.age = age;
        this.height = height;
        this.isSingle = isSingle;
    }

    public short getAge() { return this.age; }

    // booleans: use verb is/has
    public boolean isSingle() {
        return this.isSingle;
    }

    public void setIsSingle(boolean value) {
        isSingle = value;
    }

    @Override
    public String toString() {
        return String.format("Person(%s, %d, %.1f, %b)", name, age, height, isSingle);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return age == customer.age && Double.compare(customer.height, height) == 0 && isSingle == customer.isSingle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), age, height, isSingle);
    }

    @Override
    public double getBalance() {
        //return balance;
        return 10_000;
    }

    @Override
    public void buy(Object product) {
        // balance -= product.price;
    }
}
