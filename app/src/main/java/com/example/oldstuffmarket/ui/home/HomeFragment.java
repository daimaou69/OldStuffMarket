package com.example.oldstuffmarket.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TabWidget;
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
import com.example.oldstuffmarket.data_models.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeFragment extends Fragment{

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private AutoCompleteTextView edtFind;
    private GridView gridDanhMuc, gridSP;
    private ArrayList<DanhMucData> danhMucDataArrayList;
    private ArrayList<SanPham> sanPhamArrayList;
    private Intent intent;
    private Spinner spnSort;
    private SanPhamAdapter sanPhamAdapter;
    private ArrayList<String> findArr;
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

        edtFind = (AutoCompleteTextView) view.findViewById(R.id.edtFind);
        gridDanhMuc = (GridView) view.findViewById(R.id.gridDanhMuc);
        gridSP = (GridView) view.findViewById(R.id.gridSP);
        spnSort = (Spinner) view.findViewById(R.id.spnSort);
//        btnGiaTang = (Button) view.findViewById(R.id.btnGiaTang);
//        btnGiaGiam = (Button) view.findViewById(R.id.btnGiaGiam);
//        btnDoCu = (Button) view.findViewById(R.id.btnDoCu);
//        btnDoMoi = (Button) view.findViewById(R.id.btnDoMoi);


        findArr = new ArrayList<>();
        danhMucDataArrayList = new ArrayList<>();
        DanhMucData danhMucData = new DanhMucData("AllID","Tất cả","danh_muc_khac");
        danhMucDataArrayList.add(danhMucData);
        databaseReference.child("DanhMuc").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                danhMucDataArrayList.add(snapshot.getValue(DanhMucData.class));
                danhMucLoad(view);
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
//                sanPhamArrayList.add(snapshot.getValue(SanPham.class));
                findArr.add(snapshot.getValue(SanPham.class).getsTenSP());
//                sanPhamLoad(view);
                findLoad(view);
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
                    sanPhamArrayList.clear();
                    databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getValue(SanPham.class).getsDanhMuc().equals(danhMucDataArrayList.get(position).getsTenDanhMuc())){
                                sanPhamArrayList.add(snapshot.getValue(SanPham.class));
                            }
                            sanPhamLoad(view);
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
                else{
                    sanPhamArrayList.clear();
                    databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            sanPhamArrayList.add(snapshot.getValue(SanPham.class));
                            sanPhamLoad(view);
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
        });

