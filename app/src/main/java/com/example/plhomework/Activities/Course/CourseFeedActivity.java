package com.example.plhomework.Activities.Course;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plhomework.Activities.Assignment.AssignmentActivity;
import com.example.plhomework.Activities.LoginActivity;
import com.example.plhomework.Activities.Message.MessageActivity;
import com.example.plhomework.Adapters.FeedRecyclerAdapter;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CourseFeedActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //CourseFeedActivity
    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    public static NavigationView navigationView;
    Toolbar toolbar;
    TextView navName, navEmail;
    public FeedRecyclerAdapter feedRecyclerAdapter;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedRecyclerAdapter = new FeedRecyclerAdapter(CourseFeedActivity.this);
        recyclerView.setAdapter(feedRecyclerAdapter);

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        navEmail = headerView.findViewById(R.id.headerEmail);
        navEmail.setText(firebaseUser.getEmail());
        navName = headerView.findViewById(R.id.headerName);
        navName.setText(LoginActivity.currentUser.getName() + " " + LoginActivity.currentUser.getSurname());
        //feedRecyclerAdapter.notifyDataSetChanged();
        toolbar = findViewById(R.id.toolbarFeed);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_courses);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                firebaseAuth.signOut();
                Intent intentToLogin = new Intent(CourseFeedActivity.this, LoginActivity.class);
                startActivity(intentToLogin);
                finish();
                return false;
            case R.id.nav_messages:
                Intent intentToMessage = new Intent(CourseFeedActivity.this, MessageActivity.class);
                startActivity(intentToMessage);
                finish();
                return false;
            case R.id.nav_assignments:
                Intent intentToAssignment = new Intent(CourseFeedActivity.this, AssignmentActivity.class);
                startActivity(intentToAssignment);
                return false;
            default:
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (LoginActivity.currentUser != null && LoginActivity.currentUser.isStudent()) {
            MenuItem addCourse = menu.findItem(R.id.AddCourseMenu);
            addCourse.setVisible(false);

        } else if (LoginActivity.currentUser != null && !LoginActivity.currentUser.isStudent()) {
            MenuItem addCourse = menu.findItem(R.id.enrollMenu);
            addCourse.setVisible(false);
        }
        return true;
    }

    //Connecting menu to this activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Connecting xml file

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.cengonline_options_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AddCourseMenu:
                Intent intentToAddCourse = new Intent(CourseFeedActivity.this, AddCourseActivity.class);
                startActivity(intentToAddCourse);
                return true;
            case R.id.enrollMenu:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Join class");
                alert.setMessage("Ask your teacher for the class code, then enter it here.");

                final EditText input = new EditText(this);
                input.setBackgroundResource(R.drawable.popup_edittext_style);
                input.setHint("Enter class code here");
                input.setPadding(65,50,0,0);
                alert.setView(input);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (LoginActivity.allCourses.contains(input.getText().toString())|| input.getText().toString().matches("")) {
                            if(input.getText().toString().matches(""))
                                Toast.makeText(CourseFeedActivity.this, "Verify class code!", Toast.LENGTH_LONG).show();
                            else
                            Toast.makeText(CourseFeedActivity.this, "You are already enrolled to that course!", Toast.LENGTH_LONG).show();

                        } else {
                            CollectionReference collectionReference = firebaseFirestore.collection("Courses");
                            collectionReference.whereEqualTo("courseID", input.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (queryDocumentSnapshots.isEmpty()) {

                                        Toast.makeText(CourseFeedActivity.this, "No courses found with that ID. Please check your class code!", Toast.LENGTH_LONG).show();
                                    }
                                    for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                                        Map<String, Object> data = snapshot.getData();
                                        String course = (String) data.get("courseName");
                                        System.out.println(course);

                                        //kursa kayıt olma.
                                        CollectionReference collectionReference = firebaseFirestore.collection("Course_User");
                                        HashMap<String, String> enrollUser = new HashMap<>();
                                        enrollUser.put("email", firebaseUser.getEmail());
                                        enrollUser.put("courseID", input.getText().toString());
                                        enrollUser.put("date", Timestamp.now().toDate().toString());
                                        collectionReference.add(enrollUser).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                                LoginActivity.allCourses.add(input.getText().toString());
                                                Toast.makeText(CourseFeedActivity.this, "Enrolled", Toast.LENGTH_LONG).show();
                                                feedRecyclerAdapter.notifyDataSetChanged();
                                            }
                                        });


                                    }
                                }
                            });

                        }
                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                alert.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
