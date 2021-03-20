package com.project.nikhil.secfamfinal.Notification;

import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal.Post.Post;
import com.project.nikhil.secfamfinal.Post.PostAdapter;
import com.project.nikhil.secfamfinal.R;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class NotificationFragment extends Fragment {

    private String last_node;
    private  String last_Key = "";
    private RecyclerView recyclerView;
    Boolean isMaxData=false,isScrolling=false;
    private NotificationAdapter notificationAdapter;
    private List<Notification_Model> notificationList;
    private int currentPost, totalPost, scrolledPost;
    FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        notificationList = new ArrayList<>();
        //shimmerFrameLayout.startShimmerAnimation();
        notificationAdapter = new NotificationAdapter(getContext());
      //  notificationAdapter.setHasStableIds(true);

        recyclerView.setAdapter(notificationAdapter);

       firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                }
                // isScrolling = true;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentPost = mLayoutManager.getChildCount();
                totalPost = mLayoutManager.getItemCount();
                scrolledPost = mLayoutManager.findFirstVisibleItemPosition();

                if (!isScrolling && (currentPost + scrolledPost >= totalPost)) {

                    // isScrolling = false;

                    readPosts();

                    isScrolling = true;
                    //  Toast.makeText(getContext(),""+postAdapter.getLastPostId(),Toast.LENGTH_LONG).show();

                }


            }
        });

       getFirstKey();

        return view;
    }

    private void readNotifications(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                notificationList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Notification_Model notification = snapshot.getValue(Notification_Model.class);
                    notificationList.add(notification);
                }

                Collections.reverse(notificationList);
                notificationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void readPosts() {
        Toast.makeText(getContext(), "id: called", Toast.LENGTH_LONG).show();

        Query query;
        if (!isMaxData) {


            if (TextUtils.isEmpty(last_node))
                query = FirebaseDatabase.getInstance().getReference().child("Notifications").child(firebaseUser.getUid())
                        .orderByKey()
                        .limitToLast(15);

            else
                query = FirebaseDatabase.getInstance().getReference()
                        .child("Notifications").child(firebaseUser.getUid())
                        .orderByKey()
                        .endAt(last_node)
                        .limitToLast(15);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    //notificationList = new ArrayList<>();
                    notificationList.clear();

                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                            Notification_Model post = snapshot.getValue(Notification_Model.class);

                            Toast.makeText(getContext(),""+last_Key,Toast.LENGTH_LONG).show();


                         if(post.getNotificationID()!=null)  {
                         if (post.getNotificationID().equals(last_Key)) {

                               isMaxData = true;
                            }}
                          /*  for (String id : followingList) {


                                if (post.getPublisher().equals(id)) {

                                    if (post.getPostid().equals(last_Key)) {

                                        isMaxData = true;
                                    }


                                }

                            }*/
                            notificationList.add(post);


                        }
                        //Collections.sort(postList);
                        Collections.reverse(notificationList);

                        if(notificationList.size()!=0) {
                            last_node = ((Notification_Model) notificationList.get(notificationList.size() - 1)).getNotificationID();
                            System.out.println("*****++++++"+last_node+".."+notificationList.size());
                           if(last_node!=null){
                            if (!last_node.equals(last_Key)) {


                                notificationList.remove(notificationList.size() - 1);
                            } else {
                                Toast.makeText(getContext(), "END", Toast.LENGTH_LONG).show();
                                last_node = "end";
                            }}

                            notificationAdapter.addAll(notificationList);


                            isScrolling = false;

                            //shimmerFrameLayout.stopShimmerAnimation();
                           // shimmerFrameLayout.setVisibility(View.GONE);
                        }
                        else {
                            try {
                                Toast.makeText(getContext(),"Follow someone to see their posts",Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                           // shimmerFrameLayout.stopShimmerAnimation();
                            //shimmerFrameLayout.setVisibility(View.GONE);
                        }


                    } else {
                        isMaxData = true;
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // swipephoto.setRefreshing(false);
                    isScrolling = false;


                }
            });

        } else {

        }


    }


    private void getFirstKey() {

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Notifications")
                .child(firebaseUser.getUid())
                .orderByKey()
                .limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    last_Key = ds.getKey();
                    //
                    Toast.makeText(getContext(),""+last_Key,Toast.LENGTH_LONG).show();
                }



                //checkFollowing();
               // readPosts();

                if(last_Key!=null){
                    readPosts();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
              //  displayImage();

            }
        });

    }

}