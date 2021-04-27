package com.project.personalcatalogue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserFragment extends Fragment {

    private Bundle savedInstanceState;
    private Context mContext;
    public UserFragment() {
        // Required empty public constructor
    }

    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Create object of the Firebase Realtime Database
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String userID = fAuth.getCurrentUser().getUid();
    CollectionReference usersRef = db.collection("users");
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        EditText mUserName = view.findViewById(R.id.txtUsername2);
        EditText mEmail = view.findViewById(R.id.txtEmail2);
        EditText mPassword = view.findViewById(R.id.txtPassword2);

        //Setting log out function
        Button logOutButton = (Button) view.findViewById(R.id.btnLogout2);
        logOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });

        //Setting delete user function
        Button deleteButton = view.findViewById(R.id.btnDeleteUser);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = fAuth.getCurrentUser();
                user.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Delete", "User account deleted.");
                                    startActivity(new Intent(mContext, LoginActivity.class));
                                }
                            }
                        });
            }
        });

        //Setting Update function
        Button updateButton = view.findViewById(R.id.editUser);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mUserName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String pass = mPassword.getText().toString().trim();
                if(!TextUtils.isEmpty(userName)){
                    usersRef.document(userID).update("userName", userName);
                    Toast.makeText(mContext, "Username updated!", Toast.LENGTH_SHORT).show();
                }
                if(!TextUtils.isEmpty(email)){
                    FirebaseUser user = fAuth.getCurrentUser();
                    user.updateEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("Update", "User email address updated.");
                                        Toast.makeText(mContext, "Email updated!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                if(!TextUtils.isEmpty(pass)){
                    FirebaseUser user = fAuth.getCurrentUser();
                    user.updatePassword(pass)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("Update", "User password updated.");
                                        Toast.makeText(mContext, "Password updated!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });

        return view;
    }
}