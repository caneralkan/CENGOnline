package com.example.plhomework.Activities.Announcement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plhomework.Activities.Course.CourseDetailActivity;
import com.example.plhomework.Activities.LoginActivity;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class AnnouncementDetailActivity extends AppCompatActivity {
    TextView annTitle,annContext,annDate;
    FirebaseFirestore firebaseFirestore;
    Intent intent;
    String announcementID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_detail);
        intent=getIntent();
        annTitle=findViewById(R.id.announcementTtl);
        annContext=findViewById(R.id.announcementCntxt);
        annDate=findViewById(R.id.announcementDt);
        firebaseFirestore=FirebaseFirestore.getInstance();
        announcementID=intent.getStringExtra("announcementID");
        firebaseFirestore.collection("Course_Announcement").whereEqualTo("announcementID",announcementID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot snapshot:task.getResult().getDocuments()){
                    Map<String,Object> data=snapshot.getData();
                    annTitle.setText((String)data.get("announcementTitle"));
                    annContext.setText((String)data.get("announcement"));
                    annDate.setText((String)data.get("date"));
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Connecting xml file

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.announcement_detail_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteAnnouncement:

                //System.out.println("kursunyeriburda: "+LoginActivity.allCourses.indexOf(intent.getStringExtra("courseID")));
                DocumentReference documentReference=firebaseFirestore.collection("Course_Announcement").document(announcementID);
                        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AnnouncementDetailActivity.this, "Announcement Deleted!", Toast.LENGTH_SHORT).show();
                        //CourseDetailActivity.announcements.remove(intent.getIntExtra("position",0));
                        Intent intent2=new Intent(AnnouncementDetailActivity.this, CourseDetailActivity.class);
                        intent2.putExtra("courseID",intent.getStringExtra("courseID"));

                        startActivity(intent2);
                        finish();
                    }
                });
                return true;
            case R.id.editAnnouncement:

                Intent intentToEdit=new Intent(AnnouncementDetailActivity.this,EditAnnouncementActivity.class);
                intentToEdit.putExtra("announcementID",announcementID);
                intentToEdit.putExtra("courseID",intent.getStringExtra("courseID"));
                startActivity(intentToEdit);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(LoginActivity.currentUser!=null &&  LoginActivity.currentUser.isStudent()){
            MenuItem deleteAnnouncement = menu.findItem(R.id.deleteAnnouncement);
            deleteAnnouncement.setVisible(false);
            MenuItem editAnnouncement = menu.findItem(R.id.editAnnouncement);
            editAnnouncement.setVisible(false);

        }
        return super.onPrepareOptionsMenu(menu);
    }
}
