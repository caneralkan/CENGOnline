package com.example.plhomework.Activities.Announcement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.plhomework.Activities.Course.CourseDetailActivity;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AddAnnouncementActivity extends AppCompatActivity {
    EditText announcementTitle,announcement;
    Button addAnnouncement;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_announcement);
        announcementTitle=findViewById(R.id.announcementTitle);
        announcement=findViewById(R.id.announcementContext);
        addAnnouncement=findViewById(R.id.button_announcement);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
         intent=getIntent();
    }
    public void addAnnouncementClicked(View view){
        Map<String,Object> put=new HashMap<>();
        put.put("courseID",intent.getStringExtra("courseID"));
        put.put("announcementTitle",announcementTitle.getText().toString());
        put.put("announcement",announcement.getText().toString());
        put.put("date",new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
        String uniqueID = UUID.randomUUID().toString();
        put.put("announcementID",uniqueID);
        firebaseFirestore.collection("Course_Announcement").document(uniqueID).set(put).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(AddAnnouncementActivity.this, "Announcement has been created!", Toast.LENGTH_SHORT).show();
                Intent intent2=new Intent(AddAnnouncementActivity.this, CourseDetailActivity.class);
                intent2.putExtra("courseID",intent.getStringExtra("courseID"));

                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
            }
        });



    }
}
