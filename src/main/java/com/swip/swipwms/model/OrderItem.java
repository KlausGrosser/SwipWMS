package com.swip.swipwms.model;

public class OrderItem {

    //Fields:
    private String itemName;
    private int amount;

    //Constructors:
    public OrderItem(){};

    public OrderItem(String itemName, int amount) {
        this.itemName = itemName;
        this.amount = amount;
    }

    //Getters:
    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
