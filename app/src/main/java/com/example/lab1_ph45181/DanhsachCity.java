package com.example.lab1_ph45181;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DanhsachCity extends AppCompatActivity {

    Button bt,btok,bthuy;

    FirebaseFirestore db;
    EditText edimg,edcity,edcountry,edpeople;
   ProgressDialog progressDialog;

    String Tag="add";
    Dialog dialog;
    RecyclerView recyclerView;
    ArrayList<City> cityArrayList;
    CityAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_danhsach_city);
        bt = findViewById(R.id.button9);
        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recy);
        cityArrayList=new ArrayList<>();
        progressDialog=new ProgressDialog(DanhsachCity.this);
        progressDialog.setMessage("Đang tải dữ liệu...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        dialog = new Dialog(DanhsachCity.this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView.setLayoutManager(new GridLayoutManager(DanhsachCity.this, 1));
        showdata();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.setContentView(R.layout.add_data);

                edimg = dialog.findViewById(R.id.editTextText7);
                edcity = dialog.findViewById(R.id.editTextText8);
                edcountry = dialog.findViewById(R.id.editTextText9);
                edpeople = dialog.findViewById(R.id.editTextText10);
                btok = dialog.findViewById(R.id.button10);
                bthuy = dialog.findViewById(R.id.button11);

                btok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(edcountry.getText().length()==0||edcity.getText().length()==0||edpeople.getText().length()==0||edimg.getText().length()==0){
                            Toast.makeText(DanhsachCity.this, "Dữ liệu ko được để trống", Toast.LENGTH_SHORT).show();
                        }else{
                            if(TextUtils.isDigitsOnly(edpeople.getText())){
                                City cityy=new City();
                                cityy.country = edcountry.getText().toString().trim();
                                cityy.city = edcity.getText().toString().trim();
                                cityy.img = edimg.getText().toString().trim();
                                cityy.people = Integer.parseInt(edpeople.getText().toString().trim());
                                Add(cityy);
                            }else{
                                Toast.makeText(DanhsachCity.this, "Dân số phải là định dạng int", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });


                dialog.show();
            }
        });
    }

    public void showdata(){
        String TAG="showdata";
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                City city1=new City(document.getString("id"),
                                document.getString("img"),
                                document.getString("city"),
                                document.getString("country"),
                                        document.getLong("people").intValue());
                                cityArrayList.add(city1);

                            }
                            cityAdapter=new CityAdapter(DanhsachCity.this,cityArrayList);
                            recyclerView.setAdapter(cityAdapter);
                            progressDialog.dismiss();

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    public void Add(City city1){
    //random id
        String id= UUID.randomUUID().toString();
        Map<String, Object> user = new HashMap<>();

        user.put("id",id);
        user.put("img", city1.img);
        user.put("city", city1.city);
        user.put("people", city1.people);
        user.put("country", city1.country);

        progressDialog.show();
        db.collection("users")
                .document(id).set(user).
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DanhsachCity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        cityArrayList.clear();
                        showdata();
                        dialog.dismiss();
                        progressDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(Tag, "Error adding document", e);
                        Toast.makeText(DanhsachCity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}