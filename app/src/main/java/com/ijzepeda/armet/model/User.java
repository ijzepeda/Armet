package com.ijzepeda.armet.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
/**
 *
 * http://www.mocky.io/v2/5b26f8cb3000002c00ee27dd
 *
 * {
 users:
 [
 {
 id:123,
 firsName:"Ivan",
 lastName:"Zepeda",
 phone:"123-456-7890"
 email:"ijzepeda@hotmail.com",
 password:"123asd"
 position:"developer"
 },
 {
 id:1,
 firsName:"Alexis",
 lastName:"Ramirez",
 phone:"123-456-7890"
 email:"alexis@armet.com",
 password:"123asd"
 position:"owner"
 },
 {
 id:2,
 firsName:"Juan",
 lastName:"Lopez",
 phone:"123-456-7890"
 email:"juan@lopez.com",
 password:"123asd"
 position:"technician"
 }
 ]

 }*/
    String id;
    String firstName;
    String lastName;
    String email;
    String phone;
    String imageUrl;
    String position ="technician";

    public User() {
    }

    public User(String id, String firstName, String lastName, String email, String phone, String imageUrl, String position) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.imageUrl = imageUrl;
        this.position = position;
    }

    protected User(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        phone = in.readString();
        imageUrl = in.readString();
        position = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(imageUrl);
        dest.writeString(position);
    }
}
