package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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

public class ShipperMainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Button btnLogout, btnAccountInfo, btnPassWordChange, btnDanhSachDonHang, btnCacDonDangGiao;
    private TextView txtCacDonDangGiaoNotify, txtShipperAccountName;
    private ArrayList<OrderData> donDangGiao;
    private ImageView imgAccount;
    private String sUserName, userID;
    private Intent intent;
    public static ArrayList<String> shipperList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.shipper_main_layout);

        txtCacDonDangGiaoNotify = (TextView) findViewById(R.id.txtCacDonDangGiaoNotify);
        txtShipperAccountName = (TextView) findViewById(R.id.txtShipperAccountName);
        imgAccount = (ImageView) findViewById(R.id.imgAccount);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnPassWordChange = (Button) findViewById(R.id.btnPassWordChange);
        btnAccountInfo = (Button) findViewById(R.id.btnAccountInfo);
        btnDanhSachDonHang = (Button) findViewById(R.id.btnDanhSachDonHang);
        btnCacDonDangGiao =(Button) findViewById(R.id.btnCacDonDangGiao);

        donDangGiao = new ArrayList<>();
        shipperList = new ArrayList<>();

        btnLogout.setOnClickListener(logoutClick);
        btnAccountInfo.setOnClickListener(accountInfoClick);
        btnPassWordChange.setOnClickListener(passwordChangeClick);
        btnDanhSachDonHang.setOnClickListener(donDaDongGoiClick);
        btnCacDonDangGiao.setOnClickListener(cacDonDangGiaoClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        donDangGiao.clear();
        shipperList.clear();

        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserData.class).getiPermission() == 3){
                    shipperList.add(snapshot.getValue(UserData.class).getsUserName());
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
        if(getIntent().getExtras() != null){

            sUserName = getIntent().getExtras().getString("UserName");

            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getiPermission() == 3){
                        txtShipperAccountName.setText("Shipper - " + snapshot.getValue(UserData.class).getsFullName());
                        userID = snapshot.getValue(UserData.class).getsUserID();
                        if(!snapshot.getValue(UserData.class).getsImage().isEmpty()){
                            final Handler handler = new Handler();
                            final int delay = 1200; //milliseconds
                            handler.postDelayed(new Runnable(){
                                public void run(){
                                    imageLoad(snapshot.getValue(UserData.class).getsImage());
//                                    handler.postDelayed(this, delay);
                                }
                            }, delay);
                        }
                        btnCacDonDangGiao.setVisibility(View.VISIBLE);
                    }
                    else if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getiPermission() == 4){
                        txtShipperAccountName.setText("Trưởng shipper: " + snapshot.getValue(UserData.class).getsFullName());
                        userID = snapshot.getValue(UserData.class).getsUserID();
                        if(!snapshot.getValue(UserData.class).getsImage().isEmpty()){
                            final Handler handler = new Handler();
                            final int delay = 1200; //milliseconds
                            handler.postDelayed(new Runnable(){
                                public void run(){
                                    imageLoad(snapshot.getValue(UserData.class).getsImage());
//                                    handler.postDelayed(this, delay);
                                }
                            }, delay);
                        }
                        btnDanhSachDonHang.setVisibility(View.VISIBLE);
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

            databaseReference.child("Shipper").child(sUserName).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    donDangGiao.add(snapshot.getValue(OrderData.class));
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

            Handler handler = new Handler();
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(donDangGiao.size() != 0){
                        txtCacDonDangGiaoNotify.setVisibility(View.VISIBLE);
                        txtCacDonDangGiaoNotify.setText(String.valueOf(donDangGiao.size()));
                    }
                }
            }, delay);
        }
    }

    View.OnClickListener cacDonDangGiaoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ShipperDonDangGiaoActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener donDaDongGoiClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ShipperDonDaDongGoiActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener passwordChangeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), PasswordChangeActivity.class);
            intent.putExtra("UserName", sUserName);
            startActivity(intent);
        }
    };

    View.OnClickListener accountInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), AccountInfoActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener logoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            finish();
                            intent = new Intent(v.getContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;

                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Bạn muốn đăng xuất?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
        }
    };

    public void imageLoad(String sImageName){
        storageReference.child(sImageName + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ShipperMainActivity.this).load(uri).into(imgAccount);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(AdminMainActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}