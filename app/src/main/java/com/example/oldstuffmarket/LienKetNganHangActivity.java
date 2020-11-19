package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.oldstuffmarket.data_models.TaiKhoanNH;
import com.example.oldstuffmarket.data_models.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class LienKetNganHangActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Intent intent;
    private EditText edtSoTK, edtHoTenChuTK, edtTenNganHang;
    private Button btnXacNhan, btnBack;
    private String sUserName;
    private ArrayList<TaiKhoanNH> taiKhoanNHArrayList;
    private ArrayList<UserData> userDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.lien_ket_ngan_hang_layout);

        edtHoTenChuTK = (EditText) findViewById(R.id.edtHoTenChuTK);
        edtSoTK = (EditText) findViewById(R.id.edtSoTK);
        edtTenNganHang = (EditText) findViewById(R.id.edtTenNganHang);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnXacNhan = (Button) findViewById(R.id.btnXacNhan);

        taiKhoanNHArrayList = new ArrayList<>();
        userDataArrayList = new ArrayList<>();

        databaseReference.child("TaiKhoanNH").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                taiKhoanNHArrayList.add(snapshot.getValue(TaiKhoanNH.class));
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
                userDataArrayList.add(snapshot.getValue(UserData.class));
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

        btnBack.setOnClickListener(backClick);
        btnXacNhan.setOnClickListener(xacNhanClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            sUserName = getIntent().getExtras().getString("UserName");

        }
    }

    View.OnClickListener xacNhanClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtSoTK.getText().toString().length() < 12 || edtSoTK.getText().toString().length() > 12){
                edtSoTK.setError("Số tài khoản phải có 12 số!");
            }
            else if(edtSoTK.getText().toString().isEmpty()){
                edtSoTK.setError("Số tài khoản không được bỏ trống!");
            }
            else if(kTraSoTK(taiKhoanNHArrayList, edtSoTK.getText().toString()) == false){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Số tài khoản đã tồn tại trong hệ thống!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtHoTenChuTK.getText().toString().isEmpty()){
                edtHoTenChuTK.setError("Họ tên không được bỏ trống!");
            }
            else if(edtTenNganHang.getText().toString().isEmpty()){
                edtTenNganHang.setError("Tên ngân hàng không được bỏ trống!");
            }
            else {
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                String sTaiKhoanID = databaseReference.push().getKey();
                                TaiKhoanNH taiKhoanNH = new TaiKhoanNH(sTaiKhoanID,edtHoTenChuTK.getText().toString(), edtSoTK.getText().toString(), edtTenNganHang.getText().toString(), 50000000);
                                databaseReference.child("TaiKhoanNH").child(sTaiKhoanID).setValue(taiKhoanNH);
                                databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName)){

                                            UserData userUpdate = new UserData(sUserName,snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),snapshot.getValue(UserData.class).getsSdt(),
                                                    snapshot.getValue(UserData.class).getsGioiTinh(),snapshot.getValue(UserData.class).getsDiaChi(), snapshot.getValue(UserData.class).getsPassword(),snapshot.getValue(UserData.class).getsImage(),snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(),snapshot.getValue(UserData.class).getiPermission(),
                                                    snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                    snapshot.getValue(UserData.class).getiAccPoint(),snapshot.getValue(UserData.class).getiReport(),snapshot.getValue(UserData.class).getlMoney());
                                            databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);

                                            if(snapshot.getValue(UserData.class).getiPermission() == 1){
                                                intent = new Intent(LienKetNganHangActivity.this, UserMainActivity.class);
                                                intent.putExtra("UserName",sUserName);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                finish();
                                                startActivity(intent);
                                                Toast.makeText(LienKetNganHangActivity.this, "Liên kết ngân hàng thành công!", Toast.LENGTH_SHORT).show();
                                            }
                                            else if(snapshot.getValue(UserData.class).getiPermission() == 0){
                                                intent = new Intent(LienKetNganHangActivity.this, AdminMainActivity.class);
                                                intent.putExtra("UserName",sUserName);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                finish();
                                                startActivity(intent);
                                                Toast.makeText(LienKetNganHangActivity.this, "Liên kết ngân hàng thành công!", Toast.LENGTH_SHORT).show();
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
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Xác nhận liên kết ngân hàng?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            for(UserData userData : userDataArrayList){
                if(userData.getsUserName().equals(sUserName)){
                    if(userData.getiPermission() == 0){
                        intent = new Intent(v.getContext(), AdminMainActivity.class);
                        intent.putExtra("UserName", sUserName);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        finish();
                        startActivity(intent);
                    }
                    else if(userData.getiPermission() == 1){
                        intent = new Intent(v.getContext(), UserMainActivity.class);
                        intent.putExtra("UserName", sUserName);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        finish();
                        startActivity(intent);
                    }
                }
            }
        }
    };

    public boolean kTraSoTK(ArrayList<TaiKhoanNH> taiKhoanNHArrayList, String sSoTK){
        for(TaiKhoanNH taiKhoanNH : taiKhoanNHArrayList){
            if(taiKhoanNH.getsSoTK().equals(sSoTK)){
                return false;
            }
        }
        return true;
    }
}