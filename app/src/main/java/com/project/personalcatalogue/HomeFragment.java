package com.project.personalcatalogue;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Bundle savedInstanceState;
    private Context mContext;
    public HomeFragment() {
        // Required empty public constructor
    }

    RecyclerView recyclerView;
    TextView nameLabel ;
    shelfAdapter shelfadapter; // Create Object of the Adapter class
    FirebaseFirestore db = FirebaseFirestore.getInstance(); // Create object of the Firebase Realtime Database
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    String userID = fAuth.getCurrentUser().getUid();
    CollectionReference collectionReference = db.collection("users/"+ userID + "/shelves");

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirestoreRecyclerOptions<shelf> options = new FirestoreRecyclerOptions.Builder<shelf>()
                .setQuery(collectionReference, shelf.class)
                .build();

        // Connecting object of required Adapter class to
        // the Adapter class itself
        shelfadapter = new shelfAdapter(options);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.collectionsRecycler);
        nameLabel = (TextView) view.findViewById(R.id.userName);

        /* To display the Recycler view linearly */
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(shelfadapter);

        //Setting log out function
        Button logOutButton = (Button) view.findViewById(R.id.btnLogout);
        logOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        });

        //Setting "add new shelf" function
        Button addShelfButton = (Button) view.findViewById(R.id.btnAddNewShelf);
        addShelfButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(mContext, AddCollectionActivity.class);
                intent.putExtra("EXTRA_USER_ID", userID);
                startActivity(intent);
            }
        });

        //Putting username in welcome message
        db.collection("users")
        .get()
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for(QueryDocumentSnapshot doc : task.getResult()) {
                        Log.d("data received", "onComplete: "+ doc.getId());

                        nameLabel.setText(doc.getString("userName"));
                    }

                }
            }
        });


        return view;
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