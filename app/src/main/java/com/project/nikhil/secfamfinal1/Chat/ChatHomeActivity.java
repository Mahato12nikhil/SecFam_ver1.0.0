package com.project.nikhil.secfamfinal1.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.Post.PostActivity;
import com.project.nikhil.secfamfinal1.R;
import com.project.nikhil.secfamfinal1.utils.RefListenerLink;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatHomeActivity extends BaseActivity {
    ImageView icon_bck;
    private RecyclerView mChatList;
    ChatHomeViewAdapter chatListAdapter;
    private ArrayList<Chat> chatList;
   // private ArrayList<Chat> removedList;
    private ChildEventListener childEventListener;
    private Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_home);
        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.whitecolor)); //status bar or the time bar at the top
        }
        icon_bck = (ImageView) findViewById(R.id.icon_bck);
        icon_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mChatList = (RecyclerView) findViewById(R.id.chat_list);
        chatList = new ArrayList<>();
        //removedList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mChatList.setHasFixedSize(true);
        mChatList.setLayoutManager(linearLayoutManager);

        chatListAdapter = new ChatHomeViewAdapter(getApplicationContext(), chatList, myid, (id, name) -> {
            Intent public_reg = new Intent(this, ConversationActivity.class);
            public_reg.putExtra("id", id);
            public_reg.putExtra("user_name", name);
            startActivity(public_reg);
        });
        mChatList.setAdapter(chatListAdapter);

        query = FirebaseDatabase.getInstance().getReference().child("ChatList").child(myid).orderByChild("timestamp");
        query.keepSynced(true);

    }

    private void getUserDetails(Chat chat) {
        DatabaseReference mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(chat.getId());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String image = dataSnapshot.child("image").getValue(String.class);
                String status = dataSnapshot.child("isOnline").getValue(String.class);
                chat.setName(name);
                chat.setImage(image);
                chat.setStatus(status);
                chatListAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        mUsersDatabase.addValueEventListener(eventListener);
        chat.setReference(mUsersDatabase);
        chat.setListener(eventListener);
    }

    private void setListener(){
        chatList.clear();
        /*for (int i = 0; i < 20; i++){
            chatList.add(0,new Chat("jWwfwzPtIBTXPDWOEBsgVVFrCv52","last message",System.currentTimeMillis(),1,false));
        }*/
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("!!!!!", "Child Added");
                if (snapshot.child("id").exists()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    chatList.add(0, chat);
                    getUserDetails(chatList.get(0));
                    //chatListAdapter.notifyDataSetChanged();
                }

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.i("!!!!!", "Child Changed");
                if (snapshot.child("id").exists()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chatList.size() == 0) {
                        chatList.add(chat);
                        //Call Listener for getting user Name, image & status
                        getUserDetails(chatList.get(0));
                    } else if (chatList.get(0).getId().equals(chat.getId())){
                        chatList.get(0).setTimestamp(chat.getTimestamp());
                        chatList.get(0).setLastMessage(chat.getLastMessage());
                        chatList.get(0).setUnreadMessageCount(chat.getUnreadMessageCount());
                        chatList.get(0).setTyping(chat.isTyping());
                    }else{
                        for (int i = 0; i< chatList.size(); i++) {
                            if (chatList.get(i).equals(chat)) {
                                if (chat.lastMessage.equals(chatList.get(i).lastMessage)){
                                    //If Typing
                                    chatList.get(i).setTyping(chat.isTyping());
                                }else {
                                    //If Received new
                                    chatList.get(i).getReference().removeEventListener(chatList.get(i).getListener());
                                    chatList.remove(chatList.get(i));
                                    chatList.add(0, chat);
                                    //Call Listener for getting user Name, image & status
                                    getUserDetails(chatList.get(0));
                                }
                                break;
                            }
                        }

                    }
                    chatListAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("id").exists()) {
                    Chat chat = snapshot.getValue(Chat.class);
                   // removedList.add(chat);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        query.addChildEventListener(childEventListener);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //Remove deleted conversation in conversation activity
        /*if (removedList.size() > 0) {
            for (Chat c : removedList) {
                chatList.remove(c);
            }
            removedList.clear();
        }*/
        // query.addChildEventListener(childEventListener);
        setListener();
        chatListAdapter.notifyDataSetChanged();//call for reactivate listener in Adapter
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeAllChatListener();
        query.removeEventListener(childEventListener);
    }

    public void removeAllChatListener() {
        for (int i = 0; i < chatList.size(); i++) {
            chatList.get(i).getReference().removeEventListener(chatList.get(i).getListener());
        }
    }

    @Override
    public void onDestroy() {
        //chatListAdapter.removeAllListener();
        // query.removeEventListener(childEventListener);
        if (!isTaskRoot()) {
            PostActivity.selectHome();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            startActivity(new Intent(this, PostActivity.class));
        }
        super.onBackPressed();
    }

}

