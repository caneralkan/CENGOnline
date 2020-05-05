package com.example.plhomework;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.FeedRecyclerHolder> {
    @NonNull
    @Override
    public FeedRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.row,parent,false);

        return new FeedRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedRecyclerHolder holder, int position) {
        System.out.println("asdsadj");
        System.out.println(LoginActivity.allCourses.get(position).getCourseName());
        holder.courseName.setText(LoginActivity.allCourses.get(position).getCourseName());
        holder.courseID.setText(LoginActivity.allCourses.get(position).getCourseID());
        //holder.imagelv.setImageBitmap();
    }
    public class FeedRecyclerHolder extends RecyclerView.ViewHolder{
        ImageView imagelv;
        TextView courseID,courseName;
        public FeedRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            this.imagelv=itemView.findViewById(R.id.imagelv);
            this.courseID=itemView.findViewById(R.id.courseIDlv);
            this.courseName=itemView.findViewById(R.id.courseName);
        }
    }
    @Override
    public int getItemCount() {
        return LoginActivity.allCourses.size();
    }
}
