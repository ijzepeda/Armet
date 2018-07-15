package com.ijzepeda.armet.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SpreasheetDay implements Parcelable {

    String id;
    String date;
    String userId;
    String userName;
    String taskAction;
    String startTime;
    String place;
    String clientName;
    String tecName1;
    String tecName2;
    String tecName3;
    String duration;
//    String servicio;//[grupo de objetos
    ArrayList<Servicio> servicios;

    public SpreasheetDay() {
    }

    public SpreasheetDay(String id, String date, String userId, String userName, String taskAction, String startTime, String place, String clientName, String tecName1, String tecName2, String tecName3, String duration) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.userName = userName;
        this.taskAction = taskAction;
        this.startTime = startTime;
        this.place = place;
        this.clientName = clientName;
        this.tecName1 = tecName1;
        this.tecName2 = tecName2;
        this.tecName3 = tecName3;
        this.duration = duration;
    }

    public SpreasheetDay(String id, String date, String userId, String userName, String taskAction, String startTime, String place, String clientName, String tecName1, String tecName2, String tecName3, String duration, ArrayList<Servicio> servicios) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.userName = userName;
        this.taskAction = taskAction;
        this.startTime = startTime;
        this.place = place;
        this.clientName = clientName;
        this.tecName1 = tecName1;
        this.tecName2 = tecName2;
        this.tecName3 = tecName3;
        this.duration = duration;
        this.servicios = servicios;
    }

    protected SpreasheetDay(Parcel in) {
        id = in.readString();
        date = in.readString();
        userId = in.readString();
        userName = in.readString();
        taskAction = in.readString();
        startTime = in.readString();
        place = in.readString();
        clientName = in.readString();
        tecName1 = in.readString();
        tecName2 = in.readString();
        tecName3 = in.readString();
        duration = in.readString();
        servicios = in.createTypedArrayList(Servicio.CREATOR);
    }

    public static final Creator<SpreasheetDay> CREATOR = new Creator<SpreasheetDay>() {
        @Override
        public SpreasheetDay createFromParcel(Parcel in) {
            return new SpreasheetDay(in);
        }

        @Override
        public SpreasheetDay[] newArray(int size) {
            return new SpreasheetDay[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTaskAction() {
        return taskAction;
    }

    public void setTaskAction(String taskAction) {
        this.taskAction = taskAction;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTecName1() {
        return tecName1;
    }

    public void setTecName1(String tecName1) {
        this.tecName1 = tecName1;
    }

    public String getTecName2() {
        return tecName2;
    }

    public void setTecName2(String tecName2) {
        this.tecName2 = tecName2;
    }

    public String getTecName3() {
        return tecName3;
    }

    public void setTecName3(String tecName3) {
        this.tecName3 = tecName3;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public ArrayList<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(ArrayList<Servicio> servicios) {
        this.servicios = servicios;
    }

    public Servicio getService(int id){
        return  servicios.get(id);
    }
    public void setServiceio(Servicio servicio){
        servicios.add(servicio);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(date);
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(taskAction);
        dest.writeString(startTime);
        dest.writeString(place);
        dest.writeString(clientName);
        dest.writeString(tecName1);
        dest.writeString(tecName2);
        dest.writeString(tecName3);
        dest.writeString(duration);
        dest.writeTypedList(servicios);
    }
}
