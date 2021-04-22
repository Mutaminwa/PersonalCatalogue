package com.project.personalcatalogue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCollectionActivity extends AppCompatActivity implements View.OnClickListener {

    TextView sName, sDesc;
    Button addShelfButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Create object of the Firebase Realtime Database
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String userID = fAuth.getCurrentUser().getUid();
    CollectionReference shelfRef = db.collection("users/"+ userID + "/shelves");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);

        sName = findViewById(R.id.txtShelfName);
        sDesc = findViewById(R.id.txtShelfDesc);
        addShelfButton = findViewById(R.id.btnAddShelf);

        addShelfButton.setOnClickListener(this);
    }
    @Override
    public void onClick(final View v) {
        if (v == addShelfButton) {

            String shelfName = sName.getText().toString().trim();
            String shelfDesc = sDesc.getText().toString().trim();

            if(TextUtils.isEmpty(shelfName)){
                sName.setError("Name is a required field.");
                return;
            }

            Map<String,Object> shelf = new HashMap<>();
            shelf.put("name", shelfName);
            shelf.put("description", shelfDesc);
            shelfRef.document(shelfName).set(shelf).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Success", "onSuccess: Bookshelf is created for "+ userID);
                    Snackbar.make(v, "Bookshelf is created!", Snackbar.LENGTH_SHORT).show();
                    startActivity(new Intent (AddCollectionActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Fail", "That did not work. ", e);
                }
            });
        }
    }    
}