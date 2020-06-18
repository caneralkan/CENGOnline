package com.example.plhomework.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plhomework.Activities.Assignment.AssignmentDetailStudentActivity;
import com.example.plhomework.Activities.Assignment.AssignmentDetailTeacherActivity;
import com.example.plhomework.Activities.Course.PostDialogFragment;
import com.example.plhomework.Activities.Course.StreamActivity;
import com.example.plhomework.Activities.LoginActivity;
import com.example.plhomework.Model.Comment;
import com.example.plhomework.Model.Post;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostRecyclerAdapter.PostHolder> {

    String courseID;
    FirebaseFirestore firebaseFirestore;
    String teacherEmail;
    Context context;

    public PostRecyclerAdapter(String courseID, Context context) {
        this.courseID = courseID;
        this.context = context;
        firebaseFirestore=FirebaseFirestore.getInstance();

    }

    @NonNull
    @Override
    public PostRecyclerAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.post_card, parent, false);

        return new PostRecyclerAdapter.PostHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final PostRecyclerAdapter.PostHolder holder, final int position) {

        firebaseFirestore.collection("Courses").whereEqualTo("courseID", courseID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                    Map<String, Object> data = snapshot.getData();
                    teacherEmail = (String) data.get("teacherEmail");
                }

                holder.posterEmail.setText(teacherEmail);
            }
        });
        holder.postTitle.setText(StreamActivity.posts.get(position).getPostTitle());
        holder.postContext.setText(StreamActivity.posts.get(position).getPostContext());
        holder.postDate.setText(StreamActivity.posts.get(position).getPostDate());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        final CommentRecyclerAdapter commentRecyclerAdapter = new CommentRecyclerAdapter(context, StreamActivity.posts.get(position).getComments());
        holder.recyclerView.setAdapter(commentRecyclerAdapter);
        holder.sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//comment ekleme
                if (!holder.commentText.getText().toString().matches("")) {
                    //post'un içine commenti ekle
                    StreamActivity.posts.get(position).addCommentToPost(new Comment(LoginActivity.currentUser.getEmail(), holder.commentText.getText().toString(), new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date())));
                    //comment'i firebase'e ekle
                    firebaseFirestore.collection("Courses").whereEqualTo("courseID", courseID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                                HashMap<String, String> put = new HashMap<>();
                                put.put("commenterEmail", LoginActivity.currentUser.getEmail());
                                put.put("commentContext", holder.commentText.getText().toString());
                                put.put("commentDate", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                                String uniqueID = UUID.randomUUID().toString();
                                put.put("commentID", uniqueID);
                                firebaseFirestore.collection("Courses").document(snapshot.getId()).collection("Posts").document(StreamActivity.posts.get(position).getPostID()).collection("Comments").document(uniqueID).set(put);
                                commentRecyclerAdapter.notifyDataSetChanged();
                            }
                            holder.commentText.setText("");
                        }
                    });
                }

            }
        });



        if(!LoginActivity.currentUser.isStudent()) {
            //aşağıdaki kod mesajlara tıklanırsa ne olacağıdır.
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClickListener(View v, final int position) {
                    PostDialogFragment myFragment=new PostDialogFragment(context, position,courseID,teacherEmail);
                    FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();

                    myFragment.show(manager,"My Dialog");
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return StreamActivity.posts.size();
    }

    public class PostHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        ItemClickListener itemClickListener;
        TextView posterEmail, postDate, postTitle, postContext;
        RecyclerView recyclerView;
        EditText commentText;
        ImageView sendComment;

        public PostHolder(@NonNull View itemView) {
            super(itemView);
            posterEmail = itemView.findViewById(R.id.posterEmail);
            postDate = itemView.findViewById(R.id.datePost);
            postTitle = itemView.findViewById(R.id.postTitle);
            postContext = itemView.findViewById(R.id.postContext);
            recyclerView = itemView.findViewById(R.id.commentRecyclerView);
            commentText = itemView.findViewById(R.id.addComment);
            sendComment = itemView.findViewById(R.id.sendComment);
            if(!LoginActivity.currentUser.isStudent()){itemView.setOnClickListener(this);}
        }

        @Override
        public void onClick(View v) {
            if(!LoginActivity.currentUser.isStudent()){this.itemClickListener.onItemClickListener(v,getLayoutPosition());}
        }
        public void setItemClickListener(ItemClickListener ic){
            if(!LoginActivity.currentUser.isStudent()){this.itemClickListener=ic;}
        }
    }
}
