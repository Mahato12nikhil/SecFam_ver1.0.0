package com.project.nikhil.secfamfinal.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal.R;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class ChatHomeActivity extends AppCompatActivity {
    //EditText chatET;
    private FirebaseAuth mAuth;
    private Toolbar mToolbar;
    private TextView chat;
    private ViewPager mViewPager;
    //  private SectionsPagerAdapter mSectionsPagerAdapter;
    private RelativeLayout L1;
    private DatabaseReference mUserRef;
    private RecyclerView mConvList;

    private DatabaseReference mConvDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mUsersDatabase;
    // private FirebaseAuth mAuth;
    String userid;
    ChatHomeViewAdapter chatFragmentAdapter;
    private String mCurrent_user_id;
    private ArrayList<String> Userid;
    private ArrayList<String> lastMessage;

   // private View mMainView;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_home);
        mAuth = FirebaseAuth.getInstance();
        mConvList = (RecyclerView) findViewById(R.id.conv_list);
        mCurrent_user_id =FirebaseAuth.getInstance().getCurrentUser().getUid();

        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(mCurrent_user_id);

        mConvDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(mCurrent_user_id);
        mUsersDatabase.keepSynced(true);
        Userid = new ArrayList<>();
        lastMessage = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatHomeActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

//        mConvList.setHasFixedSize(true);
        mConvList.setLayoutManager(linearLayoutManager);

                mConvDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Userid.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            userid = ds.getKey();
                            lastMessage.add("hi");


                            Userid.add(userid);
                            Query lastMessageQuery = mMessageDatabase.child(userid).limitToLast(1);

                        /* lastMessageQuery.addValueEventListener(new ValueEventListener() {
                             @Override
                             public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                             {
                                 String data = dataSnapshot.child("message").getValue().toString();
                                 lastMessage.add(data);
                             }

                             @Override
                             public void onCancelled(@NonNull DatabaseError databaseError) {

                             }
                         });
        */
                        }
                        if (Userid.size() > 0) {
                            chatFragmentAdapter = new ChatHomeViewAdapter(getApplicationContext(), Userid, lastMessage,mCurrent_user_id);

                            mConvList.setAdapter(chatFragmentAdapter);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


      //  return mMainView;
        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor( ContextCompat.getColor(this,R.color.whitecolor)); //status bar or the time bar at the top
        }
        //  sendimg=findViewById(R.id.chat_send_btn);
        //chatET=findViewById(R.id.chat_message_view);

        if (mAuth.getCurrentUser() != null) {


            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();


    }


    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {

            // mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

        }

    }


}

