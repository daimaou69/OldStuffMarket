package com.example.oldstuffmarket.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.R;
import com.example.oldstuffmarket.data_models.SanPham;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class SanPhamAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<SanPham> sanPhamArrayList;

    public SanPhamAdapter(Context context, int layout, ArrayList<SanPham> sanPhamArrayList) {
        this.context = context;
        this.layout = layout;
        this.sanPhamArrayList = sanPhamArrayList;
    }

    @Override
    public int getCount() {
        return sanPhamArrayList.size();
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
        TextView txtTenSP, txtNgayDang, txtGiaSP, txtDanhMuc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);

            viewHolder.imgSP = (ImageView) convertView.findViewById(R.id.imgSP);
            viewHolder.txtGiaSP = (TextView) convertView.findViewById(R.id.txtGiaSP);
            viewHolder.txtTenSP = (TextView) convertView.findViewById(R.id.txtTenSP);
            viewHolder.txtDanhMuc = (TextView) convertView.findViewById(R.id.txtDanhMuc);
            viewHolder.txtNgayDang = (TextView) convertView.findViewById(R.id.txtNgayDang);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SanPham sanPham = sanPhamArrayList.get(position);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(sanPham.getsSPImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.imgSP);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.txtNgayDang.setText("Ngày đăng: " + sanPham.getsNgayDang());

        if(sanPham.getiTinhTrang() == 0){

            viewHolder.txtTenSP.setText(sanPham.getsTenSP() + " - New");
        }
        else {
            viewHolder.txtTenSP.setText(sanPham.getsTenSP() + " - 2nd");
        }

        if(sanPham.getiSoLuong() == 0){

            viewHolder.txtDanhMuc.setText("Hết hàng");
            viewHolder.txtDanhMuc.setTextColor(Color.RED);
        }
        else{

            viewHolder.txtDanhMuc.setText(sanPham.getsDanhMuc());
        }


        viewHolder.txtGiaSP.setText(String.valueOf(sanPham.getlGiaTien()) + "vnđ");

        return convertView;
    }
}
