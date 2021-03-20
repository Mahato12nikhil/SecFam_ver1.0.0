package com.project.nikhil.secfamfinal.Upload;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal.Notification.Data;
import com.project.nikhil.secfamfinal.Post.PostActivity;
import com.project.nikhil.secfamfinal.R;
import com.project.nikhil.secfamfinal.RichLink.MetaData;
import com.project.nikhil.secfamfinal.RichLink.ResponseListener;
import com.project.nikhil.secfamfinal.RichLink.RichPreview;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;

public class Upload_Text extends AppCompatActivity {

    private EditText textb;
    private ProgressBar mLoadingDialog;
ProgressDialog progressDoalog;
    TextView newsTextTitleXY,newsTextDescXY,newsLink;
    private ImageView click;
    private ImageView check,newsThumbXY;
    //private Preview mPreview;
    CardView cardViewPreview;
    ImageView linkSelect;
    RelativeLayout button_1_post;
    private int[] color;
    private String linKS,sDescription,sTitle,sUrl,Descv,imageUrl;
    private String userName,userThumb;

    RelativeLayout relate;
    NestedScrollView dfsg;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload__text);
        textb = findViewById(R.id.editTextst);
        color = new int[]{Color.parseColor("#696969"), Color.parseColor("#BADA55"), Color.parseColor("#7FE5F0"), Color.parseColor("#FF0000"), Color.parseColor("#FF80ED"), Color.parseColor("#407294"), Color.parseColor("#F7347A"), Color.parseColor("#FFA500"), Color.parseColor("#00FFFF"), Color.parseColor("#0000FF"), Color.parseColor("#7FFFD4"), Color.parseColor("#800080"), Color.parseColor("#C0D6E4")};
        click = (ImageView) findViewById(R.id.iclick);
        newsThumbXY=findViewById(R.id.newsThumbXY);
        newsTextDescXY=findViewById(R.id.newsTextDescXY);
        newsTextTitleXY=findViewById(R.id.newsTextTitleXY);
        mLoadingDialog=(ProgressBar) findViewById(R.id.rotateloadingXY);
        newsLink=findViewById(R.id.newsLink);
        linkSelect=findViewById(R.id.linkSelect);
        relate=findViewById(R.id.relate);
        dfsg=findViewById(R.id.dfsg);
        textb.setMovementMethod(LinkMovementMethod.getInstance());
        button_1_post=findViewById(R.id.button_1_post);

        ColorDrawable viewColor = (ColorDrawable) relate.getBackground();
        int colorId = viewColor.getColor();
        //Toast.makeText(getApplicationContext(),""+colorId,Toast.LENGTH_LONG).show();
        linkSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingAlertDialog();
            }
        });

        if(colorId==R.color.white)
        {
            textb.setTextColor(getResources().getColor(R.color.black));
        }
        cardViewPreview = findViewById(R.id.cardViewPreview);
        relate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textb.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(textb, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        button_1_post.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                progressDoalog = new ProgressDialog(Upload_Text.this);
                progressDoalog.setMessage("Loading....");
                progressDoalog.show();
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Posts");
                final String postid = reference.push().getKey();

                DatabaseReference reference2= FirebaseDatabase.getInstance().getReference().child("PersonalPosts")
                                              .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
              //  final String postid = reference.push().getKey();



                final String link_id = reference.child(postid).child("links").push().getKey();
                int data=getSaltString();

                String check;
                if(sTitle==null){
                    sTitle="";
                }

                if(sDescription==null){
                    sDescription="";
                }

                HashMap<String, Object> hashMap = new HashMap<>();

                    if (linKS != null) {
                        hashMap.put("postid", postid);
                        hashMap.put("site", sUrl);
                        hashMap.put("description", textb.getText().toString());
                        hashMap.put("time", ServerValue.TIMESTAMP);
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("type", "link");
                        hashMap.put("id", data);
                        hashMap.put("userThumb", userName);
                        hashMap.put("userName", userThumb);
                        hashMap.put("imageUrl", imageUrl);
                        hashMap.put("linkDesc", sDescription);
                        hashMap.put("linkTitle", sTitle);


                        reference.child(postid).setValue(hashMap);
                        reference2.child(postid).setValue(hashMap);
                        progressDoalog.dismiss();
                    //    System.out.println("/////////////......//////");
                        startActivity(new Intent(Upload_Text.this, PostActivity.class));
                    } else {
                        hashMap.put("postid", postid);
                        hashMap.put("description", textb.getText().toString());
                        hashMap.put("time", ServerValue.TIMESTAMP);
                        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("type", "text");

                        hashMap.put("id", data);

                        reference.child(postid).setValue(hashMap);
                        reference2.child(postid).setValue(hashMap);
                        progressDoalog.dismiss();
                        startActivity(new Intent(Upload_Text.this, PostActivity.class));
                     //   System.out.println("///////////////////");
                    }







            }
        });
        check = (ImageView) findViewById(R.id.check);
        // mPreview = (Preview) findViewById(R.id.preview);
