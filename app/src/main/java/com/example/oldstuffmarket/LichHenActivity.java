package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.example.oldstuffmarket.adapter.AppointmentAdapter;
import com.example.oldstuffmarket.data_models.Appointment;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LichHenActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private ArrayList<Appointment> appointmentArrayList;
    private String userName, userID;
    private Button btnBack;
    private GridView gridCuocHen;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.lich_hen_layout);

        gridCuocHen = (GridView) findViewById(R.id.gridCuocHen);
        btnBack = (Button) findViewById(R.id.btnBack);

        appointmentArrayList = new ArrayList<>();

        btnBack.setOnClickListener(backClick);

        gridCuocHen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(userID.equals(appointmentArrayList.get(position).getNguoiHenID())){
                    DialogInterface.OnClickListener dialogClick = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    databaseReference.child("Appointment").child(appointmentArrayList.get(position).getLichHenID()).removeValue();

                                    appointmentArrayList.clear();
                                    appointmentLoad();
                                    databaseReference.child("Appointment").addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            if(snapshot.getValue(Appointment.class).getNguoiDuocHenID().equals(userID) || snapshot.getValue(Appointment.class).getNguoiHenID().equals(userID)){
                                                appointmentArrayList.add(snapshot.getValue(Appointment.class));
                                            }
                                            appointmentLoad();
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
                                    break;
                                case DialogInterface.BUTTON_NEGATIVE:
                                    return;

                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Bạn muốn xóa cuộc hẹn?").setNegativeButton("No",dialogClick).setPositiveButton("Yes",dialogClick).show();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        appointmentArrayList.clear();
        if(getIntent().getExtras() != null){
            userID = getIntent().getExtras().getString("UserID");
            userName = getIntent().getExtras().getString("UserName");

            databaseReference.child("Appointment").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(Appointment.class).getNguoiDuocHenID().equals(userID) || snapshot.getValue(Appointment.class).getNguoiHenID().equals(userID)){
                        appointmentArrayList.add(snapshot.getValue(Appointment.class));
                    }
                    appointmentLoad();
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

    public void appointmentLoad(){
        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(LichHenActivity.this, R.layout.lich_hen_adapter_layout, appointmentArrayList, userID);
        gridCuocHen.setAdapter(appointmentAdapter);
    }

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
            intent = new Intent(v.getContext(), UserMainActivity.class);
            intent.putExtra("UserName", userName);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
    };

}