package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class UserCommentNeedsActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private GridView gridCommentNeeds;
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
        setContentView(R.layout.user_comment_needs_layout);

        orderDataArrayList = new ArrayList<>();

        btnBack = (Button) findViewById(R.id.btnBack);
        gridCommentNeeds = (GridView) findViewById(R.id.gridCommentNeeds);

        btnBack.setOnClickListener(backClick);
        gridCommentNeeds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                intent = new Intent(view.getContext(), CommentActivity.class);
                                intent.putExtra("UserID", userID);
                                intent.putExtra("SellerID", orderDataArrayList.get(position).getNguoiBanID());
                                intent.putExtra("UserName", userName);
                                intent.putExtra("ProductID", orderDataArrayList.get(position).getSanPham().getsID());
                                intent.putExtra("Navigate", "User");
                                intent.putExtra("DonHangID", orderDataArrayList.get(position).getDonHangID());
                                startActivity(intent);
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                return;
                        }
                    }
                };

                AlertDialog.Builder alert = new AlertDialog.Builder(UserCommentNeedsActivity.this);
                alert.setMessage("Tiến hành comment cho sản phẩm?").setPositiveButton("Yes", dialog).setNegativeButton("No", dialog).show();

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
            databaseReference.child("CommentNeeds").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(OrderData.class).getNguoiMuaID().equals(userID)){
                        orderDataArrayList.add(snapshot.getValue(OrderData.class));
                    }
                    donMuaLoad();
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
            Intent intent = new Intent(v.getContext(), UserMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

    public void donMuaLoad(){
        donMuaAdapter = new DonMuaAdapter(UserCommentNeedsActivity.this, R.layout.don_mua_adapter_layout, orderDataArrayList);
        gridCommentNeeds.setAdapter(donMuaAdapter);
    }
}