package com.swip.swipwms.model;

import java.util.Date;
import java.util.List;

public class Order {

    //Fields:
    private String status;
    private boolean isPaymentDone;
    private int warehouse;
    private Date dateOfOrder;
    private List<OrderItem> orderItems;
    private double totalCost;

    //Constructors:
    public Order(){};

    public Order(String status,boolean isPaymentDone, int warehouse, Date dateOfOrder, List<OrderItem> orderItems, double totalCost){
        this.status = status;
        this.warehouse = warehouse;
        this.dateOfOrder = dateOfOrder;
        this.orderItems = orderItems;
        this.totalCost = totalCost;
        this.isPaymentDone = isPaymentDone;
    };

    //Getters:
    public boolean isPaymentDone(){
        return this.isPaymentDone;
    }

    public String getStatus() {
        return this.status;
    }

    public List<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public double getTotalCost() {
        return this.totalCost;
    }

    public int getWarehouse() {
        return this.warehouse;
    }

    public Date getDateOfOrder() {
        return this.dateOfOrder;
    }

}
