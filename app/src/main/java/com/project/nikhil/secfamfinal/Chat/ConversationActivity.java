package com.project.nikhil.secfamfinal.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.nikhil.secfamfinal.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ConversationActivity extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    EditText chatET;
    //PopupWindow popupWindow;
    DatabaseReference ref;
    ImageView sendimg;
    private boolean notify = false;
    PopupWindow popUp;
    LinearLayout layout;
    ArrayList<Uri> file_list=new ArrayList<Uri>();
    int radiusArr[];
    private  String messageDesc="";
    String push_id;
    private static int MSG_IMAGE=4576,MSG_VIDEO=3476,MSG_DOCS=2378,MSG_AUDIO=9483;
    TextView tv;
    ImageView more;
    //fab
    private Boolean isFabOpen = false;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward,fabopen2,fabopen3,fab_close2,fab_close3;
    private ImageButton fab;
    WindowManager.LayoutParams params;
    LinearLayout mainLayout;
    DatabaseReference user_message_push;

    boolean click = true;
    ConversationViewAdapter messageListAdapter;
    private String mChatUser;
    private Toolbar mChatToolbar;
    private DatabaseReference mRootRef;
    private TextView mUserName;
    private TextView mLastSeenView;
    private CircleImageView mProfileImage;
    private FirebaseAuth mAuth;
    private String mCurrentUserId;
    //   private ImageButton mChatAddBtn;
    private ImageView mChatSendBtn;
    private EditText mChatMessageView;
    private RecyclerView mMessagesList;
    private SwipeRefreshLayout mRefreshLayout;
    private UploadTask uploadTask;
    private  List<ConversationActivity_Data_Model> messagesList;
    private LinearLayoutManager mLinearLayout;
    private ConversationViewAdapter mAdapter;
    private Toolbar main_app_bar;
    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int mCurrentPage = 1;
    private  String latitudetext,longitudetext;
    private static final int GALLERY_PICK = 1;
    // Storage Firebase
    private StorageReference mImageStorage;
    private ImageView BlurImage;
    //New Solution
    MenuItem deletec,blocku;
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";
    TextView online,typing;
    String a="1";
    Timer timer;
    //MenuItem dltC,blkU;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        sendimg = findViewById(R.id.chat_send_btn);
        chatET = findViewById(R.id.chat_message_view);
        popUp = new PopupWindow(this);
        layout = new LinearLayout(this);
        mCurrentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();
        mChatUser=getIntent().getStringExtra("id");
        online=(TextView) findViewById(R.id.online);
        more=(ImageView)findViewById(R.id.more);
        mProfileImage=(CircleImageView)findViewById(R.id.circle_img);
        mUserName=(TextView)findViewById(R.id.name);
        typing=(TextView)findViewById(R.id.type);

        mMessagesList = (RecyclerView) findViewById(R.id.messages_list);
        messagesList=new ArrayList<>();
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_swipe_layout);
        mLinearLayout = new LinearLayoutManager(this);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mMessagesList.setHasFixedSize(true);
        mMessagesList.setLayoutManager(mLinearLayout);
        messageListAdapter=new ConversationViewAdapter(ConversationActivity.this,messagesList, mChatUser, mCurrentUserId);
        mMessagesList.setAdapter(messageListAdapter);
       // updateOnline(true);
        checkOnline();

        checkTyping();
        loadMessages();
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(ConversationActivity.this, v);
                popup.setOnMenuItemClickListener(ConversationActivity.this);
                popup.inflate(R.menu.popupmenu);
                popup.show();
            }
        });
          String getName=getIntent().getStringExtra("user_name");
          System.out.println("/////////"+getName+".........");
          if(getName!=null)
            mUserName.setText(getName);

        String dp=getIntent().getStringExtra("dp");

        if(dp!=null)
          Glide.with(getApplicationContext()).load(dp).into(mProfileImage);
        else
            Glide.with(getApplicationContext()).load(R.drawable.man_placeholder).into(mProfileImage);



        chatET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && !chatET.getText().toString().equals("")) {
                    sendimg.setVisibility(View.VISIBLE);

                    updateTyping(true);
                    if (timer != null) {
                        timer.cancel();
                    }

                } else {
                    sendimg.setVisibility(View.GONE);
                    updateTyping(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        updateTyping(false);
                    }
                }, 1000);

            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mCurrentPage++;

                itemPos = 0;

                loadMoreMessages();


            }
        });
        sendimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendMessage();
                typing.setTextSize(0);
            }
        });


    }



