package com.project.nikhil.secfamfinal.Post;

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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdsManager;
import com.facebook.shimmer.ShimmerFrameLayout;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private int currentPage = 0;
    private NativeAdsManager fbNativeManager;
    private Map<String, String> uLists;

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
    private List<Object> postList;
    Boolean isScrolling = false;
    Boolean isLoading = false, isMaxData = false, canScroll = true;
    private PostAdapter postAdapter;
    private List<String> followingList;
    String last_node;
    FirebaseUser mCurrentUser;
    Button relativeLayout;
    int z;
    private int mCurrentPage = 1;
    private static final int TOTAL_ITEMS_TO_LOAD = 10;

    private int itemPos = 0;

    String lastnode;

    SwipeRefreshLayout swipephoto;

    private final int AD_POSITION = 1;
    private final int AD_POSITION_EVERY_COUNT = 5;
    private ArrayList<NativeAd> nativeAd = new ArrayList<>();


    String last_Key = "", prev_Key = "";

    // ProgressBar progress_circular;

    public HomeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View vieww = inflater.inflate(R.layout.fragment_home, container, false);
        // Uri uri = Uri.fromFile(getFileStreamPath(pathToImage));
        AudienceNetworkAds.initialize(getContext());
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        mCurrentUser=FirebaseAuth.getInstance().getCurrentUser();

        getFirstKey(false);
        //AdSettings.addTestDevice("0c0e4ee9-ad7e-456a-b410-e00a5fd9c261");

        fbNativeManager = new NativeAdsManager(getContext(), "906738273411447_906741433411131", 3);




        relativeLayout = vieww.findViewById(R.id.hghghj);
        swipephoto = vieww.findViewById(R.id.swipephoto);
        recyclerView = vieww.findViewById(R.id.recycler_view);
        noInternetImage = vieww.findViewById(R.id.noInternetImage);
        shimmerFrameLayout = vieww.findViewById(R.id.shimmer_view_container);

        recyclerView.setHasFixedSize(true);
        postList = new ArrayList<>();

        uLists = new HashMap<>();
        initNativeAds();

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
        postAdapter = new PostAdapter(getContext(),HomeFragment.this);


        postAdapter.setHasStableIds(true);


        recyclerView.setAdapter(postAdapter);

        //  checkFollowing();


        //   checkFollowing();


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

                    readPosts(false);

                    isScrolling = true;
                    //

                }


            }
        });


//
        swipephoto.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                shimmerFrameLayout.startShimmerAnimation();
                getFirstKey(true);
                //initNativeAds();

                // recyclerView.setAdapter(postAdapter);
                // postAdapter.notifyDataSetChanged();
                // checkFollowing();
               // swipephoto.setRefreshing(false);
                // isScrolling=false;


            }
        });

        return vieww;


    }

    private void getFirstKey(boolean refreshed) {

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

    }

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
                initNativeAds();

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
            if(refreshed){
                query = FirebaseDatabase.getInstance().getReference()
                        .child("Myposts")
                        .child(mCurrentUser.getUid())
                        .orderByKey()
                        .limitToLast(10);//
                Toast.makeText(getContext(),"refresh",Toast.LENGTH_SHORT).show();

            }


            else if (TextUtils.isEmpty(last_node)) {
                query = FirebaseDatabase.getInstance().getReference()
                        .child("Myposts")
                        .child(mCurrentUser.getUid())
                        .orderByKey()
                        .limitToLast(10);//
//                Toast.makeText(getContext(),"2nd",Toast.LENGTH_SHORT).show();

                //                           Toast.makeText(getContext(), "END", Toast.LENGTH_LONG).show();
 }

            else {
                query = FirebaseDatabase.getInstance().getReference().child("Myposts")
                        .child(mCurrentUser.getUid())
                        .orderByKey()
                        .endAt(last_node)
                        .limitToLast(10);
                Toast.makeText(getContext(),"3rd",Toast.LENGTH_SHORT).show();

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


                        if(refreshed){
                            postAdapter.addAll(postList,refreshed);
                            Toast.makeText(getContext(),"YUO: "+((Post) postList.get(0)).getPostid(),Toast.LENGTH_SHORT).show();

                            // postAdapter.addAll(postList,refreshed);
                            swipephoto.setRefreshing(false);
                        }

                     else if(postList.size()!=0) {
                            last_node = ((Post) postList.get(postList.size() - 1)).getPostid();

                            if (!last_node.equals(last_Key) ) {

                                postList.remove(postList.size() - 1);
                            } else {
//                                Toast.makeText(getContext(), "END", Toast.LENGTH_LONG).show();
                                last_node = "end";
                            }

                            postAdapter.addAll(postList,refreshed);
                           // Toast.makeText(getContext(),""+postList.g.getPostid(),Toast.LENGTH_SHORT).show();



                            isScrolling = false;


                            shimmerFrameLayout.stopShimmerAnimation();
                            shimmerFrameLayout.setVisibility(View.GONE);

                        }
                        else {
                            try {
                                Toast.makeText(getContext(),"Follow someone to see their posts",Toast.LENGTH_SHORT).show();
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
            postList.remove(AD_POSITION + (i * AD_POSITION_EVERY_COUNT));
            this.nativeAd = null;
            postAdapter.notifyDataSetChanged();
        }



        this.nativeAd.add(i, ad);

        Post post = new Post();

        if (postList.size() > (AD_POSITION + (i * AD_POSITION_EVERY_COUNT))) {
            Toast.makeText(getContext(),"OnadsLOad"+fbNativeManager.getUniqueNativeAdCount(),Toast.LENGTH_LONG).show();

            postList.add(AD_POSITION + (i * AD_POSITION_EVERY_COUNT), ad);
            postAdapter.addAll(postList,false);
           // postAdapter.notifyItemInserted(AD_POSITION + (i * AD_POSITION_EVERY_COUNT));
           // postAdapter.notifyDataSetChanged();
        }
    }
    public boolean checkPermission() {
        int READ_EXTERNAL_PERMISSION = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if((READ_EXTERNAL_PERMISSION != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 79);
            return false;
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==79 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //do somethings
        }
    }


}