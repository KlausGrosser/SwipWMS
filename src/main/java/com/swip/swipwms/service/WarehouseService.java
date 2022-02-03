package com.swip.swipwms.service;

import com.swip.swipwms.model.Item;
import com.swip.swipwms.repository.WarehouseRepository;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class WarehouseService {

    public Set<Integer> getWarehouses(){
        return WarehouseRepository.getWarehouses();
    }

    public List<Item> getAllItems(){
        return WarehouseRepository.getAllItems();
    }

    public List<Item> getItemsByWarehouse(int warehouse){
        return WarehouseRepository.getItemsByWarehouse(warehouse);
    }

    public Set<String> getCategories(){
        return WarehouseRepository.getCategories();
    }

    public List<Item> getItemsByCategory(String category){
        return WarehouseRepository.getItemsByCategory(category);
    }

    public List<Item> findItems(String keyword) {
        return WarehouseRepository.find(keyword);
    }

    public void removeItemsFromRepositoryAfterOrder(String item, int orderAmount) throws IOException, ParseException {
        System.out.printf("\nOrdered %d %s\n",orderAmount, item);
        WarehouseRepository.removeItemFromList(item, orderAmount);
    }

}
