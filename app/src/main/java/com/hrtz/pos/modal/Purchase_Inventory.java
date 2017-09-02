package com.hrtz.pos.modal;

/**
 * Created by harit on 9/2/2017.
 */

public class Purchase_Inventory {

    private int count;
    private int total;
    private Inventory inventory;

    public Purchase_Inventory(int count, int total, Inventory inventory) {
        this.count = count;
        this.total = total;
        this.inventory = inventory;
    }

    public Purchase_Inventory() {

    }


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
