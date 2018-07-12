package com.ijzepeda.armet.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataSingleton {
    private static final String TAG = "DataSingleton";
    private static DataSingleton INSTANCE = null;

    private Map<String, Client> clients = new HashMap<>();
    private Map<String, Product> products = new HashMap<>();
    private Map<String, Servicio> services = new HashMap<>();
    private Map<String, Task> tasks = new HashMap<>();
    private Map<String, User> users = new HashMap<>();
private static String PREFS_NAME = "Prefs";

public void update(Context context){
    Gson gson = new Gson();
    String json = gson.toJson(INSTANCE);

    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
      editor.putString("singleton", json);
    editor.apply();

}
public static DataSingleton loadSingleton(Context context){
    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
    String json= settings.getString("singleton", "");

    Gson gson = new Gson();

//    if(gson.fromJson(json, DataSingleton.class)!=null)
    if(json.equals(""))
        return new DataSingleton();
        else
        return gson.fromJson(json, DataSingleton.class);


}
    // other instance variables can be here

    private DataSingleton() {
    }


    public static DataSingleton getInstance(Context context) {

        if (INSTANCE == null) {
           INSTANCE= loadSingleton(context);
            // INSTANCE = new DataSingleton();
        }
        return (INSTANCE);
    }

    public static DataSingleton getInstance() {

        if (INSTANCE == null) {
//           INSTANCE= loadSingleton(context);
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

public Servicio getService(String id){
        return services.get(id);
}

    public Map<String, Servicio> getServices() {
        return services;
    }

    public ArrayList<Servicio> getServicesList() {

        ArrayList<Servicio> productList = new ArrayList<>();
        for (Map.Entry<String, Servicio> entry : services.entrySet()) {
            productList.add(entry.getValue());
        }

        return productList;
    }

    public void setService(Servicio service){
        services.put(service.getId(),service);
    }
    public void setServices(Map<String, Servicio> services) {
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
