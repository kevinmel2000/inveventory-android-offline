package com.hrtz.pos.modal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by harit on 8/21/2017.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {


    // Logcat tag
    private static final String LOG = InventoryDbHelper.class.getName();


    public final static int DATABASE_VERSION = 4;
    public final static String DATABASE_NAME = "Inventory.db";

    //table and column for fragment_inventory
    private static final String INVENTORY_TABLE_NAME = "inventory";
    private static final String INVENTORY_COLUMN_NAME_TITLE = "title";
    private static final String INVENTORY_COLUMN_NAME_PRICE = "price";
    private static final String INVENTORY_COLUMN_NAME_STOCK = "stock";

    //table and column for sales
    private static final String SALES_TABLE_NAME = "sales";
    private static final String SALES_COLUMN_NAME_TOTAL = "total";

    //table and column for salesinventory
    private static final String SALESINVENTORY_TABLE_NAME = "sales_inventory";
    private static final String SALESINVENTORY_COLUMN_SALES_ID = "sales_id";
    private static final String SALESINVENTORY_COLUMN_INVENTORY_ID = "inventory_id";
    private static final String SALESINVENTORY_COLUMN_COUNT = "count";

    //table and column for purchase
    private static final String PURCHASE_TABLE_NAME = "purchase";
    private static final String PURCHASE_COLUMN_NAME_TOTAL = "total";

    //table and column for purchaseinventory
    private static final String PURCHASEINVENTORY_TABLE_NAME = "purchase_inventory";
    private static final String PURCHASEINVENTORY_COLUMN_PURCHASE_ID = "purchase_id";
    private static final String PURCHASEINVENTORY_COLUMN_INVENTORY_ID = "inventory_id";
    private static final String PURCHASEINVENTORY_COLUMN_COUNT = "count";


    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_CREATED_AT = "created_at";

    private static final String SQL_CREATE_INVENTORY_TABLE =
            "CREATE TABLE " + INVENTORY_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    INVENTORY_COLUMN_NAME_TITLE + " TEXT," +
                    INVENTORY_COLUMN_NAME_STOCK + " INTEGER, "+
                    INVENTORY_COLUMN_NAME_PRICE + " INTEGER, "+
                    KEY_CREATED_AT +" DATETIME)";

    private static final String SQL_CREATE_SALES_TABLE =
            "CREATE TABLE " + SALES_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    SALES_COLUMN_NAME_TOTAL + " TEXT," +
                    KEY_CREATED_AT +" DATETIME)";


    private static final String SQL_CREATE_SALES_INVENTORY_TABLE =
            "CREATE TABLE " + SALESINVENTORY_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    SALESINVENTORY_COLUMN_SALES_ID+ " INTEGER," +
                    SALESINVENTORY_COLUMN_INVENTORY_ID+ " INTEGER," +
                    SALESINVENTORY_COLUMN_COUNT + " INTEGER)";


    private static final String SQL_CREATE_PURCHASE_TABLE =
            "CREATE TABLE " + PURCHASE_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    PURCHASE_COLUMN_NAME_TOTAL + " TEXT," +
                    KEY_CREATED_AT +" DATETIME)";


    private static final String SQL_CREATE_PURCHASE_INVENTORY_TABLE =
            "CREATE TABLE " + PURCHASEINVENTORY_TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY," +
                    PURCHASEINVENTORY_COLUMN_PURCHASE_ID+ " INTEGER," +
                    PURCHASEINVENTORY_COLUMN_INVENTORY_ID+ " INTEGER," +
                    PURCHASEINVENTORY_COLUMN_COUNT + " INTEGER)";

    public InventoryDbHelper(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
        db.execSQL(SQL_CREATE_SALES_TABLE);
        db.execSQL(SQL_CREATE_SALES_INVENTORY_TABLE);
        db.execSQL(SQL_CREATE_PURCHASE_TABLE);
        db.execSQL(SQL_CREATE_PURCHASE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + INVENTORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SALES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SALESINVENTORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PURCHASE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PURCHASEINVENTORY_TABLE_NAME);
        // create new tables
        onCreate(db);
    }

    public long createInventory(Inventory inventory){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INVENTORY_COLUMN_NAME_TITLE, inventory.getName());
        values.put(INVENTORY_COLUMN_NAME_PRICE, inventory.getPrice());
        values.put(INVENTORY_COLUMN_NAME_STOCK, inventory.getStock());
        values.put(KEY_CREATED_AT, getDateTime());

        // insert row
        long tag_id = db.insert(INVENTORY_TABLE_NAME, null, values);

        return tag_id;
    }

    /**
     * getting all inventories
     * */
    public List<Inventory> getAllInventories() {
        List<Inventory> inventories = new ArrayList<Inventory>();
        String selectQuery = "SELECT  * FROM " + INVENTORY_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Inventory inventory = new Inventory();
                inventory.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                inventory.setName((c.getString(c.getColumnIndex(INVENTORY_COLUMN_NAME_TITLE))));
                inventory.setPrice(c.getInt(c.getColumnIndex(INVENTORY_COLUMN_NAME_PRICE)));
                inventory.setStock(c.getInt(c.getColumnIndex(INVENTORY_COLUMN_NAME_STOCK)));
                // adding to todo list
                inventories.add(inventory);
            } while (c.moveToNext());
        }
        return inventories;
    }

    /**
     * delete inventory records
     * @param id inventory id
     */
    public void deleteInventory(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(INVENTORY_TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.delete(SALESINVENTORY_TABLE_NAME, SALESINVENTORY_COLUMN_INVENTORY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    /*
     * Updating a inventory
     */
    public int updateInventory(Inventory inventory) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(INVENTORY_COLUMN_NAME_TITLE, inventory.getName());
        values.put(INVENTORY_COLUMN_NAME_PRICE, inventory.getPrice());
        values.put(INVENTORY_COLUMN_NAME_STOCK, inventory.getStock());
        values.put(KEY_CREATED_AT, getDateTime());

        // updating row
        return db.update(INVENTORY_TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(inventory.getId()) });
    }



    /**
     * Creating a todo
     */
    public long createSales(Sales_Inventory[] sales_inventories, int total) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SALES_COLUMN_NAME_TOTAL, total);
        values.put(KEY_CREATED_AT, getDateTime());
        // insert row
        long sales_id = db.insert(SALES_TABLE_NAME, null, values);

        // insert tag_ids
        for (Sales_Inventory si : sales_inventories) {
            createSalesInventory(sales_id, si);
        }
        return sales_id;
    }


    /**
     * Creating a todo
     */
    public long updateSales(Sales sales, Sales_Inventory[] sales_inventories, int total) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(SALESINVENTORY_TABLE_NAME, SALESINVENTORY_COLUMN_SALES_ID + " = ?",
                new String[] { String.valueOf(sales.getId()) });
        Log.v("delete", "scuces");

        ContentValues values = new ContentValues();
        values.put(SALES_COLUMN_NAME_TOTAL, total);

        // updating row
        db.update(SALES_TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(sales.getId()) });


        db.close();
        Log.v("update", "scuces");
        // insert tag_ids
        for (Sales_Inventory si : sales_inventories) {
            createSalesInventory(sales.getId(), si);
            Log.v("update sales : ", si.getInventory().getName()+ " | "+si.getCount()+"|"
                    +sales.getId()+"|"+si.getInventory().getId());
        }

        return sales.getId();
    }

    /**
     *
     * @param begin beginning date, in yyyy-MM-dd format
     * @param end end date, in yyyy-MM-dd format
     * @return all sales between dates
     */
    public List<Sales> getAllSalesBetween(String begin, String end) {
        List<Sales> sales = new ArrayList<Sales>();


        String selectQuery = "SELECT  * FROM " + SALES_TABLE_NAME +" WHERE "+KEY_CREATED_AT+" BETWEEN '"
                +begin+"' AND '"+end+" 23:59:59'";
        Log.e("queery", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Sales sale = new Sales();
                sale.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                sale.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                sale.setTotal(c.getInt(c.getColumnIndex(SALES_COLUMN_NAME_TOTAL)));
                // adding to todo list
                sales.add(sale);
            } while (c.moveToNext());
        }

        //get transactions for each sales
        for (Sales s : sales) {
            selectQuery = "SELECT * FROM " + SALESINVENTORY_TABLE_NAME +" si, "+
                    INVENTORY_TABLE_NAME+" inventory WHERE si."+SALESINVENTORY_COLUMN_SALES_ID
                    +" = "+s.getId()+" AND si."+SALESINVENTORY_COLUMN_INVENTORY_ID+" = inventory."+KEY_ID;

            Log.e(LOG, selectQuery);

            c = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            List<Sales_Inventory> salesList = new ArrayList<Sales_Inventory>();

            if (c.moveToFirst()) {
                do {
                    Sales_Inventory sale = new Sales_Inventory();
                    String nameInventory = c.getString(c.getColumnIndex(INVENTORY_COLUMN_NAME_TITLE));
                    int price = c.getInt(c.getColumnIndex(INVENTORY_COLUMN_NAME_PRICE));
                    int stock = c.getInt(c.getColumnIndex(INVENTORY_COLUMN_NAME_STOCK));
                    int id = c.getInt(c.getColumnIndex(KEY_ID));
                    sale.setInventory(new Inventory(id, nameInventory, stock, price));
                    int count = c.getInt(c.getColumnIndex(SALESINVENTORY_COLUMN_COUNT));

                    sale.setCount(count);

                    salesList.add(sale);
                } while (c.moveToNext());
            }

            s.setSales_inventoryList(salesList);
        }
        return sales;
    }


    public void deleteSales(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SALES_TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.delete(SALESINVENTORY_TABLE_NAME, SALESINVENTORY_COLUMN_SALES_ID + " = ?",
                new String[] {String.valueOf(id)});

    }
    /**
     * Creating tag
     */
    public long createSalesInventory(long sales_id, Sales_Inventory si) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SALESINVENTORY_COLUMN_SALES_ID, sales_id);
        values.put(SALESINVENTORY_COLUMN_INVENTORY_ID, si.getInventory().getId());
        values.put(SALESINVENTORY_COLUMN_COUNT, si.getCount());

        // insert row
        long tag_id = db.insert(SALESINVENTORY_TABLE_NAME, null, values);
        db.close();
        return tag_id;
    }



    /**
     * Creating a todo
     */
    public long createPurchase(Purchase_Inventory[] purchase_inventories, int total) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PURCHASE_COLUMN_NAME_TOTAL, total);
        values.put(KEY_CREATED_AT, getDateTime());
        // insert row
        long purchase_id = db.insert(PURCHASE_TABLE_NAME, null, values);

        // insert tag_ids
        for (Purchase_Inventory si : purchase_inventories) {
            createPurchaseInventory(purchase_id, si);
        }
        return purchase_id;
    }


    /**
     * Creating a todo
     */
    public long updatePurchase(Purchase purchase, Purchase_Inventory[] purchase_inventories, int total) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(PURCHASEINVENTORY_TABLE_NAME, PURCHASEINVENTORY_COLUMN_PURCHASE_ID+ " = ?",
                new String[] { String.valueOf(purchase.getId()) });


        ContentValues values = new ContentValues();
        values.put(PURCHASE_COLUMN_NAME_TOTAL, total);

        // updating row
        db.update(PURCHASE_TABLE_NAME, values, KEY_ID + " = ?",
                new String[] { String.valueOf(purchase.getId()) });


        db.close();

        // insert tag_ids
        for (Purchase_Inventory si : purchase_inventories) {
            createPurchaseInventory(purchase.getId(), si);
        }

        return purchase.getId();
    }

    /**
     *
     * @param begin beginning date, in yyyy-MM-dd format
     * @param end end date, in yyyy-MM-dd format
     * @return all sales between dates
     */
    public List<Purchase> getAllPurchasesBetween(String begin, String end) {
        List<Purchase> purchases = new ArrayList<Purchase>();


        String selectQuery = "SELECT  * FROM " + PURCHASE_TABLE_NAME +" WHERE "+KEY_CREATED_AT+" BETWEEN '"
                +begin+"' AND '"+end+" 23:59:59'";
        Log.e("queery", selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Purchase purchase = new Purchase();
                purchase.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                purchase.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                purchase.setTotal(c.getInt(c.getColumnIndex(PURCHASE_COLUMN_NAME_TOTAL)));
                // adding to todo list
                purchases.add(purchase);
            } while (c.moveToNext());
        }

        //get transactions for each sales
        for (Purchase s : purchases) {
            selectQuery = "SELECT * FROM " + PURCHASEINVENTORY_TABLE_NAME+" si, "+
                    INVENTORY_TABLE_NAME+" inventory WHERE si."+PURCHASEINVENTORY_COLUMN_PURCHASE_ID
                    +" = "+s.getId()+" AND si."+PURCHASEINVENTORY_COLUMN_INVENTORY_ID+" = inventory."+KEY_ID;

            Log.e(LOG, selectQuery);

            c = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            List<Purchase_Inventory> purchaseList = new ArrayList<Purchase_Inventory>();

            if (c.moveToFirst()) {
                do {
                    Purchase_Inventory purchase = new Purchase_Inventory();
                    String nameInventory = c.getString(c.getColumnIndex(INVENTORY_COLUMN_NAME_TITLE));
                    int price = c.getInt(c.getColumnIndex(INVENTORY_COLUMN_NAME_PRICE));
                    int stock = c.getInt(c.getColumnIndex(INVENTORY_COLUMN_NAME_STOCK));
                    int id = c.getInt(c.getColumnIndex(KEY_ID));
                    purchase.setInventory(new Inventory(id, nameInventory, stock, price));
                    int count = c.getInt(c.getColumnIndex(PURCHASEINVENTORY_COLUMN_COUNT));

                    purchase.setCount(count);

                    purchaseList.add(purchase);
                } while (c.moveToNext());
            }

            s.setPurchase_inventoryList(purchaseList);
        }
        return purchases;
    }


    public void deletePurchase(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PURCHASE_TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.delete(PURCHASEINVENTORY_TABLE_NAME, PURCHASEINVENTORY_COLUMN_PURCHASE_ID+ " = ?",
                new String[] {String.valueOf(id)});

    }
    /**
     * Creating tag
     */
    public long createPurchaseInventory(long purchase_id, Purchase_Inventory pi) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PURCHASEINVENTORY_COLUMN_PURCHASE_ID, purchase_id);
        values.put(PURCHASEINVENTORY_COLUMN_INVENTORY_ID, pi.getInventory().getId());
        values.put(PURCHASEINVENTORY_COLUMN_COUNT, pi.getCount());

        // insert row
        long tag_id = db.insert(PURCHASEINVENTORY_TABLE_NAME, null, values);
        db.close();
        return tag_id;
    }



    /**
     * getting all sales
     * */
    public List<Sales> getAllSales() {
        List<Sales> sales = new ArrayList<Sales>();
        String selectQuery = "SELECT  * FROM " + SALES_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Sales sale = new Sales();
                sale.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                sale.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
                sale.setTotal(c.getInt(c.getColumnIndex(SALES_COLUMN_NAME_TOTAL)));
                // adding to todo list
                sales.add(sale);
            } while (c.moveToNext());
        }

        //get transactions for each sales
        for (Sales s : sales) {
            selectQuery = "SELECT * FROM " + SALESINVENTORY_TABLE_NAME +" si, "+
                    INVENTORY_TABLE_NAME+" inventory WHERE si."+SALESINVENTORY_COLUMN_SALES_ID
            +" = "+s.getId()+" AND si."+SALESINVENTORY_COLUMN_INVENTORY_ID+" = inventory."+KEY_ID;

            Log.e(LOG, selectQuery);

            c = db.rawQuery(selectQuery, null);
            // looping through all rows and adding to list
            List<Sales_Inventory> salesList = new ArrayList<Sales_Inventory>();

            if (c.moveToFirst()) {
                do {
                    Sales_Inventory sale = new Sales_Inventory();
                    String nameInventory = c.getString(c.getColumnIndex(INVENTORY_COLUMN_NAME_TITLE));
                    int price = c.getInt(c.getColumnIndex(INVENTORY_COLUMN_NAME_PRICE));
                    int stock = c.getInt(c.getColumnIndex(INVENTORY_COLUMN_NAME_STOCK));
                    int id = c.getInt(c.getColumnIndex(KEY_ID));
                    sale.setInventory(new Inventory(id, nameInventory, stock, price));
                    int count = c.getInt(c.getColumnIndex(SALESINVENTORY_COLUMN_COUNT));

                    sale.setCount(count);

                    salesList.add(sale);
                } while (c.moveToNext());
            }

            s.setSales_inventoryList(salesList);
        }
        return sales;
    }

    /**
     * get datetime
     * */
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
