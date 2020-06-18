package com.example.plhomework.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plhomework.Model.Comment;
import com.example.plhomework.R;

import java.util.ArrayList;

public class CommentRecyclerAdapter extends RecyclerView.Adapter<CommentRecyclerAdapter.CommentHolder> {
    Context context;
    ArrayList<Comment> comments;

    public CommentRecyclerAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override

    public CommentRecyclerAdapter.CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.comment_card, parent, false);

        return new CommentRecyclerAdapter.CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentRecyclerAdapter.CommentHolder holder, int position) {
        holder.commentDate.setText(comments.get(position).getCommentDate());
        holder.commenterEmail.setText(comments.get(position).getCommenterEmail());
        holder.commentContext.setText(comments.get(position).getCommentContext());

    }

    @Override
    public int getItemCount() {
        return this.comments.size();
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        TextView commenterEmail, commentDate, commentContext;

        public CommentHolder(@NonNull View itemView) {
            super(itemView);
            commentContext = itemView.findViewById(R.id.commentCard);
            commenterEmail = itemView.findViewById(R.id.commenterEmailCard);
            commentDate = itemView.findViewById(R.id.commentDate);
        }
    }
}
