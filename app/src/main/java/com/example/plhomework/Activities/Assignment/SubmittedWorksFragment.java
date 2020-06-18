package com.example.plhomework.Activities.Assignment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.plhomework.Adapters.AssignmentSubmissionRecyclerAdapter;
import com.example.plhomework.Model.Assignment;
import com.example.plhomework.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubmittedWorksFragment extends Fragment {
    private AssignmentSubmissionRecyclerAdapter assignmentRecyclerAdapter;
    private Assignment assignment;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    private ArrayList<String> submissionTexts,submissionDates,studentEmails;
        public SubmittedWorksFragment(Assignment assignment ) {
            this.assignment=assignment;

        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View rootView= inflater.inflate(R.layout.fragment_submitted_works, container, false);
        TextView submitCount=rootView.findViewById(R.id.submitCount);
        submitCount.setText(String.valueOf(AssignmentDetailTeacherActivity.studentEmails.size()));
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewAssignmentSubmission);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        assignmentRecyclerAdapter = new AssignmentSubmissionRecyclerAdapter(assignment);
        recyclerView.setAdapter(assignmentRecyclerAdapter);
        return rootView;
    }
}
