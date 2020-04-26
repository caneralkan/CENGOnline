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

public class FeedActivity extends AppCompatActivity {
    //FeedActivity
    EditText txtname,txtsurname;
    Button btnsend;
    Member member;
    FirebaseAuth firebaseAuth;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        firebaseAuth=FirebaseAuth.getInstance();
       /* txtname=(EditText)findViewById(R.id.txtname);
        txtsurname=(EditText)findViewById(R.id.txtsurname);
        btnsend=(Button)findViewById(R.id.btnsend);
        member=new Member();
        reff= FirebaseDatabase.getInstance().getReference().child("member");


        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member.setName(txtname.getText().toString().trim());
                member.setSurname(txtsurname.getText().toString().trim());

                reff.push().setValue(member);
                Toast.makeText(FeedActivity.this,"Data inserted :)",Toast.LENGTH_LONG).show();
            }
        });*/
    }
}
