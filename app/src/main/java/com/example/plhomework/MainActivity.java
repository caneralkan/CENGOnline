package com.example.plhomework;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
EditText txtname,txtsurname;
Button btnsend;
DatabaseReference reff;
Member member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtname=(EditText)findViewById(R.id.txtname);
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
                Toast.makeText(MainActivity.this,"Data inserted :)",Toast.LENGTH_LONG).show();
            }
        });
    }
}
