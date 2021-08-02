package com.project.nikhil.secfamfinal1.Profile;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.Post.Post;
import com.project.nikhil.secfamfinal1.Post.PostAdapter;
import com.project.nikhil.secfamfinal1.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileAllPost extends Fragment {

    String last_node="", last_Key;
    private PostAdapter mPostAdapter;
    private RecyclerView recyclerView;
    private List<Object> postList;
    private List<Object> tempPostList = new ArrayList<>();
    private List<Object> newPostList = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private ShimmerFrameLayout shimmerFrameLayout;
    private RelativeLayout relativeLayout;
    private SwipeRefreshLayout swipephoto;
    FirebaseUser firebaseUser;
    private int currentPost, totalPost, scrolledPost;
    Boolean isScrolling = false, isMaxData = false;
    private String proId;
    ProgressBar loading_progress;
    TextView new_post;
    private static final int postPerPageLimit = 10;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_all_post, container, false);
        Log.i("!!!Fragment", "ProfileAllPost");
        // relativeLayout = view.findViewById(R.id.hghghj);
        swipephoto = view.findViewById(R.id.swipephoto);
        swipephoto.setColorSchemeResources(R.color.bg_light);
        recyclerView = view.findViewById(R.id.rcVg);
        // noInternetImage = view.findViewById(R.id.noInternetImage);
        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        loading_progress = view.findViewById(R.id.loading_progress);
        new_post = view.findViewById(R.id.new_post);

        proId = ((ProfilePostActivity) getActivity()).getId();

        recyclerView.setHasFixedSize(true);
        postList = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setItemPrefetchEnabled(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setRecycledViewPool(RecyclerView.RecycledViewPool);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }


        mPostAdapter = new PostAdapter(getContext(), ProfileAllPost.this,postList);
        mPostAdapter.setHasStableIds(true);
        recyclerView.setAdapter(mPostAdapter);
        getFirstKey();
        swipephoto.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPost();
            }
        });
       /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentPost = mLayoutManager.getChildCount();
                totalPost = mLayoutManager.getItemCount();
                scrolledPost = mLayoutManager.findFirstVisibleItemPosition();

                if (!isScrolling && (currentPost + scrolledPost >= totalPost)) {
                    readMyPosts();
                    isScrolling = true;
                }
            }
        });*/

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentPost = mLayoutManager.getChildCount();
                totalPost = mLayoutManager.getItemCount();
                scrolledPost = mLayoutManager.findFirstVisibleItemPosition();

                if (!isScrolling && (currentPost + scrolledPost >= totalPost)) {
                    getMorePost();
                    isScrolling = true;
                }
            }
        });

        new_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.ShowAnimationFadeIn(new_post);
                new_post.setVisibility(View.GONE);
                postList.addAll(0,newPostList);
                newPostList.clear();
                mPostAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
            }
        });
        SetDeletePostListener();
        return view;
    }


    public void SetNewPostListener() {
        Query postQuery = FirebaseDatabase.getInstance().getReference()
                .child("PersonalPosts")
                .child(proId)
                .orderByKey()
                .limitToLast(1);

        postQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Post post = snapshot.getValue(Post.class);
                        if (post != null && post.getPostid() != null) {
                            if (post.getPostid().equals(last_Key)) {
                                isMaxData = true;
                            }
                            for (int i=0; i<postList.size(); i++){
                                if (post.equals(postList.get(i))){
                                    return;
                                }
                            }
                           // postList.add(0,post);
                            newPostList.add(0,post);

                            //Show new post button if new post available
                            if (new_post.getVisibility() != View.VISIBLE) {
                                new_post.setVisibility(View.VISIBLE);
                                BaseActivity.ShowAnimationFadeOut(new_post);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void SetDeletePostListener() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PersonalPosts").child(proId);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Post deletedPost = dataSnapshot.getValue(Post.class);
                assert deletedPost != null;
                //if post owner delete post else make it inactive
                if (deletedPost.getPublisher().equals(firebaseUser.getUid())){
                    postList.remove(deletedPost);
                    mPostAdapter.notifyDataSetChanged();
                }else {
                    for (int i = 0; i < postList.size(); i++) {
                        if (postList.get(i).equals(deletedPost)) {
                            ((Post) postList.get(i)).setPostAvailable(false);
                        }
                    }
                }

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    public void getPost() {
        if (new_post.getVisibility() == View.VISIBLE) {
            new_post.setVisibility(View.GONE);
            BaseActivity.ShowAnimationFadeIn(new_post);
        }
        newPostList.clear();
        isMaxData = false;
        isScrolling = true;
        recyclerView.removeAllViews();
        postList.clear();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmerAnimation();
        Query postQuery = FirebaseDatabase.getInstance().getReference().child("PersonalPosts").child(proId)
                .orderByKey().limitToLast(postPerPageLimit);

        postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!swipephoto.isRefreshing()){
                    SetNewPostListener();
                }
                swipephoto.setRefreshing(false);
                tempPostList.clear();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Post post = snapshot.getValue(Post.class);
                        if (post != null && post.getPostid() != null) {
                            if (post.getPostid().equals(last_Key)) {
                                isMaxData = true;
                            }
                            tempPostList.add(post);
                        }
                    }
                    if (tempPostList.size() > 0) {
                        Collections.reverse(tempPostList);
                        postList.addAll(tempPostList);
                        mPostAdapter.notifyDataSetChanged();
                    }
                }

                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                isScrolling = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                swipephoto.setRefreshing(false);
                isScrolling = false;
                Toast.makeText(getActivity(), "Problem in loading more...", Toast.LENGTH_LONG).show();
            }
        });
    }
    public void getMorePost(){
        if (!isMaxData &&  postList.size()>0) {
            loading_progress.setVisibility(View.VISIBLE);
            last_node = ((Post) postList.get(postList.size() - 1)).getPostid();
            if (!last_node.equals(last_Key)) {
                postList.remove(postList.size() - 1);
            }else {
                //Toast.makeText(getActivity(), "END", Toast.LENGTH_LONG).show();
                last_node = "end";
            }
            Query postQuery = FirebaseDatabase.getInstance().getReference().child("PersonalPosts").child(proId)
                    .orderByKey().endAt(last_node).limitToLast(postPerPageLimit);
            postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.i("!!!!","onDataChange");
                    loading_progress.setVisibility(View.GONE);
                    tempPostList.clear();
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post post = snapshot.getValue(Post.class);
                            if (post != null && post.getPostid() != null) {
                                if (post.getPostid().equals(last_Key)) {
                                    isMaxData = true;
                                }
                                tempPostList.add(post);
                            }
                        }
                        if (tempPostList.size() > 0) {
                            Collections.reverse(tempPostList);
                            postList.addAll(tempPostList);
                            mPostAdapter.notifyDataSetChanged();
                        }

                    }
                    isScrolling = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    loading_progress.setVisibility(View.GONE);
                    isScrolling = false;
                }

            });
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
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    last_Key = ds.getKey();
                    //
                }
                // readMyPosts();
                getPost();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                /// displayImage();

            }
        });

    }

     /*public void getPost() {
        isMaxData = false;
        isScrolling = false;
        recyclerView.removeAllViews();
        postList.clear();
        Query postQuery = FirebaseDatabase.getInstance().getReference().child("PersonalPosts").child(proId)
                .orderByKey().limitToLast(5);

        postQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                swipephoto.setRefreshing(false);
                isScrolling = true;
                Post post = dataSnapshot.getValue(Post.class);
                Log.i("!!!!","onChildAdded: "+post.getDescription());
                if (post != null && post.getPostid() != null) {
                    if (post.getPostid().equals(last_Key)) {
                        isMaxData = true;
                    }
                    postList.add(0,post);
                    mPostAdapter.notifyDataSetChanged();
                    shimmerFrameLayout.stopShimmerAnimation();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
                isScrolling =false;

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                postList.remove(dataSnapshot.getValue(Post.class));
                mPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                swipephoto.setRefreshing(false);
                isScrolling = false;
                Toast.makeText(getActivity(), "Problem loading more images...", Toast.LENGTH_LONG).show();
            }
        });
    }*/
    /*public void getMorePost(){

        if (!isMaxData &&  postList.size()>0) {
            loading_progress.setVisibility(View.VISIBLE);
            last_node = ((Post) postList.get(postList.size() - 1)).getPostid();
            if (!last_node.equals(last_Key)) {
                postList.remove(postList.size() - 1);
            }else {
                Toast.makeText(getActivity(), "END", Toast.LENGTH_LONG).show();
                last_node = "end";
            }
            int postListSize = postList.size();
            Query postQuery = FirebaseDatabase.getInstance().getReference().child("PersonalPosts").child(proId)
                    .orderByKey().endAt(last_node).limitToLast(5);
            postQuery.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    loading_progress.setVisibility(View.GONE);
                    Post post = snapshot.getValue(Post.class);
                    Log.i("!!!!","onChildAddedMore: "+post.getDescription());
                    if (post != null && post.getPostid() != null) {
                        if (post.getPostid().equals(last_Key)) {
                            isMaxData = true;
                        }
                        postList.add(postListSize,post);
                        mPostAdapter.notifyDataSetChanged();
                    }
                    isScrolling = false;
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    postList.remove(snapshot.getValue(Post.class));
                    mPostAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable  String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    loading_progress.setVisibility(View.GONE);
                    isScrolling = false;
                }
            });

        }
    }*/
