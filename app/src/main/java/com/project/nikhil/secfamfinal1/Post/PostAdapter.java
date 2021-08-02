package com.project.nikhil.secfamfinal1.Post;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeAdsManager;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.Interface.RecyclerViewOnClick;
import com.project.nikhil.secfamfinal1.Notification.APIService;
import com.project.nikhil.secfamfinal1.Notification.Client;
import com.project.nikhil.secfamfinal1.Profile.ProfileActivity;
import com.project.nikhil.secfamfinal1.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.SnapHelper;

import de.hdodenhof.circleimageview.CircleImageView;
import me.saket.bettermovementmethod.BetterLinkMovementMethod;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.project.nikhil.secfamfinal1.utils.Directory.generateNewVideoName;
import static com.project.nikhil.secfamfinal1.utils.Directory.getImagesFolder;
import static com.project.nikhil.secfamfinal1.utils.Directory.getVideosFolder;
import static com.project.nikhil.secfamfinal1.utils.Directory.mainAppFolder;

import com.project.nikhil.secfamfinal1.Model.*;
import com.project.nikhil.secfamfinal1.utils.Directory;
import com.project.nikhil.secfamfinal1.utils.DownloadUtil;

class PinImageViewHolder extends RecyclerView.ViewHolder {

    CircleImageView pin_image_prof_1, pin_image_prof_2;
    TextView pin_image_username_1;

    public PinImageViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}

class PinVideoViewHolder extends RecyclerView.ViewHolder {

    public PinVideoViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}

class PinTextViewHolder extends RecyclerView.ViewHolder {

    public PinTextViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}


class VideoViewHolder extends RecyclerView.ViewHolder {

    ImageView videoView, videoMore;
    TextView details_pic_video, time;
    TextView username6, share_countVideo;
    CircleImageView image_profile6;
    ImageView play_button;
    ImageView pause_button;
    ImageView shareIcon;
    TextView like_count, comment_count;
    ImageView likef;
    LinearLayout video_like_click, video_download, video_comment;
    private RecyclerViewOnClick mListener;
    ProgressBar like_progress;

    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.time);
        video_like_click = itemView.findViewById(R.id.video_like_click);
        likef = itemView.findViewById(R.id.LikeVideo);
        video_download = itemView.findViewById(R.id.video_download);
        video_comment = itemView.findViewById(R.id.video_comment);
        details_pic_video = itemView.findViewById(R.id.details_pic_video);
        // ImageButton view66=itemView.findViewById(R.id.view66);

        // shareIcon=itemView.findViewById(R.id.shareVideo);
        like_count = itemView.findViewById(R.id.like_countVideo);

        videoView = itemView.findViewById(R.id.postVideoView);
        image_profile6 = itemView.findViewById(R.id.image_profile_video);
        // videoView=itemView.findViewById(R.id.postVideoView);
        username6 = itemView.findViewById(R.id.usernameVideo);
        // play_button=itemView.findViewById(R.id.plyBut);
        comment_count = itemView.findViewById(R.id.comment_countVideo);
        videoView.setAlpha(0.7f);
        //share_countVideo=itemView.findViewById(R.id.share_countVideo);
        videoMore = itemView.findViewById(R.id.videoMore);
        like_progress = itemView.findViewById(R.id.like_progress);
        //videoView.setVideoPath();

    }


}

class LinkViewHolder extends RecyclerView.ViewHolder {
    TextView newsTextTitlePost, newsLinkPost, newsTextDescPost, postText, textLike, textComment, shareXT, usernameText, details_link, time;
    // ArrayList list;
    ImageView shareText, moreLink;
    //MetaData data;
    LinearLayout commentLink;
    View richLinkView;
    RelativeLayout checkLink;
    CircleImageView image_profileText;
    ImageView likeIcon;
    LinearLayout link_like_click;
    //RichLinkView richLinkView;
    ProgressBar like_progress;
    FrameLayout frmmm;
    ImageView newsImage;

    public LinkViewHolder(@NonNull View itemView) {
        super(itemView);

        time = itemView.findViewById(R.id.time);
        //View.inflate(itemView.getContext(),R.layout.post_textview,)
        link_like_click = itemView.findViewById(R.id.link_like_click);
        // newsTextDescPost = itemView.findViewById(R.id.newsTextDescPost);
        // button = findViewById(R.id.btnParseHTML);
        richLinkView = itemView.findViewById(R.id.richPrev);
        newsImage = itemView.findViewById(R.id.newsThumb);
        frmmm = itemView.findViewById(R.id.frmmm);
        newsLinkPost = itemView.findViewById(R.id.newsLinkPost);
        newsTextTitlePost = itemView.findViewById(R.id.newsTextTitlePost);
        postText = itemView.findViewById(R.id.postTextxx);
        checkLink = itemView.findViewById(R.id.checkLink);
        textLike = itemView.findViewById(R.id.like_countXT);
        textComment = itemView.findViewById(R.id.comment_countXT);
        // shareXT=itemView.findViewById(R.id.share_countXT);
        image_profileText = itemView.findViewById(R.id.image_profileText);
        usernameText = itemView.findViewById(R.id.usernameText);
        //    LikeText=itemView.findViewById(R.id.LikeText);
        commentLink = itemView.findViewById(R.id.commentLink);
        //shareText=itemView.findViewById(R.id.shareText);
        moreLink = itemView.findViewById(R.id.moreLink);
        likeIcon = itemView.findViewById(R.id.likeIcon);
        like_progress = itemView.findViewById(R.id.like_progress);
        details_link = itemView.findViewById(R.id.details_link);
        //postText.setMovementMethod(BetterLinkMovementMethod.getInstance() );
    }
}

class LoadingViewHolder extends RecyclerView.ViewHolder {
    ProgressBar progressBar;

    public LoadingViewHolder(@NonNull View itemView) {
        super(itemView);
        //progressBar=itemView.findViewById(R.id.bar1234);
      /*  Sprite doubleBounce = new FadingCircle();
        progressBar.setIndeterminateDrawable(doubleBounce);*/

    }
}

class TextViewHolder extends RecyclerView.ViewHolder {
    TextView texts, usernameTextSS, timeSS, like_countXTJKSS, comment_countJKSS, share_countJKSS;
    CircleImageView image_profileTextJKSS;
    ImageView shareTextJKSS, moreOnlyText;
    ImageView LikeTextSS;
    LinearLayout text_like_click, commentText;
    ProgressBar like_progress;

    public TextViewHolder(@NonNull View itemView) {
        super(itemView);
        texts = itemView.findViewById(R.id.postTextxxJK);
        usernameTextSS = itemView.findViewById(R.id.usernameTextSS);
        timeSS = itemView.findViewById(R.id.timeSS);
        like_countXTJKSS = itemView.findViewById(R.id.like_countXTJKSS);
        text_like_click = itemView.findViewById(R.id.text_like_click);
        comment_countJKSS = itemView.findViewById(R.id.comment_countJKSS);
        // share_countJKSS=itemView.findViewById(R.id.share_countJKSS);
        image_profileTextJKSS = itemView.findViewById(R.id.image_profileTextJKSS);
        LikeTextSS = itemView.findViewById(R.id.LikeTextSS);
        commentText = itemView.findViewById(R.id.commentText);
        moreOnlyText = itemView.findViewById(R.id.moreOnlyText);
        like_progress = itemView.findViewById(R.id.like_progress);
        //  itemView.setOnClickListener(this);
    }
}

