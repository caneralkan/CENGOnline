package com.example.plhomework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class FeedActivity extends AppCompatActivity {
    //FeedActivity
    EditText txtName,txtSurname;
    Button btnSend;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        //System.out.println(firebaseUser.getEmail());//emaile göre tekrar öğrenci öğretmen ayrımı yapılabilir. ona göre dinamik layout?
        txtName=findViewById(R.id.txtname);
        txtSurname=findViewById(R.id.txtsurname);
        btnSend=findViewById(R.id.btnsend);

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
        if(item.getItemId()==R.id.logout){//if logout clicked
            firebaseAuth.signOut();
            Intent intentToLogin=new Intent(FeedActivity.this,LoginActivity.class);
            startActivity(intentToLogin);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
