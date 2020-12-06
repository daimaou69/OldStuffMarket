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
import com.example.oldstuffmarket.LienKetNganHangActivity;
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

public class ChuyenTienFragment extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private EditText edtSoTien, edtUS;
    private Button btnXacNhan;
    private String sUserName, sTKID;
    private Intent intent;
    private ArrayList<UserData> userDataArrayList = WalletActivity.userDataArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chuyen_khoan, container, false);

        edtSoTien = (EditText) view.findViewById(R.id.edtSoTien);
        edtUS = (EditText) view.findViewById(R.id.edtUS);
        btnXacNhan = (Button) view.findViewById(R.id.btnXacNhan);

        Bundle bundle = getArguments();

        if(bundle != null){
            sUserName = bundle.getString("UserName");
            sTKID = bundle.getString("SoTK");
        }

        btnXacNhan.setOnClickListener(confirmClick);

        return view;
    }

    public boolean kTraSoDu(ArrayList<UserData> userDataArrayList, String sUserName, long lMoney){
        for(UserData userData : userDataArrayList){
            if(userData.getsUserName().equals(sUserName)){
                if(userData.getlMoney() < lMoney){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean accountCheck(ArrayList<UserData> userDataArrayList, String sUS){
        for(UserData userData : userDataArrayList){
            if(userData.getsUserName().equals(sUS) || userData.getsSdt().equals(sUS)){
                return true;
            }
        }
        return false;
    }

    View.OnClickListener confirmClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtSoTien.getText().toString().isEmpty()){
                edtSoTien.setError("Bạn chưa nhập số tiền cần chuyển!");
            }
            else if(Long.valueOf(edtSoTien.getText().toString()) < 50000){
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Số tiền cần chuyển phải >=50000vnđ!").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(kTraSoDu(userDataArrayList, sUserName, Long.valueOf(edtSoTien.getText().toString())) == false){
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Bạn không đủ số dư để thực hiện giao dịch!").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(edtUS.getText().toString().isEmpty()){
                edtUS.setError("Bạn chưa nhập thông tin người cần chuyển!");
            }
            else if(accountCheck(userDataArrayList, edtUS.getText().toString()) == false){
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Không tìm thấy tài khoản chuyển tiền?").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else {
                for(UserData userData : userDataArrayList){
                    if(userData.getsUserName().equals(edtUS.getText().toString()) || userData.getsSdt().equals(edtUS.getText().toString())){
                        DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName)){
                                                    long lNewMoney = snapshot.getValue(UserData.class).getlMoney() - Long.valueOf(edtSoTien.getText().toString());
                                                    UserData userUpdate = new UserData(sUserName,snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),
                                                            snapshot.getValue(UserData.class).getsSdt(),snapshot.getValue(UserData.class).getsGioiTinh(),snapshot.getValue(UserData.class).getsDiaChi(),
                                                            snapshot.getValue(UserData.class).getsPassword(),snapshot.getValue(UserData.class).getsImage(),snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(),snapshot.getValue(UserData.class).getiPermission(),
                                                            snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                            snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(),lNewMoney);
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
                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(UserData.class).getsUserName().equals(edtUS.getText().toString()) || snapshot.getValue(UserData.class).getsSdt().equals(edtUS.getText().toString())){
                                                    long lNewMoney = snapshot.getValue(UserData.class).getlMoney() + Long.valueOf(edtSoTien.getText().toString());
                                                    UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),
                                                            snapshot.getValue(UserData.class).getsSdt(),snapshot.getValue(UserData.class).getsGioiTinh(),snapshot.getValue(UserData.class).getsDiaChi(),
                                                            snapshot.getValue(UserData.class).getsPassword(),snapshot.getValue(UserData.class).getsImage(),snapshot.getValue(UserData.class).getsUserID(),
                                                            snapshot.getValue(UserData.class).getsNgayThamGia(),snapshot.getValue(UserData.class).getiPermission(),
                                                            snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                            snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(),lNewMoney);
                                                    databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
                                                    edtSoTien.setText("");
                                                    edtUS.setText("");
                                                    Toast.makeText(v.getContext(),"Chuyển tiền thành công!", Toast.LENGTH_SHORT).show();
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
                        builder.setMessage("Xác nhận chuyển " + edtSoTien.getText().toString() + "vnđ cho tài khoản: " +
                                userData.getsUserName() + "-" + userData.getsFullName() + "-" + userData.getsSdt() + ", sau khi xác nhận sẽ không hoàn tác được?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
                    }
                }
            }

        }
    };
}
