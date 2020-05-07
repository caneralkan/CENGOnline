package com.example.plhomework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.Map;

public class EditCourseActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    EditText courseName;
    EditText courseID;
    EditText teacherName,teacherSurname,teacherEmail;
    ProgressBar progressBar;
    String courseIDstring;
    String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);
        courseName=findViewById(R.id.courseName);
        courseID=findViewById(R.id.courseID);
        teacherName=findViewById(R.id.teacherName);
        teacherSurname=findViewById(R.id.teacherSurname);
        teacherEmail=findViewById(R.id.teacherEmail);
        progressBar=findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        Intent intent=getIntent();
        courseIDstring=intent.getStringExtra("courseID");
        firebaseFirestore.collection("Courses").whereEqualTo("courseID",courseIDstring).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot snapshot:task.getResult().getDocuments()){
                     documentId= snapshot.getId();
                    System.out.println(documentId);
                    Map<String, Object> data = snapshot.getData();
                    courseName.setHint((String)data.get("courseName"));
                    courseID.setHint((String)data.get("courseID"));
                    teacherEmail.setHint((String)data.get("teacherEmail"));
                    teacherName.setHint((String)data.get("teacherName"));
                    teacherSurname.setHint((String)data.get("teacherSurname"));

                }
            }
        });
    }
    public void editCourseClicked(View view) {
        final DocumentReference documentReference = firebaseFirestore.collection("Courses").document(documentId);
        WriteBatch batch = firebaseFirestore.batch();
        if (courseName.getText().toString() == null || courseID.getText().toString() == null || teacherName.getText().toString() == null || teacherEmail.getText().toString() == null || teacherSurname.getText().toString() == null) {
            Toast.makeText(EditCourseActivity.this,"Fill the spaces!",Toast.LENGTH_LONG).show();
        } else {
            batch.update(documentReference, "courseName", courseName.getText().toString());
            batch.update(documentReference, "courseID", courseID.getText().toString());
            batch.update(documentReference, "teacherName", teacherName.getText().toString());
            batch.update(documentReference, "teacherEmail", teacherEmail.getText().toString());
            batch.update(documentReference, "teacherSurname", teacherSurname.getText().toString());
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(EditCourseActivity.this, "Course has been edited!", Toast.LENGTH_LONG).show();
                    LoginActivity.allCourses.remove(LoginActivity.allCourses.indexOf(courseIDstring));
                    LoginActivity.allCourses.add(courseID.getText().toString());
                    System.out.println(LoginActivity.allCourses.indexOf(courseID.getText().toString()));
                    firebaseFirestore.collection("Course_User").whereEqualTo("courseID", courseIDstring).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                                documentId = snapshot.getId();
                                DocumentReference documentReference1 = firebaseFirestore.collection("Course_User").document(documentId);
                                documentReference1.update("courseID", courseID.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent=new Intent(EditCourseActivity.this,CourseDetailActivity.class);
                                        intent.putExtra("courseID",courseID.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    }
                                });

                            }
                        }
                    });
                    Intent intent=new Intent(EditCourseActivity.this,FeedActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
    }
}
