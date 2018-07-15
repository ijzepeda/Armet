package com.ijzepeda.armet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Task implements Parcelable {
    String id;
    int userId;
    String address;
    String client;
    String action;

    String tec1Name;
    int tec1Id;

    String tec2Name;
    int tec2Id;

    String tec3Name;
    int tec3Id;

    String finalTime;
    String startingTime;

    String date;


    public Task() {
    }

    public Task(String id, int userId, String address, String client, String action, String tec1Name, int tec1Id, String tec2Name, int tec2Id, String tec3Name, int tec3Id, String finalTime, String startingTime, String date) {
        this.id = id;
        this.userId = userId;
        this.address = address;
        this.client = client;
        this.action = action;
        this.tec1Name = tec1Name;
        this.tec1Id = tec1Id;
        this.tec2Name = tec2Name;
        this.tec2Id = tec2Id;
        this.tec3Name = tec3Name;
        this.tec3Id = tec3Id;
        this.finalTime = finalTime;
        this.startingTime = startingTime;
        this.date = date;
    }

    protected Task(Parcel in) {
        id = in.readString();
        userId = in.readInt();
        address = in.readString();
        client = in.readString();
        action = in.readString();
        tec1Name = in.readString();
        tec1Id = in.readInt();
        tec2Name = in.readString();
        tec2Id = in.readInt();
        tec3Name = in.readString();
        tec3Id = in.readInt();
        finalTime = in.readString();
        startingTime = in.readString();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(userId);
        dest.writeString(address);
        dest.writeString(client);
        dest.writeString(action);
        dest.writeString(tec1Name);
        dest.writeInt(tec1Id);
        dest.writeString(tec2Name);
        dest.writeInt(tec2Id);
        dest.writeString(tec3Name);
        dest.writeInt(tec3Id);
        dest.writeString(finalTime);
        dest.writeString(startingTime);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTec1Name() {
        return tec1Name;
    }

    public void setTec1Name(String tec1Name) {
        this.tec1Name = tec1Name;
    }

    public int getTec1Id() {
        return tec1Id;
    }

    public void setTec1Id(int tec1Id) {
        this.tec1Id = tec1Id;
    }

    public String getTec2Name() {
        return tec2Name;
    }

    public void setTec2Name(String tec2Name) {
        this.tec2Name = tec2Name;
    }

    public int getTec2Id() {
        return tec2Id;
    }

    public void setTec2Id(int tec2Id) {
        this.tec2Id = tec2Id;
    }

    public String getTec3Name() {
        return tec3Name;
    }

    public void setTec3Name(String tec3Name) {
        this.tec3Name = tec3Name;
    }

    public int getTec3Id() {
        return tec3Id;
    }

    public void setTec3Id(int tec3Id) {
        this.tec3Id = tec3Id;
    }

    public String getFinalTime() {
        return finalTime;
    }

    public void setFinalTime(String finalTime) {
        this.finalTime = finalTime;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
