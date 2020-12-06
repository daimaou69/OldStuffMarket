package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.oldstuffmarket.adapter.DonMuaAdapter;
import com.example.oldstuffmarket.data_models.OrderData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ShipperDonDaDongGoiActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<OrderData> orderDataArrayList;
    private Button btnBack;
    private GridView gridDonDaDongGoi;
    private Intent intent;
    private String userName, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.shipper_don_da_dong_goi_layout);

        orderDataArrayList = new ArrayList<>();

        gridDonDaDongGoi = (GridView) findViewById(R.id.gridDonDaDongGoi);
        btnBack = (Button) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(backClick);
        gridDonDaDongGoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(view.getContext(), ShipperDetailDonDaDongGoiActivity.class);
                intent.putExtra("UserName", userName);
                intent.putExtra("UserID", userID);
                intent.putExtra("DonHangID", orderDataArrayList.get(position).getDonHangID());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        orderDataArrayList.clear();
        databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.getValue(OrderData.class).getLoaiDonHang() != 1 && snapshot.getValue(OrderData.class).getTinhTrang() == 3){
                    orderDataArrayList.add(snapshot.getValue(OrderData.class));
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

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
            userID = getIntent().getExtras().getString("UserID");
        }

        Handler handler = new Handler();
        int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                donHangLoad();
            }
        }, delay);
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(v.getContext(), ShipperMainActivity.class);
            intent.putExtra("UserName", userName);
            startActivity(intent);
        }
    };

    public void donHangLoad(){
        DonMuaAdapter donMuaAdapter = new DonMuaAdapter(ShipperDonDaDongGoiActivity.this, R.layout.don_mua_adapter, orderDataArrayList);
        gridDonDaDongGoi.setAdapter(donMuaAdapter);
    }
}