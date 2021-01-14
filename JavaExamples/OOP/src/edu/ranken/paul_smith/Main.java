package edu.ranken.paul_smith;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Person[] people = new Person[3];
        people[0] = new Customer(2, "Paul", (short) 34, 5.5, false);
        people[1] = new Employee(3, "Sam", "123", 100_000D);
        people[2] = new Customer(4, "Moe", (short)20, 4.3, true);

        System.out.println(people[0].name);

        for (Person p : people) {
            System.out.println(p.getName());
            if (p instanceof Customer) {
                System.out.println(((Customer) p).getAge());
            }
            if (p instanceof Employee) {
                System.out.println(((Employee)p).getSalary());
            }
        }

        //Person p = new Person(1, "Person");

//        Person cust = new Customer(
//            2,
//            "Paul Smith",
//            (short)34,
//            5.5,
//            false);
//
//        Person emp = new Employee(3, "Sam", "123", 100_000D);
//
//        System.out.println(cust.getName());
//        System.out.println(emp.getName());

        //p.name = "test";
        //p.setName = "test";
        //p.setName("test");
    }

    public static <E extends Customer> E findYoungestCustomer(Iterable<E> customers) {
        Iterator<E> itr = customers.iterator();
        E youngest = itr.next();
        while (itr.hasNext()) {
            E current = itr.next();
            if (current.getAge() < youngest.getAge()) {
                youngest = current;
            }
        }
        return youngest;
    }

//    public static <E extends Comparable<E>> E findLargest(List<E> items) {
//        return items.get(0);
//    }

    public static int weirdFinally() {
        try {
            return 0;
        } finally {
            throw new NullPointerException();
            //throw new IOException();
            //return 1;
        }
    }
}
