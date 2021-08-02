package com.project.nikhil.secfamfinal1.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.Model.User;
import com.project.nikhil.secfamfinal1.Post.FollowingAdapter;
import com.project.nikhil.secfamfinal1.R;

import java.util.ArrayList;
import java.util.List;

public class FollowerActivity extends BaseActivity {

    RelativeLayout back_btn;

    RecyclerView following_rcv;
    LinearLayoutManager mLayoutManager;
    FollowingAdapter followingAdapter;
    List<User> followingList;
    FirebaseUser firebaseUser;
    DatabaseReference userRef;

    String profileid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        Log.i("!!!Activity", "FollowerActivity");
        profileid=getIntent().getStringExtra("profileid");


        back_btn = findViewById(R.id.back_follower);
        following_rcv=findViewById(R.id.follower_rcv);
        following_rcv.setHasFixedSize(true);
        followingList=new ArrayList<>();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setItemPrefetchEnabled(true);
        following_rcv.setLayoutManager(mLayoutManager);
        //recyclerView.setRecycledViewPool(RecyclerView.RecycledViewPool);
        RecyclerView.ItemAnimator animator = following_rcv.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }


        followingAdapter = new FollowingAdapter( getApplicationContext(),followingList);
        followingAdapter.setHasStableIds(true);
        following_rcv.setAdapter(followingAdapter);

        userRef= FirebaseDatabase.getInstance().getReference().child("Users");

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid);

        reference.child("followers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds :snapshot.getChildren())
                {
                    String id=ds.getKey();
                    if(id!=null && !id.equals(profileid)) {
                        userRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                User user=snapshot.getValue(User.class);
                                followingList.add(user);
                                followingAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                    else {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}