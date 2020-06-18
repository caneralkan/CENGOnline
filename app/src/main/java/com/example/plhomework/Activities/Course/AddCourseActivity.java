package com.example.plhomework.Activities.Course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.plhomework.Activities.LoginActivity;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class AddCourseActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;

    FirebaseUser firebaseUser;
    EditText courseName;
    EditText courseID;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        courseName = findViewById(R.id.courseName);
        courseID = findViewById(R.id.courseID);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();


    }

    public void addCourseClicked(View view) {
        if (courseName.getText().toString().matches("") || courseID.getText().toString().matches("")) {
            Toast.makeText(AddCourseActivity.this, "Verify your inputs!", Toast.LENGTH_LONG).show();
        } else {
            firebaseFirestore.collection("Courses").whereEqualTo("courseID", courseID.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (!task.getResult().isEmpty()) {

                        Toast.makeText(AddCourseActivity.this, "There's a course with that ID.Please change.", Toast.LENGTH_LONG).show();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);

                        HashMap<String, String> postCourse = new HashMap<>();
                        postCourse.put("courseName", courseName.getText().toString());
                        postCourse.put("courseID", courseID.getText().toString());
                        postCourse.put("teacherEmail", LoginActivity.currentUser.getEmail());
                        firebaseFirestore.collection("Courses").add(postCourse).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                CollectionReference collectionReference = firebaseFirestore.collection("Course_User");
                                HashMap<String, String> enrollUser = new HashMap<>();
                                enrollUser.put("email", firebaseUser.getEmail());
                                enrollUser.put("courseID", courseID.getText().toString());
                                enrollUser.put("date", Timestamp.now().toDate().toString());
                                collectionReference.add(enrollUser).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {

                                        Toast.makeText(AddCourseActivity.this, "Course Created", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(AddCourseActivity.this, CourseFeedActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        //Course course=new Course(courseName.getText().toString(),courseID.getText().toString(),teacherName.getText().toString(),teacherSurname.getText().toString(),teacherEmail.getText().toString());
                                        LoginActivity.allCourses.add(courseID.getText().toString());
                                        //CourseFeedActivity.feedRecyclerAdapter.notifyDataSetChanged();
                                        startActivity(intent);
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddCourseActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        });

                        progressBar.setVisibility(View.GONE);
                    }
                }
            });

        }
    }

}
