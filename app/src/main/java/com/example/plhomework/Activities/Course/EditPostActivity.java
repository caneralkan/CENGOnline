package com.example.plhomework.Activities.Course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plhomework.Activities.Assignment.AssignmentActivity;
import com.example.plhomework.Activities.Assignment.EditAssignmentActivity;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

public class EditPostActivity extends AppCompatActivity {
    EditText editPostTitle, editPostContext;
    Intent intent;

    String courseID, teacherEmail;
    int position;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        editPostContext = findViewById(R.id.editPostContext);
        editPostTitle = findViewById(R.id.editPostTitle);
        intent = getIntent();
        position = intent.getIntExtra("position", 0);
        courseID = intent.getStringExtra("courseID");
        teacherEmail = intent.getStringExtra("teacherEmail");
        firebaseFirestore = FirebaseFirestore.getInstance();
        editPostTitle.setText(StreamActivity.posts.get(position).getPostTitle());
        editPostContext.setText(StreamActivity.posts.get(position).getPostContext());
    }

    public void editPostClicked(View view) {
        if (editPostContext.getText().toString().matches("") || editPostTitle.getText().toString().matches("")) {

            Toast.makeText(EditPostActivity.this, "Fill the spaces!", Toast.LENGTH_LONG).show();
        } else {

            StreamActivity.posts.get(position).setPostContext(editPostContext.getText().toString());
            StreamActivity.posts.get(position).setPostTitle(editPostTitle.getText().toString());
            // firebase'deki postu değiştir.
            firebaseFirestore.collection("Courses").whereEqualTo("courseID", courseID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {

                        DocumentReference documentReference = firebaseFirestore.collection("Courses").document(snapshot.getId()).collection("Posts").document(StreamActivity.posts.get(position).getPostID());
                        WriteBatch batch = firebaseFirestore.batch();

                        batch.update(documentReference, "postTitle", editPostTitle.getText().toString());
                        batch.update(documentReference, "postContext", editPostContext.getText().toString());
                        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(EditPostActivity.this, "Post has been edited!", Toast.LENGTH_LONG).show();



                                Intent intent2 = new Intent(EditPostActivity.this, StreamActivity.class);
                                intent2.putExtra("courseID", courseID);
                                intent2.putExtra("teacherEmail", teacherEmail);

                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(intent2);
                                overridePendingTransition(0, 0);
                            }
                        });
                    }
                }
            });
        }
    }
}
