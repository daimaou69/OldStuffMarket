package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.oldstuffmarket.data_models.TaiKhoanNH;
import com.example.oldstuffmarket.data_models.UserData;
import com.example.oldstuffmarket.ui.wallet.ChuyenTienFragment;
import com.example.oldstuffmarket.ui.wallet.RutTienFragment;
import com.example.oldstuffmarket.ui.wallet.WalletFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WalletActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private Intent intent;
    private String sUserName, sSoTK;
    private Button btnNapTien, btnBack, btnRutTien, btnBankAccount, btnChuyenKhoan;
    private TextView txtSoDu;
    private double dMoney;
    public static ArrayList<UserData> userDataArrayList;
    public static ArrayList<TaiKhoanNH> taiKhoanNHArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.wallet_layout);

        txtSoDu = (TextView) findViewById(R.id.txtSoDu);
        btnNapTien = (Button) findViewById(R.id.btnNapTien);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnRutTien = (Button) findViewById(R.id.btnRutTien);
        btnBankAccount = (Button) findViewById(R.id.btnBankAccount);
        btnChuyenKhoan = (Button) findViewById(R.id.btnChuyenKhoan);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getiPermission() == 1){
                            Intent intent = new Intent(v.getContext(), UserMainActivity.class);
                            intent.putExtra("UserName", sUserName);
                            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            finish();
                            startActivity(intent);
                        }
                        else if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getiPermission() == 0){
                            Intent intent = new Intent(v.getContext(), AdminMainActivity.class);
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
        });

        btnNapTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                WalletFragment walletFragment = new WalletFragment();

                Bundle bundle = new Bundle();

                bundle.putString("UserName", sUserName);
                bundle.putString("SoTK", sSoTK);

                walletFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.napTienLayout, walletFragment);
                fragmentTransaction.commit();
            }
        });

        btnRutTien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dMoney < 100000){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                    alertDialog.setMessage("Số dư tối thiểu phải >= 100000vnd mới được phép rút tiền!");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
                else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    RutTienFragment rutTienFragment = new RutTienFragment();

                    Bundle bundle = new Bundle();

                    bundle.putString("UserName", sUserName);
                    bundle.putString("SoTK", sSoTK);

                    rutTienFragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.napTienLayout, rutTienFragment);
                    fragmentTransaction.commit();
                }
            }
        });

        btnChuyenKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dMoney < 50000){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                    alertDialog.setMessage("Số dư tối thiểu phải >= 50000vnd mới được phép chuyển tiền!");
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alertDialog.show();
                }
                else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    ChuyenTienFragment chuyenTienFragment = new ChuyenTienFragment();

                    Bundle bundle = new Bundle();

                    bundle.putString("UserName", sUserName);
                    bundle.putString("SoTK", sSoTK);

                    chuyenTienFragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.napTienLayout, chuyenTienFragment);
                    fragmentTransaction.commit();
                }
            }
        });

        btnBankAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(v.getContext(), TaiKhoanNganHangActivity.class);
                intent.putExtra("UserName", sUserName);
                intent.putExtra("TaiKhoanID", sSoTK);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        userDataArrayList = new ArrayList<>();
        taiKhoanNHArrayList = new ArrayList<>();

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

        if(getIntent().getExtras() != null){
            sUserName = getIntent().getExtras().getString("UserName");
            sSoTK = getIntent().getExtras().getString("SoTK");
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName)){
                        dMoney = snapshot.getValue(UserData.class).getlMoney();
                        txtSoDu.setText(String.valueOf(snapshot.getValue(UserData.class).getlMoney()) + "vnđ");
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