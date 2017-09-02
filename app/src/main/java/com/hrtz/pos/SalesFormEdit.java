package com.hrtz.pos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hrtz.pos.modal.Inventory;
import com.hrtz.pos.modal.InventoryDbHelper;
import com.hrtz.pos.modal.Sales;
import com.hrtz.pos.modal.Sales_Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harit on 8/27/2017.
 */

public class SalesFormEdit extends Fragment implements View.OnClickListener {
    InventoryDbHelper dbHelper;
    private Button btnAdd, btnSubmit;
    private ArrayList<EditText> editTextArrayList;
    private ArrayList<AutoCompleteTextView> autoCompleteTextViewArrayList;
    ArrayList<TextView> totalTextViewArrayList;
    private TextView  titleTv;
    private List<Inventory> inventoryCompleteList;
    private ArrayAdapter<Inventory> inventoryAdapter;
    private LinearLayout linearLayout;
    private ArrayList<Sales_Inventory> salesInventoryArrayList;
    private int total;
    private ArrayList<RelativeLayout> relativeLayoutArrayList;
    private Sales sales;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {


        Bundle bundle = this.getArguments();
        sales = (Sales) bundle.getSerializable(Sales.BUNDLE_TAG);


        return inflater.inflate(R.layout.fragment_sales_form, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dbHelper = new InventoryDbHelper(getActivity().getApplicationContext());

        titleTv = (TextView) view.findViewById(R.id.tvtitleaddsales);
        titleTv.setText("Edit Pembelian");

        btnAdd = (Button) view.findViewById(R.id.btn_salesform_addinventory);
        btnSubmit = (Button) view.findViewById(R.id.btn_salesform_submit);

        btnAdd.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        autoCompleteTextViewArrayList = new ArrayList<AutoCompleteTextView>();
        editTextArrayList = new ArrayList<EditText>();
        totalTextViewArrayList = new ArrayList<TextView>();
        salesInventoryArrayList = new ArrayList<Sales_Inventory>();
        relativeLayoutArrayList = new ArrayList<RelativeLayout>();
        linearLayout = (LinearLayout) view.findViewById(R.id.content_sales_form);

        inventoryCompleteList = dbHelper.getAllInventories();
        inventoryAdapter = new ArrayAdapter<Inventory>(getActivity(), R.layout.autocomplete_dropdown,
                inventoryCompleteList);

        total = 0;
        for(Sales_Inventory i: sales.getSales_inventoryList()){
            Log.v("sales iventory", i.getInventory().getName()+ " | "+i.getCount()+" | "+i.getTotal());
            addItem(i);
        }

    }

    protected void addItem(Sales_Inventory i) {
        View itemLayout = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sales_form_item, null);

        AutoCompleteTextView autoCompleteTextView =
                (AutoCompleteTextView)itemLayout.findViewById(R.id.sales_form_autocomplete);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(inventoryAdapter);
        int pos = inventoryAdapter.getPosition(i.getInventory());
        autoCompleteTextView.setText(i.getInventory().getName());
//        autoCompleteTextView.setSelection(pos);
        autoCompleteTextViewArrayList.add(autoCompleteTextView);

        //to get selected index of autotextview, add it to the mfgart list
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = autoCompleteTextViewArrayList.size()-1;
                Inventory i = (Inventory) parent.getItemAtPosition(position);

                //liat berapa dia beli
                int count = 0;
                try {
                    count = Integer.parseInt(editTextArrayList.get(index).getText().toString());
                }catch (Exception e){

                }
                //set harga total untuk barang itu
                totalTextViewArrayList.get(index).setText("@ = "+i.getPrice());

                salesInventoryArrayList.get(index).setCount(count);
                salesInventoryArrayList.get(index).setInventory(i);
                calculateTotal();
            }
        });

        salesInventoryArrayList.add(i);

        relativeLayoutArrayList.add((RelativeLayout)itemLayout.findViewById(R.id.sales_form_relativelayout));

        TextView tv = (TextView) itemLayout.findViewById(R.id.tv_salesformitem_total);
        tv.setText(i.getCount()*i.getInventory().getPrice()+"");
        totalTextViewArrayList.add(tv);

        EditText editText = (EditText) itemLayout.findViewById(R.id.sales_form_edittext);
        editText.setText(i.getCount()+"");
        editTextArrayList.add(editText);

        ImageButton button = (ImageButton) itemLayout.findViewById(R.id.btn_salesformitem_deleteRecord);
        button.setTag(relativeLayoutArrayList.size()-1);
        button.setOnClickListener(this);

        linearLayout.addView(itemLayout);
    }


    protected void addItem(){
        View itemLayout = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_sales_form_item, null);

        AutoCompleteTextView autoCompleteTextView =
                (AutoCompleteTextView)itemLayout.findViewById(R.id.sales_form_autocomplete);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(inventoryAdapter);

        autoCompleteTextViewArrayList.add(autoCompleteTextView);

        TextView tv = (TextView) itemLayout.findViewById(R.id.tv_salesformitem_total);
        totalTextViewArrayList.add(tv);

        EditText editText = (EditText) itemLayout.findViewById(R.id.sales_form_edittext);
        editTextArrayList.add(editText);


        salesInventoryArrayList.add(new Sales_Inventory());

        //to get selected index of autotextview, add it to the mfgart list
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = autoCompleteTextViewArrayList.size()-1;
                Inventory i = (Inventory) parent.getItemAtPosition(position);

                //liat berapa dia beli
                int count = 0;
                try {
                    count = Integer.parseInt(editTextArrayList.get(index).getText().toString());
                }catch (Exception e){

                }
                //set harga total untuk barang itu
                totalTextViewArrayList.get(index).setText("@ = "+i.getPrice());

                salesInventoryArrayList.get(index).setCount(count);
                salesInventoryArrayList.get(index).setInventory(i);
                calculateTotal();
            }
        });


        //add null list for future reference
        salesInventoryArrayList.add(new Sales_Inventory());

        relativeLayoutArrayList.add((RelativeLayout)itemLayout.findViewById(R.id.sales_form_relativelayout));

        ImageButton button = (ImageButton) itemLayout.findViewById(R.id.btn_salesformitem_deleteRecord);
        button.setTag(relativeLayoutArrayList.size()-1);
        button.setOnClickListener(this);

        linearLayout.addView(itemLayout);
    }

    private void calculateTotal() {

    }

    public void deleteRecord(int index){
        relativeLayoutArrayList.get(index).setVisibility(RelativeLayout.GONE);
    }

    public void submitRecord(){
        //validation of each entries
        boolean cancel = false;
        for(int i = 0; i < relativeLayoutArrayList.size(); i++){
            if(relativeLayoutArrayList.get(i).getVisibility() == AutoCompleteTextView.VISIBLE) {
                if (salesInventoryArrayList.get(i).getInventory() == null) {
                    autoCompleteTextViewArrayList.get(i).setError("Barang tidak valid");
                    autoCompleteTextViewArrayList.get(i).requestFocus();
                    cancel = true;
                }

                if (TextUtils.isEmpty(editTextArrayList.get(i).getText())) {
                    editTextArrayList.get(i).setError("Jumlah tidak boleh kosong");
                    editTextArrayList.get(i).requestFocus();
                    cancel = true;
                }
            }
        }

        if(cancel == false) {
            ArrayList<Sales_Inventory> si = new ArrayList<Sales_Inventory>();
            int grandtotal = 0;
            for (int i = 0; i < relativeLayoutArrayList.size(); i++) {
                //if the component is visible (not deleted)
                if (relativeLayoutArrayList.get(i).getVisibility() == View.VISIBLE) {
                    //jika sudah ada item yang dipilih
                    if (salesInventoryArrayList.get(i).getInventory() != null) {
                        int count = 0;
                        try {
                            count = Integer.parseInt(editTextArrayList.get(i).getText().toString());
                        }catch (Exception e){

                        }

                        grandtotal += count *
                                salesInventoryArrayList.get(i).getInventory().getPrice();
                        salesInventoryArrayList.get(i).setCount(count);
                        si.add(salesInventoryArrayList.get(i));
                    }
                }
            }
            Sales_Inventory[] salesInventories = si.toArray(new Sales_Inventory[si.size()]);

            for(int i = 0; i < salesInventories.length; i++){
                Log.v("sales inventory list: ", salesInventories[i].getInventory().getName());
                Log.v("sales inventory list: ", salesInventories[i].getInventory().getId()+"");
            }
            Log.v("grand: ", grandtotal+"");
            dbHelper.updateSales(sales, salesInventories, grandtotal);

            Toast.makeText(getActivity(), "Sales Dirubah", Toast.LENGTH_SHORT).show();

            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_salesform_addinventory:
                addItem();
                break;

            case R.id.btn_salesform_submit:
                submitRecord();
                break;

            case R.id.btn_salesformitem_deleteRecord:
                deleteRecord((int)v.getTag());
                break;
        }

//        Inventory i = new Inventory();
//        i.setName(etNama.getText().toString());
//        i.setPrice(Integer.parseInt(etHarga.getText().toString()));
//        i.setStock(Integer.parseInt(etJumlah.getText().toString()));
//        dbHelper.createInventory(i);
//        Toast.makeText(getActivity(), "Barang Ditambahkan", Toast.LENGTH_SHORT).show();
//
//        getFragmentManager().popBackStack();
    }

}
