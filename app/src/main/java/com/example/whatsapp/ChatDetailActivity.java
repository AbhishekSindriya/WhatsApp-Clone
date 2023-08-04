package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.whatsapp.Adapters.ChatAdapter;
import com.example.whatsapp.Models.Message;
import com.example.whatsapp.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

public class ChatDetailActivity extends AppCompatActivity {
ActivityChatDetailBinding binding;
FirebaseDatabase database;
FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
       final String senderId = auth.getUid();
        String recieveId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.userName2.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.user).into(binding.profileImage);

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        final ArrayList<Message> messages  = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messages,this,recieveId);
        binding.chatsRecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatsRecyclerView.setLayoutManager(layoutManager);
        final String senderRoom = senderId+recieveId;
        final String recieverRoom = recieveId+senderId;



        database.getReference().child("chats")
                .child(senderRoom)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messages.clear();
                                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                    Message model = snapshot1.getValue(Message.class);
                                    model.setMessageId(snapshot1.getKey());
                                    messages.add(model);
                                }
                                chatAdapter.notifyDataSetChanged();


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
    //Message sending and reciever code //
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           String message = binding.etMesssage.getText().toString();
           final Message messageModel = new Message(senderId,message);
           messageModel.setTimestamp(new Date().getTime());
           //Ye Code mene edit text ko khali krvane ke liye likha he //
            binding.etMesssage.setText("");
            database.getReference().child("chats")
                    .child(senderRoom)
                    .push()
                    .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            database.getReference().child("chats")
                                    .child(recieverRoom)
                                    .push()
                                    .setValue(messageModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                        }
                                    });
                        }
                    });

            }
        });
    }
}