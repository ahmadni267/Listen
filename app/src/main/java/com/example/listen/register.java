package com.example.listen;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class register extends AppCompatActivity {
    private FirebaseAuth auth;
    EditText emaildftr, passworddftr, undftr;
    Button masuk, daftar;
    private static final String TAG = "register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();
        emaildftr = findViewById(R.id.daftaremail);
        passworddftr = findViewById(R.id.daftarpassword);
        daftar = findViewById(R.id.btndaftar2);
        undftr = findViewById(R.id.username);
        masuk = findViewById(R.id.btnmasukkembali);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textfullusername = undftr.getText().toString();
                String textemail = emaildftr.getText().toString();
                String textpassword = passworddftr.getText().toString();

                if(TextUtils.isEmpty(textfullusername)){
                    Toast.makeText(register.this, "Isi Username!!!", Toast.LENGTH_LONG).show();
                    undftr.setError("Username Diperlukan!!");
                    undftr.requestFocus();
                }else if(TextUtils.isEmpty(textemail)){
                    Toast.makeText(register.this, "Isi Email!!!", Toast.LENGTH_LONG).show();
                    emaildftr.setError("Email Diperlukan!!");
                    emaildftr.requestFocus();
                }else if(TextUtils.isEmpty(textpassword)){
                    Toast.makeText(register.this, "Isi Password!!!", Toast.LENGTH_LONG).show();
                    passworddftr.setError("Passwprd Diperlukan!!");
                    passworddftr.requestFocus();
                }else{
                    registerUser(textemail,textpassword,textfullusername);
                }
            }
        });
        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this, masuk.class));
            }
        });
    }

    private void registerUser(String textemail, String textpassword, String textfullusername) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(textemail, textpassword).addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textfullusername).build();
                    firebaseUser.updateProfile(profileChangeRequest);

                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails (textfullusername, textemail,textpassword);
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered users");

                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                Toast.makeText(register.this, "Daftar akun berhasil!!",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(register.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(register.this, "Daftar akun gagal!!",Toast.LENGTH_LONG).show();
                            }
                        }

                    });

                }else{
                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        emaildftr.setError("Email invalid atau telah didaftarkan!");
                        emaildftr.requestFocus();
                    }catch (FirebaseAuthUserCollisionException e){
                        emaildftr.setError("Email telah didaftarkan oleh pengguna!");
                    }catch(Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(register.this, e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

    }
}