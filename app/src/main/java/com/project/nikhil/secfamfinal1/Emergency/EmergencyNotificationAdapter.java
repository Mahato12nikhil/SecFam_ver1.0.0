package com.project.nikhil.secfamfinal1.Emergency;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.Model.User;
import com.project.nikhil.secfamfinal1.R;
import com.project.nikhil.secfamfinal1.utils.MyUtil;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class EmergencyNotificationAdapter extends RecyclerView.Adapter<EmergencyNotificationAdapter.Holder> {
    private ArrayList<Emergency_data> noteList;
    private DatabaseReference mUsersDatabase;
    private Context context;
    private Boolean ischat;
    String name;
    public EmergencyNotificationAdapter(Context context,ArrayList<Emergency_data> noteList) {
        this.context=context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public EmergencyNotificationAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.emergency_notification_data,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EmergencyNotificationAdapter.Holder holder, final int position) {
       Emergency_data emrNotify=noteList.get(position);

        //Toast.makeText(context,""+noteList.get(0).getTime(),Toast.LENGTH_LONG).show();

        if(emrNotify!=null) {
            getDetails(holder.user_name, holder.dpDal, holder.time, emrNotify.getSender(),emrNotify.time);
        } else {}

        holder.XXXHUB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,MapVideoActivity.class);
                intent.putExtra("pushId",emrNotify.pushId );
                intent.putExtra("victim_id",emrNotify.sender );
                context.startActivity(intent);
            }
        });

    }

    private void getDetails(final   TextView user_name, final CircleImageView dpDal, TextView time, String sender,long timestamp) {
      //  Toast.makeText(context,""+sender,Toast.LENGTH_LONG).show();
        mUsersDatabase=FirebaseDatabase.getInstance().getReference().child("Users").child(sender);
        mUsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    user_name.setText(dataSnapshot.child("name").getValue().toString());
                    time.setText(MyUtil.timestampToDateTime(timestamp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String url = dataSnapshot.child("image").getValue().toString();
                    Glide.with(context).load(url)
                            .placeholder(context.getResources().getDrawable(R.drawable.man_placeholder)).into(dpDal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void background1(RelativeLayout itemview, String s) {
    }

    private void username(String s) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(s);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                   name= user.getName();
                // publisher.setText(user.getName());
               /* Intent public_reg=new Intent(context,Message2.class);
                public_reg.putExtra("user_id",user.getId());
                public_reg.putExtra("user_name",name);

                context.startActivity(public_reg);*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public class Holder extends RecyclerView.ViewHolder{
        CircleImageView dpDal;
        TextView user_name;
        ConstraintLayout XXXHUB;
        TextView time;
        public Holder(@NonNull View itemView) {
            super(itemView);
            dpDal=itemView.findViewById(R.id.dpDal);
            user_name=itemView.findViewById(R.id.victimDal);
            time=itemView.findViewById(R.id.timeDal);
            XXXHUB=itemView.findViewById(R.id.XXXHUB);
        }
    }

    private void userinfo(final CircleImageView user_single_image, final TextView user_single_name, String s) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(s);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                try {
                    Glide.with(context).load(user.getImage()).into(user_single_image);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                user_single_name.setText(user.getName());
                // publisher.setText(user.getName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void background(final RelativeLayout itemview, String s) {
      //  Toast.makeText(context,"entered",Toast.LENGTH_LONG).show();
        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Emergency")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("received").child(s);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Boolean vl=dataSnapshot.child("backcheck").getValue(Boolean.class);
                   // Toast.makeText(context,""+vl,Toast.LENGTH_LONG).show();
                    if(vl)
                    {
                        itemview.setBackground(ContextCompat.getDrawable(context, R.drawable.transperantback));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    private void checkfeedback(String s, final ImageView feedbackmsg) {

    }

   /* public void addAll(final List<Emergency_data> newUsers) {
        noteList.addAll(newUsers);
        notifyDataSetChanged();
        //  notifyItemRangeChanged(initialSize, newUsers.size());
    }*/

    public String getLastPostId() {
        return noteList.get(noteList.size()-1).getPushId();
    }
}
