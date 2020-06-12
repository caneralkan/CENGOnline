package com.example.plhomework.Activities.Assignment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.plhomework.Activities.FeedActivity;
import com.example.plhomework.Activities.LoginActivity;
import com.example.plhomework.Activities.Message.MessageActivity;
import com.example.plhomework.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AssignmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    public static NavigationView navigationView;
    Toolbar toolbar;
    private TextView navName,navEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        navigationView= findViewById(R.id.nav_view);
        View headerView=navigationView.getHeaderView(0);

        navEmail=headerView.findViewById(R.id.headerEmail);
        navEmail.setText(firebaseUser.getEmail());
        navName=headerView.findViewById(R.id.headerName);
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
                Intent intentToFeed=new Intent(AssignmentActivity.this, FeedActivity.class);
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
}
