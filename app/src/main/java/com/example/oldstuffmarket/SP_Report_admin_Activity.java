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
import com.example.oldstuffmarket.adapter.SPbibaocao_Adapter;
import com.example.oldstuffmarket.data_models.OrderData;
import com.example.oldstuffmarket.data_models.ProductReport;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SP_Report_admin_Activity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private GridView gridReportSP;
    private Button btnBack;
    private Intent intent;
    private ArrayList<ProductReport> productReportArrayList;
    private String userName;
    private SPbibaocao_Adapter sPbibaocaoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sp_report_admin);

        productReportArrayList = new ArrayList<>();
        btnBack = (Button) findViewById(R.id.btnBack);
        gridReportSP = (GridView) findViewById(R.id.gridReportSP);

        btnBack.setOnClickListener(backClick);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(getIntent().getExtras() != null) {
            userName = getIntent().getExtras().getString("UserName");
            productReportArrayList.clear();
            databaseReference.child("ProductReport").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    productReportArrayList.add(snapshot.getValue(ProductReport.class));
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
            final int delay = 500; //milliseconds
            handler.postDelayed(new Runnable(){
                public void run(){
                    sanPhamLoad();
                }
            }, delay);
        }
        gridReportSP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(view.getContext(), Product_detail_ReportActivity.class);
                intent.putExtra("ReportID", productReportArrayList.get(position).getReportID());
                intent.putExtra("UserID", productReportArrayList.get(position).getSanPham().getsUserID());
                intent.putExtra("UserName", userName);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), AdminMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };

    public void sanPhamLoad(){
        sPbibaocaoAdapter = new SPbibaocao_Adapter(SP_Report_admin_Activity.this, R.layout.activity_spbibaocao_adapter, productReportArrayList);
        gridReportSP.setAdapter(sPbibaocaoAdapter);
    }
}