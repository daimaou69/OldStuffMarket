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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.data_models.Commission;
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

public class ShopRequestDetailActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private ImageView imgShop;
    private TextView txtTenShop, txtMoTaShop;
    private Button btnAccept, btnDeny, btnBack;
    private Intent intent;
    private int commission;
    private String shopID, userID, userName, shopImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.shop_request_detail_layout);

        imgShop = (ImageView) findViewById(R.id.imgShop);
        txtMoTaShop = (TextView) findViewById(R.id.txtMoTaShop);
        txtTenShop = (TextView) findViewById(R.id.txtTenShop);
        btnAccept = (Button) findViewById(R.id.btnAccept);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnDeny = (Button) findViewById(R.id.btnDeny);

        btnBack.setOnClickListener(backClick);
        btnDeny.setOnClickListener(denyClick);
        btnAccept.setOnClickListener(acceptClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        databaseReference.child("Commission").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(Commission.class).getId().equals("-MKyZZdaQ3ucidlxPkUV")){
                    commission = snapshot.getValue(Commission.class).getShopCommission();
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

        if(getIntent().getExtras() != null){
            shopID = getIntent().getExtras().getString("ShopID");
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");
            databaseReference.child("ShopRegistration").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(ShopData.class).getShopID().equals(shopID)){
                        shopImage = snapshot.getValue(ShopData.class).getShopImage();
                        storageReference.child(snapshot.getValue(ShopData.class).getShopImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(ShopRequestDetailActivity.this).load(uri).into(imgShop);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ShopRequestDetailActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        txtTenShop.setText(snapshot.getValue(ShopData.class).getShopName());
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
        }
    }

    View.OnClickListener acceptClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            databaseReference.child("ShopRegistration").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(ShopData.class).getShopID().equals(shopID)){
                                        ShopData shopData = new ShopData(snapshot.getValue(ShopData.class).getShopID(),snapshot.getValue(ShopData.class).getUserID(),
                                                snapshot.getValue(ShopData.class).getShopName(), snapshot.getValue(ShopData.class).getMoTaShop(), snapshot.getValue(ShopData.class).getShopImage(),
                                                snapshot.getValue(ShopData.class).getNgayTaoShop(), 1);
                                        databaseReference.child("Shop").child(snapshot.getValue(ShopData.class).getShopID()).setValue(shopData);
                                        databaseReference.child("ShopRegistration").child(snapshot.getValue(ShopData.class).getShopID()).removeValue();
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
                                        if(snapshot.getValue(UserData.class).getsUserID().equals(userID)){
                                            UserData userData = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),
                                                    snapshot.getValue(UserData.class).getsSdt(), snapshot.getValue(UserData.class).getsGioiTinh(), snapshot.getValue(UserData.class).getsDiaChi(),
                                                    snapshot.getValue(UserData.class).getsPassword(), snapshot.getValue(UserData.class).getsImage(), snapshot.getValue(UserData.class).getsUserID(),
                                                    snapshot.getValue(UserData.class).getsNgayThamGia(), snapshot.getValue(UserData.class).getiPermission(), commission, snapshot.getValue(UserData.class).getiTinhTrang(),
                                                    snapshot.getValue(UserData.class).getiSoSPDaBan(), snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(), snapshot.getValue(UserData.class).getlMoney());

                                            databaseReference.child("User").child(userID).setValue(userData);

                                            intent = new Intent(v.getContext(), ShopRegistrationRequestActivity.class);
                                            intent.putExtra("UserName", userName);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            finish();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Chấp nhận yêu cầu mở shop của user?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
        }
    };

    View.OnClickListener denyClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            storageReference.child(shopImage + ".png").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
//                                                    Toast.makeText(AccountInfoActivity.this, "Xoa hinh thanh cong", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    Toast.makeText(v.getContext(), "Xoa hinh that bai", Toast.LENGTH_SHORT).show();
                                }
                            });
                            databaseReference.child("ShopRegistration").child(shopID).removeValue();
                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(UserData.class).getsUserID().equals(userID)){
                                        if(snapshot.getValue(UserData.class).getsUserID().equals(userID)){
                                            UserData userData = new UserData(snapshot.getValue(UserData.class).getsUserName(),"",snapshot.getValue(UserData.class).getsFullName(),
                                                    snapshot.getValue(UserData.class).getsSdt(), snapshot.getValue(UserData.class).getsGioiTinh(), snapshot.getValue(UserData.class).getsDiaChi(),
                                                    snapshot.getValue(UserData.class).getsPassword(), snapshot.getValue(UserData.class).getsImage(), snapshot.getValue(UserData.class).getsUserID(),
                                                    snapshot.getValue(UserData.class).getsNgayThamGia(), snapshot.getValue(UserData.class).getiPermission(), snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(),
                                                    snapshot.getValue(UserData.class).getiSoSPDaBan(), snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(), snapshot.getValue(UserData.class).getlMoney());

                                            databaseReference.child("User").child(userID).setValue(userData);

                                            intent = new Intent(v.getContext(), ShopRegistrationRequestActivity.class);
                                            intent.putExtra("UserName", userName);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            finish();
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
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Bạn muốn hủy yêu cầu mở shop của user?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ShopRegistrationRequestActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}