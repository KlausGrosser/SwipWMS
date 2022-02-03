package com.swip.swipwms.repository;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import com.swip.swipwms.model.Item;
import com.swip.swipwms.model.Warehouse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static org.json.simple.JSONArray.writeJSONString;


/**
 * The Data Repository
 *
 * @author pujanov
 *
 */
public class WarehouseRepository {

    //Fields:
    public static List<Warehouse> WAREHOUSE_LIST = new ArrayList<>();
    private static List<Integer> WAREHOUSE_IDS = new ArrayList<>();


    //Static methods:

    /**
     * Load item records from the stock.json file
     */
    static {
        System.out.println("Loading items...");
        try {
            createSessionStock(
                    "/Users/klausgrosser/IdeaProjects/SwipWMS/data/stock.json",
                    "/Users/klausgrosser/IdeaProjects/SwipWMS/data/session-stock.json");
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateStock();

    }

    //Getters:

    /**
     * Get All items available in the repository
     *
     * @return
     */
    public static List<Item> getAllItems() {
        List<Item> allItems = new ArrayList<Item>();
        //for(Warehouse warehouse : WAREHOUSE_LIST) {
        for (int i = 0; i < WAREHOUSE_LIST.size(); i++) {
            List<Item> itemsWarehouse = new ArrayList<Item>();
            for (Item item : WAREHOUSE_LIST.get(i).getStock()) {
                item.setWarehouse(WAREHOUSE_LIST.get(i).getId());
                itemsWarehouse.add(item);
                itemsWarehouse.sort(new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        return item1.compareTo(item2);
                    }
                });
                itemsWarehouse.sort(new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        return item1.getCategory().compareTo(item2.getCategory());
                    }
                });
            }
            allItems.addAll(itemsWarehouse);

        }
        return allItems;
    }

    // By Warehouse
    /**
     * Get the list of unique warehouse IDs
     *
     * @return list of unique warehouse IDs
     */
    public static Set<Integer> getWarehouses() {
        Set<Integer> warehouses = new HashSet<Integer>();
        for (Warehouse warehouse : WAREHOUSE_LIST) {
            warehouses.add(warehouse.getId());
        }
        return warehouses;
    }

    /**
     * Get the list of all items in a specific warehouse
     *
     * @param warehouse
     * @return
     */
    public static List<Item> getItemsByWarehouse(int warehouse) {
        return getItemsByWarehouse(warehouse, getAllItems());
    }

    /**
     * Get the list of items related to a specific warehouse in a given master-list
     *
     * @param warehouse
     * @return
     */
    public static List<Item> getItemsByWarehouse(int warehouse, List<Item> masterList) {
        List<Item> items = new ArrayList<Item>();
        for (Item item : masterList) {
            if (item.getWarehouse() == warehouse) {
                items.add(item);
            }
        }
        return items;
    }


    // By Category
    /**
     * Get the list of unique Categories
     *
     * @return
     */
    public static Set<String> getCategories() {
        Set<String> categories = new TreeSet<String>();
        for (Item item : getAllItems()) {
            categories.add(item.getCategory());
        }
        return categories;
    }


    /**
     * Get the list of all items of a specific category
     *
     * @param category
     * @return
     */

    public static List<Item> getItemsByCategory(String category) {

        return getItemsByCategory(category, getAllItems());
    }


    /**
     * Get the list of items of a specific category in a given master-list
     *
     * @param category
     * @return
     */
    public static  List<Item> getItemsByCategory(String category, List<Item> masterList) {
        List<Item> items = new ArrayList<>();

        for (Item item : masterList) {
            if (item.getCategory().equalsIgnoreCase(category)) {
                items.add(item);
            }
        }
        items.sort(new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return item1.compareTo(item2);
            }
        });
        return items;
    }

    //Public methods:

    public static void removeItemFromList(String itemName, int amount) throws IOException, ParseException {
        System.out.println("Updating stock...");

        File file = new File("data/temp-stock.json");
        FileWriter fileWriter = new FileWriter(file, true);
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = (JSONArray) parser.parse(new FileReader("/Users/klausgrosser/IdeaProjects/SwipWMS/data/session-stock.json"));
        JSONArray newJsonArray = new JSONArray();
        Item foundItem = findItem(itemName);
        int count = 0;


        for (Object obj : jsonArray)
        {
            JSONObject item = (JSONObject) obj;
            String jsonItemName = item.get("state").toString() + " " + item.get("category").toString().toLowerCase();
            if(jsonItemName.toLowerCase().equals(itemName.toLowerCase()) && count < amount){
                count++;
            }else{
                newJsonArray.add(obj);
            }
        }
        writeJSONString(newJsonArray, fileWriter);
        fileWriter.close();

        createSessionStock("data/temp-stock.json", "data/session-stock.json");

        updateStock();
        new FileWriter("data/temp-stock.json", false).close();
    }

    //Private methods:

    private static Item findItem(String itemName){
        Item itemFound = new Item();
        for (Item item : getAllItems()){
            if(item.toString().toLowerCase().equals(itemName.toLowerCase().trim())){
                itemFound = item;
            }
        }
        return itemFound;
    }


    private static void updateStock(){

        BufferedReader reader = null;

        try {
            //ITEM_LIST.clear();
            WAREHOUSE_LIST.clear();
            WAREHOUSE_IDS.clear();

            reader = new BufferedReader(new FileReader("data/session-stock.json"));
            Object data = JSONValue.parse(reader);

            if (data instanceof JSONArray) {
                JSONArray dataArray = (JSONArray) data;

                for (Object obj : dataArray) {
                    if (obj instanceof JSONObject) {
                        JSONObject jsonData = (JSONObject) obj;
                        Item item = new Item();
                        item.setState(jsonData.get("state").toString());
                        item.setCategory(jsonData.get("category").toString());
                        String date = jsonData.get("date_of_stock").toString();
                        // System.out.println("Item Date " + date);
                        item.setDateOfStock(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date));
                        // System.out.println(item);

                        int jsonWarehouseId = Integer.parseInt(jsonData.get("warehouse").toString());

                        if (!WAREHOUSE_IDS.contains(jsonWarehouseId)) {
                            WAREHOUSE_IDS.add(jsonWarehouseId);

                            Warehouse warehouse = new Warehouse(jsonWarehouseId);
                            warehouse.addItem(item);
                            WAREHOUSE_LIST.add(warehouse);
                        } else {
                            for (int i = 0; i < WAREHOUSE_LIST.size(); i++) {
                                if (WAREHOUSE_LIST.get(i).getId() == jsonWarehouseId) {
                                    WAREHOUSE_LIST.get(i).addItem(item);
                                    //break;
                                }
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private static void createSessionStock(String inputPath, String outputPath) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(
                    inputPath);

            fos = new FileOutputStream(
                    outputPath);
            int c;
            while ((c = fis.read()) != -1) {
                fos.write(c);
            }

        } finally {
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }
}