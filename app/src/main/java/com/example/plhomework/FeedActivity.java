package com.example.plhomework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {
    //FeedActivity
    Button btnSend;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    ArrayList<String> courseName;
    ArrayList<String> courseID;
    static FeedRecyclerAdapter feedRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        //System.out.println(firebaseUser.getEmail());//emaile göre tekrar öğrenci öğretmen ayrımı yapılabilir. ona göre dinamik layout?
        firebaseFirestore=FirebaseFirestore.getInstance();
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
       // System.out.println(LoginActivity.allCourses.get(0).getCourseName());
        feedRecyclerAdapter=new FeedRecyclerAdapter();
        recyclerView.setAdapter(feedRecyclerAdapter);
        feedRecyclerAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        if(LoginActivity.currentUser!=null &&  LoginActivity.currentUser.isStudent()){
            MenuItem addCourse = menu.findItem(R.id.AddCourseMenu);
            addCourse.setVisible(false);

        }
        return true;
    }
    //Connecting menu to this activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Connecting xml file

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.cengonline_options_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutMenu:
                firebaseAuth.signOut();
                Intent intentToLogin=new Intent(FeedActivity.this,LoginActivity.class);
                startActivity(intentToLogin);
                finish();
                return true;
            case  R.id.AddCourseMenu:
                Intent intentToAddCourse=new Intent(FeedActivity.this,AddCourseActivity.class);
                startActivity(intentToAddCourse);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }



    }
}
