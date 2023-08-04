package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
//import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.whatsapp.Adapters.ChatAdapter;
import com.example.whatsapp.Models.Message;
import com.example.whatsapp.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<Message> message = new ArrayList<>();

        final String senderId = FirebaseAuth.getInstance().getUid();
        binding.userName2.setText("Friends Group");

         final ChatAdapter adapter = new ChatAdapter(message,this);
         binding.chatsRecyclerView.setAdapter(adapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatsRecyclerView.setLayoutManager(layoutManager);

//ye code recyler view me msg show krvane ke liye likha he//
//        final String senderRoom = senderId;
          database.getReference().child("Group Chat")
                          .addValueEventListener(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                  message.clear();
                                  for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                      Message model = dataSnapshot.getValue(Message.class);
                                      message.add(model);
                                  }
                                  adapter.notifyDataSetChanged();
                              }

                              @Override
                              public void onCancelled(@NonNull DatabaseError error) {

                              }
                          });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = binding.etMesssage.getText().toString();
                final Message model = new Message(senderId,message);
                model.setTimestamp(new Date().getTime());
                //final com.example.whatsapp.Models.Message messageModel = new com.example.whatsapp.Models.Message(senderId,message);

                binding.etMesssage.setText("");

                database.getReference().child("Group Chat")
                        .push()
                        .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(GroupChatActivity.this, "Message Send", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}