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

    // To read inputs from the console/CLI
    //private Scanner reader = new Scanner(System.in);

    //Fields:
    private String[] adminOptions = { "1. List orders by warehouse", "2. List orders by status",
            "3. List Orders whose total cost greater than provided value", "4. Quit" };


    //Public methods:
    @Override
    public void authenticateAdmin() {
        // TODO implement
        //prompt for passsword
        System.out.print("Enter your password: ");
        String password = TheWarehouseManager.reader.nextLine();
        TheWarehouseApp.SESSION_USER.setPassword(password);

        //prompt for password while admin is valid i.e the password matches
        if(UserRepository.isAdminValid(TheWarehouseApp.SESSION_USER.getName(), TheWarehouseApp.SESSION_USER.getPassword())){
            TheWarehouseApp.SESSION_USER.setAuthenticated(true);
        }
        //set the SESSION_USER's isAuthenticated property to true.
    }

    @Override
    public int getAdminsChoice() {
        // TODO implement
        int choice = 0;
        // List all options
        System.out.println("What would you like to do?");
        for (String option : this.adminOptions) {
            System.out.println(option);
        }

        System.out.print("Type the number of the operation: ");
        // Keep asking for admin's choice until a valid value is received
        do {
            String selectedOption = TheWarehouseManager.reader.nextLine();
            try {
                choice = Integer.parseInt(selectedOption);
            } catch (Exception e) {
                choice = 0;
            }
            // Guide the user to enter correct value
            if (choice < 1 || choice > adminOptions.length) {
                System.out.printf("Sorry! Enter an integer between 1 and %d. ", this.adminOptions.length);
            }
        } while (choice < 1 || choice > adminOptions.length);
        // return the valid choice
        return choice;
    }

    @Override
    public void performAction(int option) {
        // TODO implement

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
            default:
                System.out.println("Sorry! Invalid option.");
        }
    }

    @Override
    public void listOrdersByWarehouse() {
        // TODO implement
        //get all orders using OrderRepository
        List<Order> orders = OrderRepository.getAllOrders();

        //prompt user to enter the warehouse id
        System.out.print("Enter id number of warehouse: ");
        int id = 0;
        do {
             id = Integer.parseInt(TheWarehouseManager.reader.nextLine());
             if(!(id > 0 && id <= WarehouseRepository.getWarehouses().size())){
                 System.out.print("Please enter a number between 1 and "+WarehouseRepository.getWarehouses().size()+": ");
             }
        }while(!(id > 0 && id <= WarehouseRepository.getWarehouses().size()));

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
        // TODO implement
        // Keep asking for user's choice until a valid value is received
        int choice = 0;
        System.out.println("List Orders by Status : ");
        System.out.println("1. NEW");
        System.out.println("2. PROCESSING");
        System.out.println("3. DELIVERED");
        System.out.print("Choose 1, 2 or 3: ");
        // prompt user to select 1, 2 or 3 unless valid choice is made.
        do {
            String selectedOption = TheWarehouseManager.reader.nextLine();
            try {
                choice = Integer.parseInt(selectedOption);
            } catch (Exception e) {
                choice = 0;
            }
            // Guide the user to enter correct value
            if (choice < 1 || choice > 3) {
                System.out.print("Sorry! Enter an integer between 1 and 3: ");
            }
        } while (choice < 1 || choice > 3);
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
        // TODO implement
        double choice = 0;
        //prompt user to enter marginal value for total cost until a valid numerical value is entered.
        System.out.print("Enter value of total cost: ");
        int value = 0;
        do {
             value = Integer.parseInt(TheWarehouseManager.reader.nextLine());
        }while(value <=0);
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
        SESSION_ACTIONS.add("Listed Orders of "+value+" euros and more.");
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
        // TODO implement
        //implement as similar to TheWarehouseManager.quit();
        TheWarehouseApp.SESSION_USER.bye();
        System.exit(0);
    }
}