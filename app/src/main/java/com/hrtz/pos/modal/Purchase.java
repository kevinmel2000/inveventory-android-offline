package com.hrtz.pos.modal;

import java.io.Serializable;
import java.util.List;

/**
 * Created by harit on 8/21/2017.
 */

public class Purchase implements Serializable {
    public static final String BUNDLE_TAG = "purchaseObject";
    private long id;
    private String created_at;
    private int total;
    List<Purchase_Inventory> purchase_inventoryList;

    public Purchase(int total) {
        this.total = total;
    }

    public Purchase() {

    }

    public List<Purchase_Inventory> getPurchase_inventoryList() {
        return purchase_inventoryList;
    }

    public void setPurchase_inventoryList(List<Purchase_Inventory> purchase_inventoryList) {
        this.purchase_inventoryList = purchase_inventoryList;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Purchase sales = (Purchase) o;
        return (sales.getId() == this.getId() && this.getCreated_at() == sales.getCreated_at());
    }

    @Override
    public int hashCode() {
        int result = id != 0 ? created_at.hashCode() : 0;
        result = 31 * result + (id != 0 ? created_at.hashCode() : 0);
        return result;
    }
}
