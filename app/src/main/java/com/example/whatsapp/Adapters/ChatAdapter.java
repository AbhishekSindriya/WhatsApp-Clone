package com.example.whatsapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsapp.GroupChatActivity;
import com.example.whatsapp.Models.Message;
import com.example.whatsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatAdapter extends RecyclerView.Adapter{
   ArrayList<Message> messages;

   Context context;


    public ChatAdapter(ArrayList<Message> messages, Context context, String recId) {
        this.messages = messages;
        this.context = context;
        this.recId = recId;
    }

    int SENDER_SEND_TYPE = 1;
    int RECIEVER_SEND_TYPE =2;
    String recId;




    public ChatAdapter(ArrayList<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_SEND_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender,parent,false);
            return new senderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_reciever, parent, false);
            return new recieveViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//       Message message = messages.get(position);
//
//       holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//           @Override
//           public boolean onLongClick(View view) {
//               new AlertDialog.Builder(context)
//                       .setTitle("Delete")
//                       .setMessage("Are you sure you want to delete this message")
//                       .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                           @Override
//                           public void onClick(DialogInterface dialogInterface, int i) {
//                               FirebaseDatabase database = FirebaseDatabase.getInstance();
//                               String senderRoom = FirebaseAuth.getInstance().getUid()+recId;
//                               database.getReference().child("chats").child(senderRoom)
//                                       .child(message.getMessageId())
//                                       .setValue(null);
//                           }
//                       }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                           @Override
//                           public void onClick(DialogInterface dialogInterface, int i) {
//                               dialogInterface.dismiss();
//                           }
//                       }).show();
//               return false;
//           }
//       });
//       if(holder.getClass()==senderViewHolder.class){
//           ((senderViewHolder)holder).senderMsg.setText(message.getMessage());
//       }else {
//           ((recieveViewHolder)holder).recieverMsg.setText(message.getMessage());
//       }
        Message message = messages.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this message")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                                database.getReference().child("chats").child(senderRoom)
                                        .child(message.getMessageId())
                                        .setValue(null);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                return false;
            }
        });

        if (holder.getItemViewType() == SENDER_SEND_TYPE) {
            senderViewHolder senderHolder = (senderViewHolder) holder;
            senderHolder.senderMsg.setText(message.getMessage());
        } else if (holder.getItemViewType() == RECIEVER_SEND_TYPE) {
            recieveViewHolder recieverHolder = (recieveViewHolder) holder;
            recieverHolder.recieverMsg.setText(message.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if(messages!=null) {
            return messages.size();
        }else{
            return 0;
        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (messages != null && position < messages.size() && FirebaseAuth.getInstance().getUid() != null && FirebaseAuth.getInstance().getUid().equals(messages.get(position))){
//            return SENDER_SEND_TYPE;
//        }else
//            return RECIEVER_SEND_TYPE;
//        if(messages.get(position).equals(FirebaseAuth.getInstance().getUid()))
//
//            return SENDER_SEND_TYPE;
//        else
//            return RECIEVER_SEND_TYPE;
//    }


    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_SEND_TYPE;
        }
        else {

            return RECIEVER_SEND_TYPE;
        }

    }

    public class recieveViewHolder extends RecyclerView.ViewHolder {
        TextView recieverMsg,recieverTime;
        public recieveViewHolder(@NonNull View itemView) {
            super(itemView);
            recieverMsg = itemView.findViewById(R.id.recieverText);
            recieverTime = itemView.findViewById(R.id.recieverTime);
        }
    }
    public class senderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg,senderTime;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);

        }
    }
}
