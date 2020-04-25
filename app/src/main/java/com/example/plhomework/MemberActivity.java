package com.example.plhomework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.zip.Inflater;

public class MemberActivity extends AppCompatActivity {
EditText txtname,txtsurname;
Button btnsend;
DatabaseReference reff;
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
        if(item.getItemId()==R.id.logout){
            firebaseAuth.signOut();
            Intent intentToLogin=new Intent(MemberActivity.this,LoginActivity.class);
            startActivity(intentToLogin);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

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
                Toast.makeText(MemberActivity.this,"Data inserted :)",Toast.LENGTH_LONG).show();
            }
        });*/
    }
}
