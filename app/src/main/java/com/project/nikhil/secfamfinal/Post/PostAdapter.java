package com.project.nikhil.secfamfinal.Post;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
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
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdsManager;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal.Interface.RecyclerViewOnClick;
import com.project.nikhil.secfamfinal.Notification.APIService;
import com.project.nikhil.secfamfinal.Notification.Client;
import com.project.nikhil.secfamfinal.Notification.Notification_Model;
import com.project.nikhil.secfamfinal.R;

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

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import com.project.nikhil.secfamfinal.Model.*;

class PinImageViewHolder extends RecyclerView.ViewHolder{

    CircleImageView pin_image_prof_1,pin_image_prof_2;
    TextView pin_image_username_1;
    public PinImageViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
class PinVideoViewHolder extends RecyclerView.ViewHolder{

    public PinVideoViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}
class PinTextViewHolder extends RecyclerView.ViewHolder{

    public PinTextViewHolder(@NonNull View itemView) {
        super(itemView);
    }
}

class AdvertisementHolder extends RecyclerView.ViewHolder
{ AdIconView adIconView;
    TextView tvAdTitle;
    TextView tvAdBody;
    Button btnCTA;
    View container;
    View adView;
    TextView sponsorLabel,nativeAdSocialContext;
    LinearLayout adChoicesContainer;
    MediaView mediaView;
    NativeAdLayout nativeAdLayout;


    LinearLayout nativeAdContainer;
    public AdvertisementHolder(@NonNull View itemView) {
        super(itemView);
        this.container = itemView;
        nativeAdLayout = itemView.findViewById(R.id.native_ad_container);

        LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
        adView = (ConstraintLayout) inflater.inflate(R.layout.custom_ad_layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);
        adChoicesContainer = adView.findViewById(R.id.ad_choices_container);



        nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);

        adIconView = adView.findViewById(R.id.native_ad_icon);
        tvAdTitle = adView.findViewById(R.id. native_advertiser_name);
        tvAdBody = adView.findViewById(R.id.native_ad_body);
        btnCTA = adView.findViewById(R.id.native_ad_call_to_action);
        mediaView = adView.findViewById(R.id.native_ad_media);
        sponsorLabel = adView.findViewById(R.id.native_ad_sponsored_label);
    }
}

class VideoViewHolder extends RecyclerView.ViewHolder
{

    ImageView videoView,videoMore;
    TextView details_pic_video,youchkdVd,time;
    TextView username6,share_countVideo;
    CircleImageView image_profile6;
    ImageView play_button;
    ImageView pause_button;
    ImageView commentIcon;
    ImageView shareIcon;
    TextView like_count,comment_count;
    ImageView likef;
    LinearLayout video_like_click;
    private RecyclerViewOnClick mListener;

    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.time);
        video_like_click=itemView.findViewById(R.id.video_like_click);
        likef=itemView.findViewById(R.id.LikeVideo);
        commentIcon=itemView.findViewById(R.id.commentVideo);
        details_pic_video=itemView.findViewById(R.id.details_pic_video);
        youchkdVd=itemView.findViewById(R.id.youchkdVd);
       // ImageButton view66=itemView.findViewById(R.id.view66);

       // shareIcon=itemView.findViewById(R.id.shareVideo);
        like_count=itemView.findViewById(R.id.like_countVideo);

        videoView=itemView.findViewById(R.id.postVideoView);
        image_profile6=itemView.findViewById(R.id.image_profile_video);
        // videoView=itemView.findViewById(R.id.postVideoView);
        username6=itemView.findViewById(R.id.usernameVideo);
       // play_button=itemView.findViewById(R.id.plyBut);
        comment_count=itemView.findViewById(R.id.comment_countVideo);
        videoView.setAlpha(0.7f);
        //share_countVideo=itemView.findViewById(R.id.share_countVideo);
        videoMore=itemView.findViewById(R.id.videoMore);
        //videoView.setVideoPath();

    }


}
class LinkViewHolder extends  RecyclerView.ViewHolder
{
    TextView newsTextTitlePost,newsLinkPost,newsTextDescPost,postText,textLike,textComment,shareXT,usernameText,time;
    // ArrayList list;
    ImageView shareText,moreLink;
    //MetaData data;
    TextView commentText;
    RelativeLayout checkLink;
    CircleImageView image_profileText;
    ImageView likeIcon;
    LinearLayout link_like_click;
    TextView youchkdlink;
    //RichLinkView richLinkView;


    ImageView newsImage;