/*
    private void readMyPosts() {
        //     Toast.makeText(getContext(),"called"+itemPos++,Toast.LENGTH_LONG).show();
        Query query;
        if (!isMaxData) {
            if (TextUtils.isEmpty(last_node))
                query = FirebaseDatabase.getInstance().getReference()
                        .child("PersonalPosts")
                        .child(proId)
                        .orderByKey()
                        .limitToLast(10);
            else
                query = FirebaseDatabase.getInstance().getReference()
                        .child("PersonalPosts")
                        .child(proId)
                        .orderByKey()
                        .endAt(last_node)
                        .limitToLast(10);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {

                    postList = new ArrayList<>();
                    postList.clear();
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Post post = snapshot.getValue(Post.class);
                            if (post != null && post.getPostid() != null) {
                                if (post.getPostid().equals(last_Key)) {
                                    isMaxData = true;
                                }
                            }
                            postList.add(post);
                        }
                    }
                    if (postList.size()>0) {
                        Collections.reverse(postList);
                        last_node = ((Post) postList.get(postList.size() - 1)).getPostid();
                        if (!last_node.equals(last_Key)) {
                            postList.remove(postList.size() - 1);
                        } else {
                            Toast.makeText(getActivity(), "END", Toast.LENGTH_LONG).show();
                            last_node = "end";
                        }
                        mPostAdapter.addAll(postList, false);
                        isScrolling = false;
                        shimmerFrameLayout.stopShimmerAnimation();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }
                    Log.i("!!!!AllPost","PostCount"+postList.size());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    // swipephoto.setRefreshing(false);
                    isScrolling = false;


                }
            });

        } else {
            isMaxData = true;
        }
    }
*/



}
