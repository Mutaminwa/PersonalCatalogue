package com.project.personalcatalogue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mUserName, mEmail, mPassword, mPasswordConfirm;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUserName = findViewById(R.id.txtUsername);
        mEmail = findViewById(R.id.txtEmail);
        mPassword = findViewById(R.id.txtPassword);
        mPasswordConfirm = findViewById(R.id.txtPasswordConfirm);
        mRegisterBtn = findViewById(R.id.btnRegister);
        mLoginBtn = findViewById(R.id.txtLogin);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);

        if(fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String passwordConf = mPasswordConfirm.getText().toString().trim();
                final String userName = mUserName.getText().toString();

                if(TextUtils.isEmpty(userName)){
                    mEmail.setError("Username is a required field.");
                    return;
                }
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is a required field.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is a required field.");
                    return;
                }
                if(TextUtils.isEmpty(passwordConf)){
                    mPassword.setError("You need to confirm your Password.");
                    return;
                }

                if(password.length() < 5){
                    mPassword.setError("Password must be 5 or more characters.");
                    return;
                }
                if(!password.equals(passwordConf)){
                    mPassword.setError("Oops! Your passwords don't match.");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // register the user in firebase

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(RegisterActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference docRef = fStore.collection("users").document(userID);

                            Map<String,Object> user = new HashMap<>();
                            user.put("userName", userName);
                            user.put("email", email);
                            docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: User profile is created for "+ userID);
                                }
                            });
                            startActivity(new Intent (getApplicationContext(), MainActivity.class));

                        }else {
                            Toast.makeText(RegisterActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

    }

    public void login(View view) {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}