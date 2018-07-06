package com.ijzepeda.armet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Servicio implements Parcelable {

    int id;
    int clientId;
    String name;
    int product_id; //este es un array de products_id
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
    public Servicio() {
    }

    public Servicio(int id, int clientId, String name, int product_id, String status) {
        this.id = id;
        this.clientId = clientId;
        this.name = name;
        this.product_id = product_id;
        this.status = status;
    }

    protected Servicio(Parcel in) {
        id = in.readInt();
        clientId = in.readInt();
        name = in.readString();
        product_id = in.readInt();
        status = in.readString();
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
