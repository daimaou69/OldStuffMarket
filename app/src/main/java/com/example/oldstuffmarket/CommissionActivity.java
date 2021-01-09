package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.oldstuffmarket.data_models.Commission;
import com.example.oldstuffmarket.data_models.ShopData;
import com.example.oldstuffmarket.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CommissionActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    private int commissionShop, commissionUser, commissionShipper;
    private Intent intent;
    private Button btnBack, btnChangeShop, btnChangeUser, btnChangeShipper;
    private EditText edtCommissionShop, edtCommissionUser, edtCommissionShipper;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.commission_layout);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnChangeShop = (Button) findViewById(R.id.btnChangeShop);
        btnChangeUser = (Button) findViewById(R.id.btnChangeUser);
        edtCommissionShop = (EditText) findViewById(R.id.edtCommissionShop);
        edtCommissionUser = (EditText) findViewById(R.id.edtCommissionUser);
        btnChangeShipper = (Button) findViewById(R.id.btnChangeShipper);
        edtCommissionShipper = (EditText) findViewById(R.id.edtCommissionShipper);


        btnBack.setOnClickListener(backClick);
        btnChangeShop.setOnClickListener(changeShopClick);
        btnChangeUser.setOnClickListener(changeUserClick);
        btnChangeShipper.setOnClickListener(changeShipperClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");

        }

        databaseReference.child("Commission").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.getValue(Commission.class).getId().equals("-MKyZZdaQ3ucidlxPkUV"))
                {
                    edtCommissionShop.setText(String.valueOf(snapshot.getValue(Commission.class).getShopCommission()));
                    edtCommissionUser.setText(String.valueOf(snapshot.getValue(Commission.class).getUserCommission()));
                    edtCommissionShipper.setText(String.valueOf(snapshot.getValue(Commission.class).getShipperCommission()));
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

    View.OnClickListener changeShipperClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtCommissionShipper.getText().toString().isEmpty()){
                edtCommissionShipper.setError("Không được để trống!");
            }
            else if (Integer.valueOf(edtCommissionShipper.getText().toString()) <= 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Không được nhập nhỏ hơn 0 hoặc bằng 0").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
            }
            else {
                commissionShipper = Integer.valueOf(edtCommissionShipper.getText().toString());
                databaseReference.child("Commission").child("-MKyZZdaQ3ucidlxPkUV").child("shipperCommission").setValue(commissionShipper);
            }
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(UserData.class).getiPermission() == 3) {
                        databaseReference.child("User").child(snapshot.getKey()).child("iCommission").setValue(commissionShipper);
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

    View.OnClickListener changeShopClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtCommissionShop.getText().toString().isEmpty()){
                edtCommissionShop.setError("Không được để trống!");
            }
            else if (Integer.valueOf(edtCommissionShop.getText().toString()) <= 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Không được nhập nhỏ hơn 0 hoặc bằng 0").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
            else if (Integer.valueOf(edtCommissionUser.getText().toString()) <= Integer.valueOf(edtCommissionShop.getText().toString())) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Hoa Hồng Shop phải nhỏ hơn hoa hồng User").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
            else {
                commissionShop = Integer.valueOf(edtCommissionShop.getText().toString());
                commissionUser = Integer.valueOf(edtCommissionUser.getText().toString());
                databaseReference.child("Commission").child("-MKyZZdaQ3ucidlxPkUV").child("shopCommission").setValue(commissionShop);
                databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.getValue(UserData.class).getsShopID().isEmpty()) {
                            databaseReference.child("User").child(snapshot.getKey()).child("iCommission").setValue(commissionUser);
                        }
                        else {
                            String shopID = snapshot.getValue(UserData.class).getsShopID();
                            String userID = snapshot.getValue(UserData.class).getsUserID();
//                            databaseReference.child("User").child(snapshot.getKey()).child("iCommission").setValue(commissionShop);
                            databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if (snapshot.getValue(ShopData.class).getShopID().equals(shopID) && snapshot.getValue(ShopData.class).getTinhTrangShop() != -1) {
                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                databaseReference.child("User").child(userID).child("iCommission").setValue(commissionShop);
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
                                    else {
                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                databaseReference.child("User").child(userID).child("iCommission").setValue(commissionUser);
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
    };

    View.OnClickListener changeUserClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtCommissionUser.getText().toString().isEmpty()){
                edtCommissionUser.setError("Không được để trống!");
            }
            else if (Integer.valueOf(edtCommissionUser.getText().toString()) <= 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Không được nhập nhỏ hơn 0 hoặc bằng 0").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
            else if (Integer.valueOf(edtCommissionUser.getText().toString()) < Integer.valueOf(edtCommissionShop.getText().toString())) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Hoa Hồng User phải lớn hơn hoa hồng Shop").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
            else {
                commissionUser = Integer.valueOf(edtCommissionUser.getText().toString());
                databaseReference.child("Commission").child("-MKyZZdaQ3ucidlxPkUV").child("userCommission").setValue(commissionUser);
            }
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(UserData.class).getsShopID().isEmpty()) {
                        databaseReference.child("User").child(snapshot.getKey()).child("iCommission").setValue(commissionUser);
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

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), AdminMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}