package com.example.listen;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class masuk extends AppCompatActivity {
    private FirebaseAuth authProfile;
    Button masuk, device, daftar;
    EditText user, password;
    Button btnhide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masuk);
        authProfile = FirebaseAuth.getInstance();
        daftar = findViewById(R.id.btndaftar);
        btnhide = findViewById(R.id.btnhidepw);
        user = findViewById(R.id.emaillogin);
        password = findViewById(R.id.passwordlogin);
        masuk = findViewById(R.id.btnlogin);
        btnhide.setBackgroundResource(R.drawable.hidepw);

        btnhide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    // klo kliatan ya sembunyikna
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    btnhide.setBackgroundResource(R.drawable.hidepw);
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    btnhide.setBackgroundResource(R.drawable.showpw);
                }
            }
        });
        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user.getText().toString();
                String pass = password.getText().toString();

                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if(!pass.isEmpty()){
                        authProfile.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(masuk.this, "Berhasil Masuk", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(masuk.this, MainActivity.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(masuk.this, "Gagal Masuk!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        password.setError("Isi Password!!");
                    }
                }else if(email.isEmpty()){
                    user.setError("Isi Email!!");

                } else {
                    user.setError("Silahkan Isi Email!!");
                }
            }
        });



        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(masuk.this, register.class));
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (authProfile.getCurrentUser() != null){
            Toast.makeText(masuk.this, "Sudah LogIn!",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(masuk.this, MainActivity.class));
            finish();
        }else{
            Toast.makeText(masuk.this, "Silahkan Login!",Toast.LENGTH_SHORT).show();
        }
    }
}