//        mPreview.setListener(this);
        final RelativeLayout relate = (RelativeLayout) findViewById(R.id.relate);
        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
        click.setAnimation(animation);







        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                int clrLength = color.length;
                Random random = new Random();
                int rNumb = random.nextInt(clrLength);
                relate.setBackgroundColor(color[rNumb]);
                ///if(relate.getBackground().)

            }
        });
        textb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().length()>70)
                {
                    textb.setTextSize(17);
                }
                else{
                    textb.setTextSize(26);

                }
                // Toast.makeText(getApplicationContext(),""+textb.getText().toString(),Toast.LENGTH_LONG).show();

          /*      if (Patterns.WEB_URL.matcher(textb.getText().toString()).matches()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            linKS=textb.getText().toString();

                        }
                    });
                }*/

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    protected int getSaltString() {
        String SALTCHARS = "1234567892";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 9) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        ;
        return   Integer.parseInt(saltStr);

    }


    private void linkToDisplay(final ImageView newsImage, final TextView newsTextTitle, final TextView textView,String link) {
        RichPreview richPreview;

        richPreview = new RichPreview(new ResponseListener() {
            @Override
            public void onData(MetaData metaData) {
                MetaData data;
                data= metaData;

                try {
                    Glide.with(getApplicationContext()).load(data.getImageurl()).into(newsImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                sDescription = null;
                sTitle=null;
                //  sUrl=data.getUrl();


                if(data.getSitename().isEmpty()){
                    sUrl=data.getUrl();


                    URL url= null;
                    try {
                        url = new URL(sUrl);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                /*  //  newsLinkPost.setText(url.getHost());
                    sUrl=sUrl.trim();
                    int index;
                    index=sUrl.lastIndexOf("//");
                    String add=sUrl.substring(index+2,sUrl.length());

                    //Toast.makeText(getApplicationContext(),""+add, Toast.LENGTH_LONG).show();


                    index=0;
                    while(add.contains("/"))
                    {
                        index=add.lastIndexOf("/");
                        add=add.substring(0,index);
                    }*/
                    // sUrl=add;
                    if(url!=null)
                        newsLink.setText(url.getHost());
                    else
                        newsLink.setText(sUrl);
                }
                else {
                    sUrl=data.getSitename();
                    newsLink.setText(sUrl);
                }


                if(data.getDescription().length()>=50) {
                    sDescription = data.getDescription().substring(0,50) + "...";
                    textView.setText(sDescription);
                }
                else{
                    sDescription=data.getDescription();
                    textView.setText(data.getDescription());
                }

                if(data.getTitle().length()>=60) {
                    sTitle = data.getTitle().substring(0, 59) + "...";
                    newsTextTitle.setText(sTitle);

                }

                else
                {
                    sTitle = data.getTitle();
                    newsTextTitle.setText(sTitle);

                }
                imageUrl=data.getImageurl();

                //Implement your Layout
            }

            @Override
            public void onError(Exception e) {
                //handle error
            }
        });

        if(link!=null)
            richPreview.getPreview(link);
    }


    void loadingAlertDialog(){
        RelativeLayout Okdeclare;
        final EditText linksg;
        AlertDialog.Builder builder = new AlertDialog.Builder ( this );

        LayoutInflater inflater = this.getLayoutInflater ();
        builder.setView ( inflater.inflate ( R.layout.linkpaste, null) );


        dialog =builder.create ();

        dialog.show ();
        linksg=dialog.findViewById(R.id.linksg);
        Okdeclare=dialog.findViewById(R.id.Okdeclare);

        Okdeclare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(linksg.getText().toString().trim().length()!=0) {
                    linKS=linksg.getText().toString().trim();

                    cardViewPreview.setVisibility(View.VISIBLE);

                    mLoadingDialog.setIndeterminate(true);

                    if(linKS.startsWith("https://")) {
                        linkToDisplay(newsThumbXY, newsTextTitleXY, newsTextDescXY, linKS);
                        cardViewPreview.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    }
                    else
                    {


                        Toast.makeText(getApplicationContext(),"Enter a secure link",Toast.LENGTH_LONG).show();

                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Enter a valid link",Toast.LENGTH_LONG).show();

                }
            }
        });

    }


}
