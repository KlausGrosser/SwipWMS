package com.swip.swipwms.controller;

import com.swip.swipwms.model.Employee;
import com.swip.swipwms.model.LoginDTO;
import com.swip.swipwms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/getAllEmployees")
    public List<Employee> getAllEmployees(){
        return userService.getAllEmployees();
    }

    @GetMapping("/getAllAdmins")
    public List<Employee> getAllAdmins(){
        return userService.getAllAdmins();
    }

    @GetMapping("/employee/login")
    public boolean isUserValid(@RequestBody LoginDTO employee){
        return userService.isUserValid(employee.getUsername(), employee.getPassword());
    }

    @GetMapping("/admin/login")
    public boolean isAdminValid(@RequestBody LoginDTO admin){
        return userService.isAdminValid(admin.getUsername(), admin.getPassword());
    }
}
