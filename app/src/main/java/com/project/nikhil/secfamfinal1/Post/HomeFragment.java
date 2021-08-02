package com.project.nikhil.secfamfinal1.Post;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;
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
import com.project.nikhil.secfamfinal1.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private int currentPage = 0;
    private NativeAdsManager fbNativeManager;
    private Map<String, String> uLists;

    private List<Object> postList;
    private List<Object> tempPostList = new ArrayList<>();
    private List<Object> newPostList = new ArrayList<>();
    private int currentPost, totalPost, scrolledPost;
    ShimmerFrameLayout shimmerFrameLayout;
    final int ITEM_LOAD_COUNT = 10;
    private static final int TOTAL_ITEM_EACH_LOAD = 10;
    private int n = 0;
    private BroadcastReceiver broadcastReceiver = null;
    int total_item = 0, last_visible_item;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<Post> paginatedpost;
    ImageView noInternetImage;
    int photos_number = 4;
    Boolean isScrolling = false;
    Boolean isLoading = false, isMaxData = false, canScroll = true;
    private PostAdapter postAdapter;
    private List<String> followingList;
    String last_node= "";
    FirebaseUser mCurrentUser;
    Button relativeLayout;
    int z;
    private int mCurrentPage = 1;
    private static final int TOTAL_ITEMS_TO_LOAD = 10;

    private int itemPos = 0;

    String lastnode;

    SwipeRefreshLayout swipephoto;

    private final int AD_POSITION = 1;
    private final int AD_INTERVAL = 5;
    private ArrayList<NativeAd> nativeAd = new ArrayList<>();


    String last_Key = "", prev_Key = "";

    // ProgressBar progress_circular;
    ProgressBar loading_progress;
    TextView new_post;
    private static final int postPerPageLimit = 10;

    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vieww = inflater.inflate(R.layout.fragment_home, container, false);
        // Uri uri = Uri.fromFile(getFileStreamPath(pathToImage));
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        //getFirstKey(false);
        //AdSettings.addTestDevice("0c0e4ee9-ad7e-456a-b410-e00a5fd9c261");
        fbNativeManager = new NativeAdsManager(getContext(), "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID", 3);

        relativeLayout = vieww.findViewById(R.id.hghghj);
        swipephoto = vieww.findViewById(R.id.swipephoto);
        recyclerView = vieww.findViewById(R.id.recycler_view);
        noInternetImage = vieww.findViewById(R.id.noInternetImage);
        shimmerFrameLayout = vieww.findViewById(R.id.shimmer_view_container);
        loading_progress = vieww.findViewById(R.id.loading_progress);
        new_post = vieww.findViewById(R.id.new_post);
        recyclerView.setHasFixedSize(true);
        postList = new ArrayList<>();

        uLists = new HashMap<>();
        //  initNativeAds();

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setItemPrefetchEnabled(true);
        recyclerView.setLayoutManager(mLayoutManager);
        //recyclerView.setRecycledViewPool(RecyclerView.RecycledViewPool);
        RecyclerView.ItemAnimator animator = recyclerView.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            //  Toast.makeText(getContext(),"Its Okk",Toast.LENGTH_LONG).show();
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        // ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);

        // getFirstKey();
       /* relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swipephoto.setRefreshing(true);
                checkFollowing();

            }
        });*/
        shimmerFrameLayout.startShimmerAnimation();
        postAdapter = new PostAdapter(getContext(), HomeFragment.this, postList);
        postAdapter.setHasStableIds(true);
        recyclerView.setAdapter(postAdapter);
        //  checkFollowing();
        //   checkFollowing();


       /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    readPosts(false);
                    isScrolling = true;
                    //

                }


            }
        });*/


