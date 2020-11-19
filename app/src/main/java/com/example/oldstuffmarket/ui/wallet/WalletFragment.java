package com.example.oldstuffmarket.ui.wallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.oldstuffmarket.AdminMainActivity;
import com.example.oldstuffmarket.LoginActivity;
import com.example.oldstuffmarket.R;
import com.example.oldstuffmarket.UserMainActivity;
import com.example.oldstuffmarket.WalletActivity;
import com.example.oldstuffmarket.data_models.TaiKhoanNH;
import com.example.oldstuffmarket.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WalletFragment extends Fragment {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private EditText edtSoTien;
    private Button btnXacNhan;
    private String sUserName, sTKID;
    private Intent intent;
    private ArrayList<UserData> userDataArrayList = WalletActivity.userDataArrayList;
    private ArrayList<TaiKhoanNH> taiKhoanNHArrayList = WalletActivity.taiKhoanNHArrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nap_tien, container, false);
//        View view = super.onCreateView(inflater, container, savedInstanceState);

        edtSoTien = (EditText) view.findViewById(R.id.edtSoTien);
        btnXacNhan = (Button) view.findViewById(R.id.btnXacNhan);

        btnXacNhan.setOnClickListener(xacNhanClick);

        Bundle bundle = getArguments();

        if(bundle != null){
            sUserName = bundle.getString("UserName");
            sTKID = bundle.getString("SoTK");
        }

        return view;
    }

    public int kTraSoDu(ArrayList<TaiKhoanNH> taiKhoanNHArrayList, String sSoTK, long lSoTien){
        for(TaiKhoanNH taiKhoanNH : taiKhoanNHArrayList){
            if(taiKhoanNH.getsTKID().equals(sSoTK)){
                if(taiKhoanNH.getlMoney() < lSoTien){
                    return -1;
                }
                else if((taiKhoanNH.getlMoney() - lSoTien) < 50000){
                    return 0;
                }
            }
        }
        return 1;
    }

    View.OnClickListener xacNhanClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtSoTien.getText().toString().isEmpty()){
                edtSoTien.setError("Bạn chưa nhập số tiền cần nạp");
            }
            else if(Long.valueOf(edtSoTien.getText().toString())  < 50000){
                edtSoTien.setError("Phải nạp số tiền tối thiểu từ 50000vnđ");
            }
            else if(kTraSoDu(taiKhoanNHArrayList, sTKID, Long.valueOf(edtSoTien.getText().toString())) == -1 || kTraSoDu(taiKhoanNHArrayList, sTKID, Long.valueOf(edtSoTien.getText().toString())) == 0){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Tài khoản của bạn không đủ số dư để thực hiện giao dịch!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(kTraSoDu(taiKhoanNHArrayList, sTKID, Long.valueOf(edtSoTien.getText().toString())) == 1){
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                databaseReference.child("TaiKhoanNH").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(TaiKhoanNH.class).getsTKID().equals(sTKID)){
                                            long lNewMoneyData = snapshot.getValue(TaiKhoanNH.class).getlMoney() - Long.valueOf(edtSoTien.getText().toString());
                                            TaiKhoanNH taiKhoanNH = new TaiKhoanNH(snapshot.getValue(TaiKhoanNH.class).getsTKID(), snapshot.getValue(TaiKhoanNH.class).getsTenChuTK(),
                                                    snapshot.getValue(TaiKhoanNH.class).getsSoTK(), snapshot.getValue(TaiKhoanNH.class).getsTenNH(), lNewMoneyData);
                                            databaseReference.child("TaiKhoanNH").child(snapshot.getKey()).setValue(taiKhoanNH);
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
                                        if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName)){
                                            long iMyMoney = snapshot.getValue(UserData.class).getlMoney() + Long.valueOf(edtSoTien.getText().toString());
                                            UserData userUpdate = new UserData(sUserName,snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),
                                                    snapshot.getValue(UserData.class).getsSdt(),snapshot.getValue(UserData.class).getsGioiTinh(),snapshot.getValue(UserData.class).getsDiaChi(),
                                                    snapshot.getValue(UserData.class).getsPassword(),snapshot.getValue(UserData.class).getsImage(),snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(),snapshot.getValue(UserData.class).getiPermission(),
                                                    snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                    snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(),iMyMoney);
                                            databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
                                            edtSoTien.setText("");
                                            if(snapshot.getValue(UserData.class).getiPermission() == 1){
                                                intent = new Intent(v.getContext(),UserMainActivity.class);
                                                intent.putExtra("UserName", sUserName);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                startActivity(intent);
                                            }
                                            else if(snapshot.getValue(UserData.class).getiPermission() == 0){
                                                intent = new Intent(v.getContext(), AdminMainActivity.class);
                                                intent.putExtra("UserName", sUserName);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Bạn muốn nạp " + edtSoTien.getText().toString() + "vnđ vào tài khoản?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
            }

        }
    };
}
