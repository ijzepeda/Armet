package com.ijzepeda.armet.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Day implements Parcelable{
    String dayId;
    String date;
    ArrayList<String> tasksIds=new ArrayList<>();
    ArrayList<String> clientServiceIds =new ArrayList<>();
    String userId;

    public Day() {
    }

    public Day(String dayId, String date, ArrayList<String> tasksIds, ArrayList<String> clientServiceIds, String userId) {
        this.dayId = dayId;
        this.date = date;
        this.tasksIds = tasksIds;
        this.clientServiceIds = clientServiceIds;
        this.userId = userId;
    }

    protected Day(Parcel in) {
        dayId = in.readString();
        date = in.readString();
        tasksIds = in.createStringArrayList();
        clientServiceIds = in.createStringArrayList();
        userId = in.readString();
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getTasksIds() {
        return tasksIds;
    }

    public void setTask(String taskid){
        tasksIds.add(taskid);
    }
    public void setTasksIds(ArrayList<String> tasksIds) {
        this.tasksIds = tasksIds;
    }

    public ArrayList<String> getClientServiceIds() {
        return clientServiceIds;
    }

    public void setClientServiceIds(ArrayList<String> clientServiceIds) {
        this.clientServiceIds = clientServiceIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dayId);
        dest.writeString(date);
        dest.writeStringList(tasksIds);
        dest.writeStringList(clientServiceIds);
        dest.writeString(userId);
    }
}
