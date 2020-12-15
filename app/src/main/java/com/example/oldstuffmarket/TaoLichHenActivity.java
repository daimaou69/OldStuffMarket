package com.example.oldstuffmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class TaoLichHenActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private String userID, userName, nguoiCanGapID;
    private Intent intent;
    private EditText edtTieuDe, edtNgayHen, edtNguoiCanGap, edtChiTietCuocHen;
    private Button btnChonNgay, btnTimUser, btnCreate, btnBack, btnClear;
    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.tao_lich_hen_layout);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnChonNgay = (Button) findViewById(R.id.btnChonNgay);
        btnTimUser = (Button) findViewById(R.id.btnTimUser);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        btnBack = (Button) findViewById(R.id.btnBack);
        edtTieuDe = (EditText) findViewById(R.id.edtTieuDe);
        edtNgayHen = (EditText) findViewById(R.id.edtNgayHen);
        edtNguoiCanGap = (EditText) findViewById(R.id.edtNguoiCanGap);
        edtChiTietCuocHen = (EditText) findViewById(R.id.edtChiTietCuocHen);


        btnBack.setOnClickListener(backClick);

        btnClear.setOnClickListener(clearClick);
        btnTimUser.setOnClickListener(timUserClick);

        btnChonNgay.setOnClickListener(chonNgayClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");
            nguoiCanGapID = getIntent().getExtras().getString("NguoiCanGapID");



        }
    }

    View.OnClickListener timUserClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    View.OnClickListener chonNgayClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            edtNgayHen.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    };

    View.OnClickListener clearClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            edtChiTietCuocHen.setText("");
            edtNgayHen.setText("");
            edtNguoiCanGap.setText("");
            edtTieuDe.setText("");
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), UserMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}