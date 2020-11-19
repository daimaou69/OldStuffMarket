package com.example.oldstuffmarket.ui.wallet;

import androidx.fragment.app.Fragment;

import android.app.FragmentTransaction;
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
import androidx.appcompat.app.AlertDialog;

import com.example.oldstuffmarket.AdminMainActivity;
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

public class RutTienFragment extends Fragment {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private EditText edtSoTien;
    private Button btnXacNhan;
    private String sUserName, sTKID;
    private Intent intent;
    private ArrayList<UserData> userDataArrayList = WalletActivity.userDataArrayList;
    private ArrayList<TaiKhoanNH> taiKhoanNHArrayList = WalletActivity.taiKhoanNHArrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rut_tien, container, false);

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

    public boolean kTraSoDuVi(ArrayList<UserData> userDataArrayList, String sUserName, long lMoney){
        for (UserData userData : userDataArrayList){
            if(userData.getsUserName().equals(sUserName)){
                if(userData.getlMoney() < lMoney){
                    return false;
                }
            }
        }
        return true;
    }

    View.OnClickListener xacNhanClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(edtSoTien.getText().toString().isEmpty()){
                edtSoTien.setError("Bạn chưa nhập số tiền cần rút");
            }
            else if(Double.valueOf(edtSoTien.getText().toString())  < 50000){
                edtSoTien.setError("Số tiền rút tối thiểu từ 100000vnđ");
            }
            else if(kTraSoDuVi(userDataArrayList, sUserName, Long.valueOf(edtSoTien.getText().toString())) == false){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Không thể rút số tiền lớn hơn số tiền trong ví!");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alert.show();
            }
            else {
                DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName)){
                                            long iMyMoney = snapshot.getValue(UserData.class).getlMoney() - Long.valueOf(edtSoTien.getText().toString());
                                            UserData userUpdate = new UserData(sUserName,snapshot.getValue(UserData.class).getsShopID(),snapshot.getValue(UserData.class).getsFullName(),
                                                    snapshot.getValue(UserData.class).getsSdt(),snapshot.getValue(UserData.class).getsGioiTinh(),snapshot.getValue(UserData.class).getsDiaChi(),
                                                    snapshot.getValue(UserData.class).getsPassword(),snapshot.getValue(UserData.class).getsImage(),snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(),snapshot.getValue(UserData.class).getiPermission(),
                                                    snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                    snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(),iMyMoney);
                                            databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);
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
                                databaseReference.child("TaiKhoanNH").addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        if(snapshot.getValue(TaiKhoanNH.class).getsTKID().equals(sTKID)){
                                            long lNewMoneyData = snapshot.getValue(TaiKhoanNH.class).getlMoney() + Long.valueOf(edtSoTien.getText().toString());
                                            TaiKhoanNH taiKhoanNH = new TaiKhoanNH(snapshot.getValue(TaiKhoanNH.class).getsTKID(), snapshot.getValue(TaiKhoanNH.class).getsTenChuTK(),
                                                    snapshot.getValue(TaiKhoanNH.class).getsSoTK(), snapshot.getValue(TaiKhoanNH.class).getsTenNH(), lNewMoneyData);
                                            databaseReference.child("TaiKhoanNH").child(snapshot.getKey()).setValue(taiKhoanNH);
                                            edtSoTien.setText("");
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
                builder.setMessage("Bạn muốn rút " + edtSoTien.getText().toString() + "vnđ về tài khoản ngân hàng?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();

            }

        }
    };
}
