package com.example.plhomework.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.plhomework.Activities.Course.CourseFeedActivity;
import com.example.plhomework.R;
import com.example.plhomework.OOPFiles.Student;
import com.example.plhomework.OOPFiles.Teacher;
import com.example.plhomework.OOPFiles.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    EditText emailText,passwordText,nameText,surnameText;
    ProgressBar progressBar;
    Button signUpButton;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Intent intent= getIntent();
        String email=intent.getStringExtra("email");
        emailText=findViewById(R.id.emailText);

        if (!email.matches(""))
         emailText.setText(email);

        passwordText=findViewById(R.id.passwordText);
        nameText=findViewById(R.id.nameText);
        surnameText=findViewById(R.id.surnameText);
        progressBar=findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        signUpButton=findViewById(R.id.button_register);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
    }
    public void registerClicked(View view){
        progressBar.setVisibility(View.VISIBLE);
        final String email=emailText.getText().toString();
        final String password=passwordText.getText().toString();
        final String name=nameText.getText().toString();
        final String surname=surnameText.getText().toString();
         if(!email.matches("") && !password.matches("") && (email.endsWith("co.edu.tr") || email.endsWith("ogr.co.edu.tr"))){

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                     //database'e user oluştur.

                    HashMap<String,String> postUser=new HashMap<>();
                    postUser.put("email",email);
                    postUser.put("password",password);
                    postUser.put("name",name);
                    postUser.put("surname",surname);
                    String uniqueID = UUID.randomUUID().toString();
                    postUser.put("userID",uniqueID);
                    User user;
                    if(email.endsWith("ogr.co.edu.tr")){
                        postUser.put("userType","student");
                         user=new Student(name,surname,email,true);
                    }
                    else{
                        postUser.put("userType","teacher");
                        user=new Teacher(name,surname,email);
                    }
                    LoginActivity.currentUser=user;

                    firebaseFirestore.collection("Users").document(uniqueID).set(postUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Toast.makeText(SignUpActivity.this,"User Created", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignUpActivity.this, CourseFeedActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        else {
            if(!(email.endsWith("co.edu.tr") || email.endsWith("ogr.co.edu.tr")) && !(email.matches("") || password.matches("") || name.matches("") || surname.matches("")) ){
                Toast.makeText(SignUpActivity.this, "Your email should end with co.edu.tr or ogr.co.edu.tr !", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(SignUpActivity.this, "Fill the spaces!", Toast.LENGTH_LONG).show();

             progressBar.setVisibility(View.GONE);
        }

    }
}
