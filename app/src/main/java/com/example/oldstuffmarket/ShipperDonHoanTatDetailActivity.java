package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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

public class ShipperDonHoanTatDetailActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgSP;
    private TextView txtTenSP, txtSoLuongSP, txtTongGiaTriDonHang, txtSellerLabel, txtSellerDiaChi, txtSellerSDT;
    private CheckBox cbLayHangThanhCong, cbDangGiaoHang;
    private Button btnGiaoThanhCong, btnGiaoThatBai, btnBack;
    private String userName, userID, donHangID, nguoiMuaID, nguoiBanID;
    private int loaiDonHang = 0, commission = 0, truTienCheck = 0, hoanTienCheck = 0;
    private long tongGiaTri;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.shipper_don_hoan_tat_detail_layout);

        imgSP = (ImageView) findViewById(R.id.imgSP);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtTongGiaTriDonHang = (TextView) findViewById(R.id.txtTongGiaTriDonHang);
        txtSellerLabel = (TextView) findViewById(R.id.txtSellerLabel);
        txtSellerDiaChi = (TextView) findViewById(R.id.txtSellerDiaChi);
        txtSellerSDT = (TextView) findViewById(R.id.txtSellerSDT);
        cbLayHangThanhCong = (CheckBox) findViewById(R.id.cbLayHangThanhCong);
        cbDangGiaoHang = (CheckBox) findViewById(R.id.cbDangGiaoHang);
        btnGiaoThatBai = (Button) findViewById(R.id.btnGiaoThatBai);
        btnGiaoThanhCong = (Button) findViewById(R.id.btnGiaoThanhCong);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(backClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");
            donHangID = getIntent().getExtras().getString("DonHangID");

            databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(OrderData.class).getDonHangID().equals(donHangID)){
                        nguoiMuaID = snapshot.getValue(OrderData.class).getNguoiMuaID();
                        nguoiBanID = snapshot.getValue(OrderData.class).getNguoiBanID();
                        loaiDonHang = snapshot.getValue(OrderData.class).getLoaiDonHang();
                        tongGiaTri = snapshot.getValue(OrderData.class).getGiaTien();

                        storageReference.child(snapshot.getValue(OrderData.class).getSanPham().getsSPImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(ShipperDonHoanTatDetailActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        if(snapshot.getValue(OrderData.class).getSanPham().getiTinhTrang() == 0){
                            txtTenSP.setText(snapshot.getValue(OrderData.class).getSanPham().getsTenSP() + " - New");
                        }
                        else if(snapshot.getValue(OrderData.class).getSanPham().getiTinhTrang() == 1){
                            txtTenSP.setText(snapshot.getValue(OrderData.class).getSanPham().getsTenSP() + " - 2nd");
                        }

                        txtSoLuongSP.setText("Số lượng: " + String.valueOf(snapshot.getValue(OrderData.class).getSanPham().getiSoLuong()));

                        long money = snapshot.getValue(OrderData.class).getGiaTien() - (snapshot.getValue(OrderData.class).getGiaTien() * snapshot.getValue(OrderData.class).getSellerCommission() / 100);

                        if(snapshot.getValue(OrderData.class).getTinhTrang() == -1){
                            txtTongGiaTriDonHang.setText("Trả hàng");
                        }
                        else if(snapshot.getValue(OrderData.class).getTinhTrang() == 7){
                            txtTongGiaTriDonHang.setText("Số tiền cần trả: " + String.valueOf(money) + "vnđ");
                        }

                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiBanID)){
                                    commission = snapshot.getValue(UserData.class).getiCommission();
                                    txtSellerLabel.setText("Họ tên người bán: " + snapshot.getValue(UserData.class).getsFullName());
                                    txtSellerDiaChi.setText("Địa chỉ: " + snapshot.getValue(UserData.class).getsDiaChi());
                                    txtSellerSDT.setText("Liên hệ: " + snapshot.getValue(UserData.class).getsSdt());
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
        }
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ShipperDonHoanTatActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}