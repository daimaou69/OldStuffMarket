package com.example.oldstuffmarket.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.oldstuffmarket.ProductDetailActivity;
import com.example.oldstuffmarket.R;
import com.example.oldstuffmarket.UserMainActivity;
import com.example.oldstuffmarket.adapter.DanhMucAdapter;
import com.example.oldstuffmarket.adapter.SanPhamAdapter;
import com.example.oldstuffmarket.data_models.DanhMucData;
import com.example.oldstuffmarket.data_models.SanPham;
import com.example.oldstuffmarket.data_models.ShopData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private EditText edtFind;
    private GridView gridDanhMuc, gridSP;
    private ArrayList<DanhMucData> danhMucDataArrayList;
    private ArrayList<SanPham> sanPhamArrayList;
    private Intent intent;
//    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
////                textView.setText(s);
//            }
//        });

        edtFind = (EditText) view.findViewById(R.id.edtFind);
        gridDanhMuc = (GridView) view.findViewById(R.id.gridDanhMuc);
        gridSP = (GridView) view.findViewById(R.id.gridSP);

        danhMucDataArrayList = new ArrayList<>();
        DanhMucData danhMucData = new DanhMucData("AllID","Tất cả","danh_muc_khac");
        danhMucDataArrayList.add(danhMucData);
        databaseReference.child("DanhMuc").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                danhMucDataArrayList.add(snapshot.getValue(DanhMucData.class));
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

        sanPhamArrayList = new ArrayList<>();
        databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                sanPhamArrayList.add(snapshot.getValue(SanPham.class));
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

                danhMucLoad(view);
                sanPhamLoad(view);
//                handler.postDelayed(this, delay);
            }
        }, delay);

        gridSP.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent(view.getContext(), ProductDetailActivity.class);
                intent.putExtra("ProductID", sanPhamArrayList.get(position).getsID());
                intent.putExtra("SoLuongSP", sanPhamArrayList.get(position).getiSoLuong());
                intent.putExtra("NavigateTo", "Home");
                startActivity(intent);
            }
        });

        gridDanhMuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!danhMucDataArrayList.get(position).getsDanhMucID().equals("AllID")){
                    ArrayList<SanPham> findSP = new ArrayList<>();
                    for(SanPham sanPham : sanPhamArrayList){
                        if(sanPham.getsDanhMuc().equals(danhMucDataArrayList.get(position).getsTenDanhMuc())){
                            findSP.add(sanPham);
                        }
                    }
                    final Handler handler = new Handler();
                    final int delay = 1000; //milliseconds
                    handler.postDelayed(new Runnable(){
                        public void run(){
                            SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(view.getContext(), R.layout.san_pham_adapter_layout, findSP);
                            gridSP.setAdapter(sanPhamAdapter);
//                handler.postDelayed(this, delay);
                        }
                    }, delay);
                }
                else{
                    sanPhamArrayList.clear();
                    databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            sanPhamArrayList.add(snapshot.getValue(SanPham.class));
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
                            sanPhamLoad(view);
//                handler.postDelayed(this, delay);
                        }
                    }, delay);
                }
            }
        });

        edtFind.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        // the user is done typing.
                        if(!edtFind.getText().toString().isEmpty()){
                            ArrayList<SanPham> findSP = new ArrayList<>();
                            for(SanPham sanPham : sanPhamArrayList){
                                if(sanPham.getsTenSP().contains(edtFind.getText().toString()) || sanPham.getsDanhMuc().contains(edtFind.getText().toString())){
                                    findSP.add(sanPham);
                                }
                            }
                            final Handler handler = new Handler();
                            final int delay = 1000; //milliseconds
                            handler.postDelayed(new Runnable(){
                                public void run(){
                                    SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(v.getContext(), R.layout.san_pham_adapter_layout, findSP);
                                    gridSP.setAdapter(sanPhamAdapter);
//                handler.postDelayed(this, delay);
                                }
                            }, delay);
                        }
                        else{
                            sanPhamArrayList.clear();
                            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    sanPhamArrayList.add(snapshot.getValue(SanPham.class));
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
                                    sanPhamLoad(v);
//                handler.postDelayed(this, delay);
                                }
                            }, delay);
                        }
                        return true; // consume.
                    }
                }
                return false;
            }
        });

        return view;
    }


    public void danhMucLoad(View view){
        DanhMucAdapter danhMucAdapter = new DanhMucAdapter(view.getContext(),R.layout.danh_muc_adapter_layout,danhMucDataArrayList);
        gridDanhMuc.setAdapter(danhMucAdapter);
    }

    public void sanPhamLoad(View view){
        SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(view.getContext(), R.layout.san_pham_adapter_layout, sanPhamArrayList);
        gridSP.setAdapter(sanPhamAdapter);
    }

}