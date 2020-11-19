package com.example.oldstuffmarket;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.data_models.Commission;
import com.example.oldstuffmarket.data_models.DanhMucData;
import com.example.oldstuffmarket.data_models.SanPham;
import com.example.oldstuffmarket.data_models.ShopData;
import com.example.oldstuffmarket.data_models.UserData;
import com.example.oldstuffmarket.ui.dashboard.SettingsFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class UserMainActivity extends AppCompatActivity{

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    static public String sUserName;
    static public String sUserID;
    static public String sShopID;
    static public ArrayList<UserData> userDataArrayList;
    static public ArrayList<DanhMucData> danhMucDataArrayList;
    static public ArrayList<SanPham> sanPhamArrayList;
    private ArrayList<ShopData> shopDataArrayList;
    private Intent intent = LoginActivity.intent;
    private BottomNavigationView navView;
    private int userCommission = 0, shopCommission = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.user_main_layout);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
//                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        shopDataArrayList = new ArrayList<>();

        danhMucDataArrayList = new ArrayList<>();
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

        sanPhamArrayList = new ArrayList<>();
        databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                sanPhamArrayList.add(snapshot.getValue(SanPham.class));
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

    @Override
    protected void onResume() {
        super.onResume();

        databaseReference.child("Commission").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(Commission.class).getId().equals("-MKyZZdaQ3ucidlxPkUV")){
                    userCommission = snapshot.getValue(Commission.class).getUserCommission();
                    shopCommission = snapshot.getValue(Commission.class).getShopCommission();
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

        reloadShopArrayList();

        if(getIntent().getExtras() != null){
//            Toast.makeText(this, getIntent().getExtras().getString("UserName"), Toast.LENGTH_SHORT).show();
            sUserName = getIntent().getExtras().getString("UserName");

            userDataArrayList = new ArrayList<>();

            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName)){
                        sShopID = snapshot.getValue(UserData.class).getsShopID();
                        sUserID = snapshot.getValue(UserData.class).getsUserID();

                        if(!sShopID.isEmpty()){
                            Handler handler = new Handler();
                            int delay = 1000;
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(snapshot.getValue(UserData.class).getiAccPoint() < 20){
                                        databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(ShopData.class).getUserID().equals(sUserID)){
                                                    ShopData shopData = new ShopData(snapshot.getValue(ShopData.class).getShopID(), snapshot.getValue(ShopData.class).getUserID(),
                                                            snapshot.getValue(ShopData.class).getShopName(), snapshot.getValue(ShopData.class).getMoTaShop(), snapshot.getValue(ShopData.class).getShopImage(),
                                                            snapshot.getValue(ShopData.class).getNgayTaoShop(), -1);
                                                    databaseReference.child("Shop").child(snapshot.getValue(ShopData.class).getShopID()).setValue(shopData);
                                                    updateSanPham();

                                                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            if(snapshot.getValue(UserData.class).getsUserID().equals(sUserID)){
                                                                UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),
                                                                        snapshot.getValue(UserData.class).getsSdt(),snapshot.getValue(UserData.class).getsGioiTinh(),snapshot.getValue(UserData.class).getsDiaChi(),
                                                                        snapshot.getValue(UserData.class).getsPassword(),snapshot.getValue(UserData.class).getsImage(),snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(),snapshot.getValue(UserData.class).getiPermission(),
                                                                        userCommission, snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                                        snapshot.getValue(UserData.class).getiAccPoint(),snapshot.getValue(UserData.class).getiReport(),snapshot.getValue(UserData.class).getlMoney());
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
                                    else if(snapshot.getValue(UserData.class).getiAccPoint() >= 20 && shopActiveCheck(shopDataArrayList, sShopID) == -1){
                                        databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(ShopData.class).getUserID().equals(sUserID)){
                                                    ShopData shopData = new ShopData(snapshot.getValue(ShopData.class).getShopID(), snapshot.getValue(ShopData.class).getUserID(),
                                                            snapshot.getValue(ShopData.class).getShopName(), snapshot.getValue(ShopData.class).getMoTaShop(), snapshot.getValue(ShopData.class).getShopImage(),
                                                            snapshot.getValue(ShopData.class).getNgayTaoShop(), 1);
                                                    databaseReference.child("Shop").child(snapshot.getValue(ShopData.class).getShopID()).setValue(shopData);
                                                    updateSanPham();

                                                    databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                                        @Override
                                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                            if(snapshot.getValue(UserData.class).getsUserID().equals(sUserID)){
                                                                UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),
                                                                        snapshot.getValue(UserData.class).getsSdt(),snapshot.getValue(UserData.class).getsGioiTinh(),snapshot.getValue(UserData.class).getsDiaChi(),
                                                                        snapshot.getValue(UserData.class).getsPassword(),snapshot.getValue(UserData.class).getsImage(),snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(),snapshot.getValue(UserData.class).getiPermission(),
                                                                        shopCommission, snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                                        snapshot.getValue(UserData.class).getiAccPoint(),snapshot.getValue(UserData.class).getiReport(),snapshot.getValue(UserData.class).getlMoney());
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
                                }
                            }, delay);
                        }
                    }
                    UserData userData = snapshot.getValue(UserData.class);
                    userDataArrayList.add(userData);
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
        else{
            Toast.makeText(this, "Không nhận được data", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateSanPham(){
        databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(ShopData.class).getShopID().equals(sShopID)){
                    if(snapshot.getValue(ShopData.class).getTinhTrangShop() == 1){
                        databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getValue(SanPham.class).getsUserID().equals(sUserID)){
                                    SanPham sanPham = new SanPham(snapshot.getValue(SanPham.class).getsID(), snapshot.getValue(SanPham.class).getsUserID(),
                                            sShopID, snapshot.getValue(SanPham.class).getsTenSP(), snapshot.getValue(SanPham.class).getsSPImage(), snapshot.getValue(SanPham.class).getsMoTa(),
                                            snapshot.getValue(SanPham.class).getsDanhMuc(), snapshot.getValue(SanPham.class).getsNgayDang(), snapshot.getValue(SanPham.class).getsDiaChiDang(), snapshot.getValue(SanPham.class).getlGiaTien(), snapshot.getValue(SanPham.class).getiSoLuong(), snapshot.getValue(SanPham.class).getiTinhTrang());
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
                    }
                    else if(snapshot.getValue(ShopData.class).getTinhTrangShop() == 3){
                        databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if(snapshot.getValue(SanPham.class).getsUserID().equals(sUserID)){
                                    SanPham sanPham = new SanPham(snapshot.getValue(SanPham.class).getsID(), snapshot.getValue(SanPham.class).getsUserID(),
                                            "", snapshot.getValue(SanPham.class).getsTenSP(), snapshot.getValue(SanPham.class).getsSPImage(), snapshot.getValue(SanPham.class).getsMoTa(),
                                            snapshot.getValue(SanPham.class).getsDanhMuc(), snapshot.getValue(SanPham.class).getsNgayDang(), snapshot.getValue(SanPham.class).getsDiaChiDang(), snapshot.getValue(SanPham.class).getlGiaTien(), snapshot.getValue(SanPham.class).getiSoLuong(), snapshot.getValue(SanPham.class).getiTinhTrang());
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

    public void reloadShopArrayList(){
        shopDataArrayList.clear();
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
    }

    public int shopActiveCheck(ArrayList<ShopData> shopDataArrayList, String shopID){
        for(ShopData shopData : shopDataArrayList){
            if(shopData.getShopID().equals(shopID)){
                return shopData.getTinhTrangShop();
            }
        }
        return -1;
    }
}