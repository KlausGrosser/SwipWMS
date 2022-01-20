package com.swip.swipwms.service;

import java.util.ArrayList;
import java.util.List;

import com.swip.swipwms.model.Order;
import com.swip.swipwms.repository.OrderRepository;
import com.swip.swipwms.repository.UserRepository;
import com.swip.swipwms.repository.WarehouseRepository;
import com.swip.swipwms.intro.TheWarehouseApp;
import com.swip.swipwms.intro.TheWarehouseManager;

import static com.swip.swipwms.intro.TheWarehouseApp.SESSION_ACTIONS;

public class AdminServiceImpl implements AdminService{

    //Fields:
    private String[] adminOptions = { "1. List orders by warehouse", "2. List orders by status",
            "3. List Orders whose total cost greater than provided value", "4. Quit" };
    private final TheWarehouseManager warehouseManager = new TheWarehouseManager();

    //Public methods:
    @Override
    public void authenticateAdmin() {
        //prompt for passsword
        System.out.print("Enter your password: ");
        String password = TheWarehouseManager.reader.nextLine();
        TheWarehouseApp.SESSION_USER.setPassword(password);

        //prompt for password while admin is valid i.e the password matches
        if(UserRepository.isAdminValid(TheWarehouseApp.SESSION_USER.getName(), TheWarehouseApp.SESSION_USER.getPassword())){
            //set the SESSION_USER's isAuthenticated property to true.
            TheWarehouseApp.SESSION_USER.setAuthenticated(true);
        }
    }

    @Override
    public int getAdminsChoice() {
        // List all options
        System.out.println("What would you like to do?");
        for (String option : this.adminOptions) {
            System.out.println(option);
        }
        // return the valid choice
        return warehouseManager.promptIntChoice("Type the number of the operation: ", this.adminOptions.length);
    }

    @Override
    public void performAction(int option) {
        switch (option) {
            case 1: // "1. List orders by warehouse"
                this.listOrdersByWarehouse();
                break;
            case 2: // "2. List order by status"
                this.listOrdersByStatus();
                break;
            case 3: // 3. Sort orders as per totalCost (descending)
                this.listOrdersWhoseTotalCostGreaterThan();
                break;
            case 4: // "4. Quit"
                this.quit();
                break;
        }
    }

    @Override
    public void listOrdersByWarehouse() {
        //get all orders using OrderRepository
        List<Order> orders = OrderRepository.getAllOrders();

        //prompt user to enter the warehouse id
        int id = warehouseManager.promptIntChoice("Enter id number of warehouse: ", WarehouseRepository.getWarehouses().size());

        //create an empty list of order
        List<Order> ordersByWarehouse = new ArrayList<>();
        //Iterate over all the orders obtained from OrderRepository and add the orders belonging to the desired warehouse to the list
        for (Order order : orders) {
            if (order.getWarehouse() == id){
                ordersByWarehouse.add(order);
            }
        }
        // use the printsListOfOrdersToConsole method and pass the created list as argument.
        this.printsListOfOrdersToConsole(ordersByWarehouse);
        //Add the action detail string to the SESSION_ACTIONS in TheWarehouseApp. Eg: 'Listed Orders of warehouse ' + {warehouseId}
        SESSION_ACTIONS.add("Listed Orders of warehouse "+id+".");
    }

    @Override
    public void listOrdersByStatus() {
        // Keep asking for user's choice until a valid value is received

        System.out.println("List Orders by Status : ");
        System.out.println("1. NEW");
        System.out.println("2. PROCESSING");
        System.out.println("3. DELIVERED");

        // prompt user to select 1, 2 or 3 unless valid choice is made.
        int choice = warehouseManager.promptIntChoice("Choose 1, 2 or 3: ",3);

        String status = "";
        switch(choice) {
            case 1:
                status = "NEW";
                break;
            case 2:
                status = "PROCESSING";
                break;
            case 3:
                status = "DELIVERED";
                break;
            default:
                break;
        }
        //initialize  new empty list of order
        List<Order> orders = new ArrayList<>();
        //iterate over all the orders from OrderRepository and add only the orders with desired status to the list
        for (Order order : OrderRepository.getAllOrders()) {
            if(order.getStatus().equals(status)){
                orders.add(order);
            }
        }
        // use the printsListOfOrdersToConsole method and pass the created list as argument.
        this.printsListOfOrdersToConsole(orders);
        //Add the action detail string to the SESSION_ACTIONS in TheWarehouseApp. Eg: 'Listed Orders with status ' + {status}
        SESSION_ACTIONS.add("Listed Orders with status: "+status+".");
    }

    @Override
    public void listOrdersWhoseTotalCostGreaterThan() {
        int value = this.getValue();

        //initialize an empty list of orders
        List<Order> orders = new ArrayList<>();
        //iterate over all the orders from OrderRepository and add only the orders that meets the criteria to the list
    for (Order order : OrderRepository.getAllOrders()){
        if(order.getTotalCost() >= value){
            orders.add(order);
        }
    }
        // use the printsListOfOrdersToConsole method and pass the created list as argument.
        this.printsListOfOrdersToConsole(orders);
        //Add the action detail string to the SESSION_ACTIONS in TheWarehouseApp. Eg: 'Listed Orders with total cost greater than ' + {marginalValue}
        SESSION_ACTIONS.add("Listed Orders above "+value+"€");
    }

    @Override
    public void printsListOfOrdersToConsole(List<Order> orders) {
        if(orders.size() == 0) {
            System.out.println("No order found");
        }
        for(Order order: orders) {
            System.out.println("===============================================================================================================================");
            System.out.println("status : "+ order.getStatus() + ", isPaymentDone : " + order.isPaymentDone() + ", warehouse: " + order.getWarehouse() + ", dateOfOrder : " + order.getDateOfOrder()
                    + ", totalCost : " + order.getTotalCost());
            System.out.println( "orderItems : " + order.getOrderItems());
            System.out.println("================================================================================================================================");
        }
    }

    @Override
    public void quit() {
        //implement as similar to TheWarehouseManager.quit();
        TheWarehouseApp.SESSION_USER.bye();
        System.exit(0);
    }

    //Private methods:
    private int getValue() {
        int value = 0;
        String input;
        boolean valid = false;

        //prompt user to enter marginal value for total cost until a valid numerical value is entered.
        do {
            System.out.print("Enter value of total cost: ");
            input = TheWarehouseManager.reader.nextLine();
            if(input.matches("[0-9]+[\\.]?[0-9]*") && !input.isBlank()){
                value = Integer.parseInt(input);
                if(value>0){
                    valid = true;
                }
            }
            if(input.isBlank()){
                System.out.println("**************************************************\n" +
                        "No amount typed. Please enter a number above 0!\n" +
                        "**************************************************");
            } else if(!valid){
                System.out.println("**************************************************\n" +
                        "\"" + input + "\" is not a valid input. Please enter a number above 0!\n" +
                        "**************************************************");
            }
        }while(!valid);

        return value;
    }
}