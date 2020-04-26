package com.example.plhomework;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText emailText,passwordText;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Init
        firebaseAuth=FirebaseAuth.getInstance();
        emailText=findViewById(R.id.emailText);
        passwordText=findViewById(R.id.passwordText);
        //Is user already logged in?
        FirebaseUser firebaseUser= firebaseAuth.getCurrentUser();
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        if(firebaseUser!= null){
            Intent intent=new Intent(LoginActivity.this, FeedActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void signInClicked(View view){
        progressBar.setVisibility(View.VISIBLE);
        String email=emailText.getText().toString();
        String password=passwordText.getText().toString();
        if(!email.matches("") && !password.matches("")) {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
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
}
