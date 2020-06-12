package com.example.plhomework.Activities.Announcement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.plhomework.Activities.Course.CourseDetailActivity;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class EditAnnouncementActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    EditText annTitle;
    EditText annContext;
    ProgressBar progressBar;
    String announcementID;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_announcement);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        annTitle=findViewById(R.id.annTitle);
        annContext=findViewById(R.id.annContext);
        progressBar=findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        intent=getIntent();
        announcementID=intent.getStringExtra("announcementID");
        firebaseFirestore.collection("Course_Announcement").whereEqualTo("announcementID",announcementID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot snapshot:task.getResult().getDocuments()){
                    Map<String, Object> data = snapshot.getData();
                    annTitle.setText((String)data.get("announcementTitle"));
                    annContext.setText((String)data.get("announcement"));

                }
            }
        });

    }
    public void editAnnouncementClicked(View view){
        DocumentReference documentReference=firebaseFirestore.collection("Course_Announcement").document(announcementID);
        WriteBatch batch = firebaseFirestore.batch();
        if(annTitle.getText().toString()==null || annContext.getText().toString()==null){
            Toast.makeText(EditAnnouncementActivity.this,"Fill the spaces!",Toast.LENGTH_LONG).show();
        }else{
            batch.update(documentReference,"announcement",annContext.getText().toString());
            batch.update(documentReference,"announcementTitle",annTitle.getText().toString());
            batch.update(documentReference,"date",new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
            batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(EditAnnouncementActivity.this, "Announcement has been edited!", Toast.LENGTH_LONG).show();

                    Intent intent2=new Intent(EditAnnouncementActivity.this, CourseDetailActivity.class);
                    intent2.putExtra("courseID",intent.getStringExtra("courseID"));
                    startActivity(intent2);
                    finish();
                }
            });
        }
    }
}
