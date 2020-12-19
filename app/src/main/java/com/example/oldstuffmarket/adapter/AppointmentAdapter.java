package com.example.oldstuffmarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.oldstuffmarket.R;
import com.example.oldstuffmarket.data_models.Appointment;
import com.example.oldstuffmarket.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AppointmentAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Appointment> appointmentArrayList;
    private String userID;

    public AppointmentAdapter(Context context, int layout, ArrayList<Appointment> appointmentArrayList, String userID) {
        this.context = context;
        this.layout = layout;
        this.appointmentArrayList = appointmentArrayList;
        this.userID = userID;
    }

    @Override
    public int getCount() {
        return appointmentArrayList.size();
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
        TextView txtTieuDe, txtNguoiHen, txtChiTiet;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.txtTieuDe = (TextView) convertView.findViewById(R.id.txtTieuDe);
            viewHolder.txtNguoiHen = (TextView) convertView.findViewById(R.id.txtNguoiHen);
            viewHolder.txtChiTiet = (TextView) convertView.findViewById(R.id.txtChiTiet);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Appointment appointment = appointmentArrayList.get(position);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        viewHolder.txtTieuDe.setText("Tiêu đề: " + appointment.getTieuDe());
        if(userID.equals(appointment.getNguoiHenID())){
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getsUserID().equals(appointment.getNguoiDuocHenID())){
                        viewHolder.txtNguoiHen.setText("Bạn hẹn gặp " + snapshot.getValue(UserData.class).getsFullName() + " vào ngày " + appointment.getNgayHen());

                        if(appointment.getMoTaCuocHen().length() > 30){
                            viewHolder.txtChiTiet.setText("Chi tiết: " + appointment.getMoTaCuocHen().substring(0, 30) + "..." + " - Liên hệ: " + snapshot.getValue(UserData.class).getsSdt());
                        }
                        else{
                            viewHolder.txtChiTiet.setText("Chi tiết: " + appointment.getMoTaCuocHen() + " - Liên hệ: " + snapshot.getValue(UserData.class).getsSdt());
                        }

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
        else if(userID.equals(appointment.getNguoiDuocHenID())){
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getsUserID().equals(appointment.getNguoiHenID())){
                        viewHolder.txtNguoiHen.setText("Bạn có một cuộc hẹn với " + snapshot.getValue(UserData.class).getsFullName() + " vào ngày " + appointment.getNgayHen());
                        if(appointment.getMoTaCuocHen().length() > 30){
                            viewHolder.txtChiTiet.setText("Chi tiết: " + appointment.getMoTaCuocHen().substring(0, 30) + "..." + " - Liên hệ: " + snapshot.getValue(UserData.class).getsSdt());
                        }
                        else{
                            viewHolder.txtChiTiet.setText("Chi tiết: " + appointment.getMoTaCuocHen() + " - Liên hệ: " + snapshot.getValue(UserData.class).getsSdt());
                        }

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

        return convertView;
    }
}
