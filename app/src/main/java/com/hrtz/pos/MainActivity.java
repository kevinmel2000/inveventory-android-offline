package com.hrtz.pos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hrtz.pos.modal.Inventory;
import com.hrtz.pos.modal.InventoryDbHelper;
import com.hrtz.pos.modal.Sales;
import com.hrtz.pos.modal.Sales_Inventory;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//
//        InventoryDbHelper dbHelper = new InventoryDbHelper(getApplicationContext());
//
//        Inventory inven1 = new Inventory("Barang 1", 10, 1000);
//        Inventory inven2 = new Inventory("Barang 2", 20, 2000);
//        Inventory inven3 = new Inventory("Barang 3", 30, 3000);
//
//
//        long inven1_id = dbHelper.createInventory(inven1);
//        long inven2_id = dbHelper.createInventory(inven2);
//        long inven3_id = dbHelper.createInventory(inven3);
//
//        Log.d("Inventory insert", "Inserting fragment_inventory");
//
//        List<Inventory> inventories = dbHelper.getAllInventories();
//        for(int i = 0; i < inventories.size(); i++){
//            Inventory in = inventories.get(i);
//            Log.d("fragment_inventory list:", in.getId()+" | "+in.getName()+ " | "+in.getPrice()+" | "+in.getStock());
//        }
//
//        Sales_Inventory si1 = new Sales_Inventory(inven1_id, 5, inven1.getPrice()*5);
//        Sales_Inventory si2 = new Sales_Inventory(inven2_id, 6, inven2.getPrice()*6);
//        Sales_Inventory si3 = new Sales_Inventory(inven3_id, 7, inven3.getPrice()*7);
//
//        Sales_Inventory[] siarray1 = new Sales_Inventory[]{si1, si2, si3};
//        dbHelper.createSales(siarray1, si1.getTotal()+si2.getTotal()+si3.getTotal());
//
//        List<Sales> sales = dbHelper.getAllSales();
//
//        for(Sales s: sales){
//            Log.d("sales list", "sale id: "+s.getId()+" | "+s.getCreated_at());
//            List<Sales_Inventory> slist = s.getSales_inventoryList();
//            for(int i = 0; i < slist.size(); i++){
//                Sales_Inventory si = slist.get(i);
//                Log.d("fragment_inventory list for", "sale id: "+s.getId()+" | "+si.getInventory()+ " | "+si.getId_inventory());
//            }
//
//        }
//
//        dbHelper.close();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment_placeholder, new MainFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void onClick(View v){
        int id = v.getId();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        Log.d("clicked", "onclickclicke");
        if (id == R.id.btnInventory) {
            ft.add(R.id.fragment_placeholder, new InventoryFragment());
        }
        if(id == R.id.btnPenjualan){
            ft.add(R.id.fragment_placeholder, new SalesFragment());
        }
        if(id == R.id.btnPembelian){
            ft.add(R.id.fragment_placeholder, new PurchaseFragment());
        }
        if(id == R.id.btnAddInventory){
            ft.add(R.id.fragment_placeholder, new InventoryForm());
        }
        if(id == R.id.btnAddSales){
            ft.add(R.id.fragment_placeholder, new SalesForm());
        }
        if(id == R.id.btnAddPurchase){
            ft.add(R.id.fragment_placeholder, new PurchaseForm());
        }
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
