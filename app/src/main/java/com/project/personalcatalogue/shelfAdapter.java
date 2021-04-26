package com.project.personalcatalogue;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;


// FirestoreRecyclerAdapter is a class provided by
// FirebaseUI. it provides functions to bind, adapt and show
// database contents in a Recycler View
public class shelfAdapter extends FirestoreRecyclerAdapter<
        shelf, shelfAdapter.shelfAdapterVH> {
    private static DocumentSnapshot snapshot;
    private OnItemClickListener listener;
        //public DocumentSnapshot snapshot;
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
        shelfAdapter.shelfAdapterVH viewHolder = new shelfAdapter.shelfAdapterVH(view);
        return viewHolder;
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
        LinearLayout shelfLayout;
        public shelfAdapterVH(@NonNull View itemView) {
            super(itemView);
            shelfname = itemView.findViewById(R.id.shelfname);
            shelfLayout = itemView.findViewById(R.id.frameLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position );
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}