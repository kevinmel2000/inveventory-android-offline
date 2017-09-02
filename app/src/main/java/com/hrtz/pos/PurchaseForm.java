package com.hrtz.pos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
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
import com.hrtz.pos.modal.Purchase;
import com.hrtz.pos.modal.Purchase_Inventory;
import com.hrtz.pos.modal.Sales_Inventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harit on 8/27/2017.
 */

public class PurchaseForm extends Fragment implements View.OnClickListener {
    InventoryDbHelper dbHelper;
    private Button btnAdd, btnSubmit;
    private ArrayList<EditText> editTextArrayList;
    private ArrayList<AutoCompleteTextView> autoCompleteTextViewArrayList;
    ArrayList<TextView> totalTextViewArrayList;
    private List<Inventory> inventoryCompleteList;
    private ArrayAdapter<Inventory> inventoryAdapter;
    private LinearLayout linearLayout;
    private ArrayList<Purchase_Inventory> purchaseInventoryArrayList;
    private int total;
    private ArrayList<RelativeLayout> relativeLayoutArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_purchase_form, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dbHelper = new InventoryDbHelper(getActivity().getApplicationContext());

        btnAdd = (Button) view.findViewById(R.id.btn_purchaseform_addinventory);
        btnSubmit = (Button) view.findViewById(R.id.btn_purchaseform_submit);

        btnAdd.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);

        autoCompleteTextViewArrayList = new ArrayList<AutoCompleteTextView>();
        editTextArrayList = new ArrayList<EditText>();

        totalTextViewArrayList = new ArrayList<TextView>();
        purchaseInventoryArrayList = new ArrayList<Purchase_Inventory>();

        relativeLayoutArrayList = new ArrayList<RelativeLayout>();

        linearLayout = (LinearLayout) view.findViewById(R.id.content_purchase_form);

        inventoryCompleteList = dbHelper.getAllInventories();
        inventoryAdapter = new ArrayAdapter<Inventory>(getActivity(), R.layout.autocomplete_dropdown,
                inventoryCompleteList);

        total = 0;
        addItem();
    }



    protected void addItem(){
        View itemLayout = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_purchase_form_item, null);

        AutoCompleteTextView autoCompleteTextView =
                (AutoCompleteTextView)itemLayout.findViewById(R.id.purchase_form_autocomplete);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(inventoryAdapter);

        autoCompleteTextViewArrayList.add(autoCompleteTextView);

        TextView tv = (TextView) itemLayout.findViewById(R.id.tv_purchaseformitem_total);
        totalTextViewArrayList.add(tv);

        EditText editText = (EditText) itemLayout.findViewById(R.id.purchase_form_edittext);
        editTextArrayList.add(editText);


        purchaseInventoryArrayList.add(new Purchase_Inventory());

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

                purchaseInventoryArrayList.get(index).setCount(count);
                purchaseInventoryArrayList.get(index).setInventory(i);
                calculateTotal();
            }
        });


        //add null list for future reference
        purchaseInventoryArrayList.add(new Purchase_Inventory());

        relativeLayoutArrayList.add((RelativeLayout)itemLayout.findViewById(R.id.purchase_form_relativelayout));

        ImageButton button = (ImageButton) itemLayout.findViewById(R.id.btn_purchaseformitem_deleteRecord);
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
                if (purchaseInventoryArrayList.get(i).getInventory() == null) {
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
            ArrayList<Purchase_Inventory> si = new ArrayList<Purchase_Inventory>();
            int grandtotal = 0;
            for (int i = 0; i < relativeLayoutArrayList.size(); i++) {
                //if the component is visible (not deleted)
                if (relativeLayoutArrayList.get(i).getVisibility() == View.VISIBLE) {
                    //jika sudah ada item yang dipilih
                    if (purchaseInventoryArrayList.get(i).getInventory() != null) {
                        int count = 0;
                        try {
                            count = Integer.parseInt(editTextArrayList.get(i).getText().toString());
                        }catch (Exception e){

                        }
                        grandtotal += count *
                                purchaseInventoryArrayList.get(i).getInventory().getPrice();
                        purchaseInventoryArrayList.get(i).setCount(count);
                        si.add(purchaseInventoryArrayList.get(i));
                    }
                }
            }
            Purchase_Inventory[] purchase = si.toArray(new Purchase_Inventory[si.size()]);
            for(int i = 0; i < purchase.length; i++){
                Log.v("purchase list: ", purchase[i].getInventory().getName());
                Log.v("purchase list: ", purchase[i].getCount()+"");
            }
            Log.v("grand: ", grandtotal+"");
            dbHelper.createPurchase(purchase, grandtotal);

            Toast.makeText(getActivity(), "purchase Ditambahkan", Toast.LENGTH_SHORT).show();

            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_purchaseform_addinventory:
                addItem();
                break;

            case R.id.btn_purchaseform_submit:
                submitRecord();
                break;

            case R.id.btn_purchaseformitem_deleteRecord:
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
