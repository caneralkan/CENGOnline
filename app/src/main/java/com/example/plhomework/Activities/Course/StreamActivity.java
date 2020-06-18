package com.example.plhomework.Activities.Course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.plhomework.Activities.Assignment.AssignmentActivity;
import com.example.plhomework.Adapters.AssignmentRecyclerAdapter;
import com.example.plhomework.Adapters.PostRecyclerAdapter;
import com.example.plhomework.Model.Comment;
import com.example.plhomework.Model.Post;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class StreamActivity extends AppCompatActivity {
    String courseID, teacherEmail;
    Intent intent;
    Dialog popAddPost;
    ImageView popupAddBtn;
    TextView popupTitle, popupDescription;
    ProgressBar popupClickProgress;
    public static ArrayList<Post> posts;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        intent = getIntent();
        courseID = intent.getStringExtra("courseID");
        posts=new ArrayList<>();
        teacherEmail = intent.getStringExtra("teacherEmail");
        iniPopup();
        firebaseFirestore = FirebaseFirestore.getInstance();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popAddPost.show();
            }
        });
        //TODO intent ile course alınacak, recyclerViewAdapter oluşturup bağla, posts'u oluştur. posts'un içine commentleri de ekle

        System.out.println("burdayım işte");
        firebaseFirestore.collection("Courses").whereEqualTo("courseID", courseID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {//her post için gez
                    firebaseFirestore.collection("Courses").document(documentSnapshot.getId()).collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (!task.getResult().isEmpty()) {//post var demektir.
                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {//her post için gez

                                    Map<String, Object> data = documentSnapshot.getData();
                                    System.out.println("postTitleları:" + data.get("postTitle"));
                                    final Post post = new Post((String) data.get("postID"), (String) data.get("postTitle"), (String) data.get("date"), (String) data.get("postContext"), teacherEmail);
                                    firebaseFirestore.collection("Courses").document(courseID).collection("Posts").document((String) data.get("postID")).collection("Comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                            if (!task.getResult().isEmpty()) {//comment var demektir
                                                for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {//her comment için gez
                                                    Map<String, Object> data = documentSnapshot.getData();
                                                    post.addCommentToPost(new Comment((String) data.get("commenterEmail"), (String) data.get("commentContext"), (String) data.get("commentDate")));
                                                }

                                            }
                                            System.out.println("say beni");
                                            StreamActivity.posts.add(post);//tüm postları ekle
                                            //TODO buralarda karışıyor
                                        }
                                    });
                                }
                                System.out.println("burdayım işte");

                            }
                        }
                    });
                }
                //TODO bu arkadaşlar erken çalışıyor. buraya bak!!!!!!!!!!!
                System.out.println("ben çalıştım");
                RecyclerView recyclerView = findViewById(R.id.postRecyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(StreamActivity.this));
                PostRecyclerAdapter postRecyclerAdapter = new PostRecyclerAdapter(courseID, StreamActivity.this);
                recyclerView.setAdapter(postRecyclerAdapter);

            }
        });
    }

    private void iniPopup() {
        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        //ini popup widgets
        popupAddBtn = popAddPost.findViewById(R.id.kalem);
        popupTitle = popAddPost.findViewById(R.id.postAddTitle);
        popupDescription = popAddPost.findViewById(R.id.postAddContext);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);

        //Add post click Listener

        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!popupTitle.getText().toString().matches("") && !popupDescription.getText().toString().matches("")) {
                    popupAddBtn.setVisibility(View.INVISIBLE);
                    popupClickProgress.setVisibility(View.VISIBLE);
                    firebaseFirestore.collection("Courses").whereEqualTo("courseID", courseID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                                DocumentReference documentReference = snapshot.getReference();
                                HashMap<String, String> put = new HashMap<>();
                                put.put("postTitle", popupTitle.getText().toString());
                                put.put("postContext", popupDescription.getText().toString());
                                put.put("date", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                                String uniqueID = UUID.randomUUID().toString();
                                put.put("postID", uniqueID);
                                documentReference.collection("Posts").document(uniqueID).set(put).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                            }
                            popupAddBtn.setVisibility(View.VISIBLE);
                            popupClickProgress.setVisibility(View.INVISIBLE);
                            popAddPost.dismiss();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        }
                    });
                } else {
                    Toast.makeText(StreamActivity.this, "Fill the spaces!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
