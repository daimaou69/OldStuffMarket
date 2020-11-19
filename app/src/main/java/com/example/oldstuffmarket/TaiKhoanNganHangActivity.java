package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.oldstuffmarket.data_models.TaiKhoanNH;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TaiKhoanNganHangActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private TextView txtHoTen, txtSoDu;
    private Button btnBack;
    private ArrayList<TaiKhoanNH> taiKhoanNHArrayList;
    private String sTKID, sUserName;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.tai_khoan_ngan_hang_layout);

        txtHoTen = (TextView) findViewById(R.id.txtHoTen);
        txtSoDu = (TextView) findViewById(R.id.txtSoDu);
        btnBack = (Button) findViewById(R.id.btnBack);

        taiKhoanNHArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            sTKID = getIntent().getExtras().getString("TaiKhoanID");
            sUserName = getIntent().getExtras().getString("UserName");
            databaseReference.child("TaiKhoanNH").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(TaiKhoanNH.class).getsTKID().equals(sTKID)){
                        txtHoTen.setText(snapshot.getValue(TaiKhoanNH.class).getsTenChuTK());
                        txtSoDu.setText(String.valueOf(snapshot.getValue(TaiKhoanNH.class).getlMoney()) + "vnđ");
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

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), WalletActivity.class);
            intent.putExtra("SoTK", sTKID);
            intent.putExtra("UserName", sUserName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}