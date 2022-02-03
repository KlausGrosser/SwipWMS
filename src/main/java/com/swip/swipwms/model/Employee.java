package com.swip.swipwms.model;

import com.swip.swipwms.intro.TheWarehouseApp;
import com.swip.swipwms.intro.TheWarehouseManager;
import com.swip.swipwms.repository.WarehouseRepository;

import java.io.IOException;

import static com.swip.swipwms.intro.TheWarehouseApp.SESSION_ACTIONS;

public class Employee extends User{

    //Fields:
    private String password;
    private Roles role;

    //Constructors:
    public Employee() {}

    public Employee(String userName,String password){
        this.name = userName;
        this.password = password;
        this.role = Roles.EMPLOYEE;
    }

    //Getters and Setters:
    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public Roles getRole(){
        return this.role;
    }

    //Public Methods:
    @Override
    public boolean authenticate(String password){
        return (password.equals(this.password));
    }

    public void order(String name, int amount) throws IOException {
        System.out.printf("\nOrdered %d %s%s", amount, name,(amount == 1 ? "" : TheWarehouseManager.checkPluralOrder(name.toLowerCase())));
    }

    @Override
    public void greet(){
            System.out.printf("Hello, %s!\n" +
                    "If you experience a problem with the system,\n" +
                    "please contact technical support.\n", this.name);
    }

    @Override
    public void bye() {
        System.out.printf("\nThank you for your visit, %s!\n", this.name);
        if(TheWarehouseApp.SESSION_USER.checkAuthenticated()){
            for(int i = 0; i < SESSION_ACTIONS.size(); i++){
                System.out.printf("%d) %s\n", i+1, SESSION_ACTIONS.get(i));
            }
        }
    }

}
