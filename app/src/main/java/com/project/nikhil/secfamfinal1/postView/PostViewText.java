package com.project.nikhil.secfamfinal1.postView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.MovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.project.nikhil.secfamfinal1.Post.Post;
import com.project.nikhil.secfamfinal1.Profile.ProfileActivity;
import com.project.nikhil.secfamfinal1.R;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import me.saket.bettermovementmethod.BetterLinkMovementMethod;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PostViewText extends BaseActivity {
    private String postID;
    private TextView texts, usernameTextSS, timeSS, like_countXTJKSS, comment_countJKSS, post_not_exist;
    private CircleImageView image_profileTextJKSS;
    private ImageView back,moreOnlyText;
    private ImageView LikeTextSS;
    private LinearLayout text_like_click, commentText;
    private ProgressBar like_progress;
    private MovementMethod method;
    private String count;
    private String currUid;
    private AlertDialog alertDialogX;
    private RadioButton radioButton;
    private int radioId;
    private String content;
    DatabaseReference mReference;
    private MaterialCardView postView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view_text);
        Log.i("!!!Activity", "PostViewText");
        
        texts = findViewById(R.id.postTextxxJK);
        usernameTextSS = findViewById(R.id.usernameTextSS);
        timeSS = findViewById(R.id.timeSS);
        like_countXTJKSS = findViewById(R.id.like_countXTJKSS);
        text_like_click = findViewById(R.id.text_like_click);
        comment_countJKSS = findViewById(R.id.comment_countJKSS);
        image_profileTextJKSS = findViewById(R.id.image_profileTextJKSS);
        LikeTextSS = findViewById(R.id.LikeTextSS);
        commentText = findViewById(R.id.commentText);
        moreOnlyText = findViewById(R.id.moreOnlyText);
        like_progress = findViewById(R.id.like_progress);
        postView = findViewById(R.id.postView);
        post_not_exist = findViewById(R.id.post_not_exist);
        back = findViewById(R.id.back);
        method = BetterLinkMovementMethod.getInstance();
        currUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        postID = getIntent().getExtras().getString("POST_ID");
        Log.i("!!!!postID",postID);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mReference = FirebaseDatabase.getInstance().getReference();
        FirebaseDatabase.getInstance().getReference()
                .child("Myposts").child(currUid)
                .child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    publisherInfo(image_profileTextJKSS,usernameTextSS, null, post.getPublisher());
                    isLiked(post,LikeTextSS,like_countXTJKSS);
                    setEditPostListener(post.getPostid(),texts, post.getPublisher());
                    getCommentsCount(post.getPostid(),comment_countJKSS, post.getPublisher());
                    if (post.getDescription().length() != 0) {
                        if (post.getDescription().length() > 70) {
                            texts.setTextSize(17);
                            Typeface typeface = Typeface.SANS_SERIF;
                            texts.setTypeface(typeface);
                        } else {
                            texts.setTextSize(26);
                        }
                        texts.setText(post.getDescription());
                    }
                    texts.setMovementMethod(method);
                    Linkify.addLinks(texts, Linkify.ALL);
                    count =like_countXTJKSS.getText().toString();
                    long ms = post.getTime();
                    long lastTime = ms;
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, PostViewText.this);
                    timeSS.setText(lastSeenTime);

                    text_like_click.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (post.isPostAvailable()) {
                                if (post.getLikeButtonClickCount() < 5) {
                                    text_like_click.setEnabled(false);
                                    like_progress.setVisibility(VISIBLE);
                                    LikeTextSS.setVisibility(GONE);
                                    if (LikeTextSS.getTag() == "liked") {
                                        FirebaseDatabase.getInstance().getReference().child("Posts").child(post.getPostid()).child("likes")
                                                .child(currUid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                text_like_click.setEnabled(true);
                                                like_progress.setVisibility(GONE);
                                                LikeTextSS.setVisibility(VISIBLE);
                                                LikeTextSS.setTag("like");
                                                LikeHandler(post,LikeTextSS,like_countXTJKSS);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                text_like_click.setEnabled(true);
                                                like_progress.setVisibility(GONE);
                                                LikeTextSS.setVisibility(VISIBLE);
                                            }
                                        });

                                    } else if (LikeTextSS.getTag() == "like") {
                                        FirebaseDatabase.getInstance().getReference().child("Posts").child(post.getPostid()).child("likes")
                                                .child(currUid).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                text_like_click.setEnabled(true);
                                                like_progress.setVisibility(GONE);
                                                LikeTextSS.setVisibility(VISIBLE);
                                               
                                                LikeTextSS.setTag("liked");
                                                LikeHandler(post,LikeTextSS,like_countXTJKSS);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                text_like_click.setEnabled(true);
                                                like_progress.setVisibility(GONE);
                                                LikeTextSS.setVisibility(VISIBLE);
                                            }
                                        });

                                        //like_countXTJKSS.setText("" + post.getLikes_count() + " others");
                                    }
                                } else {
                                    Toast.makeText(PostViewText.this, "You have reached your limit. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                                post.setLikeButtonClickCount(post.getLikeButtonClickCount() + 1);

                            } else {
                                Toast.makeText(PostViewText.this, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                    moreOnlyText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (post.isPostAvailable()) {
                                PopupMenu popupMenu = new PopupMenu(PostViewText.this, view);
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
                                                // Toast.makeText(PostViewText.this, "Reported clicked!", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(PostViewText.this, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    image_profileTextJKSS.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SharedPreferences.Editor editor = PostViewText.this.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            editor.putString("profileid", post.getPublisher());
                            editor.apply();
                            Intent intent = new Intent(PostViewText.this, ProfileActivity.class);
                            intent.putExtra("publisherid", post.getPublisher());
                            PostViewText.this.startActivity(intent);

                        }
                    });
                    commentText.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (post.isPostAvailable()) {
                                Intent intent = new Intent(PostViewText.this, CommentsActivity.class);
                                intent.putExtra("postid", post.getPostid());
                                intent.putExtra("postType", "text");
                                intent.putExtra("publisherid", post.getPublisher());
                                PostViewText.this.startActivity(intent);
                            } else {
                                Toast.makeText(PostViewText.this, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    usernameTextSS.setOnClickListener(new View.OnClickListener() {
                        @Override

                        public void onClick(View view) {
                            SharedPreferences.Editor editor = PostViewText.this.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                            editor.putString("profileid", post.getPublisher());
                            editor.apply();
                            Intent intent = new Intent(PostViewText.this, ProfileActivity.class);
                            intent.putExtra("publisherid", post.getPublisher());
                            PostViewText.this.startActivity(intent);

                        }
                    });

                }else {
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
                        Glide.with(PostViewText.this).load(user.getThumb()).into(image_profile);
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

    private void isLiked(Post post, final ImageView imageView, TextView likes) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
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
                    // Toast.makeText(PostViewText.this,""+postId,Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(PostViewText.this, "Write something", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(PostViewText.this, "Reported successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                alertDialogX.dismiss();
                                Toast.makeText(PostViewText.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(PostViewText.this, "Please select reason", Toast.LENGTH_LONG).show();
                }
            }
        });

        alertDialogX = dialogBuilder.create();
        alertDialogX.show();
    }
    private void deleteNotifications(final String postid, String userid) {
        showProgressDialog();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("postid").exists() && snapshot.child("postid").getValue().equals(postid)) {
                        snapshot.getRef().removeValue();
                    }
                }
                Toast.makeText(PostViewText.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}