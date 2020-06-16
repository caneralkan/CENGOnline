
package com.example.plhomework.Activities.Assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plhomework.Activities.Announcement.EditAnnouncementActivity;
import com.example.plhomework.Activities.Course.CourseDetailActivity;
import com.example.plhomework.OOPFiles.Assignment;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class EditAssignmentActivity extends AppCompatActivity {
    EditText assignmentTitle,assignmentContext,endDate;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Intent intent;
    Assignment assignment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_assignment);
        intent=getIntent();
        assignment=(Assignment) intent.getSerializableExtra("assignment");
        assignmentContext=findViewById(R.id.assignmentContext);
        assignmentTitle=findViewById(R.id.assignmentTitle);
        endDate=findViewById(R.id.endDate);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        assignmentContext.setText(assignment.getDescription());
        assignmentTitle.setText(assignment.getAssignmentTitle());
        endDate.setText(assignment.getEndDate());
    }
    public void editAssignmentClicked(View view){
        DocumentReference documentReference=firebaseFirestore.collection("Course_Assignment").document(assignment.getAssignmentID());
        WriteBatch batch = firebaseFirestore.batch();
        if(assignmentContext.getText().toString().matches("") ||assignmentTitle.getText().toString().matches("")||endDate.getText().toString().matches("") ){
            Toast.makeText(EditAssignmentActivity.this,"Fill the spaces!",Toast.LENGTH_LONG).show();
        }else{
            batch.update(documentReference,"assignmentContext",assignmentContext.getText().toString());
            batch.update(documentReference,"assignmentTitle",assignmentTitle.getText().toString());
            batch.update(documentReference,"endDate",endDate.getText().toString());
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(EditAssignmentActivity.this, "Assignment has been edited!", Toast.LENGTH_LONG).show();

                    Intent intent2=new Intent(EditAssignmentActivity.this, AssignmentDetailTeacherActivity.class);
                    intent2.putExtra("assignment",assignment);
                    startActivity(intent2);
                    finish();
                }
            });
        }

    }
}
