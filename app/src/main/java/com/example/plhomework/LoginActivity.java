package com.example.plhomework;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executor;

import static java.lang.Thread.sleep;

public class LoginActivity extends AppCompatActivity {
    EditText emailText,passwordText;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    public static ArrayList<User> allUsers;
    public static ArrayList<Course> allCourses;
    public static User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Init
        firebaseAuth=FirebaseAuth.getInstance();
        emailText=findViewById(R.id.emailText);
        passwordText=findViewById(R.id.passwordText);
        //Is user already logged in?
        firebaseUser= firebaseAuth.getCurrentUser();
        firebaseFirestore=FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        allCourses=new ArrayList<>();
        allUsers=new ArrayList<>();
        fillCourseListFromFirestore();
        for (Course course:LoginActivity.allCourses){
            System.out.println("oldu mu lan:"+course.getCourseName());
        }

        if(firebaseUser!= null){
            setUserFromCurrentUser(firebaseUser.getEmail());

            System.out.println(LoginActivity.currentUser);
            Intent intent=new Intent(LoginActivity.this, FeedActivity.class);
            startActivity(intent);

            finish();
        }

    }

    public void signInClicked(View view){
        progressBar.setVisibility(View.VISIBLE);
        final String email=emailText.getText().toString();
        String password=passwordText.getText().toString();
        if(!email.matches("") && !password.matches("")) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                        setUserFromCurrentUser(email);

                    System.out.println(LoginActivity.currentUser);
                    Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_LONG).show();
                    //if(email.endsWith(""))   burda teacher/student login ayrımı yapılacak
                    Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(LoginActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();


                }
            });
        }
        else
            Toast.makeText(LoginActivity.this, "Fill the spaces!", Toast.LENGTH_LONG).show();

        progressBar.setVisibility(View.GONE);
    }

    public void signUpClicked(View view){
        progressBar.setVisibility(View.VISIBLE);
        String email=emailText.getText().toString();
        String password=passwordText.getText().toString();

        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        intent.putExtra("email",email);
        intent.putExtra("password",password);
        startActivity(intent);




        progressBar.setVisibility(View.GONE);
    }
    public User findUserFromListbyEmail(String email){//foreach loop
        for (User listUser:allUsers){
            if(email.matches(listUser.getEmail()))
                return listUser;
        }
        return null;
    }
    public boolean fillCourseListFromFirestore(){
        CollectionReference collectionReference=firebaseFirestore.collection("Courses");
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots!=null){
                    for (DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
                        Map<String,Object> data=snapshot.getData();

                        String courseName=(String) data.get("courseName");
                        String courseID=(String) data.get("courseID");
                        String teacherName=(String) data.get("teacherName");
                        String teacherSurname=(String) data.get("teacherSurname");
                        String teacherEmail=(String) data.get("teacherEmail");


                        Course course=new Course(courseName,courseID,new Teacher(teacherName,teacherSurname,teacherEmail));
                        LoginActivity.allCourses.add(course);
                        System.out.println("Course Name:"+ courseName);
                        System.out.println(LoginActivity.allCourses.get(0).getCourseID());

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                    Toast.makeText(LoginActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });
        return true;
        /*collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Toast.makeText(LoginActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(queryDocumentSnapshots!=null){
                    for (DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
                        Map<String,Object> data=snapshot.getData();

                        String courseName=(String) data.get("courseName");
                        String courseID=(String) data.get("courseID");
                        String teacherName=(String) data.get("teacherName");
                        String teacherSurname=(String) data.get("teacherSurname");
                        String teacherEmail=(String) data.get("teacherEmail");


                        Course course=new Course(courseName,courseID,new Teacher(teacherName,teacherSurname,teacherEmail));
                        LoginActivity.allCourses.add(course);
                        System.out.println("Course Name:"+ courseName);
                        System.out.println(LoginActivity.allCourses.get(0).getCourseID());
                    }
                }
            }
        });*/
    }

    public void setUserFromCurrentUser(final String email2){//this fuction get users' email and read users data from database and set user
        CollectionReference collectionReference=firebaseFirestore.collection("Users");
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot snapshot:task.getResult().getDocuments()){
                    Map<String,Object> data=snapshot.getData();

                    String email=(String) data.get("email");

                    if(email.matches(email2)){

                        System.out.println(email);
                        String name=(String) data.get("name");
                        String surname=(String) data.get("surname");
                        String userType=(String) data.get("userType");
                        if(userType.matches("student")){
                            LoginActivity.currentUser=new Student(name,surname,email,true);

                        }
                        else if(userType.matches("teacher")){
                            LoginActivity.currentUser=new Teacher(name,surname,email);
                        }


                    }
                }
            }
        });
        /*
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e!=null){
                    Toast.makeText(LoginActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(queryDocumentSnapshots!=null){
                    for (DocumentSnapshot snapshot:queryDocumentSnapshots.getDocuments()){
                        Map<String,Object> data=snapshot.getData();


                        String email=(String) data.get("email");

                        if(email.matches(firebaseUser.getEmail())){
                            String name=(String) data.get("name");
                            String surname=(String) data.get("surname");
                            String userType=(String) data.get("userType");
                            if(userType.matches("student")){
                                LoginActivity.currentUser=new Student(name,surname,email,true);

                            }
                            else if(userType.matches("teacher")){
                                LoginActivity.currentUser=new Teacher(name,surname,email,false);
                            }


                        }
                    }
                }
            }
        });*/
    }
}
