package com.hrtz.pos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.hrtz.pos.R;
import com.hrtz.pos.modal.Inventory;

import java.util.List;

/**
 * Created by harit on 8/23/2017.
 */

public class InventoryAdapter extends BaseAdapter implements ListAdapter {
    private  List<Inventory> list;
    private Context context;

    public InventoryAdapter(List<Inventory> inventories, Context applicationContext) {
        list = inventories;
        context = applicationContext;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.inventory_list_item, null);
        }

        TextView tvTitle, tvStock, tvPrice;
        tvTitle = (TextView) view.findViewById(R.id.list_inventory_title);
        tvTitle.setText(list.get(position).getName());
        tvStock = (TextView) view.findViewById(R.id.list_inventory_stock);
        tvStock.setText("Stok: "+list.get(position).getStock());
        tvPrice = (TextView) view.findViewById(R.id.list_inventory_price);
        tvPrice.setText("Harga: "+list.get(position).getPrice());

        return view;
    }

}
