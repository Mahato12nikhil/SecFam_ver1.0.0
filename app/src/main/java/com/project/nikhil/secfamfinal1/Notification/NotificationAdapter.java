package com.project.nikhil.secfamfinal1.Notification;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.Model.User;
import com.project.nikhil.secfamfinal1.Post.ExoPlayerActivity;
import com.project.nikhil.secfamfinal1.Post.LinkOpenerActivity;
import com.project.nikhil.secfamfinal1.Post.Post;
import com.project.nikhil.secfamfinal1.Post.ShowAlIPostmages;
import com.project.nikhil.secfamfinal1.Profile.ProfileActivity;
import com.project.nikhil.secfamfinal1.R;
import com.project.nikhil.secfamfinal1.postView.PostViewImage;
import com.project.nikhil.secfamfinal1.postView.PostViewLink;
import com.project.nikhil.secfamfinal1.postView.PostViewText;
import com.project.nikhil.secfamfinal1.postView.PostViewVideo;
import com.project.nikhil.secfamfinal1.utils.MyUtil;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Notification_Model> notificationList;

    public NotificationAdapter(Context context, List<Notification_Model> notificationList){
        this.mContext = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public NotificationAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notificaion_item, parent, false);
        return new NotificationAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.ImageViewHolder holder, final int position) {


        final Notification_Model notification = notificationList.get(position);
        holder.text.setText(notification.getText());
        holder.notification_date_time.setText(MyUtil.timestampToDateTime(notification.getTimestamp()));

        getUserInfo(holder.image_profile, holder.username, notification.getUserid());

        if (notification.isIspost()) {
           // holder.post_image.setVisibility(View.VISIBLE);
          //  getPostImage(holder.post_image, notification.getPostid());
        } else {
           // holder.post_image.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (notification.isIspost()) {
                    if(notification.getPostType().equals("video")){
                        Intent intent=new Intent(mContext, PostViewVideo.class);
                        intent.putExtra("POST_ID",notification.getPostid());
                        mContext.startActivity(intent);
                    }
                    else if(notification.getPostType().equals("link")){
                        Intent intent=new Intent(mContext, PostViewLink.class);
                        intent.putExtra("POST_ID",notification.getPostid());
                        mContext.startActivity(intent);
                    }
                    else if(notification.getPostType().equals("image")){
                        Intent intent=new Intent(mContext, PostViewImage.class);
                        intent.putExtra("POST_ID",notification.getPostid());
                        mContext.startActivity(intent);
                    } else if(notification.getPostType().equals("text")){
                        Intent intent=new Intent(mContext, PostViewText.class);
                        intent.putExtra("POST_ID",notification.getPostid());
                        mContext.startActivity(intent);
                    }

                   /* SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("postid", notification.getPostid());
                    editor.apply();
*/
                   /* ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new PostDetailFragment()).commit();*/
                } else {




                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", notification.getUserid());
                    editor.apply();
                    Intent intent=new Intent(mContext, ProfileActivity.class);
                    intent.putExtra("publisherid",notification.getUserid());
                    mContext.startActivity(intent);


                  /*  ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();*/
                }
            }
        });



    }
    //
    @Override
    public int getItemCount() {

        return notificationList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_profile, post_image;
        public TextView username, text,notification_date_time;

        public ImageViewHolder(View itemView) {
            super(itemView);

            image_profile = itemView.findViewById(R.id.image_profile);
           //post_image = itemView.findViewById(R.id.post_image);
            username = itemView.findViewById(R.id.username);
            text = itemView.findViewById(R.id.comment);
            notification_date_time = itemView.findViewById(R.id.notification_date_time);
        }
    }

    private void getUserInfo(final ImageView imageView, final TextView username, String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(publisherid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    User user = dataSnapshot.getValue(User.class);
                    if(user.getImage()!=null){
                        Glide.with(mContext).load(user.getImage()).into(imageView);
                    }
                    else {
                        Glide.with(mContext).load(R.drawable.man_placeholder).into(imageView);
                    }

                    username.setText(user.getName());
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

 /*   @Override
    public long getItemId(int position) {
        return mNotification.hashCode();
    }*/

    private void getPostImage(final ImageView post_image, String postid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Posts").child(postid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                try {
                    Glide.with(mContext).load(post.getImageUrl()).into(post_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    /*public void addAll(final List<Notification_Model> newUsers) {
        int initialSize = mNotification.size();


        mNotification.addAll(newUsers);

      //  notifyDataSetChanged();
        if(initialSize==0)
        {
            notifyDataSetChanged();
        }
        else {
            notifyItemRangeInserted(initialSize,newUsers.size());

        }

        //notifyItemInserted(initialSize, newUsers.size());


    }*/
}