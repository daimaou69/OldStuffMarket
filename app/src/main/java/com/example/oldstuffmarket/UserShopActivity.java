package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.adapter.SanPhamAdapter;
import com.example.oldstuffmarket.data_models.SanPham;
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

import java.util.ArrayList;

public class UserShopActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private TextView txtUserName, txtNgayThamGia, txtDiaChi, txtLienHe, txtMoTaShop;
    private ListView lstSanPham;
    private Button btnBack, btnAll, btnDoCu, btnDoMoi, btnPost, btnShopEdit;
    private ImageView imgUser;
    private String userName, userID, shopID;
    private Intent intent;
    private ArrayList<SanPham> sanPhamArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_shop_layout);

        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtNgayThamGia = (TextView) findViewById(R.id.txtNgayThamGia);
        txtDiaChi = (TextView) findViewById(R.id.txtDiaChi);
        txtLienHe = (TextView) findViewById(R.id.txtLienHe);
        txtMoTaShop = (TextView) findViewById(R.id.txtMoTaShop);
        lstSanPham = (ListView) findViewById(R.id.lstSanPham);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnAll = (Button) findViewById(R.id.btnAll);
        btnDoCu = (Button) findViewById(R.id.btnDoCu);
        btnDoMoi = (Button) findViewById(R.id.btnDoMoi);
        btnPost = (Button) findViewById(R.id.btnPost);
        btnShopEdit = (Button) findViewById(R.id.btnShopEdit);
        imgUser = (ImageView) findViewById(R.id.imgUser);

        sanPhamArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);

        btnAll.setOnClickListener(allClick);
        btnDoCu.setOnClickListener(doCuClick);
        btnDoMoi.setOnClickListener(doMoiClick);
        btnShopEdit.setOnClickListener(shopEditClick);

        lstSanPham.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                intent = new Intent(view.getContext(), ProductDetailActivity.class);
//                intent.putExtra("ProductID", sanPhamArrayList.get(position).getsID());
//                intent.putExtra("SoLuongSP", sanPhamArrayList.get(position).getiSoLuong());
//                intent.putExtra("NavigateTo", "Shop");
//                intent.putExtra("SanPhamID", sanPhamID);
//                startActivity(intent);
            }
        });

        btnPost.setOnClickListener(postClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");
            sanPhamArrayList.clear();
            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(SanPham.class).getsUserID().equals(userID)){
                        sanPhamArrayList.add(snapshot.getValue(SanPham.class));
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

            databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(ShopData.class).getUserID().equals(userID)){
                        shopID = snapshot.getValue(ShopData.class).getShopID();
                        storageReference.child(snapshot.getValue(ShopData.class).getShopImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(UserShopActivity.this).load(uri).into(imgUser);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        txtNgayThamGia.setText("Ngày tham gia: " + snapshot.getValue(ShopData.class).getNgayTaoShop());
                        txtUserName.setText(snapshot.getValue(ShopData.class).getShopName());
                        txtMoTaShop.setText(snapshot.getValue(ShopData.class).getMoTaShop());

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

                        txtDiaChi.setText("Địa chỉ: " + snapshot.getValue(UserData.class).getsDiaChi());
                        txtLienHe.setText("Liên hệ: " + snapshot.getValue(UserData.class).getsSdt());

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
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    sanPhamLoad();
                }
            }, delay);
        }
    }

    View.OnClickListener shopEditClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserShopEditActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.putExtra("ShopID", shopID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener postClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserNewPostActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("UserID", userID);
            intent.putExtra("ShopID", shopID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener doMoiClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sanPhamArrayList.clear();
            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(SanPham.class).getsUserID().equals(userID) && snapshot.getValue(SanPham.class).getiTinhTrang() == 0){
                        sanPhamArrayList.add(snapshot.getValue(SanPham.class));
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
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(UserShopActivity.this, R.layout.san_pham_adapter_layout, sanPhamArrayList);
                    lstSanPham.setAdapter(sanPhamAdapter);
                }
            }, delay);
        }
    };

    View.OnClickListener doCuClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sanPhamArrayList.clear();
            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(SanPham.class).getsUserID().equals(userID) && snapshot.getValue(SanPham.class).getiTinhTrang() == 1){
                        sanPhamArrayList.add(snapshot.getValue(SanPham.class));
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
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(UserShopActivity.this, R.layout.san_pham_adapter_layout, sanPhamArrayList);
                    lstSanPham.setAdapter(sanPhamAdapter);
                }
            }, delay);
        }
    };

    View.OnClickListener allClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sanPhamArrayList.clear();
            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(SanPham.class).getsUserID().equals(userID)){
                        sanPhamArrayList.add(snapshot.getValue(SanPham.class));
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
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(UserShopActivity.this, R.layout.san_pham_adapter_layout, sanPhamArrayList);
                    lstSanPham.setAdapter(sanPhamAdapter);
                }
            }, delay);
        }
    };

    public void sanPhamLoad(){
        SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(UserShopActivity.this, R.layout.san_pham_adapter_layout, sanPhamArrayList);
        lstSanPham.setAdapter(sanPhamAdapter);
    }

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
}