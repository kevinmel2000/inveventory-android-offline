package com.hrtz.pos.modal;

/**
 * Created by harit on 8/21/2017.
 */

public class Sales_Inventory {
    private long id_inventory;
    private int count;
    private int total;
    private String inventory;

    public void setId_inventory(long id_inventory) {
        this.id_inventory = id_inventory;
    }

    public String getInventory() {
        return inventory;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }

    /**
     *
     * @param id_inventory id of fragment_inventory item
     * @param count number of item bought
     * @param total total number
     */
    public Sales_Inventory(long id_inventory, int count, int total) {
        this.id_inventory = id_inventory;
        this.count = count;
        this.total = total;
    }

    public Sales_Inventory() {

    }

    public long getId_inventory() {
        return id_inventory;
    }

    public void setId_inventory(int id_inventory) {
        this.id_inventory = id_inventory;
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
}
