package com.example.plhomework.Activities.Message;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plhomework.Activities.Assignment.AssignmentActivity;
import com.example.plhomework.Activities.Course.CourseFeedActivity;
import com.example.plhomework.Activities.LoginActivity;
import com.example.plhomework.Adapters.MessageRecyclerAdapter;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    public static NavigationView navigationView;
    Toolbar toolbar;
    EditText messageTo,messageText;
    ArrayList<String> messages;
    ArrayList<String> messageFrom;
    ArrayList<String> dates;
    String userID;
    private TextView navName,navEmail;

    public MessageRecyclerAdapter messageRecyclerAdapter;
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
        messageTo=findViewById(R.id.sentTo);
        messageText=findViewById(R.id.messageText);
        messages=new ArrayList<>();
        messageFrom=new ArrayList<>();
        dates=new ArrayList<>();

        final FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        firebaseFirestore.collection("Users").whereEqualTo("email",firebaseUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                    Map<String, Object> data = snapshot.getData();
                    userID = (String) data.get("userID");
                }
                firebaseFirestore.collection("Users").document(userID).collection("Messages").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.getResult().isEmpty()){//koleksiyonu yoksa

                            for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                                Map<String, Object> data = snapshot.getData();
                                messages.add((String) data.get("messageText"));
                                messageFrom.add((String) data.get("sender"));
                                dates.add((String) data.get("date"));
                            }
                            RecyclerView recyclerView=findViewById(R.id.recyclerViewMessage);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                            messageRecyclerAdapter=new MessageRecyclerAdapter(MessageActivity.this,messages,messageFrom,dates);
                            recyclerView.setAdapter(messageRecyclerAdapter);

                        }

                    }
                });

            }
        });






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
                Intent intentToFeed=new Intent(MessageActivity.this, CourseFeedActivity.class);
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
    public void sendMessage(View view){
        String emailTo=messageTo.getText().toString();
        final String emailText=messageText.getText().toString();
        System.out.println(emailTo);
        if(!emailTo.matches("") && !emailText.matches("")){
            firebaseFirestore.collection("Users").whereEqualTo("email",emailTo).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(!queryDocumentSnapshots.getDocuments().isEmpty()) {
                        for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                            Map<String, Object> data = snapshot.getData();
                            String userID = (String) data.get("userID");
                            Map<String, Object> put = new HashMap<>();
                            put.put("sender", firebaseUser.getEmail());
                            put.put("messageText", emailText);
                            put.put("date", new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss", Locale.getDefault()).format(new Date()));
                            firebaseFirestore.collection("Users").document(userID).collection("Messages").add(put);
                            Toast.makeText(MessageActivity.this, "Message sent!", Toast.LENGTH_LONG).show();
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());
                            overridePendingTransition(0, 0);
                        }
                    }
                    else{

                        Toast.makeText(MessageActivity.this, "Email is not found!", Toast.LENGTH_LONG).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(MessageActivity.this, "Email is wrong!", Toast.LENGTH_LONG).show();
                }
            });
        }else {
            Toast.makeText(MessageActivity.this, "Fill the spaces!", Toast.LENGTH_LONG).show();
        }
    }
}
