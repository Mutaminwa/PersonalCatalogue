package com.project.personalcatalogue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class CollectionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView shelfName, descLabel;
    Button btnDelete, btnEdit;
    FloatingActionButton fab;
    userBookAdapter userbookadapter; // Create Object of the Adapter class
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Create object of the Firebase Realtime Database
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String shelfID, userID = fAuth.getCurrentUser().getUid();
    CollectionReference shelfRef = db.collection("users/"+ userID + "/shelves");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        if(getIntent().hasExtra("EXTRA_ID")) {
            shelfID = getIntent().getStringExtra("EXTRA_ID");
        }
        CollectionReference booksRef = shelfRef.document(shelfID).collection("books");
        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirestoreRecyclerOptions<userBook> options = new FirestoreRecyclerOptions.Builder<userBook>()
                .setQuery(booksRef, userBook.class)
                .build();

        // Connecting object of required Adapter class to
        // the Adapter class itself
        userbookadapter = new userBookAdapter(options);

        recyclerView = findViewById(R.id.bookshelfRecycler);
        descLabel = findViewById(R.id.txtCollectionsDesc);
        shelfName = findViewById(R.id.txtCollectionsName);
        btnDelete = findViewById(R.id.btnDeleteShelf);
        btnEdit = findViewById(R.id.editShelf);
        fab = findViewById(R.id.floatingActionButton);

        /* To display the Recycler view linearly */
        recyclerView.setLayoutManager(new LinearLayoutManager(CollectionActivity.this));

        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(userbookadapter);
        userbookadapter.setOnItemClickListener(new userBookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Intent intent = new Intent(getApplicationContext(), ItemActivity.class);
                intent.putExtra("EXTRA_BookID", id);
                intent.putExtra("EXTRA_ShelfID", shelfID);
                startActivity(intent);

            }
        });

        //Putting Collection name and details
        shelfRef.document(shelfID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            shelfName.setText(documentSnapshot.getString("name"));
                            descLabel.setText(documentSnapshot.getString("description"));
                        } else {
                            Log.d("Fail", "That failed");
                        }
                    }
                });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CollectionActivity.this, AddItemActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXTRA_ID", shelfID);
                startActivity(intent);
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shelfRef.document(shelfID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(v, "Bookshelf was deleted", Snackbar.LENGTH_SHORT).show();
                        startActivity(new Intent(CollectionActivity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Fail", "That did not work. ", e);
                    }
                });
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CollectionActivity.this, UpdateCollectionActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXTRA_ID", shelfID);
                startActivity(intent);
            }
        });

    }
    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override
    public void onStart()
    {
        super.onStart();
        userbookadapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override public void onStop()
    {
        super.onStop();
        userbookadapter.stopListening();
    }
}