package com.project.nikhil.secfamfinal.Profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.project.nikhil.secfamfinal.Post.HomeFragment;
import com.project.nikhil.secfamfinal.Post.Post;
import com.project.nikhil.secfamfinal.Post.PostAdapter;
import com.project.nikhil.secfamfinal.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileAllPost extends Fragment {

    String last_node,last_Key;
    private PostAdapter mPostAdapter;
    private RecyclerView recyclerView;
    private List<Object> postList;
    private LinearLayoutManager mLayoutManager;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RelativeLayout relativeLayout;
    private SwipeRefreshLayout swipephoto;
    FirebaseUser firebaseUser;
    private int currentPost, totalPost, scrolledPost;
    Boolean isScrolling = false,isMaxData=false;
    private  String proId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_all_post, container, false);

        // relativeLayout = view.findViewById(R.id.hghghj);
        swipephoto = view.findViewById(R.id.swipephoto);
        recyclerView = view.findViewById(R.id.rcVg);
        // noInternetImage = view.findViewById(R.id.noInternetImage);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
       proId=((ProfilePostActivity) getActivity()).getId();

        recyclerView.setHasFixedSize(true);
        postList=new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setItemPrefetchEnabled(true);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setRecycledViewPool(RecyclerView.RecycledViewPool);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }


        shimmerFrameLayout.startShimmerAnimation();
        mPostAdapter = new PostAdapter( getContext(), ProfileAllPost.this);
        mPostAdapter.setHasStableIds(true);



        recyclerView.setAdapter(mPostAdapter);
        getFirstKey();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);


                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){}

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentPost = mLayoutManager.getChildCount();
                totalPost = mLayoutManager.getItemCount();
                scrolledPost = mLayoutManager.findFirstVisibleItemPosition();

                if (!isScrolling && (currentPost + scrolledPost >=totalPost)) {


                    readMyPosts();

                    isScrolling=true;


                }


            }
        });




        return view;
    }

    private void readMyPosts() {
        //     Toast.makeText(getContext(),"called"+itemPos++,Toast.LENGTH_LONG).show();
        Query query;
        if (!isMaxData) {


            if(TextUtils.isEmpty(last_node))
                query = FirebaseDatabase.getInstance().getReference()
                        .child("PersonalPosts")

                        .child(proId)
                        .orderByKey()
                        .limitToLast(10);

            else
                query=FirebaseDatabase.getInstance().getReference().child("PersonalPosts")
                        .child(proId)
                        .orderByKey()
                        .endAt(last_node)
                        .limitToLast(10);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {



                    postList=new ArrayList<>();


                    postList.clear();

                    if(dataSnapshot.hasChildren()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                            Post post = snapshot.getValue(Post.class);

                            if(post!=null && post.getPostid()!=null)

                            {  if (post.getPostid().equals(last_Key)) {

                                isMaxData = true;
                            }
                            }
                            postList.add(post);




                        }


                    }
                    Collections.reverse(postList);

                    last_node = ((Post) postList.get(postList.size() - 1)).getPostid();
                    if (!last_node.equals(last_Key)) {

                        postList.remove(postList.size() - 1);
                    } else {
                        Toast.makeText(getContext(), "END", Toast.LENGTH_LONG).show();
                        last_node = "end";
                    }


                    mPostAdapter.addAll(postList,false);


                    isScrolling = false;

                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);


                }



                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // swipephoto.setRefreshing(false);
                    isScrolling=false;


                }
            });

        }
        else {
            isMaxData=true;
        }
    }
    private void getFirstKey() {

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("PersonalPosts")
                .child(proId)
                .orderByKey()
                .limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    last_Key = ds.getKey();
                    //
                }

                readMyPosts();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                /// displayImage();

            }
        });

    }



}
