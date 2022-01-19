package com.swip.swipwms.model;

public class Guest extends User{

    //Constructor:
    public Guest() {
        super();
    }


    //Public methods:
    @Override
    public boolean checkAuthenticated() {
        return false;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public void setPassword(String password) {}

    @Override
    public void order(String item, int orderAmount) {}


}
