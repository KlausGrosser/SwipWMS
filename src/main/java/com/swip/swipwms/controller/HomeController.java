package com.swip.swipwms.controller;

import com.swip.swipwms.model.Item;
import com.swip.swipwms.model.OrderItem;
import com.swip.swipwms.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {

    RestTemplate restTemplate;
    WarehouseService warehouseService;

    @Autowired
    public HomeController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/")
    public String getIndex() {
        return "index.html";
    }

    @RequestMapping("/loginPage")
    public String login(@RequestParam(value = "loginFailed", defaultValue = "false") Boolean loginFailed, Model model) {
        model.addAttribute("loginFailed", loginFailed);
        return "login.html";
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

    @RequestMapping(value = "/searchItemPage", method = RequestMethod.GET)
    public String getSearchItemPage(HttpServletRequest request, Model model, @RequestParam(value = "keyword", defaultValue = "") String keyword) {

            restTemplate = new RestTemplate();
            String itemResourceUrl = null;

        if(!keyword.isBlank()){
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
    public String getOrderPage(Model model, @RequestParam(value = "state") String state, @RequestParam(value = "category") String category) {
        String itemName = state + " " + category;

        List<Item> availableItems = warehouseService.findItems(itemName);

        model.addAttribute("availableItems", availableItems);
        model.addAttribute("amount", availableItems.size());
        model.addAttribute("orderItem", new OrderItem(itemName, 1));

        return "order_item_page.html";
    }

    @PostMapping("/orderPage")
    public String postOrderPage(@ModelAttribute("orderItem") @Valid OrderItem orderItem, HttpServletRequest request, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "order_item_page.html";
        }

        restTemplate = new RestTemplate();

        String itemResourceUrl = "http://localhost:" + request.getLocalPort() + "/warehouse/removeItems/" + orderItem.getItemName() + "/" + orderItem.getAmount();

        List<Item> response = restTemplate.getForObject(
                itemResourceUrl,
                List.class
        );

        model.addAttribute("orderedItems", orderItem.getItemName());
        model.addAttribute("amount", orderItem.getAmount());
        return "post_order_item_page";
    }

}
