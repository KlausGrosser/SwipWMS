package com.swip.swipwms.service;

import com.swip.swipwms.model.Admin;
import com.swip.swipwms.model.Employee;
import com.swip.swipwms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    public List<Employee> getAllEmployees(){
        return UserRepository.getAllEmployees();
    }

    public List<Admin> getAllAdmins(){
        return UserRepository.getAllAdmins();
    }

    public boolean isUserValid(String userName, String password){
        return UserRepository.isUserValid(userName, password);
    }

    public boolean isAdminValid(String userName, String password){
        return UserRepository.isAdminValid(userName, password);
    }
}
