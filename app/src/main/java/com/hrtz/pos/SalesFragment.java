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
import com.hrtz.pos.modal.Sales;

import java.util.List;

/**
 * Created by harit on 8/23/2017.
 */

public class SalesFragment extends Fragment {
    List<Sales> salesList;
    ListView lv;
    SalesAdapter salesAdapter;
    InventoryDbHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sales, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        lv = (ListView) view.findViewById(R.id.lvSales);
        dbHelper = new InventoryDbHelper(getActivity().getApplicationContext());
        salesList = dbHelper.getAllSales();

        salesAdapter = new SalesAdapter(salesList, getActivity().getApplicationContext());
        lv.setAdapter(salesAdapter);

    }
}
