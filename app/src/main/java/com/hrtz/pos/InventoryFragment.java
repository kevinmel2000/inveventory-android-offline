package com.hrtz.pos;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hrtz.pos.modal.Inventory;
import com.hrtz.pos.modal.InventoryDbHelper;

import java.util.List;

/**
 * Created by harit on 8/23/2017.
 */

public class InventoryFragment extends Fragment implements FragmentManager.OnBackStackChangedListener {
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
        getFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public void onBackStackChanged() {
        //jika fragment ini aktif baru dipanggil
        if(isAdded() && isVisible() && getUserVisibleHint()) {
            inventories = dbHelper.getAllInventories();
            //instantiate custom adapter
            inventoryAdapter = new InventoryAdapter(inventories, getActivity().getApplicationContext());
            lv.setAdapter(inventoryAdapter);
        }
    }

    class InventoryAdapter extends BaseAdapter implements ListAdapter {
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

            //Handle buttons and add onClickListeners
            ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.btnDeleteInventory);
            ImageButton editBtn = (ImageButton)view.findViewById(R.id.btnEditInventory);

            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    AlertDialog alert = new AlertDialog.Builder(getActivity()).create();

                    /** disable cancel outside touch */
                    alert.setCanceledOnTouchOutside(true);
                    /** disable cancel on press back button */
                    alert.setCancelable(true);
                    alert.setMessage("Yakin mau menghapus?");
                    alert.setButton(AlertDialog.BUTTON_POSITIVE, "Hapus", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dbHelper.deleteInventory(inventories.get(position).getId());
                            inventories.remove(position);
                            inventoryAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });

                    alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Batal", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
            });
            editBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                    Bundle bundle = new Bundle();
                    Inventory i = inventories.get(position);

                    bundle.putSerializable(Inventory.BUNDLE_TAG, inventories.get(position));
                    Log.v("inventory", i.getId()+" | "+i.getName());
                    InventoryFormEdit inventoryFormEditFragment = new InventoryFormEdit();
                    inventoryFormEditFragment.setArguments(bundle);
                    ft.add(R.id.fragment_placeholder, inventoryFormEditFragment);


                    ft.addToBackStack(null);
                    ft.commit();

                }
            });

            return view;
        }
    }
}
