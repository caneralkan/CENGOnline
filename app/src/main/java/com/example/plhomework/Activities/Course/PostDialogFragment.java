package com.example.plhomework.Activities.Course;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.plhomework.Activities.Assignment.AssignmentActivity;
import com.example.plhomework.Activities.Assignment.AssignmentDetailTeacherActivity;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class PostDialogFragment extends DialogFragment implements View.OnClickListener {
    ProgressBar progressBar;
    TextView editPost, deletePost;
    String courseID,teacherEmail;
    int position;
    Context context;
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    public PostDialogFragment(Context context, int position,String courseID,String teacherEmail) {
        this.position = position;
        this.context=context;
        this.courseID=courseID;
        this.teacherEmail=teacherEmail;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.popup_edit_delete_menu, null);
        editPost = view.findViewById(R.id.editPost);
        progressBar=view.findViewById(R.id.progressBarMenu);
        progressBar.setVisibility(View.INVISIBLE);
        deletePost = view.findViewById(R.id.deletePost);
        editPost.setOnClickListener(this);
        deletePost.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editPost://edit intent git
                Intent intent=new Intent(context,EditPostActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("courseID",courseID);
                intent.putExtra("teacherEmail",teacherEmail);
                startActivity(intent);
                ((Activity) context).finish();
                dismiss();
                break;
            case R.id.deletePost:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                progressBar.setVisibility(View.VISIBLE);
                                //Yes button clicked
                                // databasede silme işlemleri

                                firebaseFirestore.collection("Courses").whereEqualTo("courseID",courseID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (DocumentSnapshot snapshot:task.getResult().getDocuments()){
                                            DocumentReference documentReference=firebaseFirestore.collection("Courses").document(snapshot.getId());
                                            final DocumentReference postDocumentReference=documentReference.collection("Posts").document(StreamActivity.posts.get(position).getPostID());
                                            postDocumentReference.collection("Comments").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    for (DocumentSnapshot snapshot:task.getResult().getDocuments()){
                                                        DocumentReference commentDocumentReference=postDocumentReference.collection("Comments").document(snapshot.getId());
                                                        commentDocumentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                //for döngüsüyle tüm commentler silindi
                                                            }
                                                        });
                                                    }
                                                    postDocumentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            dismiss();
                                                            Intent intent2 = new Intent(context, StreamActivity.class);
                                                            intent2.putExtra("courseID", courseID);
                                                            intent2.putExtra("teacherEmail", teacherEmail);


                                                            ((Activity) context).finish();
                                                            startActivity(intent2);
                                                        }
                                                    });//post dökümanı da silindi.

                                                }
                                            });


                                        }
                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked

                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to delete this post?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;
            default:

                break;
        }
    }
}
