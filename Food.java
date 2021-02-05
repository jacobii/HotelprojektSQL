package com.company;

import java.io.Serializable;

public class Food implements Serializable {
    private String typeOfFood;
    private int price;
    private int quantity;


    public Food(String name, int price, int quantity) {
        this.typeOfFood = name;
        this.price = price;

    }


    public String getTypeOfFood() {
        return typeOfFood;
    }

    public void setTypeOfFood(String typeOfFood) {
        this.typeOfFood = typeOfFood;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Food{" +
                "name='" + typeOfFood + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }


}
