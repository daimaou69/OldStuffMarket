package com.example.oldstuffmarket.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.R;
import com.example.oldstuffmarket.data_models.ProductReport;
import com.example.oldstuffmarket.data_models.SanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SPbibaocao_Adapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<ProductReport> productReportArrayList;

    public SPbibaocao_Adapter(Context context, int layout, ArrayList<ProductReport> productReportArrayList) {
        this.context = context;
        this.layout = layout;
        this.productReportArrayList = productReportArrayList;
    }

    @Override
    public int getCount() {
        return productReportArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        ImageView imgSP;
        TextView txtTenSP, txtGiaTien;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.imgSP = (ImageView) convertView.findViewById(R.id.imgSpReport);
            viewHolder.txtTenSP = (TextView) convertView.findViewById(R.id.txtTenSpReport);
            viewHolder.txtGiaTien = (TextView) convertView.findViewById(R.id.txtGiaTienSpReport);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ProductReport productReport = productReportArrayList.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(productReport.getSanPham().getsSPImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.imgSP);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        viewHolder.txtTenSP.setText(productReport.getSanPham().getsTenSP());
        viewHolder.txtGiaTien.setText(String.valueOf(productReport.getSanPham().getlGiaTien()));

        return convertView;
    }
}