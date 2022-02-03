package com.swip.swipwms.controller;

import com.swip.swipwms.model.Item;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {

    RestTemplate restTemplate;

    @GetMapping("/loginPage")
    public String login(@RequestParam(value = "loginFailed", defaultValue = "false") Boolean loginFailed, Model model) {
        model.addAttribute("loginFailed", loginFailed);
        return "login.html";
    }

    @GetMapping("/")
    public String getIndex() {
        return "index.html";
    }

    @GetMapping("/listAllItems")
    public String getListAllItemsPage(HttpServletRequest request, Model model) {
        restTemplate = new RestTemplate();
        String itemResourceUrl = "http://localhost:" + request.getLocalPort() + "/warehouse/getAllItems";
        String warehouseResourceUrl = "http://localhost:" + request.getLocalPort() + "/warehouse/getWarehouses";

        List<Item> response = restTemplate.getForObject(
                itemResourceUrl,
                List.class
        );

        Set<Integer> warehouseResponse = restTemplate.getForObject(
                warehouseResourceUrl,
                Set.class
        );

        model.addAttribute("warehouses", warehouseResponse);
        model.addAttribute("items", response);
        model.addAttribute("itemCount", response.size());
        return "items_list.html";
    }

    @GetMapping("/listItemsByWarehouse/{warehouseId}")
    public String getListItemsByWarehousePage(HttpServletRequest request, Model model, @PathVariable("warehouseId") int warehouseId) {
        restTemplate = new RestTemplate();
        String itemResourceUrl = "http://localhost:" + request.getLocalPort() + "/warehouse/getItemsByWarehouse/" + warehouseId;

        List<Item> response = restTemplate.getForObject(
                itemResourceUrl,
                List.class
        );


        model.addAttribute("warehouse", warehouseId);
        model.addAttribute("items", response);
        model.addAttribute("itemCount", response.size());
        return "items_list_by_warehouse.html";
    }

    @GetMapping("/browseByCategory")
    public String getBrowseByCategoryPage(HttpServletRequest request, Model model) {
        restTemplate = new RestTemplate();
        String categoryResourceUrl = "http://localhost:" + request.getLocalPort() + "/warehouse/getCategories";
        String warehouseResourceUrl = "http://localhost:" + request.getLocalPort() + "/warehouse/getWarehouses";

        List<Item> response = restTemplate.getForObject(
                categoryResourceUrl,
                List.class
        );

        Set<Integer> warehouseResponse = restTemplate.getForObject(
                warehouseResourceUrl,
                Set.class
        );

        model.addAttribute("warehouses", warehouseResponse);
        model.addAttribute("category", response);
        model.addAttribute("itemCount", response.size());
        return "browse_by_category.html";
    }

    @GetMapping("/browseByCategory/{category}")
    public String getBrowseByCategoryPage(HttpServletRequest request, Model model, @PathVariable("category") String category) {
        restTemplate = new RestTemplate();
        String itemResourceUrl = "http://localhost:" + request.getLocalPort() + "/warehouse/getItemsByCategory/" + category;

        List<Item> response = restTemplate.getForObject(
                itemResourceUrl,
                List.class
        );

        model.addAttribute("category", category);
        model.addAttribute("items", response);
        model.addAttribute("itemCount", response.size());
        return "browse_by_specific_category.html";
    }

    @GetMapping("/searchItemPage")
    public String getSearchItemPage(HttpServletRequest request, Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        restTemplate = new RestTemplate();
        String itemResourceUrl = null;

        if(keyword != null){
            itemResourceUrl = "http://localhost:" + request.getLocalPort() + "/warehouse/searchItem/" + keyword;
        }else{
            itemResourceUrl = "http://localhost:" + request.getLocalPort() + "/warehouse/getAllItems/";
        }

        List<Item> response = restTemplate.getForObject(
                itemResourceUrl,
                List.class
        );

        model.addAttribute("items", response);
        model.addAttribute("itemCount", response.size());

        return "search_items_page.html";
    }

    @GetMapping("/orderPage")
    public String getOrderPage(HttpServletRequest request, Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        restTemplate = new RestTemplate();
        String itemResourceUrl = null;

        if(keyword != null){
            itemResourceUrl = "http://localhost:" + request.getLocalPort() + "/warehouse/searchItem/" + keyword;
        }else{
            itemResourceUrl = "http://localhost:" + request.getLocalPort() + "/warehouse/getAllItems/";
        }

        List<Item> response = restTemplate.getForObject(
                itemResourceUrl,
                List.class
        );

        model.addAttribute("items", response);
        model.addAttribute("itemCount", response.size());

        return "search_items_page.html";
    }

    @PostMapping("/postOrderPage")
    public String postOrderPage(HttpServletRequest request, Model model, @RequestParam(name = "keyword", required = false) String keyword) {
        restTemplate = new RestTemplate();
        String itemResourceUrl = null;

        if(keyword != null){
            itemResourceUrl = "http://localhost:" + request.getLocalPort() + "/warehouse/searchItem/" + keyword;
        }else{
            itemResourceUrl = "http://localhost:" + request.getLocalPort() + "/warehouse/getAllItems/";
        }

        List<Item> response = restTemplate.getForObject(
                itemResourceUrl,
                List.class
        );

        model.addAttribute("items", response);
        model.addAttribute("itemCount", response.size());

        return "post_order_item_page.html";
    }

}
