package com.swip.swipwms.intro;


import com.swip.swipwms.model.*;
import com.swip.swipwms.repository.UserRepository;
import com.swip.swipwms.repository.WarehouseRepository;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Provides necessary methods to deal through the Warehouse management actions
 *
 * @author riteshp
 */
public class TheWarehouseManager extends WarehouseRepository {
    // =====================================================================================
    // Member Variables
    // =====================================================================================



    // To read inputs from the console/CLI
    public static Scanner reader = new Scanner(System.in);
    private final String[] userOptions = {
            "1. List items by warehouse", "2. Search an item and place an order", "3. Browse items by category", "4. Quit"
    };
    // To refer the user provided name.
    private String userName;


    // =====================================================================================
    // Public Member Methods
    // =====================================================================================

    /**
     * Welcome User
     */
    public void welcomeUser() {
        //this.statusCheck();

        this.seekUserName();
        TheWarehouseApp.SESSION_USER.greet();
    }

    /**
     * Ask for user's choice of action
     */
    public int getUsersChoice() {
        System.out.println();
        for (String option : this.userOptions) {
            System.out.println(option);
        }
        return this.promptIntChoice("Type the number of the operation: ", this.userOptions.length);
    }

    /**
     * Initiate an action based on given option
     */
    public void performAction(int option) {
        switch (option) {
            case 1:
                this.listItemsByWarehouse();
                break;
            case 2:
                this.searchItemAndPlaceOrder();
                break;
            case 3:
                this.browseByCategory();
                break;
            case 4:
                this.quit();
                break;
            default:
                break;

        }
    }

    /**
     * Confirm an action
     * @return action
     */
    public boolean confirm(String message) {
        String choice;
        do {
            System.out.printf("%s (y/n): ", message);
            choice = reader.nextLine();
            if (choice.length() > 0) {
                choice = choice.trim().toLowerCase();
            }
        } while (!choice.startsWith("y") && !choice.startsWith("n"));
        return choice.startsWith("y");
    }

    /**
     * End the application
     */
    public void quit() {
        TheWarehouseApp.SESSION_USER.bye();

        System.exit(0);
    }

    public int promptIntChoice(String prompt, int numOfOptions){
        int choice = 0;
        System.out.print(prompt);
        String input;
        do {
            input = reader.nextLine();
            if(input.isBlank()){
                reader = new Scanner(System.in);
            }
            if(input.matches("[0-9]+[\\.]?[0-9]*")){
                choice = Integer.parseInt(input);
            }
            this.printErrorMessage(choice, input, numOfOptions, prompt);
        } while (choice < 1 || choice > numOfOptions || !input.matches("[0-9]+[\\.]?[0-9]*"));
        return choice;
    }

    // =====================================================================================
    // Private Methods
    // =====================================================================================

    /**
     Prints an error message if the entered option is not a valid number.
     */
    private void printErrorMessage(int choice, String input, int numOfOptions, String newPrompt){
        if (choice < 1 || choice > this.userOptions.length || !input.matches("[0-9]+[\\.]?[0-9]*")) {
            if(input.isBlank()){
                System.out.println("**************************************************\n" +
                        "No option selected. Please enter a number between 1 and " + numOfOptions +"!\n" +
                        "**************************************************");
                System.out.print(newPrompt);
            }else {
                System.out.println("**************************************************\n" +
                        "\"" + input + "\" is not a valid operation. Please enter a number between 1 and " + numOfOptions + "!\n" +
                        "**************************************************");
                System.out.print(newPrompt);
            }
        }
    }
    /**
     * Get user's name via CLI
     */
    private String seekUserName() {
        System.out.print("Enter your username: ");
        String userName = reader.nextLine().replaceAll("\\s", "");
        if(UserRepository.isUserEmployee(userName)) {
            TheWarehouseApp.SESSION_USER = new Employee();
            TheWarehouseApp.SESSION_USER.setName(userName);
        }else if(UserRepository.isUserAdmin(userName)){
            TheWarehouseApp.SESSION_USER = new Admin();
            TheWarehouseApp.SESSION_USER.setName(userName);
        }else if (!userName.isBlank()&&(!UserRepository.isUserEmployee(TheWarehouseApp.SESSION_USER.getName()))){
            TheWarehouseApp.SESSION_USER = new Guest();
            TheWarehouseApp.SESSION_USER.setName(userName);
        }else{
            TheWarehouseApp.SESSION_USER = new Guest();
        }
        return userName;
    }

    String seekPassword() {
        System.out.print("Please enter your password: ");
        String password = reader.nextLine();
        TheWarehouseApp.SESSION_USER.setPassword(password);
        return password;
    }