/*
    private void updateOnline(boolean b) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Chat").child(mChatUser).child(mCurrentUserId).child("isOnline");
        ref.setValue(b);
    }
*/

    private void checkOnline() {

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mChatUser).child("isOnline");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()) {
                    Boolean check1 = snapshot.getValue(Boolean.class);


                    if (check1) {
                        online.setVisibility(View.VISIBLE);
                        typing.setVisibility(View.GONE);
                       // typing.setTextSize(0);
                        online.setText("Online");
                        online.setTextSize(15);
                    } else {
                        online.setTextSize(0);
                        typing.setTextSize(0);
                        online.setVisibility(View.GONE);
                        typing.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void updateTyping(boolean b) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Chat").child(mChatUser).child(mCurrentUserId).child("isTyping");


        ref.setValue(b);

        checkTyping();


    }

    private void checkTyping() {

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Chat").child(mCurrentUserId).child(mChatUser).child("isTyping");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                if(snapshot.exists())
                {   Boolean check= snapshot.getValue(Boolean.class);
                    if(check)
                {
             //   online.setVisibility(View.GONE);
                typing.setVisibility(View.VISIBLE);
                    online.setTextSize(0);
                    typing.setText("Typing...");
                    typing.setTextSize(15);
                }
                else {
                   // typing.setTextSize(0);
                        typing.setVisibility(View.GONE);
                       /* online.setVisibility(View.VISIBLE);
                        online.setText("Online");
                        online.setTextSize(15);*/
                        checkOnline();
                }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void sendMessage() {


        final String message = chatET.getText().toString().trim();

        if (!TextUtils.isEmpty(message)) {

            notify = true;
            String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
            String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

            user_message_push = mRootRef.child("messages")
                    .child(mCurrentUserId).child(mChatUser).push();

            String push_id = user_message_push.getKey();
            new ConversationActivity_Data_Model().setId(push_id);



            Map messageMap = new HashMap();
            messageMap.put("message", message);
            messageMap.put("type", "text");
            messageMap.put("time", ServerValue.TIMESTAMP);
            messageMap.put("from", mCurrentUserId);
            messageMap.put("id",push_id);
            messageMap.put("isSeen",false);


            Map messageUserMap = new HashMap();
            messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
            messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);
            chatET.setText("");
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                    .child("Chat").child(mCurrentUserId).child(mChatUser).child("timestamp");


            ref.setValue(ServerValue.TIMESTAMP);
            DatabaseReference ref1=FirebaseDatabase.getInstance().getReference()
                    .child("Chat").child(mChatUser).child(mCurrentUserId).child("timestamp");


            ref1.setValue(ServerValue.TIMESTAMP);
      /*mRootRef.child("Chat").child(mCurrentUserId).child(mChatUser).child("seen").setValue(true);
      mRootRef.child("Chat").child(mCurrentUserId).child(mChatUser).child("timestamp").setValue(ServerValue.TIMESTAMP);

      mRootRef.child("Chat").child(mChatUser).child(mCurrentUserId).child("seen").setValue(false);
      mRootRef.child("Chat").child(mChatUser).child(mCurrentUserId).child("timestamp").setValue(ServerValue.TIMESTAMP);*/

            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                    if (databaseError != null) {

                        Log.d("CHAT_LOG", databaseError.getMessage().toString());

                    }

                }
            });
        }
    }



    ///Loading messages
    private void loadMessages() {

        DatabaseReference messageRef = mRootRef.child("messages").child(mCurrentUserId).child(mChatUser);
        final Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                ConversationActivity_Data_Model message1 = dataSnapshot.getValue(ConversationActivity_Data_Model.class);

                itemPos++;

                if (itemPos == 1) {

                    String messageKey = dataSnapshot.getKey();

                    mLastKey = messageKey;
                    mPrevKey = messageKey;

                }

                messagesList.add(message1);
                // Toast.makeText(getApplicationContext(),"aaaaaaaaa"+messagesList.size(),Toast.LENGTH_LONG).show();
                messageListAdapter.notifyDataSetChanged();

                mMessagesList.scrollToPosition(messagesList.size() - 1);

                new ConversationActivity_Data_Model().setIsSeen(true);


                mRefreshLayout.setRefreshing(false);



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void loadMoreMessages() {

        DatabaseReference messageRef = mRootRef.child("messages").child(mCurrentUserId).child(mChatUser);

        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(15);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                ConversationActivity_Data_Model message = dataSnapshot.getValue(ConversationActivity_Data_Model.class);
                String messageKey = dataSnapshot.getKey();

                if(!mPrevKey.equals(messageKey)){

                    messagesList.add(itemPos++, message);

                } else {

                    mPrevKey = mLastKey;

                }


                if(itemPos == 1) {

                    mLastKey = messageKey;

                }


                Log.d("TOTALKEYS", "Last Key : " + mLastKey + " | Prev Key : " + mPrevKey + " | Message Key : " + messageKey);

                messageListAdapter.notifyDataSetChanged();

                mRefreshLayout.setRefreshing(false);

                //  mLinearLayout.scrollToPositionWithOffset(10,10*itemPos);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                messageListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                messageListAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    @Override
    public void onResume() {
        super.onResume();
        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){


        }

    }
    @Override
    public void onClick(View v) {

    }




    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.deleteC:
                deleteConversation();
                break;
            case R.id.blockU:
                Toast.makeText(getApplicationContext(),"Blocked",Toast.LENGTH_LONG).show();

                return true;
            default:

        }return false;}

    private boolean deleteConversation() {
        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("messages").child(mCurrentUserId).child(mChatUser);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConversationActivity.this);
        alertDialog.setTitle("Do you want to delete task?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ref.removeValue();
                        messagesList.clear();
                        messageListAdapter.notifyDataSetChanged();
                        messageListAdapter.setItemcount();
                        //System.out.println("++++++++++++" + messagesList.size());
                        mMessagesList.getAdapter().notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        alertDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#6B5B95"));

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#6B5B95"));

        return true;  }
}

