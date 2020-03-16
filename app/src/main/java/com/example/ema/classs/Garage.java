package com.example.ema.classs;

import java.util.ArrayList;
import java.util.List;

public class Garage {
    private String id;
    private String name;
    private List<Order> orders;


    public Garage(String id, String name, List<Order> orders) {
        this.orders=new ArrayList<Order>();
        this.id = id;
        this.name = name;
        this.orders.addAll(orders);
    }

    public Garage(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
