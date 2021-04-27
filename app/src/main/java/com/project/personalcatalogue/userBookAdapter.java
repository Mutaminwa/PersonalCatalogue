package com.project.personalcatalogue;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class userBookAdapter extends FirestoreRecyclerAdapter<userBook, userBookAdapter.userBookVH> {
    private static DocumentSnapshot snapshot;
    private userBookAdapter.OnItemClickListener listener;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public userBookAdapter(@NonNull FirestoreRecyclerOptions<userBook> options) {

        super(options);
    }
    @NonNull
    @Override
    public userBookVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_books, parent, false);
        userBookAdapter.userBookVH viewHolder = new userBookAdapter.userBookVH(view);
        return viewHolder;
    }

    @Override
    protected void onBindViewHolder(@NonNull userBookVH holder, int position, @NonNull userBook model) {
        // Add book and author names from model class (here
        // "userBook.class")to appropriate view in Card
        // view (here "row_books.xml")
        holder.bookName.setText(model.getTitle());
        holder.bookAuthorName.setText(model.getAuthors());
    }

    public class userBookVH extends RecyclerView.ViewHolder{

        TextView bookName, bookAuthorName;

        public userBookVH(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.bookName);
            bookAuthorName = itemView.findViewById(R.id.bookAuthorName);
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
    public void setOnItemClickListener(userBookAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }




}
