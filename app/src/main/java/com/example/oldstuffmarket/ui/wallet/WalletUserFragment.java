package com.example.oldstuffmarket.ui.wallet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.oldstuffmarket.R;
import com.example.oldstuffmarket.UserMainActivity;
import com.example.oldstuffmarket.WalletActivity;
import com.example.oldstuffmarket.data_models.TaiKhoanNH;
import com.example.oldstuffmarket.data_models.UserData;
import com.example.oldstuffmarket.data_models.UserDepositData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WalletUserFragment extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private EditText edtSoTien;
    private Button btnXacNhan;
    private String sUserName, userID;
    private Intent intent;
    private ArrayList<UserData> userDataArrayList = WalletActivity.userDataArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nap_tien, container, false);

        edtSoTien = (EditText) view.findViewById(R.id.edtSoTien);
        btnXacNhan = (Button) view.findViewById(R.id.btnXacNhan);

        Bundle bundle = getArguments();

        if(bundle != null){
            sUserName = bundle.getString("UserName");
            userID = bundle.getString("UserID");
        }

        btnXacNhan.setOnClickListener(xacNhanClick);

        return view;
    }

    View.OnClickListener xacNhanClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch(which){
                        case DialogInterface.BUTTON_POSITIVE:
//                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
//                                @Override
//                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                                    if(snapshot.getValue(UserData.class).getsUserName().equals("admin")){
//                                        long iMyMoney = snapshot.getValue(UserData.class).getlMoney() + Long.valueOf(edtSoTien.getText().toString());
//                                        UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),
//                                                snapshot.getValue(UserData.class).getsSdt(),snapshot.getValue(UserData.class).getsGioiTinh(),snapshot.getValue(UserData.class).getsDiaChi(),
//                                                snapshot.getValue(UserData.class).getsPassword(),snapshot.getValue(UserData.class).getsImage(),snapshot.getValue(UserData.class).getsUserID(),snapshot.getValue(UserData.class).getsTaiKhoanNH(), snapshot.getValue(UserData.class).getsNgayThamGia(),snapshot.getValue(UserData.class).getiPermission(),
//                                                snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
//                                                snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(),iMyMoney);
//                                        databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
//                                    }
//                                }
//
//                                @Override
//                                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                                }
//
//                                @Override
//                                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//                                }
//
//                                @Override
//                                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });

                            String giaoDichID = databaseReference.push().getKey();
                            UserDepositData userDepositData = new UserDepositData(giaoDichID, userID, Long.valueOf(edtSoTien.getText().toString()), 0);
                            databaseReference.child("UserDepositRequest").child(giaoDichID).setValue(userDepositData);
                            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                            alert.setMessage("Yêu cầu của bạn đang được xử lí!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    intent = new Intent(v.getContext(), UserMainActivity.class);
                                    intent.putExtra("UserName", sUserName);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(intent);
                                    edtSoTien.setText("");
                                }
                            }).show();
                            break;
                        case DialogInterface.BUTTON_NEGATIVE:
                            return;
                    }
                }
            };
            AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
            alert.setMessage("Bạn muốn nạp " + edtSoTien.getText().toString() + "vnđ vào ví?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
        }
    };
}
