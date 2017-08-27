package com.hrtz.pos.modal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harit on 8/21/2017.
 */

public class Sales implements Serializable {
    private int id;
    private String created_at;
    private int total;
    List<Sales_Inventory> sales_inventoryList;

    public Sales(int total) {
        this.total = total;
    }

    public Sales() {

    }

    public List<Sales_Inventory> getSales_inventoryList() {
        return sales_inventoryList;
    }

    public void setSales_inventoryList(List<Sales_Inventory> sales_inventoryList) {
        this.sales_inventoryList = sales_inventoryList;
    }

    public int getId() {
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
}
