package com.project.nikhil.secfamfinal1.Chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.Model.User;
import com.project.nikhil.secfamfinal1.Notification.APIService;
import com.project.nikhil.secfamfinal1.Notification.Client;
import com.project.nikhil.secfamfinal1.Notification.Data;
import com.project.nikhil.secfamfinal1.Notification.MyResponse;
import com.project.nikhil.secfamfinal1.Notification.Sender;
import com.project.nikhil.secfamfinal1.Profile.ProfileActivity;
import com.project.nikhil.secfamfinal1.R;
import com.project.nikhil.secfamfinal1.constant.Constant;
import com.project.nikhil.secfamfinal1.utils.CalculateTimeAgo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.project.nikhil.secfamfinal1.constant.Constant.NOTIFICATION_TYPE_CHAT;

public class ConversationActivity extends BaseActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    RelativeLayout belowET;
    EditText chatET;
    //PopupWindow popupWindow;
    ImageView sendimg, icon_bck;
    private boolean notify = false;
    PopupWindow popUp;
    LinearLayout layout;
    ArrayList<Uri> file_list = new ArrayList<Uri>();
    int radiusArr[];
    private String messageDesc = "";
    String push_id;
    private static int MSG_IMAGE = 4576, MSG_VIDEO = 3476, MSG_DOCS = 2378, MSG_AUDIO = 9483;
    TextView tv;
    ImageView more;
    //fab
    private Boolean isFabOpen = false;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward, fabopen2, fabopen3, fab_close2, fab_close3;
    private ImageButton fab;
    WindowManager.LayoutParams params;
    LinearLayout mainLayout;
    DatabaseReference user_message_push;

    boolean click = true;
    ConversationViewAdapter messageListAdapter;
    private String mChatUser;
    private String mChatUserToken;
    private Toolbar mChatToolbar;
    private DatabaseReference mRootRef;
    private TextView mUserName;
    private TextView mLastSeenView;
    private CircleImageView mProfileImage;
    private FirebaseAuth mAuth;
    //   private ImageButton mChatAddBtn;
    private ImageView mChatSendBtn;
    private EditText mChatMessageView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mRefreshLayout;
    private UploadTask uploadTask;
    private List<MessageModel> messagesList;
    private List<MessageModel> tempMessagesList;
    private LinearLayoutManager mLinearLayout;
    private ConversationViewAdapter mAdapter;
    private Toolbar main_app_bar;
    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private int mCurrentPage = 1;
    private String latitudetext, longitudetext;
    private static final int GALLERY_PICK = 1;
    // Storage Firebase
    private StorageReference mImageStorage;
    private ImageView BlurImage;
    //New Solution
    MenuItem deletec, blocku;
    private int itemPos = 0;
    private String mLastKey = "";
    private String mPrevKey = "";
    TextView online, typing;
    String a = "1";
    Timer timer;
    //MenuItem dltC,blkU;
    PopupMenu popup;
    boolean checkBlocked = false;
    boolean amIBlocked = false;
    private int unreadMessageCount = 0;

    private int currentPost, totalPost, scrolledPost;
    Boolean isMaxData = false;
    String last_node = "", last_Key;
    //private int messageFetchingLimit = 10;
    String currentUserName = "";
    String mobile = "";

    APIService apiService;
    Query newMessageQuery;
    DatabaseReference checkTypingRef, checkOnlineRef, checkBlockRef, imBLockedOrNotRef, unreadMessageCountRef;
    ValueEventListener newMessageQueryListener, checkTypingRefListener, checkOnlineRefListener, checkBlockRefListener, imBLockedOrNotRefListener, unreadMessageCountRefListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Log.i("!!!Activity", "ConversationActivity");
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        more = (ImageView) findViewById(R.id.more);
        popup = new PopupMenu(ConversationActivity.this, more);
        popup.setOnMenuItemClickListener(ConversationActivity.this);
        popup.inflate(R.menu.popupmenu);
        belowET = findViewById(R.id.belowET);
        sendimg = findViewById(R.id.chat_send_btn);
        icon_bck = findViewById(R.id.icon_bck);
        chatET = findViewById(R.id.chat_message_view);
        popUp = new PopupWindow(this);
        layout = new LinearLayout(this);
        mChatUser = getIntent().getStringExtra("id");
        String getName = getIntent().getStringExtra("user_name");
        Log.i("!!!!id+user", getIntent().getStringExtra("id")+" "+getIntent().getStringExtra("user_name"));
        online = (TextView) findViewById(R.id.online);
        mProfileImage = (CircleImageView) findViewById(R.id.circle_img);
        mUserName = (TextView) findViewById(R.id.name);
        typing = (TextView) findViewById(R.id.type);
        mRootRef = FirebaseDatabase.getInstance().getReference();

        messagesList = new ArrayList<>();
        tempMessagesList = new ArrayList<>();
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.message_swipe_layout);

        recyclerView = (RecyclerView) findViewById(R.id.messages_list);
        mLinearLayout = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        //mLinearLayout.setReverseLayout(true);
        //mLinearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(mLinearLayout);
        messageListAdapter = new ConversationViewAdapter(ConversationActivity.this, messagesList, mChatUser, myid);
        recyclerView.setAdapter(messageListAdapter);
        // updateOnline(true);

        if (app.pref.isNotificationChannelIdExist(mChatUser)) {
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(app.pref.getNotificationChannelData(mChatUser).getChannelId());
            app.pref.removeChannelId(mChatUser);
        }


        //loadMessages();

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.show();
            }
        });

        System.out.println("/////////" + getName + ".........");
        if (getName != null)
            mUserName.setText(getName);

        FirebaseDatabase.getInstance().getReference().child("Users").child(myid).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserName = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Users").child(myid).child("phone").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mobile = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(mChatUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getImage() != null && !user.getImage().equals(""))
                    Glide.with(getApplicationContext()).load(user.getImage()).into(mProfileImage);
                else
                    Glide.with(getApplicationContext()).load(R.drawable.man_placeholder).into(mProfileImage);

                mChatUserToken = user.getTokenid();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
                }, 2000);
            }
        });
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isMaxData) {
                    getMoreMsg();
                } else {
                    mRefreshLayout.setRefreshing(false);
                    Toast.makeText(ConversationActivity.this, "No more data is available.", Toast.LENGTH_SHORT).show();
                }
                //mCurrentPage++;
                //  itemPos = 0;
                // loadMoreMessages();
            }
        });
        sendimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
                //typing.setTextSize(0);
            }
        });

        icon_bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoUserProfile();
            }
        });
        mUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoUserProfile();
            }
        });

    }

    private void GotoUserProfile() {
        Intent intent = new Intent(ConversationActivity.this, ProfileActivity.class);
        intent.putExtra("publisherid", mChatUser);
        startActivity(intent);
    }

    private void setUnreadMessageCount() {
        unreadMessageCountRef = FirebaseDatabase.getInstance().getReference()
                .child("ChatList").child(mChatUser).child(myid).child("unreadMessageCount");
        unreadMessageCountRefListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    unreadMessageCount = snapshot.getValue(Integer.class);
                } catch (Exception e) {
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        unreadMessageCountRef.addValueEventListener(unreadMessageCountRefListener);
    }

