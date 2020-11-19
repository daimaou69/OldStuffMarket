package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.oldstuffmarket.data_models.ShopData;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ShopRegistrationActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgShop;
    private Button btnChooseFromGallery, btnOpenCamera, btnRegistry, btnBack;
    private EditText edtTenShop, edtMoTaShop;
    private String userName, userID;
    private Intent intent;
    private int PICK_IMAGE = 123, galleryChoose = 0;
    private int CAMERA_IMAGE = 123, cameraChoose = 0;
    private ArrayList<ShopData> shopDataArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.shop_registration_layout);

        imgShop = (ImageView) findViewById(R.id.imgShop);
        btnChooseFromGallery = (Button) findViewById(R.id.btnChooseFromGallery);
        btnOpenCamera = (Button) findViewById(R.id.btnOpenCamera);
        btnRegistry = (Button) findViewById(R.id.btnRegistry);
        btnBack = (Button) findViewById(R.id.btnBack);
        edtTenShop = (EditText) findViewById(R.id.edtTenShop);
        edtMoTaShop = (EditText) findViewById(R.id.edtMoTaShop);

        shopDataArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);
        btnChooseFromGallery.setOnClickListener(chooseImageClick);
        btnOpenCamera.setOnClickListener(openCameraClick);
        btnRegistry.setOnClickListener(registryClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                shopDataArrayList.add(snapshot.getValue(ShopData.class));
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
            userName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");
        }
    }

    View.OnClickListener registryClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(cameraChoose == 0 && galleryChoose == 0){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa chọn hình cho shop!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtTenShop.getText().toString().isEmpty()){
                edtTenShop.setError("Bạn chưa nhập tên shop!");
            }
            else if(shopNameCheck(shopDataArrayList, edtTenShop.getText().toString()) == false){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Tên shop đã tồn tại, xin chọn tên khác!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtMoTaShop.getText().toString().isEmpty()){
                edtMoTaShop.setError("Bạn chưa nhập mô tả shop!");
            }
            else{
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                String shopImage = databaseReference.push().getKey();

                                final StorageReference mountainsRef = storageReference.child(shopImage + ".png");

                                imgShop.setDrawingCacheEnabled(true);
                                imgShop.buildDrawingCache();
                                Bitmap bitmap = ((BitmapDrawable) imgShop.getDrawable()).getBitmap();
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                byte[] data = baos.toByteArray();
                                final UploadTask uploadTask = mountainsRef.putBytes(data);
                                uploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Toast.makeText(v.getContext(), "Thêm hình thất bại!", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                                    }
                                });

                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                Date date = new Date();

                                String shopID = databaseReference.push().getKey();

                                ShopData shopData = new ShopData(shopID, userID, edtTenShop.getText().toString(), edtMoTaShop.getText().toString(), shopImage, dateFormat.format(date),0);

                                databaseReference.child("ShopRegistration").child(shopID).setValue(shopData);

                                databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(UserData.class).getsUserID().equals(userID)){
                                            UserData userData = new UserData(snapshot.getValue(UserData.class).getsUserName(),shopID,snapshot.getValue(UserData.class).getsFullName(),
                                                    snapshot.getValue(UserData.class).getsSdt(), snapshot.getValue(UserData.class).getsGioiTinh(), snapshot.getValue(UserData.class).getsDiaChi(),
                                                    snapshot.getValue(UserData.class).getsPassword(), snapshot.getValue(UserData.class).getsImage(), snapshot.getValue(UserData.class).getsUserID(),
                                                    snapshot.getValue(UserData.class).getsNgayThamGia(), snapshot.getValue(UserData.class).getiPermission(), snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(),
                                                    snapshot.getValue(UserData.class).getiSoSPDaBan(), snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(), snapshot.getValue(UserData.class).getlMoney());

                                            databaseReference.child("User").child(userID).setValue(userData);
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

                                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                alert.setMessage("Yêu cầu tạo shop đang được xét duyệt!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        intent = new Intent(v.getContext(), UserMainActivity.class);
                                        intent.putExtra("UserName", userName);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        finish();
                                        startActivity(intent);
                                    }
                                }).show();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Xác nhận đúng thông tin và đăng ký shop?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }
        }
    };

    public boolean shopNameCheck(ArrayList<ShopData> shopDataArrayList, String shopName){
        for(ShopData shopData : shopDataArrayList){
            if(shopData.getShopName().equals(shopName)){
                return false;
            }
        }
        return true;
    }

    View.OnClickListener chooseImageClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PICK_IMAGE = 1;
            galleryChoose++;
            Intent gallery = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            startActivityForResult(gallery, PICK_IMAGE);
        }
    };

    View.OnClickListener openCameraClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CAMERA_IMAGE = 2;
            cameraChoose++;
            Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, CAMERA_IMAGE);
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(PICK_IMAGE != 123){
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri imageUri = data.getData();
                imgShop.setImageURI(imageUri);
            }
            PICK_IMAGE = 123;
        }
        if(CAMERA_IMAGE != 123){
            if(requestCode == CAMERA_IMAGE && resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgShop.setImageBitmap(bitmap);
            }
            CAMERA_IMAGE = 123;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}