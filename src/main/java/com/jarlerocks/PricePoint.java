package com.jarlerocks;

public class PricePoint implements Comparable<PricePoint> {
    private String time;
    private Float price;

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

    @Override
    public int compareTo(PricePoint o) {
        return this.price.compareTo(o.price);
    }
}
