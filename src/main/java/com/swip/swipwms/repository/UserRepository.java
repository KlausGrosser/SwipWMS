package com.swip.swipwms.repository;

import com.swip.swipwms.model.Admin;
import com.swip.swipwms.model.Employee;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Data Repository
 *
 * @author pujanov
 *
 */
public class UserRepository {

    //Fields:
    private static final List<Employee> EMPLOYEE_LIST = new ArrayList<>();

    private static final List<Admin> ADMIN_LIST = new ArrayList<>();

    //Static methods:
    /**
     * Load employee, records from the personnel.json file
     */
    static {
        // System.out.println("Loading items");
        BufferedReader reader = null;
        try {
            EMPLOYEE_LIST.clear();

            reader = new BufferedReader(new FileReader("data/personnel.json"));
            Object data = JSONValue.parse(reader);
            if (data instanceof JSONArray) {
                JSONArray dataArray = (JSONArray) data;
                for (Object obj : dataArray) {
                    if (obj instanceof JSONObject) {
                        JSONObject jsonData = (JSONObject) obj;
                        String userName = jsonData.get("user_name").toString();
                        String password = jsonData.get("password").toString();
                        String role = jsonData.get("role").toString();

                        if(role.equals("ADMIN")){
                            Admin admin = new Admin(userName, password);
                            ADMIN_LIST.add(admin);
                        }else {
                            Employee employee = new Employee(userName, password);
                            EMPLOYEE_LIST.add(employee);
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

    //Getters:
    /**
     * Get All persons
     *
     * @return
     */
    public static List<Employee> getAllEmployees() {
        return EMPLOYEE_LIST;
    }

    public static List<Admin> getAllAdmins() {
        return ADMIN_LIST;
    }

    //Public methods:
    public static boolean isUserValid(String userName, String password) {
        for(Employee employee : getAllEmployees()) {
            if(isUserEmployee(userName)) {
                if(password.equals(employee.getPassword())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isUserEmployee(String name) {
        for(Employee employee : getAllEmployees()) {
            if(employee.getName().equals(name))return true;
        }
        return false;
    }

    public static boolean isUserAdmin(String name) {
        for(Admin admin : getAllAdmins()) {
            if(admin.getName().equals(name))return true;
        }
        return false;
    }

    public static boolean isAdminValid(String userName, String password) {
        for(Admin admin : getAllAdmins()) {
            if(isUserAdmin(userName)) {
                if(password.equals(admin.getPassword())) {
                    return true;
                }
            }
        }
        return false;
    }

}