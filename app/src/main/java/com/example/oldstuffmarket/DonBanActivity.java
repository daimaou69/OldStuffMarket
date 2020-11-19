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

public class DonBanActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private GridView gridDonBan;
    private Button btnBack;
    private ArrayList<OrderData> orderDataArrayList;
    private String userID, userName;
    private DonMuaAdapter donMuaAdapter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.don_ban_layout);

        orderDataArrayList = new ArrayList<>();

        btnBack = (Button) findViewById(R.id.btnBack);
        gridDonBan = (GridView) findViewById(R.id.gridDonBan);

        btnBack.setOnClickListener(backClick);

        gridDonBan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(orderDataArrayList.get(position).getLoaiDonHang() == 1){
                    intent = new Intent(view.getContext(), OrderThanhToanTrucTiepActivity.class);
                    intent.putExtra("UserID", userID);
                    intent.putExtra("UserName", userName);
                    intent.putExtra("DonHangID", orderDataArrayList.get(position).getDonHangID());
                    startActivity(intent);
                }
                else if(orderDataArrayList.get(position).getLoaiDonHang() == 2 || orderDataArrayList.get(position).getLoaiDonHang() == 3){
                    intent = new Intent(view.getContext(), OrderProccessingForSellerActivity.class);
                    intent.putExtra("UserID", userID);
                    intent.putExtra("UserName", userName);
                    intent.putExtra("DonHangID", orderDataArrayList.get(position).getDonHangID());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null){
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");

            orderDataArrayList.clear();
            databaseReference.child("DonHang").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(OrderData.class).getNguoiBanID().equals(userID)){
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

        }

        Handler handler = new Handler();
        int delay = 1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                donMuaLoad();
            }
        }, delay);
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), UserMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    public void donMuaLoad(){
        donMuaAdapter = new DonMuaAdapter(DonBanActivity.this, R.layout.don_mua_adapter, orderDataArrayList);
        gridDonBan.setAdapter(donMuaAdapter);
    }
}