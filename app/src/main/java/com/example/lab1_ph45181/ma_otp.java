package com.example.lab1_ph45181;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ma_otp extends AppCompatActivity {
Button bt1,bt2;
EditText ed;
TextInputEditText ted;
FirebaseAuth mAuth;
String TAG=ma_otp.class.getName();
String verification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ma_otp);
         mAuth=FirebaseAuth.getInstance();
        bt1=findViewById(R.id.button5);
        bt2=findViewById(R.id.button6);
        ted=findViewById(R.id.tedd);
        ed=findViewById(R.id.editTextText6);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sdt=ted.getText().toString().trim();

                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(sdt)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(ma_otp.this)                 // (optional) Activity for callback binding
                                // If no activity is passed, reCAPTCHA verification can not be used.
                                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                    @Override
                                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                        signInWithPhoneAuthCredential(phoneAuthCredential);
                                    }

                                    @Override
                                    public void onVerificationFailed(@NonNull FirebaseException e) {
                                        Toast.makeText(ma_otp.this, "xác minh thất bại", Toast.LENGTH_SHORT).show();

                                    }
                                    @Override
                                    public void onCodeSent(@NonNull String verificationId,
                                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                        // The SMS verification code has been sent to the provided phone number, we
                                        // now need to ask the user to enter the code and then construct a credential
                                        // by combining the code with a verification ID.
                                        Log.d(TAG, "onCodeSent:" + verificationId);
                                        verification=verificationId;
                                        // Save verification ID and resending token so we can use them later

                                    }
                                })          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp=ed.getText().toString().trim();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verification, otp);
                signInWithPhoneAuthCredential(credential);
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            Intent intent=new Intent(ma_otp.this,Logout.class);
                            startActivity(intent);
                            Toast.makeText(ma_otp.this, "xac minh thanh cong", Toast.LENGTH_SHORT).show();
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(ma_otp.this, "Mã xác minh ko hợp lệ", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}