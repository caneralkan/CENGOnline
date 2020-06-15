package com.example.plhomework.Activities.Assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plhomework.Activities.LoginActivity;
import com.example.plhomework.OOPFiles.Assignment;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AssignmentDetailStudentActivity extends AppCompatActivity {
    Intent intent;
    Assignment assignment;
    TextView assignmentTitle,startDate,dueDate,context,submissionStatus,courseIDAsDetail;
    EditText submitText;
    Toolbar toolbar;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_detail_student);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        intent=getIntent();
        assignment=(Assignment) intent.getSerializableExtra("assignment");//assignment objesini assignmentRecyclerAdapterdan alıyoruz.

        toolbar=findViewById(R.id.toolbarAssignmentStudent);
        setSupportActionBar(toolbar);

        courseIDAsDetail=findViewById(R.id.courseIDAsDetail);
        assignmentTitle=findViewById(R.id.assignmentTitleText);
        startDate=findViewById(R.id.assignmentStartDate);
        dueDate=findViewById(R.id.assignmentDueDate);
        context=findViewById(R.id.assignmentContextDetail);
        submissionStatus=findViewById(R.id.assignmentSubmissionStatusText);
        submitText=findViewById(R.id.assignmentSubmit);

        assignmentTitle.setText(assignment.getAssignmentTitle());
        startDate.setText(assignment.getStartDate());
        dueDate.setText(assignment.getEndDate());
        context.setText(assignment.getDescription());
        courseIDAsDetail.setText(assignment.getCourseID());
        //submission status ve submitText databasede yapılan aramaya göre belirlenecek
        firebaseFirestore.collection("Course_Assignment").document(assignment.getAssignmentID()).collection("Submits").whereEqualTo("userID", LoginActivity.userID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.getDocuments().isEmpty()) {
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        submissionStatus.setText("Handed in on "+data.get("submissionDate") );
                        submitText.setText((String)data.get("assignment"));
                    }
                }
                else {
                    submissionStatus.setText("Missing");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AssignmentDetailStudentActivity.this, "Submission is not found!", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void submitAssignmentClicked(View view){
        if(submitText.getText().toString().matches("")){
            Toast.makeText(this, "Put your assignment first!", Toast.LENGTH_SHORT).show();
        }
        else{
            Map<String, Object> put = new HashMap<>();
            put.put("userEmail", LoginActivity.currentUser.getEmail());
            put.put("assignment",submitText.getText().toString());
            put.put("userID",LoginActivity.userID);
            put.put("submissionDate",new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss", Locale.getDefault()).format(new Date()));
            firebaseFirestore.collection("Course_Assignment").document(assignment.getAssignmentID()).collection("Submits").document(LoginActivity.userID).set(put).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(AssignmentDetailStudentActivity.this, "Your work has been submitted!", Toast.LENGTH_LONG).show();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
            });
        }
    }
}
