package com.example.plhomework.Activities.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.plhomework.Activities.Assignment.AssignmentActivity;
import com.example.plhomework.Activities.FeedActivity;
import com.example.plhomework.Activities.LoginActivity;
import com.example.plhomework.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MessageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    public static NavigationView navigationView;
    Toolbar toolbar;
    private TextView navName,navEmail;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
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
        toolbar=findViewById(R.id.toolbarFeed);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer_layout_message);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        navigationView.setCheckedItem(R.id.nav_messages);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_logout:
                firebaseAuth.signOut();
                Intent intentToLogin=new Intent(MessageActivity.this,LoginActivity.class);
                startActivity(intentToLogin);
                finish();
                return false;
            case R.id.nav_courses:
                Intent intentToFeed=new Intent(MessageActivity.this, FeedActivity.class);
                startActivity(intentToFeed);
                finish();
                return false;
            case R.id.nav_assignments:
                Intent intentToAssignment=new Intent(MessageActivity.this, AssignmentActivity.class);
                startActivity(intentToAssignment);
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
