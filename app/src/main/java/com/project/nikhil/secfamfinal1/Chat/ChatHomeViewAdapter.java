package com.project.nikhil.secfamfinal1.Chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.R;
import com.project.nikhil.secfamfinal1.utils.RefListenerLink;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatHomeViewAdapter extends RecyclerView.Adapter<ChatHomeViewAdapter.ChatHolder> {
    ArrayList<Chat> chatList;

    Context context;
    String mCurrentUserId;
    HashMap<Integer, RefListenerLink> listenerHashMap;

    interface OnClickListener {
        void onClick(String id, String name);
    }

    OnClickListener onClickListener;

    public ChatHomeViewAdapter(Context context, ArrayList<Chat> chatList, String mCurrentUserId, OnClickListener onClickListener) {
        this.context = context;
        this.chatList = chatList;
        this.mCurrentUserId = mCurrentUserId;
        this.onClickListener = onClickListener;
        listenerHashMap = new HashMap<>();
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_chatlist_view, parent, false);
        return new ChatHomeViewAdapter.ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
       // getNameAndPhoto(position, holder.user_single_name, holder.user_single_image, holder.status, chatList.get(position).getId());

        holder.user_single_name.setText(chatList.get(position).getName());
        Glide.with(context).load(chatList.get(position).getImage()).placeholder(R.drawable.man_placeholder).into(holder.user_single_image);
        if (chatList.get(position).getStatus().equals("online")) {
            holder.status.setVisibility(View.VISIBLE);
        } else {
            holder.status.setVisibility(View.GONE);
        }

        holder.chattt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //deleteConversation(Userid.get(position));
                return true;
            }
        });

        holder.bindPosition(holder.chattt, chatList.get(position));
    }

    ////////////////////////////////****////////////////////////////
    private void deleteConversation(String id) {
        FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrentUserId).child(id).removeValue();
        FirebaseDatabase.getInstance().getReference().child("ChatList").child(mCurrentUserId).child(id).removeValue();
    }
    ////////////////////////////////////////////////////////////


    private void getNameAndPhoto(int position, final TextView user_single_name, final CircleImageView user_single_image, final ImageView ivStatus, String s) {
        DatabaseReference mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(s);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String name = dataSnapshot.child("name").getValue(String.class);
                String image = dataSnapshot.child("image").getValue(String.class);
                String status = dataSnapshot.child("isOnline").getValue(String.class);
                user_single_name.setText(name);
                Glide.with(context).load(image).placeholder(R.drawable.man_placeholder).into(user_single_image);
                if (status.equals("online")) {
                    ivStatus.setVisibility(View.VISIBLE);
                } else {
                    ivStatus.setVisibility(View.GONE);
                }
                Log.i("!!!!", "listener called");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mUsersDatabase.addValueEventListener(eventListener);
        listenerHashMap.put(position, new RefListenerLink(mUsersDatabase, eventListener));
    }


    /*public void removeAllListener() {
        Object key[] = listenerHashMap.keySet().toArray();
        for (int i = 0; i < listenerHashMap.size(); i++) {
            RefListenerLink link = listenerHashMap.get(key[i]);
            link.getReference().removeEventListener(link.getListener());
        }

    }
*/
    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        CircleImageView user_single_image;
        TextView user_single_name;
        ImageView status;
        TextView unreadMessageCount, last_message_time;
        TextView last_chat, tv_typing;
        RelativeLayout chattt;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            user_single_image = itemView.findViewById(R.id.user_single_image);
            user_single_name = itemView.findViewById(R.id.user_single_name);
            last_message_time = itemView.findViewById(R.id.last_message_time);
            status = itemView.findViewById(R.id.status);
            last_chat = itemView.findViewById(R.id.last_chat);
            tv_typing = itemView.findViewById(R.id.tv_typing);
            unreadMessageCount = itemView.findViewById(R.id.unreadMessageCount);
            chattt = itemView.findViewById(R.id.chattt);


            if (user_single_name.getText().toString().length() > 20) {
                user_single_name.setText(user_single_name.getText().toString().substring(0, 20) + "...");
            }

        }

        @SuppressLint("SetTextI18n")
        public void bindPosition(RelativeLayout chatItem, Chat chat) {
            chatItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("!!!!!!", "Clicked");
                    onClickListener.onClick(chat.getId(), user_single_name.getText().toString());
                }
            });

            if (chat.isTyping()) {
                last_chat.setVisibility(View.GONE);
                tv_typing.setVisibility(View.VISIBLE);
            } else {
                last_chat.setVisibility(View.VISIBLE);
                tv_typing.setVisibility(View.GONE);
            }
            // Last Message
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            last_message_time.setText(dateFormat.format(new Date(chat.getTimestamp())));
            last_chat.setText(chat.getLastMessage());
            if (last_chat.getText().toString().length() > 29) {
                last_chat.setText(last_chat.getText().toString().substring(0, 29) + "...");
            }
            if (chat.getUnreadMessageCount() > 0) {
                unreadMessageCount.setVisibility(View.VISIBLE);
                unreadMessageCount.setText(String.valueOf(chat.getUnreadMessageCount()));
            } else {
                unreadMessageCount.setVisibility(View.GONE);
            }
        }
    }

}
