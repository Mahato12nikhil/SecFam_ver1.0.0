package com.project.nikhil.secfamfinal1.Profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.Post.Post;
import com.project.nikhil.secfamfinal1.R;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileMediaAdapter  extends RecyclerView.Adapter<ProfileMediaAdapter.MyViewHolder> {


    int []arr;

    List<Post> postList;
    Context context;
    Map<String,String> links;
    private FirebaseUser firebaseUser;
    public interface OnItemClickListener {
        void onItemClick(Post post);
    }
    private final OnItemClickListener listener;
    public ProfileMediaAdapter(List<Post> postList, Context context,OnItemClickListener listener) {
        this.postList = postList;
        this.context=context;
        this.listener = listener;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {

        View view = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.profile_media_image_item, parent,false);
        MyViewHolder myViewHolder = new MyViewHolder ( view );
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder , int position) {
        Post nPosts=postList.get(position);

        if (nPosts.getType().equals("image")){
            holder.play_icon.setVisibility(View.GONE);
            Glide.with(context).load(nPosts.getSite()).into(holder.imageView);
        }else {
            holder.play_icon.setVisibility(View.VISIBLE);
            Glide.with(context).load(nPosts.getThumb()).into(holder.imageView);
        }

        holder.bind(postList.get(position), listener);
    }


    @Override
    public int getItemCount() {

        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView,play_icon;

        public MyViewHolder(@NonNull View itemView) {
            super ( itemView );
            play_icon=itemView.findViewById (R.id.play_icon);
            imageView=itemView.findViewById (R.id.row_img);
        }
        @SuppressLint("ClickableViewAccessibility")
        public void bind(final Post post, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(post);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
/*
private void DeleteMediaPost(Post post,Context context){
    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
    dialogBuilder.setIcon(R.drawable.ic_delete);
    dialogBuilder.setTitle("Delete " + post.getType());
    dialogBuilder.setMessage("Are you sure?");
    dialogBuilder.setNegativeButton("Cancel", (dialogInterface, i) -> {
        dialogInterface.dismiss();
    });
    dialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
        FirebaseDatabase.getInstance().getReference("PersonalPosts").child(firebaseUser.getUid())
                .child(post.getPostid()).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //deleteNotifications(post.getPostid(), firebaseUser.getUid());
                            Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    });
    dialogBuilder.show();
}*/
 /* private void deleteNotifications(final String postid, String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("postid").getValue().equals(postid)) {
                        snapshot.getRef().removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/
}
