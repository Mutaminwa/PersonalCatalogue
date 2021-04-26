package com.project.personalcatalogue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CollectionActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView shelfName, descLabel ;
    ImageButton btnDelete, btnEdit;
    shelfAdapter shelfadapter; // Create Object of the Adapter class
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Create object of the Firebase Realtime Database
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String shelfID, userID = fAuth.getCurrentUser().getUid();
    CollectionReference shelfRef = db.collection("users/"+ userID + "/shelves");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        recyclerView = findViewById(R.id.bookshelfRecycler);
        toolbar = findViewById(R.id.searchBar);
        descLabel = findViewById(R.id.txtCollectionsDesc);
        shelfName = findViewById(R.id.txtCollectionsName);
        btnDelete = findViewById(R.id.btnDeleteShelf);
        btnEdit = findViewById(R.id.btnEditShelf);

        this.setSupportActionBar(findViewById(R.id.searchBar));

        if(getIntent().hasExtra("EXTRA_ID")) {
            shelfID = getIntent().getStringExtra("EXTRA_ID");
        }

        /* To display the Recycler view linearly */
        recyclerView.setLayoutManager(new LinearLayoutManager(CollectionActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(shelfadapter);

        //Putting Collection name and details
        db.collection("users/"+ userID + "/shelves/"+ shelfID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot doc : task.getResult()) {
                                Log.d("data received", "onComplete: "+ doc.getId());

                                shelfName.setText(doc.getString("name"));
                                descLabel.setText(doc.getString("description"));
                            }

                        }
                    }
                });

    }
    @Override
    public void onClick(final View v) {
        if (v == btnDelete) {

            shelfRef.document(shelfID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Snackbar.make(v, "Bookshelf was deleted", Snackbar.LENGTH_SHORT).show();
                    startActivity(new Intent(CollectionActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Fail", "That did not work. ", e);
                }
            });
        }
        if (v == btnEdit) {
            Intent intent = new Intent(CollectionActivity.this, UpdateCollectionActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXTRA_ID", shelfID);
            startActivity(intent);

        }
    }

}