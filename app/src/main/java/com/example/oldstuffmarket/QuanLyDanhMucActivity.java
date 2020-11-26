package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.adapter.DanhMucAdapter;
import com.example.oldstuffmarket.data_models.DanhMucData;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class QuanLyDanhMucActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Intent intent;
    private int PICK_IMAGE = 123;
    private int CAMERA_IMAGE = 123;
    private ImageView imgDanhMuc;
    private Button btnChooseFromGallery, btnOpenCamera, btnBack, btnAddDanhMuc, btnXoaDanhMuc, btnSuaDanhMuc, btnClear;
    private String sUserName, sDanhMucID = "", sDanhMucIMG = "", sTenDanhMuc = "";
    private EditText edtTenDanhMuc;
    private GridView danhMucGrid;
    private ArrayList<DanhMucData> danhMucDataArrayList;
    private DanhMucAdapter danhMucAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.quan_ly_danh_muc_layout);

        btnAddDanhMuc = (Button) findViewById(R.id.btnAddDanhMuc);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnSuaDanhMuc = (Button) findViewById(R.id.btnSuaDanhMuc);
        btnXoaDanhMuc = (Button) findViewById(R.id.btnXoaDanhMuc);
        btnChooseFromGallery = (Button) findViewById(R.id.btnChooseFromGallery);
        btnOpenCamera = (Button) findViewById(R.id.btnOpenCamera);
        btnClear = (Button) findViewById(R.id.btnClear);
        imgDanhMuc = (ImageView) findViewById(R.id.imgDanhMuc);
        edtTenDanhMuc = (EditText) findViewById(R.id.edtTenDanhMuc);
        danhMucGrid = (GridView) findViewById(R.id.danhMucGrid);

        danhMucDataArrayList = new ArrayList<>();

        btnChooseFromGallery.setOnClickListener(chooseImageClick);
        btnOpenCamera.setOnClickListener(openCameraClick);
        btnBack.setOnClickListener(backClick);
        btnAddDanhMuc.setOnClickListener(addDanhMucClick);
        btnClear.setOnClickListener(clearClick);
        btnSuaDanhMuc.setOnClickListener(suaClick);
        btnXoaDanhMuc.setOnClickListener(xoaClick);

    }

    @Override
    protected void onResume() {
        super.onResume();
        danhMucDataArrayList.clear();

        databaseReference.child("DanhMuc").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                danhMucDataArrayList.add(snapshot.getValue(DanhMucData.class));
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

        }

        final Handler handler = new Handler();
        final int delay = 500; //milliseconds
        handler.postDelayed(new Runnable(){
            public void run(){
                danhMucLoad();
//                handler.postDelayed(this, delay);
            }
        }, delay);

        danhMucGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DanhMucData danhMucData = danhMucDataArrayList.get(position);
                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                storageReference.child(danhMucData.getsDanhMucIMG()+ ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(view.getContext()).load(uri).into(imgDanhMuc);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                    }
                });
                sDanhMucID = danhMucData.getsDanhMucID();
                sDanhMucIMG = danhMucData.getsDanhMucIMG();
                sTenDanhMuc = danhMucData.getsTenDanhMuc();
                edtTenDanhMuc.setText(danhMucData.getsTenDanhMuc());
            }
        });

    }

    View.OnClickListener xoaClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(sTenDanhMuc.equals("")){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa chọn danh mục cần xóa!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else {
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                storageReference.child(sDanhMucIMG + ".png").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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

                                sDanhMucIMG = databaseReference.push().getKey();

                                databaseReference.child("DanhMuc").child(sDanhMucID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(QuanLyDanhMucActivity.this, "Xóa danh mục thành công!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                edtTenDanhMuc.setText("");
                                imgDanhMuc.setImageResource(R.mipmap.no_image_icon);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Bạn chắc chắn muốn xóa danh mục " + sTenDanhMuc + "?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }
        }
    };

    View.OnClickListener suaClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtTenDanhMuc.getText().toString().isEmpty()){
                edtTenDanhMuc.setError("Bạn chưa nhập tên danh mục!");
            }
            else if(danhMucCheck(danhMucDataArrayList, sDanhMucID, edtTenDanhMuc.getText().toString())){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Tên danh mục đã tồn tại!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }else if(sTenDanhMuc.equals("")){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa chọn danh mục cần sửa!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else {
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                storageReference.child(sDanhMucIMG + ".png").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                sDanhMucIMG = databaseReference.push().getKey();

                                final StorageReference mountainsRef = storageReference.child(sDanhMucIMG + ".png");

                                imgDanhMuc.setDrawingCacheEnabled(true);
                                imgDanhMuc.buildDrawingCache();
                                Bitmap bitmap = ((BitmapDrawable) imgDanhMuc.getDrawable()).getBitmap();
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

                                DanhMucData danhMucData = new DanhMucData(sDanhMucID, edtTenDanhMuc.getText().toString(), sDanhMucIMG);
                                databaseReference.child("DanhMuc").child(sDanhMucID).setValue(danhMucData);

                                edtTenDanhMuc.setText("");
                                imgDanhMuc.setImageResource(R.mipmap.no_image_icon);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Bạn chắc chắn muốn sửa danh mục " + sTenDanhMuc + " thành " + edtTenDanhMuc.getText().toString() + "?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }
        }
    };

    View.OnClickListener clearClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            danhMucDataArrayList.clear();

            databaseReference.child("DanhMuc").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    danhMucDataArrayList.add(snapshot.getValue(DanhMucData.class));
