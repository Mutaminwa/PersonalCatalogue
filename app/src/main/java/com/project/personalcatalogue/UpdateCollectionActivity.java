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
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class UpdateCollectionActivity extends AppCompatActivity implements View.OnClickListener {

    TextView sName;
    EditText sDesc;
    Button editShelfButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Create object of the Firebase Realtime Database
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String shelfID, userID = fAuth.getCurrentUser().getUid();
    CollectionReference shelfRef = db.collection("users/"+ userID + "/shelves");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_collection);

        sName = findViewById(R.id.txtEditCollection);
        sDesc = findViewById(R.id.txtNewDesc);
        editShelfButton = findViewById(R.id.btnEditShelf);

        editShelfButton.setOnClickListener(this);

        if(getIntent().hasExtra("EXTRA_ID")) {
            shelfID = getIntent().getStringExtra("EXTRA_ID");
        }

        shelfRef.document(shelfID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        sName.setText(shelfID);
                    }
                });

    }
    @Override
    public void onClick(final View v) {
        if (v == editShelfButton) {

            String shelfDesc = sDesc.getText().toString().trim();

            if(shelfDesc.length() > 250){
                sDesc.setError("That's too many words ( ._.)");
                return;
            }

            shelfRef.document(shelfID).update("description", shelfDesc).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Snackbar.make(v, "Shelf is updated!", Snackbar.LENGTH_SHORT).show();
                    startActivity(new Intent(UpdateCollectionActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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