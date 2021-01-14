package com.example.oldstuffmarket.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.BuildConfig;
import com.example.oldstuffmarket.DanhSachUserActivity;
import com.example.oldstuffmarket.R;
import com.example.oldstuffmarket.SP_Report_admin_Activity;
import com.example.oldstuffmarket.data_models.Comment;
import com.example.oldstuffmarket.data_models.LockUser;
import com.example.oldstuffmarket.data_models.ProductReport;
import com.example.oldstuffmarket.data_models.SanPham;
import com.example.oldstuffmarket.data_models.ShopData;
import com.example.oldstuffmarket.data_models.UserData;
import com.example.oldstuffmarket.data_models.UserReport;
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

public class LockUserAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<LockUser> lockUserArrayList;

    public LockUserAdapter(Context context, int layout, ArrayList<LockUser> lockUserArrayList) {
        this.context = context;
        this.layout = layout;
        this.lockUserArrayList = lockUserArrayList;
    }

    @Override
    public int getCount() {
        return lockUserArrayList.size();
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
        TextView txtUserName;
        RadioGroup radGroup;
        RadioButton radDisable;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
            viewHolder.imgUser = (ImageView) convertView.findViewById(R.id.imgUser);
            viewHolder.txtUserName = (TextView) convertView.findViewById(R.id.txtUserName);
            viewHolder.radGroup = (RadioGroup) convertView.findViewById(R.id.radGroup);
            viewHolder.radDisable = (RadioButton) convertView.findViewById(R.id.radDisable);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LockUser lockUser = lockUserArrayList.get(position);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        View finalConvertView = convertView;
        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue(UserData.class).getsUserID().equals(lockUser.getUserID())) {
                    String sID = snapshot.getKey();
                    String userID = snapshot.getValue(UserData.class).getsUserID();
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
                    viewHolder.txtUserName.setText(snapshot.getValue(UserData.class).getsUserName());
                    viewHolder.radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            switch (i) {
                                case R.id.radActive:
                                    DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    databaseReference.child("LockUser").addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            if (snapshot.getValue(LockUser.class).getUserID().equals(userID)) {
                                                                databaseReference.child("LockUser").child(snapshot.getKey()).removeValue();
                                                                databaseReference.child("User").child(sID).child("iTinhTrang").setValue(0);
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
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:

                                                    viewHolder.radDisable.setChecked(true);
                                                    return;
                                            }
                                        }
                                    };
                                    AlertDialog.Builder alert = new AlertDialog.Builder(finalConvertView.getContext());
                                    alert.setMessage("Bạn chắn chắn muốn Active User!").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
                                    break;
                                case R.id.radRemove:
                                    DialogInterface.OnClickListener dialoga = new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    databaseReference.child("LockUser").addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            if (snapshot.getValue(LockUser.class).getUserID().equals(userID)) {
                                                                databaseReference.child("LockUser").child(snapshot.getKey()).removeValue();
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
                                                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            if (snapshot.getValue(UserData.class).getsUserID().equals(userID)) {
                                                                databaseReference.child("User").child(snapshot.getKey()).removeValue();
                                                                UserData userData = new UserData(snapshot.getValue(UserData.class).getsUserName(), snapshot.getValue(UserData.class).getsShopID(),
                                                                        snapshot.getValue(UserData.class).getsFullName(), snapshot.getValue(UserData.class).getsSdt(),
                                                                        snapshot.getValue(UserData.class).getsGioiTinh(), snapshot.getValue(UserData.class).getsDiaChi(),
                                                                        snapshot.getValue(UserData.class).getsPassword(), snapshot.getValue(UserData.class).getsImage(),
                                                                        snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(),
                                                                        snapshot.getValue(UserData.class).getiPermission(), snapshot.getValue(UserData.class).getiCommission(),
                                                                        snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                                        snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(),
                                                                        snapshot.getValue(UserData.class).getlMoney());
                                                                databaseReference.child("BlackList").child(userID).setValue(userData);
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
                                                    databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            if (snapshot.getValue(ShopData.class).getUserID().equals(userID)) {
                                                                databaseReference.child("Shop").child(snapshot.getKey()).removeValue();
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
                                                    databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            if (snapshot.getValue(SanPham.class).getsUserID().equals(lockUser.getUserID())) {
                                                                databaseReference.child("SanPham").child(snapshot.getKey()).removeValue();
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
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    viewHolder.radDisable.setChecked(true);
                                                    return;
                                            }
                                        }
                                    };
                                    AlertDialog.Builder alertt = new AlertDialog.Builder(finalConvertView.getContext());
                                    alertt.setMessage("Bạn chắn chắn muốn Remove User!").setNegativeButton("No", dialoga).setPositiveButton("Yes", dialoga).show();
                                    break;
                            }
                        }
                    });
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
