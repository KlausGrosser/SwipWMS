package com.swip.swipwms.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrderItem {

    //Fields:
    private String itemName;
    @NotNull
    @Min(1)
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
