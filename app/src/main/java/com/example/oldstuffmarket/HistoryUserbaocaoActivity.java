package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;

import com.example.oldstuffmarket.adapter.DonMuaAdapter;
import com.example.oldstuffmarket.adapter.GiaoDichAdapter;
import com.example.oldstuffmarket.data_models.OrderData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class HistoryUserbaocaoActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Intent intent;
    private Button btnBack;
    private String userName, reportID, userID, sUserID;
    private ArrayList<OrderData> orderDataArrayList;
    private GridView gridHistory1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_history_userbaocao);

        btnBack = (Button) findViewById(R.id.btnBack);
        gridHistory1 = (GridView) findViewById(R.id.gridHistory1);

        orderDataArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString("UserName");
            sUserID = getIntent().getExtras().getString("sUserID");
            reportID = getIntent().getExtras().getString("ReportID");
            userID = getIntent().getExtras().getString("UserID");
            orderDataArrayList.clear();
            databaseReference.child("LichSuGiaoDich").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(OrderData.class).getNguoiMuaID().equals(sUserID)) {
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
            final Handler handler = new Handler();
            final int delay = 1000; //milliseconds
            handler.postDelayed(new Runnable(){
                public void run(){
                    userLoad();
                }
            }, delay);

        }
    }

    public void userLoad(){
        DonMuaAdapter donMuaAdapter = new DonMuaAdapter(HistoryUserbaocaoActivity.this, R.layout.don_mua_adapter_layout, orderDataArrayList);
        gridHistory1.setAdapter(donMuaAdapter);
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), UserDetailReportActivity.class);
            intent.putExtra("UserName", userName);
            intent.putExtra("ReportID", reportID);
            intent.putExtra("sUserID", sUserID);
            intent.putExtra("UserID", userID);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
}