package com.hrtz.pos;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrtz.pos.modal.Inventory;
import com.hrtz.pos.modal.InventoryDbHelper;
import com.hrtz.pos.modal.Purchase;
import com.hrtz.pos.modal.Purchase_Inventory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by harit on 8/23/2017.
 */

public class PurchaseFragment extends Fragment implements FragmentManager.OnBackStackChangedListener, View.OnClickListener {
    List<Purchase> purchaseList;
    ListView lv;
    PurchaseAdapter purchaseAdapter;
    InventoryDbHelper dbHelper;
    Button btnDateBegin, btnDateEnd;
    Calendar beginCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    TextView tvgrandtotal;
    DatePickerDialog.OnDateSetListener beginDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Calendar begin = Calendar.getInstance();
            begin.set(year, monthOfYear, dayOfMonth);

            //check if the end dates is before or the same as the begin calendar
            if(begin.compareTo(endCalendar)<= 0){
                beginCalendar = begin;
            }else{
                Toast.makeText(getContext(), "Beginning date must be before end date", Toast.LENGTH_SHORT).show();
            }
            calendarChanged();
        }
    };


    DatePickerDialog.OnDateSetListener endDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Calendar end = Calendar.getInstance();
            end.set(year, monthOfYear, dayOfMonth);

            //check if the end dates is after or the same as the begin calendar
            if(end.compareTo(beginCalendar)>= 0){
                endCalendar = end;
            }else{
                Toast.makeText(getContext(), "End date must be after beginning date", Toast.LENGTH_SHORT).show();
            }
            calendarChanged();
        }
    };

    /**
     * when the user finishes editing time
     */
    private void calendarChanged() {
        btnDateEnd.setText(format.format(endCalendar.getTime()));
        btnDateBegin.setText(format.format(beginCalendar.getTime()));

        purchaseList = dbHelper.getAllPurchasesBetween(format.format(beginCalendar.getTime()), format.format(endCalendar.getTime()));
        tvgrandtotal.setText("Total pembelian: "+sumList(purchaseList));

        purchaseAdapter = new PurchaseAdapter(purchaseList, getActivity().getApplicationContext());

        purchaseAdapter.notifyDataSetChanged();
        lv.setAdapter(purchaseAdapter);
    }

    public int sumList(List<Purchase> list) {
        int sum = 0;

        for (Purchase s : list)
            sum = sum + s.getTotal();

        return sum;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_purchase, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        tvgrandtotal = (TextView) view.findViewById(R.id.tv_fragment_purchase_total);

        //finding the reference to these two button
        btnDateBegin = (Button) view.findViewById(R.id.datePurchaseBegin);
        btnDateEnd = (Button) view.findViewById(R.id.datePurchaseEnd);
        btnDateBegin.setOnClickListener(this);
        btnDateEnd.setOnClickListener(this);

        lv = (ListView) view.findViewById(R.id.lvPurchase);
        dbHelper = new InventoryDbHelper(getActivity().getApplicationContext());

        //set the begin date a week before
        beginCalendar.add(Calendar.DAY_OF_MONTH, -7);
        //initialize initial sales data and set the text on calendar button
        calendarChanged();

        //if there are changes on the data set, reset the content of the list
        getFragmentManager().addOnBackStackChangedListener(this);
    }


    @Override
    public void onBackStackChanged() {
        //jika fragment ini aktif baru dipanggil
        if(isAdded() && isVisible() && getUserVisibleHint()) {
            endCalendar = Calendar.getInstance();
            beginCalendar = Calendar.getInstance();
            beginCalendar.add(Calendar.DAY_OF_MONTH, -7);
            calendarChanged();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.datePurchaseBegin:
                new DatePickerDialog(getActivity(), beginDate, beginCalendar
                        .get(Calendar.YEAR), beginCalendar.get(Calendar.MONTH),
                        beginCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.datePurchaseEnd:
                new DatePickerDialog(getActivity(), endDate, endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }

    }

    class PurchaseAdapter extends BaseAdapter implements ListAdapter {
        private  List<Purchase> list;
        private Context context;

        public PurchaseAdapter(List<Purchase> purchaseList, Context applicationContext) {
            list = purchaseList;
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
                view = inflater.inflate(R.layout.purchase_list_item, null);
            }

            TextView tvDate, tvTotal, tvItemList;
            tvDate = (TextView) view.findViewById(R.id.list_purchase_date);
            tvDate.setText(list.get(position).getCreated_at());
            tvTotal = (TextView) view.findViewById(R.id.list_purchase_total);
            tvTotal.setText("Total: "+list.get(position).getTotal());

            tvItemList = (TextView) view.findViewById(R.id.list_purchase_details);
            StringBuilder stringBuilder = new StringBuilder();
            for(Purchase_Inventory s: list.get(position).getPurchase_inventoryList()){
                Inventory i = s.getInventory();
                stringBuilder.append(s.getCount()+" X "+i.getName() + " ("+i.getPrice()+" | "+s.getCount()*i.getPrice()+")");
                stringBuilder.append(System.getProperty("line.separator"));
            }
            tvItemList.setText(stringBuilder.toString());

            //Handle buttons and add onClickListeners
            ImageButton deleteBtn = (ImageButton)view.findViewById(R.id.btn_purchaseitem_delete);
            ImageButton editBtn = (ImageButton)view.findViewById(R.id.btn_purchaseitem_edit);

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
                            dbHelper.deletePurchase((int) purchaseList.get(position).getId());
                            purchaseList.remove(position);
                            purchaseAdapter.notifyDataSetChanged();
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
                    Purchase i = purchaseList.get(position);

                    bundle.putSerializable(Purchase.BUNDLE_TAG, i);


                    PurchaseFormEdit purchaseFormEditFragment = new PurchaseFormEdit();
                    purchaseFormEditFragment.setArguments(bundle);
                    ft.add(R.id.fragment_placeholder, purchaseFormEditFragment);


                    ft.addToBackStack(null);
                    ft.commit();

                }
            });

            return view;
        }

    }


}

