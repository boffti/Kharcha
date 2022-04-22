package com.example.kharcha;

public class ExpenseModel {
    private int id;
    private String name;
    private Float amount;
    private String category;
    private String addedOn;

    //constructor
    public ExpenseModel(int id, String name, Float amount, String category, String addedOn) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.addedOn = addedOn;
    }

    public ExpenseModel() {
    }

    //toString
    @Override
    public String toString() {
        return "ExpenseModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", addedOn='" + addedOn + '\'' +
                '}';
    }

    // Getters & Setters
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

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }
}
