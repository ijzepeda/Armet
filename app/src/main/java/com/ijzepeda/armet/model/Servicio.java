package com.ijzepeda.armet.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Servicio implements Parcelable {

    String id;
    String clientId;
    String clientName;
    String name;
    int product_id; //este es un array de products_id
    String status;
    ArrayList<String> productsId;// this way I will only edit in one place, and then just request it
    ArrayList<Product> products;
/**
 * rel cliente-servicio:
 * cliente_id
 * servicio_id
 *
 *
 *
 *
 * */
    public Servicio() {
    }

    public Servicio(String id, String clientId, String clientName, String name, int product_id, String status, ArrayList<String> productsId, ArrayList<Product> products) {
        this.id = id;
        this.clientId = clientId;
        this.clientName = clientName;
        this.name = name;
        this.product_id = product_id;
        this.status = status;
        this.productsId = productsId;
        this.products = products;
    }

    public Servicio(String id, String clientId, String name, int product_id, String status, ArrayList<String> productsId, ArrayList<Product> products) {
        this.id = id;
        this.clientId = clientId;
        this.name = name;
        this.product_id = product_id;
        this.status = status;
        this.productsId = productsId;
        this.products = products;
    }

    protected Servicio(Parcel in) {
        id = in.readString();
        clientId = in.readString();
        name = in.readString();
        product_id = in.readInt();
        status = in.readString();
        productsId = in.createStringArrayList();
        products = in.createTypedArrayList(Product.CREATOR);
    }

    public static final Creator<Servicio> CREATOR = new Creator<Servicio>() {
        @Override
        public Servicio createFromParcel(Parcel in) {
            return new Servicio(in);
        }

        @Override
        public Servicio[] newArray(int size) {
            return new Servicio[size];
        }
    };

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getProductsId() {
        return productsId;
    }

    public void setProductsId(ArrayList<String> productsId) {
        this.productsId = productsId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(clientId);
        dest.writeString(clientName);
        dest.writeString(name);
        dest.writeInt(product_id);
        dest.writeString(status);
        dest.writeStringList(productsId);
        dest.writeTypedList(products);
    }
}
