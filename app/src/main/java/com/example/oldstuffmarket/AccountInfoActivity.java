package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AccountInfoActivity extends AppCompatActivity {

    private String sUserName, sFullname, sGenDer, sDiaChi, sSDT;
    private ArrayList<UserData> userDataArrayList;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgAccChange;
    private Button btnChooseFromGallery, btnOpenCamera, btnSave, btnBack;
    private Spinner spnGender;
    private EditText edtUserName, edtFullName, edtSDT, edtDiaChi, edtCommission;
    private Intent intent;
    private int PICK_IMAGE = 123;
    private int CAMERA_IMAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.account_info_layout);

        imgAccChange = (ImageView) findViewById(R.id.imgAccChange);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnChooseFromGallery = (Button) findViewById(R.id.btnChooseFromGallery);
        btnOpenCamera = (Button) findViewById(R.id.btnOpenCamera);
        btnSave = (Button) findViewById(R.id.btnSave);
        spnGender = (Spinner) findViewById(R.id.spnGender);
        edtDiaChi = (EditText) findViewById(R.id.edtDiaChi);
        edtFullName = (EditText) findViewById(R.id.edtFullName);
        edtSDT = (EditText) findViewById(R.id.edtSDT);
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtCommission = (EditText) findViewById(R.id.edtCommission);

        userDataArrayList = new ArrayList<>();

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

        if(getIntent().getExtras() != null){
            sUserName = getIntent().getExtras().getString("UserName");
            edtUserName.setText(sUserName);

            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName)){
                        edtFullName.setText(snapshot.getValue(UserData.class).getsFullName());
                        edtSDT.setText(snapshot.getValue(UserData.class).getsSdt());
                        edtDiaChi.setText(snapshot.getValue(UserData.class).getsDiaChi());
                        edtCommission.setText(String.valueOf(snapshot.getValue(UserData.class).getiCommission()) + "%");
                        if(snapshot.getValue(UserData.class).getsGioiTinh().equals("Nam")){
                            spnGender.setSelection(0);
                        }
                        else {
                            spnGender.setSelection(1);
                        }
                        if(!snapshot.getValue(UserData.class).getsImage().isEmpty()){
                            storageReference.child(snapshot.getValue(UserData.class).getsImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(AccountInfoActivity.this).load(uri).into(imgAccChange);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(AccountInfoActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
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


        btnBack.setOnClickListener(backClick);
        btnChooseFromGallery.setOnClickListener(chooseImageClick);
        btnSave.setOnClickListener(saveClick);
        btnOpenCamera.setOnClickListener(openCameraClick);



    }

    public boolean sdtCheck(ArrayList<UserData> userList, String sSDT, String sUserName){
        for(UserData user : userList){
            if(!user.getsUserName().equals(sUserName) && user.getsSdt().equals(sSDT)){
                return false;
            }
        }
        return true;
    }

    View.OnClickListener openCameraClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CAMERA_IMAGE = 2;
            Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, CAMERA_IMAGE);
        }
    };

    View.OnClickListener saveClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sUserName = edtUserName.getText().toString();
            sFullname = edtFullName.getText().toString();
            sGenDer = spnGender.getSelectedItem().toString();
            sDiaChi = edtDiaChi.getText().toString();
            sSDT = edtSDT.getText().toString();

            if(sFullname.isEmpty()){
                edtFullName.setError("Họ tên không được để trống!");
            }
            else if(sSDT.isEmpty()) {
                edtSDT.setError("Số điện thoại không được để trống!");
            }
            else if(sSDT.length() > 11 || sSDT.length() < 10){
                edtSDT.setError("Bạn nhập sai số điện thoại!");
            }
            else if(sdtCheck(userDataArrayList,edtSDT.getText().toString(), sUserName) == false){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Số điện thoại đã tồn tại trong hệ thống!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(sDiaChi.isEmpty()){
                edtDiaChi.setError("Địa chỉ không được để trống!");
            }
            else{

                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName)){

                                            storageReference.child(snapshot.getValue(UserData.class).getsImage() + ".png").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
//                                                    Toast.makeText(AccountInfoActivity.this, "Xoa hinh thanh cong", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
//                                                    Toast.makeText(AccountInfoActivity.this, "Xoa hinh that bai", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            String sKey = databaseReference.push().getKey();

                                            final StorageReference mountainsRef = storageReference.child(sKey + ".png");

                                            imgAccChange.setDrawingCacheEnabled(true);
                                            imgAccChange.buildDrawingCache();
                                            Bitmap bitmap = ((BitmapDrawable) imgAccChange.getDrawable()).getBitmap();
                                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                            byte[] data = baos.toByteArray();
                                            final UploadTask uploadTask = mountainsRef.putBytes(data);
                                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception exception) {
                                                    Toast.makeText(AccountInfoActivity.this, "Thêm hình thất bại!", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                                }
                                            });

                                            UserData userUpdate = new UserData(sUserName,snapshot.getValue(UserData.class).getsShopID(),sFullname,sSDT,sGenDer,sDiaChi, snapshot.getValue(UserData.class).getsPassword(),sKey,snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(),snapshot.getValue(UserData.class).getiPermission(),
                                                    snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                    snapshot.getValue(UserData.class).getiAccPoint(),snapshot.getValue(UserData.class).getiReport(),snapshot.getValue(UserData.class).getlMoney());
                                            databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);

                                            if(snapshot.getValue(UserData.class).getiPermission() == 1){
                                                intent = new Intent(AccountInfoActivity.this, UserMainActivity.class);
                                                intent.putExtra("UserName",sUserName);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                startActivity(intent);
                                                Toast.makeText(AccountInfoActivity.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                                            }
                                            else if(snapshot.getValue(UserData.class).getiPermission() == 0 || snapshot.getValue(UserData.class).getiPermission() == 2){
                                                intent = new Intent(AccountInfoActivity.this, AdminMainActivity.class);
                                                intent.putExtra("UserName",sUserName);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                startActivity(intent);
                                                Toast.makeText(AccountInfoActivity.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                                            }
                                            else if(snapshot.getValue(UserData.class).getiPermission() == 3){
                                                intent = new Intent(AccountInfoActivity.this, ShipperMainActivity.class);
                                                intent.putExtra("UserName",sUserName);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                startActivity(intent);
                                                Toast.makeText(AccountInfoActivity.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
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
                builder.setMessage("Bạn muốn lưu thông tin hiện tại?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();


            }
        }
    };

    View.OnClickListener chooseImageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PICK_IMAGE = 1;
            Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getiPermission() == 1){
                        Intent intent = new Intent(v.getContext(), UserMainActivity.class);
                        intent.putExtra("UserName", sUserName);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                    else if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getiPermission() == 0 || snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getiPermission() == 2){
                        Intent intent = new Intent(v.getContext(), AdminMainActivity.class);
                        intent.putExtra("UserName", sUserName);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);
                    }
                    else if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getiPermission() == 3 || snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getiPermission() == 4){
                        Intent intent = new Intent(v.getContext(), ShipperMainActivity.class);
                        intent.putExtra("UserName", sUserName);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        finish();
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
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(PICK_IMAGE != 123){
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri imageUri = data.getData();
                imgAccChange.setImageURI(imageUri);
            }
            PICK_IMAGE = 123;
        }
        if(CAMERA_IMAGE != 123){
            if(requestCode == CAMERA_IMAGE && resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgAccChange.setImageBitmap(bitmap);
            }
            CAMERA_IMAGE = 123;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}