//
        getFirstKey();
        swipephoto.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPost();
                //shimmerFrameLayout.startShimmerAnimation();
                //getFirstKey(true);
                //initNativeAds();

                // recyclerView.setAdapter(postAdapter);
                // postAdapter.notifyDataSetChanged();
                // checkFollowing();
                // swipephoto.setRefreshing(false);
                // isScrolling=false;

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
                postAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(0);
            }
        });
        SetDeletePostListener();
        return vieww;


    }

    public void SetNewPostListener() {
        Query postQuery = FirebaseDatabase.getInstance().getReference()
                .child("Myposts")
                .child(mCurrentUser.getUid())
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
                            for (int i = 0; i < postList.size(); i++) {
                                if (postList.get(i) instanceof Post) {
                                    if (post.equals(postList.get(i))) {
                                        return;
                                    }
                                }
                            }
                            // postList.add(0,post);
                            newPostList.add(0, post);

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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Myposts").child(mCurrentUser.getUid());
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
                //if post owner delete post else make it inactive
                if (deletedPost.getPublisher().equals(mCurrentUser.getUid())) {
                    postList.remove(deletedPost);
                    postAdapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < postList.size(); i++) {
                        if (postList.get(i) instanceof Post) {
                            if (postList.get(i).equals(deletedPost)) {
                                ((Post) postList.get(i)).setPostAvailable(false);
                            }
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
        Query postQuery = FirebaseDatabase.getInstance().getReference().child("Myposts").child(mCurrentUser.getUid())
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

                        last_node = ((Post) postList.get(postList.size() - 1)).getPostid();
                        if (!last_node.equals(last_Key)) {
                            postList.remove(postList.size() - 1);
                        } else {
                            //Toast.makeText(getActivity(), "END", Toast.LENGTH_LONG).show();
                            last_node = "end";
                        }
                        //**************ADD AD LOGIC
                        int lSize = postList.size();
                        int noOfAD = lSize / AD_INTERVAL;
                        for (int j = 0; j < noOfAD; j++) {
                            postList.add(((AD_INTERVAL * (j + 1)) + j), "AD");
                        }
                        postAdapter.notifyDataSetChanged();
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
                Toast.makeText(getActivity(), "Problem in loading more ...", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getMorePost() {
        if (!isMaxData && postList.size() > 0) {
            loading_progress.setVisibility(View.VISIBLE);

            Query postQuery = FirebaseDatabase.getInstance().getReference().child("Myposts").child(mCurrentUser.getUid())
                    .orderByKey().endAt(last_node).limitToLast(postPerPageLimit);
            postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.i("!!!!", "onDataChange");
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

                            last_node = ((Post) postList.get(postList.size() - 1)).getPostid();
                            if (!last_node.equals(last_Key)) {
                                postList.remove(postList.size() - 1);
                            } else {
                                //Toast.makeText(getActivity(), "END", Toast.LENGTH_LONG).show();
                                last_node = "end";
                            }

                            //**************ADD AD LOGIC
                            int lastADPosition = 0;
                            for (int l = (postList.size() - 1); l >= 0; l--) {
                                if (postList.get(l) instanceof String) {
                                    lastADPosition = l;
                                    break;
                                }
                            }

                            int k = postList.size() - lastADPosition - 1;
                            int noOfAD = k / AD_INTERVAL;
                            for (int j = 0; j < noOfAD; j++) {
                                postList.add((lastADPosition + (AD_INTERVAL * (j + 1)) + (j + 1)), "AD");
                            }
                            postAdapter.notifyDataSetChanged();
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
                .child("Myposts")
                .child(mCurrentUser.getUid())
                .orderByKey()
                .limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    last_Key = ds.getKey();
                    //
                }
                getPost();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                /// displayImage();

            }
        });

    }
  /*  private void getFirstKey(boolean refreshed) {

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("PersonalPosts")
                .child(mCurrentUser.getUid())
                .orderByKey()
                .limitToFirst(1);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    last_Key = ds.getKey();
                    //
                }

                //  Toast.makeText(getContext(),""+last_Key,Toast.LENGTH_LONG).show();

                //checkFollowing();
                readPosts(refreshed);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                displayImage();

            }
        });

    }*/

    private void broadcastIntent() {


        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


    }

    @Override
    public void onResume() {
        super.onResume();
        //   Toast.makeText(getContext(),"called",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPause() {
        super.onPause();
//        getActivity().unregisterReceiver(broadcastReceiver);
    }

    public void checkFollowing() {
        followingList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("following");

        String key = null;
        //   recyclerView = getView().findViewById(R.id.recycler_view);


        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    followingList.add(snapshot.getKey());
                }


                recyclerView.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);
                noInternetImage.setVisibility(View.GONE);
                readPosts(false);
                //initNativeAds();

                // readStory();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        if (n == 5) {

        } else {


        }

    }

    private void setValue(int n) {
        this.n = n;
    }

    public String getLast_Key() {

        Query q = FirebaseDatabase.getInstance().getReference()
                .child("Posts")
                .limitToLast(1);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                last_Key = dataSnapshot.getKey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return last_Key;
    }


    public void displayImage() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipephoto.setRefreshing(false);
            }
        }, 1000);
        //  Toast.makeText(getContext(),"entered",Toast.LENGTH_LONG).show();
        recyclerView.setVisibility(View.GONE);
        relativeLayout.setVisibility(View.VISIBLE);
        noInternetImage.setVisibility(View.VISIBLE);

    }


    private void readPosts(boolean refreshed) {

        Query query;
        if (!isMaxData) {
            if (refreshed) {
                query = FirebaseDatabase.getInstance().getReference()
                        .child("Myposts")
                        .child(mCurrentUser.getUid())
                        .orderByKey()
                        .limitToLast(10);//
               // Toast.makeText(getContext(), "refresh", Toast.LENGTH_SHORT).show();

            } else if (TextUtils.isEmpty(last_node)) {
                query = FirebaseDatabase.getInstance().getReference()
                        .child("Myposts")
                        .child(mCurrentUser.getUid())
                        .orderByKey()
                        .limitToLast(10);//
//                Toast.makeText(getContext(),"2nd",Toast.LENGTH_SHORT).show();

                //                           Toast.makeText(getContext(), "END", Toast.LENGTH_LONG).show();
            } else {
                query = FirebaseDatabase.getInstance().getReference().child("Myposts")
                        .child(mCurrentUser.getUid())
                        .orderByKey()
                        .endAt(last_node)
                        .limitToLast(10);
               // Toast.makeText(getContext(), "3rd", Toast.LENGTH_SHORT).show();

            }

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    // postList = new ArrayList<>();
                    postList.clear();

                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                            try {
                                Post post = snapshot.getValue(Post.class);


                                if (post.getPostid().equals(last_Key)) {

                                    isMaxData = true;
                                }

                                postList.add(post);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                        //Collections.sort(postList);
                        Collections.reverse(postList);


                        if (refreshed) {
                            postAdapter.addAll(postList, refreshed);
                           // Toast.makeText(getContext(), "YUO: " + ((Post) postList.get(0)).getPostid(), Toast.LENGTH_SHORT).show();

                            // postAdapter.addAll(postList,refreshed);
                            swipephoto.setRefreshing(false);
                        } else if (postList.size() != 0) {
                            last_node = ((Post) postList.get(postList.size() - 1)).getPostid();

                            if (!last_node.equals(last_Key)) {

                                postList.remove(postList.size() - 1);
                            } else {
//                                Toast.makeText(getContext(), "END", Toast.LENGTH_LONG).show();
                                last_node = "end";
                            }

                            postAdapter.addAll(postList, refreshed);
                            // Toast.makeText(getContext(),""+postList.g.getPostid(),Toast.LENGTH_SHORT).show();


                            isScrolling = false;


                            shimmerFrameLayout.stopShimmerAnimation();
                            shimmerFrameLayout.setVisibility(View.GONE);

                        } else {
                            try {
                                Toast.makeText(getContext(), "Follow someone to see their posts", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            shimmerFrameLayout.stopShimmerAnimation();
                            shimmerFrameLayout.setVisibility(View.GONE);
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

    @Override
    public void onStart() {
        super.onStart();
    }


    /*@Override
    public void onRefresh() {

    }*/

    public Boolean haveNetwork() {
        Boolean HAVE_WIFI = false, HAVE_MOBILE = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();

        for (NetworkInfo networkInfo : networkInfos) {
            if (networkInfo.getTypeName().equalsIgnoreCase("WIFI"))
                if (networkInfo.isConnected()) {
                    HAVE_WIFI = true;
                }
            if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                if (networkInfo.isConnected()) {
                    HAVE_MOBILE = true;
                }

        }
        return HAVE_MOBILE || HAVE_WIFI;

    }

    public void initNativeAds() {
        // Toast.makeText(getContext(),"initNative"+fbNativeManager,Toast.LENGTH_LONG).show();

        fbNativeManager.setListener(new NativeAdsManager.Listener() {
            @Override
            public void onAdsLoaded() {


                int count = fbNativeManager.getUniqueNativeAdCount();

                for (int i = 0; i < count; i++) {

                    NativeAd ad = fbNativeManager.nextNativeAd();
                    addNativeAd(i, ad);

                }
            }

            @Override
            public void onAdError(AdError adError) {

            }

        });
        fbNativeManager.loadAds();
    }

    public void addNativeAd(int i, NativeAd ad) {
        if (ad == null) {

            return;
        }
        if (this.nativeAd.size() > i && this.nativeAd.get(i) != null) {
            this.nativeAd.get(i).unregisterView();
            postList.remove(AD_POSITION + (i * AD_INTERVAL));
            this.nativeAd = null;
            postAdapter.notifyDataSetChanged();
        }


        this.nativeAd.add(i, ad);

        Post post = new Post();

        if (postList.size() > (AD_POSITION + (i * AD_INTERVAL))) {
           // Toast.makeText(getContext(), "OnadsLOad" + fbNativeManager.getUniqueNativeAdCount(), Toast.LENGTH_LONG).show();

            postList.add(AD_POSITION + (i * AD_INTERVAL), ad);
            postAdapter.addAll(postList, false);
            // postAdapter.notifyItemInserted(AD_POSITION + (i * AD_POSITION_EVERY_COUNT));
            // postAdapter.notifyDataSetChanged();
        }
    }

    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if ((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 79);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 79 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //do somethings
        }
    }


}