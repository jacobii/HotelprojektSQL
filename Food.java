package com.company;

import java.io.Serializable;

public class Food implements Serializable {
    private String typeOfFood;
    private int price;
    private int quantity;
     //   case 1 -> price = quantity * 50;
            //case 2 -> price = quantity * 60;
           // case 3 -> price = quantity * 70;
           // case 4 -> price = quantity * 30;


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
