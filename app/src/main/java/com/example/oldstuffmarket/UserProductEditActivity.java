package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.data_models.Comment;
import com.example.oldstuffmarket.data_models.DanhMucData;
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
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserProductEditActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private String userID, userName, productID, navigateTo;
    private Intent intent;
    private Button btnBack, btnUpdate, btnDelete, btnChooseFromGallery, btnOpenCamera;
    private ArrayList<DanhMucData> danhMucDataArrayList = UserMainActivity.danhMucDataArrayList;
    private ImageView spIMG;
    private EditText edtTenSP, edtDiaChiDang, edtGiaSP, edtSoLuongSP, edtMoTaSP;
    private Spinner spnPhanLoai, spnDanhMuc;
    private ArrayList<String> danhMucList;
    private int PICK_IMAGE = 123;
    private int CAMERA_IMAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_product_edit_layout);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnChooseFromGallery = (Button) findViewById(R.id.btnChooseFromGallery);
        btnOpenCamera = (Button) findViewById(R.id.btnOpenCamera);
        spIMG = (ImageView) findViewById(R.id.spIMG);
        edtTenSP = (EditText) findViewById(R.id.edtTenSP);
        edtDiaChiDang = (EditText) findViewById(R.id.edtDiaChiDang);
        edtGiaSP = (EditText) findViewById(R.id.edtGiaSP);
        edtSoLuongSP = (EditText) findViewById(R.id.edtSoLuongSP);
        edtMoTaSP = (EditText) findViewById(R.id.edtMoTaSP);
        spnPhanLoai = (Spinner) findViewById(R.id.spnPhanLoai);
        spnDanhMuc = (Spinner) findViewById(R.id.spnDanhMuc);

        danhMucList = new ArrayList<>();

        for(DanhMucData danhMucData : danhMucDataArrayList){
            danhMucList.add(danhMucData.getsTenDanhMuc());
        }
        danhMucList.add("Khác");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(UserProductEditActivity.this, android.R.layout.simple_spinner_item, danhMucList);
        spnDanhMuc.setAdapter(arrayAdapter);

        if(getIntent().getExtras() != null){
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");
            productID = getIntent().getExtras().getString("ProductID");
            navigateTo = getIntent().getExtras().getString("NavigateTo");

            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(SanPham.class).getsID().equals(productID)){
                        SanPham sanPham = snapshot.getValue(SanPham.class);
                        storageReference.child(sanPham.getsSPImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(UserProductEditActivity.this).load(uri).into(spIMG);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        edtTenSP.setText(sanPham.getsTenSP());
                        edtDiaChiDang.setText(sanPham.getsDiaChiDang());
                        edtGiaSP.setText(String.valueOf(sanPham.getlGiaTien()));
                        edtSoLuongSP.setText(String.valueOf(sanPham.getiSoLuong()));
                        edtMoTaSP.setText(sanPham.getsMoTa());
                        if(sanPham.getiTinhTrang() == 0){
                            spnPhanLoai.setSelection(0);
                        }
                        else if(sanPham.getiTinhTrang() == 1){
                            spnPhanLoai.setSelection(1);
                        }

                        for(String s : danhMucList){
                            if(sanPham.getsDanhMuc().equals(s)){
                                spnDanhMuc.setSelection(danhMucList.indexOf(s));
                            }
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
        btnDelete.setOnClickListener(deleteClick);
        btnUpdate.setOnClickListener(updateClick);
        btnChooseFromGallery.setOnClickListener(chooseImageClick);
        btnOpenCamera.setOnClickListener(openCameraClick);

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

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

    View.OnClickListener updateClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtTenSP.getText().toString().isEmpty()){
                edtTenSP.setError("Bạn chưa nhập tên sản phẩm!");
            }
            else if(edtDiaChiDang.getText().toString().isEmpty()){
                edtDiaChiDang.setError("Bạn chưa nhập địa chỉ đăng sản phẩm!");
            }
            else if(edtGiaSP.getText().toString().isEmpty()){
                edtGiaSP.setError("Bạn chưa nhập giá sản phẩm!");
            }
            else if(edtSoLuongSP.getText().toString().isEmpty()){
                edtSoLuongSP.setError("Bạn chưa nhập số lượng sản phẩm!");
            }
            else if(Integer.parseInt(edtSoLuongSP.getText().toString()) < 0){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Số lượng sản phẩm không được < 0!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtMoTaSP.getText().toString().isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa mô tả sản phẩm!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else {
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                String sKey = databaseReference.push().getKey();

                                final StorageReference mountainsRef = storageReference.child(sKey + ".png");

                                spIMG.setDrawingCacheEnabled(true);
                                spIMG.buildDrawingCache();
                                Bitmap bitmap = ((BitmapDrawable) spIMG.getDrawable()).getBitmap();
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

                                databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(OrderData.class).getSanPham().getsID().equals(productID)){
                                            SanPham sanPham = new SanPham(snapshot.getValue(OrderData.class).getSanPham().getsID(), snapshot.getValue(OrderData.class).getSanPham().getsUserID(), snapshot.getValue(OrderData.class).getSanPham().getsShopID(), snapshot.getValue(OrderData.class).getSanPham().getsTenSP(), sKey,
                                                    snapshot.getValue(OrderData.class).getSanPham().getsMoTa(),snapshot.getValue(OrderData.class).getSanPham().getsDanhMuc(), snapshot.getValue(OrderData.class).getSanPham().getsNgayDang(),
                                                    snapshot.getValue(OrderData.class).getSanPham().getsDiaChiDang(), snapshot.getValue(OrderData.class).getSanPham().getlGiaTien(),
                                                    snapshot.getValue(OrderData.class).getSanPham().getiSoLuong(),snapshot.getValue(OrderData.class).getSanPham().getiTinhTrang());
                                            OrderData orderData = new OrderData(snapshot.getValue(OrderData.class).getDonHangID(), snapshot.getValue(OrderData.class).getNguoiMuaID(), snapshot.getValue(OrderData.class).getNguoiBanID(),
                                                    snapshot.getValue(OrderData.class).getNgayTaoDonHang(), snapshot.getValue(OrderData.class).getSoDienThoai(), snapshot.getValue(OrderData.class).getDiaChi(),
                                                    sanPham, snapshot.getValue(OrderData.class).getLoaiDonHang(), snapshot.getValue(OrderData.class).getTinhTrang(), snapshot.getValue(OrderData.class).getSellerCommission(), snapshot.getValue(OrderData.class).getGiaTien(), snapshot.getValue(OrderData.class).getShipperID());
                                            databaseReference.child("DonHang").child(snapshot.getValue(OrderData.class).getDonHangID()).setValue(orderData);
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

                                databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(SanPham.class).getsID().equals(productID)){

                                            storageReference.child(snapshot.getValue(SanPham.class).getsSPImage() + ".png").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
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

                                            SanPham sanPham = new SanPham(snapshot.getValue(SanPham.class).getsID(), snapshot.getValue(SanPham.class).getsUserID(), snapshot.getValue(SanPham.class).getsShopID(), edtTenSP.getText().toString(), sKey,edtMoTaSP.getText().toString(),spnDanhMuc.getSelectedItem().toString(), dateFormat.format(date), edtDiaChiDang.getText().toString(), Long.valueOf(edtGiaSP.getText().toString()),
                                                    Integer.valueOf(edtSoLuongSP.getText().toString()),spnPhanLoai.getSelectedItemPosition());
                                            databaseReference.child("SanPham").child(productID).setValue(sanPham);


                                            if(navigateTo.equals("UserPost")){
                                                finish();
                                                intent = new Intent(v.getContext(), UserUploadedPostActivity.class);
                                                intent.putExtra("UserName", userName);
                                                intent.putExtra("UserID", userID);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                startActivity(intent);
                                            }
                                            else if(navigateTo.equals("UserShop")) {
                                                finish();
                                                intent = new Intent(v.getContext(), UserShopActivity.class);
                                                intent.putExtra("UserName", userName);
                                                intent.putExtra("UserID", userID);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                startActivity(intent);
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
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Xác nhận đúng thông tin và cập nhật sản phẩm?").setPositiveButton("Yes", dialog).setNegativeButton("No", dialog).show();
            }
        }
    };

    View.OnClickListener deleteClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:

                            databaseReference.child("ProductReview").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(Comment.class).getSanPhamID().equals(productID)){

                                        databaseReference.child("ProductReview").child(productID).removeValue();

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

                            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(SanPham.class).getsID().equals(productID)){

                                        databaseReference.child("SanPham").child(productID).removeValue();

                                        finish();
                                        intent = new Intent(v.getContext(), UserUploadedPostActivity.class);
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

                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                    }
                }
            };
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
            alert.setMessage("Bạn muốn xóa sản phẩm?").setPositiveButton("Yes", dialog).setNegativeButton("No", dialog).show();
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(navigateTo.equals("UserPost")){
                finish();
                intent = new Intent(v.getContext(), UserUploadedPostActivity.class);
                intent.putExtra("UserName", userName);
                intent.putExtra("UserID", userID);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
            else if(navigateTo.equals("UserShop")){
                finish();
                intent = new Intent(v.getContext(), UserShopActivity.class);
                intent.putExtra("UserName", userName);
                intent.putExtra("UserID", userID);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(PICK_IMAGE != 123){
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
                Uri imageUri = data.getData();
                spIMG.setImageURI(imageUri);
            }
            PICK_IMAGE = 123;
        }
        if(CAMERA_IMAGE != 123){
            if(requestCode == CAMERA_IMAGE && resultCode == RESULT_OK){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                spIMG.setImageBitmap(bitmap);
            }
            CAMERA_IMAGE = 123;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}