package com.example.oldstuffmarket.adapter;

import android.content.Context;
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
import com.example.oldstuffmarket.R;
import com.example.oldstuffmarket.data_models.OrderData;
import com.example.oldstuffmarket.data_models.ShopData;
import com.example.oldstuffmarket.data_models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ThongKeAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<UserData> userDataArrayList;
    private ArrayList<OrderData> orderDataArrayList;

    public ThongKeAdapter(Context context, int layout, ArrayList<UserData> userDataArrayList, ArrayList<OrderData> orderDataArrayList) {
        this.context = context;
        this.layout = layout;
        this.userDataArrayList = userDataArrayList;
        this.orderDataArrayList = orderDataArrayList;
    }

    @Override
    public int getCount() {
        return userDataArrayList.size();
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
        TextView txtUserName, txtSanPhamDaBan, txtDiemThanhVien, txtDoanhThu;
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
            viewHolder.txtDoanhThu = (TextView) convertView.findViewById(R.id.txtDoanhThu);
            viewHolder.txtSanPhamDaBan = (TextView) convertView.findViewById(R.id.txtSanPhamDaBan);
            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        UserData userData = userDataArrayList.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        if (userData.getsShopID().isEmpty()) {
            storageReference.child(userData.getsImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(context).load(uri).into(viewHolder.imgUser);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
            viewHolder.txtUserName.setText("User: " + userData.getsUserName() + " - " + userData.getsFullName());
            viewHolder.txtSanPhamDaBan.setText("Số sản phẩm đã bán: " + String.valueOf(userData.getiSoSPDaBan()));
            viewHolder.txtDiemThanhVien.setText("Điểm thành viên: " + String.valueOf(userData.getiAccPoint()));
        }
        else {
            databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(ShopData.class).getUserID().equals(userData.getsUserID())) {
                        storageReference.child(snapshot.getValue(ShopData.class).getShopImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(context).load(uri).into(viewHolder.imgUser);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }
                        });
                        viewHolder.txtUserName.setText("Shop: " + snapshot.getValue(ShopData.class).getShopName());
                        viewHolder.txtSanPhamDaBan.setText("Số sản phẩm đã bán: " + String.valueOf(userData.getiSoSPDaBan()));
                        viewHolder.txtDiemThanhVien.setText("Điểm thành viên: " + String.valueOf(userData.getiAccPoint()));
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
        }
        long commission = 0;
        for(OrderData orderData : orderDataArrayList){
            if (orderData.getNguoiBanID().equals(userData.getsUserID()) && orderData.getTinhTrang() == 8 && orderData.getLoaiDonHang() != 1) {
                commission += orderData.getGiaTien() * orderData.getSellerCommission() / 100;

                viewHolder.txtDoanhThu.setText(String.valueOf(commission) + "VNĐ");
            }
        }

        return convertView;
    }
}
