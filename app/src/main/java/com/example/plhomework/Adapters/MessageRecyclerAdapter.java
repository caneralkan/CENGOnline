package com.example.plhomework.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.plhomework.Activities.Course.CourseDetailActivity;
import com.example.plhomework.Activities.LoginActivity;
import com.example.plhomework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.MessageRecyclerHolder>{
    ArrayList<String> messages;
    ArrayList<String> messageFrom;
    ArrayList<String> dates;

    Context context;
    public MessageRecyclerAdapter(Context context,ArrayList<String> messages, ArrayList<String> messageFrom, ArrayList<String> dates) {
        this.context=context;
        this.messages = messages;
        this.messageFrom = messageFrom;
        this.dates = dates;
    }

    @NonNull
    @Override

    public MessageRecyclerAdapter.MessageRecyclerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.message_card,parent,false);

        return new MessageRecyclerAdapter.MessageRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecyclerAdapter.MessageRecyclerHolder holder, int position) {

        holder.message.setText(messages.get(position));
        holder.fromEmail.setText(messageFrom.get(position));
        holder.date.setText(dates.get(position));


        //aşağıdaki kod mesajlara tıklanırsa ne olacağıdır. düzelt
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {
            }
        });
    }

    @Override
    public int getItemCount() {//burası patlayabilir. hiç mesaj gözükmezse buraya bak
        return messages.size();
    }
    public class MessageRecyclerHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        //tasarladığın card'a göre değişkenleri oluştur.
        ItemClickListener itemClickListener;
        TextView fromEmail,date,message;
        public MessageRecyclerHolder(@NonNull View itemView) {
            super(itemView);

            this.fromEmail=itemView.findViewById(R.id.fromEmail);
            this.date=itemView.findViewById(R.id.dateCard);
            this.message=itemView.findViewById(R.id.messageCard);
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