    public LinkViewHolder(@NonNull View itemView) {
        super(itemView);

         time=itemView.findViewById(R.id.time);
        //View.inflate(itemView.getContext(),R.layout.post_textview,)
        link_like_click=itemView.findViewById(R.id.link_like_click);
       // newsTextDescPost = itemView.findViewById(R.id.newsTextDescPost);
        // button = findViewById(R.id.btnParseHTML);
        //richLinkView=itemView.findViewById(R.id.richPrev);
        newsImage=itemView.findViewById(R.id.newsThumb);
        newsLinkPost=itemView.findViewById(R.id.newsLinkPost);
        newsTextTitlePost=itemView.findViewById(R.id.newsTextTitlePost);
        postText=itemView.findViewById(R.id.postTextxx);
        checkLink=itemView.findViewById(R.id.checkLink);
        textLike=itemView.findViewById(R.id.like_countXT);
        textComment=itemView.findViewById(R.id.comment_countXT);
       // shareXT=itemView.findViewById(R.id.share_countXT);
        image_profileText=itemView.findViewById(R.id.image_profileText);
        usernameText=itemView.findViewById(R.id.usernameText);
    //    LikeText=itemView.findViewById(R.id.LikeText);
        commentText=itemView.findViewById(R.id.details_link);
        //shareText=itemView.findViewById(R.id.shareText);
        moreLink=itemView.findViewById(R.id.moreLink);
        likeIcon=itemView.findViewById(R.id.likeIcon);
        youchkdlink=itemView.findViewById(R.id.youchkdlink);
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
class TextViewHolder extends RecyclerView.ViewHolder
        {
           TextView texts,usernameTextSS,timeSS,like_countXTJKSS,comment_countJKSS,share_countJKSS,youchkdtxt;
    CircleImageView image_profileTextJKSS;
    ImageView commentTextJKSS,shareTextJKSS,moreOnlyText;
    ImageView LikeTextSS;
    LinearLayout text_like_click;

    public TextViewHolder(@NonNull View itemView) {
        super(itemView);
        texts=itemView.findViewById(R.id.postTextxxJK);
        usernameTextSS=itemView.findViewById(R.id.usernameTextSS);
        timeSS=itemView.findViewById(R.id.timeSS);
        like_countXTJKSS=itemView.findViewById(R.id.like_countXTJKSS);
        text_like_click=itemView.findViewById(R.id.text_like_click);
        comment_countJKSS=itemView.findViewById(R.id.comment_countJKSS);
       // share_countJKSS=itemView.findViewById(R.id.share_countJKSS);
        image_profileTextJKSS=itemView.findViewById(R.id.image_profileTextJKSS);
        LikeTextSS=itemView.findViewById(R.id.LikeTextSS);
        commentTextJKSS=itemView.findViewById(R.id.commentTextJKSS);
        moreOnlyText=itemView.findViewById(R.id.moreOnlyText);
        youchkdtxt=itemView.findViewById(R.id.youchkdtxt);
      //  itemView.setOnClickListener(this);
    }



}

class PostViewHolder extends RecyclerView.ViewHolder {
    public ImageView image_profile, post_image,  save, more;
    ImageView like;
    LinearLayout image_like_click,comment,image_download;
    RecyclerView nRecyclerView;
    public TextView username, likes, publisher, description, comments,time;
    ArrayList<String> list;
    MultipleImageAdapter mainAdapter;
    int countx=0;
    TextView youchkdImg;
    private static final int MY_PERMISSION=100;




    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.time);
        image_profile = itemView.findViewById(R.id.image_profile);
        username = itemView.findViewById(R.id.username);
       // post_image = itemView.findViewById(R.id.post_image);
        like = itemView.findViewById(R.id.Likesa);
        comments = itemView.findViewById(R.id.comment_count);
        nRecyclerView=itemView.findViewById(R.id.nRecycler_view);
//            save = itemView.findViewById(R.id.save);
        image_like_click=itemView.findViewById(R.id.image_like_click);
        likes = itemView.findViewById(R.id.like_count);
        //publisher = itemView.findViewById(R.id.publisher);
        description = itemView.findViewById(R.id.details_pic);
        comment = itemView.findViewById(R.id.commentsp);
        more = itemView.findViewById(R.id.more);
        youchkdImg=itemView.findViewById(R.id.youchkdImg);

        image_download=itemView.findViewById(R.id.image_download);

        LinearLayoutManager linearLayoutManagerx=new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false);
        nRecyclerView.setLayoutManager ( linearLayoutManagerx );
        nRecyclerView.setHasFixedSize(true);

        // nRecyclerView.

        nRecyclerView.setItemAnimator ( new DefaultItemAnimator() );
        //nRecyclerView.setOnFlingListener(null);

        SnapHelper spacePagerSnapHelper = new PagerSnapHelper();
        spacePagerSnapHelper.attachToRecyclerView(nRecyclerView);
        final int color = ContextCompat.getColor( itemView.getContext(),R.color.lightcolor);
        final int color_1 = ContextCompat.getColor( itemView.getContext(),R.color.bg);

        // this.list.clear();

        nRecyclerView.addItemDecoration ( new DotsIndicatorDecoration(10, 30, 26, color, color_1)  );

        list=new ArrayList<>();
        RecyclerView.ItemAnimator animator = nRecyclerView.getItemAnimator();

