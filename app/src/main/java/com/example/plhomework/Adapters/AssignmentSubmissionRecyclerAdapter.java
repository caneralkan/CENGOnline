package com.example.plhomework.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plhomework.Activities.Assignment.AssignmentDetailTeacherActivity;
import com.example.plhomework.Model.Assignment;
import com.example.plhomework.R;

public class AssignmentSubmissionRecyclerAdapter extends RecyclerView.Adapter<AssignmentSubmissionRecyclerAdapter.AssignmentSubmissionRecyclerHolder> {

    Assignment assignment;
    public AssignmentSubmissionRecyclerAdapter( Assignment assignment) {
        this.assignment=assignment;


    }

    @NonNull
    @Override
    public AssignmentSubmissionRecyclerAdapter.AssignmentSubmissionRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.assignment_submission_card,parent,false);

        return new AssignmentSubmissionRecyclerAdapter.AssignmentSubmissionRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentSubmissionRecyclerAdapter.AssignmentSubmissionRecyclerHolder holder, int position) {
        holder.studentNameSurname.setText(AssignmentDetailTeacherActivity.studentEmails.get(position));
        holder.submissionText.setText(AssignmentDetailTeacherActivity.submissionTexts.get(position));
        holder.submissionDate.setText(AssignmentDetailTeacherActivity.submissionDates.get(position));
    }

    @Override
    public int getItemCount() {
        return AssignmentDetailTeacherActivity.studentEmails.size();
    }

    public class AssignmentSubmissionRecyclerHolder extends RecyclerView.ViewHolder{

        TextView studentNameSurname,submissionDate,submissionText;
        public AssignmentSubmissionRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            this.studentNameSurname=itemView.findViewById(R.id.studentNameSurname);
            this.submissionDate=itemView.findViewById(R.id.submissionDate);
            this.submissionText=itemView.findViewById(R.id.submissionText);
        }
    }
}
