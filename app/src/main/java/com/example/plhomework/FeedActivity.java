package com.example.plhomework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class FeedActivity extends AppCompatActivity {
    //FeedActivity
    Button btnSend;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    static FeedRecyclerAdapter feedRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedRecyclerAdapter=new FeedRecyclerAdapter(FeedActivity.this);
        recyclerView.setAdapter(feedRecyclerAdapter);
        //feedRecyclerAdapter.notifyDataSetChanged();

        for (Course courseP:LoginActivity.currentUser.courses){
            System.out.println("abinin kursları2: "+ courseP.getCourseName());
        }
        CollectionReference collectionReference=firebaseFirestore.collection("Users");
        collectionReference.whereEqualTo("name","ogrenc").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot snapshot:task.getResult().getDocuments()){
                    Map<String,Object> data=snapshot.getData();
                    String name= (String) data.get("password");
                    System.out.println("namemiz: "+name);
                }

                System.out.println("");
            }
        });


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
            case R.id.enrollMenu:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);

                alert.setTitle("Enroll To Course");
                alert.setMessage("Enter Course ID");

                final EditText input = new EditText(this);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Course course=LoginActivity.retrieveCoursebyCourseID(input.getText().toString());
                        if(course==null){
                            Toast.makeText(FeedActivity.this,"No courses found with that ID. Please check your Course ID!",Toast.LENGTH_LONG).show();
                        }
                        else {
                            if (LoginActivity.currentUser.isStudent()) {
                                LoginActivity.currentUser.enrollCourse(course);
                                course.addStudentToCourse((Student) LoginActivity.currentUser);
                            } else {
                                course.setTeacher((Teacher) LoginActivity.currentUser);
                            }
                            feedRecyclerAdapter.notifyDataSetChanged();

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
