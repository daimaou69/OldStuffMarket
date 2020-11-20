package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oldstuffmarket.data_models.Commission;
import com.example.oldstuffmarket.data_models.TaiKhoanNH;
import com.example.oldstuffmarket.data_models.UserData;
import com.example.oldstuffmarket.ui.dashboard.SettingsFragment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();// liên kết firebase
    private ArrayList<UserData> userDataArrayList; // khai bao list User
    private TextView txtRegistry; //link đăng ký tài khoản mới
    private Button btnLogin; //nút login
    private EditText edtLoginName, edtLoginPass; // EditText user name, password
    static public Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.login_layout);

        txtRegistry = findViewById(R.id.txtRegistry);
        btnLogin = findViewById(R.id.btnLogin);
        edtLoginName = findViewById(R.id.edtLoginName);
        edtLoginPass = findViewById(R.id.edtLoginPass);

        userDataArrayList = new ArrayList<>();

        txtRegistry.setOnClickListener(new View.OnClickListener() {// click vào link đăng ký tài khoản mới
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Đăng ký tài khoản!",Toast.LENGTH_SHORT).show(); //hiện thông báo chuyển trang
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class); //chuyển sang trang khác
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

//        databaseReference = FirebaseDatabase.getInstance().getReference();
//        String sUserID = databaseReference.push().getKey();
//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        Date date = new Date();
//        UserData user = new UserData("admin","", "Trần Thanh Tùng", "0123456789", "Nam", "Thủ Đức", "admin", "", sUserID, dateFormat.format(date), 0, 10, 0, 0, 0, 0, 0);
//        databaseReference.child("User").child(sUserID).setValue(user);
//        databaseReference.child("TaiKhoanNH").child(sTKID).setValue(taiKhoanNH);
//        String sKey = databaseReference.push().getKey();
//        Commission commission = new Commission(sKey, 4, 3);
//        databaseReference.child("Commission").child(sKey).setValue(commission);

    }

    @Override
    protected void onResume() {
        super.onResume();
        edtLoginPass.setText("");
        userDataArrayList.clear();// làm rỗng danh sách User cũ

        databaseReference.child("User").addChildEventListener(new ChildEventListener() {//lọc dữ liệu trong mục User tên firebase
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                UserData userData = snapshot.getValue(UserData.class);
                userDataArrayList.add(userData); //thêm user vào user list
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

        btnLogin.setOnClickListener(LoginClick);//bấm nút login
    }

    public int loginCheck(ArrayList<UserData> userDataArrayList, String sUserName, String sPass){//kiểm tra user name và password đưa vào
        int iLoginResult = -1;
        for(UserData user : userDataArrayList){
            if(user.getsUserName().equals(sUserName) && user.getsPassword().equals(sPass) && user.getiTinhTrang() == 0){
                iLoginResult = user.getiPermission();
            }
            else if(user.getsUserName().equals(sUserName) && user.getsPassword().equals(sPass) && user.getiTinhTrang() == -2){
                iLoginResult = user.getiTinhTrang();
            }
        }
        return iLoginResult;//kết quả trả về
    }

    View.OnClickListener LoginClick = new View.OnClickListener() {//tạo sự kiện bấm nút login
        @Override
        public void onClick(View v) {
            int iLogin = loginCheck(userDataArrayList, edtLoginName.getText().toString(), edtLoginPass.getText().toString());//kết quả sau khi kiểm tra user name và password
            if(edtLoginName.getText().toString().isEmpty()){// kiểm tra user name đã được nhập hay chưa
                edtLoginName.setError("Bạn chưa nhập user name!");//xuất thông báo chưa nhập user name
            }
            else if(edtLoginPass.getText().toString().isEmpty()){// kiểm tra password đã được nhập hay chưa
                edtLoginPass.setError("Bạn chưa nhập mật khẩu!");//xuất thông báo chưa nhập password
            }
            else if(iLogin == 0){//nếu kết quả đăng nhập == 0
                intent = new Intent(LoginActivity.this, AdminMainActivity.class);//chuyển đến trang admin
                intent.putExtra("UserName", edtLoginName.getText().toString());
                startActivity(intent);
            }
            else if(iLogin == 1){//nếu kết quả đăng nhập == 1

                intent = new Intent(LoginActivity.this, UserMainActivity.class);//chuyển đến trang user
                intent.putExtra("UserName", edtLoginName.getText().toString());
                startActivity(intent);
            }
            else if(iLogin == -1) {//nếu kết quả đăng nhập == -1 (tài khoản bị khóa)
                Toast.makeText(v.getContext(), "Tài khoản của bạn đã bị khóa, hãy liên hệ admin!",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(v.getContext(), "Đăng nhập thất bại!",Toast.LENGTH_SHORT).show();
            }
        }
    };
}