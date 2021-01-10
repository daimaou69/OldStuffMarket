package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class ShipperXuLiDonHangActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgSP;
    private TextView txtTenSP, txtSoLuongSP, txtGiaSP, txtHoTenNguoiMua, txtDiaChi, txtLienHe, txtTongGiaTriDonHang, txtSellerLabel, txtSellerDiaChi, txtSellerSDT;
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
        setContentView(R.layout.shipper_xu_li_don_hang_layout);

        imgSP = (ImageView) findViewById(R.id.imgSP);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        txtHoTenNguoiMua = (TextView) findViewById(R.id.txtHoTenNguoiMua);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        txtLienHe = (TextView) findViewById(R.id.txtLienHe);
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
        cbDangGiaoHang.setOnCheckedChangeListener(giaoHangCheck);
        cbLayHangThanhCong.setOnCheckedChangeListener(layHangThanhCongCheck);
        btnGiaoThatBai.setOnClickListener(giaoThatBaiClick);
        btnGiaoThanhCong.setOnClickListener(giaoThanhCongClick);
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
                                Glide.with(ShipperXuLiDonHangActivity.this).load(uri).into(imgSP);
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

                        txtSoLuongSP.setText("Số lượng: " + String.valueOf(snapshot.getValue(OrderData.class).getSanPham().getiSoLuong()) + " x ");
                        txtGiaSP.setText(String.valueOf(snapshot.getValue(OrderData.class).getSanPham().getlGiaTien()) + "vnđ");
                        if(snapshot.getValue(OrderData.class).getLoaiDonHang() == 3){
                            txtTongGiaTriDonHang.setText("Đã thanh toán");
                        }
                        else{
                            txtTongGiaTriDonHang.setText("Số tiền cần thu: " + String.valueOf(snapshot.getValue(OrderData.class).getGiaTien()) + "vnđ");
                        }
                        txtDiaChi.setText("Địa chỉ: " + snapshot.getValue(OrderData.class).getDiaChi());
                        txtLienHe.setText("Liên hệ: " + snapshot.getValue(OrderData.class).getSoDienThoai());

                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiMuaID)){
                                    txtHoTenNguoiMua.setText("Họ tên người nhận: " + snapshot.getValue(UserData.class).getsFullName());

                                }
                                else if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiBanID)){
                                    commission = snapshot.getValue(UserData.class).getiCommission();
                                    txtSellerLabel.setText("Họ tên người bán: " + snapshot.getValue(UserData.class).getsFullName());
                                    txtSellerDiaChi.setText("Địa chỉ lấy hàng: " + snapshot.getValue(UserData.class).getsDiaChi());
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

                        if(snapshot.getValue(OrderData.class).getTinhTrang() < 5){
                            cbLayHangThanhCong.setChecked(false);
                            cbLayHangThanhCong.setEnabled(true);
                            cbDangGiaoHang.setChecked(false);
                            txtHoTenNguoiMua.setVisibility(View.GONE);
                            txtDiaChi.setVisibility(View.GONE);
                            txtLienHe.setVisibility(View.GONE);
                        }
                        else if(snapshot.getValue(OrderData.class).getTinhTrang() < 6){
                            cbDangGiaoHang.setChecked(false);
                            cbDangGiaoHang.setEnabled(true);

                            txtSellerLabel.setVisibility(View.GONE);
                            txtSellerDiaChi.setVisibility(View.GONE);
                            txtSellerSDT.setVisibility(View.GONE);
                        }

                        if(snapshot.getValue(OrderData.class).getTinhTrang() >= 6){
                            btnGiaoThanhCong.setVisibility(View.VISIBLE);
                            btnGiaoThatBai.setVisibility(View.VISIBLE);

                            txtSellerLabel.setVisibility(View.GONE);
                            txtSellerDiaChi.setVisibility(View.GONE);
                            txtSellerSDT.setVisibility(View.GONE);
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

    }

    CompoundButton.OnCheckedChangeListener layHangThanhCongCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked == true){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                cbLayHangThanhCong.setEnabled(false);
                                cbDangGiaoHang.setEnabled(true);

                                txtSellerLabel.setVisibility(View.GONE);
                                txtSellerDiaChi.setVisibility(View.GONE);
                                txtSellerSDT.setVisibility(View.GONE);

                                txtHoTenNguoiMua.setVisibility(View.VISIBLE);
                                txtDiaChi.setVisibility(View.VISIBLE);
                                txtLienHe.setVisibility(View.VISIBLE);

                                databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(OrderData.class).getDonHangID().equals(donHangID)){
                                            OrderData orderUpdate = new OrderData(snapshot.getValue(OrderData.class).getDonHangID(), snapshot.getValue(OrderData.class).getNguoiMuaID(),
                                                    snapshot.getValue(OrderData.class).getNguoiBanID(), snapshot.getValue(OrderData.class).getNgayTaoDonHang(), snapshot.getValue(OrderData.class).getSoDienThoai(),
                                                    snapshot.getValue(OrderData.class).getDiaChi(), snapshot.getValue(OrderData.class).getSanPham(), snapshot.getValue(OrderData.class).getLoaiDonHang(), 5, snapshot.getValue(OrderData.class).getSellerCommission(), snapshot.getValue(OrderData.class).getGiaTien(), snapshot.getValue(OrderData.class).getShipperID());
                                            databaseReference.child("DonHang").child(donHangID).setValue(orderUpdate);
                                            databaseReference.child("Shipper").child(userID).child(donHangID).setValue(orderUpdate);
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
                                cbLayHangThanhCong.setChecked(false);
                                return;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(ShipperXuLiDonHangActivity.this);
                alert.setMessage("Lấy hàng người bán thành công?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
        }
    };

    CompoundButton.OnCheckedChangeListener giaoHangCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked == true){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                cbDangGiaoHang.setEnabled(false);
                                btnGiaoThanhCong.setVisibility(View.VISIBLE);
                                btnGiaoThatBai.setVisibility(View.VISIBLE);
                                databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(OrderData.class).getDonHangID().equals(donHangID)){
                                            OrderData orderUpdate = new OrderData(snapshot.getValue(OrderData.class).getDonHangID(), snapshot.getValue(OrderData.class).getNguoiMuaID(),
                                                    snapshot.getValue(OrderData.class).getNguoiBanID(), snapshot.getValue(OrderData.class).getNgayTaoDonHang(), snapshot.getValue(OrderData.class).getSoDienThoai(),
                                                    snapshot.getValue(OrderData.class).getDiaChi(), snapshot.getValue(OrderData.class).getSanPham(), snapshot.getValue(OrderData.class).getLoaiDonHang(), 6, snapshot.getValue(OrderData.class).getSellerCommission(), snapshot.getValue(OrderData.class).getGiaTien(), snapshot.getValue(OrderData.class).getShipperID());
                                            databaseReference.child("DonHang").child(donHangID).setValue(orderUpdate);
                                            databaseReference.child("Shipper").child(userID).child(donHangID).setValue(orderUpdate);
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
                                cbDangGiaoHang.setChecked(false);
                                return;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(ShipperXuLiDonHangActivity.this);
                alert.setMessage("Tiến hành giao đơn hàng?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
        }
    };

    View.OnClickListener giaoThanhCongClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(OrderData.class).getDonHangID().equals(donHangID) && snapshot.getValue(OrderData.class).getLoaiDonHang() == 2){

                                        int soLuongSP = snapshot.getValue(OrderData.class).getSanPham().getiSoLuong();
                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiBanID)){
                                                    int newSL = snapshot.getValue(UserData.class).getiSoSPDaBan() + soLuongSP;
                                                    databaseReference.child("User").child(nguoiBanID).child("iSoSPDaBan").setValue(newSL);
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

                                        OrderData orderUpdate = new OrderData(snapshot.getValue(OrderData.class).getDonHangID(), snapshot.getValue(OrderData.class).getNguoiMuaID(),
                                                snapshot.getValue(OrderData.class).getNguoiBanID(), snapshot.getValue(OrderData.class).getNgayTaoDonHang(), snapshot.getValue(OrderData.class).getSoDienThoai(),
                                                snapshot.getValue(OrderData.class).getDiaChi(), snapshot.getValue(OrderData.class).getSanPham(),
                                                snapshot.getValue(OrderData.class).getLoaiDonHang(), 7, commission,
                                                snapshot.getValue(OrderData.class).getGiaTien(), snapshot.getValue(OrderData.class).getShipperID());
                                        databaseReference.child("MoneyIncome").child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("Shipper").child(userID).child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("DonHang").child(donHangID).setValue(orderUpdate);



                                        finish();
                                        intent = new Intent(v.getContext(), ShipperDonDangGiaoActivity.class);
                                        intent.putExtra("UserName", userName);
                                        intent.putExtra("UserID", userID);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(intent);
                                    }
                                    else if(snapshot.getValue(OrderData.class).getDonHangID().equals(donHangID) && snapshot.getValue(OrderData.class).getLoaiDonHang() == 3){
                                        int soLuongSP = snapshot.getValue(OrderData.class).getSanPham().getiSoLuong();
                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiBanID)){
                                                    int newSL = snapshot.getValue(UserData.class).getiSoSPDaBan() + soLuongSP;
                                                    databaseReference.child("User").child(nguoiBanID).child("iSoSPDaBan").setValue(newSL);
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

                                        OrderData orderUpdate = new OrderData(snapshot.getValue(OrderData.class).getDonHangID(), snapshot.getValue(OrderData.class).getNguoiMuaID(),
                                                snapshot.getValue(OrderData.class).getNguoiBanID(), snapshot.getValue(OrderData.class).getNgayTaoDonHang(), snapshot.getValue(OrderData.class).getSoDienThoai(),
                                                snapshot.getValue(OrderData.class).getDiaChi(), snapshot.getValue(OrderData.class).getSanPham(),
                                                snapshot.getValue(OrderData.class).getLoaiDonHang(), 8, commission,
                                                snapshot.getValue(OrderData.class).getGiaTien(), snapshot.getValue(OrderData.class).getShipperID());
                                        databaseReference.child("LichSuGiaoDich").child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("CommentNeeds").child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("Shipper").child(userID).child(donHangID).removeValue();
                                        databaseReference.child("DonHang").child(donHangID).removeValue();

                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiBanID)){
                                                    long newMoney = snapshot.getValue(UserData.class).getlMoney() - (tongGiaTri * commission / 100);
                                                    UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(), snapshot.getValue(UserData.class).getsShopID(),
                                                            snapshot.getValue(UserData.class).getsFullName(), snapshot.getValue(UserData.class).getsSdt(), snapshot.getValue(UserData.class).getsGioiTinh(),
                                                            snapshot.getValue(UserData.class).getsDiaChi(), snapshot.getValue(UserData.class).getsPassword(), snapshot.getValue(UserData.class).getsImage(),
                                                            snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(), snapshot.getValue(UserData.class).getiPermission(),
                                                            snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                            snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(), newMoney);

                                                    databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
                                                    truTienCheck++;
                                                }
                                                else if(snapshot.getValue(UserData.class).getsUserID().equals("-MMR0F6xxKcg9TXwvTfX")){
                                                    long newMoney = snapshot.getValue(UserData.class).getlMoney() + (tongGiaTri * commission / 100);
                                                    UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(), snapshot.getValue(UserData.class).getsShopID(),
                                                            snapshot.getValue(UserData.class).getsFullName(), snapshot.getValue(UserData.class).getsSdt(), snapshot.getValue(UserData.class).getsGioiTinh(),
                                                            snapshot.getValue(UserData.class).getsDiaChi(), snapshot.getValue(UserData.class).getsPassword(), snapshot.getValue(UserData.class).getsImage(),
                                                            snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(), snapshot.getValue(UserData.class).getiPermission(),
                                                            snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                            snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(), newMoney);

                                                    databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
                                                    hoanTienCheck++;
                                                }
                                                if(truTienCheck != 0 && hoanTienCheck != 0){
                                                    finish();
                                                    intent = new Intent(v.getContext(), ShipperDonDangGiaoActivity.class);
                                                    intent.putExtra("UserName", userName);
                                                    intent.putExtra("UserID", userID);
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
            AlertDialog.Builder alert = new AlertDialog.Builder(ShipperXuLiDonHangActivity.this);
            alert.setMessage("Xác nhận giao hàng thành công?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
        }
    };

    View.OnClickListener giaoThatBaiClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(OrderData.class).getDonHangID().equals(donHangID) && snapshot.getValue(OrderData.class).getLoaiDonHang() == 2){
                                        OrderData orderUpdate = new OrderData(snapshot.getValue(OrderData.class).getDonHangID(), snapshot.getValue(OrderData.class).getNguoiMuaID(),
                                                snapshot.getValue(OrderData.class).getNguoiBanID(), snapshot.getValue(OrderData.class).getNgayTaoDonHang(), snapshot.getValue(OrderData.class).getSoDienThoai(),
                                                snapshot.getValue(OrderData.class).getDiaChi(), snapshot.getValue(OrderData.class).getSanPham(),
                                                snapshot.getValue(OrderData.class).getLoaiDonHang(), -1, commission,
                                                snapshot.getValue(OrderData.class).getGiaTien(), snapshot.getValue(OrderData.class).getShipperID());
                                        databaseReference.child("MoneyIncome").child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("Shipper").child(userID).child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("DonHang").child(donHangID).removeValue();

                                        finish();
                                        intent = new Intent(v.getContext(), ShipperDonDangGiaoActivity.class);
                                        intent.putExtra("UserName", userName);
                                        intent.putExtra("UserID", userID);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(intent);
                                    }
                                    else if(snapshot.getValue(OrderData.class).getDonHangID().equals(donHangID) && snapshot.getValue(OrderData.class).getLoaiDonHang() == 3){
                                        OrderData orderUpdate = new OrderData(snapshot.getValue(OrderData.class).getDonHangID(), snapshot.getValue(OrderData.class).getNguoiMuaID(),
                                                snapshot.getValue(OrderData.class).getNguoiBanID(), snapshot.getValue(OrderData.class).getNgayTaoDonHang(), snapshot.getValue(OrderData.class).getSoDienThoai(),
                                                snapshot.getValue(OrderData.class).getDiaChi(), snapshot.getValue(OrderData.class).getSanPham(),
                                                snapshot.getValue(OrderData.class).getLoaiDonHang(), -1, commission,
                                                snapshot.getValue(OrderData.class).getGiaTien(), snapshot.getValue(OrderData.class).getShipperID());
                                        databaseReference.child("LichSuGiaoDich").child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("CommentNeeds").child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("MoneyIncome").child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("Shipper").child(userID).child(donHangID).setValue(orderUpdate);
                                        databaseReference.child("DonHang").child(donHangID).removeValue();

                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiBanID)){
                                                    long newMoney = snapshot.getValue(UserData.class).getlMoney() - tongGiaTri;
                                                    UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(), snapshot.getValue(UserData.class).getsShopID(),
                                                            snapshot.getValue(UserData.class).getsFullName(), snapshot.getValue(UserData.class).getsSdt(), snapshot.getValue(UserData.class).getsGioiTinh(),
                                                            snapshot.getValue(UserData.class).getsDiaChi(), snapshot.getValue(UserData.class).getsPassword(), snapshot.getValue(UserData.class).getsImage(),
                                                            snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(), snapshot.getValue(UserData.class).getiPermission(),
                                                            snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                            snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(), newMoney);

                                                    databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
                                                    truTienCheck++;
                                                }
                                                else if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiMuaID)){
                                                    long newMoney = snapshot.getValue(UserData.class).getlMoney() + tongGiaTri;
                                                    UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(), snapshot.getValue(UserData.class).getsShopID(),
                                                            snapshot.getValue(UserData.class).getsFullName(), snapshot.getValue(UserData.class).getsSdt(), snapshot.getValue(UserData.class).getsGioiTinh(),
                                                            snapshot.getValue(UserData.class).getsDiaChi(), snapshot.getValue(UserData.class).getsPassword(), snapshot.getValue(UserData.class).getsImage(),
                                                            snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(), snapshot.getValue(UserData.class).getiPermission(),
                                                            snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                            snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(), newMoney);

                                                    databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
                                                    hoanTienCheck++;
                                                }
                                                if(truTienCheck != 0 && hoanTienCheck != 0){
                                                    finish();
                                                    intent = new Intent(v.getContext(), ShipperDonDangGiaoActivity.class);
                                                    intent.putExtra("UserName", userName);
                                                    intent.putExtra("UserID", userID);
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
            AlertDialog.Builder alert = new AlertDialog.Builder(ShipperXuLiDonHangActivity.this);
            alert.setMessage("Xác nhận hủy đơn do giao hàng thất bại?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();


        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ShipperDonDangGiaoActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}