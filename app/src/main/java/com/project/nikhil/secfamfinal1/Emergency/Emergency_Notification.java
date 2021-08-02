package com.project.nikhil.secfamfinal1.Emergency;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import de.hdodenhof.circleimageview.CircleImageView;

import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Emergency_Notification extends Fragment {
    SwipeRefreshLayout emergency_noti_swipe;
    Boolean isScrollin = false;
    private DatabaseReference mConvDatabase;
    private RecyclerView usersconv_list;
    ArrayList<String> received;
    private DatabaseReference mMessageDatabase, mAllMessages;
    private DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;
    private String userid;
    private String lastEmergencyKey, lastNode, topKey;
    private int currentPost, totalPost, scrolledPost;
    int a = 1;
    Boolean isMaxDat = false;

    private String LastKey, firstKey;
    private EmergencyNotificationAdapter messageReceivedAdapter;
    private String mCurrent_user_id;
    private ArrayList<Emergency_data> noteList;
    private ArrayList<String> lastMessage;
    private FirebaseUser fuser;
    Boolean lastRead = false;
    CircleImageView ntRam;
    RelativeLayout JustIncase;
    TextView victimNtRam;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_emergency__notification, container, false);
        received = new ArrayList<>();
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        emergency_noti_swipe = view.findViewById(R.id.emergency_noti_swipe);
        usersconv_list = view.findViewById(R.id.older_notification_list);
        mCurrent_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);
        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Emergency");
        mConvDatabase.keepSynced(true);
        noteList = new ArrayList<>();
        lastMessage = new ArrayList<>();
        //latest notification declaration

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        usersconv_list.setHasFixedSize(true);
        usersconv_list.setLayoutManager(linearLayoutManager);
        //usersconv_list.addItemDecoration(new DividerItemDecoration(getContext(), 0));
        messageReceivedAdapter = new EmergencyNotificationAdapter(getContext(), noteList);
        usersconv_list.setAdapter(messageReceivedAdapter);
        // findiflastNotificationRead(mConvDatabase);
        //readNotification();
        findFirstKey(mConvDatabase);
        //findTopKey();

       /* usersconv_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentPost = linearLayoutManager.getChildCount();
                totalPost = linearLayoutManager.getItemCount();
                scrolledPost = ((LinearLayoutManager) (recyclerView.getLayoutManager())).findFirstVisibleItemPosition();
                if (!isScrollin && (currentPost + scrolledPost >= totalPost)) {
                    // isScrolling = false;
                    readNotification();
                    isScrollin = true;
                    //  Toast.makeText(getContext(),""+postAdapter.getLastPostId(),Toast.LENGTH_LONG).show();
                }
            }
        });*/

       /* JustIncase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),MapVideoActivity.class);
                //   intent.putExtra("pushID",lastEmergencyKey);
                // if(LastKey!=null)
                if(LastKey!=null) {
                    intent.putExtra("pushId",lastEmergencyKey );
                    getContext().startActivity(intent);
                }else{
                    ((EmergencyActivity)getActivity()).setSnackBar(bhugy,"please wait...");
                    //   setSnackBar();
                }
            }
        });*/
       /* DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(usersconv_list.getContext(),
                linearLayoutManager.getOrientation());*/


        emergency_noti_swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readNotification();
            }
        });
        return view;
    }

    private void stopRefresh(){
        if (emergency_noti_swipe.isRefreshing()){
            emergency_noti_swipe.setRefreshing(false);
        }
    }
    private void findFirstKey(DatabaseReference mConvDatabase) {
        Query query = mConvDatabase.child("receive").orderByKey().limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    firstKey = snapshot.getKey();
                readNotification();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }



    private void readNotification() {
        Query query;
       // if (!isMaxDat) {
           /* if (TextUtils.isEmpty(lastNode)) {
                query = FirebaseDatabase.getInstance().getReference()
                        .child("Emergency")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("receive")
                        .orderByKey()
                        .limitToLast(6);
            } else {
                query = mConvDatabase.child("Emergency").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("receive")
                        .orderByKey()
                        .endAt(lastNode)
                        .limitToLast(6);
            }*/
        query = FirebaseDatabase.getInstance().getReference()
                .child("Emergency")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("receive")
                .orderByKey()
                .limitToLast(5);
        query.keepSynced(true);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    stopRefresh();
                    noteList.clear();
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Emergency_data emergency_data = snapshot.getValue(Emergency_data.class);
                            if (emergency_data.getStatus().equals("receive"))
                                noteList.add(emergency_data);
                        }
                        Collections.reverse(noteList);
                        /*lastNode = noteList.get(noteList.size() - 1).getPushId();
                        if (!lastNode.equals(firstKey)) {
                            noteList.remove(noteList.size() - 1);
                        } else {
                            // Toast.makeText(getContext(), "END", Toast.LENGTH_LONG).show();
                            lastNode = "end";
                            //Toast.makeText(getContext(),""+topKey,Toast.LENGTH_LONG).show();
                            isMaxDat = true;
                        }*/
                        messageReceivedAdapter.notifyDataSetChanged();
                    }
                    isScrollin = false;
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    stopRefresh();
                    isScrollin = false;
                }
            });
        //}
    }



    private void updateNotification(String lastKey) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(lastKey);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = null;
                String image = null;
                try {
                    name = dataSnapshot.child("name").getValue().toString();
                    image = dataSnapshot.child("image").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                victimNtRam.setText(name + " seeks your help");
                try {
                    Glide.with(getContext()).load(image).into(ntRam);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void findTopKey() {
        Query query = mConvDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("receive").orderByKey().limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    topKey = snapshot.child("pushId").getValue().toString();
                    // lastEmergencyKey=snapshot.child("pushId").getValue().toString();
                }
                final int delay = 1000;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (topKey != null) {
                            readNotification();
                            // updateNotification(LastKey);
                            handler.removeCallbacks(this);
                        } else {
                            handler.postDelayed(this, delay);
                        }
                    }
                }, delay);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void findiflastNotificationRead(DatabaseReference mConvDatabase) {
        Query query = mConvDatabase.orderByKey().limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lastRead = dataSnapshot.child("seen").getValue(Boolean.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    private void findlastKey(DatabaseReference mConvDatabase) {

        Query query = mConvDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("receive")
                .orderByKey()
                .limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                LastKey = dataSnapshot.child("sender")
                        .getValue().toString();


                lastEmergencyKey = dataSnapshot.child("pushId").getValue().toString();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                }

                final int delay = 1000;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (LastKey != null) {

                            updateNotification(LastKey);

                            handler.removeCallbacks(this);

                        } else {
                            handler.postDelayed(this, delay);
                        }
                    }
                }, delay);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
