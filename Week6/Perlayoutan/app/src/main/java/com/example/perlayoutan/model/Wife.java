package com.example.perlayoutan.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Wife implements Parcelable {
    private String name;
    private String description;
    private int age;
    private String origin;
    private int profileImageResId;

    public Wife(String name, String description, int age, String origin, int profileImageResId) {
        this.name = name;
        this.description = description;
        this.age = age;
        this.origin = origin;
        this.profileImageResId = profileImageResId;
    }

    protected Wife(Parcel in) {
        name = in.readString();
        description = in.readString();
        age = in.readInt();
        origin = in.readString();
        profileImageResId = in.readInt();
    }

    // Getters and Setters
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getProfileImageResId() {
        return profileImageResId;
    }

    public void setProfileImageResId(int profileImageResId) {
        this.profileImageResId = profileImageResId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(age);
        dest.writeString(origin);
        dest.writeInt(profileImageResId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Wife> CREATOR = new Creator<Wife>() {
        @Override
        public Wife createFromParcel(Parcel in) {
            return new Wife(in);
        }

        @Override
        public Wife[] newArray(int size) {
            return new Wife[size];
        }
    };
}