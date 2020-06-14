package com.example.plhomework.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plhomework.OOPFiles.Assignment;
import com.example.plhomework.R;

import java.util.ArrayList;

public class AssignmentRecyclerAdapter extends RecyclerView.Adapter<AssignmentRecyclerAdapter.AssignmentRecyclerHolder> {
    ArrayList<Assignment> assignments;
    Context context;

    public AssignmentRecyclerAdapter(Context context,ArrayList<Assignment> assignments) {
        this.context=context;
        this.assignments = assignments;
    }

    @NonNull
    @Override
    public AssignmentRecyclerAdapter.AssignmentRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.assignment_card,parent,false);

        return new AssignmentRecyclerAdapter.AssignmentRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssignmentRecyclerAdapter.AssignmentRecyclerHolder holder, int position) {
        holder.assignmentTitle.setText(assignments.get(position).getAssignmentTitle());
        holder.assignmentContext.setText(assignments.get(position).getDescription());
        holder.endDate.setText(assignments.get(position).getEndDate());


        //aşağıdaki kod mesajlara tıklanırsa ne olacağıdır. düzelt
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return assignments.size();
    }
    public class AssignmentRecyclerHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        ItemClickListener itemClickListener;
        TextView assignmentTitle,assignmentContext,endDate;
        public AssignmentRecyclerHolder(@NonNull View itemView) {
            super(itemView);
            this.assignmentTitle= itemView.findViewById(R.id.assignmentTitle);
            this.assignmentContext= itemView.findViewById(R.id.assignmentContext);
            this.endDate= itemView.findViewById(R.id.dueDateAssignment);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClickListener(v,getLayoutPosition());
        }
        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener=ic;
        }
    }
}
