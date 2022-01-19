package com.swip.swipwms.controller;

import com.swip.swipwms.model.Item;
import com.swip.swipwms.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController (WarehouseService warehouseService){
        this.warehouseService = warehouseService;
    }

    @GetMapping("/getWarehouses")
    public Set<Integer> getWarehouses() {
        return warehouseService.getWarehouses();
    }

    @GetMapping("/getAllItems")
    public List<Item> getAllItems(){
        return warehouseService.getAllItems();
    }

    @GetMapping("/getItemsByWarehouse/{warehouseId}")
    public List<Item> getItemsByWarehouse(@PathVariable("warehouseId") int warehouseId){
        return warehouseService.getItemsByWarehouse(warehouseId);
    }

    @GetMapping("/getCategories")
    public Set<String> getCategories() {
        return warehouseService.getCategories();
    }

    @GetMapping("/getItemsByCategory/{category}")
    public List<Item> getItemsByCategory(@PathVariable("category") String category){
        return warehouseService.getItemsByCategory(category);
    }


}