        if (animator instanceof SimpleItemAnimator) {
            //  Toast.makeText(getContext(),"Its Okk",Toast.LENGTH_LONG).show();
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

    }
}

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private static final Object MY_PERMISSION =589 ;
    private String myUid;
    String content;
    Map<String,String> linkLists;
    boolean isPlaying=false;
    Post post;
    String file_name;

    private String count;
    int radioId;
    MovementMethod method;
    DatabaseReference mReference;
   // private final SparseArray<MetaData> mFetchedMetadata = new SparseArray<>();
    String text=null;
    RadioButton radioButton;
    private NativeAdsManager fbNativeManager;
    private final Context mContext;
    ProgressDialog progressDialog;
    private ArrayList<Object> dataList;
    private ArrayList<NativeAd> nativeAd = new ArrayList<>();
   // private RichPreview richPreview;
    long likeCount;
    int countx=0;
    String userName,userThumb;
    RadioButton radioButt;
    double file_size = 0;


    private final int ITEM_TYPE_DATA = 0;
    private final int ITEM_TYPE_AD = 1;

    private final int AD_POSITION = 1;
    private final int AD_POSITION_EVERY_COUNT = 5;

    private static final String  FB_NATIVE_AD_ID = "359445208372075_35944659170527";
    private final int VIEW_TYPE_PHOTO=0,VIEW_TYPE_LOADING=1,VIEW_TYPE_VIDEO=2,VIEW_TYPE_LINK=3,VIEW_TYPE_TEXT=4;
    private final int VIEW_TYPE_PIN_IMAGE=8,VIEW_TYPE_PIN_VIDEO=9,VIEW_TYPE_PIN_LINK=11,VIEW_TYPE_PIN_TEXT=13;

    // ILoadMore iLoadMore;
    Boolean isLoading=false;
    private int visibleThreshold=5,lastVisibleItem,totalCount;

    private List<Object> mPosts;
    ArrayList<String> list;

    private FirebaseUser firebaseUser;
     private APIService apiService;
    private FrameLayout frameLayout;
    private PlayerView videoSurfaceView;
    public SimpleExoPlayer videoPlayer;
    private int videoSurfaceDefaultHeight = 0;
    private int screenDefaultHeight = 0;
    private enum VolumeState {ON, OFF};
    private VolumeState volumeState;
    private  ImageView volumeControl,thumbnail;
    private RequestManager requestManager;
    private boolean isVideoViewAdded;
    RecyclerView.RecycledViewPool viewPool;
    private RecyclerViewOnClick mListener;

    RecyclerView recyclerView2;

    AlertDialog alertDialogX ;

    String linkTitle,linkDesc,linkImage;
    Fragment mFragment;


    public PostAdapter(final Context context, Fragment homeFragment){


        viewPool = new RecyclerView.RecycledViewPool();
        ProgressDialog progressDialog;

        this.mContext = context;
        this.mListener=mListener;
        this.mFragment=homeFragment;

        mReference=FirebaseDatabase.getInstance().getReference();
        this.mPosts=new ArrayList<>();
        this.linkLists=new HashMap<>() ;
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        myUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        // this.mPosts = posts;
        //    final LinearLayoutManager linearLayoutManager=(LinearLayoutManager) recyclerView.getLayoutManager();
        fbNativeManager = new NativeAdsManager(mContext, FB_NATIVE_AD_ID,3);


        method= BetterLinkMovementMethod.getInstance();


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == ITEM_TYPE_AD) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.test2, parent, false);
            return new AdvertisementHolder(v);
        }
        else if(viewType==VIEW_TYPE_PHOTO)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);


            return new PostViewHolder(view);
        }
        else if(viewType ==VIEW_TYPE_VIDEO)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_video_item, parent, false);
            return new VideoViewHolder(view);

        }
        else if(viewType ==VIEW_TYPE_LINK)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_textview, parent, false);

            return new LinkViewHolder(view);

        }
        else if(viewType ==VIEW_TYPE_TEXT)
        {
            View view = LayoutInflater.from(mContext).inflate(R.layout.post_only_text, parent, false);

            return new TextViewHolder(view);

        }

        else
        {

            View view = LayoutInflater.from(mContext).inflate(R.layout.load_more, parent, false);
            return new LoadingViewHolder(view);
        }

    }



    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof AdvertisementHolder)
        {
            //AdvertisementHolder viewHolder = (AdvertisementHolder) holder;


            AdvertisementHolder nativeAdViewHolder = (AdvertisementHolder) holder;
            NativeAd nativeAd = (NativeAd) mPosts.get(position);



            AdOptionsView adOptionsView = new AdOptionsView(mContext, nativeAd,((AdvertisementHolder) holder).nativeAdLayout);
            ((AdvertisementHolder) holder).adChoicesContainer.removeAllViews();
            ((AdvertisementHolder) holder).adChoicesContainer.addView(adOptionsView,0);


            ((AdvertisementHolder) holder).nativeAdSocialContext.setText(nativeAd.getAdSocialContext());

            ((AdvertisementHolder) holder).tvAdTitle.setText(nativeAd.getAdvertiserName());
            ((AdvertisementHolder) holder).tvAdBody.setText(nativeAd.getAdBodyText());
            ((AdvertisementHolder) holder).btnCTA.setText(nativeAd.getAdCallToAction());
            ((AdvertisementHolder) holder).sponsorLabel.setText(nativeAd.getSponsoredTranslation());


            List<View> clickableViews = new ArrayList<>();
            clickableViews.add(((AdvertisementHolder) holder).btnCTA);
            clickableViews.add(((AdvertisementHolder) holder).mediaView);

            nativeAd.registerViewForInteraction(((AdvertisementHolder) holder).adView, ((AdvertisementHolder) holder).mediaView, ((AdvertisementHolder) holder).adIconView, clickableViews);


        }
        else if(holder instanceof VideoViewHolder)
        {
            //   init();

            final Post postV;
            postV=(Post) mPosts.get(position);
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

            thumShow(((VideoViewHolder) holder).videoView,postV.getThumb());

            isLiked(postV,postV.getPostid(), ((VideoViewHolder) holder).likef,((VideoViewHolder) holder).youchkdVd,((VideoViewHolder) holder).like_count);

            //count=((VideoViewHolder) holder).like_count.getText().toString();
            //likeCount=Long.parseLong(count);
            
            getCommetns(postV.getPostid(),((VideoViewHolder) holder).comment_count);

            ((VideoViewHolder) holder).video_like_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    {

                        if(((VideoViewHolder) holder).likef.getTag()=="liked")
                        {
                            int countU=0;
                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Posts").child(postV.getPostid())
                                    ;

                            ((VideoViewHolder) holder).likef.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postV.getPostid()).child("likes")
                                    .child(firebaseUser.getUid()).removeValue();
                            // deleteNotifications(postL.getPostid(),postL.getPublisher());



                            // Toast.makeText(mContext,"Checked",Toast.LENGTH_SHORT).show();


                            if(!postV.getPublisher().equals(firebaseUser.getUid()))
                            {deleteNotifications(postV.getPostid(),postV.getPublisher());}

                            //((PostViewHolder) holder).likes.setText();
                            ((VideoViewHolder) holder).likef.setTag("like");
                            ((VideoViewHolder) holder).youchkdVd.setVisibility(VISIBLE);
                            ((VideoViewHolder) holder).like_count.setText(""+postV.getLikes_count());

                        }
                        else if(((VideoViewHolder) holder).likef.getTag()=="like") {
                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postV.getPostid()).child("likes")
                                    .child(firebaseUser.getUid()).setValue(true);

                            if(!postV.getPublisher().equals(firebaseUser.getUid()))
                            {
                                addNotification(postV.getPublisher(), postV.getPostid(), "like","video");}

                            ((VideoViewHolder) holder).likef.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_1));

                            ((VideoViewHolder) holder).likef.setTag("liked");
                            ((VideoViewHolder) holder).youchkdVd.setVisibility(VISIBLE);
                            ((VideoViewHolder) holder).like_count.setText(""+postV.getLikes_count()+" others");
                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Posts").child(postV.getPostid());

                            if (!postV.getPublisher().equals(firebaseUser.getUid())) {

                            }
                        }
                    }


                }
            });
            ((VideoViewHolder) holder).videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext,VideoExample.class);
                    intent.putExtra("url",postV.getSite());
                    intent.putExtra("postId",postV.getPostid());
                    intent.putExtra("publisher",postV.getPublisher());
                    mContext.startActivity(intent);
                }
            });
            ((VideoViewHolder) holder).videoMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(mContext, view);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
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
                                                    if (task.isSuccessful()){
                                                        deleteNotifications(id, firebaseUser.getUid());

                                                    }
                                                }
                                            });
                                    return true;
                                case R.id.report:
                                    report(postV.getPostid(),postV.getPublisher());
                                    // Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                    popupMenu.inflate(R.menu.post_menu);
                    if (!postV.getPublisher().equals(firebaseUser.getUid())){
                        popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                    }
                    popupMenu.show();
                }
            });


            ((VideoViewHolder) holder).image_profile6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postV.getPublisher());
                    editor.apply();

                }
            });
            ((VideoViewHolder) holder).commentIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CommentsActivity.class);
                    intent.putExtra("postid", postV.getPostid());
                    intent.putExtra("postType","video");
                    intent.putExtra("publisherid", postV.getPublisher());
                    mContext.startActivity(intent);
                }
            });
            ((VideoViewHolder) holder).username6.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postV.getPublisher());
                    editor.apply();

                    /*((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();*/
                }
            });

        }

        else if(holder instanceof LoadingViewHolder)
        {
            LoadingViewHolder loadingViewHolder= (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
        else if(holder instanceof TextViewHolder)
        {

            final Post postP ;
            postP = (Post) mPosts.get(position);

            //((TextViewHolder) holder).like_countXTJKSS.setText(""+postP.getLikes_count());
            publisherInfo(((TextViewHolder) holder).image_profileTextJKSS, ((TextViewHolder) holder).usernameTextSS, null, postP.getPublisher());
            isLiked(postP,postP.getPostid(), ((TextViewHolder) holder).LikeTextSS,((TextViewHolder) holder).youchkdtxt,((TextViewHolder) holder).like_countXTJKSS);
            //nrLikes(((TextViewHolder) holder).like_countXTJKSS, postP.getPostid());
            getCommetns(postP.getPostid(), ((TextViewHolder) holder).comment_countJKSS);

            if(postP.getDescription().length()!=0) {
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


            count=((TextViewHolder) holder).like_countXTJKSS.getText().toString();
          //  likeCount=Long.parseLong(count);
            long ms = postP.getTime();
            long lastTime = ms;
            GetTimeAgo getTimeAgo = new GetTimeAgo();
            String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, mContext);
            ((TextViewHolder) holder).timeSS.setText(lastSeenTime);
            ((TextViewHolder) holder).text_like_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    {

                        if(((TextViewHolder) holder).LikeTextSS.getTag()=="liked")
                        {
                            int countU=0;
                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Posts").child(postP.getPostid())
                                    ;

                            ((TextViewHolder) holder).LikeTextSS.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postP.getPostid()).child("likes")
                                    .child(firebaseUser.getUid()).removeValue();
                            // deleteNotifications(postL.getPostid(),postL.getPublisher());

                            // Toast.makeText(mContext,"Checked",Toast.LENGTH_SHORT).show();


                            if(!postP.getPublisher().equals(firebaseUser.getUid()))
                            {deleteNotifications(postP.getPostid(),postP.getPublisher());}

                            //((PostViewHolder) holder).likes.setText();
                            ((TextViewHolder) holder).LikeTextSS.setTag("like");
                            ((TextViewHolder) holder).youchkdtxt.setVisibility(GONE);
                            ((TextViewHolder) holder).like_countXTJKSS.setText(""+postP.getLikes_count());

                        }
                        else if(((TextViewHolder) holder).LikeTextSS.getTag()=="like") {
                            try {
                                FirebaseDatabase.getInstance().getReference().child("Posts").child(postP.getPostid()).child("likes")
                                        .child(firebaseUser.getUid()).setValue(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            if(!postP.getPublisher().equals(firebaseUser.getUid()))
                            { addNotification(postP.getPublisher(), postP.getPostid(), "liked your post","text");}

                            ((TextViewHolder) holder).LikeTextSS.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_1));

                            ((TextViewHolder) holder).LikeTextSS.setTag("liked");
                            ((TextViewHolder) holder).youchkdtxt.setVisibility(VISIBLE);
                            ((TextViewHolder) holder).like_countXTJKSS.setText(""+postP.getLikes_count()+" others");

                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Posts").child(postP.getPostid())
                                    ;




                            if (!postP.getPublisher().equals(firebaseUser.getUid())) {

                            }
                        }
                    }
                }
            });

            ((TextViewHolder) holder).moreOnlyText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(mContext, view);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
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
                                                    if (task.isSuccessful()){
                                                        deleteNotifications(id, firebaseUser.getUid());

                                                    }
                                                }
                                            });
                                    return true;
                                case R.id.report:
                                    report(postP.getPostid(),postP.getPublisher());
                                    // Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                    popupMenu.inflate(R.menu.post_menu);
                    if (!postP.getPublisher().equals(firebaseUser.getUid())){
                        popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                    }
                    popupMenu.show();
                }
            });

            ((TextViewHolder) holder).image_profileTextJKSS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postP.getPublisher());
                    editor.apply();

                  /*  ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();*/
                }
            });
            ((TextViewHolder) holder).commentTextJKSS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CommentsActivity.class);
                    intent.putExtra("postid", postP.getPostid());
                    intent.putExtra("postType","text");
                    intent.putExtra("publisherid", postP.getPublisher());
                    mContext.startActivity(intent);
                }
            });
            ((TextViewHolder) holder).usernameTextSS.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postP.getPublisher());
                    editor.apply();

                    /*((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();*/
                }
            });


        }
        else if(holder instanceof LinkViewHolder)
        {


            final Post postL=(Post)mPosts.get(holder.getAdapterPosition() );


          /*  try {
             //   ((LinkViewHolder) holder).textLike.setText(""+postL.getLikes_count());
            } catch (Exception e) {
                e.printStackTrace();
            }*/
            isLiked(postL,postL.getPostid(),((LinkViewHolder) holder).likeIcon,((LinkViewHolder) holder).youchkdlink,((LinkViewHolder) holder).textLike);
            linkToDisplay(((LinkViewHolder) holder).commentText,((LinkViewHolder) holder).newsImage, ((LinkViewHolder) holder).newsLinkPost,((LinkViewHolder) holder).newsTextTitlePost, ((LinkViewHolder) holder).newsTextDescPost,postL);

            publisherInfo(((LinkViewHolder) holder).image_profileText, ((LinkViewHolder) holder).usernameText, null, postL.getPublisher());

          //  nrLikes(((LinkViewHolder) holder).textLike, postL.getPostid());
            getCommetns(postL.getPostid(), ((LinkViewHolder) holder).textComment);
            long ms = postL.getTime();
            long lastTime = ms;
            GetTimeAgo getTimeAgo = new GetTimeAgo();
            String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, mContext);
            ((LinkViewHolder) holder).time.setText(lastSeenTime);


            ((LinkViewHolder) holder).link_like_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(((LinkViewHolder) holder).likeIcon.getTag()=="liked")
                    {
                        int countU=0;
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Posts").child(postL.getPostid())
                                ;

                        ((LinkViewHolder) holder).likeIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
                        FirebaseDatabase.getInstance().getReference().child("Posts").child(postL.getPostid()).child("likes")
                                .child(firebaseUser.getUid()).removeValue();
                       // deleteNotifications(postL.getPostid(),postL.getPublisher());



                        // Toast.makeText(mContext,"Checked",Toast.LENGTH_SHORT).show();


                        if(!postL.getPublisher().equals(firebaseUser.getUid()))
                        {deleteNotifications(postL.getPostid(),postL.getPublisher());}

                        //((PostViewHolder) holder).likes.setText();
                        ((LinkViewHolder) holder).likeIcon.setTag("like");
                        ((LinkViewHolder) holder).youchkdlink.setVisibility(GONE);
                        ((LinkViewHolder) holder).textLike.setText(""+postL.getLikes_count());

                    }
                    else if(((LinkViewHolder) holder).likeIcon.getTag()=="like") {
                        FirebaseDatabase.getInstance().getReference().child("Posts").child(postL.getPostid()).child("likes")
                                .child(firebaseUser.getUid()).setValue(true);

                        if(!postL.getPublisher().equals(firebaseUser.getUid()))
                        { addNotification(postL.getPublisher(), postL.getPostid(), "liked your post","link");}

                        ((LinkViewHolder) holder).likeIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_1));

                        ((LinkViewHolder) holder).likeIcon.setTag("liked");

                        ((LinkViewHolder) holder).youchkdlink.setVisibility(VISIBLE);
                        ((LinkViewHolder) holder).textLike.setText(""+postL.getLikes_count()+" others");
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Posts").child(postL.getPostid())
                                ;



                        if (!postL.getPublisher().equals(firebaseUser.getUid())) {

                        }
                    }
                }
            });
            ((LinkViewHolder) holder).moreLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(mContext, view);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
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
                                                    if (task.isSuccessful()){
                                                        deleteNotifications(id, firebaseUser.getUid());

                                                    }
                                                }
                                            });
                                    return true;
                                case R.id.report:
                                    report(postL.getPostid(),postL.getPublisher());
                                    // Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                    popupMenu.inflate(R.menu.post_menu);
                    if (!postL.getPublisher().equals(firebaseUser.getUid())){
                        popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                    }
                    popupMenu.show();
                }
            });
            if(postL.getDescription().trim().length()!=0)
            {

                ((LinkViewHolder) holder).postText.setVisibility(VISIBLE);
                ((LinkViewHolder) holder).postText.setText(postL.getDescription());

            }

            if(postL.getSite()!=null){

            if(!postL.getSite().trim().equals("")) {



            }}
            firebaseUser=FirebaseAuth.getInstance().getCurrentUser();;
            ((LinkViewHolder) holder).postText.setMovementMethod(method);
            Linkify.addLinks(((LinkViewHolder) holder).postText, Linkify.ALL);



            ((LinkViewHolder) holder).image_profileText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postL.getPublisher());
                    editor.apply();


                }
            });
            ((LinkViewHolder) holder).commentText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CommentsActivity.class);
                    intent.putExtra("postid", postL.getPostid());
                    intent.putExtra("postType","link");
                    intent.putExtra("publisherid", postL.getPublisher());
                    mContext.startActivity(intent);
                }
            });
            ((LinkViewHolder) holder).usernameText.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postL.getPublisher());
                    editor.apply();