    private void printNumberOfItemsByWarehouse() {
        System.out.println();
        for (int warehouse : getWarehouses()) {
            System.out.printf("Total items in warehouse %d: %d\n", warehouse, WarehouseRepository.getItemsByWarehouse(warehouse).size());
        }
    }

    private void listItemsByWarehouse() {
        for (Warehouse warehouse : WarehouseRepository.WAREHOUSE_LIST) {
            System.out.println(warehouse.toString());
            System.out.println("====================================\n====================================\n");
        }
        printNumberOfItemsByWarehouse();
        TheWarehouseApp.SESSION_ACTIONS.add("Listed "+getTotalListedItems(WarehouseRepository.getAllItems())+" items.");
    }

    private int getTotalListedItems(List<Item> masterList){
        return masterList.size();
    }

    private boolean checkItem(String itemName){
        return this.getAvailableAmount(itemName.toLowerCase()) > 0;
    }

    private void searchItemAndPlaceOrder() {
        String itemName;
        boolean found;
        do{
            itemName = askItemToOrder();
            found = this.checkItem(itemName);
            if(itemName.isBlank()){
                System.out.println("You didn't entered anything. Please try again.");
            }else if(!found){
                if(confirm("\"" + itemName + "\" is not in Stock. Do you want to try again?")){
                    found = false;
                }else{
                    return;
                }
            }
        }while(!found);

        if(this.checkItem(itemName)){
            printAmountAvailable(itemName.toLowerCase());
            printLocations(itemName.toLowerCase());
            if (getAvailableAmount(itemName.toLowerCase()) > 0) {
                printMaximumAvailability(itemName.toLowerCase());
                TheWarehouseApp.SESSION_ACTIONS.add("Searched "+getAppropriateIndefiniteArticle(formattedItem(itemName))+ formattedItem(itemName)+".");
                askAmountAndConfirmOrder(getAvailableAmount(itemName.toLowerCase()), itemName);
            }
        }
    }

    private boolean hasVowel(String itemName) {
        switch (itemName.charAt(0)) {
            case 'a':
            case 'e':
            case 'i':
            case 'o':
            case 'u':
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'U':
                return true;
            default:
                return false;
        }
    }

    private String getAppropriateIndefiniteArticle(String itemName){
            return (hasVowel(itemName))?"an " : "a ";
    }

    private void printMaximumAvailability(String item) {
        int itemsInW1 = find(item, 1);
        int itemsInW2 = find(item, 2);

        if(itemsInW1 < itemsInW2){
            System.out.printf("Maximum availability: %d in Warehouse %s\n", itemsInW2, 2);
        }else if(itemsInW1 > itemsInW2){
            System.out.printf("Maximum availability: %d in Warehouse %s\n", itemsInW1, 1);
        }else{
            System.out.printf("Maximum availability: %d in each Warehouse\n", itemsInW1);
        }
  }

    private void printAmountAvailable(String item) {
      System.out.println("Amount available: " + getAvailableAmount(item));
    }

    /**
     * Ask the user to specify an Item to Order
     *
     * @return String itemName
     */
    private String askItemToOrder() {
        System.out.print("\nWhat is the name of the item? : ");
        return  reader.nextLine().trim().replaceAll("\\s+", " ");
    }

    /**
     * Calculate total availability of the given item
     *
     * @param itemName itemName
     * @return integer availableCount
     */
    private int getAvailableAmount(String itemName) {
        int count = 0;
        for (int x : getWarehouses()) {
            count += find(itemName, x);
        }
        return count;
    }


    private void printLocations(String item) {
            System.out.println("Location:");
            for (Item items : getItemsByWarehouse(1)) {
                if ((items.getState() + " " + items.getCategory()).toLowerCase().equals(item)) {
                    System.out.println("- Warehouse " + items.getWarehouse() + " (in stock for " + daysSinceStocked(items) + " days.)");
                }
            }
            for (Item items : getItemsByWarehouse(2)) {
                if ((items.getState() + " " + items.getCategory()).toLowerCase().equals(item)) {
                    System.out.println("- Warehouse " + items.getWarehouse() + " (in stock for " + daysSinceStocked(items) + " days.)");
                }
            }
    }

    private long daysSinceStocked(Item item) {
        return TimeUnit.DAYS.convert((new Date().getTime() - item.getDateOfStock().getTime()), TimeUnit.MILLISECONDS);
    }

