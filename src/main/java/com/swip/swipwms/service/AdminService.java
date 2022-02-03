package com.swip.swipwms.service;

import com.swip.swipwms.model.Order;

import java.io.IOException;
import java.util.List;

public interface AdminService {

    public void authenticateAdmin();

    public int getAdminsChoice();

    public void performAction(int option) throws IOException;

    public void listOrdersByWarehouse() throws IOException;

    public void listOrdersByStatus() throws IOException;

    public void listOrdersWhoseTotalCostGreaterThan() throws IOException;

    public void printsListOfOrdersToConsole(List<Order> orders);

    public void quit();

}