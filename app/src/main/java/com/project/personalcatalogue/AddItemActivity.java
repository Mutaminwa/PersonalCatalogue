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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity {

    EditText bTitle, bAuthors, bISBN, bISBNthirteen, bDesc, bNotes;
    RatingBar bRating;
    Button btnAdd;
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Create object of the Firebase Realtime Database
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String shelfID, userID = fAuth.getCurrentUser().getUid();
    CollectionReference shelfRef = db.collection("users/"+ userID + "/shelves");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        if(getIntent().hasExtra("EXTRA_ID")) {
            shelfID = getIntent().getStringExtra("EXTRA_ID");
        }

        bTitle = findViewById(R.id.txtTitle);
        bAuthors = findViewById(R.id.txtAuthors);
        bISBN = findViewById(R.id.txtISBN);
        bISBNthirteen = findViewById(R.id.txtISBNthirteen);
        bDesc = findViewById(R.id.txtBookDesc);
        bNotes = findViewById(R.id.txtBookNotes);
        bRating = findViewById(R.id.ratingBar);
        btnAdd = findViewById(R.id.btnAddBook);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Title = bTitle.getText().toString();
                final String Authors = bAuthors.getText().toString();
                final String ISBN = bISBN.getText().toString();
                final String ISBNthirteen = bISBNthirteen.getText().toString();
                final String Desc = bDesc.getText().toString();
                final String Notes = bNotes.getText().toString();
                final Integer Rating = (int) bRating.getRating();

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
                else {
                    Map<String,Object> book = new HashMap<>();
                    book.put("title", Title);
                    book.put("authors", Authors);
                    book.put("isbn", ISBN);
                    book.put("isbn13", ISBNthirteen);
                    book.put("description", Desc);
                    book.put("notes", Notes);
                    book.put("rating", Rating);
                    shelfRef.document(shelfID).collection("books").document().set(book).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("Success", "onSuccess: Book is created for "+ userID);
                            Snackbar.make(v, "Your book has been added!", Snackbar.LENGTH_SHORT).show();
                            startActivity(new Intent(AddItemActivity.this, CollectionActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Fail", "That did not work. ", e);
                            Snackbar.make(v, "Your book couldn't be added. Maybe try again?", Snackbar.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }
}