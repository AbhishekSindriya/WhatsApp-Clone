package com.example.whatsapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.ChatDetailActivity;
import com.example.whatsapp.Models.Users;
import com.example.whatsapp.R;
import com.example.whatsapp.databinding.ActivityChatDetailBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.viewHolder> {

    ArrayList<Users> list;
    Context context;

    public UsersAdapter(ArrayList<Users> list, Context context) {
       this.list = list;
       this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_user,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
     Users users = list.get(position);
     Picasso.get().load(users.getProfilePic()).placeholder(R.drawable.user).into(holder.image);
     holder.userName.setText(users.getUserName());
     //Ye code last message show krvane ke liye likha he //
        FirebaseDatabase.getInstance().getReference().child("chats")
                .child(FirebaseAuth.getInstance().getUid() + users.getUserId())
                .orderByChild("timestamp")
                .limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChildren()){
                            for(DataSnapshot snapshot1 : snapshot.getChildren()){holder.lastMessage.
                                    setText(snapshot1.child("message").getValue().toString());
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                    }
                });





     holder.itemView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent = new Intent(context, ChatDetailActivity.class);
             intent.putExtra("userId",users.getUserId());
             intent.putExtra("profilePic",users.getProfilePic());
             intent.putExtra("userName",users.getUserName());
             context.startActivity(intent);
         }
     });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class viewHolder extends RecyclerView.ViewHolder{
     ImageView image;
     TextView userName, lastMessage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userNameList);
            lastMessage = itemView.findViewById(R.id.lastMessage);
        }
    }


}
