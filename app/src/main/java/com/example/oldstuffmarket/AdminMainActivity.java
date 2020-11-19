package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.data_models.ShopData;
import com.example.oldstuffmarket.data_models.UserData;
import com.example.oldstuffmarket.data_models.UserDepositData;
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

public class AdminMainActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Button btnLogout, btnQuanLyDanhMuc, btnAccountInfo, btnWallet, btnQuanLyBaiDang, btnUserDepositMoney, btnViGiaoDich, btnPassWordChange, btnShopRegistration, btnCommission;
    private TextView txtAdminAccountName, txtNotify, txtUserDepositMoneyNotify;
    private ImageView imgAccount;
    private Intent intent = LoginActivity.intent;
    private ArrayList<UserData> userDataArrayList;
    private ArrayList<ShopData> shopDataArrayList;
    private ArrayList<UserDepositData> userDepositDataArrayList;
    private String sUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.admin_main_layout);

        txtAdminAccountName = (TextView) findViewById(R.id.txtAdminAccountName);
        txtNotify = (TextView) findViewById(R.id.txtNotify);
        txtUserDepositMoneyNotify = (TextView) findViewById(R.id.txtUserDepositMoneyNotify);
        imgAccount = (ImageView) findViewById(R.id.imgAccount);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnPassWordChange = (Button) findViewById(R.id.btnPassWordChange);
        btnAccountInfo = (Button) findViewById(R.id.btnAccountInfo);
        btnWallet = (Button) findViewById(R.id.btnWallet);
        btnQuanLyDanhMuc = (Button) findViewById(R.id.btnQuanLyDanhMuc);
        btnShopRegistration = (Button) findViewById(R.id.btnShopRegistration);
        btnUserDepositMoney = (Button) findViewById(R.id.btnUserDepositMoney);
        btnCommission = (Button) findViewById(R.id.btnCommission);

        userDataArrayList = new ArrayList<>();
        shopDataArrayList = new ArrayList<>();
        userDepositDataArrayList = new ArrayList<>();

        btnLogout.setOnClickListener(logoutClick);
        btnAccountInfo.setOnClickListener(accountInfoClick);
        btnPassWordChange.setOnClickListener(passwordChangeClick);
        btnWallet.setOnClickListener(walletClick);
        btnQuanLyDanhMuc.setOnClickListener(danhMucClick);
        btnShopRegistration.setOnClickListener(shopRegistrationClick);
        btnUserDepositMoney.setOnClickListener(depositMoneyClick);
        btnCommission.setOnClickListener(commissionClick);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userDepositDataArrayList.clear();
        shopDataArrayList.clear();
        databaseReference.child("ShopRegistration").addChildEventListener(new ChildEventListener() {
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
        databaseReference.child("UserDepositRequest").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(UserDepositData.class).getTinhTrang() == 0){
                    userDepositDataArrayList.add(snapshot.getValue(UserDepositData.class));
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

            sUserName = getIntent().getExtras().getString("UserName");

            txtAdminAccountName.setText(String.valueOf(userDataArrayList.size()));

            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName)){
                        txtAdminAccountName.setText(snapshot.getValue(UserData.class).getsUserName() + " - " + snapshot.getValue(UserData.class).getsFullName());
                        btnWallet.setText("Ví tài khoản: " + String.valueOf(snapshot.getValue(UserData.class).getlMoney()) + "vnđ");
                        if(!snapshot.getValue(UserData.class).getsImage().isEmpty()){
                            final Handler handler = new Handler();
                            final int delay = 900; //milliseconds
                            handler.postDelayed(new Runnable(){
                                public void run(){
                                    imageLoad(snapshot.getValue(UserData.class).getsImage());
//                                    handler.postDelayed(this, delay);
                                }
                            }, delay);
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

        Handler handler = new Handler();
        int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                txtNotify.setText(String.valueOf(shopDataArrayList.size()));
                txtUserDepositMoneyNotify.setText(String.valueOf(userDepositDataArrayList.size()));
            }
        }, delay);
    }

    View.OnClickListener commissionClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), CommissionActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener depositMoneyClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), YeuCauChuyenKhoanActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener shopRegistrationClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ShopRegistrationRequestActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener danhMucClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), QuanLyDanhMucActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("UserName", sUserName);
            startActivity(intent);

        }
    };

    public void imageLoad(String sImageName){
        storageReference.child(sImageName + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(AdminMainActivity.this).load(uri).into(imgAccount);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminMainActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    View.OnClickListener walletClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), WalletActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener passwordChangeClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), PasswordChangeActivity.class);
            intent.putExtra("UserName", sUserName);
            startActivity(intent);
        }
    };

    View.OnClickListener accountInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), AccountInfoActivity.class);
            intent.putExtra("UserName", sUserName);
            startActivity(intent);
        }
    };

    View.OnClickListener logoutClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            finish();
                            intent = new Intent(v.getContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;

                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Bạn muốn đăng xuất?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
        }
    };
}