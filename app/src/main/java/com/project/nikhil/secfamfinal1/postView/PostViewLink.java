package com.project.nikhil.secfamfinal1.postView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.Model.Report;
import com.project.nikhil.secfamfinal1.Model.User;
import com.project.nikhil.secfamfinal1.Post.CommentsActivity;
import com.project.nikhil.secfamfinal1.Post.GetTimeAgo;
import com.project.nikhil.secfamfinal1.Post.LinkOpenerActivity;
import com.project.nikhil.secfamfinal1.Post.Post;
import com.project.nikhil.secfamfinal1.Profile.ProfileActivity;
import com.project.nikhil.secfamfinal1.R;

import java.net.URL;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import me.saket.bettermovementmethod.BetterLinkMovementMethod;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PostViewLink extends BaseActivity {
    TextView newsTextTitlePost, newsLinkPost, newsTextDescPost, postText, textLike, textComment, shareXT, usernameText, details_link, time,post_not_exist;
    ImageView back, moreLink;
    //MetaData data;
    LinearLayout commentLink;
    View richLinkView;
    RelativeLayout checkLink;
    CircleImageView image_profileText;
    ImageView likeIcon;
    LinearLayout link_like_click;
    ProgressBar like_progress;
    FrameLayout frmmm;
    ImageView newsImage;

    private String currUid,postID;
    private MaterialCardView postView;
    private AlertDialog alertDialogX;
    private RadioButton radioButton;
    private int radioId;
    private String content;
    DatabaseReference mReference;
    private MovementMethod method;
    String linkTitle, linkDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view_link);
        Log.i("!!!Activity", "PostViewLink");


        postView = findViewById(R.id.postView);
        post_not_exist = findViewById(R.id.post_not_exist);
        back = findViewById(R.id.back);
        time = findViewById(R.id.time);
        link_like_click = findViewById(R.id.link_like_click);
        richLinkView = findViewById(R.id.richPrev);
        newsImage = findViewById(R.id.newsThumb);
        frmmm = findViewById(R.id.frmmm);
        newsLinkPost = findViewById(R.id.newsLinkPost);
        newsTextTitlePost = findViewById(R.id.newsTextTitlePost);
        postText = findViewById(R.id.postTextxx);
        checkLink = findViewById(R.id.checkLink);
        textLike = findViewById(R.id.like_countXT);
        textComment = findViewById(R.id.comment_countXT);
        image_profileText = findViewById(R.id.image_profileText);
        usernameText = findViewById(R.id.usernameText);
        commentLink = findViewById(R.id.commentLink);
        moreLink = findViewById(R.id.moreLink);
        likeIcon = findViewById(R.id.likeIcon);
        like_progress = findViewById(R.id.like_progress);
        details_link = findViewById(R.id.details_link);
        method = BetterLinkMovementMethod.getInstance();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        postID = getIntent().getExtras().getString("POST_ID");
        FirebaseDatabase.getInstance().getReference()
                .child("Myposts").child(currUid)
                .child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    isLiked(post, likeIcon, textLike);
                    linkToDisplay(details_link, newsImage, frmmm, newsLinkPost, newsTextTitlePost, newsTextDescPost, post);

                    publisherInfo(image_profileText, usernameText, null, post.getPublisher());

                    setEditPostListener(post.getPostid(), details_link, post.getPublisher());
                    getCommentsCount(post.getPostid(), textComment, post.getPublisher());
                    long ms = post.getTime();
                    long lastTime = ms;
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, PostViewLink.this);
                    time.setText(lastSeenTime);


                    link_like_click.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (post.isPostAvailable()) {
                                if (post.getLikeButtonClickCount() < 5) {
                                    link_like_click.setEnabled(false);
                                    like_progress.setVisibility(VISIBLE);
                                    likeIcon.setVisibility(GONE);
                                    if (likeIcon.getTag() == "liked") {
                                        FirebaseDatabase.getInstance().getReference().child("Posts").child(post.getPostid()).child("likes")
                                                .child(currUid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                link_like_click.setEnabled(true);
                                                like_progress.setVisibility(GONE);
                                                likeIcon.setVisibility(VISIBLE);

                                                likeIcon.setTag("like");
                                                LikeHandler(post, likeIcon, textLike);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                link_like_click.setEnabled(true);
                                                like_progress.setVisibility(GONE);
                                                likeIcon.setVisibility(VISIBLE);
                                            }
                                        });


                                    } else if (likeIcon.getTag() == "like") {
                                        FirebaseDatabase.getInstance().getReference().child("Posts").child(post.getPostid()).child("likes")
                                                .child(currUid).setValue(true)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        link_like_click.setEnabled(true);
                                                        like_progress.setVisibility(GONE);
                                                        likeIcon.setVisibility(VISIBLE);

                                                        likeIcon.setTag("liked");
                                                        LikeHandler(post, likeIcon, textLike);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                link_like_click.setEnabled(true);
                                                like_progress.setVisibility(GONE);
                                                likeIcon.setVisibility(VISIBLE);
                                            }
                                        });

                                    }
                                } else {
                                    Toast.makeText(PostViewLink.this, "You have reached your limit. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                                post.setLikeButtonClickCount(post.getLikeButtonClickCount() + 1);
                            } else {
                                Toast.makeText(PostViewLink.this, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    moreLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (post.isPostAvailable()) {
                                PopupMenu popupMenu = new PopupMenu(PostViewLink.this, view);
                                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        switch (menuItem.getItemId()) {
                                            case R.id.edit:
                                                editPost(post.getPostid());
                                                return true;
                                            case R.id.delete:
                                                final String id = post.getPostid();
                                                FirebaseDatabase.getInstance().getReference("PersonalPosts").child(currUid)
                                                        .child(post.getPostid()).removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    deleteNotifications(id, currUid);
                                                                }
                                                            }
                                                        });
                                                return true;
                                            case R.id.report:
                                                report(post.getPostid(), post.getPublisher());
                                                // Toast.makeText(PostViewLink.this, "Reported clicked!", Toast.LENGTH_SHORT).show();
                                                return true;
                                            default:
                                                return false;
                                        }
                                    }
                                });

                                popupMenu.inflate(R.menu.post_menu);
                                if (!post.getPublisher().equals(currUid)) {
                                    popupMenu.getMenu().findItem(R.id.edit).setVisible(false);
                                    popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
                                }
                                popupMenu.show();
                            } else {
                                Toast.makeText(PostViewLink.this, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    if (post.getDescription().trim().length() != 0) {

                        postText.setVisibility(VISIBLE);
                        postText.setText(post.getDescription());

                    }

                    if (post.getSite() != null) {

                        if (!post.getSite().trim().equals("")) {


                        }
                    }
                    postText.setMovementMethod(method);
                    Linkify.addLinks(postText, Linkify.ALL);


                    image_profileText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(PostViewLink.this, ProfileActivity.class);
                            intent.putExtra("publisherid", post.getPublisher());
                            PostViewLink.this.startActivity(intent);

                        }
                    });
                    richLinkView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (post.isPostAvailable()) {
                                Intent intent = new Intent(PostViewLink.this, LinkOpenerActivity.class);
                                intent.putExtra("url", post.getSite());
                                PostViewLink.this.startActivity(intent);
                            } else {
                                Toast.makeText(PostViewLink.this, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    commentLink.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (post.isPostAvailable()) {
                                Intent intent = new Intent(PostViewLink.this, CommentsActivity.class);
                                intent.putExtra("postid", post.getPostid());
                                intent.putExtra("postType", "link");
                                intent.putExtra("publisherid", post.getPublisher());
                                PostViewLink.this.startActivity(intent);
                            } else {
                                Toast.makeText(PostViewLink.this, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    usernameText.setOnClickListener(new View.OnClickListener() {
                        @Override

                        public void onClick(View view) {

                            Intent intent = new Intent(PostViewLink.this, ProfileActivity.class);
                            intent.putExtra("publisherid", post.getPublisher());
                            PostViewLink.this.startActivity(intent);
                        }
                    });
                } else {
                        //post is no mere exist. Deleted by the post owner
                        postView.setVisibility(GONE);
                        post_not_exist.setVisibility(VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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
                        Glide.with(PostViewLink.this).load(user.getThumb()).into(image_profile);
                    }

                    //
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isLiked(Post post, final ImageView imageView, TextView likes) {
        if (post.getPostid() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Posts")
                    .child(post.getPostid())
                    .child("likes");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long count = dataSnapshot.getChildrenCount();
                    if (dataSnapshot.child(currUid).exists()) {
                        post.setLikes_count(count - 1);
                        imageView.setTag("liked");
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.heart_1));
                        if (count == 1) {
                            likes.setText("You liked this");
                        } else if (count > 1) {
                            likes.setText("You & " + (count - 1) + " Other");
                        }
                    } else {
                        post.setLikes_count(count);
                        imageView.setTag("like");
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.heart));
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

    private void getCommentsCount(String postId, final TextView comments, String publisher) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Myposts").child(publisher).child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String number = dataSnapshot.child("comment_count").getValue().toString();
                    // Toast.makeText(PostViewLink.this,""+postId,Toast.LENGTH_SHORT).show();
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

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void LikeHandler(Post post, ImageView imageView, TextView likes) {
        if (imageView.getTag().equals("liked")) {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.heart_1));
            if (post.getLikes_count() == 0) {
                likes.setText("You liked this");
            } else if (post.getLikes_count() > 0) {
                likes.setText("You & " + (post.getLikes_count()) + " Other");
            }
        } else {
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.heart));
            if (post.getLikes_count() == 0) {
                likes.setText("0");
            } else if (post.getLikes_count() == 1) {
                likes.setText(post.getLikes_count() + " Like");
            } else {
                likes.setText(post.getLikes_count() + " Likes");
            }

        }
    }

    private void editPost(final String postid) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Edit Post");

        final EditText editText = new EditText(this);
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
                        FirebaseDatabase.getInstance().getReference("PersonalPosts").child(currUid).child(postid).updateChildren(hashMap);
                        FirebaseDatabase.getInstance().getReference().child("Myposts").child(currUid).child(postid).updateChildren(hashMap);
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
    public void report(final String postId, final String postedBy) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        final View dialogView = LayoutInflater.from(this).inflate(R.layout.alert_label_editor, null);
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
                            Toast.makeText(this, "Reported successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(PostViewLink.this, "Write something", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(PostViewLink.this, "Reported successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                alertDialogX.dismiss();
                                Toast.makeText(PostViewLink.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(PostViewLink.this, "Please select reason", Toast.LENGTH_LONG).show();
                }
            }
        });

        alertDialogX = dialogBuilder.create();
        alertDialogX.show();
    }
    private void linkToDisplay(TextView commentText, final ImageView newsImage, FrameLayout frmmm, final TextView newsLinkPost, final TextView newsTextTitle, final TextView newsDesc, Post postDEto) {

        // richLinkView=new RichLinkView(mContext);
        try {
            if (postDEto.getImageUrl() != null && !postDEto.getImageUrl().equals("")) {
                frmmm.setVisibility(VISIBLE);
                Glide.with(PostViewLink.this).load(postDEto.getImageUrl().trim()).placeholder(R.drawable.ic_link).into(newsImage);
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
    private void deleteNotifications(final String postid, String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("postid").exists() && snapshot.child("postid").getValue().equals(postid)) {
                        snapshot.getRef().removeValue();
                    }
                }
                Toast.makeText(PostViewLink.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}