/*
                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();*/
                }
            });

        }
        else if(holder instanceof PostViewHolder) {

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
            isLiked(postX,postX.getPostid(),((PostViewHolder) holder).like,((PostViewHolder) holder).youchkdImg,((PostViewHolder) holder).likes);
            ((PostViewHolder) holder).nRecyclerView.setRecycledViewPool(viewPool);
            ((PostViewHolder) holder).description.setText(postX.getDescription());
            System.out.println("000000000000"+postX.getDescription());


             count=((PostViewHolder) holder).likes.getText().toString();
//            likeCount=Long.parseLong(count);
            getCommetns(postX.getPostid(),((PostViewHolder) holder).comments);

            ((PostViewHolder) holder).image_like_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){

                        if(((PostViewHolder) holder).like.getTag()=="liked")
                        {
                            int countU=0;
                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Posts").child(postX.getPostid())
                                    ;

                            ((PostViewHolder) holder).like.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postX.getPostid()).child("likes")
                                    .child(firebaseUser.getUid()).removeValue();
                            // deleteNotifications(postL.getPostid(),postL.getPublisher());

                            // Toast.makeText(mContext,"Checked",Toast.LENGTH_SHORT).show();


                            if(!postX.getPublisher().equals(firebaseUser.getUid()))
                            {deleteNotifications(postX.getPostid(),postX.getPublisher());}

                            //((PostViewHolder) holder).likes.setText();
                            ((PostViewHolder) holder).like.setTag("like");
                            ((PostViewHolder) holder).likes.setText(""+postX.getLikes_count());
                            ((PostViewHolder) holder).youchkdImg.setVisibility(GONE);


                        }
                        else if(((PostViewHolder) holder).like.getTag()=="like") {
                            FirebaseDatabase.getInstance().getReference().child("Posts").child(postX.getPostid()).child("likes")
                                    .child(firebaseUser.getUid()).setValue(true);

                            if(!postX.getPublisher().equals(firebaseUser.getUid()))
                            { addNotification(postX.getPublisher(), postX.getPostid(), "liked your post","image");}

                            ((PostViewHolder) holder).like.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_1));

                            ((PostViewHolder) holder).like.setTag("liked");
                            ((PostViewHolder) holder).youchkdImg.setVisibility(VISIBLE);
                            ((PostViewHolder) holder).likes.setText(""+postX.getLikes_count()+" others");

                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Posts").child(postX.getPostid())
                                    ;

                            if (!postX.getPublisher().equals(firebaseUser.getUid())) {

                            }
                        }

                }
            });


            ((PostViewHolder) holder).image_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //List<String> links=new ArrayList<>(postX.getLinks().values());
                    String []uri=postX.getLinks().values().toArray(new String[0]);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        mFragment.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, (Integer) MY_PERMISSION);
                    } else {
                        new DownloadTask().execute(uri);
                    }
                }
            });


            getLinks(((PostViewHolder) holder).mainAdapter,postX.getPostid(), ((PostViewHolder) holder).nRecyclerView,((PostViewHolder) holder).list,((PostViewHolder) holder).mainAdapter,postX.getLinks());

            //  postDesign(post,((PostViewHolder) holder).post_image);
             publisherInfo(((PostViewHolder) holder).image_profile, ((PostViewHolder) holder).username, ((PostViewHolder) holder).publisher, postX.getPublisher());
            isSaved(postX.getPostid(), ((PostViewHolder) holder).save);
              //nrLikes(((PostViewHolder) holder).likes, postX.getPostid());
            getCommetns(postX.getPostid(), ((PostViewHolder) holder).comments);

            ((PostViewHolder) holder).image_profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postX.getPublisher());
                    editor.apply();

                   /* ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();*/
                }
            });


            ((PostViewHolder) holder).username.setOnClickListener(new View.OnClickListener() {
                @Override

                public void onClick(View view) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", postX.getPublisher());
                    editor.apply();

                    /*((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();*/
                }
            });

            ((PostViewHolder) holder).comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, CommentsActivity.class);
                    intent.putExtra("postid", postX.getPostid());
                    intent.putExtra("postType","image");
                    intent.putExtra("publisherid", postX.getPublisher());
                    mContext.startActivity(intent);
                }
            });

            ((PostViewHolder) holder).more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(mContext, view);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
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
                                                    if (task.isSuccessful()){
                                                        deleteNotifications(id, firebaseUser.getUid());

                                                    }
                                                }
                                            });
                                    return true;
                                case R.id.report:
                                    report(postX.getPostid(),postX.getPublisher());
                                    // Toast.makeText(mContext, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });

                    popupMenu.inflate(R.menu.post_menu);
                    if (!postX.getPublisher().equals(firebaseUser.getUid())){
                        popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                        popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                    }
                    popupMenu.show();
                }
            });
        }
        //  holder.setIsRecyclable(false);
    }







    private void thumShow(ImageView videoView, String site) {

        if(site!=null)
        {
            Glide.with(mContext)
                    .load(site)
                    .into(videoView);
        }
    }

    private void getLinks(MultipleImageAdapter mainAdapter, String postid, final RecyclerView recyclerView, ArrayList<String> list, final MultipleImageAdapter adapter, Map<String, String> lists) {





        //DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("Posts").child(postid).child("links");
        // final ArrayList<String> finalList = ;



        list=new ArrayList<>(lists.values());
        // Toast.makeText(mContext,""+list.size(),Toast.LENGTH_LONG).show();


        mainAdapter = new MultipleImageAdapter (mContext,list );
        mainAdapter.setHasStableIds(true);

        recyclerView.setAdapter (  mainAdapter);

        mainAdapter.notifyDataSetChanged();

    }


    private void linkToDisplay(TextView commentText, final ImageView newsImage, final TextView newsLinkPost, final TextView newsTextTitle, final TextView newsDesc, Post postDEto) {

        // richLinkView=new RichLinkView(mContext);

        try {
            Glide.with(mContext)
                    .load(postDEto.getImageUrl())
                    .into(newsImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        commentText.setText(postDEto.getDescription());

        try {
            //newsTextTitle.setText(postDEto.getLinkTitle());


            if(postDEto.getLinkTitle().length()>=60) {
                linkTitle = postDEto.getLinkTitle().substring(0, 59) + "...";
                newsTextTitle.setText(linkTitle);

            }

            else
            {
                linkTitle = postDEto.getLinkTitle();
                newsTextTitle.setText(linkTitle);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if(postDEto.getSite().contains("https://"))
            {
                URL url=new URL(postDEto.getSite());
                newsLinkPost.setText(url.getHost());
            }
            else
                newsLinkPost.setText(postDEto.getSite());

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            newsDesc.setText(postDEto.getLinkDesc());
            if(postDEto.getLinkDesc().length()>=50) {
                linkDesc= postDEto.getLinkDesc().substring(0, 50) + "...";
                newsDesc.setText(linkDesc);
            }
            else{
                newsDesc.setText(postDEto.getLinkDesc());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void addNotification(String userid, String postid, String message,String postType){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        DatabaseReference reH=FirebaseDatabase.getInstance().getReference().child("Users").child(userid);
        reH.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName=snapshot.child("name").getValue(String.class);
                try {
                    userThumb=snapshot.child("image").getValue(String.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if(userName!=null) {
            String pushID = reference.push().getKey();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userid", firebaseUser.getUid());
            hashMap.put("text", message);
            hashMap.put("postid", postid);
            hashMap.put("notificationID", pushID);
            hashMap.put("ispost", true);
            hashMap.put("timestamp", ServerValue.TIMESTAMP);
            hashMap.put("type","like");
            hashMap.put("postType",postType);

            assert pushID != null;
            reference.child(pushID).setValue(hashMap);
        }
    }

    private void deleteNotifications(final String postid, String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if (snapshot.child("postid").getValue().equals(postid)){
                        snapshot.getRef().removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
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
    private void getCommetns(String postId, final TextView comments){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Myposts").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String number=dataSnapshot.child("comment_count").getValue().toString();
                    // Toast.makeText(mContext,""+postId,Toast.LENGTH_SHORT).show();
                    comments.setText(" "+number);
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

    private void publisherInfo(final ImageView image_profile, final TextView username, final TextView publisher, final String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                try {

                    if(user.getName()!=null)
                    { username.setText(user.getName());}
                    if(user.getImage()!=null){
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

    private void isLiked(final Post postL,final String postid, final ImageView imageView, final TextView youchkd, TextView likes) {

        //  imageView.setLiked(true);
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (postid != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts")
                    .child(postid)
                    .child("likes");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(firebaseUser.getUid()).exists()) {

                        imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart_1));
                        imageView.setTag("liked");
                        youchkd.setVisibility(VISIBLE);

                        try {
                            likes.setText("" + postL.getLikes_count() + " others");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {

                        imageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.heart));
                        imageView.setTag("like");
                        youchkd.setVisibility(GONE);

                        try {
                            likes.setText("" + post.getLikes_count());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    private void isSaved(final String postid, final ImageView imageView){

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


    private void getText(String postid, final EditText editText){
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



    public String getLastPostId()
    {

        return ((Post)mPosts.get(mPosts.size() - 1)).getPostid();
    }



    public void clearPosts()
    {
        try {
            mPosts.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shareContent(TextView desc , String url, String check, ImageView imageView) throws IOException {
        String s=desc.getText().toString()+"/n";
        File file =new File(mContext.getExternalCacheDir(),"sample."+"png");
        FileOutputStream fout=new FileOutputStream(file);
        Bitmap bitmap = null;
        bitmap=((BitmapDrawable)imageView.getDrawable()).getBitmap();

        bitmap.compress(Bitmap.CompressFormat.PNG,100,fout);
        fout.flush();
        fout.close();
        file.setReadable(true,false);
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Intent.EXTRA_TEXT,s);
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        intent.setType("image/png");
        mContext.startActivity(Intent.createChooser(intent,"share via"));






    }
    private void editPost(final String postid){
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
    public void report(final String postId, final String postedBy)
    {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);

        final View dialogView = LayoutInflater.from(mContext).inflate(R.layout.alert_label_editor, null);
        dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.reportOwn);
        editText.setText("");
        RadioButton radioButton2=dialogView.findViewById(R.id.radioButtonTwo);

        TextView button=dialogView.findViewById(R.id.reportButton);
        TextView cancelButton=dialogView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogX.dismiss();
            }
        });
        RadioButton radioButton3=dialogView.findViewById(R.id.radioButtonFive);

        final RadioGroup rad=dialogView.findViewById(R.id.radioGroups);

        radioButton = dialogView.findViewById(radioId);
        int idx=rad.indexOfChild(radioButton);

        //  final RadioButton r =(RadioButton) rad.getChildAt(idx);

        rad.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if(checkedId==R.id.radioButtonFive)
                {
                    editText.setVisibility(VISIBLE);
                    content=editText.getText().toString();
                }
                else
                {

                   radioButt=dialogView.findViewById(checkedId);
                   editText.setVisibility(GONE);
                   content= radioButt.getText().toString();
                   DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Report").child(postId)
                            .child(postedBy);
                    reference.child("content").setValue(content);


                }



            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                radioId=rad.getCheckedRadioButtonId();


                if(radioId!= R.id.radioButtonFive )
                {
                    // text=r.getText().toString();

                    radioButton=dialogView.findViewById(radioId);

                    try {
                        DatabaseReference reportRef=mReference.child("Reports")
                                .child(postId).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                ;
                        reportRef.child("issue").setValue(radioButton.getText().toString());
                        reportRef.child("timestamp").setValue(ServerValue.TIMESTAMP);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    if(!editText.getText().toString().equals(""))
                    {
                        DatabaseReference reportRef = mReference.child("Reports")
                                .child(postId).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        reportRef.child("issue").setValue(editText.getText().toString());
                    }
                    else {
                        Toast.makeText(mContext,"write something",Toast.LENGTH_LONG).show();

                    }
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

    public void addAll(final List<Object> newUsers,boolean refreshed) {
        int initialSize = mPosts.size();


        ;
        if(refreshed){
           // Toast.makeText(mContext,"refreshed",Toast.LENGTH_LONG).show();
            mPosts.clear();
            mPosts.addAll(newUsers);
            Toast.makeText(mContext,"YUO: "+((Post) mPosts.get(0)).getPostid(),Toast.LENGTH_SHORT).show();
            notifyDataSetChanged();
        }

       else if(initialSize==0)
        {
            mPosts.addAll(newUsers);

            notifyDataSetChanged();
        }
        else {
            mPosts.addAll(newUsers);

            notifyItemRangeInserted(initialSize,newUsers.size());

        }

        //notifyItemInserted(initialSize, newUsers.size());


    }
    @Override
    public int getItemViewType(int position) {

        Object post=mPosts.get(position);


        if(post instanceof Post && ((Post) post).getType()!=null) {

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


        }
        else if(post instanceof NativeAd)
        { Toast.makeText(mContext,"Entered AD",Toast.LENGTH_LONG).show();
            return ITEM_TYPE_AD;
        }
        return 0;
    }

    public  class DownloadTask extends AsyncTask<String, Integer, String[]> {


        @Override
        public void onPreExecute() {
            super .onPreExecute();

        }
        @Override
        public void onPostExecute(String[] s) {
            super .onPostExecute(s);
//            progressDialog.dismiss();
            Toast.makeText(mContext, "Images Saved"+s, Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String[] doInBackground(String... url) {
            String[] filePath = new String[url.length];
            // System.out.println ("++++++++"+filePath);
            for (int i = 0; i < url.length; i++) {
                try {
                    File mydir = new File(Environment.getExternalStorageDirectory() + "/Secfam/Downloads/Photos");
                    if (!mydir.exists()) {
                        mydir.mkdirs();
                    }

                    DownloadManager manager = (DownloadManager)mContext.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri downloadUri = Uri.parse(url[i]);

                    DownloadManager.Request request = new DownloadManager.Request (  downloadUri);
                    //request.setDestinationUri ( downloadUri );
                    SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmmss");
                    String date = dateFormat.format(new Date());

                    request.setAllowedNetworkTypes(
                            DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                            .setTitle("Downloading")
                            .setNotificationVisibility ( DownloadManager.Request.VISIBILITY_VISIBLE )
                            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)


                            .setDestinationInExternalPublicDir ("Secfam/Download/Photos", "pic"+".jpg");
                    // .setDestinationInExternalFilesDir ( getApplicationContext (), )
                    long downloadReference= manager.enqueue(request);
                    //
                    filePath[i] = mydir.getAbsolutePath() + File.separator + date + ".jpg";
                    System.out.println (downloadReference);
                    // Toast.makeText ( getApplicationContext (),"Hello: "+filePath[i],Toast.LENGTH_LONG ).show ();
                }catch (Exception ed) {
                    ed.printStackTrace();
                }
            }
            return filePath;
        }


    }

    @Override
    public long getItemId(int position) {


        Object post=mPosts.get(position);
        if(post instanceof Post && ((Post) post).getPostid()!=null) {
            return ((Post) post).getPostid().hashCode();
        }
        else
            return  hashCode();
    }
    @Override
    public int getItemCount() {

        return mPosts.size();
    }


}