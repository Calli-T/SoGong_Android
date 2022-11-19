package com.example.sogong.Model;

import com.google.gson.annotations.SerializedName;

public class Recipe_Ingredients {
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
}