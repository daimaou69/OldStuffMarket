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
import android.widget.TextView;

import com.example.oldstuffmarket.adapter.ThongKeAdapter;
import com.example.oldstuffmarket.data_models.OrderData;
import com.example.oldstuffmarket.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ThongKeActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Intent intent;
    private String userName;
    private Button btnBack;
    private GridView gridThongke;
    private TextView txtDoanhThu;
    private ArrayList<UserData> userDataArrayList;
    private ArrayList<OrderData> orderDataArrayList;
    private ThongKeAdapter thongKeAdapter;
    private long tong, commission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_thong_ke);

        btnBack = (Button) findViewById(R.id.btnBack);
        gridThongke = (GridView) findViewById(R.id.gridThongke);
        txtDoanhThu = (TextView) findViewById(R.id.txtDoanhThu);

        userDataArrayList = new ArrayList<>();
        orderDataArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);
        gridThongke.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(view.getContext(), GiaoDichActivity.class);
                intent.putExtra("UserID", userDataArrayList.get(position).getsUserID());
                intent.putExtra("UserName", userName);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        userDataArrayList.clear();
        orderDataArrayList.clear();

        if(getIntent().getExtras() != null){
            userName = getIntent().getExtras().getString("UserName");
            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.getValue(UserData.class).getiPermission() == 1) {
                        userDataArrayList.add(snapshot.getValue(UserData.class));
                    }
                    userLoad();
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
            databaseReference.child("LichSuGiaoDich").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    orderDataArrayList.add(snapshot.getValue(OrderData.class));
                    if (snapshot.getValue(OrderData.class).getTinhTrang() == 8 && snapshot.getValue(OrderData.class).getLoaiDonHang() != 1) {
                        commission = snapshot.getValue(OrderData.class).getGiaTien() * snapshot.getValue(OrderData.class).getSellerCommission() / 100;
                        tong = tong + commission;
                        txtDoanhThu.setText(String.valueOf(tong) + "VNĐ");
                    }
                    userLoad();
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
            Intent intent = new Intent(v.getContext(), AdminMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            finish();
            startActivity(intent);
        }
    };
    public void userLoad(){
        thongKeAdapter = new ThongKeAdapter(ThongKeActivity.this, R.layout.thongke_adapter_layout, userDataArrayList, orderDataArrayList);
        gridThongke.setAdapter(thongKeAdapter);
    }
}