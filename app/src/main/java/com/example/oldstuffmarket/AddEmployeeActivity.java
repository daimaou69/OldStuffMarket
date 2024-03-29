package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.oldstuffmarket.data_models.UserData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

public class AddEmployeeActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();//tạo liên kết firebase
    private EditText edtUserName, edtFullName, edtSDT, edtDiaChi;// các trường nhập thông tin tài khoản
    private Spinner spnGender;//Spinner giới tính user
    private Button btnRegistry, btnBack;//nút đăng ký
    private Spinner spnLoaiNhanVien;
    private String UserName = "", FullName = "", SDT = "", DiaChi = "", Gender = "";//các biến lưu thông tin như user name, full name,...
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private ArrayList<UserData> userList;//khai báo danh sách user
    private ArrayList<UserData> blackList;
    private static final String USERNAME_PATTERN = "^[a-z0-9]{3,8}$";//kiểm tra user name nhập vào
    private Pattern pattern;
    private String admin = "";
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.add_employee_layout);

        spnGender = findViewById(R.id.spnGender);
        edtUserName = findViewById(R.id.edtUserName);
        edtFullName = findViewById(R.id.edtFullName);
        edtSDT = findViewById(R.id.edtSDT);
        edtDiaChi = findViewById(R.id.edtDiaChi);
        spnLoaiNhanVien = findViewById(R.id.spnLoaiNhanVien);
        btnRegistry = findViewById(R.id.btnRegistry);
        btnBack = findViewById(R.id.btnBack);

        userList = new ArrayList<UserData>();//tạo mới user list
        blackList = new ArrayList<UserData>();

        mDatabase.child("User").addValueEventListener(new ValueEventListener() {// lọc dữ liệu trong mục User
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    userList.add(dataSnapshot.getValue(UserData.class));// thêm dữ liệu vào user list
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase.child("BlackList").addValueEventListener(new ValueEventListener() {// lọc dữ liệu trong mục User
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    blackList.add(dataSnapshot.getValue(UserData.class));// thêm dữ liệu vào user list
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnRegistry.setOnClickListener(OnClick);//sự kiện bấm nút đăng ký

        btnBack.setOnClickListener(backClick);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            admin = getIntent().getExtras().getString("UserName");
        }
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), AdminMainActivity.class);//quay trở lại trang đăng nhập
            intent.putExtra("UserName", admin);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };

    public boolean userNameValidate(String username) {//kiểm tra user name nhập vào có hợp lệ (ít nhất từ 3-8 ký tự bào gồm các chữ cái a-z hoặc các số từ 0-9)
        pattern = Pattern.compile(USERNAME_PATTERN);
        return pattern.matcher(username).matches();
    }

    public boolean sdtCheck(ArrayList<UserData> userList, String sSDT){// kiểm tra sdt có tồn tại trên csdl hay chưa
        for(UserData user : userList){
            if(user.getsSdt().equals(sSDT)){
                return false;
            }
        }
        return true;
    }

    public boolean UserNameCheck(ArrayList<UserData> userList, String sUserName){// kiểm tra user name có tồn tại trên csdl hay chưa
        for(UserData user : userList){
            if(user.getsUserName().equals(sUserName)){
                return false;
            }
        }
        return true;
    }

    public boolean blackListSdtCheck(ArrayList<UserData> blackList, String sSDT){// kiểm tra sdt có tồn tại trên csdl hay chưa
        for(UserData user : blackList){
            if(user.getsSdt().equals(sSDT)){
                return false;
            }
        }
        return true;
    }

    public boolean blackListNameCheck(ArrayList<UserData> blackList, String sUserName){// kiểm tra user name có tồn tại trên csdl hay chưa
        for(UserData user : blackList){
            if(user.getsUserName().equals(sUserName)){
                return false;
            }
        }
        return true;
    }

    View.OnClickListener OnClick = new View.OnClickListener() {// tạo sự kiện click cho nút đăng ký
        @Override
        public void onClick(View v) {
            UserName = edtUserName.getText().toString();
            FullName = edtFullName.getText().toString();
            SDT = edtSDT.getText().toString();
            DiaChi = edtDiaChi.getText().toString();
            Gender = spnGender.getSelectedItem().toString();
            if(UserName.isEmpty()){//kiểm tra user có nhập hay chưa
                edtUserName.setError("Bạn chưa nhập user name!");
            }
            else if(UserNameCheck(userList,UserName) == false){//kiểm tra user có tồn tại hay không
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Username đã tồn tại trong hệ thống!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(userNameValidate(UserName) == false){// kiểm tra user name có hợp lệ chưa
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("User name phải có từ 3-5 ký tự a-z và số từ 0-9!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if (blackListNameCheck(blackList, UserName) == false) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Username đã tồn tại trong danh sách đen!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(FullName.isEmpty()){//kiểm tra họ tên có nhập hay chưa
                edtFullName.setError("Bạn chưa nhập họ tên!");
            }
            else if(SDT.isEmpty()) {//kiểm tra sdt có nhập hay chưa
                edtSDT.setError("Bạn chưa nhập số điện thoại!");
            }
            else if(SDT.length() > 11 || SDT.length() < 10){//kiểm tra SDT có hợp lệ không
                edtSDT.setError("Bạn nhập sai số điện thoại!");
            }
            else if(sdtCheck(userList,edtSDT.getText().toString()) == false){//kiểm tra SDT đã tồn tại chưa
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Số điện thoại đã tồn tại trong hệ thống!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if (blackListSdtCheck(blackList, edtSDT.getText().toString()) == false) {
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Số điện thoại đã tồn tại trong danh sách đen!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(DiaChi.isEmpty()){//kiểm tra địa chỉ có nhập hay chưa
                edtDiaChi.setError("Bạn chưa nhập địa chỉ!");
            }
            else{
                if(spnLoaiNhanVien.getSelectedItemPosition() == 0){
                    DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    UserData user = new UserData();
                                    CreateUser(UserName, FullName, SDT, Gender, DiaChi, UserName, 2);//chạy hàm tạo mới user và đưa user vào firebase
                                    Toast.makeText(AddEmployeeActivity.this, "Thêm nhân viên thành công!", Toast.LENGTH_SHORT).show();// thông báo tạo tài khoản thành công
                                    edtDiaChi.setText("");
                                    edtSDT.setText("");
                                    edtFullName.setText("");
                                    edtUserName.setText("");
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    return;
                            }
                        }
                    };

                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    alert.setMessage("Bạn muốn thêm nhân viên quản lí?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
                }
                else if(spnLoaiNhanVien.getSelectedItemPosition() == 1){
                    DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    UserData user = new UserData();
                                    CreateUser(UserName, FullName, SDT, Gender, DiaChi, UserName, 3);//chạy hàm tạo mới user và đưa user vào firebase
                                    Toast.makeText(AddEmployeeActivity.this, "Thêm nhân viên thành công!", Toast.LENGTH_SHORT).show();// thông báo tạo tài khoản thành công
                                    edtDiaChi.setText("");
                                    edtSDT.setText("");
                                    edtFullName.setText("");
                                    edtUserName.setText("");
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    return;
                            }
                        }
                    };

                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    alert.setMessage("Bạn muốn thêm nhân viên giao hàng?").setNegativeButton("No", dialog).setPositiveButton("Yes", dialog).show();
                }
            }
        }
    };
    private void CreateUser(String sUserName, String sFullName, String sSdt, String sGioiTinh, String sDiaChi, String sPassword, int iPermission){// hàm tạo mới user và đưa vào firebase

        String sKey = mDatabase.push().getKey();

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");// định dạng ngày
        Date date = new Date();// lấy ngày hiện tại trong hệ thống

        UserData user = new UserData(sUserName,"", sFullName, sSdt, sGioiTinh, sDiaChi, sPassword, "",sKey, dateFormat.format(date), iPermission, 0, 0, 0, 0, 0, 0);// tạo mới user

        mDatabase.child("User").child(sKey).setValue(user);//đưa user mới vào firebase
    }
}