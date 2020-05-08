package com.example.plhomework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class CourseDetailActivity extends AppCompatActivity {
    TextView courseNamead,courseIDad;
    FirebaseFirestore firebaseFirestore;
    static AnnouncementRecyclerAdapter announcementRecyclerAdapter;
    static ArrayList<Announcement> announcements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        this.courseNamead=findViewById(R.id.courseNameAD);
        this.courseIDad=findViewById(R.id.courseIDad);
        firebaseFirestore=FirebaseFirestore.getInstance();
        final Intent intent=getIntent();

        CollectionReference collectionReference=firebaseFirestore.collection("Courses");
        System.out.println("kursidsi: "+intent.getStringExtra("courseID"));
        System.out.println("kursunyeri: "+LoginActivity.allCourses.indexOf(intent.getStringExtra("courseID")));
        collectionReference.whereEqualTo("courseID",LoginActivity.allCourses.get(LoginActivity.allCourses.indexOf(intent.getStringExtra("courseID")))).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for(DocumentSnapshot snapshot:task.getResult().getDocuments()) {
                    Map<String, Object> data = snapshot.getData();

                    courseNamead.setText((String)data.get("courseName"));
                    courseIDad.setText((String)data.get("courseID"));
                }
                firebaseFirestore=FirebaseFirestore.getInstance();
                firebaseFirestore.collection("Course_Announcement").whereEqualTo("courseID",courseIDad.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        announcements=new ArrayList<>();
                        for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                            Map<String, Object> data = documentSnapshot.getData();
                            announcements.add(new Announcement((String)data.get("announcement"),(String)data.get("announcementTitle"),(String)data.get("courseID"),(String)data.get("date"),(String)data.get("announcementID")));
                        }

                        RecyclerView recyclerView=findViewById(R.id.detailRecyclerView);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CourseDetailActivity.this));
                        announcementRecyclerAdapter=new AnnouncementRecyclerAdapter(CourseDetailActivity.this,announcements);
                        System.out.println("CourseID: "+courseIDad.getText().toString());
                        recyclerView.setAdapter(announcementRecyclerAdapter);

                    }
                });
            }
        });
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if(LoginActivity.currentUser!=null &&  LoginActivity.currentUser.isStudent()){
            MenuItem addCourse = menu.findItem(R.id.EditCourse);
            addCourse.setVisible(false);
            MenuItem deleteCourse=menu.findItem(R.id.DeleteCourse);
            deleteCourse.setVisible(false);
            MenuItem announcementMenu=menu.findItem(R.id.announcementMenu);
            announcementMenu.setVisible(false);
        }
        return true;
    }
    //Connecting menu to this activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Connecting xml file

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.course_detail_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.EditCourse:
                Intent intentToEdit=new Intent(CourseDetailActivity.this,EditCourseActivity.class);
                intentToEdit.putExtra("courseID",courseIDad.getText().toString());
                startActivity(intentToEdit);
                return true;
            case R.id.DeleteCourse:
                firebaseFirestore.collection("Courses").whereEqualTo("courseID",courseIDad.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot snapshot:task.getResult().getDocuments()) {
                            DocumentReference documentReference=firebaseFirestore.collection("Courses").document(snapshot.getId());
                            documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(CourseDetailActivity.this, "Course Deleted!", Toast.LENGTH_SHORT).show();
                                    LoginActivity.allCourses.remove(courseIDad.getText().toString());

                                    firebaseFirestore.collection("Course_User").whereEqualTo("courseID",courseIDad.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            for (DocumentSnapshot snapshot:task.getResult().getDocuments()){
                                                DocumentReference documentReference=firebaseFirestore.collection("Course_User").document(snapshot.getId());
                                                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                    }
                                                });
                                            }
                                            Intent intent=new Intent(CourseDetailActivity.this,FeedActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
                return true;
            case R.id.createAnnouncement:
                Intent intentToAddAnnouncement=new Intent(CourseDetailActivity.this,AddAnnouncementActivity.class);
                intentToAddAnnouncement.putExtra("courseID",courseIDad.getText().toString());
                startActivity(intentToAddAnnouncement);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
