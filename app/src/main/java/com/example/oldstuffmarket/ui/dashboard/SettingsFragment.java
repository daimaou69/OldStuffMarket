package com.example.oldstuffmarket.ui.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.AccountInfoActivity;
import com.example.oldstuffmarket.DonBanActivity;
import com.example.oldstuffmarket.DonMuaActivity;
import com.example.oldstuffmarket.LichHenActivity;
import com.example.oldstuffmarket.LoginActivity;
import com.example.oldstuffmarket.PasswordChangeActivity;
import com.example.oldstuffmarket.R;
import com.example.oldstuffmarket.RemoveProductActivity;
import com.example.oldstuffmarket.ShopRegistrationActivity;
import com.example.oldstuffmarket.UserTaoLichHenActivity;
import com.example.oldstuffmarket.UserCommentNeedsActivity;
import com.example.oldstuffmarket.UserLichSuDonBanActivity;
import com.example.oldstuffmarket.UserMainActivity;
import com.example.oldstuffmarket.UserShopActivity;
import com.example.oldstuffmarket.UserTransactionHistoryActivity;
import com.example.oldstuffmarket.UserUploadedPostActivity;
import com.example.oldstuffmarket.UserWaitingForMoneyActivity;
import com.example.oldstuffmarket.WalletUserActivity;
import com.example.oldstuffmarket.data_models.Appointment;
import com.example.oldstuffmarket.data_models.OrderData;
import com.example.oldstuffmarket.data_models.RemoveProductData;
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

public class SettingsFragment extends Fragment {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private SettingsViewModel dashboardViewModel;
    private Button btnLogout, btnUploadPost, btnShop, btnWallet, btnNotification, btnLichHen, btnTaoLichHen, btnReceiveMoney, btnLichSuDonHang, btnLichSuDonBan, btnAccountInfo, btnDonMua, btnDonBan, btnPassWordChange, btnDanhGiaSP;
    private ImageView imgAccount;
    private Intent intent;
    private TextView txtAccountName, txtSpDaBan, txtDiemThanhVien, txtNotification, txtDonMuaNotify, txtDonBanNotify, txtDanhGiaSP, txtMoneyNotify, txtLichHen;
    private String sUserName = UserMainActivity.sUserName, userID = "";
    private ArrayList<ShopData> shopDataArrayList;
    private ArrayList<OrderData> donMuaArrayList;
    private ArrayList<OrderData> donBanArrayList;
    private ArrayList<OrderData> danhGiaSPList;
    private ArrayList<OrderData> waitingList;
    private ArrayList<Appointment> appointmentArrayList;
    private ArrayList<RemoveProductData> removeProductDataArrayList;
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private int diemThanhVien = 0;
    private String shopID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        dashboardViewModel =
//                new ViewModelProvider(this).get(SettingsViewModel.class);
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        btnLogout = (Button) view.findViewById(R.id.btnLogout);
        btnAccountInfo = (Button) view.findViewById(R.id.btnAccountInfo);
        btnShop = (Button) view.findViewById(R.id.btnShop);
        btnLichSuDonHang = (Button) view.findViewById(R.id.btnLichSuDonHang);
        btnLichSuDonBan = (Button) view.findViewById(R.id.btnLichSuDonBan);
        btnUploadPost = (Button) view.findViewById(R.id.btnUploadPost);
        btnWallet = (Button) view.findViewById(R.id.btnWallet);
        btnDonMua = (Button) view.findViewById(R.id.btnDonMua);
        btnDonBan = (Button) view.findViewById(R.id.btnDonBan);
        btnPassWordChange = (Button) view.findViewById(R.id.btnPassWordChange);
        btnDanhGiaSP = (Button) view.findViewById(R.id.btnDanhGiaSP);
        btnTaoLichHen = (Button) view.findViewById(R.id.btnTaoLichHen);
        btnLichHen = (Button) view.findViewById(R.id.btnLichHen);
        txtDanhGiaSP = (TextView) view.findViewById(R.id.txtDanhGiaSP);
        txtAccountName = (TextView) view.findViewById(R.id.txtAccountName);
        txtSpDaBan = (TextView) view.findViewById(R.id.txtSpDaBan);
        txtDiemThanhVien = (TextView) view.findViewById(R.id.txtDiemThanhVien);
        txtDonMuaNotify = (TextView) view.findViewById(R.id.txtDonMuaNotify);
        txtDonBanNotify = (TextView) view.findViewById(R.id.txtDonBanNotify);
        txtLichHen = (TextView) view.findViewById(R.id.txtLichHen);
        imgAccount = (ImageView) view.findViewById(R.id.imgAccount);
        btnReceiveMoney = (Button) view.findViewById(R.id.btnReceiveMoney);
        txtMoneyNotify = (TextView) view.findViewById(R.id.txtMoneyNotify);
        btnNotification = (Button) view.findViewById(R.id.btnNotification);
        txtNotification = (TextView) view.findViewById(R.id.txtNotification);

