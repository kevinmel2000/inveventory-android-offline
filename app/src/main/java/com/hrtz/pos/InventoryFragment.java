package com.hrtz.pos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hrtz.pos.modal.Inventory;
import com.hrtz.pos.modal.InventoryDbHelper;

import java.util.List;

/**
 * Created by harit on 8/23/2017.
 */

public class InventoryFragment extends Fragment {
    List<Inventory> inventories;
    ListView lv;
    InventoryAdapter inventoryAdapter;
    InventoryDbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_inventory, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        lv = (ListView) view.findViewById(R.id.lvToDoList);
        dbHelper = new InventoryDbHelper(getActivity().getApplicationContext());
        inventories = dbHelper.getAllInventories();

        inventoryAdapter = new InventoryAdapter(inventories, getActivity().getApplicationContext());
        lv.setAdapter(inventoryAdapter);

    }
}
