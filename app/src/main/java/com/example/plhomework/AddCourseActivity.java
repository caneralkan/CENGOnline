package com.example.plhomework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddCourseActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    EditText courseName;
    EditText courseID;
    EditText teacherName,teacherSurname,teacherEmail;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        courseName=findViewById(R.id.courseName);
        courseID=findViewById(R.id.courseID);
        teacherName=findViewById(R.id.teacherName);
        teacherSurname=findViewById(R.id.teacherSurname);
        teacherEmail=findViewById(R.id.teacherEmail);
        progressBar=findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();


    }
    public void addCourseClicked(View view){

        progressBar.setVisibility(View.VISIBLE);

        HashMap<String,String> postCourse=new HashMap<>();
        postCourse.put("courseName",courseName.getText().toString());
        postCourse.put("courseID",courseID.getText().toString());
        postCourse.put("teacherName",teacherName.getText().toString());
        postCourse.put("teacherSurname",teacherSurname.getText().toString());
        postCourse.put("teacherEmail",teacherEmail.getText().toString());
        firebaseFirestore.collection("Courses").add(postCourse).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AddCourseActivity.this,"Course Created", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AddCourseActivity.this, FeedActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Course course=new Course(courseName.getText().toString(),courseID.getText().toString(),teacherName.getText().toString(),teacherSurname.getText().toString(),teacherEmail.getText().toString());
                LoginActivity.allCourses.add(course);
                FeedActivity.feedRecyclerAdapter.notifyDataSetChanged();
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddCourseActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });

        progressBar.setVisibility(View.GONE);
    }
}
