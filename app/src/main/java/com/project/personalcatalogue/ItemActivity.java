package com.project.personalcatalogue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ItemActivity extends AppCompatActivity {

    EditText bTitle, bAuthors, bISBN, bISBNthirteen, bDesc, bNotes;
    Button btnDelete, btnEdit;
    RatingBar ratingBar;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Create object of the Firebase Realtime Database
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String bookID, shelfID, userID = fAuth.getCurrentUser().getUid();
    CollectionReference shelfRef = db.collection("users/"+ userID + "/shelves");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        if(getIntent().hasExtra("EXTRA_BookID") && getIntent().hasExtra("EXTRA_ShelfID")) {
            shelfID = getIntent().getStringExtra("EXTRA_ShelfID");
            bookID = getIntent().getStringExtra("EXTRA_BookID");
        }


        bTitle = findViewById(R.id.txtTitle2);
        bAuthors = findViewById(R.id.txtAuthors2);
        bISBN = findViewById(R.id.txtISBN2);
        bISBNthirteen = findViewById(R.id.txtISBNthirteen2);
        bDesc = findViewById(R.id.txtBookDesc2);
        bNotes = findViewById(R.id.txtBookNotes2);
        btnEdit = findViewById(R.id.editBook);
        btnDelete = findViewById(R.id.btnDeleteBook);
        ratingBar = findViewById(R.id.ratingBar2);

        //Putting book details
        CollectionReference booksRef = shelfRef.document(shelfID).collection("books");
        booksRef.document(shelfID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            bTitle.setText(documentSnapshot.getString("title"));
                            bAuthors.setText(documentSnapshot.getString("authors"));
                            bISBN.setText(documentSnapshot.getString("isbn"));
                            bISBNthirteen.setText(documentSnapshot.getString("isbn13"));
                            bDesc.setText(documentSnapshot.getString("description"));
                            bNotes.setText(documentSnapshot.getString("notes"));
                            ratingBar.setRating(Float.parseFloat(documentSnapshot.getString("rating")));

                        } else {
                            Log.d("Fail", "That failed");
                        }
                    }
                });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Title = bTitle.getText().toString();
                final String Authors = bAuthors.getText().toString();
                final String ISBN = bISBN.getText().toString();
                final String ISBNthirteen = bISBNthirteen.getText().toString();
                final String Desc = bDesc.getText().toString();
                final String Notes = bNotes.getText().toString();
                final Integer Rating = (int) ratingBar.getRating();

                // Data Validation for the input data
                if(Title.length()==0)
                {
                    bTitle.requestFocus();
                    bTitle.setError("You can't leave this empty.");
                }
                else if(!Authors.matches("[a-zA-Z ]+"))
                {
                    bAuthors.requestFocus();
                    bAuthors.setError("Names can't have numbers.");
                }
                else if(!ISBN.matches("[0-9 ]+"))
                {
                    bISBN.requestFocus();
                    bISBN.setError("The ISBN can't have letters.");
                }
                else if(!ISBNthirteen.matches("[0-9 ]+"))
                {
                    bISBNthirteen.requestFocus();
                    bISBNthirteen.setError("The ISBN can't have letters.");
                }
                else {
                    booksRef.document(bookID).update("title", Title);
                    booksRef.document(bookID).update("authors", Authors);
                    booksRef.document(bookID).update("isbn", ISBN);
                    booksRef.document(bookID).update("isbn13", ISBNthirteen);
                    booksRef.document(bookID).update("description", Desc);
                    booksRef.document(bookID).update("notes", Notes);
                    booksRef.document(bookID).update("rating", Rating);
                    Snackbar.make(v, "Your book has been updated!", Snackbar.LENGTH_SHORT).show();
                    Intent intent = new Intent(ItemActivity.this, CollectionActivity.class);
                    intent.putExtra("EXTRA_ID", shelfID);
                    startActivity(intent);
                    finish();
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booksRef.document(bookID).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(v, "Book was deleted", Snackbar.LENGTH_SHORT).show();
                        Intent intent = new Intent(ItemActivity.this, CollectionActivity.class);
                        intent.putExtra("EXTRA_ID", shelfID);
                        startActivity(intent);
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
    }
}