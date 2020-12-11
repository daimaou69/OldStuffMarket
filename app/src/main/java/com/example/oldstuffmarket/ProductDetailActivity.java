package com.example.oldstuffmarket;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.oldstuffmarket.adapter.CommentAdapter;
import com.example.oldstuffmarket.data_models.Comment;
import com.example.oldstuffmarket.data_models.SanPham;
import com.example.oldstuffmarket.data_models.ShopData;
import com.example.oldstuffmarket.data_models.UserData;
import com.example.oldstuffmarket.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class ProductDetailActivity extends AppCompatActivity {

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Intent intent;
    private Button btnDecs, btnInc, btnBuy, btnXemTrang, btnBack, btnAllStar, btnOneStar, btnTwoStar, btnThreeStar, btnFourStar, btnFiveStar;
    private EditText edtQuantity;
    private TextView txtSoLuongSP, txtTenSP, txtGiaSP, txtTenShop, txtMoTaSP, txtNgayDang, txtDiaChiDang;
    private GridView gridComment;
    private ImageView imgSP, imgShop;
    private int soLuongSP;
    private String sanPhamID;
    private ArrayList<ShopData> shopDataArrayList;
    private ArrayList<Comment> commentArrayList;
    private CommentAdapter commentAdapter;
    private String sUserName = UserMainActivity.sUserName;
    private String userID = UserMainActivity.sUserID;
    private String shopID, sanPhamUserID, navigateTo, sellerSanPhamID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.product_detail_layout);

        btnDecs = (Button) findViewById(R.id.btnDecs);
        btnInc = (Button) findViewById(R.id.btnInc);
        btnBuy = (Button) findViewById(R.id.btnBuy);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnXemTrang = (Button) findViewById(R.id.btnXemTrang);
        btnAllStar = (Button) findViewById(R.id.btnAllStar);
        btnOneStar = (Button) findViewById(R.id.btnOneStar);
        btnTwoStar = (Button) findViewById(R.id.btnTwoStar);
        btnThreeStar = (Button) findViewById(R.id.btnThreeStar);
        btnFourStar = (Button) findViewById(R.id.btnFourStar);
        btnFiveStar = (Button) findViewById(R.id.btnFiveStar);
        edtQuantity = (EditText) findViewById(R.id.edtQuantity);
        txtSoLuongSP = (TextView) findViewById(R.id.txtSoLuongSP);
        txtTenSP = (TextView) findViewById(R.id.txtTenSP);
        txtGiaSP = (TextView) findViewById(R.id.txtGiaSP);
        txtTenShop = (TextView) findViewById(R.id.txtTenShop);
        txtMoTaSP = (TextView) findViewById(R.id.txtMoTaSP);
        txtNgayDang = (TextView) findViewById(R.id.txtNgayDang);
        txtDiaChiDang = (TextView) findViewById(R.id.txtDiaChiDang);
        imgSP = (ImageView) findViewById(R.id.imgSP);
        imgShop = (ImageView) findViewById(R.id.imgShop);
        gridComment = (GridView) findViewById(R.id.gridComment);

        shopDataArrayList = new ArrayList<>();
        commentArrayList = new ArrayList<>();