//                danhMucLoad();
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

            final Handler handler = new Handler();
            final int delay = 500; //milliseconds
            handler.postDelayed(new Runnable(){
                public void run(){
                    danhMucLoad();
//                handler.postDelayed(this, delay);
                }
            }, delay);
            edtTenDanhMuc.setText("");
            imgDanhMuc.setImageResource(R.mipmap.no_image_icon);
        }
    };

    public void danhMucLoad(){

        danhMucAdapter = new DanhMucAdapter(QuanLyDanhMucActivity.this, R.layout.danh_muc_adapter_layout, danhMucDataArrayList);
        danhMucGrid.setAdapter(danhMucAdapter);
    }

    public Boolean danhMucCheck(ArrayList<DanhMucData> danhMucDataArrayList, String sDanhMucID, String sDanhMucName){
        for(DanhMucData danhMucData : danhMucDataArrayList){
            if(!danhMucData.getsDanhMucID().equals(sDanhMucID) && danhMucData.getsTenDanhMuc().equals(sDanhMucName)){
                return true;
            }
        }
        return false;
    }

    public Boolean addDanhMucCheck(ArrayList<DanhMucData> danhMucDataArrayList, String sDanhMucName){
        for(DanhMucData danhMucData : danhMucDataArrayList){
            if(danhMucData.getsTenDanhMuc().equals(sDanhMucName)){
                return true;
            }
        }
        return false;
    }

    View.OnClickListener addDanhMucClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtTenDanhMuc.getText().toString().isEmpty()){
                edtTenDanhMuc.setError("Bạn chưa nhập tên danh mục!");
            }
            else if(addDanhMucCheck(danhMucDataArrayList, edtTenDanhMuc.getText().toString())){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Tên danh mục đã tồn tại!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else {
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                String sDanhMucID = databaseReference.push().getKey();
                                String sDanhMucIMG = databaseReference.push().getKey();

                                final StorageReference mountainsRef = storageReference.child(sDanhMucIMG + ".png");

                                imgDanhMuc.setDrawingCacheEnabled(true);
                                imgDanhMuc.buildDrawingCache();
                                Bitmap bitmap = ((BitmapDrawable) imgDanhMuc.getDrawable()).getBitmap();
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

                                DanhMucData danhMucData = new DanhMucData(sDanhMucID, edtTenDanhMuc.getText().toString(), sDanhMucIMG);
                                databaseReference.child("DanhMuc").child(sDanhMucID).setValue(danhMucData);



                                edtTenDanhMuc.setText("");
                                imgDanhMuc.setImageResource(R.mipmap.no_image_icon);

                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Bạn muốn thêm danh mục " + edtTenDanhMuc.getText().toString() + "?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }

        }
    };

    View.OnClickListener openCameraClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CAMERA_IMAGE = 2;
            Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(camera, CAMERA_IMAGE);
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
            Intent intent = new Intent(v.getContext(), AdminMainActivity.class);
            intent.putExtra("UserName", sUserName);
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
                imgDanhMuc.setImageURI(imageUri);
            }
            PICK_IMAGE = 123;
        }
        if(CAMERA_IMAGE != 123){
            if(requestCode == CAMERA_IMAGE && resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgDanhMuc.setImageBitmap(bitmap);
            }
            CAMERA_IMAGE = 123;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}