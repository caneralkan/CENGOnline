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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

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
    public void onBindViewHolder(@NonNull final FeedRecyclerHolder holder, final int position) {
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        CollectionReference collectionReference=firebaseFirestore.collection("Courses");
        collectionReference.whereEqualTo("courseID",LoginActivity.allCourses.get(position)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(DocumentSnapshot snapshot:task.getResult().getDocuments()){
                    Map<String,Object> data=snapshot.getData();

                    holder.courseName.setText((String) data.get("courseName"));
                    holder.courseID.setText((String) data.get("courseID"));
                }
            }
        });



        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String courseIDCD=LoginActivity.allCourses.get(position);
                Intent intent =new Intent(context,CourseDetailActivity.class);
                intent.putExtra("courseID",courseIDCD);
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
