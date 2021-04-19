package com.project.personalcatalogue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

// FirestoreRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class shelfAdapter extends FirestoreRecyclerAdapter<
        shelf, shelfAdapter.shelfAdapterVH> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public shelfAdapter(@NonNull FirestoreRecyclerOptions<shelf> options) {
        super(options);
    }

    @NonNull
    @Override
    public shelfAdapterVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_shelf, parent, false);
        return new shelfAdapter.shelfAdapterVH(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull shelfAdapterVH holder, int position, @NonNull shelf model) {
        // Add shelfname from model class (here
        // "shelf.class")to appropriate view in Card
        // view (here "row_shelf.xml")
        holder.shelfname.setText(model.getName());
    }

    public class shelfAdapterVH extends RecyclerView.ViewHolder{
        TextView shelfname;
        public shelfAdapterVH(@NonNull View itemView) {
            super(itemView);

            shelfname = itemView.findViewById(R.id.shelfname);
        }
    }
}