//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        Date date = new Date();

        btnInc.setOnClickListener(quanIncClick);
        btnDecs.setOnClickListener(quanDecsClick);
        btnBuy.setOnClickListener(buyClick);
        btnBack.setOnClickListener(backClick);
        btnXemTrang.setOnClickListener(xemTrangClick);
        btnAllStar.setOnClickListener(allStarClick);
        btnOneStar.setOnClickListener(oneStarClick);
        btnTwoStar.setOnClickListener(twoStarClick);
        btnThreeStar.setOnClickListener(threeStarClick);
        btnFourStar.setOnClickListener(fourStarClick);
        btnFiveStar.setOnClickListener(fiveStarClick);
    }

    @Override
    protected void onResume() {
        super.onResume();

        shopDataArrayList.clear();
        commentArrayList.clear();

        databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                shopDataArrayList.add(snapshot.getValue(ShopData.class));
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
            sanPhamID = getIntent().getExtras().getString("ProductID");
//            soLuongSP = getIntent().getExtras().getInt("SoLuongSP");
            navigateTo = getIntent().getExtras().getString("NavigateTo");
            sellerSanPhamID = getIntent().getExtras().getString("SanPhamID");

            if(!navigateTo.equals("Home")){
                btnXemTrang.setEnabled(false);
            }

            databaseReference.child("SanPham").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(SanPham.class).getsID().equals(sanPhamID)){
                        soLuongSP = snapshot.getValue(SanPham.class).getiSoLuong();
                        if(snapshot.getValue(SanPham.class).getiSoLuong() == 0){ ;
                            edtQuantity.setText("Hết hàng!");
                            edtQuantity.setEnabled(false);
                            btnBuy.setEnabled(false);
                        }

                        txtSoLuongSP.setText("Số lượng: " + String.valueOf(snapshot.getValue(SanPham.class).getiSoLuong()));

                        storageReference.child(snapshot.getValue(SanPham.class).getsSPImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(ProductDetailActivity.this).load(uri).into(imgSP);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(ProductDetailActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        if(snapshot.getValue(SanPham.class).getsUserID().equals(userID)){
                            btnBuy.setEnabled(false);
                            btnXemTrang.setEnabled(false);
                        }

                        txtMoTaSP.setText(snapshot.getValue(SanPham.class).getsMoTa());
                        txtGiaSP.setText(String.valueOf(snapshot.getValue(SanPham.class).getlGiaTien()) + "vnđ");
                        txtNgayDang.setText("Ngày đăng: " + snapshot.getValue(SanPham.class).getsNgayDang());
                        txtDiaChiDang.setText("Điểm bán: " + snapshot.getValue(SanPham.class).getsDiaChiDang());

                        if(snapshot.getValue(SanPham.class).getiTinhTrang() == 0){
                            txtTenSP.setText(snapshot.getValue(SanPham.class).getsTenSP() + " - New");
                        }
                        else if(snapshot.getValue(SanPham.class).getiTinhTrang() == 1){
                            txtTenSP.setText(snapshot.getValue(SanPham.class).getsTenSP() + " - 2nd");
                        }

                        sanPhamUserID = snapshot.getValue(SanPham.class).getsUserID();
                        shopID = snapshot.getValue(SanPham.class).getsShopID();

                        if(snapshot.getValue(SanPham.class).getsShopID().isEmpty() || shopActiveCheck(shopDataArrayList, shopID) == 3){
                            databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(UserData.class).getsUserID().equals(sanPhamUserID)){
                                        storageReference.child(snapshot.getValue(UserData.class).getsImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Glide.with(ProductDetailActivity.this).load(uri).into(imgShop);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
//                                                Toast.makeText(ProductDetailActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        txtTenShop.setText(snapshot.getValue(UserData.class).getsFullName());
                                        if(snapshot.getValue(UserData.class).getiTinhTrang() != 0){
                                            btnBuy.setEnabled(false);
                                            btnXemTrang.setEnabled(false);
                                            btnDecs.setEnabled(false);
                                            btnInc.setEnabled(false);
                                            txtTenShop.setText(snapshot.getValue(UserData.class).getsFullName() + "(Người bán đang bị khóa)");
                                        }
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
                        else if(!snapshot.getValue(SanPham.class).getsShopID().isEmpty() && shopActiveCheck(shopDataArrayList, shopID) == 1){
                            databaseReference.child("Shop").addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    if(snapshot.getValue(ShopData.class).getUserID().equals(sanPhamUserID)){
                                        storageReference.child(snapshot.getValue(ShopData.class).getShopImage() + ".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                Glide.with(ProductDetailActivity.this).load(uri).into(imgShop);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ProductDetailActivity.this, "Hinh anh khong ton tai!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        String shopName = snapshot.getValue(ShopData.class).getShopName();
                                        txtTenShop.setText(shopName);
                                        databaseReference.child("User").addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                                if(snapshot.getValue(UserData.class).getsUserID().equals(sanPhamUserID)){
                                                    if(snapshot.getValue(UserData.class).getiTinhTrang() != 0){
                                                        btnBuy.setEnabled(false);
                                                        btnXemTrang.setEnabled(false);
                                                        btnDecs.setEnabled(false);
                                                        btnInc.setEnabled(false);
                                                        txtTenShop.setText(shopName + "(Người bán đang bị khóa)");
                                                    }
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

            databaseReference.child("ProductReview").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(Comment.class).getSanPhamID().equals(sanPhamID)){
                        commentArrayList.add(snapshot.getValue(Comment.class));
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

            Handler handler = new Handler();
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    commentLoad();
                }
            }, delay);
        }
    }

    View.OnClickListener fiveStarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            commentArrayList.clear();
            databaseReference.child("ProductReview").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(Comment.class).getSanPhamID().equals(sanPhamID) && snapshot.getValue(Comment.class).getSoSao() == 5){
                        commentArrayList.add(snapshot.getValue(Comment.class));
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

            Handler handler = new Handler();
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    commentLoad();
                }
            }, delay);
        }
    };

    View.OnClickListener fourStarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            commentArrayList.clear();
            databaseReference.child("ProductReview").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(Comment.class).getSanPhamID().equals(sanPhamID) && snapshot.getValue(Comment.class).getSoSao() == 4){
                        commentArrayList.add(snapshot.getValue(Comment.class));
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

            Handler handler = new Handler();
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    commentLoad();
                }
            }, delay);
        }
    };

    View.OnClickListener threeStarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            commentArrayList.clear();
            databaseReference.child("ProductReview").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(Comment.class).getSanPhamID().equals(sanPhamID) && snapshot.getValue(Comment.class).getSoSao() == 3){
                        commentArrayList.add(snapshot.getValue(Comment.class));
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

            Handler handler = new Handler();
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    commentLoad();
                }
            }, delay);
        }
    };

    View.OnClickListener twoStarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            commentArrayList.clear();
            databaseReference.child("ProductReview").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(Comment.class).getSanPhamID().equals(sanPhamID) && snapshot.getValue(Comment.class).getSoSao() == 2){
                        commentArrayList.add(snapshot.getValue(Comment.class));
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

            Handler handler = new Handler();
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    commentLoad();
                }
            }, delay);
        }
    };

    View.OnClickListener oneStarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            commentArrayList.clear();
            databaseReference.child("ProductReview").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(Comment.class).getSanPhamID().equals(sanPhamID) && snapshot.getValue(Comment.class).getSoSao() == 1){
                        commentArrayList.add(snapshot.getValue(Comment.class));
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

            Handler handler = new Handler();
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    commentLoad();
                }
            }, delay);
        }
    };

    View.OnClickListener allStarClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            commentArrayList.clear();
            databaseReference.child("ProductReview").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.getValue(Comment.class).getSanPhamID().equals(sanPhamID)){
                        commentArrayList.add(snapshot.getValue(Comment.class));
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

            Handler handler = new Handler();
            int delay = 1000;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    commentLoad();
                }
            }, delay);
        }
    };

    public void commentLoad(){
        commentAdapter = new CommentAdapter(ProductDetailActivity.this, R.layout.comment_adapter_layout, commentArrayList);
        gridComment.setAdapter(commentAdapter);
    }

    View.OnClickListener xemTrangClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(shopID.isEmpty()){
                intent = new Intent(v.getContext(), SellerMainActivity.class);
                intent.putExtra("UserID", sanPhamUserID);
                intent.putExtra("UserName", sUserName);
                intent.putExtra("ProductID", sanPhamID);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
            else {
                intent = new Intent(v.getContext(), ShopMainActivity.class);
                intent.putExtra("UserID", sanPhamUserID);
                intent.putExtra("UserName", sUserName);
                intent.putExtra("ProductID", sanPhamID);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        }
    };

    public int shopActiveCheck(ArrayList<ShopData> shopDataArrayList, String shopID){
        for(ShopData shopData : shopDataArrayList){
            if(shopData.getShopID().equals(shopID)){
                return shopData.getTinhTrangShop();
            }
        }
        return -1;
    }

    View.OnClickListener buyClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(shopActiveCheck(shopDataArrayList, shopID) == 2){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Người bán đang tạm nghỉ!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(Integer.valueOf(edtQuantity.getText().toString()) <= 0){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Bạn chưa chọn số lượng sản phẩm!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else if(Integer.valueOf(edtQuantity.getText().toString()) > soLuongSP){
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setMessage("Số lượng sản phẩm không hợp lệ!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
            else{
                intent = new Intent(v.getContext(), BuyActivity.class);
                intent.putExtra("UserName", sUserName);
                intent.putExtra("SoLuongSP", edtQuantity.getText().toString());
                intent.putExtra("SanPhamID", sanPhamID);
                intent.putExtra("UserID", userID);
                intent.putExtra("SellerID", sanPhamUserID);
                intent.putExtra("NavigateTo", navigateTo);
                startActivity(intent);
            }
        }
    };

    View.OnClickListener backClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(navigateTo.equals("Home")){
                finish();
                intent = new Intent(ProductDetailActivity.this, UserMainActivity.class);
                intent.putExtra("UserName", sUserName);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
            else if(navigateTo.equals("Seller")){
                finish();
                intent = new Intent(ProductDetailActivity.this, SellerMainActivity.class);
                intent.putExtra("UserName", sUserName);
                intent.putExtra("UserID", sanPhamUserID);
                intent.putExtra("ProductID", sellerSanPhamID);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
            else if(navigateTo.equals("Shop")){
                finish();
                intent = new Intent(ProductDetailActivity.this, ShopMainActivity.class);
                intent.putExtra("UserName", sUserName);
                intent.putExtra("UserID", sanPhamUserID);
                intent.putExtra("ProductID", sellerSanPhamID);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        }
    };

    View.OnClickListener quanDecsClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(soLuongSP > 0){
                if(Integer.valueOf(edtQuantity.getText().toString()) > 1){
                    int newNum = Integer.valueOf(edtQuantity.getText().toString()) - 1;
                    edtQuantity.setText(String.valueOf(newNum));
                }
            }
        }
    };

    View.OnClickListener quanIncClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(soLuongSP > 0){
                if(Integer.valueOf(edtQuantity.getText().toString()) < soLuongSP){
                    int newNum = Integer.valueOf(edtQuantity.getText().toString()) + 1;
                    edtQuantity.setText(String.valueOf(newNum));
                }
            }
        }
    };
}