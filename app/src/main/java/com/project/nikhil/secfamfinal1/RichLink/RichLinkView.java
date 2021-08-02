package com.project.nikhil.secfamfinal1.RichLink;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.project.nikhil.secfamfinal1.R;

import androidx.annotation.RequiresApi;

public class RichLinkView extends RelativeLayout {

    private View view;
    Context context;
    private MetaData meta;
    TextView newsTextTitlePost,newsLinkPost,newsTextDescPost;
    ImageView newsImage;

    LinearLayout linearLayout;
    ImageView imageView;
    TextView textViewTitle;
    TextView textViewDesp;
    TextView textViewUrl;

    private String main_url;

    private boolean isDefaultClick = true;

    private RichListener richLinkListener;


    public RichLinkView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public RichLinkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public RichLinkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RichLinkView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    public void initView() {
        this.view = this;
       if(findLinearLayoutChild() != null) {
            this.view = findLinearLayoutChild();
        } else  {
           inflate(context, R.layout.link_layout,this);

        }

       // newsTextDescPost = findViewById(R.id.newsTextDescPost);
        // button = findViewById(R.id.btnParseHTML);
        newsImage=findViewById(R.id.newsThumb);
        newsLinkPost=findViewById(R.id.newsLinkPost);
        newsTextTitlePost=findViewById(R.id.newsTextTitlePost);
       /* linearLayout = (LinearLayout) findViewById(R.id.rich_link_card);
        imageView = (ImageView) findViewById(R.id.rich_link_image);
        textViewTitle = (TextView) findViewById(R.id.rich_link_title);
        textViewDesp = (TextView) findViewById(R.id.rich_link_desp);
        textViewUrl = (TextView) findViewById(R.id.rich_link_url);*/



    }

    private void setData()
    {

        if(meta.getImageurl().equals("") || meta.getImageurl().isEmpty()) {
            newsImage.setVisibility(GONE);
        } else {
            newsImage.setVisibility(VISIBLE);
            Glide.with(getContext())
                    .load(meta.getImageurl())
                    .into(newsImage);

        }

        String mDescription,title;
        // newsLinkPost.setText(add.substring(0,add.length()).toUpperCase());

     /*   String url=meta.getUrl();
        url=url.trim();
        int index;
        index=url.lastIndexOf("//");
        String add=url.substring(index+2,url.length()-1);
        while(add.contains("/"))
        {
            index=add.lastIndexOf("/");
            add=add.substring(0,index);
        }

        newsLinkPost.setText(add.substring(0,add.length()).toUpperCase());
*/
        if(meta.getTitle().isEmpty() || meta.getTitle().equals("")) {
            newsTextTitlePost.setVisibility(GONE);
        }
        else {

            if(meta.getTitle().length()>=60) {
                title = meta.getTitle().substring(0, 59) + "...";
                newsTextTitlePost.setText(title);

            }

            else
            {
                title = meta.getTitle();
                newsTextTitlePost.setText(title);

            }
        }
        if(meta.getUrl().isEmpty() || meta.getUrl().equals("")) {
            newsLinkPost.setVisibility(GONE);
        } else {
            newsLinkPost.setVisibility(VISIBLE);
        //    newsLinkPost.setText(meta.getUrl());
        }
        if(meta.getDescription().isEmpty() || meta.getDescription().equals("")) {
            newsTextDescPost.setVisibility(GONE);
        } else {
            if(meta.getDescription().length()>=50) {
                mDescription = meta.getDescription().substring(0, 50) + "...";
                newsTextDescPost.setText(mDescription);
            }
            else{
                newsTextDescPost.setText(meta.getDescription());
            }

        }



    }

    private void richLinkClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(main_url));
        context.startActivity(intent);
    }


    public void setDefaultClickListener(boolean isDefault) {
        isDefaultClick = isDefault;
    }

    public void setClickListener(RichListener richLinkListener1) {
        richLinkListener = richLinkListener1;
    }

    protected LinearLayout findLinearLayoutChild() {
        if (getChildCount() > 0 && getChildAt(0) instanceof LinearLayout) {
            return (LinearLayout) getChildAt(0);
        }
        return null;
    }

    public void setLinkFromMeta(MetaData metaData) {
        meta = metaData;
        initView();
    }

    public MetaData getMetaData() {
        return meta;
    }

    public void setLink(String url, final ViewListener viewListener) {
        main_url = url;
        RichPreview richPreview = new RichPreview(new ResponseListener() {
            @Override
            public void onData(MetaData metaData) {
                meta = metaData;
                if(!meta.getTitle().isEmpty() || !meta.getTitle().equals("")) {
                    viewListener.onSuccess(true);
                }

                setData();
            }

            @Override
            public void onError(Exception e) {
                viewListener.onError(e);
            }
        });
        richPreview.getPreview(url);
    }

}