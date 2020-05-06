package com.example.plhomework;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.FeedRecyclerHolder> {


    Context context;
    public FeedRecyclerAdapter(Context context){
        this.context=context;
    }
    @NonNull
    @Override
    public FeedRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.row,parent,false);

        return new FeedRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedRecyclerHolder holder, int position) {
        holder.itemView.setVisibility(View.INVISIBLE);
        if(!LoginActivity.currentUser.isStudent() || (LoginActivity.currentUser.isStudent()&& LoginActivity.currentUser.isEnrolled(LoginActivity.allCourses.get(position)))){
        holder.courseName.setText(LoginActivity.allCourses.get(position).getCourseName());
        holder.courseID.setText(LoginActivity.allCourses.get(position).getCourseID());
        //holder.imagelv.setImageBitmap();
        holder.itemView.setVisibility(View.VISIBLE);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String courseNameCD=LoginActivity.allCourses.get(position).getCourseName();
                String courseIDCD=LoginActivity.allCourses.get(position).getCourseID();
                Intent intent =new Intent(context,CourseDetailActivity.class);
                intent.putExtra("courseName",courseNameCD);
                intent.putExtra("courseID",courseIDCD);
                int i=position;
                intent.putExtra("coursePosition",i);
                context.startActivity(intent);
            }
        });
    }
    public class FeedRecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imagelv;
        TextView courseID,courseName;
        ItemClickListener itemClickListener;
        public FeedRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            this.imagelv=itemView.findViewById(R.id.imagelv);
            this.courseID=itemView.findViewById(R.id.courseIDlv);
            this.courseName=itemView.findViewById(R.id.courseName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClickListener(v,getLayoutPosition());
        }
        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener=ic;
        }
    }
    @Override
    public int getItemCount() {
        return LoginActivity.allCourses.size();
    }
}
