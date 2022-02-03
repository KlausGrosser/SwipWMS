package com.swip.swipwms.model;

import com.swip.swipwms.intro.TheWarehouseManager;
import com.swip.swipwms.repository.WarehouseRepository;

import java.util.ArrayList;

import static com.swip.swipwms.intro.TheWarehouseApp.SESSION_ACTIONS;

public class Admin extends User{

    //Fields:
    private String password;
    private Roles role;
    private ArrayList<Employee> headOf;

    //Constructor:
    public Admin() {}

    public Admin(String name, String password) {
        this.name = name;
        this.password = password;
        this.role = Roles.ADMIN;
    }

    //Getters and Setters:
    @Override
    public String getPassword(){
        return this.password;
    }

    @Override
    public void setPassword(String password){
        this.password = password;
    }

    public Roles getRole(){
        return this.role;
    }

    //Public methods:
    @Override
    public void order(String name, int amount) {
        int count = amount;
        do{
            for(Item item : WarehouseRepository.getAllItems()){
                if(item.toString().toLowerCase().equals(name.toLowerCase())){
                    count--;
                    WarehouseRepository.getAllItems().remove(item);
                }
            }
        }while(count != 0);

        System.out.printf("\nOrdered %d %s%s", amount, name,(amount == 1 ? "" : TheWarehouseManager.checkPluralOrder(name.toLowerCase())));
    }

    @Override
    public void greet(){
        System.out.printf("Hello, %s!\n" +
                "    Welcome to the Admin Panel.\n" +
                "    With higher authority comes higher responsibility.\n", this.name);
    }

    @Override
    public void bye() {
        System.out.printf("\nThank you for your visit, %s!\n", this.name);
        for(int i = 0; i < SESSION_ACTIONS.size(); i++){
            System.out.printf("%d) %s\n", i+1, SESSION_ACTIONS.get(i));
        }
    }
}