//        btnGiaTang.setOnClickListener(giaTangClick);
//        btnGiaGiam.setOnClickListener(giaGiamClick);
//        btnDoCu.setOnClickListener(doCuClick);
//        btnDoMoi.setOnClickListener(doMoiClick);

        spnSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch(position){
                    case 0:
                        sanPhamArrayList.clear();
                        databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                sanPhamArrayList.add(snapshot.getValue(SanPham.class));
                                sanPhamLoad(view);
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
                    case 1:
                        Collections.sort(sanPhamArrayList, new Comparator<SanPham>() {
                            @Override
                            public int compare(SanPham sp1, SanPham sp2) {
                                if(sp1.getlGiaTien() == sp2.getlGiaTien()){
                                    return 0;
                                }
                                else if(sp1.getlGiaTien() > sp2.getlGiaTien()){
                                    return 1;
                                }
                                else{
                                    return -1;
                                }
                            }
                        });

                        sanPhamAdapter = new SanPhamAdapter(view.getContext(), R.layout.san_pham_adapter_layout, sanPhamArrayList);
                        gridSP.setAdapter(sanPhamAdapter);
                        break;
                    case 2:
                        Collections.sort(sanPhamArrayList, new Comparator<SanPham>() {
                            @Override
                            public int compare(SanPham sp1, SanPham sp2) {
                                if(sp1.getlGiaTien() == sp2.getlGiaTien()){
                                    return 0;
                                }
                                else if(sp1.getlGiaTien() < sp2.getlGiaTien()){
                                    return 1;
                                }
                                else{
                                    return -1;
                                }
                            }
                        });

                        sanPhamAdapter = new SanPhamAdapter(view.getContext(), R.layout.san_pham_adapter_layout, sanPhamArrayList);
                        gridSP.setAdapter(sanPhamAdapter);
                        break;
                    case 3:
                        Collections.sort(sanPhamArrayList, new Comparator<SanPham>() {
                            @Override
                            public int compare(SanPham sp1, SanPham sp2) {
                                if(sp1.getiTinhTrang() == sp2.getiTinhTrang()){
                                    return 0;
                                }
                                else if(sp1.getiTinhTrang() < sp2.getiTinhTrang()){
                                    return 1;
                                }
                                else{
                                    return -1;
                                }
                            }
                        });
                        sanPhamAdapter = new SanPhamAdapter(view.getContext(), R.layout.san_pham_adapter_layout, sanPhamArrayList);
                        gridSP.setAdapter(sanPhamAdapter);
                        break;
                    case 4:
                        Collections.sort(sanPhamArrayList, new Comparator<SanPham>() {
                            @Override
                            public int compare(SanPham sp1, SanPham sp2) {
                                if(sp1.getiTinhTrang() == sp2.getiTinhTrang()){
                                    return 0;
                                }
                                else if(sp1.getiTinhTrang() > sp2.getiTinhTrang()){
                                    return 1;
                                }
                                else{
                                    return -1;
                                }
                            }
                        });

                        sanPhamAdapter = new SanPhamAdapter(view.getContext(), R.layout.san_pham_adapter_layout, sanPhamArrayList);
                        gridSP.setAdapter(sanPhamAdapter);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        edtFind.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    sanPhamArrayList.clear();
                    databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            if(snapshot.getValue(SanPham.class).getsTenSP().toLowerCase().contains(edtFind.getText().toString().toLowerCase())){
                                sanPhamArrayList.add(snapshot.getValue(SanPham.class));
                            }
                            sanPhamLoad(view);
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
                else{
                    sanPhamArrayList.clear();
                    databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            sanPhamArrayList.add(snapshot.getValue(SanPham.class));
                            sanPhamLoad(view);
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

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    View.OnClickListener doMoiClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Collections.sort(sanPhamArrayList, new Comparator<SanPham>() {
                @Override
                public int compare(SanPham sp1, SanPham sp2) {
                    if(sp1.getiTinhTrang() == sp2.getiTinhTrang()){
                        return 0;
                    }
                    else if(sp1.getiTinhTrang() > sp2.getiTinhTrang()){
                        return 1;
                    }
                    else{
                        return -1;
                    }
                }
            });

            SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(v.getContext(), R.layout.san_pham_adapter_layout, sanPhamArrayList);
            gridSP.setAdapter(sanPhamAdapter);
        }
    };

    View.OnClickListener doCuClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Collections.sort(sanPhamArrayList, new Comparator<SanPham>() {
                @Override
                public int compare(SanPham sp1, SanPham sp2) {
                    if(sp1.getiTinhTrang() == sp2.getiTinhTrang()){
                        return 0;
                    }
                    else if(sp1.getiTinhTrang() < sp2.getiTinhTrang()){
                        return 1;
                    }
                    else{
                        return -1;
                    }
                }
            });

            SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(v.getContext(), R.layout.san_pham_adapter_layout, sanPhamArrayList);
            gridSP.setAdapter(sanPhamAdapter);
        }
    };

    View.OnClickListener giaGiamClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Collections.sort(sanPhamArrayList, new Comparator<SanPham>() {
                @Override
                public int compare(SanPham sp1, SanPham sp2) {
                    if(sp1.getlGiaTien() == sp2.getlGiaTien()){
                        return 0;
                    }
                    else if(sp1.getlGiaTien() < sp2.getlGiaTien()){
                        return 1;
                    }
                    else{
                        return -1;
                    }
                }
            });

            SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(v.getContext(), R.layout.san_pham_adapter_layout, sanPhamArrayList);
            gridSP.setAdapter(sanPhamAdapter);
        }
    };

    View.OnClickListener giaTangClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Collections.sort(sanPhamArrayList, new Comparator<SanPham>() {
                @Override
                public int compare(SanPham sp1, SanPham sp2) {
                    if(sp1.getlGiaTien() == sp2.getlGiaTien()){
                        return 0;
                    }
                    else if(sp1.getlGiaTien() > sp2.getlGiaTien()){
                        return 1;
                    }
                    else{
                        return -1;
                    }
                }
            });

            SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(v.getContext(), R.layout.san_pham_adapter_layout, sanPhamArrayList);
            gridSP.setAdapter(sanPhamAdapter);
        }
    };

    public void findLoad(View view){
        ArrayAdapter<String> autoAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, findArr);
        edtFind.setAdapter(autoAdapter);
    }

    public void danhMucLoad(View view){
        DanhMucAdapter danhMucAdapter = new DanhMucAdapter(view.getContext(),R.layout.danh_muc_adapter_layout,danhMucDataArrayList);
        gridDanhMuc.setAdapter(danhMucAdapter);
    }

    public void sanPhamLoad(View view){
        Collections.reverse(sanPhamArrayList);
        SanPhamAdapter sanPhamAdapter = new SanPhamAdapter(view.getContext(), R.layout.san_pham_adapter_layout, sanPhamArrayList);
        gridSP.setAdapter(sanPhamAdapter);
    }

}