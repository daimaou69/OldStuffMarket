package com.example.oldstuffmarket.adapter;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.AccountInfoActivity;
import com.example.oldstuffmarket.R;
import com.example.oldstuffmarket.data_models.SanPham;
import com.example.oldstuffmarket.data_models.ShopData;
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

public class ShopRequestAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<ShopData> shopDataArrayList;

    public ShopRequestAdapter(Context context, int layout, ArrayList<ShopData> shopDataArrayList) {
        this.context = context;
        this.layout = layout;
        this.shopDataArrayList = shopDataArrayList;
    }

    @Override
    public int getCount() {
        return shopDataArrayList.size();
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
        ImageView imgUser;
        TextView txtUserName, txtSanPhamDaBan, txtDiemThanhVien, txtReported;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.imgUser = (ImageView) convertView.findViewById(R.id.imgUser);
            viewHolder.txtDiemThanhVien = (TextView) convertView.findViewById(R.id.txtDiemThanhVien);
            viewHolder.txtReported = (TextView) convertView.findViewById(R.id.txtReported);
            viewHolder.txtSanPhamDaBan = (TextView) convertView.findViewById(R.id.txtSanPhamDaBan);
            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ShopData shopData = shopDataArrayList.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserData.class).getsUserID().equals(shopData.getUserID())){
                    storageReference.child(snapshot.getValue(UserData.class).getsImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context).load(uri).into(viewHolder.imgUser);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    viewHolder.txtUserName.setText(snapshot.getValue(UserData.class).getsUserName() + " - " + snapshot.getValue(UserData.class).getsFullName());
                    viewHolder.txtSanPhamDaBan.setText("Số sản phẩm đã bán: " + String.valueOf(snapshot.getValue(UserData.class).getiSoSPDaBan()));
                    viewHolder.txtDiemThanhVien.setText("Điểm thành viên: " + String.valueOf(snapshot.getValue(UserData.class).getiAccPoint()));
                    viewHolder.txtReported.setText("Report: " + String.valueOf(snapshot.getValue(UserData.class).getiReport()));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return convertView;
    }
}
