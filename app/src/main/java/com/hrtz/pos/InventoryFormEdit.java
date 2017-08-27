package com.hrtz.pos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hrtz.pos.modal.Inventory;
import com.hrtz.pos.modal.InventoryDbHelper;

/**
 * Created by harit on 8/27/2017.
 */

public class InventoryFormEdit extends Fragment implements View.OnClickListener {
    InventoryDbHelper dbHelper;
    private Button btnAdd;
    private EditText etNama, etHarga, etJumlah;
    private Inventory inventory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        inventory = (Inventory) bundle.getSerializable(Inventory.BUNDLE_TAG);

        return inflater.inflate(R.layout.fragment_inventory_form_edit, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        dbHelper = new InventoryDbHelper(getActivity().getApplicationContext());


        btnAdd = (Button) view.findViewById(R.id.btn_inventoryformedit_tambah);
        etNama = (EditText) view.findViewById(R.id.et_inventoryformedit_nama);
        etHarga = (EditText) view.findViewById(R.id.et_inventoryformedit_harga);
        etJumlah = (EditText) view.findViewById(R.id.et_inventoryformedit_jumlah);
        etNama.setText(inventory.getName());
        etHarga.setText(inventory.getPrice()+"");
        etJumlah.setText(inventory.getStock()+"");
        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Inventory i = new Inventory();
        i.setName(etNama.getText().toString());
        i.setPrice(Integer.parseInt(etHarga.getText().toString()));
        i.setStock(Integer.parseInt(etJumlah.getText().toString()));
        i.setId(inventory.getId());

        dbHelper.updateInventory(i);

        Toast.makeText(getActivity(), "Barang Ditambahkan", Toast.LENGTH_SHORT).show();

        getFragmentManager().popBackStack();
    }
}
