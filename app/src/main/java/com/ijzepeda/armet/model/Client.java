package com.ijzepeda.armet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Client implements Parcelable {
    String id;
    String name;
    String address;
    String location;
    String phone;
    String contactName;

    public Client() {
    }

    public Client(String id, String name, String address, String location, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.location = location;
        this.phone = phone;
    }

    public Client(String id, String name, String address, String location, String phone, String contactName) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.location = location;
        this.phone = phone;
        this.contactName = contactName;
    }

    protected Client(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        location = in.readString();
        phone = in.readString();
        contactName = in.readString();
    }

    public static final Creator<Client> CREATOR = new Creator<Client>() {
        @Override
        public Client createFromParcel(Parcel in) {
            return new Client(in);
        }

        @Override
        public Client[] newArray(int size) {
            return new Client[size];
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(location);
        dest.writeString(phone);
        dest.writeString(contactName);
    }
}
