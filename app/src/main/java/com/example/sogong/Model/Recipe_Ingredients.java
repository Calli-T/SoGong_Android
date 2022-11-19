package com.example.sogong.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Recipe_Ingredients implements Parcelable {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("unit")
    private String unit;
    @SerializedName("post_id")
    private int post_id;
    @SerializedName("amount")
    private float amount;

    public Recipe_Ingredients() {}
    public Recipe_Ingredients(int id, String name, String unit, int post_id, float amount) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.post_id = post_id;
        this.amount = amount;
    }

    protected Recipe_Ingredients(Parcel in) {
        id = in.readInt();
        name = in.readString();
        unit = in.readString();
        post_id = in.readInt();
        amount = in.readFloat();
    }

    public static final Creator<Recipe_Ingredients> CREATOR = new Creator<Recipe_Ingredients>() {
        @Override
        public Recipe_Ingredients createFromParcel(Parcel in) {
            return new Recipe_Ingredients(in);
        }

        @Override
        public Recipe_Ingredients[] newArray(int size) {
            return new Recipe_Ingredients[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "ingre_id=" + id +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", post_id=" + post_id +
                ", ammount=" + amount +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(unit);
        parcel.writeInt(post_id);
        parcel.writeFloat(amount);
    }
}