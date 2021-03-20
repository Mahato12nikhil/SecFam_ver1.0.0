package com.project.nikhil.secfamfinal.Chat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatHomeViewAdapter extends RecyclerView.Adapter<ChatHomeViewAdapter.ChatHolder> {
    ArrayList<String> Userid;
    DatabaseReference mUsersDatabase;
    DatabaseReference reference;
    ArrayList<String> lastMessage;
    Context context;
    String mCurrentUserId;
    public ChatHomeViewAdapter(Context context, ArrayList<String> Userid, ArrayList<String> lastMessage,String mCurrentUserId) {
        this.context=context;
        this.Userid = Userid;
        this.lastMessage = lastMessage;
        this.mCurrentUserId=mCurrentUserId;
    }
    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.user_chatlist_view,parent,false);
        return  new ChatHomeViewAdapter.ChatHolder(view)   ;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        getnameadphoto(holder.user_single_name,holder.user_single_image,Userid.get(position));
        holder.chattt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name(holder.user_single_name,Userid.get(position));

            }
        });
        holder.chattt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                deleteConversation(holder.chattt,Userid.get(position));
                return true;
            }
        });
    }
////////////////////////////////****////////////////////////////
    private void deleteConversation(RelativeLayout chattt, final String id) {
        System.out.println("//////////////"+Userid.size());
        mUsersDatabase =FirebaseDatabase.getInstance().getReference()
                .child("messages").child(mCurrentUserId).child(id);
        reference=FirebaseDatabase.getInstance().getReference()
                .child("Chat").child(mCurrentUserId).child(id);
                        mUsersDatabase.removeValue();
                        Userid.remove(id);
                        reference.removeValue();
                       notifyDataSetChanged();

                    }
////////////////////////////////////////////////////////////
    private void name(TextView name, final String id) {

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(id);

        mUsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String name=dataSnapshot.child("name").getValue(String.class);
                String image=dataSnapshot.child("image").getValue(String.class);
                Boolean isOnline=dataSnapshot.child("isOnline").getValue(Boolean.class);

                Intent public_reg=new Intent(context,ConversationActivity.class);
                public_reg.putExtra("id",id);
                public_reg.putExtra("user_name",name);
                public_reg.putExtra("dp",image);
                public_reg.putExtra("isOnline",isOnline);
               public_reg.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(public_reg);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getnameadphoto(final TextView user_single_name, final CircleImageView user_single_image, String s) {

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(s);

        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final String name=dataSnapshot.child("name").getValue(String.class);
                String image=dataSnapshot.child("image").getValue(String.class);
                String status=dataSnapshot.child("status").getValue(String.class);
                user_single_name.setText(name);

                Glide.with(context).load(image).into(user_single_image);

                try {


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
        return Userid.size();
    }

    public class ChatHolder extends RecyclerView.ViewHolder {
        CircleImageView user_single_image;
        TextView user_single_name;
        ImageView user_single_online_icon;
        TextView user_single_status;
        RelativeLayout chattt;
        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            user_single_image=itemView.findViewById(R.id.user_single_image);
            user_single_name=itemView.findViewById(R.id.user_single_name);
            user_single_online_icon=itemView.findViewById(R.id.status);
            user_single_status=itemView.findViewById(R.id.user_single_status);
            chattt=itemView.findViewById(R.id.chattt);


            if (user_single_name.getText().toString().length()>20){
                user_single_name.setText(user_single_name.getText().toString().substring(0,20)+"...");
            }

            if (user_single_status.getText().toString().length()>29){
                user_single_status.setText(user_single_status.getText().toString().substring(0,29)+"...");
            }

        }
    }
}
