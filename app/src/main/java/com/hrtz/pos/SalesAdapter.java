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
import com.hrtz.pos.modal.Sales;
import com.hrtz.pos.modal.Sales_Inventory;

import java.util.List;

/**
 * Created by harit on 8/23/2017.
 */

public class SalesAdapter extends BaseAdapter implements ListAdapter {
    private  List<Sales> list;
    private Context context;

    public SalesAdapter(List<Sales> salesList, Context applicationContext) {
        list = salesList;
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
            view = inflater.inflate(R.layout.sales_list_item, null);
        }

        TextView tvDate, tvTotal, tvItemList;
        tvDate = (TextView) view.findViewById(R.id.list_sales_date);
        tvDate.setText(list.get(position).getCreated_at());
        tvTotal = (TextView) view.findViewById(R.id.list_sales_total);
        tvTotal.setText("Total: "+list.get(position).getTotal());

        tvItemList = (TextView) view.findViewById(R.id.list_sales_details);
        StringBuilder stringBuilder = new StringBuilder();
        for(Sales_Inventory s: list.get(position).getSales_inventoryList()){
            stringBuilder.append(s.getCount()+" X "+s.getInventory() + " ("+s.getTotal()+")");
            stringBuilder.append(System.getProperty("line.separator"));

        }
        tvItemList.setText(stringBuilder.toString());


        return view;
    }

}
