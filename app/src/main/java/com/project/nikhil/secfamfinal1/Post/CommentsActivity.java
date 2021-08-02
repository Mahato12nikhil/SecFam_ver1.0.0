package com.project.nikhil.secfamfinal1.Post;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.Model.Comment;
import com.project.nikhil.secfamfinal1.Model.User;
import com.project.nikhil.secfamfinal1.Notification.APIService;
import com.project.nikhil.secfamfinal1.Notification.Client;
import com.project.nikhil.secfamfinal1.Notification.Data;
import com.project.nikhil.secfamfinal1.Notification.MyResponse;
import com.project.nikhil.secfamfinal1.Notification.Sender;
import com.project.nikhil.secfamfinal1.Notification.Token;
import com.project.nikhil.secfamfinal1.R;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.project.nikhil.secfamfinal1.constant.Constant.NOTIFICATION_TYPE_COMMENT;

public class CommentsActivity extends BaseActivity {
    LinearLayout finshL;
    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private String postType;
    private String name;
    private String myId;
    EditText addcomment;
    ImageView image_profile;
    private String userName,userThumb;
    ImageView post;
    TextView tv_no_comments;
    String postid;
    String publisherid;
    private APIService apiService;

    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        Log.i("!!!Activity", "CommentsActivity");

        finshL = findViewById(R.id.finshL);
        intitilizeID();
       postType=getIntent().getStringExtra("postType");
       /* Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
       /* toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/
        myId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        Intent intent = getIntent();
        postid = intent.getStringExtra("postid");
        publisherid = intent.getStringExtra("publisherid");

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(CommentsActivity.this, commentList, postid);
        recyclerView.setAdapter(commentAdapter);

        post = findViewById(R.id.post);
        addcomment = findViewById(R.id.add_comment);
        addcomment.setMovementMethod(LinkMovementMethod.getInstance());
        image_profile = findViewById(R.id.image_profile);
        tv_no_comments = findViewById(R.id.tv_no_comments);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addcomment.getText().toString().equals("")){
                    Toast.makeText(CommentsActivity.this, "Write something", Toast.LENGTH_SHORT).show();
                } else {
                    addComment();
                }
            }
        });

        getImage();
        readComments();
        finshL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void intitilizeID() {
        //  post=findViewById()
    }

    private void addComment(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postid).child("comments");

        String commentid = reference.push().getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("comment", addcomment.getText().toString());
        hashMap.put("publisher", firebaseUser.getUid());
        hashMap.put("commentid", commentid);

        reference.child(commentid).setValue(hashMap);
        addNotification();
        DatabaseReference reference23 = FirebaseDatabase.getInstance().getReference("Users").child(myId);

        reference23.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recyclerView.scrollToPosition(commentList.size()-1);
                name = dataSnapshot.child("name").getValue().toString();
                // name=user.getName();
                //sendNotification(publisherid,name, "commented on your post");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        addcomment.setText("");

    }

    private void addNotification(){

        if(!publisherid.equals(firebaseUser.getUid())) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(publisherid);


            String pushID = reference.push().getKey();

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("userid", firebaseUser.getUid());
            hashMap.put("text", "commented: " + addcomment.getText().toString());
            hashMap.put("postid", postid);
            hashMap.put("notificationID", pushID);
            hashMap.put("postType",postType);
            hashMap.put("ispost", true);
            hashMap.put("timestamp", ServerValue.TIMESTAMP);
            hashMap.put("type", "comment");

            reference.child(pushID).setValue(hashMap);
        }


    }

    private void getImage(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                assert user != null;
                Glide.with(getApplicationContext()).load(user.getImage()).into(image_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readComments(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postid).child("comments");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Comment comment = snapshot.getValue(Comment.class);
                    commentList.add(comment);
                }

                if (commentList.size()==0){
                    tv_no_comments.setVisibility(View.VISIBLE);
                }else {
                    tv_no_comments.setVisibility(View.GONE);
                }

                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void sendNotification(final String key, final String name, final String s) {

//        Toast.makeText(getActivity(), "call", Toast.LENGTH_SHORT).show();

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("tokenid");
        Query query = tokens.orderByKey().equalTo(key);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final Token token = snapshot.getValue(Token.class);
                    Data data = new Data(myId, name,s, "",
                            key,NOTIFICATION_TYPE_COMMENT,"",0);

                    final Sender sender = new Sender(data, token.getTokenid());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){

                                            //  Toast.makeText(getActivity(), "sender="+token.getTokenid(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}