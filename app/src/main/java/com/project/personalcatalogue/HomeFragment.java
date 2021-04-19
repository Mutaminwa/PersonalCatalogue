package com.project.personalcatalogue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;
    shelfAdapter shelfadapter; // Create Object of the Adapter class
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Create object of the Firebase Realtime Database
    CollectionReference collectionReference = db.collection("shelves");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = recyclerView.findViewById(R.id.collectionsRecycler);

        /* To display the Recycler view linearly */
        recyclerView.setLayoutManager(
                new LinearLayoutManager( getActivity()));


        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirestoreRecyclerOptions<shelf> options = new FirestoreRecyclerOptions.Builder<shelf>()
                .setQuery(collectionReference, shelf.class)
                .build();

        // Connecting object of required Adapter class to
        // the Adapter class itself
        shelfadapter = new shelfAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(shelfadapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override
    public void onStart()
    {
        super.onStart();
        shelfadapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override public void onStop()
    {
        super.onStop();
        shelfadapter.stopListening();
    }

}