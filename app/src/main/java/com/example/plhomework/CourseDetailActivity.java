package com.example.plhomework;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class CourseDetailActivity extends AppCompatActivity {
    TextView courseNamead,courseIDad;
    Course course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        ActionBar actionBar=getSupportActionBar();
        this.courseNamead=findViewById(R.id.courseNameAD);
        this.courseIDad=findViewById(R.id.courseIDad);
        Intent intent=getIntent();
        course=LoginActivity.allCourses.get(intent.getIntExtra("coursePosition",0));
        courseNamead.setText(course.getCourseName());
        courseIDad.setText(course.getCourseID());
        actionBar.setTitle(courseIDad.getText().toString());
    }
}
