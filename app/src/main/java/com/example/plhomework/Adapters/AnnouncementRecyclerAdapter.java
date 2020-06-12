package com.example.plhomework.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plhomework.Activities.Announcement.AnnouncementDetailActivity;
import com.example.plhomework.OOPFiles.Announcement;
import com.example.plhomework.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class AnnouncementRecyclerAdapter extends RecyclerView.Adapter<AnnouncementRecyclerAdapter.AnnouncementRecyclerHolder> {
    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
    ArrayList<Announcement> announcements;

    Context context;
    public AnnouncementRecyclerAdapter(Context context, ArrayList<Announcement> announcements) {
        this.announcements=announcements;
        this.context=context;

    }

    @NonNull
    @Override
    public AnnouncementRecyclerAdapter.AnnouncementRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.announcement_cardview,parent,false);

        return new AnnouncementRecyclerAdapter.AnnouncementRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AnnouncementRecyclerAdapter.AnnouncementRecyclerHolder holder, int position) {
        holder.annContext.setText(announcements.get(position).getContext());
        holder.annTitle.setText(announcements.get(position).getTitle());
        holder.annDate.setText(announcements.get(position).getDate());

       holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
                String courseIDCD=announcements.get(position).getCourseID();
                Intent intent =new Intent(context, AnnouncementDetailActivity.class);
                intent.putExtra("announcementID",announcements.get(position).getID());
                intent.putExtra("position",position);
                intent.putExtra("courseID",courseIDCD);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return announcements.size();
    }


    public class AnnouncementRecyclerHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView annImage;
        TextView annTitle,annContext,annDate;
        ItemClickListener itemClickListener;

        public AnnouncementRecyclerHolder(@NonNull View itemView) {

            super(itemView);
            annDate=itemView.findViewById(R.id.announcementDate);
            annImage=itemView.findViewById(R.id.announcementImage);
            annTitle=itemView.findViewById(R.id.announcementTitle);
            annContext=itemView.findViewById(R.id.announcementContext);
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
