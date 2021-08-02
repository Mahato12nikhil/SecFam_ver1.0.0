package com.project.nikhil.secfamfinal1.Post;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.Model.User;
import com.project.nikhil.secfamfinal1.Profile.ProfileActivity;
import com.project.nikhil.secfamfinal1.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class FollowingAdapter  extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    Context context;
    List<User> followingList;
    FirebaseUser firebaseUser;
    public FollowingAdapter(Context context, List<User> followingList) {
        this.context=context;
        this.followingList=followingList;
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public FollowingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from ( parent.getContext () )
                .inflate ( R.layout.follow_followers_item ,parent,false);

        return new FollowingAdapter.ViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.ViewHolder holder, int position) {

        User user=followingList.get(position);

        if (user.getId().equals(firebaseUser.getUid())){
            holder.btn_follow_xx.setVisibility(View.GONE);
        }

        holder.follow_username_xx.setText(user.getName());
        if (user.getImage()!=null){
            Glide.with(context).load(user.getImage()).placeholder(R.drawable.man_placeholder).into(holder.follow_profile);
        }else {
            Glide.with(context).load(R.drawable.man_placeholder).into(holder.follow_profile);
        }


        isFollowing(user.getId(),holder.btn_follow_xx);

        holder.btn_follow_xx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (holder.btn_follow_xx.getText().toString().equals("follow")) {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                                .child("following").child(user.getId()).setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                                .child("followers").child(firebaseUser.getUid()).setValue(true);
                        addNotification(user.getId());
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                                .child("following").child(user.getId()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                                .child("followers").child(firebaseUser.getUid()).removeValue();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Follow")
                                .child(firebaseUser.getUid()).child("notifyId");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child(user.getId()).exists()) {
                                    String id = Objects.requireNonNull(snapshot.child(user.getId()).getValue()).toString();
                                    deleteNotification(id);
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("publisherid", user.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private void deleteNotification(String id) {
       FirebaseDatabase.getInstance()
                .getReference("Notifications").child(id).removeValue();
    }

    private void addNotification(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(id);

        String push=reference.push().getKey();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "started following you");
        hashMap.put("postid", "");
        hashMap.put("type","follow");
        hashMap.put("ispost", false);

       // reference.push().setValue(hashMap);

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                .child("notifyId").child(id);
        reference.child(push).setValue(hashMap);
        ref.setValue(push);
    }

    private void isFollowing(final String userid, final Button button){
        assert firebaseUser != null;
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.child(userid).exists()){
                        button.setText("following");
                    } else{
                        button.setText("follow");
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


    @Override
    public int getItemCount() {
        return followingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView follow_profile;
        TextView follow_username_xx;
        Button btn_follow_xx;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            follow_profile=itemView.findViewById(R.id.follow_profile);
            follow_username_xx=itemView.findViewById(R.id.follow_username_xx);
            btn_follow_xx=itemView.findViewById(R.id.btn_follow_xx);

        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
