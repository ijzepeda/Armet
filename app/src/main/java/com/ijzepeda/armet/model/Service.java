package com.ijzepeda.armet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Service implements Parcelable {

    int id;
    int clientId;
    String name;
    int product_id;
    String status;
/**
 * rel cliente-servicio:
 * cliente_id
 * servicio_id
 *
 *
 *
 *
 * */
    public Service() {
    }

    public Service(int id, int clientId, String name, int product_id, String status) {
        this.id = id;
        this.clientId = clientId;
        this.name = name;
        this.product_id = product_id;
        this.status = status;
    }

    protected Service(Parcel in) {
        id = in.readInt();
        clientId = in.readInt();
        name = in.readString();
        product_id = in.readInt();
        status = in.readString();
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(clientId);
        dest.writeString(name);
        dest.writeInt(product_id);
        dest.writeString(status);
    }
}
