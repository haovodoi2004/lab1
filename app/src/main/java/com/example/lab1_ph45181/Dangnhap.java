package com.example.lab1_ph45181;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Dangnhap extends AppCompatActivity {
EditText ed1,ed2;
Button bt;
TextView tv,vt;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bt=findViewById(R.id.button);
        ed1=findViewById(R.id.editTextText);
        ed2=findViewById(R.id.editTextText2);
        tv=findViewById(R.id.textView7);
        vt=findViewById(R.id.textView9);
        progressDialog=new ProgressDialog(this);

        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth=FirebaseAuth.getInstance();
                String email=ed1.getText().toString();
                String password=ed2.getText().toString();
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Dangnhap.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Intent intent=new Intent(Dangnhap.this, Logout.class);
                                    startActivity(intent);
                                    finishAffinity();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Dangnhap.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        vt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Dangnhap.this,Quenmk.class);
                startActivity(intent);
            }
        });

tv.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(Dangnhap.this, Dangky.class);
        startActivity(intent);
    }
});

    }
    }