    /**
     * Find the count of an item in a given warehouse
     *
     * @param item      the item
     * @param warehouse the warehouse
     * @return count
     */
    private int find(String item, int warehouse) {
        int count = 0;
        for (Item itemFound : getItemsByWarehouse(warehouse)) {
            if (item.equals(itemFound.getState().toLowerCase() + " " + itemFound.getCategory().toLowerCase())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Ask order amount and confirm order
     */
    private void askAmountAndConfirmOrder(int availableAmount, String item) {
        if(TheWarehouseApp.SESSION_USER.checkAuthenticated()) {
            System.out.println("\nYou are authorized to place orders.");
        boolean toOrder = this.confirm("\nWould you like to order this item? ");
        if (toOrder) {
                int orderAmount = this.getOrderAmount(availableAmount);
                if(orderAmount == 0){
                    return;
                }else{
                    TheWarehouseApp.SESSION_USER.order(formattedItem(item),orderAmount);
                    if(orderAmount == 1){
                        TheWarehouseApp.SESSION_ACTIONS.add("Ordered "+orderAmount+" "+formattedItem(item)+".");
                    }else {
                        if (checkPluralName(item.toLowerCase())) {
                            TheWarehouseApp.SESSION_ACTIONS.add("Ordered " + orderAmount + " " + formattedItem(item) + this.checkPluralOrder(item)+".");
                        } else {
                            TheWarehouseApp.SESSION_ACTIONS.add("Ordered " + orderAmount + " " + formattedItem(item) + this.checkPluralOrder(item)+".");
                        }
                    }
                }
            }
        }
        else{
            System.out.println("\nYou are not authorized to place orders.");
        }
    }

    public static boolean checkPluralName(String itemName) {
        return itemName.endsWith("s");
    }

    public static String checkPluralOrder(String itemName){
        if(checkPluralName(itemName)){
            return "";
        }else{
            return "s";
        }
    }

    private String formattedItem(String itemName) {
        String item = null;
        if(!itemName.isBlank()){
            item = itemName.toUpperCase().charAt(0) + itemName.substring(1).toLowerCase();
        }
        return item;
    }


    private int getOrderAmount(int availableAmount) {
        int orderAmount = -1;

        System.out.print("How many would you like? ");
        do {
            while(!reader.hasNextInt()) {
                reader.nextLine();
                System.out.print("Please enter a number: ");
            }
            orderAmount = Integer.parseInt(reader.nextLine());

            if (orderAmount > availableAmount) {
                System.out.println("**************************************************\n" +
                        "There are not this many available. The maximum amount that can be ordered is: " + availableAmount +
                        "\n**************************************************");

                boolean orderAll = this.confirm("Would you like to order the maximum available?");
                    if (orderAll) {
                        orderAmount = availableAmount;
                        } else {
                            boolean keepOrdering = this.confirm("Would you like to order another amount of this item?");

                            if (keepOrdering) {
                             System.out.print("How many would you like? ");
                             orderAmount = -1;
                             } else {
                                orderAmount = 0;
                                }
                            }
            } else if (orderAmount < 0) {
                System.out.print("Error!! the entered amount must be greater than 0. Please try again: ");
            } else {
                return orderAmount;
            }
        } while (orderAmount < 0 || orderAmount > availableAmount);
        return orderAmount;
    }

    private void browseByCategory() {
        Map<Integer, String> menu = createCategoryMenu();
        System.out.println("\nCategories: ");
        printCategoryMenu(menu);
        int choice = chooseCategory();
        printCategoryItems(choice, menu);

        TheWarehouseApp.SESSION_ACTIONS.add("Browsed the category "+menu.get(choice));
    }

    private int amountOfItemsPerCategory(String category){
        return getItemsByCategory(category).size();
    }

    private Map<Integer, String> createCategoryMenu(){
        Map<Integer, String> result = new LinkedHashMap<>();
        int count = 1;
        Set<String> sortedSet = getCategories();
        for(String category: sortedSet){
            result.put(count, category);
            count++;
        }
        return result;
    }

    private void printCategoryMenu(Map<Integer, String> menu){
        for(var entry : menu.entrySet()) {
            System.out.printf("%d. %s (%d)%n", entry.getKey(), entry.getValue(), amountOfItemsPerCategory(entry.getValue()));
        }
    }

    private int chooseCategory(){
        return this.promptIntChoice("Type the number of the category to browse: ", getCategories().size());
    }

    private void printCategoryItems(int choice, Map<Integer, String> menu){
        String category = menu.get(choice);
        System.out.printf("\nList of %ss available:%n", category);

        List<String> items = new ArrayList<>();

        for (Item item: getItemsByCategory(category)){
            System.out.printf("%s %s, Warehouse %d%n", item.getState(), category, item.getWarehouse());
        }
    }
}