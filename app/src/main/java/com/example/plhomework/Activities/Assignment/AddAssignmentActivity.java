package com.example.plhomework.Activities.Assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plhomework.Activities.Announcement.AddAnnouncementActivity;
import com.example.plhomework.Activities.Course.CourseDetailActivity;
import com.example.plhomework.Activities.LoginActivity;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddAssignmentActivity extends AppCompatActivity {
    EditText assignmentTitle,assignmentContext,courseID,endDate;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        assignmentContext=findViewById(R.id.assignmentContext);
        assignmentTitle=findViewById(R.id.assignmentTitle);
        courseID=findViewById(R.id.assignmentCourseID);
        endDate=findViewById(R.id.endDate);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();

        System.out.println("1: "+LoginActivity.currentUser.getName());

    }
    public void addAssignmentClicked(View view){
        Map<String,Object> put=new HashMap<>();
        put.put("assignmentTitle",assignmentTitle.getText().toString());
        put.put("assignmentContext",assignmentContext.getText().toString());
        put.put("startDate",new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
        put.put("courseID",courseID.getText().toString());
        put.put("endDate",endDate.getText().toString());//burasını değiştir!
        String uniqueID = UUID.randomUUID().toString();
        put.put("assignmentID",uniqueID);
        firebaseFirestore.collection("Course_Assignment").document(uniqueID).set(put).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AddAssignmentActivity.this, "Assignment has been created!", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(AddAssignmentActivity.this, AssignmentActivity.class);
                startActivity(intent);
            }
        });
    }

}
