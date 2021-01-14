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

import com.example.oldstuffmarket.adapter.NotificationAdapter;
import com.example.oldstuffmarket.adapter.Userbibaocao_Adapter;
import com.example.oldstuffmarket.data_models.RemoveProductData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RemoveProductActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private GridView gridNotification;
    private Button btnBack;
    private String userID, userName;
    private ArrayList<RemoveProductData> removeProductDataArrayList;
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_remove_product);

        gridNotification = (GridView) findViewById(R.id.gridNotification);
        btnBack = (Button) findViewById(R.id.btnBack);

        removeProductDataArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().getExtras() != null) {
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");
            removeProductDataArrayList.clear();
            databaseReference.child("DeletedProduct").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    removeProductDataArrayList.add(snapshot.getValue(RemoveProductData.class));
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
            handler.postDelayed(new Runnable() {
                public void run() {
                    userLoad();
                }
            }, delay);
        }
    }

    public void userLoad() {
        notificationAdapter = new NotificationAdapter(RemoveProductActivity.this, R.layout.notification_adapter, removeProductDataArrayList);
        gridNotification.setAdapter(notificationAdapter);
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
}