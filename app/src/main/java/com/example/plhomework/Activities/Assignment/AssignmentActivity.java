package com.example.plhomework.Activities.Assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.plhomework.Activities.Course.CourseFeedActivity;
import com.example.plhomework.Activities.LoginActivity;
import com.example.plhomework.Activities.Message.MessageActivity;
import com.example.plhomework.Adapters.AssignmentRecyclerAdapter;
import com.example.plhomework.Model.Assignment;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class AssignmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView navName,navEmail;
    ArrayList<Assignment> assignments;
    AssignmentRecyclerAdapter assignmentRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {//loginactivity allcourses arrayini kullanarak Assignment içindeki courseID'leri karşılaştır. olanı göster
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        navigationView= findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);
        assignments=new ArrayList<>();
        navEmail=headerView.findViewById(R.id.headerEmail);
        navEmail.setText(firebaseUser.getEmail());
        navName=headerView.findViewById(R.id.headerName);
        System.out.println("2: "+LoginActivity.currentUser.getName());
        navName.setText(LoginActivity.currentUser.getName()+" "+LoginActivity.currentUser.getSurname());
        //feedRecyclerAdapter.notifyDataSetChanged();
        toolbar=findViewById(R.id.toolbarAssignment);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer_layout_assignment);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_assignments);
        navigationView.setNavigationItemSelectedListener(this);
        //aşağıdaki sorgu, allCourses içindeki courseIDlerden herhangi birine eşit assignment courseIDsi varsa , o assignmentı döndürmeye çalışıyor.
        if(!LoginActivity.allCourses.isEmpty()) {
            firebaseFirestore.collection("Course_Assignment").whereIn("courseID", LoginActivity.allCourses).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            Map<String, Object> data = documentSnapshot.getData();
                            assignments.add(new Assignment((String) data.get("assignmentID"), (String) data.get("assignmentTitle"), (String) data.get("assignmentContext"), (String) data.get("courseID"), (String) data.get("startDate"), (String) data.get("endDate")));
                        }
                        RecyclerView recyclerView = findViewById(R.id.recyclerViewAssignment);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AssignmentActivity.this));
                        assignmentRecyclerAdapter = new AssignmentRecyclerAdapter(AssignmentActivity.this, assignments);
                        recyclerView.setAdapter(assignmentRecyclerAdapter);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_logout:
                firebaseAuth.signOut();
                Intent intentToLogin=new Intent(AssignmentActivity.this,LoginActivity.class);
                startActivity(intentToLogin);
                finish();
                return false;
            case R.id.nav_messages:
                Intent intentToMessage=new Intent(AssignmentActivity.this, MessageActivity.class);
                startActivity(intentToMessage);
                finish();
                return false;
            case R.id.nav_courses:
                Intent intentToFeed=new Intent(AssignmentActivity.this, CourseFeedActivity.class);
                startActivity(intentToFeed);
                finish();
                return false;
            default :
                return false;
        }
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{

            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Connecting xml file

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.assignment_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(LoginActivity.currentUser!=null &&  LoginActivity.currentUser.isStudent()){
            MenuItem createAssignment = menu.findItem(R.id.createAssignmentMenu);
            createAssignment.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.createAssignmentMenu:
                Intent intentToAdd=new Intent(AssignmentActivity.this, AddAssignmentActivity.class);
                startActivity(intentToAdd);
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
