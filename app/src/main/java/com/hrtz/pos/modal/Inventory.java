package com.hrtz.pos.modal;

import java.io.Serializable;

/**
 * Created by harit on 8/21/2017.
 */

public class Inventory implements Serializable{
    public static final String BUNDLE_TAG = "inventoryObject";
    private int id;
    private String name;
    private int stock, price;

    public Inventory() {
    }

    /**
     *
     * @param name nama barang fragment_inventory
     * @param stock stock awal barang
     * @param price harga barang
     */
    public Inventory(int id, String name, int stock, int price) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
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

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return getName();
    }

}
