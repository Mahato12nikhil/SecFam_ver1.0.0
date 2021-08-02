package com.project.nikhil.secfamfinal1.postView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.MovementMethod;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.SnapHelper;

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
import com.project.nikhil.secfamfinal1.Post.DotsIndicatorDecoration;
import com.project.nikhil.secfamfinal1.Post.GetTimeAgo;
import com.project.nikhil.secfamfinal1.Post.MultipleImageAdapter;
import com.project.nikhil.secfamfinal1.Post.Post;
import com.project.nikhil.secfamfinal1.Profile.ProfileActivity;
import com.project.nikhil.secfamfinal1.R;
import com.project.nikhil.secfamfinal1.utils.DownloadUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.saket.bettermovementmethod.BetterLinkMovementMethod;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class PostViewImage extends BaseActivity {
    private static final int MY_PERMISSION = 100;
    public ImageView image_profile, post_image, save, more;
    ImageView back,like;
    LinearLayout image_like_click, comment, image_download;
    RecyclerView nRecyclerView;
    public TextView username, likes, publisher, description, comments, time, post_not_exist;
    ArrayList<String> list;
    MultipleImageAdapter mainAdapter;
    ProgressBar like_progress;
    private MovementMethod method;
    private String count;
    private String postID,currUid;
    private AlertDialog alertDialogX;
    private RadioButton radioButton;
    private int radioId;
    private String content;
    DatabaseReference mReference;
    private MaterialCardView postView;
    RecyclerView.RecycledViewPool viewPool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view_image);
        Log.i("!!!Activity", "PostViewImage");
        time = findViewById(R.id.time);
        image_profile = findViewById(R.id.image_profile);
        username = findViewById(R.id.username);
        like = findViewById(R.id.Likesa);
        comments = findViewById(R.id.comment_count);
        nRecyclerView = findViewById(R.id.nRecycler_view);
        image_like_click = findViewById(R.id.image_like_click);
        likes = findViewById(R.id.like_count);
        description = findViewById(R.id.description);
        comment = findViewById(R.id.commentsp);
        more = findViewById(R.id.more);
        like_progress = findViewById(R.id.like_progress);
        image_download = findViewById(R.id.image_download);
        LinearLayoutManager linearLayoutManagerx = new LinearLayoutManager(PostViewImage.this, LinearLayoutManager.HORIZONTAL, false);
        nRecyclerView.setLayoutManager(linearLayoutManagerx);
        nRecyclerView.setHasFixedSize(true);
        nRecyclerView.setItemAnimator(new DefaultItemAnimator());

        SnapHelper spacePagerSnapHelper = new PagerSnapHelper();
        spacePagerSnapHelper.attachToRecyclerView(nRecyclerView);
        final int color = ContextCompat.getColor(PostViewImage.this, R.color.lightcolor);
        final int color_1 = ContextCompat.getColor(PostViewImage.this, R.color.bg);

        nRecyclerView.addItemDecoration(new DotsIndicatorDecoration(10, 30, 26, color, color_1));

        list = new ArrayList<>();
        RecyclerView.ItemAnimator animator = nRecyclerView.getItemAnimator();

        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }

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
        viewPool = new RecyclerView.RecycledViewPool();
        FirebaseDatabase.getInstance().getReference()
                .child("Myposts").child(currUid)
                .child(postID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    long ms = post.getTime();
                    long lastTime = ms;
                    GetTimeAgo getTimeAgo = new GetTimeAgo();
                    String lastSeenTime = getTimeAgo.getTimeAgo(lastTime, PostViewImage.this);
                    time.setText(lastSeenTime);

                    //Toast.makeText(PostViewImage.this,""+post.getPostid(),Toast.LENGTH_LONG).show();
                    //  linkLists=post.getLists();
                    try {
                        //   Toast.makeText(PostViewImage.this,""+post.getLists(),Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // likes.setText(""+post.getLikes_count());
                    isLiked(post, like, likes);
                    nRecyclerView.setRecycledViewPool(viewPool);
                    description.setText(post.getDescription());

                    System.out.println("000000000000" + post.getDescription());


                    count = likes.getText().toString();
//            likeCount=Long.parseLong(count);
                    setEditPostListener(post.getPostid(), description, post.getPublisher());
                    getCommentsCount(post.getPostid(), comments, post.getPublisher());

                    image_like_click.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (post.isPostAvailable()) {
                                if (post.getLikeButtonClickCount() < 5) {
                                    image_like_click.setEnabled(false);
                                    like_progress.setVisibility(VISIBLE);
                                    like.setVisibility(GONE);
                                    if (like.getTag() == "liked") {
                                        FirebaseDatabase.getInstance().getReference().child("Posts").child(post.getPostid()).child("likes")
                                                .child(currUid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                image_like_click.setEnabled(true);
                                                like_progress.setVisibility(GONE);
                                                like.setVisibility(VISIBLE);
                                                if (!post.getPublisher().equals(currUid)) {
                                                    deleteNotifications(post.getPostid(), post.getPublisher());
                                                }
                                                like.setTag("like");
                                                LikeHandler(post, like, likes);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                image_like_click.setEnabled(true);
                                                like_progress.setVisibility(GONE);
                                                like.setVisibility(VISIBLE);
                                            }
                                        });
                                        
                                    } else if (like.getTag() == "like") {
                                        FirebaseDatabase.getInstance().getReference().child("Posts").child(post.getPostid()).child("likes")
                                                .child(currUid).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                image_like_click.setEnabled(true);
                                                like_progress.setVisibility(GONE);
                                                like.setVisibility(VISIBLE);
                                                like.setTag("liked");
                                                LikeHandler(post, like, likes);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                image_like_click.setEnabled(true);
                                                like_progress.setVisibility(GONE);
                                                like.setVisibility(VISIBLE);
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(PostViewImage.this, "You have reached your limit. Please try again later.", Toast.LENGTH_SHORT).show();
                                }
                                post.setLikeButtonClickCount(post.getLikeButtonClickCount() + 1);

                            } else {
                                Toast.makeText(PostViewImage.this, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    image_download.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (post.isPostAvailable()) {
                                //List<String> links=new ArrayList<>(post.getLinks().values());
                                String[] uri = post.getLinks().values().toArray(new String[0]);
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && PostViewImage.this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, (Integer) MY_PERMISSION);
                                } else {
                                    //new ImageDownload().execute(uri);
                                    for (String url : uri) {
                                        new DownloadUtil().checkAndLoad(PostViewImage.this, url, "Image");
                                    }
                                }
                            } else {
                                Toast.makeText(PostViewImage.this, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    getLinks(mainAdapter, post.getPostid(), nRecyclerView, list, mainAdapter, post.getLinks(), post.isPostAvailable());

                    //  postDesign(post,post_image);
                    publisherInfo(image_profile, username, publisher, post.getPublisher());
                    isSaved(post.getPostid(), save);
                    //nrLikes(likes, post.getPostid());
                    // getCommentsCount(post.getPostid(), comments, post.getPublisher());

                    image_profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                  /*  SharedPreferences.Editor editor = PostViewImage.this.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", post.getPublisher());
                    editor.apply();*/
                            Intent intent = new Intent(PostViewImage.this, ProfileActivity.class);
                            intent.putExtra("publisherid", post.getPublisher());
                            PostViewImage.this.startActivity(intent);
                   /* ((FragmentActivity)PostViewImage.this).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();*/
                        }
                    });


                    username.setOnClickListener(new View.OnClickListener() {
                        @Override

                        public void onClick(View view) {
                            Intent intent = new Intent(PostViewImage.this, ProfileActivity.class);
                            intent.putExtra("publisherid", post.getPublisher());
                            PostViewImage.this.startActivity(intent);

                        }
                    });

                    comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (post.isPostAvailable()) {
                                Intent intent = new Intent(PostViewImage.this, CommentsActivity.class);
                                intent.putExtra("postid", post.getPostid());
                                intent.putExtra("postType", "image");
                                intent.putExtra("publisherid", post.getPublisher());
                                PostViewImage.this.startActivity(intent);
                            } else {
                                Toast.makeText(PostViewImage.this, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (post.isPostAvailable()) {
                                PopupMenu popupMenu = new PopupMenu(PostViewImage.this, view);
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
                                Toast.makeText(PostViewImage.this, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                            }
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
                        Glide.with(PostViewImage.this).load(user.getThumb()).into(image_profile);
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
                    // Toast.makeText(PostViewImage.this,""+postId,Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(PostViewImage.this, "Write something", Toast.LENGTH_LONG).show();
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
                                Toast.makeText(PostViewImage.this, "Reported successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                alertDialogX.dismiss();
                                Toast.makeText(PostViewImage.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(PostViewImage.this, "Please select reason", Toast.LENGTH_LONG).show();
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
                Toast.makeText(PostViewImage.this, "Deleted Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void getLinks(MultipleImageAdapter mainAdapter, String postid, final RecyclerView recyclerView, ArrayList<String> list, final MultipleImageAdapter adapter, Map<String, String> lists, boolean postAvailable) {
        list = new ArrayList<>(lists.values());
        mainAdapter = new MultipleImageAdapter(PostViewImage.this, list, postAvailable);
        mainAdapter.setHasStableIds(true);
        recyclerView.setAdapter(mainAdapter);
        mainAdapter.notifyDataSetChanged();
    }

    private void isSaved(final String postid, final ImageView imageView) {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Saves").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postid).exists()) {
                } else {
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}