/*
    private void updateOnline(boolean b) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("Chat").child(mChatUser).child(myid).child("isOnline");
        ref.setValue(b);
    }
*/

    private void checkOnline() {
        checkOnlineRef = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(mChatUser).child("isOnline");
        checkOnlineRefListener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String check1 = snapshot.getValue(String.class);
                    if (check1 == null) {
                        online.setVisibility(View.GONE);
                        typing.setVisibility(View.GONE);
                    } else if (check1.equals("online")) {
                        online.setVisibility(View.VISIBLE);
                        typing.setVisibility(View.GONE);
                        online.setText("Online");
                    } else if (check1.equals("")) {
                        online.setVisibility(View.GONE);
                        typing.setVisibility(View.GONE);
                    } else {
                        online.setText(CalculateTimeAgo.getTimeAgo(Long.parseLong(check1)));
                        online.setVisibility(View.VISIBLE);
                        typing.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        checkOnlineRef.addValueEventListener(checkOnlineRefListener);

    }

    private void updateTyping(boolean b) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                .child("ChatList").child(mChatUser).child(myid).child("typing");
        ref.setValue(b);
    }

    private void checkTyping() {
        checkTypingRef = FirebaseDatabase.getInstance().getReference()
                .child("ChatList").child(myid).child(mChatUser).child("typing");
        checkTypingRefListener = new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Boolean check = snapshot.getValue(Boolean.class);
                    if (check) {
                        typing.setVisibility(View.VISIBLE);
                        online.setVisibility(View.GONE);
                    } else {
                        typing.setVisibility(View.GONE);
                        online.setVisibility(View.VISIBLE);
                        //checkOnline();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        checkTypingRef.addValueEventListener(checkTypingRefListener);

    }

    private void sendMessage() {
        final String message = chatET.getText().toString().trim();
        if (!TextUtils.isEmpty(message)) {
            notify = true;
            String current_user_ref = "messages/" + myid + "/" + mChatUser;
            String chat_user_ref = "messages/" + mChatUser + "/" + myid;

            user_message_push = mRootRef.child("messages")
                    .child(myid).child(mChatUser).push();

            String push_id = user_message_push.getKey();
            new MessageModel().setId(push_id);
            long timeStamp = System.currentTimeMillis();

            MessageModel model = new MessageModel(push_id, myid, message, "text", Constant.MESSAGE_STATUS_PENDING, timeStamp);
            Map<String, Object> messageUserMap = new HashMap<>();
            messageUserMap.put(current_user_ref + "/" + push_id, model);
            messageUserMap.put(chat_user_ref + "/" + push_id, model);
            chatET.setText("");
            mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Log.d("CHAT_LOG", databaseError.getMessage().toString());
                    } else {
                        FirebaseDatabase.getInstance().getReference()
                                .child("messages").child(mChatUser).child(myid)
                                .child(push_id).child("messageStatus")
                                .setValue(Constant.MESSAGE_STATUS_SENT);
                        sendNotification(mChatUserToken, currentUserName, push_id);
                    }
                }
            });
            Chat chat = new Chat(mChatUser, message, timeStamp, 0, false);
            FirebaseDatabase.getInstance().getReference()
                    .child("ChatList").child(myid).child(mChatUser).setValue(chat);

            unreadMessageCount++;
            Chat chat2 = new Chat(myid, message, timeStamp, unreadMessageCount, false);
            Log.i("!!!unreadMessageCount", myid + ":" + unreadMessageCount);
            FirebaseDatabase.getInstance().getReference()
                    .child("ChatList").child(mChatUser).child(myid).setValue(chat2);
          /*
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                    .child("Chat").child(myid).child(mChatUser).child("timestamp");
            ref.setValue(ServerValue.TIMESTAMP);
            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference()
                    .child("Chat").child(mChatUser).child(myid).child("timestamp");
            ref1.setValue(ServerValue.TIMESTAMP);
*/





      /*mRootRef.child("Chat").child(myid).child(mChatUser).child("seen").setValue(true);
      mRootRef.child("Chat").child(myid).child(mChatUser).child("timestamp").setValue(ServerValue.TIMESTAMP);

      mRootRef.child("Chat").child(mChatUser).child(myid).child("seen").setValue(false);
      mRootRef.child("Chat").child(mChatUser).child(myid).child("timestamp").setValue(ServerValue.TIMESTAMP);*/


        }
    }

    ///Loading messages
    private void getLastKey() {
        FirebaseDatabase.getInstance().getReference()
                .child("ChatList").child(myid).child(mChatUser).child("unreadMessageCount").setValue(0);
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("messages")
                .child(myid)
                .child(mChatUser)
                .orderByKey()
                .limitToFirst(1);
        query.keepSynced(true);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    last_Key = ds.getKey();
                    Log.i("!!!!last_Key", last_Key);
                }
                loadMsg();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    public void updateKey(String key) {
        last_Key = key;
        last_node = messagesList.get(0).getId();
       /* if (Recycler View is Empty or short of data){
            getMoreMsg();
        }*/

    }

    private void loadMsg() {
        isMaxData = false;
        recyclerView.removeAllViews();
        messagesList.clear();
        Query postQuery = FirebaseDatabase.getInstance().getReference().child("messages").child(myid)
                .child(mChatUser)
                .orderByKey().limitToLast(Constant.CHAT_MESSAGE_FETCHING_LIMIT);
        postQuery.keepSynced(true);
        postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SetNewMessageListener();
                mRefreshLayout.setRefreshing(false);
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MessageModel model = snapshot.getValue(MessageModel.class);
                        if (model != null && model.getId() != null) {
                            if (model.getId().equals(last_Key)) {
                                isMaxData = true;
                            }
                            tempMessagesList.add(model);
                            Log.i("!!!!id", model.getId());
                        }
                    }
                    if (tempMessagesList.size() > 0) {
                        messagesList.addAll(tempMessagesList);
                        messageListAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(messagesList.size() - 1);
                        last_node = messagesList.get(0).getId();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mRefreshLayout.setRefreshing(false);
                Toast.makeText(ConversationActivity.this, "Problem in loading more ...", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void getMoreMsg() {
        if (!isMaxData && messagesList.size() > 0) {
            Query postQuery = FirebaseDatabase.getInstance().getReference().child("messages")
                    .child(myid)
                    .child(mChatUser)
                    .orderByKey().endAt(last_node).limitToLast(Constant.CHAT_MESSAGE_FETCHING_LIMIT);
            postQuery.keepSynced(true);
            postQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    mRefreshLayout.setRefreshing(false);
                    tempMessagesList.clear();
                    if (dataSnapshot.hasChildren()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            MessageModel model = snapshot.getValue(MessageModel.class);
                            if (model != null && model.getId() != null) {
                                if (model.getId().equals(last_Key)) {
                                    isMaxData = true;
                                }
                                Log.i("!!!!id", model.getId());
                                tempMessagesList.add(model);
                            }
                        }
                        tempMessagesList.remove(tempMessagesList.size()-1);//Remove, because it is already placed in the array list
                        if (tempMessagesList.size() > 0) {
                            messagesList.addAll(0, tempMessagesList);
                            messageListAdapter.notifyDataSetChanged();
                            last_node = tempMessagesList.get(0).getId();
                            recyclerView.scrollToPosition(tempMessagesList.size()+(mLinearLayout.findLastVisibleItemPosition() - mLinearLayout.findFirstVisibleItemPosition()-2));
                            messageListAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    mRefreshLayout.setRefreshing(false);
                }
            });
        } else {
            mRefreshLayout.setRefreshing(false);
        }
    }

    private void SetNewMessageListener() {
        newMessageQuery = FirebaseDatabase.getInstance().getReference()
                .child("messages")
                .child(myid)
                .child(mChatUser)
                .orderByKey()
                .limitToLast(1);

        newMessageQuery.keepSynced(true);
        newMessageQueryListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MessageModel model = snapshot.getValue(MessageModel.class);
                        if (model != null && model.getId() != null) {
                            if (model.getId().equals(last_Key)) {
                                isMaxData = true;
                            }
                            FirebaseDatabase.getInstance().getReference()
                                    .child("ChatList").child(myid).child(mChatUser).child("unreadMessageCount").setValue(0);
                            FirebaseDatabase.getInstance().getReference()
                                    .child("messages").child(myid).child(mChatUser).child(model.getId()).child("messageStatus").setValue(Constant.MESSAGE_STATUS_SEEN);

                            for (int i = 0; i < messagesList.size(); i++) {
                                if (model.equals(messagesList.get(i))) {
                                    return;
                                }
                            }
                            if (messagesList.size() == 0) {
                                last_Key = model.getId();
                            }
                            messagesList.add(model);
                            messageListAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(messagesList.size() - 1);
                            last_node = messagesList.get(0).getId();

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        newMessageQuery.addValueEventListener(newMessageQueryListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        Constant.CURRENT_CHAT_USER_ID = mChatUser;
        messageListAdapter.notifyDataSetChanged();//call for reactivate listener in Adapter
        setUnreadMessageCount();
        checkBlocked();
        imBLockedOrNotForMessage();
        checkTyping();
        checkOnline();
        getLastKey();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constant.CURRENT_CHAT_USER_ID = "";
        removeAllListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constant.CURRENT_CHAT_USER_ID = "";
        removeAllListener();
    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            startActivity(new Intent(this, ChatHomeActivity.class));
        }
        super.onBackPressed();
    }

    private void removeAllListener() {
        if (newMessageQuery != null) {
            newMessageQuery.removeEventListener(newMessageQueryListener);
        }
        if (checkTypingRef != null) {
            checkTypingRef.removeEventListener(checkTypingRefListener);
        }
        if (checkOnlineRef != null) {
            checkOnlineRef.removeEventListener(checkOnlineRefListener);
        }
        if (checkBlockRef != null) {
            checkBlockRef.removeEventListener(checkBlockRefListener);
        }
        if (checkBlockRef != null) {
            checkBlockRef.removeEventListener(checkBlockRefListener);
        }
        if (imBLockedOrNotRef != null) {
            imBLockedOrNotRef.removeEventListener(imBLockedOrNotRefListener);
        }
        if (unreadMessageCountRef != null) {
            unreadMessageCountRef.removeEventListener(unreadMessageCountRefListener);
        }

        messageListAdapter.removeAllListener();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteC:
                deleteConversation();
                break;
            case R.id.blockU:
                //Toast.makeText(getApplicationContext(),"Blocked not implemented yet",Toast.LENGTH_LONG).show();
                if (item.getTitle().equals("Block from texting")) {
                    BlockUserMessage(item);
                } else {
                    UnblockUserMessage(item);
                }
                return true;
            default:
        }
        return false;
    }

    private boolean deleteConversation() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConversationActivity.this);
        alertDialog.setTitle("Do you want to delete Conversation?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseDatabase.getInstance().getReference().child("messages").child(myid).child(mChatUser).removeValue();
                FirebaseDatabase.getInstance().getReference().child("ChatList").child(myid).child(mChatUser).removeValue();
                messagesList.clear();
                messageListAdapter.notifyDataSetChanged();
                messageListAdapter.setItemcount();

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

        return true;
    }

    private void checkBlocked() {
        checkBlockRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(myid).child("BlockedUsersMessage").child(mChatUser).child("isBlocked");
        checkBlockRefListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String isBlocked = snapshot.getValue(String.class);
                    if (isBlocked.equals("true")) {
                        popup.getMenu().getItem(1).setTitle("Unblock from texting");
                        belowET.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "This user is blocked by you", Toast.LENGTH_LONG).show();
                        checkBlocked = true;
                    } else {
                        checkBlocked = false;
                        popup.getMenu().getItem(1).setTitle("Block from texting");
                        if (!amIBlocked) {
                            belowET.setVisibility(View.VISIBLE);
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        };
        checkBlockRef.addValueEventListener(checkBlockRefListener);
    }

    private void imBLockedOrNotForMessage() {
        imBLockedOrNotRef = FirebaseDatabase.getInstance().getReference("Users").child(mChatUser).child("BlockedUsersMessage").child(myid).child("isBlocked");
        imBLockedOrNotRefListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String isBlocked = snapshot.getValue(String.class);
                    if (isBlocked.equals("true")) {
                        amIBlocked = true;
                        belowET.setVisibility(View.GONE);
                        if (!checkBlocked) {
                            Toast.makeText(getApplicationContext(), "You are blocked by this user",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        amIBlocked = false;
                        if (checkBlocked) {
                            belowET.setVisibility(View.GONE);
                        } else {
                            belowET.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        };
        imBLockedOrNotRef.addValueEventListener(imBLockedOrNotRefListener);
    }

    private void BlockUserMessage(MenuItem item) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("isBlocked", "true");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(myid).child("BlockedUsersMessage").child(mChatUser).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void UnblockUserMessage(MenuItem item) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(myid).child("BlockedUsersMessage").child(mChatUser).child("isBlocked").setValue("false").
                addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Unblock Successful",
                                Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private void sendNotification(String token, String name, String pushId) {
        int randomId = (int) (Math.random() * 10000);
        Data data = new Data(myid, name, " new message", name, "0", NOTIFICATION_TYPE_CHAT, pushId, randomId);
        Sender sender = new Sender(data, token);
        apiService.sendNotification(sender)
                .enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {}
                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {}
                });
    }


    /*

    private void loadMessages() {
        FirebaseDatabase.getInstance().getReference()
                .child("ChatList").child(myid).child(mChatUser).child("unreadMessageCount").setValue(0);
        DatabaseReference messageRef = mRootRef.child("messages").child(myid).child(mChatUser);
        final Query messageQuery = messageRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);
        messageQuery.keepSynced(true);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                MessageModel message1 = dataSnapshot.getValue(MessageModel.class);
                itemPos++;
                if (itemPos == 1) {
                    String messageKey = dataSnapshot.getKey();
                    mLastKey = messageKey;
                    mPrevKey = messageKey;
                }

                messagesList.add(message1);
                // Toast.makeText(getApplicationContext(),"aaaaaaaaa"+messagesList.size(),Toast.LENGTH_LONG).show();
                messageListAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messagesList.size() - 1);
                new MessageModel().setIsSeen(true);
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

        DatabaseReference messageRef = mRootRef.child("messages").child(myid).child(mChatUser);
        Query messageQuery = messageRef.orderByKey().endAt(mLastKey).limitToLast(15);
        messageQuery.keepSynced(true);
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                MessageModel message = dataSnapshot.getValue(MessageModel.class);
                String messageKey = dataSnapshot.getKey();

                if (!mPrevKey.equals(messageKey)) {
                    messagesList.add(itemPos++, message);
                } else {
                    mPrevKey = mLastKey;
                }
                if (itemPos == 1) {
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
*/
}

