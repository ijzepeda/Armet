package com.ijzepeda.armet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
    String id;
    String name;
    String description;
    String factura;
    int qty;
    int localQty; //never upload ON create

    public Product() {
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getLocalQty() {
        return localQty;
    }

    public void setLocalQty(int localQty) {
        this.localQty = localQty;
    }

    public void addLocalQty(int addQty) {
        this.localQty += addQty;
    }

    public Product(String id, String name, String description, String factura, int qty) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.factura = factura;
        this.qty = qty;
    }

    public Product(String id, String name, String description, String factura, int qty, int localQty) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.factura = factura;
        this.qty = qty;
        this.localQty = localQty;
    }

    public Product(String id, String name, String description, String factura) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.factura = factura;
    }

    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        factura = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFactura() {
        return factura;
    }

    public void setFactura(String factura) {
        this.factura = factura;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(factura);
    }
}
