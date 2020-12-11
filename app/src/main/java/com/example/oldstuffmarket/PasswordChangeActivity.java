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
import android.widget.Toast;

import com.example.oldstuffmarket.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PasswordChangeActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<UserData> userDataArrayList;
    private Button btnPasswordSave, btnBack;
    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Intent intent;
    private String sOldPassword, sNewPassword, sConfirmPassword, sUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.password_change_layout);

        edtOldPassword = findViewById(R.id.edtOldPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnBack = findViewById(R.id.btnBack);
        btnPasswordSave = findViewById(R.id.btnPasswordSave);

        userDataArrayList = new ArrayList<>();
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

        btnBack.setOnClickListener(backClick);
        btnPasswordSave.setOnClickListener(saveClick);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            sUserName = getIntent().getExtras().getString("UserName");

        }
    }

    public boolean oldPasswordCheck(ArrayList<UserData> userDataArrayList, String sUserName, String sPassword){
        for(UserData userData : userDataArrayList){
            if(userData.getsUserName().equals(sUserName) && userData.getsPassword().equals(sPassword)){
                return true;
            }
        }
        return false;
    }

    View.OnClickListener saveClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sOldPassword = edtOldPassword.getText().toString();
            sNewPassword = edtNewPassword.getText().toString();
            sConfirmPassword = edtConfirmPassword.getText().toString();
            if(sOldPassword.isEmpty()){
                edtOldPassword.setError("Bạn chưa nhập mật khẩu cũ!");
            }
            else if (sNewPassword.isEmpty()) {
                edtNewPassword.setError("Bạn chưa nhập mật khẩu mới!");
            }
            else if(sNewPassword.length() < 6) {
                edtNewPassword.setError("Mật khẩu phải có ít nhất 6 ký tự!");
            }
            else if(sNewPassword.equals(sOldPassword)) {
                edtNewPassword.setError("Mật khẩu mới phải khác mật khẩu cũ!");
            }
            else if (sConfirmPassword.isEmpty()) {
                edtConfirmPassword.setError("Bạn chưa xác nhận mật khẩu!");
            }
            else if(sConfirmPassword.length() < 6) {
                edtConfirmPassword.setError("Mật khẩu phải có ít nhất 6 ký tự!");
            }
            else if(!sNewPassword.equals(sConfirmPassword)) {
                edtConfirmPassword.setError("Mật khẩu xác nhận chưa chính xác!");
            }
            else if(oldPasswordCheck(userDataArrayList, sUserName, edtOldPassword.getText().toString()) == false){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn nhập mật khẩu cũ chưa chính xác!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else{
                databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getsPassword().equals(sOldPassword)){
                            DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which){
                                        case DialogInterface.BUTTON_POSITIVE:
                                            UserData userUpdate = new UserData(snapshot.getValue(UserData.class).getsUserName(),snapshot.getValue(UserData.class).getsShopID(),
                                                    snapshot.getValue(UserData.class).getsFullName(), snapshot.getValue(UserData.class).getsSdt(), snapshot.getValue(UserData.class).getsGioiTinh(),
                                                    snapshot.getValue(UserData.class).getsDiaChi(), sConfirmPassword, snapshot.getValue(UserData.class).getsImage(),snapshot.getValue(UserData.class).getsUserID(), snapshot.getValue(UserData.class).getsNgayThamGia(), snapshot.getValue(UserData.class).getiPermission(),
                                                    snapshot.getValue(UserData.class).getiCommission(), snapshot.getValue(UserData.class).getiTinhTrang(), snapshot.getValue(UserData.class).getiSoSPDaBan(),
                                                    snapshot.getValue(UserData.class).getiAccPoint(), snapshot.getValue(UserData.class).getiReport(), snapshot.getValue(UserData.class).getlMoney());
                                            databaseReference.child("User").child(snapshot.getKey()).setValue(userUpdate);

                                            intent = new Intent(PasswordChangeActivity.this, LoginActivity.class);
                                            intent.setFlags(intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            finish();
                                            startActivity(intent);
                                            Toast.makeText(PasswordChangeActivity.this, "Cập nhật mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                            break;
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            return;

                                    }
                                }
                            };
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setMessage("Bạn muốn đổi mật khẩu?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();

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

    View.OnClickListener backClick = new View.OnClickListener() {
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
                    else if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getiPermission() == 0 || snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getiPermission() == 2){
                        Intent intent = new Intent(v.getContext(), AdminMainActivity.class);
                        intent.putExtra("UserName", sUserName);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        finish();
                        startActivity(intent);
                    }
                    else if(snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getiPermission() == 3 || snapshot.getValue(UserData.class).getsUserName().equals(sUserName) && snapshot.getValue(UserData.class).getiPermission() == 4){
                        Intent intent = new Intent(v.getContext(), ShipperMainActivity.class);
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
    };
}