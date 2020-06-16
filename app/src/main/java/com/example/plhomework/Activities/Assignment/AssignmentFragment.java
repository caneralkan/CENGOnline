package com.example.plhomework.Activities.Assignment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.plhomework.OOPFiles.Assignment;
import com.example.plhomework.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignmentFragment extends Fragment {
    TextView assignmentTitle,startDate,dueDate,context,courseIDAsDetail;
    Assignment assignment;
    public AssignmentFragment(Assignment assignment) {
        this.assignment=assignment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assignment, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        courseIDAsDetail=view.findViewById(R.id.courseIDAsDetailTeacher);
        assignmentTitle=view.findViewById(R.id.assignmentTitleTextTeacher);
        startDate=view.findViewById(R.id.assignmentStartDateTeacher);
        dueDate=view.findViewById(R.id.assignmentDueDateTeacher);
        context=view.findViewById(R.id.assignmentContextDetailTeacher);
        assignmentTitle.setText(assignment.getAssignmentTitle());
        startDate.setText(assignment.getStartDate());
        dueDate.setText(assignment.getEndDate());
        context.setText(assignment.getDescription());
        courseIDAsDetail.setText(assignment.getCourseID());
    }
}
