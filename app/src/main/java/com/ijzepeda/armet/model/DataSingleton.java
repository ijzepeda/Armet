package com.ijzepeda.armet.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataSingleton implements Parcelable {
    private static final String TAG = "DataSingleton";
    private static DataSingleton INSTANCE = null;
private Day day;
    private Map<String, Client> clients = new HashMap<>();
    private Map<String, Product> products = new HashMap<>();
    private Map<String, Servicio> services = new HashMap<>();
    private Map<String, Task> tasks = new HashMap<>();
    private Map<String, User> users = new HashMap<>();
    private User user;
private static String PREFS_NAME = "Prefs";

    protected DataSingleton(Parcel in) {
        day = in.readParcelable(Day.class.getClassLoader());
        user = in.readParcelable(User.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(day, flags);
        dest.writeParcelable(user, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DataSingleton> CREATOR = new Creator<DataSingleton>() {
        @Override
        public DataSingleton createFromParcel(Parcel in) {
            return new DataSingleton(in);
        }

        @Override
        public DataSingleton[] newArray(int size) {
            return new DataSingleton[size];
        }
    };

    public void update(Context context){
    Log.e(TAG, "update singleton, saving it for later: " );
    Gson gson = new Gson();
    String json = gson.toJson(INSTANCE);

    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
      editor.putString("singleton", json);
    editor.apply();

}

public boolean clearSingleton(Context context){
    Log.e(TAG, "clearSingleton: " );
    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.clear();
    editor.apply();
    return true;
}


public static DataSingleton loadSingleton(Context context){
    SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
    String json= settings.getString("singleton", "");
    Gson gson = new Gson();

//    if(gson.fromJson(json, DataSingleton.class)!=null)
    if(json.equals("")) {
        return getInstance();//new DataSingleton();
    }else {
        INSTANCE=gson.fromJson(json, DataSingleton.class);
        return INSTANCE;


    }
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

    public Day getDay() {
        Log.e(TAG, "getDay: loading day from singleton" );
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
    public void updateDay(Day day){
    this.day=day;

    }
    public Day restoreDay(){
    return day;
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
        Log.e(TAG, "getServicesList size: "+services.size() );
        for (Map.Entry<String, Servicio> entry : services.entrySet()) {
            productList.add(entry.getValue());
        }

        return productList;
    }

    public void setService(Servicio service){
        Log.e(TAG, "setService: "+service.getId() );
        if(services.isEmpty())
            services=new HashMap<>();
        services.put(service.getId(),service);

//        Log.e(TAG, "setService saved on singleton: "+services.get(service).getId() );//todo checar, da error. nunca se crea, da nulo en services
    }
    public void setServices(Map<String, Servicio> services) {
        this.services = services;
    }

    public void removeService(Servicio servicio){
        services.remove(servicio);//.getId()
    }
    public void removeTask(Task task){
        tasks.remove(task.id);
    }


    public Map<String, Task> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, Task> tasks) {
        this.tasks = tasks;
    }
    public void setTask(Task task){
        tasks.put(task.id, task);
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public User getUser(){
        return user;
    }
}
