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
import com.example.oldstuffmarket.data_models.OrderData;
import com.example.oldstuffmarket.data_models.UserData;
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

public class GiaoDichAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<OrderData> orderDataArrayList;

    public GiaoDichAdapter(Context context, int layout, ArrayList<OrderData> orderDataArrayList) {
        this.context = context;
        this.layout = layout;
        this.orderDataArrayList = orderDataArrayList;
    }

    @Override
    public int getCount() {
        return orderDataArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder {
        ImageView imgSP;
        TextView txtTenSP, txtMoney, txtSoLuongSP, txtTinhTrang;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.imgSP = (ImageView) convertView.findViewById(R.id.imgSP);
            viewHolder.txtTenSP = (TextView) convertView.findViewById(R.id.txtTenSP);
            viewHolder.txtMoney = (TextView) convertView.findViewById(R.id.txtMoney);
            viewHolder.txtTinhTrang = (TextView) convertView.findViewById(R.id.txtTinhTrang);
            viewHolder.txtSoLuongSP = (TextView) convertView.findViewById(R.id.txtSoLuongSP);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OrderData orderData = orderDataArrayList.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(orderData.getSanPham().getsSPImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.imgSP);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        viewHolder.txtTenSP.setText(orderData.getSanPham().getsTenSP());
        viewHolder.txtSoLuongSP.setText(String.valueOf(orderData.getSanPham().getiSoLuong()));
        viewHolder.txtMoney.setText(String.valueOf(orderData.getGiaTien()));
        if (orderData.getTinhTrang() == -1) {
            viewHolder.txtTinhTrang.setText("Thất bại");
        }
        else if (orderData.getTinhTrang() == 4) {
            viewHolder.txtTinhTrang.setText("Thành công");
        }
        return convertView;
    }
}
