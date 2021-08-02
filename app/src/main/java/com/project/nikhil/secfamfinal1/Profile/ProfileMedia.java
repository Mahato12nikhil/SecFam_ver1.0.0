package com.project.nikhil.secfamfinal1.Profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import com.project.nikhil.secfamfinal1.Post.ExoPlayerActivity;
import com.project.nikhil.secfamfinal1.Post.Post;
import com.project.nikhil.secfamfinal1.Post.ShowAlIPostmages;
import com.project.nikhil.secfamfinal1.Post.VideoExample;
import com.project.nikhil.secfamfinal1.R;
import com.project.nikhil.secfamfinal1.postView.PostViewImage;
import com.project.nikhil.secfamfinal1.postView.PostViewVideo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProfileMedia extends Fragment {

    private List<Post> postList;
    private List<Post> tempPostList = new ArrayList<>();
    private List<Post> newPostList = new ArrayList<>();
    RecyclerView recyclerView;
    ProfileMediaAdapter mPostAdapter;
    private String proId;
    ShimmerFrameLayout shimmerFrameLayout;
    private FirebaseUser firebaseUser;
    private SwipeRefreshLayout swipephoto;
    GridLayoutManager mLayoutManager;
    private int currentPost, totalPost, scrolledPost;
    Boolean isScrolling = false, isMaxData = false;
    String last_node = "", last_Key;
    ProgressBar loading_progress;
    TextView new_post;
    private static final int postPerPageLimit = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_media, container, false);
        Log.i("!!!Fragment", "ProfileMedia");
        proId = ((ProfilePostActivity) getActivity()).getId();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.rcv);
        mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        shimmerFrameLayout = view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmerAnimation();
        swipephoto = view.findViewById(R.id.swipephoto);
        swipephoto.setColorSchemeResources(R.color.bg_light);
        loading_progress = view.findViewById(R.id.loading_progress);
        new_post = view.findViewById(R.id.new_post);

        recyclerView.setHasFixedSize(true);
        postList = new ArrayList<>();
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        mPostAdapter = new ProfileMediaAdapter(postList, getContext(), post -> {
            if (post.isPostAvailable()) {
                if (post.getType().equals("image")) {
                    startActivity(new Intent(getActivity(), PostViewImage.class).putExtra("POST_ID", post.getPostid()));
                } else if (post.getType().equals("video")) {
                    startActivity(new Intent(getActivity(), PostViewVideo.class).putExtra("POST_ID", post.getPostid()));
                }
            } else {
                Toast.makeText(getActivity(), "The post is no longer available. Deleted by the Owner.", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(mPostAdapter);
        getFirstKey();
        swipephoto.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPost();
            }
        });
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
                postList.addAll(0, newPostList);
                newPostList.clear();
                mPostAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
            }
        });
        SetDeletePostListener();
        return view;
    }

    private void getNrPosts() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PersonalPosts").child(proId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    //if (post.getPublisher().equals(profileid)){
                    i++;
                    // }
                }
                //posts.setText(""+i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void myPhotos() {
        Query query = FirebaseDatabase.getInstance().getReference().child("PersonalPosts")
                .child(proId).orderByChild("isMedia").equalTo(true);
       /* reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(links.size()>0)
                    links.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Post post = null;
                    try {
                        post = snapshot.getValue(Post.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                  if(post!=null && post.getType()!=null) {
                      if (post.getType().equals("image") || post.getType().equals("video"))
                          links.add(post);
                  }

                }
                Collections.reverse(links);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        postList.clear();
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                Log.i("!!!!", "onChildAddedMedia");
                Post post = snapshot.getValue(Post.class);
                if (post != null && post.getPostid() != null && (post.getType().equals("image") || post.getType().equals("video"))) {
                    postList.add(0, post);
                    mPostAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                postList.remove(snapshot.getValue(Post.class));
                mPostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void SetNewPostListener() {
        Query postQuery = FirebaseDatabase.getInstance().getReference()
                .child("MediaPosts")
                .child(proId)
                .orderByKey()
                .limitToLast(1);

        postQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Post post = snapshot.getValue(Post.class);
                        assert post != null;
                        if (post.getPostid() != null) {
                            if (post.getPostid().equals(last_Key)) {
                                isMaxData = true;
                            }
                            for (int i = 0; i < postList.size(); i++) {
                                if (post.equals(postList.get(i))) {
                                    return;
                                }
                            }
                            newPostList.add(0, post);
                        }
                        //Show new post button if new post available
                        if (new_post.getVisibility() != View.VISIBLE) {
                            new_post.setVisibility(View.VISIBLE);
                            BaseActivity.ShowAnimationFadeOut(new_post);
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("MediaPosts").child(proId);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Post deletedPost = dataSnapshot.getValue(Post.class);
                assert deletedPost != null;
                //if post owner delete post else inactive
                if (deletedPost.getPublisher().equals(firebaseUser.getUid())) {
                    postList.remove(deletedPost);
                    mPostAdapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < postList.size(); i++) {
                        if (postList.get(i).equals(deletedPost)) {
                            postList.get(i).setPostAvailable(false);
                        }
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
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

        Query postQuery = FirebaseDatabase.getInstance().getReference().child("MediaPosts").child(proId)
                .orderByKey().limitToLast(postPerPageLimit);

        postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!swipephoto.isRefreshing()) {
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

    public void getMorePost() {
        if (!isMaxData && postList.size() > 0) {
            loading_progress.setVisibility(View.VISIBLE);
            last_node = ((Post) postList.get(postList.size() - 1)).getPostid();
            if (!last_node.equals(last_Key)) {
                postList.remove(postList.size() - 1);
            } else {
                Toast.makeText(getActivity(), "END", Toast.LENGTH_LONG).show();
                last_node = "end";
            }
            Query postQuery = FirebaseDatabase.getInstance().getReference().child("MediaPosts").child(proId)
                    .orderByKey().endAt(last_node).limitToLast(postPerPageLimit);

            postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
                .child("MediaPosts")
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


}
