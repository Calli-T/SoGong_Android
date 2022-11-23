package com.example.sogong.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.sql.Ref;

public class Refrigerator implements Parcelable {
    @SerializedName("id")
    private int refrigerator_id;
    @SerializedName("name")
    private String name;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("unit")
    private String unit;
    @SerializedName("amount")
    private float ammount;
    @SerializedName("expiry_date")
    private String expiry_date;

    public Refrigerator() {}
    public Refrigerator(int refrigerator_id, String name, String nickname, String unit, float ammount, String expiry_date) {
        this.refrigerator_id = refrigerator_id;
        this.name = name;
        this.nickname = nickname;
        this.unit = unit;
        this.ammount = ammount;
        this.expiry_date = expiry_date;
    }

    protected Refrigerator(Parcel in) {
        refrigerator_id = in.readInt();
        name = in.readString();
        nickname = in.readString();
        unit = in.readString();
        ammount = in.readFloat();
        expiry_date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(refrigerator_id);
        dest.writeString(name);
        dest.writeString(nickname);
        dest.writeString(unit);
        dest.writeFloat(ammount);
        dest.writeString(expiry_date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Refrigerator> CREATOR = new Creator<Refrigerator>() {
        @Override
        public Refrigerator createFromParcel(Parcel in) {
            return new Refrigerator(in);
        }

        @Override
        public Refrigerator[] newArray(int size) {
            return new Refrigerator[size];
        }
    };

    public int getRefrigerator_id() {
        return refrigerator_id;
    }

    public void setRefrigerator_id(int refrigerator_id) {
        this.refrigerator_id = refrigerator_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public float getAmmount() {
        return ammount;
    }

    public void setAmmount(float ammount) {
        this.ammount = ammount;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    @Override
    public String toString() {
        return "Refrigerator{" +
                "refrigerator_id=" + refrigerator_id +
                ", name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", unit='" + unit + '\'' +
                ", ammount=" + ammount +
                ", expiry_date='" + expiry_date + '\'' +
                '}';
    }
}
