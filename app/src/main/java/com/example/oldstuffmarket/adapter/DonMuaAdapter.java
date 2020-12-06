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
import com.example.oldstuffmarket.data_models.DanhMucData;
import com.example.oldstuffmarket.data_models.OrderData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DonMuaAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<OrderData> orderDataList;

    public DonMuaAdapter(Context context, int layout, List<OrderData> orderDataList) {
        this.context = context;
        this.layout = layout;
        this.orderDataList = orderDataList;
    }

    @Override
    public int getCount() {
        return orderDataList.size();
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
        TextView txtTenSP, txtGiaSP, txtLoaiThanhToan, txtTinhTrang;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.imgSP = (ImageView) convertView.findViewById(R.id.imgSP);
            viewHolder.txtTenSP = (TextView) convertView.findViewById(R.id.txtTenSP);
            viewHolder.txtGiaSP = (TextView) convertView.findViewById(R.id.txtGiaSP);
            viewHolder.txtLoaiThanhToan = (TextView) convertView.findViewById(R.id.txtLoaiThanhToan);
            viewHolder.txtTinhTrang = (TextView) convertView.findViewById(R.id.txtTinhTrang);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        OrderData orderData = orderDataList.get(position);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(orderData.getSanPham().getsSPImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
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

        viewHolder.txtTenSP.setText(orderData.getSanPham().getsTenSP());
        viewHolder.txtGiaSP.setText("Tổng tiền: " + String.valueOf(orderData.getGiaTien()) + "vnđ");

        if(orderData.getTinhTrang() == 0){
            viewHolder.txtTinhTrang.setText("Chờ xác nhận");
        }
        else if(orderData.getTinhTrang() == 1){
            viewHolder.txtTinhTrang.setText("Đang đóng gói");
        }
        else if(orderData.getTinhTrang() == 2){
            viewHolder.txtTinhTrang.setText("Đóng gói hoàn tất");
        }
        else if(orderData.getTinhTrang() == 3){
            viewHolder.txtTinhTrang.setText("Chờ vận chuyển");
        }
        else if(orderData.getTinhTrang() == 5){
            viewHolder.txtTinhTrang.setText("Shipper đang lấy hàng");
        }
        else if(orderData.getTinhTrang() == 6){
            viewHolder.txtTinhTrang.setText("Shipper lấy hàng thành công");
        }
        else if(orderData.getTinhTrang() == 7){
            viewHolder.txtTinhTrang.setText("Đang giao hàng");
        }
        else if(orderData.getTinhTrang() == 4){
            viewHolder.txtTinhTrang.setText("Hoàn thành");
            viewHolder.txtTinhTrang.setTextColor(Color.GREEN);
        }
        else if(orderData.getTinhTrang() == -1){
            viewHolder.txtTinhTrang.setText("Hủy");
            viewHolder.txtTinhTrang.setTextColor(Color.RED);
        }

        if(orderData.getLoaiDonHang() == 1){
            viewHolder.txtLoaiThanhToan.setText("Giao dịch trực tiếp");
        }
        else if(orderData.getLoaiDonHang() == 2){
            viewHolder.txtLoaiThanhToan.setText("Thanh toán COD");
        }
        else if(orderData.getLoaiDonHang() == 3){
            viewHolder.txtLoaiThanhToan.setText("Đã thanh toán");
        }

        return convertView;
    }
}
