package com.swip.swipwms.model;

import java.io.IOException;
import java.util.List;

public abstract class User {

    //Fields:
    protected String name = "Anonymous";
    private boolean isAuthenticated = false;

    //Constructors:
    public User() {}
    public User(String userName){
        this.name = userName;
    }

    //Getters and Setters:

    public String getName() {
        return this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public boolean getAuthenticated() {
        return this.isAuthenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.isAuthenticated = authenticated;
    }


    //Public methods:
    public boolean authenticate(String password) {
        return false;
    }

    public boolean isNamed(String name) {
        return (name.equals(this.name));
    }

    public abstract void order(String item, int orderAmount) throws IOException;

    public void greet(){
        System.out.printf("Hello, %s!\n" +
                "Welcome to our Warehouse Database.\n" +
                "If you don't find what you are looking for,\n" +
                "please ask one of our staff members to assist you.",this.name);
    }

    public void bye() {
        System.out.printf("\nThank you for your visit %s!\n", this.name);
    }

    public boolean checkAuthenticated(){
        return this.isAuthenticated;
    }

    public abstract String getPassword();

    public abstract void setPassword(String password);

}
