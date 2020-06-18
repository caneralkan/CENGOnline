package com.example.plhomework.Activities.Assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;

import com.example.plhomework.Activities.LoginActivity;
import com.example.plhomework.Adapters.PageAdapter;
import com.example.plhomework.Model.Assignment;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AssignmentDetailTeacherActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    PageAdapter pageAdapter;
    Intent intent;
    Assignment assignment;
    public static ArrayList<String> studentEmails,submissionTexts,submissionDates;
    public static int submissionCount=0;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_detail_teacher);
        intent=getIntent();
        assignment=(Assignment) intent.getSerializableExtra("assignment");//assignment objesini assignmentRecyclerAdapterdan alıyoruz.
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        Toolbar toolbar = findViewById(R.id.toolbarAssignmentTeacher);
        toolbar.setTitle(assignment.getAssignmentTitle());
        setSupportActionBar(toolbar);

        firebaseFirestore.collection("Course_Assignment").document(assignment.getAssignmentID()).collection("Submits").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                submissionCount= task.getResult().getDocuments().size();
            }

        });
        submissionDates=new ArrayList<>();
        submissionTexts=new ArrayList<>();
        studentEmails=new ArrayList<>();
        firebaseFirestore= FirebaseFirestore.getInstance();
        firebaseAuth= FirebaseAuth.getInstance();
        firebaseFirestore.collection("Course_Assignment").document(assignment.getAssignmentID()).collection("Submits").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.getResult().isEmpty()) {
                    for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                        Map<String, Object> data = snapshot.getData();
                        submissionTexts.add((String) data.get("assignment"));
                        submissionDates.add((String) data.get("submissionDate"));
                        studentEmails.add((String) data.get("userEmail"));
                        System.out.println("beni say");
                    }



                }
                viewPager=findViewById(R.id.viewPager);

                pageAdapter=new PageAdapter(getSupportFragmentManager(),assignment);
                viewPager.setAdapter(pageAdapter);
                tabLayout=findViewById(R.id.tabLayout);
                tabLayout.setupWithViewPager(viewPager);

            }
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.assignment_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteAssignment:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                //databasede silme işlemleri yapılacak
                                final DocumentReference documentReference=firebaseFirestore.collection("Course_Assignment").document(assignment.getAssignmentID());
                                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        documentReference.collection("Submits").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                for (DocumentSnapshot snapshot:task.getResult().getDocuments()){
                                                    documentReference.collection("Submits").document(snapshot.getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                        }
                                                    });
                                                }

                                                Toast.makeText(AssignmentDetailTeacherActivity.this, "Assignment Deleted!", Toast.LENGTH_SHORT).show();
                                                Intent intent2=new Intent(AssignmentDetailTeacherActivity.this, AssignmentActivity.class);

                                                startActivity(intent2);
                                                finish();
                                            }
                                        });
                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(AssignmentDetailTeacherActivity.this);
                builder.setMessage("Are you sure you want to delete this assignment?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return true;
            case R.id.editAssignment:

                Intent intent=new Intent(AssignmentDetailTeacherActivity.this, EditAssignmentActivity.class);
                intent.putExtra("assignment",assignment);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(LoginActivity.currentUser!=null &&  LoginActivity.currentUser.isStudent()){
            MenuItem deleteAnnouncement = menu.findItem(R.id.deleteAssignment);
            deleteAnnouncement.setVisible(false);
            MenuItem editAnnouncement = menu.findItem(R.id.editAssignment);
            editAnnouncement.setVisible(false);

        }
        return super.onPrepareOptionsMenu(menu);
    }
}
