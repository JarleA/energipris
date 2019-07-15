package com.jarlerocks;

public class PricePoint {
    private String time;
    private float price;

    PricePoint(String time, float price) {
        this.time = time;
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public float getPrice() {
        return price;
    }
}
