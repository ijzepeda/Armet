package com.ijzepeda.armet.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSingleton {
    private static final String TAG = "DataSingleton";
    private static DataSingleton INSTANCE = null;

    private Map<String, Client> clients = new HashMap<>();
    private Map<String, Product> products = new HashMap<>();
    private Map<String, Service> services = new HashMap<>();
    private Map<String, Task> tasks = new HashMap<>();
    private Map<String, User> users = new HashMap<>();


    // other instance variables can be here

    private DataSingleton() {
    }

    ;

    public static DataSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataSingleton();
        }
        return (INSTANCE);
    }

    public Map<String, Client> getClients() {
        return clients;
    }

    public void setClients(Map<String, Client> clients) {
        this.clients = clients;
    }

    public Product getProduct(String id) {
        return products.get(id);
    }

    public Map<String, Product> getProductsMap() {
        return products;
    }

    public ArrayList<Product> getProductsList() {

        ArrayList<Product> productList = new ArrayList<>();
        for (Map.Entry<String, Product> entry : products.entrySet()) {
            productList.add(entry.getValue());
        }

        return productList;
    }

    public void setProducts(Product product) {
        Log.e(TAG, "singleton setProducts.getId(): " + product.getId());
        products.put(product.getId(), product);

        Log.e(TAG, "singleton getProduct(products.getid()).getId: " + getProduct(product.getId()).getId());
//        this.products = products;
    }

    public ArrayList<String> getProductNamesList() {
        ArrayList<String> productNamesList = new ArrayList<>();
        for (Map.Entry<String, Product> entry : products.entrySet()) {
//            Product product = entry.getValue();
//            productNamesList.add(product.getName());
            productNamesList.add(entry.getValue().getName());
            //falta el ID :  va a hacer falta usar un ad
        }
        return productNamesList;
    }


    public Map<String, Service> getServices() {
        return services;
    }

    public void setServices(Map<String, Service> services) {
        this.services = services;
    }

    public Map<String, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, Task> tasks) {
        this.tasks = tasks;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }
}
