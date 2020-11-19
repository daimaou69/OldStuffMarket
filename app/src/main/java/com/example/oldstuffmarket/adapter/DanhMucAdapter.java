package com.example.oldstuffmarket.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.R;
import com.example.oldstuffmarket.data_models.DanhMucData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class DanhMucAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<DanhMucData> danhMucDataList;

    public DanhMucAdapter(Context context, int layout, List<DanhMucData> danhMucDataList) {
        this.context = context;
        this.layout = layout;
        this.danhMucDataList = danhMucDataList;
    }

    @Override
    public int getCount() {
        return danhMucDataList.size();
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
        ImageView imgDanhMuc;
        TextView txtTenDanhMuc;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            viewHolder.imgDanhMuc = (ImageView) convertView.findViewById(R.id.imgDanhMuc);
            viewHolder.txtTenDanhMuc = (TextView) convertView.findViewById(R.id.txtTenDanhMuc);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final DanhMucData danhMucData = danhMucDataList.get(position);
//        final Handler handler = new Handler();
//        final int delay = 800; //milliseconds
//        handler.postDelayed(new Runnable(){
//            public void run(){
//
////                handler.postDelayed(this, delay);
//            }
//        }, delay);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child(danhMucData.getsDanhMucIMG()+ ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(viewHolder.imgDanhMuc);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.txtTenDanhMuc.setText(danhMucData.getsTenDanhMuc());

        return convertView;
    }
}