class PostViewHolder extends RecyclerView.ViewHolder {
    public ImageView image_profile, post_image, save, more;
    ImageView like;
    LinearLayout image_like_click, comment, image_download;
    RecyclerView nRecyclerView;
    public TextView username, likes, publisher, description, comments, time;
    ArrayList<String> list;
    MultipleImageAdapter mainAdapter;
    ProgressBar like_progress;
    int countx = 0;
    private static final int MY_PERMISSION = 100;


    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.time);
        image_profile = itemView.findViewById(R.id.image_profile);
        username = itemView.findViewById(R.id.username);
        // post_image = itemView.findViewById(R.id.post_image);
        like = itemView.findViewById(R.id.Likesa);
        comments = itemView.findViewById(R.id.comment_count);
        nRecyclerView = itemView.findViewById(R.id.nRecycler_view);
//            save = itemView.findViewById(R.id.save);
        image_like_click = itemView.findViewById(R.id.image_like_click);
        likes = itemView.findViewById(R.id.like_count);
        //publisher = itemView.findViewById(R.id.publisher);
        description = itemView.findViewById(R.id.description);
        comment = itemView.findViewById(R.id.commentsp);
        more = itemView.findViewById(R.id.more);
        like_progress = itemView.findViewById(R.id.like_progress);

        image_download = itemView.findViewById(R.id.image_download);

        LinearLayoutManager linearLayoutManagerx = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        nRecyclerView.setLayoutManager(linearLayoutManagerx);
        nRecyclerView.setHasFixedSize(true);

        // nRecyclerView.

        nRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //nRecyclerView.setOnFlingListener(null);

        SnapHelper spacePagerSnapHelper = new PagerSnapHelper();
        spacePagerSnapHelper.attachToRecyclerView(nRecyclerView);
        final int color = ContextCompat.getColor(itemView.getContext(), R.color.lightcolor);
        final int color_1 = ContextCompat.getColor(itemView.getContext(), R.color.bg);

        // this.list.clear();

        nRecyclerView.addItemDecoration(new DotsIndicatorDecoration(10, 30, 26, color, color_1));

        list = new ArrayList<>();
        RecyclerView.ItemAnimator animator = nRecyclerView.getItemAnimator();

        if (animator instanceof SimpleItemAnimator) {
            //  Toast.makeText(getContext(),"Its Okk",Toast.LENGTH_LONG).show();
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

    }
}

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final Object MY_PERMISSION = 589;
    private String myUid;
    String content;
    Map<String, String> linkLists;
    boolean isPlaying = false;
    Post post;
    String file_name;

    private String count;
    int radioId;
    MovementMethod method;
    DatabaseReference mReference;
    // private final SparseArray<MetaData> mFetchedMetadata = new SparseArray<>();
    String text = null;
    RadioButton radioButton;
    private NativeAdsManager fbNativeManager;
    private final Context mContext;
    ProgressDialog progressDialog;
    private ArrayList<Object> dataList;
    private ArrayList<NativeAd> nativeAd = new ArrayList<>();
    // private RichPreview richPreview;
    long likeCount;
    int countx = 0;
    String userName, userThumb;
    RadioButton radioButt;
    double file_size = 0;


    private final int ITEM_TYPE_DATA = 0;
    private final int ITEM_TYPE_AD = 1;

    private final int AD_POSITION = 1;
    private final int AD_POSITION_EVERY_COUNT = 5;

    private static final String FB_NATIVE_AD_ID = "359445208372075_35944659170527";
    private final int VIEW_TYPE_PHOTO = 0, VIEW_TYPE_LOADING = 1, VIEW_TYPE_VIDEO = 2, VIEW_TYPE_LINK = 3, VIEW_TYPE_TEXT = 4;
    private final int VIEW_TYPE_PIN_IMAGE = 8, VIEW_TYPE_PIN_VIDEO = 9, VIEW_TYPE_PIN_LINK = 11, VIEW_TYPE_PIN_TEXT = 13;

    // ILoadMore iLoadMore;
    Boolean isLoading = false;
    private int visibleThreshold = 5, lastVisibleItem, totalCount;

    private List<Object> mPosts;
    ArrayList<String> list;

    private FirebaseUser firebaseUser;
    private APIService apiService;
    private FrameLayout frameLayout;
    private PlayerView videoSurfaceView;
    public SimpleExoPlayer videoPlayer;
    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;

    private enum VolumeState {ON, OFF}

    ;
    private VolumeState volumeState;
    private ImageView volumeControl, thumbnail;
    private RequestManager requestManager;
    private boolean isVideoViewAdded;
    RecyclerView.RecycledViewPool viewPool;
    private RecyclerViewOnClick mListener;

    RecyclerView recyclerView2;

    AlertDialog alertDialogX;

    String linkTitle, linkDesc, linkImage;
    Fragment mFragment;


    public PostAdapter(final Context context, Fragment homeFragment, List<Object> postList) {


        viewPool = new RecyclerView.RecycledViewPool();
        ProgressDialog progressDialog;

        this.mContext = context;
        this.mListener = mListener;
        this.mFragment = homeFragment;

        mReference = FirebaseDatabase.getInstance().getReference();
        this.mPosts = new ArrayList<>();
        this.linkLists = new HashMap<>();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        // this.mPosts = posts;
        //    final LinearLayoutManager linearLayoutManager=(LinearLayoutManager) recyclerView.getLayoutManager();
        // fbNativeManager = new NativeAdsManager(mContext, FB_NATIVE_AD_ID, 3);


        method = BetterLinkMovementMethod.getInstance();
        this.mPosts = postList;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_TYPE_AD) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fb_native_ad_layout, parent, false);
            return new AdvertisementHolder((NativeAdLayout) v);
        } else if (viewType == VIEW_TYPE_PHOTO) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);


            return new PostViewHolder(view);
        } else if (viewType == VIEW_TYPE_VIDEO) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_video_item, parent, false);
            return new VideoViewHolder(view);

        } else if (viewType == VIEW_TYPE_LINK) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_textview, parent, false);

            return new LinkViewHolder(view);

        } else if (viewType == VIEW_TYPE_TEXT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_only_text, parent, false);

            return new TextViewHolder(view);

        } else {

            View view = LayoutInflater.from(mContext).inflate(R.layout.load_more, parent, false);
            return new LoadingViewHolder(view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AdvertisementHolder) {
            try {
                NativeAd ad;
                ad = new NativeAd(mContext, mContext.getResources().getString(R.string.native_placement_id));

                NativeAdListener nativeAdListener = new NativeAdListener() {
                    @Override
                    public void onMediaDownloaded(Ad ad) {
                        // Native ad finished downloading all assets
                        // Log.e(TAG, "Native ad finished downloading all assets.");
                    }

                    @Override
                    public void onError(Ad ad, AdError adError) {
                        // Native ad failed to load
                        Log.i("!!!!AdError: ", adError.getErrorMessage());
                    }

                    @Override
                    public void onAdLoaded(Ad ad) {
                        ((AdvertisementHolder) holder).bind((NativeAd) ad);

                        Log.i("!!!!onAdLoaded: ", "onAdLoaded");
                        // Native ad is loaded and ready to be displayed
                        // Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                    }

                    @Override
                    public void onAdClicked(Ad ad) {
                        // Native ad clicked
                        // Log.d(TAG, "Native ad clicked!");
                    }

                    @Override
                    public void onLoggingImpression(Ad ad) {
                        // Native ad impression
                        // Log.d(TAG, "Native ad impression logged!");
                    }
                };


                // Request an ad
                ad.loadAd(
                        ad.buildLoadAdConfig()
                                .withAdListener(nativeAdListener)
                                .build());
            } catch (Exception e) {
            }

        } else if (holder instanceof VideoViewHolder) {
            //   init();

            final Post postV;
            postV = (Post) mPosts.get(position);
            // ((VideoViewHolder) holder).like_count.setText(""+postV.getLikes_count());
            // final Post post2 =(Post) mPosts.get(position);
            ((VideoViewHolder) holder).details_pic_video.setText(postV.getDescription());

            long ms = postV.getTime();
            long lastTime = ms;
            GetTimeAgo getTimeAgo = new GetTimeAgo();
            String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, mContext);
            ((VideoViewHolder) holder).time.setText(lastSeenTime);

            //viewAdd(postV.getPostid(),firebaseUser.getUid());
            publisherInfo(((VideoViewHolder) holder).image_profile6, ((VideoViewHolder) holder).username6, null, postV.getPublisher());

            thumShow(((VideoViewHolder) holder).videoView, postV.getThumb());

            isLiked(postV, ((VideoViewHolder) holder).likef, ((VideoViewHolder) holder).like_count);

            //count=((VideoViewHolder) holder).like_count.getText().toString();
            //likeCount=Long.parseLong(count);
            setEditPostListener(postV.getPostid(), ((VideoViewHolder) holder).details_pic_video, postV.getPublisher());
            getCommentsCount(postV.getPostid(), ((VideoViewHolder) holder).comment_count, postV.getPublisher());

            ((VideoViewHolder) holder).video_like_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postV.isPostAvailable()) {
                        if (postV.getLikeButtonClickCount() < 5) {
                            ((VideoViewHolder) holder).video_like_click.setEnabled(false);
                            ((VideoViewHolder) holder).like_progress.setVisibility(VISIBLE);
                            ((VideoViewHolder) holder).likef.setVisibility(GONE);
                            if (((VideoViewHolder) holder).likef.getTag() == "liked") {
                                FirebaseDatabase.getInstance().getReference().child("Posts").child(postV.getPostid()).child("likes")
                                        .child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ((VideoViewHolder) holder).video_like_click.setEnabled(true);
                                        ((VideoViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((VideoViewHolder) holder).likef.setVisibility(VISIBLE);
                                        if (!postV.getPublisher().equals(firebaseUser.getUid())) {
                                            deleteNotifications(postV.getPostid(), postV.getPublisher());
                                        }
                                        ((VideoViewHolder) holder).likef.setTag("like");
                                        LikeHandler(postV, ((VideoViewHolder) holder).likef, ((VideoViewHolder) holder).like_count);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        ((VideoViewHolder) holder).video_like_click.setEnabled(true);
                                        ((VideoViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((VideoViewHolder) holder).likef.setVisibility(VISIBLE);
                                    }
                                });

                                // deleteNotifications(postL.getPostid(),postL.getPublisher());
                                // Toast.makeText(mContext,"Checked",Toast.LENGTH_SHORT).show();
                                //((PostViewHolder) holder).likes.setText();
                                //((VideoViewHolder) holder).like_count.setText("" + postV.getLikes_count());

                            } else if (((VideoViewHolder) holder).likef.getTag() == "like") {
                                FirebaseDatabase.getInstance().getReference().child("Posts").child(postV.getPostid()).child("likes")
                                        .child(firebaseUser.getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ((VideoViewHolder) holder).video_like_click.setEnabled(true);
                                        ((VideoViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((VideoViewHolder) holder).likef.setVisibility(VISIBLE);
                                        if (!postV.getPublisher().equals(firebaseUser.getUid())) {
                                            addNotification(postV.getPublisher(), postV.getPostid(), "liked your post", "video", postV.getSite());
                                        }
                                        ((VideoViewHolder) holder).likef.setTag("liked");
                                        LikeHandler(postV, ((VideoViewHolder) holder).likef, ((VideoViewHolder) holder).like_count);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        ((VideoViewHolder) holder).video_like_click.setEnabled(true);
                                        ((VideoViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((VideoViewHolder) holder).likef.setVisibility(VISIBLE);
                                    }
                                });


                                // ((VideoViewHolder) holder).like_count.setText("" + postV.getLikes_count() + " others");

                            }
                        } else {
                            Toast.makeText(mContext, "You have reached your limit. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                        postV.setLikeButtonClickCount(postV.getLikeButtonClickCount() + 1);

                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((VideoViewHolder) holder).videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (postV.isPostAvailable()) {
                        //Intent intent = new Intent(mContext, VideoExample.class);
                        Intent intent = new Intent(mContext, ExoPlayerActivity.class);
                        intent.putExtra("url", postV.getSite());
                        intent.putExtra("postId", postV.getPostid());
                        intent.putExtra("publisher", postV.getPublisher());
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((VideoViewHolder) holder).videoMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postV.isPostAvailable()) {
                        PopupMenu popupMenu = new PopupMenu(mContext, view);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.edit:
                                        editPost(postV.getPostid());
                                        return true;
                                    case R.id.delete:
                                        final String id = postV.getPostid();
                                        FirebaseDatabase.getInstance().getReference("PersonalPosts").child(firebaseUser.getUid())
                                                .child(postV.getPostid()).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            deleteNotifications(id, firebaseUser.getUid());
                                                            Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        return true;
                                    case R.id.report:
                                        report(postV.getPostid(), postV.getPublisher());
                                        // Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });

                        popupMenu.inflate(R.menu.post_menu);
                        if (!postV.getPublisher().equals(firebaseUser.getUid())) {
                            popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                            popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                        }
                        popupMenu.show();
                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            ((VideoViewHolder) holder).image_profile6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postV.getPublisher());
                    editor.apply();*/
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("publisherid", postV.getPublisher());
                    mContext.startActivity(intent);
                }
            });
            ((VideoViewHolder) holder).video_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postV.isPostAvailable()) {
                        //List<String> links=new ArrayList<>(postX.getLinks().values());
                        String[] uri = new String[]{postV.getSite()};
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            mFragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, (Integer) MY_PERMISSION);
                        } else {
                            // new VideoDownload().execute(uri);
                            new DownloadUtil().checkAndLoad(mContext, uri[0], "Video");
                        }
                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((VideoViewHolder) holder).video_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postV.isPostAvailable()) {
                        Intent intent = new Intent(mContext, CommentsActivity.class);
                        intent.putExtra("postid", postV.getPostid());
                        intent.putExtra("postType", "video");
                        intent.putExtra("publisherid", postV.getPublisher());
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((VideoViewHolder) holder).username6.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    /*SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postV.getPublisher());
                    editor.apply();*/
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("publisherid", postV.getPublisher());
                    mContext.startActivity(intent);
                    /*((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();*/
                }
            });

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        } else if (holder instanceof TextViewHolder) {

            final Post postP;
            postP = (Post) mPosts.get(position);

            //((TextViewHolder) holder).like_countXTJKSS.setText(""+postP.getLikes_count());
            publisherInfo(((TextViewHolder) holder).image_profileTextJKSS, ((TextViewHolder) holder).usernameTextSS, null, postP.getPublisher());
            isLiked(postP, ((TextViewHolder) holder).LikeTextSS, ((TextViewHolder) holder).like_countXTJKSS);
            //nrLikes(((TextViewHolder) holder).like_countXTJKSS, postP.getPostid());
            setEditPostListener(postP.getPostid(), ((TextViewHolder) holder).texts, postP.getPublisher());
            getCommentsCount(postP.getPostid(), ((TextViewHolder) holder).comment_countJKSS, postP.getPublisher());

            if (postP.getDescription().length() != 0) {
                if (postP.getDescription().length() > 70) {
                    ((TextViewHolder) holder).texts.setTextSize(17);
                    Typeface typeface = Typeface.SANS_SERIF;
                    ((TextViewHolder) holder).texts.setTypeface(typeface);
                } else {
                    ((TextViewHolder) holder).texts.setTextSize(26);
                }
                ((TextViewHolder) holder).texts.setText(postP.getDescription());
            }
            ((TextViewHolder) holder).texts.setMovementMethod(method);
            Linkify.addLinks(((TextViewHolder) holder).texts, Linkify.ALL);


            count = ((TextViewHolder) holder).like_countXTJKSS.getText().toString();
            //  likeCount=Long.parseLong(count);
            long ms = postP.getTime();
            long lastTime = ms;
            GetTimeAgo getTimeAgo = new GetTimeAgo();
            String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, mContext);
            ((TextViewHolder) holder).timeSS.setText(lastSeenTime);
            ((TextViewHolder) holder).text_like_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postP.isPostAvailable()) {
                        if (postP.getLikeButtonClickCount() < 5) {
                            ((TextViewHolder) holder).text_like_click.setEnabled(false);
                            ((TextViewHolder) holder).like_progress.setVisibility(VISIBLE);
                            ((TextViewHolder) holder).LikeTextSS.setVisibility(GONE);
                            if (((TextViewHolder) holder).LikeTextSS.getTag() == "liked") {
                                FirebaseDatabase.getInstance().getReference().child("Posts").child(postP.getPostid()).child("likes")
                                        .child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ((TextViewHolder) holder).text_like_click.setEnabled(true);
                                        ((TextViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((TextViewHolder) holder).LikeTextSS.setVisibility(VISIBLE);
                                        if (!postP.getPublisher().equals(firebaseUser.getUid())) {
                                            deleteNotifications(postP.getPostid(), postP.getPublisher());
                                        }
                                        ((TextViewHolder) holder).LikeTextSS.setTag("like");
                                        LikeHandler(postP, ((TextViewHolder) holder).LikeTextSS, ((TextViewHolder) holder).like_countXTJKSS);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        ((TextViewHolder) holder).text_like_click.setEnabled(true);
                                        ((TextViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((TextViewHolder) holder).LikeTextSS.setVisibility(VISIBLE);
                                    }
                                });

                                // deleteNotifications(postL.getPostid(),postL.getPublisher());
                                // Toast.makeText(mContext,"Checked",Toast.LENGTH_SHORT).show();
                                //((PostViewHolder) holder).likes.setText();
                                //((TextViewHolder) holder).like_countXTJKSS.setText("" + postP.getLikes_count());

                            } else if (((TextViewHolder) holder).LikeTextSS.getTag() == "like") {
                                FirebaseDatabase.getInstance().getReference().child("Posts").child(postP.getPostid()).child("likes")
                                        .child(firebaseUser.getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ((TextViewHolder) holder).text_like_click.setEnabled(true);
                                        ((TextViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((TextViewHolder) holder).LikeTextSS.setVisibility(VISIBLE);
                                        if (!postP.getPublisher().equals(firebaseUser.getUid())) {
                                            addNotification(postP.getPublisher(), postP.getPostid(), "liked your post", "text", "null");
                                        }
                                        ((TextViewHolder) holder).LikeTextSS.setTag("liked");
                                        LikeHandler(postP, ((TextViewHolder) holder).LikeTextSS, ((TextViewHolder) holder).like_countXTJKSS);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        ((TextViewHolder) holder).text_like_click.setEnabled(true);
                                        ((TextViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((TextViewHolder) holder).LikeTextSS.setVisibility(VISIBLE);
                                    }
                                });

                                //((TextViewHolder) holder).like_countXTJKSS.setText("" + postP.getLikes_count() + " others");
                            }
                        } else {
                            Toast.makeText(mContext, "You have reached your limit. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                        postP.setLikeButtonClickCount(postP.getLikeButtonClickCount() + 1);

                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ((TextViewHolder) holder).moreOnlyText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postP.isPostAvailable()) {
                        PopupMenu popupMenu = new PopupMenu(mContext, view);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.edit:
                                        editPost(postP.getPostid());
                                        return true;
                                    case R.id.delete:
                                        final String id = postP.getPostid();
                                        FirebaseDatabase.getInstance().getReference("PersonalPosts").child(firebaseUser.getUid())
                                                .child(postP.getPostid()).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            deleteNotifications(id, firebaseUser.getUid());
                                                            Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        return true;
                                    case R.id.report:
                                        report(postP.getPostid(), postP.getPublisher());
                                        // Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });

                        popupMenu.inflate(R.menu.post_menu);
                        if (!postP.getPublisher().equals(firebaseUser.getUid())) {
                            popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                            popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                        }
                        popupMenu.show();
                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ((TextViewHolder) holder).image_profileTextJKSS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postP.getPublisher());
                    editor.apply();*/
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("publisherid", postP.getPublisher());
                    mContext.startActivity(intent);
                  /*  ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();*/
                }
            });
            ((TextViewHolder) holder).commentText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postP.isPostAvailable()) {
                        Intent intent = new Intent(mContext, CommentsActivity.class);
                        intent.putExtra("postid", postP.getPostid());
                        intent.putExtra("postType", "text");
                        intent.putExtra("publisherid", postP.getPublisher());
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((TextViewHolder) holder).usernameTextSS.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                   /* SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postP.getPublisher());
                    editor.apply();*/
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("publisherid", postP.getPublisher());
                    mContext.startActivity(intent);
                    /*((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();*/
                }
            });


        } else if (holder instanceof LinkViewHolder) {
            final Post postL = (Post) mPosts.get(holder.getAdapterPosition());
          /*  try {
             //   ((LinkViewHolder) holder).textLike.setText(""+postL.getLikes_count());
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            isLiked(postL, ((LinkViewHolder) holder).likeIcon, ((LinkViewHolder) holder).textLike);
            linkToDisplay(((LinkViewHolder) holder).details_link, ((LinkViewHolder) holder).newsImage, ((LinkViewHolder) holder).frmmm, ((LinkViewHolder) holder).newsLinkPost, ((LinkViewHolder) holder).newsTextTitlePost, ((LinkViewHolder) holder).newsTextDescPost, postL);

            publisherInfo(((LinkViewHolder) holder).image_profileText, ((LinkViewHolder) holder).usernameText, null, postL.getPublisher());

            //  nrLikes(((LinkViewHolder) holder).textLike, postL.getPostid());
            setEditPostListener(postL.getPostid(), ((LinkViewHolder) holder).details_link, postL.getPublisher());
            getCommentsCount(postL.getPostid(), ((LinkViewHolder) holder).textComment, postL.getPublisher());
            long ms = postL.getTime();
            long lastTime = ms;
            GetTimeAgo getTimeAgo = new GetTimeAgo();
            String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, mContext);
            ((LinkViewHolder) holder).time.setText(lastSeenTime);


            ((LinkViewHolder) holder).link_like_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postL.isPostAvailable()) {
                        if (postL.getLikeButtonClickCount() < 5) {
                            ((LinkViewHolder) holder).link_like_click.setEnabled(false);
                            ((LinkViewHolder) holder).like_progress.setVisibility(VISIBLE);
                            ((LinkViewHolder) holder).likeIcon.setVisibility(GONE);
                            if (((LinkViewHolder) holder).likeIcon.getTag() == "liked") {
                                FirebaseDatabase.getInstance().getReference().child("Posts").child(postL.getPostid()).child("likes")
                                        .child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ((LinkViewHolder) holder).link_like_click.setEnabled(true);
                                        ((LinkViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((LinkViewHolder) holder).likeIcon.setVisibility(VISIBLE);
                                        if (!postL.getPublisher().equals(firebaseUser.getUid())) {
                                            deleteNotifications(postL.getPostid(), postL.getPublisher());
                                        }
                                        ((LinkViewHolder) holder).likeIcon.setTag("like");
                                        LikeHandler(postL, ((LinkViewHolder) holder).likeIcon, ((LinkViewHolder) holder).textLike);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        ((LinkViewHolder) holder).link_like_click.setEnabled(true);
                                        ((LinkViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((LinkViewHolder) holder).likeIcon.setVisibility(VISIBLE);
                                    }
                                });

                                // deleteNotifications(postL.getPostid(),postL.getPublisher());
                                // Toast.makeText(mContext,"Checked",Toast.LENGTH_SHORT).show();
                                //((PostViewHolder) holder).likes.setText();
                                //((LinkViewHolder) holder).textLike.setText("" + postL.getLikes_count());

                            } else if (((LinkViewHolder) holder).likeIcon.getTag() == "like") {
                                FirebaseDatabase.getInstance().getReference().child("Posts").child(postL.getPostid()).child("likes")
                                        .child(firebaseUser.getUid()).setValue(true)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                ((LinkViewHolder) holder).link_like_click.setEnabled(true);
                                                ((LinkViewHolder) holder).like_progress.setVisibility(GONE);
                                                ((LinkViewHolder) holder).likeIcon.setVisibility(VISIBLE);
                                                if (!postL.getPublisher().equals(firebaseUser.getUid())) {
                                                    addNotification(postL.getPublisher(), postL.getPostid(), "liked your post", "link", postL.getSite());
                                                }
                                                ((LinkViewHolder) holder).likeIcon.setTag("liked");
                                                LikeHandler(postL, ((LinkViewHolder) holder).likeIcon, ((LinkViewHolder) holder).textLike);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        ((LinkViewHolder) holder).link_like_click.setEnabled(true);
                                        ((LinkViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((LinkViewHolder) holder).likeIcon.setVisibility(VISIBLE);
                                    }
                                });


                                //((LinkViewHolder) holder).textLike.setText("" + postL.getLikes_count() + " others");



                              /*  if (!postL.getPublisher().equals(firebaseUser.getUid())) {

                                }*/
                            }
                        } else {
                            Toast.makeText(mContext, "You have reached your limit. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                        postL.setLikeButtonClickCount(postL.getLikeButtonClickCount() + 1);
                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((LinkViewHolder) holder).moreLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postL.isPostAvailable()) {
                        PopupMenu popupMenu = new PopupMenu(mContext, view);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.edit:
                                        editPost(postL.getPostid());
                                        return true;
                                    case R.id.delete:
                                        final String id = postL.getPostid();
                                        FirebaseDatabase.getInstance().getReference("PersonalPosts").child(firebaseUser.getUid())
                                                .child(postL.getPostid()).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            deleteNotifications(id, firebaseUser.getUid());
                                                            Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        return true;
                                    case R.id.report:
                                        report(postL.getPostid(), postL.getPublisher());
                                        // Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });

                        popupMenu.inflate(R.menu.post_menu);
                        if (!postL.getPublisher().equals(firebaseUser.getUid())) {
                            popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                            popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                        }
                        popupMenu.show();
                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (postL.getDescription().trim().length() != 0) {

                ((LinkViewHolder) holder).postText.setVisibility(VISIBLE);
                ((LinkViewHolder) holder).postText.setText(postL.getDescription());

            }

            if (postL.getSite() != null) {

                if (!postL.getSite().trim().equals("")) {


                }
            }
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            ((LinkViewHolder) holder).postText.setMovementMethod(method);
            Linkify.addLinks(((LinkViewHolder) holder).postText, Linkify.ALL);


            ((LinkViewHolder) holder).image_profileText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postL.getPublisher());
                    editor.apply();*/

                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("publisherid", postL.getPublisher());
                    mContext.startActivity(intent);

                }
            });
            ((LinkViewHolder) holder).richLinkView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postL.isPostAvailable()) {
                        Intent intent = new Intent(mContext, LinkOpenerActivity.class);
                        intent.putExtra("url", postL.getSite());
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((LinkViewHolder) holder).commentLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postL.isPostAvailable()) {
                        Intent intent = new Intent(mContext, CommentsActivity.class);
                        intent.putExtra("postid", postL.getPostid());
                        intent.putExtra("postType", "link");
                        intent.putExtra("publisherid", postL.getPublisher());
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((LinkViewHolder) holder).usernameText.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                   /* SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postL.getPublisher());
                    editor.apply();*/
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("publisherid", postL.getPublisher());
                    mContext.startActivity(intent);
/*
                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();*/
                }
            });

        } else if (holder instanceof PostViewHolder) {

            //It is set to control extra space automatically created above the photo post....
            //holder.setIsRecyclable(false);

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            final Post postX = (Post) mPosts.get(position);

            long ms = postX.getTime();
            long lastTime = ms;
            GetTimeAgo getTimeAgo = new GetTimeAgo();
            String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, mContext);
            ((PostViewHolder) holder).time.setText(lastSeenTime);

            //Toast.makeText(mContext,""+postX.getPostid(),Toast.LENGTH_LONG).show();
            //  linkLists=postX.getLists();
            try {
                //   Toast.makeText(mContext,""+postX.getLists(),Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // ((PostViewHolder) holder).likes.setText(""+postX.getLikes_count());
            isLiked(postX, ((PostViewHolder) holder).like, ((PostViewHolder) holder).likes);
            ((PostViewHolder) holder).nRecyclerView.setRecycledViewPool(viewPool);
            ((PostViewHolder) holder).description.setText(postX.getDescription());

            System.out.println("000000000000" + postX.getDescription());


            count = ((PostViewHolder) holder).likes.getText().toString();
//            likeCount=Long.parseLong(count);
            setEditPostListener(postX.getPostid(), ((PostViewHolder) holder).description, postX.getPublisher());
            getCommentsCount(postX.getPostid(), ((PostViewHolder) holder).comments, postX.getPublisher());

            ((PostViewHolder) holder).image_like_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postX.isPostAvailable()) {
                        if (postX.getLikeButtonClickCount() < 5) {
                            ((PostViewHolder) holder).image_like_click.setEnabled(false);
                            ((PostViewHolder) holder).like_progress.setVisibility(VISIBLE);
                            ((PostViewHolder) holder).like.setVisibility(GONE);
                            if (((PostViewHolder) holder).like.getTag() == "liked") {
                                FirebaseDatabase.getInstance().getReference().child("Posts").child(postX.getPostid()).child("likes")
                                        .child(firebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ((PostViewHolder) holder).image_like_click.setEnabled(true);
                                        ((PostViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((PostViewHolder) holder).like.setVisibility(VISIBLE);
                                        if (!postX.getPublisher().equals(firebaseUser.getUid())) {
                                            deleteNotifications(postX.getPostid(), postX.getPublisher());
                                        }
                                        ((PostViewHolder) holder).like.setTag("like");
                                        LikeHandler(postX, ((PostViewHolder) holder).like, ((PostViewHolder) holder).likes);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        ((PostViewHolder) holder).image_like_click.setEnabled(true);
                                        ((PostViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((PostViewHolder) holder).like.setVisibility(VISIBLE);
                                    }
                                });


                                //deleteNotifications(postX.getPostid(),postX.getPublisher());
                                // Toast.makeText(mContext,"Checked",Toast.LENGTH_SHORT).show();
                                //((PostViewHolder) holder).likes.setText();
                                //((PostViewHolder) holder).likes.setText("" + postX.getLikes_count());

                            } else if (((PostViewHolder) holder).like.getTag() == "like") {
                                FirebaseDatabase.getInstance().getReference().child("Posts").child(postX.getPostid()).child("likes")
                                        .child(firebaseUser.getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        ((PostViewHolder) holder).image_like_click.setEnabled(true);
                                        ((PostViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((PostViewHolder) holder).like.setVisibility(VISIBLE);

                                        if (!postX.getPublisher().equals(firebaseUser.getUid())) {
                                            addNotification(postX.getPublisher(), postX.getPostid(), "liked your post", "image", "null");
                                        }
                                        ((PostViewHolder) holder).like.setTag("liked");
                                        LikeHandler(postX, ((PostViewHolder) holder).like, ((PostViewHolder) holder).likes);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        ((PostViewHolder) holder).image_like_click.setEnabled(true);
                                        ((PostViewHolder) holder).like_progress.setVisibility(GONE);
                                        ((PostViewHolder) holder).like.setVisibility(VISIBLE);
                                    }
                                });
                                // ((PostViewHolder) holder).likes.setText("" + postX.getLikes_count() + " others");
                            }
                        } else {
                            Toast.makeText(mContext, "You have reached your limit. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                        postX.setLikeButtonClickCount(postX.getLikeButtonClickCount() + 1);

                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            ((PostViewHolder) holder).image_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (postX.isPostAvailable()) {
                        //List<String> links=new ArrayList<>(postX.getLinks().values());
                        String[] uri = postX.getLinks().values().toArray(new String[0]);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                            mFragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, (Integer) MY_PERMISSION);
                        } else {
                            //new ImageDownload().execute(uri);
                            for (String url : uri) {
                                new DownloadUtil().checkAndLoad(mContext, url, "Image");
                            }
                        }
                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            getLinks(((PostViewHolder) holder).mainAdapter, postX.getPostid(), ((PostViewHolder) holder).nRecyclerView, ((PostViewHolder) holder).list, ((PostViewHolder) holder).mainAdapter, postX.getLinks(), postX.isPostAvailable());

            //  postDesign(post,((PostViewHolder) holder).post_image);
            publisherInfo(((PostViewHolder) holder).image_profile, ((PostViewHolder) holder).username, ((PostViewHolder) holder).publisher, postX.getPublisher());
            isSaved(postX.getPostid(), ((PostViewHolder) holder).save);
            //nrLikes(((PostViewHolder) holder).likes, postX.getPostid());
            // getCommentsCount(postX.getPostid(), ((PostViewHolder) holder).comments, postX.getPublisher());

            ((PostViewHolder) holder).image_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postX.getPublisher());
                    editor.apply();*/
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("publisherid", postX.getPublisher());
                    mContext.startActivity(intent);
                   /* ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();*/
                }
            });


            ((PostViewHolder) holder).username.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                   /* SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postX.getPublisher());
                    editor.apply();*/
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("publisherid", postX.getPublisher());
                    mContext.startActivity(intent);
                    /*((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();*/
                }
            });

            ((PostViewHolder) holder).comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postX.isPostAvailable()) {
                        Intent intent = new Intent(mContext, CommentsActivity.class);
                        intent.putExtra("postid", postX.getPostid());
                        intent.putExtra("postType", "image");
                        intent.putExtra("publisherid", postX.getPublisher());
                        mContext.startActivity(intent);
                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ((PostViewHolder) holder).more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (postX.isPostAvailable()) {
                        PopupMenu popupMenu = new PopupMenu(mContext, view);
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.edit:
                                        editPost(postX.getPostid());
                                        return true;
                                    case R.id.delete:
                                        final String id = postX.getPostid();
                                        FirebaseDatabase.getInstance().getReference("PersonalPosts").child(firebaseUser.getUid())
                                                .child(postX.getPostid()).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            deleteNotifications(id, firebaseUser.getUid());
                                                            Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        return true;
                                    case R.id.report:
                                        report(postX.getPostid(), postX.getPublisher());
                                        // Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });

                        popupMenu.inflate(R.menu.post_menu);
                        if (!postX.getPublisher().equals(firebaseUser.getUid())) {
                            popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                            popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                        }
                        popupMenu.show();
                    } else {
                        Toast.makeText(mContext, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        //  holder.setIsRecyclable(false);
    }

    class AdvertisementHolder extends RecyclerView.ViewHolder {
        private NativeAdLayout mNativeAdLayout;
        MediaView nativeAdIcon, nativeAdMedia;
        TextView nativeAdTitle, nativeAdSponsoredLabel, nativeAdSocialContext, nativeAdBody;
        Button nativeAdCallToAction;
        LinearLayout adChoicesContainer, ll_ad;

        public AdvertisementHolder(NativeAdLayout nativeAdLayout) {
            super(nativeAdLayout);
            mNativeAdLayout = nativeAdLayout;
            ll_ad = nativeAdLayout.findViewById(R.id.ll_ad);
            nativeAdIcon = nativeAdLayout.findViewById(R.id.native_ad_icon);
            nativeAdTitle = nativeAdLayout.findViewById(R.id.native_ad_title);
            nativeAdSponsoredLabel = nativeAdLayout.findViewById(R.id.native_ad_sponsored_label);
            adChoicesContainer = nativeAdLayout.findViewById(R.id.ad_choices_container);
            nativeAdMedia = nativeAdLayout.findViewById(R.id.native_ad_media);
            nativeAdSocialContext = nativeAdLayout.findViewById(R.id.native_ad_social_context);
            nativeAdBody = nativeAdLayout.findViewById(R.id.native_ad_body);
            nativeAdCallToAction = nativeAdLayout.findViewById(R.id.native_ad_call_to_action);
        }

        public void bind(NativeAd ad) {
            if (ad != null) {
                ll_ad.setVisibility(VISIBLE);
            } else {
                ll_ad.setVisibility(GONE);
            }
            adChoicesContainer.removeAllViews();
            if (ad != null) {
                nativeAdTitle.setText(ad.getAdvertiserName());
                nativeAdBody.setText(ad.getAdBodyText());
                nativeAdSocialContext.setText(ad.getAdSocialContext());
                nativeAdSponsoredLabel.setText(ad.getSponsoredTranslation());
                nativeAdCallToAction.setText(ad.getAdCallToAction());
                nativeAdCallToAction.setVisibility(ad.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                AdOptionsView adOptionsView = new AdOptionsView(mContext, ad, mNativeAdLayout);
                adChoicesContainer.addView(adOptionsView, 0);

                ArrayList<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdIcon);
                clickableViews.add(nativeAdMedia);
                clickableViews.add(nativeAdCallToAction);
                ad.registerViewForInteraction(
                        mNativeAdLayout,
                        nativeAdMedia,
                        nativeAdIcon,
                        clickableViews
                );
            }
        }
    }

    private void thumShow(ImageView videoView, String site) {

        if (site != null) {
            Glide.with(mContext)
                    .load(site)
                    .into(videoView);
        }
    }

    private void getLinks(MultipleImageAdapter mainAdapter, String postid, final RecyclerView recyclerView, ArrayList<String> list, final MultipleImageAdapter adapter, Map<String, String> lists, boolean postAvailable) {


        //DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Posts").child(postid).child("links");
        // final ArrayList<String> finalList = ;


        list = new ArrayList<>(lists.values());
        // Toast.makeText(mContext,""+list.size(),Toast.LENGTH_LONG).show();


        mainAdapter = new MultipleImageAdapter(mContext, list, postAvailable);
        mainAdapter.setHasStableIds(true);

        recyclerView.setAdapter(mainAdapter);

        mainAdapter.notifyDataSetChanged();

    }


    private void linkToDisplay(TextView commentText, final ImageView newsImage, FrameLayout frmmm, final TextView newsLinkPost, final TextView newsTextTitle, final TextView newsDesc, Post postDEto) {

        // richLinkView=new RichLinkView(mContext);
        try {
            if (postDEto.getImageUrl() != null && !postDEto.getImageUrl().equals("")) {
                frmmm.setVisibility(VISIBLE);
                Glide.with(mContext).load(postDEto.getImageUrl().trim()).placeholder(R.drawable.ic_link).into(newsImage);
            } else {
                frmmm.setVisibility(GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        commentText.setText(postDEto.getDescription());

        try {
            //newsTextTitle.setText(postDEto.getLinkTitle());
            if (postDEto.getLinkTitle().length() >= 60) {
                linkTitle = postDEto.getLinkTitle().substring(0, 59) + "...";
                newsTextTitle.setText(linkTitle);

            } else {
                linkTitle = postDEto.getLinkTitle();
                newsTextTitle.setText(linkTitle);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (postDEto.getSite().contains("https://")) {
                URL url = new URL(postDEto.getSite());
                newsLinkPost.setText(url.getHost());
            } else
                newsLinkPost.setText(postDEto.getSite());

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            newsDesc.setText(postDEto.getLinkDesc());
            if (postDEto.getLinkDesc().length() >= 50) {
                linkDesc = postDEto.getLinkDesc().substring(0, 50) + "...";
                newsDesc.setText(linkDesc);
            } else {
                newsDesc.setText(postDEto.getLinkDesc());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void addNotification(String userid, String postid, String message, String postType, String url) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        DatabaseReference reH = FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        reH.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = snapshot.child("name").getValue(String.class);
                try {
                    userThumb = snapshot.child("image").getValue(String.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (userName != null) {
                    String pushID = reference.push().getKey();
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("userid", firebaseUser.getUid());
                    hashMap.put("text", message);
                    hashMap.put("postid", postid);
                    hashMap.put("notificationID", pushID);
                    hashMap.put("ispost", true);
                    hashMap.put("timestamp", ServerValue.TIMESTAMP);
                    hashMap.put("type", "like");
                    hashMap.put("url", url);
                    hashMap.put("postType", postType);
                    assert pushID != null;
                    reference.child(pushID).setValue(hashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void deleteNotifications(final String postid, String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("postid").exists() && snapshot.child("postid").getValue().equals(postid)) {
                        snapshot.getRef().removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                       // Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public void setLoaded() {
        isLoading = false;
    }

    /*  private void nrLikes(final TextView likes, String postId){
          DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Likes").child(postId);
          reference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
  
                  likes.setText(prettyCount(dataSnapshot.getChildrenCount()));
              }
  
              @Override
              public void onCancelled(DatabaseError databaseError) {
  
              }
          });
  
      }
  */
    private void getCommentsCount(String postId, final TextView comments, String publisher) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Myposts").child(publisher).child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String number = dataSnapshot.child("comment_count").getValue().toString();
                    // Toast.makeText(mContext,""+postId,Toast.LENGTH_SHORT).show();
                    comments.setText(" " + number);
                } catch (Exception e) {
                    comments.setText("0");
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setEditPostListener(String postId, final TextView postTitle, String publisher) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PersonalPosts").child(publisher).child(postId).child("description");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String text = dataSnapshot.getValue(String.class);
                    if (text != null) {
                        postTitle.setText(text);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void publisherInfo(final ImageView image_profile, final TextView username, final TextView publisher, final String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                try {

                    if (user.getName() != null) {
                        username.setText(user.getName());
                    }
                    if (user.getImage() != null) {
                        Glide.with(mContext).load(user.getThumb()).into(image_profile);
                    }

                    //
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                publisher.setText(user.getName());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void LikeHandler(Post post, ImageView imageView, TextView likes) {
        if (imageView.getTag().equals("liked")) {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_1));
            if (post.getLikes_count() == 0) {
                likes.setText("You liked this");
            } else if (post.getLikes_count() > 0) {
                likes.setText("You & " + (post.getLikes_count()) + " Other");
            }
        } else {
            imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
            if (post.getLikes_count() == 0) {
                likes.setText("0");
            } else if (post.getLikes_count() == 1) {
                likes.setText(post.getLikes_count() + " Like");
            } else {
                likes.setText(post.getLikes_count() + " Likes");
            }

        }
    }

    private void isLiked(Post post, final ImageView imageView, TextView likes) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (post.getPostid() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts")
                    .child(post.getPostid())
                    .child("likes");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long count = dataSnapshot.getChildrenCount();
                    if (dataSnapshot.child(firebaseUser.getUid()).exists()) {
                        post.setLikes_count(count - 1);
                        imageView.setTag("liked");
                        imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_1));
                        if (count == 1) {
                            likes.setText("You liked this");
                        } else if (count > 1) {
                            likes.setText("You & " + (count - 1) + " Other");
                        }
                    } else {
                        post.setLikes_count(count);
                        imageView.setTag("like");
                        imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
                        if (count == 0) {
                            likes.setText("0");
                        } else if (count == 1) {
                            likes.setText(count + " Like");
                        } else {
                            likes.setText(count + " Likes");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    private void isSaved(final String postid, final ImageView imageView) {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Saves").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postid).exists()) {
                    // imageView.setImageResource(R.drawable.ic_save);
                    //   imageView.setTag("saved");
                } else {

//                    imageView.setImageResource(R.drawable.ic_save1);
//                    imageView.setTag("save");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void getText(String postid, final EditText editText) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts")
                .child(postid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                editText.setText(dataSnapshot.getValue(Post.class).getDescription());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String prettyCount(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.00").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat().format(numValue);
        }

    }


    public String getLastPostId() {

        return ((Post) mPosts.get(mPosts.size() - 1)).getPostid();
    }


    public void clearPosts() {
        try {
            mPosts.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shareContent(TextView desc, String url, String check, ImageView imageView) throws IOException {
        String s = desc.getText().toString() + "/n";
        File file = new File(mContext.getExternalCacheDir(), "sample." + "png");
        FileOutputStream fout = new FileOutputStream(file);
        Bitmap bitmap = null;
        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
        fout.flush();
        fout.close();
        file.setReadable(true, false);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT, s);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType("image/png");
        mContext.startActivity(Intent.createChooser(intent, "share via"));


    }

    private void editPost(final String postid) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Edit Post");

        final EditText editText = new EditText(mContext);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        getText(postid, editText);

        alertDialog.setPositiveButton("Edit",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("description", editText.getText().toString());

                        FirebaseDatabase.getInstance().getReference("Posts")
                                .child(postid).updateChildren(hashMap);
                        FirebaseDatabase.getInstance().getReference("PersonalPosts").child(firebaseUser.getUid()).child(postid).updateChildren(hashMap);
                        FirebaseDatabase.getInstance().getReference().child("Myposts").child(firebaseUser.getUid()).child(postid).updateChildren(hashMap);
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        alertDialog.show();
    }

    public void report(final String postId, final String postedBy) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);

        final View dialogView = LayoutInflater.from(mContext).inflate(R.layout.alert_label_editor, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.reportOwn);
        editText.setText("");
        RadioButton radioButton2 = dialogView.findViewById(R.id.radioButtonTwo);

        TextView button = dialogView.findViewById(R.id.reportButton);
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogX.dismiss();
            }
        });
        RadioButton radioButton3 = dialogView.findViewById(R.id.radioButtonFive);

        final RadioGroup rad = dialogView.findViewById(R.id.radioGroups);

        radioButton = dialogView.findViewById(radioId);
        int idx = rad.indexOfChild(radioButton);

        //  final RadioButton r =(RadioButton) rad.getChildAt(idx);

        rad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonFive) {
                    editText.setVisibility(VISIBLE);
                    //content = editText.getText().toString();
                } else {
                    editText.setVisibility(GONE);
                    //radioButt = dialogView.findViewById(checkedId);
                    // content = radioButt.getText().toString();
                   /* DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Report").child(postId)
                            .child(postedBy);
                    reference.child("content").setValue(content).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(mContext, "Reported successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mContext, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioId = rad.getCheckedRadioButtonId();
                //radioGroup.getCheckedRadioButtonId() == -1(when not selected any button)
                if (radioId != -1) {
                    if (radioId != R.id.radioButtonFive) {
                        radioButton = dialogView.findViewById(radioId);
                        content = radioButton.getText().toString();
                    } else {
                        if (!editText.getText().toString().trim().equals("")) {
                            content = editText.getText().toString();
                        } else {
                            Toast.makeText(mContext, "Write something", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                        try {
                            Report report = new Report(content, ServerValue.TIMESTAMP);
                            DatabaseReference reportRef = mReference.child("Reports")
                                    .child(postId).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            reportRef.setValue(report).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    alertDialogX.dismiss();
                                    Toast.makeText(mContext, "Reported successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    alertDialogX.dismiss();
                                    Toast.makeText(mContext, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    
                } else {
                    Toast.makeText(mContext, "Please select reason", Toast.LENGTH_LONG).show();
                }
            }
        });

        alertDialogX = dialogBuilder.create();
        alertDialogX.show();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);

    }

    public void addAll(final List<Object> newUsers, boolean refreshed) {
        int initialSize = mPosts.size();
        if (refreshed) {
            // Toast.makeText(mContext,"refreshed",Toast.LENGTH_LONG).show();
            mPosts.clear();
            mPosts.addAll(newUsers);
           // Toast.makeText(mContext, "YUO: " + ((Post) mPosts.get(0)).getPostid(), Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        } else if (initialSize == 0) {
            mPosts.addAll(newUsers);
            notifyDataSetChanged();
        } else {
            mPosts.addAll(newUsers);
            notifyItemRangeInserted(initialSize, newUsers.size());
        }
        //notifyItemInserted(initialSize, newUsers.size());
    }

    @Override
    public int getItemViewType(int position) {

        Object post = mPosts.get(position);


        if (post instanceof Post && ((Post) post).getType() != null) {

            //  post=(Post)mPosts.get(position);

            switch (((Post) post).getType()) {
                case "image":
                    return VIEW_TYPE_PHOTO;
                case "video":
                    return VIEW_TYPE_VIDEO;

                case "text":
                    return VIEW_TYPE_TEXT;
                case "link":
                    return VIEW_TYPE_LINK;
                case "pin_image":
                    return VIEW_TYPE_PIN_IMAGE;


                case "pin_video":
                    return VIEW_TYPE_PIN_VIDEO;

                case "pin_text":
                    return VIEW_TYPE_PIN_TEXT;
                case "pin_link":
                    return VIEW_TYPE_PIN_LINK;
            }


        } else if (post instanceof String) {
            return ITEM_TYPE_AD;
        }
        return 0;
    }

    public class ImageDownload extends AsyncTask<String, Boolean, Boolean> {


        @Override
        public void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(mContext, "Download Started", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            Toast.makeText(mContext, "Downloaded Completed.", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... url) {
            for (int i = 0; i < url.length; i++) {
                DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri downloadUri = Uri.parse(url[i]);
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setTitle("Downloading")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(getImagesFolder(), Directory.generateNewImageName());
                long downloadReference = manager.enqueue(request);
            }
            return true;
        }


    }

    public class VideoDownload extends AsyncTask<String, Boolean, Boolean> {


        @Override
        public void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(mContext, "Download Started", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            Toast.makeText(mContext, "Downloaded Completed.", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(String... url) {
            for (int i = 0; i < url.length; i++) {
                DownloadManager manager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri downloadUri = Uri.parse(url[i]);
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                        .setTitle("Downloading")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationInExternalPublicDir(getVideosFolder(), generateNewVideoName());
                long downloadReference = manager.enqueue(request);
            }
            return true;
        }


    }

    @Override
    public long getItemId(int position) {


        Object post = mPosts.get(position);
        if (post instanceof Post && ((Post) post).getPostid() != null) {
            return ((Post) post).getPostid().hashCode();
        } else
            return hashCode();
    }

    @Override
    public int getItemCount() {

        return mPosts.size();
    }


}