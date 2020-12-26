package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.example.oldstuffmarket.adapter.ShipperAdapter;
import com.example.oldstuffmarket.data_models.OrderData;
import com.example.oldstuffmarket.data_models.SanPham;
import com.example.oldstuffmarket.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShipperSelectingActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<UserData> userDataArrayList;
    private GridView gridShipper;
    private Button btnBack;
    private EditText edtFind;
    private Intent intent;
    private String userName, userID, donHangID, shipperID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.shipper_selecting_layout);

        gridShipper = (GridView) findViewById(R.id.gridShipper);
        btnBack = (Button) findViewById(R.id.btnBack);
        edtFind = (EditText) findViewById(R.id.edtFind);

        btnBack.setOnClickListener(backClick);

        gridShipper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(OrderData.class).getDonHangID().equals(donHangID)){
                                            String donHangID = snapshot.getValue(OrderData.class).getDonHangID();
                                            String nguoiMuaID = snapshot.getValue(OrderData.class).getNguoiMuaID();
                                            String nguoiBanID = snapshot.getValue(OrderData.class).getNguoiBanID();
                                            String ngayTaoDon = snapshot.getValue(OrderData.class).getNgayTaoDonHang();
                                            String sdt = snapshot.getValue(OrderData.class).getSoDienThoai();
                                            String diaChi = snapshot.getValue(OrderData.class).getDiaChi();
                                            SanPham sanPham = snapshot.getValue(OrderData.class).getSanPham();
                                            int loaiDonHang = snapshot.getValue(OrderData.class).getLoaiDonHang();
                                            int sellerCommission = snapshot.getValue(OrderData.class).getSellerCommission();
                                            long giaTien = snapshot.getValue(OrderData.class).getGiaTien();


                                            if(!shipperID.isEmpty() && !shipperID.equals(userDataArrayList.get(position).getsUserID())){
                                                databaseReference.child("Shipper").child(shipperID).child(donHangID).removeValue();
                                            }


                                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                    if(snapshot.getValue(UserData.class).getsUserID().equals(userDataArrayList.get(position).getsUserID())){

                                                        OrderData orderData = new OrderData(donHangID, nguoiMuaID, nguoiBanID,
                                                                ngayTaoDon, sdt, diaChi,
                                                                sanPham, loaiDonHang, 4, sellerCommission, giaTien, snapshot.getValue(UserData.class).getsUserID());

                                                        databaseReference.child("DonHang").child(donHangID).setValue(orderData);

                                                        databaseReference.child("Shipper").child(snapshot.getValue(UserData.class).getsUserID()).child(donHangID).setValue(orderData);

                                                        finish();
                                                        intent = new Intent(view.getContext(), OrderProccessingForSellerActivity.class);
                                                        intent.putExtra("UserName", userName);
                                                        intent.putExtra("UserID", userID);
                                                        intent.putExtra("DonHangID", donHangID);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                        startActivity(intent);
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
                                return;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setMessage("Bạn chọn giao đơn hàng cho shipper " + userDataArrayList.get(position).getsFullName() + "?").setPositiveButton("Yes", dialog).setNegativeButton("No", dialog).show();

            }
        });

        edtFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    userDataArrayList.clear();
                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getValue(UserData.class).getiPermission() == 3){
                                if(snapshot.getValue(UserData.class).getsSdt().contains(edtFind.getText().toString()) || snapshot.getValue(UserData.class).getsFullName().toLowerCase().contains(edtFind.getText().toString().toLowerCase()) || snapshot.getValue(UserData.class).getsUserName().toLowerCase().contains(edtFind.getText().toString().toLowerCase())){
                                    userDataArrayList.add(snapshot.getValue(UserData.class));
                                }
                            }
                            shipperLoad();
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
                else{
                    userDataArrayList.clear();
                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getValue(UserData.class).getiPermission() == 3){
                                userDataArrayList.add(snapshot.getValue(UserData.class));
                            }
                            shipperLoad();
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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        userDataArrayList = new ArrayList<>();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");
            donHangID = getIntent().getExtras().getString("DonHangID");
            shipperID = getIntent().getExtras().getString("ShipperID");

            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getiPermission() == 3){
                        userDataArrayList.add(snapshot.getValue(UserData.class));
                    }
                    shipperLoad();
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
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            intent = new Intent(v.getContext(), OrderProccessingForSellerActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.putExtra("ShipperID", "");
            intent.putExtra("DonHangID", donHangID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    public void shipperLoad(){
        ShipperAdapter shipperAdapter = new ShipperAdapter(ShipperSelectingActivity.this, R.layout.shipper_adapter_layout, userDataArrayList);
        gridShipper.setAdapter(shipperAdapter);
    }
}