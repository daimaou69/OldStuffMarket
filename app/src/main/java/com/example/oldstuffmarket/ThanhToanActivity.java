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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.data_models.OrderData;
import com.example.oldstuffmarket.data_models.SanPham;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThanhToanActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private TextView txtTenSP, txtSoLuongSP, txtGiaSP, txtTongGiaTriDonHang, txtPhuongThucThanhToan;
    private EditText edtUserName, edtDiaChi, edtLienHe;
    private ImageView imgSP;
    private Button btnConfirm, btnBack, btnHome;
    private String userID, nguoiBanID, sanPhamID, userName, navigateTo;
    private boolean paymentCheck = false;
    private int soLuongSP, kieuThanhToan;
    private long tongGiaTri;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.thanh_toan_layout);

        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtLienHe = (EditText) findViewById(R.id.edtLienHe);
        txtPhuongThucThanhToan = (TextView) findViewById(R.id.txtPhuongThucThanhToan);
        txtTongGiaTriDonHang = (TextView) findViewById(R.id.txtTongGiaTriDonHang);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        imgSP = (ImageView) findViewById(R.id.imgSP);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnHome = (Button) findViewById(R.id.btnHome);

        btnBack.setOnClickListener(backClick);
        btnConfirm.setOnClickListener(confirmClick);
        btnHome.setOnClickListener(homeClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
            sanPhamID = getIntent().getExtras().getString("SanPhamID");
            nguoiBanID = getIntent().getExtras().getString("NguoiBanID");
            userID = getIntent().getExtras().getString("UserID");
            soLuongSP = getIntent().getExtras().getInt("SoLuongSP");
            tongGiaTri = getIntent().getExtras().getLong("TongGiaTri");
            kieuThanhToan = getIntent().getExtras().getInt("KieuThanhToan");
            navigateTo = getIntent().getExtras().getString("NavigateTo");

            if(kieuThanhToan == 1){
                txtPhuongThucThanhToan.setText("  Thanh toán trực tiếp");
            }
            else if(kieuThanhToan == 2){
                txtPhuongThucThanhToan.setText("  Thanh toán COD");
            }
            else if(kieuThanhToan == 3){
                txtPhuongThucThanhToan.setText("  Thanh toán trước qua ví điện tử");
                databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.getValue(UserData.class).getsUserID().equals(userID)){
                            if(snapshot.getValue(UserData.class).getlMoney() >= tongGiaTri){
                                paymentCheck = true;
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


            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(SanPham.class).getsID().equals(sanPhamID)){
//                        tongGiaTri = snapshot.getValue(SanPham.class).getlGiaTien() * soLuongSP;
                        storageReference.child(snapshot.getValue(SanPham.class).getsSPImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(ThanhToanActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        txtGiaSP.setText(Long.valueOf(snapshot.getValue(SanPham.class).getlGiaTien()) + "vnđ");
                        txtTongGiaTriDonHang.setText("Tổng tiền: " + String.valueOf(tongGiaTri) + "vnđ");
                        txtTenSP.setText(snapshot.getValue(SanPham.class).getsTenSP());
                        txtSoLuongSP.setText("Số lượng: " + String.valueOf(soLuongSP) + " x ");
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
                    if(snapshot.getValue(UserData.class).getsUserID().equals(userID)){
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
    }

    View.OnClickListener homeClick = new View.OnClickListener() {
        @Override

        public void onClick(View v) {
            finish();
            intent = new Intent(v.getContext(), UserMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener confirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(kieuThanhToan == 2){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                alert.setMessage("Đặt hàng thành công, người bán đang xử lí giao dịch cho bạn!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(SanPham.class).getsID().equals(sanPhamID)){
                                                    int newSL = snapshot.getValue(SanPham.class).getiSoLuong() - soLuongSP;
                                                    SanPham sanPham = new SanPham(snapshot.getValue(SanPham.class).getsID(), snapshot.getValue(SanPham.class).getsUserID(),
                                                            snapshot.getValue(SanPham.class).getsShopID(), snapshot.getValue(SanPham.class).getsTenSP(),snapshot.getValue(SanPham.class).getsSPImage(),
                                                            snapshot.getValue(SanPham.class).getsMoTa(),snapshot.getValue(SanPham.class).getsDanhMuc(), snapshot.getValue(SanPham.class).getsNgayDang(),
                                                            snapshot.getValue(SanPham.class).getsDiaChiDang(), snapshot.getValue(SanPham.class).getlGiaTien(),newSL,snapshot.getValue(SanPham.class).getiTinhTrang());
                                                    databaseReference.child("SanPham").child(snapshot.getKey()).setValue(sanPham);
                                                    SanPham sanPhamOrder = new SanPham(snapshot.getValue(SanPham.class).getsID(), snapshot.getValue(SanPham.class).getsUserID(),
                                                            snapshot.getValue(SanPham.class).getsShopID(), snapshot.getValue(SanPham.class).getsTenSP(),snapshot.getValue(SanPham.class).getsSPImage(),
                                                            snapshot.getValue(SanPham.class).getsMoTa(),snapshot.getValue(SanPham.class).getsDanhMuc(), snapshot.getValue(SanPham.class).getsNgayDang(),
                                                            snapshot.getValue(SanPham.class).getsDiaChiDang(), snapshot.getValue(SanPham.class).getlGiaTien(),soLuongSP,snapshot.getValue(SanPham.class).getiTinhTrang());
                                                    String donHangID = databaseReference.push().getKey();
                                                    long tongGiaTriDonHang = snapshot.getValue(SanPham.class).getlGiaTien() * soLuongSP;
                                                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                                    Date date = new Date();
                                                    OrderData orderData = new OrderData(donHangID, userID, nguoiBanID, dateFormat.format(date), edtLienHe.getText().toString(), edtDiaChi.getText().toString(), sanPhamOrder, 2, 0, 0, tongGiaTriDonHang, "");
                                                    databaseReference.child("DonHang").child(donHangID).setValue(orderData);
                                                    finish();
                                                    intent = new Intent(v.getContext(), UserMainActivity.class);
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
                                }).show();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Xác nhận thông tin và đặt hàng?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }
            else if(kieuThanhToan == 3){
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                if(paymentCheck == true){
                                    thanhToanEWallet(tongGiaTri, userID, nguoiBanID);
                                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                    alert.setMessage("Thanh toán thành công, người bán đang xử lí giao dịch cho bạn!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                    if(snapshot.getValue(SanPham.class).getsID().equals(sanPhamID)){
                                                        int newSL = snapshot.getValue(SanPham.class).getiSoLuong() - soLuongSP;
                                                        SanPham sanPham = new SanPham(snapshot.getValue(SanPham.class).getsID(), snapshot.getValue(SanPham.class).getsUserID(),
                                                                snapshot.getValue(SanPham.class).getsShopID(), snapshot.getValue(SanPham.class).getsTenSP(),snapshot.getValue(SanPham.class).getsSPImage(),
                                                                snapshot.getValue(SanPham.class).getsMoTa(),snapshot.getValue(SanPham.class).getsDanhMuc(), snapshot.getValue(SanPham.class).getsNgayDang(),
                                                                snapshot.getValue(SanPham.class).getsDiaChiDang(), snapshot.getValue(SanPham.class).getlGiaTien(),newSL,snapshot.getValue(SanPham.class).getiTinhTrang());
                                                        databaseReference.child("SanPham").child(snapshot.getKey()).setValue(sanPham);
                                                        SanPham sanPhamOrder = new SanPham(snapshot.getValue(SanPham.class).getsID(), snapshot.getValue(SanPham.class).getsUserID(),
                                                                snapshot.getValue(SanPham.class).getsShopID(), snapshot.getValue(SanPham.class).getsTenSP(),snapshot.getValue(SanPham.class).getsSPImage(),
                                                                snapshot.getValue(SanPham.class).getsMoTa(),snapshot.getValue(SanPham.class).getsDanhMuc(), snapshot.getValue(SanPham.class).getsNgayDang(),
                                                                snapshot.getValue(SanPham.class).getsDiaChiDang(), snapshot.getValue(SanPham.class).getlGiaTien(),soLuongSP,snapshot.getValue(SanPham.class).getiTinhTrang());
                                                        String donHangID = databaseReference.push().getKey();
                                                        long tongGiaTriDonHang = snapshot.getValue(SanPham.class).getlGiaTien() * soLuongSP;
                                                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                                        Date date = new Date();
                                                        OrderData orderData = new OrderData(donHangID, userID, nguoiBanID, dateFormat.format(date), edtLienHe.getText().toString(), edtDiaChi.getText().toString(), sanPhamOrder, 3, 0, 0, tongGiaTriDonHang, "");
                                                        databaseReference.child("DonHang").child(donHangID).setValue(orderData);
                                                        finish();
                                                        intent = new Intent(v.getContext(), UserMainActivity.class);
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
                                    }).show();
                                }
                                else if (paymentCheck == false){
                                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                    alert.setMessage("Số dư trong ví không đủ thực hiện giao dịch, vui lòng nạp thêm!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    }).show();
                                }
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn muốn xác nhận thanh toán và đặt hàng?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
            }

        }
    };

    public void thanhToanEWallet(Long tongGiaTri, String userID, String nguoiBanID){
        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserData.class).getsUserID().equals(userID) && snapshot.getValue(UserData.class).getlMoney() >= tongGiaTri){
                    long newMoney = snapshot.getValue(UserData.class).getlMoney() - tongGiaTri;
                    UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),
                            snapshot.getValue(UserData.class).getsFullName(),snapshot.getValue(UserData.class).getsSdt(),snapshot.getValue(UserData.class).getsGioiTinh(),
                            snapshot.getValue(UserData.class).getsDiaChi(), snapshot.getValue(UserData.class).getsPassword(),snapshot.getValue(UserData.class).getsImage(),snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(),snapshot.getValue(UserData.class).getiPermission(),
                            snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                            snapshot.getValue(UserData.class).getiAccPoint(),snapshot.getValue(UserData.class).getiReport(), newMoney);
                    databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);

                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiBanID)){
                                long newMoney = snapshot.getValue(UserData.class).getlMoney() + tongGiaTri;
                                UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),
                                        snapshot.getValue(UserData.class).getsFullName(),snapshot.getValue(UserData.class).getsSdt(),snapshot.getValue(UserData.class).getsGioiTinh(),
                                        snapshot.getValue(UserData.class).getsDiaChi(), snapshot.getValue(UserData.class).getsPassword(),snapshot.getValue(UserData.class).getsImage(),snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(),snapshot.getValue(UserData.class).getiPermission(),
                                        snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                        snapshot.getValue(UserData.class).getiAccPoint(),snapshot.getValue(UserData.class).getiReport(), newMoney);
                                databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
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

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            intent = new Intent(v.getContext(), BuyActivity.class);
            intent.putExtra("UserID", userID);
            intent.putExtra("UserName", userName);
            intent.putExtra("SellerID", nguoiBanID);
            intent.putExtra("SanPhamID", sanPhamID);
            intent.putExtra("SoLuongSP", String.valueOf(soLuongSP));
            intent.putExtra("NavigateTo", navigateTo);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };
}