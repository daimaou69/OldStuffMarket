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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.data_models.OrderData;
import com.example.oldstuffmarket.data_models.SanPham;
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

public class UserOrderConfirmActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgSP;
    private EditText edtUserName, edtDiaChi, edtLienHe;
    private TextView txtTenSP, txtSoLuongSP, txtGiaSP, txtPhuongThucThanhToan, txtTongGiaTriDonHang, txtNameLabel, txtDiaChi;
    private Button btnAccept, btnRefuse, btnBack;
    private Intent intent;
    private String userName, userID, donHangID, nguoiBanID, nguoiMuaID, productID;
    private int loaiDonHang, sellerCommission;
    private long tongGiaTri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_order_confirm_layout);

        imgSP = (ImageView) findViewById(R.id.imgSP);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtLienHe = (EditText) findViewById(R.id.edtLienHe);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        txtPhuongThucThanhToan = (TextView) findViewById(R.id.txtPhuongThucThanhToan);
        txtTongGiaTriDonHang = (TextView) findViewById(R.id.txtTongGiaTriDonHang);
        txtNameLabel = (TextView) findViewById(R.id.txtNameLabel);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnRefuse = (Button) findViewById(R.id.btnRefuse);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(backClick);
        btnRefuse.setOnClickListener(refuseClick);
        btnAccept.setOnClickListener(acceptClick);
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
                        nguoiBanID = snapshot.getValue(OrderData.class).getNguoiBanID();
                        nguoiMuaID = snapshot.getValue(OrderData.class).getNguoiMuaID();
                        loaiDonHang = snapshot.getValue(OrderData.class).getLoaiDonHang();
                        tongGiaTri = snapshot.getValue(OrderData.class).getGiaTien();
                        productID = snapshot.getValue(OrderData.class).getSanPham().getsID();
                        storageReference.child(snapshot.getValue(OrderData.class).getSanPham().getsSPImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(UserOrderConfirmActivity.this).load(uri).into(imgSP);
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
                        else {
                            txtTenSP.setText(snapshot.getValue(OrderData.class).getSanPham().getsTenSP() + " - 2nd");
                        }

                        txtSoLuongSP.setText("Số lượng: " + String.valueOf(snapshot.getValue(OrderData.class).getSanPham().getiSoLuong()) + " x ");
                        txtGiaSP.setText(String.valueOf(snapshot.getValue(OrderData.class).getGiaTien()) + "vnđ");
                        txtTongGiaTriDonHang.setText(String.valueOf(snapshot.getValue(OrderData.class).getGiaTien()) + "vnđ");

                        String diaChi = snapshot.getValue(OrderData.class).getDiaChi();
                        String lienHe = snapshot.getValue(OrderData.class).getSoDienThoai();

                        if(snapshot.getValue(OrderData.class).getLoaiDonHang() == 1){
                            txtPhuongThucThanhToan.setText("Thanh toán trực tiếp!");

                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiBanID)){
                                        sellerCommission = snapshot.getValue(UserData.class).getiCommission();
                                        edtUserName.setText(snapshot.getValue(UserData.class).getsFullName());
                                        edtDiaChi.setText(snapshot.getValue(UserData.class).getsDiaChi());
                                        edtLienHe.setText(snapshot.getValue(UserData.class).getsSdt());
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
                        else if(snapshot.getValue(OrderData.class).getLoaiDonHang() == 2 || snapshot.getValue(OrderData.class).getLoaiDonHang() == 3){
                            if(snapshot.getValue(OrderData.class).getLoaiDonHang() == 2){
                                txtPhuongThucThanhToan.setText("Thanh toán COD!");

                            }
                            else{
                                txtPhuongThucThanhToan.setText("Thanh toán E-Wallet!");
                            }
                            if(snapshot.getValue(OrderData.class).getTinhTrang() < 3){
                                btnAccept.setEnabled(false);
                                btnRefuse.setEnabled(false);
                            }
                            txtDiaChi.setText("Địa chỉ giao hàng: ");
                            txtNameLabel.setText("Họ tên người mua: ");
                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiMuaID)){
                                        edtUserName.setText(snapshot.getValue(UserData.class).getsFullName());
                                        edtDiaChi.setText(diaChi);
                                        edtLienHe.setText(lienHe);
                                    }
                                    if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiBanID)){
                                        sellerCommission = snapshot.getValue(UserData.class).getiCommission();
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

    View.OnClickListener acceptClick = new View.OnClickListener() {
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
                                    if(snapshot.getValue(OrderData.class).getDonHangID().equals(donHangID)){
                                        OrderData orderData = new OrderData(snapshot.getValue(OrderData.class).getDonHangID(), snapshot.getValue(OrderData.class).getNguoiMuaID(), snapshot.getValue(OrderData.class).getNguoiBanID(),
                                                snapshot.getValue(OrderData.class).getNgayTaoDonHang(), snapshot.getValue(OrderData.class).getSoDienThoai(), snapshot.getValue(OrderData.class).getDiaChi(),
                                                snapshot.getValue(OrderData.class).getSanPham(), snapshot.getValue(OrderData.class).getLoaiDonHang(), 4, sellerCommission, snapshot.getValue(OrderData.class).getGiaTien(), snapshot.getValue(OrderData.class).getShipperID());
                                        databaseReference.child("LichSuGiaoDich").child(snapshot.getValue(OrderData.class).getDonHangID()).setValue(orderData);
                                        databaseReference.child("DonHang").child(snapshot.getValue(OrderData.class).getDonHangID()).removeValue();
                                        nguoiBanID = snapshot.getValue(OrderData.class).getNguoiBanID();
                                        long giaTriDonHang = snapshot.getValue(OrderData.class).getGiaTien();
                                        int soLuongSP = snapshot.getValue(OrderData.class).getSanPham().getiSoLuong();
                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiBanID)){
                                                    int accPoint = snapshot.getValue(UserData.class).getiAccPoint() + 2;
                                                    int newSL = snapshot.getValue(UserData.class).getiSoSPDaBan() + soLuongSP;
                                                    double commission = giaTriDonHang * snapshot.getValue(UserData.class).getiCommission() / 100;
                                                    long newBalance = snapshot.getValue(UserData.class).getlMoney() - (long)commission;
                                                    if(newBalance < 0){
//                                                        UserData userData = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),
//                                                                snapshot.getValue(UserData.class).getsSdt(), snapshot.getValue(UserData.class).getsGioiTinh(), snapshot.getValue(UserData.class).getsDiaChi(),
//                                                                snapshot.getValue(UserData.class).getsPassword(), snapshot.getValue(UserData.class).getsImage(), snapshot.getValue(UserData.class).getsUserID(),
//                                                                snapshot.getValue(UserData.class).getsNgayThamGia(), snapshot.getValue(UserData.class).getiPermission(), snapshot.getValue(UserData.class).getiCommission(), -1,
//                                                                newSL, accPoint, snapshot.getValue(UserData.class).getiReport(), newBalance);
//                                                        databaseReference.child("User").child(snapshot.getValue(UserData.class).getsUserID()).setValue(userData);
                                                        String reportID = databaseReference.push().getKey();
                                                        UserReport userReport = new UserReport(reportID, nguoiBanID, "Số dư ví thấp hơn 0: " + newBalance + "vnđ", 0, false);
                                                        databaseReference.child("Report").child(reportID).setValue(userReport);
                                                        UserData userData = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),
                                                                snapshot.getValue(UserData.class).getsSdt(), snapshot.getValue(UserData.class).getsGioiTinh(), snapshot.getValue(UserData.class).getsDiaChi(),
                                                                snapshot.getValue(UserData.class).getsPassword(), snapshot.getValue(UserData.class).getsImage(), snapshot.getValue(UserData.class).getsUserID(),
                                                                snapshot.getValue(UserData.class).getsNgayThamGia(), snapshot.getValue(UserData.class).getiPermission(), snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(),
                                                                newSL, accPoint, snapshot.getValue(UserData.class).getiReport(), snapshot.getValue(UserData.class).getlMoney());
                                                        databaseReference.child("User").child(snapshot.getValue(UserData.class).getsUserID()).setValue(userData);
                                                        finish();

                                                        intent = new Intent(v.getContext(), CommentActivity.class);
                                                        intent.putExtra("ProductID", productID);
                                                        intent.putExtra("UserID", userID);
                                                        intent.putExtra("SellerID", nguoiBanID);
                                                        intent.putExtra("UserName", userName);
                                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                        startActivity(intent);
                                                    }
                                                    else{
                                                        UserData userData = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),
                                                                snapshot.getValue(UserData.class).getsSdt(), snapshot.getValue(UserData.class).getsGioiTinh(), snapshot.getValue(UserData.class).getsDiaChi(),
                                                                snapshot.getValue(UserData.class).getsPassword(), snapshot.getValue(UserData.class).getsImage(), snapshot.getValue(UserData.class).getsUserID(),
                                                                snapshot.getValue(UserData.class).getsNgayThamGia(), snapshot.getValue(UserData.class).getiPermission(), snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(),
                                                                newSL, accPoint, snapshot.getValue(UserData.class).getiReport(), newBalance);
                                                        databaseReference.child("User").child(snapshot.getValue(UserData.class).getsUserID()).setValue(userData);

                                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                                            @Override
                                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                                if(snapshot.getValue(UserData.class).getsUserName().equals("admin")){
                                                                    long money = snapshot.getValue(UserData.class).getlMoney() + (long)commission;
                                                                    UserData userAdmin = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),
                                                                            snapshot.getValue(UserData.class).getsSdt(), snapshot.getValue(UserData.class).getsGioiTinh(), snapshot.getValue(UserData.class).getsDiaChi(),
                                                                            snapshot.getValue(UserData.class).getsPassword(), snapshot.getValue(UserData.class).getsImage(), snapshot.getValue(UserData.class).getsUserID(),
                                                                            snapshot.getValue(UserData.class).getsNgayThamGia(), snapshot.getValue(UserData.class).getiPermission(), snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(),
                                                                            snapshot.getValue(UserData.class).getiSoSPDaBan(), snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(), money);
                                                                    databaseReference.child("User").child(snapshot.getValue(UserData.class).getsUserID()).setValue(userAdmin);

                                                                    finish();

                                                                    intent = new Intent(v.getContext(), CommentActivity.class);
                                                                    intent.putExtra("ProductID", productID);
                                                                    intent.putExtra("UserID", userID);
                                                                    intent.putExtra("SellerID", nguoiBanID);
                                                                    intent.putExtra("UserName", userName);
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
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
            alert.setMessage("Đồng ý nhận hàng?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
        }
    };

    View.OnClickListener refuseClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(loaiDonHang == 2){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(OrderData.class).getDonHangID().equals(donHangID)){
                                            String sanPhamID = snapshot.getValue(OrderData.class).getSanPham().getsID();
                                            int soLuongSP = snapshot.getValue(OrderData.class).getSanPham().getiSoLuong();
                                            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                    if(snapshot.getValue(SanPham.class).getsID().equals(sanPhamID)){
                                                        int newSL = snapshot.getValue(SanPham.class).getiSoLuong() + soLuongSP;
                                                        SanPham sanPham = new SanPham(snapshot.getValue(SanPham.class).getsID(), snapshot.getValue(SanPham.class).getsUserID(),
                                                                snapshot.getValue(SanPham.class).getsShopID(), snapshot.getValue(SanPham.class).getsTenSP(),snapshot.getValue(SanPham.class).getsSPImage(),
                                                                snapshot.getValue(SanPham.class).getsMoTa(),snapshot.getValue(SanPham.class).getsDanhMuc(), snapshot.getValue(SanPham.class).getsNgayDang(),
                                                                snapshot.getValue(SanPham.class).getsDiaChiDang(), snapshot.getValue(SanPham.class).getlGiaTien(),newSL,snapshot.getValue(SanPham.class).getiTinhTrang());
                                                        databaseReference.child("SanPham").child(snapshot.getValue(SanPham.class).getsID()).setValue(sanPham);
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

                                            OrderData orderData = new OrderData(snapshot.getValue(OrderData.class).getDonHangID(), snapshot.getValue(OrderData.class).getNguoiMuaID(), snapshot.getValue(OrderData.class).getNguoiBanID(),
                                                    snapshot.getValue(OrderData.class).getNgayTaoDonHang(), snapshot.getValue(OrderData.class).getSoDienThoai(), snapshot.getValue(OrderData.class).getDiaChi(),
                                                    snapshot.getValue(OrderData.class).getSanPham(), snapshot.getValue(OrderData.class).getLoaiDonHang(), -1, sellerCommission, snapshot.getValue(OrderData.class).getGiaTien(), snapshot.getValue(OrderData.class).getShipperID());
                                            databaseReference.child("LichSuGiaoDich").child(snapshot.getValue(OrderData.class).getDonHangID()).setValue(orderData);
                                            databaseReference.child("DonHang").child(snapshot.getValue(OrderData.class).getDonHangID()).removeValue();

                                            finish();

                                            intent = new Intent(v.getContext(), CommentActivity.class);
                                            intent.putExtra("ProductID", productID);
                                            intent.putExtra("UserID", userID);
                                            intent.putExtra("SellerID", nguoiBanID);
                                            intent.putExtra("UserName", userName);
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
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn từ chối nhận hàng?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
            else if(loaiDonHang == 3){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(OrderData.class).getDonHangID().equals(donHangID)){
                                            String sanPhamID = snapshot.getValue(OrderData.class).getSanPham().getsID();
                                            int soLuongSP = snapshot.getValue(OrderData.class).getSanPham().getiSoLuong();
                                            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                    if(snapshot.getValue(SanPham.class).getsID().equals(sanPhamID)){
                                                        int newSL = snapshot.getValue(SanPham.class).getiSoLuong() + soLuongSP;
                                                        SanPham sanPham = new SanPham(snapshot.getValue(SanPham.class).getsID(), snapshot.getValue(SanPham.class).getsUserID(),
                                                                snapshot.getValue(SanPham.class).getsShopID(), snapshot.getValue(SanPham.class).getsTenSP(),snapshot.getValue(SanPham.class).getsSPImage(),
                                                                snapshot.getValue(SanPham.class).getsMoTa(),snapshot.getValue(SanPham.class).getsDanhMuc(), snapshot.getValue(SanPham.class).getsNgayDang(),
                                                                snapshot.getValue(SanPham.class).getsDiaChiDang(), snapshot.getValue(SanPham.class).getlGiaTien(),newSL,snapshot.getValue(SanPham.class).getiTinhTrang());
                                                        databaseReference.child("SanPham").child(snapshot.getValue(SanPham.class).getsID()).setValue(sanPham);
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

                                            OrderData orderData = new OrderData(snapshot.getValue(OrderData.class).getDonHangID(), snapshot.getValue(OrderData.class).getNguoiMuaID(), snapshot.getValue(OrderData.class).getNguoiBanID(),
                                                    snapshot.getValue(OrderData.class).getNgayTaoDonHang(), snapshot.getValue(OrderData.class).getSoDienThoai(), snapshot.getValue(OrderData.class).getDiaChi(),
                                                    snapshot.getValue(OrderData.class).getSanPham(), snapshot.getValue(OrderData.class).getLoaiDonHang(), -1, sellerCommission, snapshot.getValue(OrderData.class).getGiaTien(), snapshot.getValue(OrderData.class).getShipperID());
                                            databaseReference.child("LichSuGiaoDich").child(snapshot.getValue(OrderData.class).getDonHangID()).setValue(orderData);
                                            databaseReference.child("DonHang").child(snapshot.getValue(OrderData.class).getDonHangID()).removeValue();

                                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                    if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiBanID)){
                                                        long newBalance = snapshot.getValue(UserData.class).getlMoney() - tongGiaTri;
                                                        databaseReference.child("User").child(snapshot.getKey()).child("lMoney").setValue(newBalance);
                                                    }
                                                    else if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiMuaID)){
                                                        long newBalance = snapshot.getValue(UserData.class).getlMoney() + tongGiaTri;
                                                        databaseReference.child("User").child(snapshot.getKey()).child("lMoney").setValue(newBalance);
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

                                            Handler handler = new Handler();
                                            int delay = 1500;
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    finish();

                                                    intent = new Intent(v.getContext(), CommentActivity.class);
                                                    intent.putExtra("ProductID", productID);
                                                    intent.putExtra("UserID", userID);
                                                    intent.putExtra("SellerID", nguoiBanID);
                                                    intent.putExtra("UserName", userName);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                    startActivity(intent);
                                                }
                                            }, delay);

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
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn từ chối nhận hàng và lấy lại tiền?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            intent = new Intent(v.getContext(), DonMuaActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };
}