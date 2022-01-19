package com.swip.swipwms.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Warehouse {
    //Fields:
    private int id;
    private List<Item> STOCK;

    //Constructors:
    public Warehouse(){}
    public Warehouse(int warehouseId){
        this.id = warehouseId;
        this.STOCK = new ArrayList<Item>();
    }

    //Getters:
    public int getId() {
        return this.id;
    }

    public List<Item> getStock() {
        return this.STOCK;
    }

    //Private methods:
    private String listStock(){
        List<String> list = new ArrayList<String>();
        for(Item item : this.STOCK){
            list.add(item.toString());
        }
        Collections.sort(list);
        for(String item : list){
            System.out.println("- " + item);
        }
        return "";
    }

    //Public Methods:
    /**
     *
     * @return STOCK.size();
     */
    public int occupancy(){
        return STOCK.size();
    }

    public void addItem(Item item){
        STOCK.add(item);

    }

    public List<Item> search(String searchTerm) throws ParseException {
        List<Item> result = new LinkedList<Item>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = dateFormat.parse(searchTerm);
       for(Item item : this.STOCK){
           if(item.getState().equals(searchTerm))result.add(item);
           if(item.getCategory().equals(searchTerm))result.add(item);
           if (item.getDateOfStock().equals(date))result.add(item);
           if(item.getWarehouse() == Integer.parseInt(searchTerm))result.add(item);
       }
        return result;
    }

    @Override
    public String toString() {
        System.out.println("Warehouse " + this.id + ": \nStock:");
       return "" + this.listStock();
    }

}



