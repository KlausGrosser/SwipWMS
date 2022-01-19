package data;

import intro.TheWarehouseApp;
import intro.TheWarehouseManager;

import java.util.ArrayList;
import java.util.List;

import static intro.TheWarehouseApp.IS_EMPLOYEE;
import static intro.TheWarehouseApp.SESSION_ACTIONS;

public class Employee extends User{

    //Fields:
    private String password;
    private String role;
    private ArrayList<Employee> headOf;

    //Constructors:
    public Employee() {}

    public Employee(String userName,String password){
        this.name = userName;
        this.password = password;
    }
    public Employee(String userName, String password, String role, ArrayList<Employee> headOf){
        super.name = userName;
        this.password = password;
        this.headOf = headOf;
        this.role = role;
    }

    //Getters and Setters:
    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getRole(){
        return this.role;
    }

    //Public Methods:
    @Override
    public boolean authenticate(String password){
        return (password.equals(this.password));
    }

    public void order(String name, int amount){
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
