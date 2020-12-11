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
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.data_models.Comment;
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

public class CommentActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgSP;
    private TextView txtTenSP, txtGiaSP;
    private Spinner spnStarLevel;
    private Button btnPostComment, btnHome, btnReport;
    private EditText edtComment;
    private String productID, userID, nguoiBanID, userName;
    private int diemThanhVien;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.comment_layout);

        imgSP = (ImageView) findViewById(R.id.imgSP);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        spnStarLevel = (Spinner) findViewById(R.id.spnStarLevel);
        btnPostComment = (Button) findViewById(R.id.btnPostComment);
        btnHome = (Button) findViewById(R.id.btnHome);
        edtComment = (EditText) findViewById(R.id.edtComment);
        btnReport = (Button) findViewById(R.id.btnReport);

        btnHome.setOnClickListener(homeClick);
        btnPostComment.setOnClickListener(postCommentClick);
        btnReport.setOnClickListener(reportClick);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            productID = getIntent().getExtras().getString("ProductID");
            userID = getIntent().getExtras().getString("UserID");
            nguoiBanID = getIntent().getExtras().getString("SellerID");
            userName = getIntent().getExtras().getString("UserName");

            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(SanPham.class).getsID().equals(productID)){
                        storageReference.child(snapshot.getValue(SanPham.class).getsSPImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(CommentActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        if(snapshot.getValue(SanPham.class).getiTinhTrang() == 0){

                            txtTenSP.setText(snapshot.getValue(SanPham.class).getsTenSP() + " - New");
                        }
                        else {
                            txtTenSP.setText(snapshot.getValue(SanPham.class).getsTenSP() + " - 2nd");
                        }

                        txtGiaSP.setText(String.valueOf(snapshot.getValue(SanPham.class).getlGiaTien()) + "vnđ");
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

    View.OnClickListener reportClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
            intent = new Intent(CommentActivity.this, Report_SP_Activity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("ProductID", productID);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener postCommentClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtComment.getText().toString().isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa nhập đánh giá sản phẩm!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtComment.getText().toString().length() < 10){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Đánh giá sản phẩm quá ngắn!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else{

                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                alert.setMessage("Cám ơn bài đánh giá của bạn, điểm sẽ được cộng vào tài khoản của bạn trong giây lát!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int soSao = spnStarLevel.getSelectedItemPosition() + 1;
                                        diemThanhVien = 0;
                                        if(soSao == 3){
                                            diemThanhVien = 3;
                                        }
                                        else if(soSao == 4){
                                            diemThanhVien = 4;
                                        }
                                        else if(soSao == 5){
                                            diemThanhVien = 5;
                                        }
                                        else if(soSao == 2){
                                            diemThanhVien = -2;
                                        }
                                        else if(soSao == 1){
                                            diemThanhVien = -4;
                                        }
                                        String cmtID = databaseReference.push().getKey();
                                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        Date date = new Date();
                                        Comment comment = new Comment(cmtID, productID, userID, edtComment.getText().toString(), dateFormat.format(date), soSao);
                                        databaseReference.child("ProductReview").child(cmtID).setValue(comment);
                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(UserData.class).getsUserID().equals(userID)){
                                                    int newPoint = snapshot.getValue(UserData.class).getiAccPoint() + 1;
                                                    UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),snapshot.getValue(UserData.class).getsSdt(),
                                                            snapshot.getValue(UserData.class).getsGioiTinh(),snapshot.getValue(UserData.class).getsDiaChi(), snapshot.getValue(UserData.class).getsPassword(),snapshot.getValue(UserData.class).getsImage(),snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(),snapshot.getValue(UserData.class).getiPermission(),
                                                            snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                            newPoint,snapshot.getValue(UserData.class).getiReport(),snapshot.getValue(UserData.class).getlMoney());
                                                    databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);

                                                }
                                                else if(snapshot.getValue(UserData.class).getsUserID().equals(nguoiBanID)){
                                                    int newPoint = snapshot.getValue(UserData.class).getiAccPoint() + diemThanhVien;
                                                    UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),snapshot.getValue(UserData.class).getsSdt(),
                                                            snapshot.getValue(UserData.class).getsGioiTinh(),snapshot.getValue(UserData.class).getsDiaChi(), snapshot.getValue(UserData.class).getsPassword(),snapshot.getValue(UserData.class).getsImage(),snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(),snapshot.getValue(UserData.class).getiPermission(),
                                                            snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                            newPoint,snapshot.getValue(UserData.class).getiReport(),snapshot.getValue(UserData.class).getlMoney());
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

                                        finish();
                                        intent = new Intent(v.getContext(), UserMainActivity.class);
                                        intent.putExtra("UserName", userName);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(intent);
                                    }
                                }).show();

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Gửi đánh giá sản phẩm?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();

            }
        }
    };

    View.OnClickListener homeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            finish();
                            intent = new Intent(v.getContext(), UserMainActivity.class);
                            intent.putExtra("UserName", userName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                    }
                }
            };

            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
            alert.setMessage("Bạn muốn thoát đánh giá sản phẩm, bạn sẽ không được cộng điểm?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
        }
    };
}