        shopDataArrayList = new ArrayList<>();
        donMuaArrayList = new ArrayList<>();
        donBanArrayList = new ArrayList<>();
        danhGiaSPList = new ArrayList<>();
        waitingList = new ArrayList<>();
        appointmentArrayList = new ArrayList<>();
        removeProductDataArrayList = new ArrayList<>();

        if (sUserName != "") {

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
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(UserData.class).getsUserName().equals(sUserName)) {
                        txtAccountName.setText(snapshot.getValue(UserData.class).getsFullName());
                        btnWallet.setText("Ví điện tử: " + String.valueOf(snapshot.getValue(UserData.class).getlMoney()) + "vnđ");
                        txtSpDaBan.setText("Số sản phẩm đã bán: " + String.valueOf(snapshot.getValue(UserData.class).getiSoSPDaBan()));
                        txtDiemThanhVien.setText("Điểm thành viên: " + String.valueOf(snapshot.getValue(UserData.class).getiAccPoint()));
                        shopID = snapshot.getValue(UserData.class).getsShopID();
                        userID = snapshot.getValue(UserData.class).getsUserID();
                        diemThanhVien = snapshot.getValue(UserData.class).getiAccPoint();
                        if (!snapshot.getValue(UserData.class).getsImage().isEmpty()) {
                            storageReference.child(snapshot.getValue(UserData.class).getsImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(view.getContext()).load(uri).into(imgAccount);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(view.getContext(), "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        databaseReference.child("DeletedProduct").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if (snapshot.getValue(RemoveProductData.class).getSanPham().getsUserID().equals(userID)) {
                                    removeProductDataArrayList.add(snapshot.getValue(RemoveProductData.class));
                                }

                                if (removeProductDataArrayList.size() != 0) {
                                    txtNotification.setVisibility(View.VISIBLE);
                                    txtNotification.setText(String.valueOf(removeProductDataArrayList.size()));
                                }
                                else {
                                    txtNotification.setVisibility(View.GONE);
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
                        databaseReference.child("CommentNeeds").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if (snapshot.getValue(OrderData.class).getNguoiMuaID().equals(userID)) {
                                    danhGiaSPList.add(snapshot.getValue(OrderData.class));
                                }

                                if (danhGiaSPList.size() != 0) {
                                    txtDanhGiaSP.setVisibility(View.VISIBLE);
                                    txtDanhGiaSP.setText(String.valueOf(danhGiaSPList.size()));
                                }
                                else{
                                    txtDanhGiaSP.setVisibility(View.GONE);
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
                        databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if (snapshot.getValue(OrderData.class).getNguoiMuaID().equals(userID) && snapshot.getValue(OrderData.class).getTinhTrang() != 7) {
                                    donMuaArrayList.add(snapshot.getValue(OrderData.class));
                                } else if (snapshot.getValue(OrderData.class).getNguoiBanID().equals(userID) && snapshot.getValue(OrderData.class).getTinhTrang() < 7) {
                                    donBanArrayList.add(snapshot.getValue(OrderData.class));
                                }

                                if (donMuaArrayList.size() != 0) {
                                    txtDonMuaNotify.setVisibility(View.VISIBLE);
                                    txtDonMuaNotify.setText(String.valueOf(donMuaArrayList.size()));
                                }
                                else{
                                    txtDonMuaNotify.setVisibility(View.GONE);
                                }

                                if (donBanArrayList.size() != 0) {
                                    txtDonBanNotify.setVisibility(View.VISIBLE);
                                    txtDonBanNotify.setText(String.valueOf(donBanArrayList.size()));
                                }
                                else{
                                    txtDonBanNotify.setVisibility(View.GONE);
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
                        databaseReference.child("MoneyIncome").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                if (snapshot.getValue(OrderData.class).getNguoiBanID().equals(userID)) {
                                    waitingList.add(snapshot.getValue(OrderData.class));
                                }
                                if (waitingList.size() != 0) {
                                    txtMoneyNotify.setVisibility(View.VISIBLE);
                                    txtMoneyNotify.setText(String.valueOf(waitingList.size()));
                                }
                                else {
                                    txtMoneyNotify.setVisibility(View.GONE);
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
            databaseReference.child("Appointment").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if ((snapshot.getValue(Appointment.class).getNguoiDuocHenID().equals(userID) && snapshot.getValue(Appointment.class).isActive()) || snapshot.getValue(Appointment.class).getNguoiHenID().equals(userID)) {
                        appointmentArrayList.add(snapshot.getValue(Appointment.class));
                    }

                    if (appointmentArrayList.size() != 0) {
                        txtLichHen.setVisibility(View.VISIBLE);
                        txtLichHen.setText(String.valueOf(appointmentArrayList.size()));
                    }
                    else{
                        txtLichHen.setVisibility(View.GONE);
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
        } else {
            Toast.makeText(view.getContext(), "Không nhận được dữ liệu!", Toast.LENGTH_SHORT).show();
        }

        btnLogout.setOnClickListener(logoutClick);
        btnAccountInfo.setOnClickListener(accountInfoClick);
        btnPassWordChange.setOnClickListener(passwordChangeClick);
        btnWallet.setOnClickListener(walletClick);
        btnShop.setOnClickListener(shopClick);
        btnDonMua.setOnClickListener(donMuaClick);
        btnDonBan.setOnClickListener(donBanClick);
        btnLichSuDonHang.setOnClickListener(lichSuDonHang);
        btnLichSuDonBan.setOnClickListener(lichSuDonBanClick);
        btnUploadPost.setOnClickListener(uploadedPostClick);
        btnDanhGiaSP.setOnClickListener(danhGiaSPClick);
        btnReceiveMoney.setOnClickListener(choNhanTienClick);
        btnTaoLichHen.setOnClickListener(taoLichHenClick);
        btnLichHen.setOnClickListener(lichHenClick);
        btnNotification.setOnClickListener(notificationClick);

//        Handler handler = new Handler();
//        int delay = 1000;
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//




//            }
//        }, delay);

        return view;
    }

    View.OnClickListener notificationClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), RemoveProductActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener lichHenClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), LichHenActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener taoLichHenClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserTaoLichHenActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.putExtra("NguoiCanGapID", "");
            intent.putExtra("NguoiCanGapName", "");
            intent.putExtra("TieuDe", "");
            intent.putExtra("NgayHen", "");
            intent.putExtra("ChiTiet", "");
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener choNhanTienClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserWaitingForMoneyActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener danhGiaSPClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserCommentNeedsActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            startActivity(intent);
        }
    };

    View.OnClickListener lichSuDonBanClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserLichSuDonBanActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener uploadedPostClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserUploadedPostActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener lichSuDonHang = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserTransactionHistoryActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener donBanClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), DonBanActivity.class);
            intent.putExtra("UserID", userID);
            intent.putExtra("UserName", sUserName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    View.OnClickListener donMuaClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), DonMuaActivity.class);
            intent.putExtra("UserID", userID);
            intent.putExtra("UserName", sUserName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    public boolean shopRegistrationCheck(ArrayList<ShopData> shopDataArrayList, String shopID) {
        for (ShopData shopData : shopDataArrayList) {
            if (shopData.getShopID().equals(shopID)) {
                return false;
            }
        }
        return true;
    }

    View.OnClickListener shopClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (diemThanhVien < 20 && shopID.isEmpty()) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa đủ điều kiện tạo shop!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            } else if (shopRegistrationCheck(shopDataArrayList, shopID) == false) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Yêu cầu tạo shop đang được xử lý!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            } else if (diemThanhVien >= 20 && shopID.isEmpty()) {
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                intent = new Intent(v.getContext(), ShopRegistrationActivity.class);
                                intent.putExtra("UserName", sUserName);
                                intent.putExtra("UserID", userID);
                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;

                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Bạn chưa đăng ký shop, tiến hành đăng ký?").setNegativeButton("No", dialogClick).setPositiveButton("Yes", dialogClick).show();
            }
//            else if(shopActiveCheck(shopDataArrayList, shopID) == 3){
//                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
//                alert.setMessage("Shop bạn đã bị khóa do điểm thành viên <20!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                }).show();
//            }
            else if (!shopID.isEmpty()) {
                databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if (snapshot.getValue(ShopData.class).getShopID().equals(shopID)) {
                            if (snapshot.getValue(ShopData.class).getTinhTrangShop() == -1) {
                                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                                alert.setMessage("Shop bạn đã bị khóa do điểm thành viên <20!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                            } else if (snapshot.getValue(ShopData.class).getTinhTrangShop() == 1) {
//                                Toast.makeText(v.getContext(), "Bạn đã tạo shop thành công!", Toast.LENGTH_SHORT).show();
                                intent = new Intent(v.getContext(), UserShopActivity.class);
                                intent.putExtra("UserName", sUserName);
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
            }
        }
    };

    View.OnClickListener walletClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), WalletUserActivity.class);
            intent.putExtra("UserName", sUserName);
            intent.putExtra("UserID", userID);
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
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
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
            builder.setMessage("Bạn muốn đăng xuất?").setNegativeButton("No", dialogClick).setPositiveButton("Yes", dialogClick).show();
        }
    };
}