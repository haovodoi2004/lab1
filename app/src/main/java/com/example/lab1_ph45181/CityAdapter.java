package com.example.lab1_ph45181;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.city>{
    Context context;
    ArrayList<City> cityArrayList;
    EditText edimg,edcity,edcountry,edpeople;
    Button bt1,bt2;
    ProgressDialog progressDialog;

    public CityAdapter(Context context, ArrayList<City> cityArrayList) {
        this.context = context;
        this.cityArrayList = cityArrayList;
    }

    @NonNull
    @Override
    public city onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_city,null);
        return new city(view);
    }

    @Override
    public void onBindViewHolder(@NonNull city holder, int position) {

        holder.city.setText("Tên thành phố: "+cityArrayList.get(position).city);
        holder.people.setText("Dân số: "+ cityArrayList.get(position).people+"");
        holder.country.setText("Quốc gia: "+cityArrayList.get(position).country);
        Picasso.get().load(cityArrayList.get(position).img).into(holder.view);
        holder.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=new ProgressDialog(v.getContext());
                progressDialog.setMessage("Đang xóa dữ liệu...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
                builder.setIcon(R.drawable.notification);
                builder.setTitle("Thông báo");
                builder.setMessage("bạn có muốn xóa thành phố "+cityArrayList.get(position).city);

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                });

                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String TAG="delete";
                        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                        firestore.collection("users").document(cityArrayList.get(position).id)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                        Toast.makeText(context, "Đã xóa thành công thành phố: "+cityArrayList.get(position).city, Toast.LENGTH_LONG).show();
                                        cityArrayList.remove(position);
                                        progressDialog.dismiss();
                                        notifyDataSetChanged();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error deleting document", e);
                                    }
                                });
                    }
                });
                 builder.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(v.getContext());
                dialog.setContentView(R.layout.update_data);
                edimg=dialog.findViewById(R.id.editTextText7);
                edcity=dialog.findViewById(R.id.editTextText8);
                edcountry=dialog.findViewById(R.id.editTextText9);
                edpeople=dialog.findViewById(R.id.editTextText10);
                bt1=dialog.findViewById(R.id.button10);
                bt2=dialog.findViewById(R.id.button11);

                edimg.setText(cityArrayList.get(position).img);
                edpeople.setText(cityArrayList.get(position).people+"");
                edcountry.setText(cityArrayList.get(position).country);
                edcity.setText(cityArrayList.get(position).city);
                bt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                bt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog=new ProgressDialog(v.getContext());
                        progressDialog.setMessage("Đang sửa dữ liệu...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        if(edcity.getText().length()==0||edpeople.getText().length()==0||edimg.getText().length()==0||edcountry.getText().length()==0){
                            Toast.makeText(v.getContext(), "Dữ liệu ko được để trống", Toast.LENGTH_SHORT).show();
                        }else{
                            if(TextUtils.isDigitsOnly(edpeople.getText().toString())){
                                FirebaseFirestore firestore=FirebaseFirestore.getInstance();
                                firestore.collection("users").document(cityArrayList.get(position).id)
                                        .update("city",edcity.getText().toString().trim(),
                                                "country",edcountry.getText().toString().trim(),
                                                "img",edimg.getText().toString().trim(),
                                                "people",Integer.parseInt(edpeople.getText().toString().trim()))
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(v.getContext(), "Đã Sửa thành công", Toast.LENGTH_SHORT).show();
                                                cityArrayList.get(position).setCity(edcity.getText().toString().trim());
                                                cityArrayList.get(position).setCountry(edcountry.getText().toString().trim());
                                                cityArrayList.get(position).setImg(edimg.getText().toString().trim());
                                                cityArrayList.get(position).setPeople(Integer.parseInt(edpeople.getText().toString().trim()));
                                                notifyItemChanged(position);
                                                progressDialog.dismiss();
                                                dialog.dismiss();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                dialog.dismiss();
                                            }
                                        });
                            }else{
                                Toast.makeText(v.getContext(), "Dân số phải là kiểu int", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cityArrayList.size();
    }

    class city extends RecyclerView.ViewHolder {
        TextView city,country,people;
        Button bt;
        ImageView view;
        public city(@NonNull View itemView) {
            super(itemView);
            city=itemView.findViewById(R.id.textView16);
            country=itemView.findViewById(R.id.textView17);
            people=itemView.findViewById(R.id.textView18);
            bt=itemView.findViewById(R.id.bt);
            view=itemView.findViewById(R.id.imageView5);
        }
    }
}
