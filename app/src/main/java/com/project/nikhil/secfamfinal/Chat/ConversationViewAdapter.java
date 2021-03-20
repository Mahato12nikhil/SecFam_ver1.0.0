package com.project.nikhil.secfamfinal.Chat;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationViewAdapter extends RecyclerView.Adapter <RecyclerView.ViewHolder>{
    Context mcontext;
    String mCurrentUserId;
    String mChatUser;
    ValueEventListener seenListener;

    private List<ConversationActivity_Data_Model> mMessageList;
    // Boolean dateChanged=false;
    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT=1;
    // private static final int msg_type_division=2;
/*    private static final int IMAGE_TYPE_RIGHT=2;
    private static final int IMAGE_TYPE_LEFT=3;

    private static final int IMAGE_ADD=6;


    private static final int VIDEO_TYPE_RIGHT=4;
    private static final int VIDEO_TYPE_LEFT=5;

    private static final int DOC_TYPE_RIGHT=7;
    private static final int DOC_TYPE_LEFT=8;

    private static final int AUDIO_TYPE_RIGHT=9;
    private static final int AUDIO_TYPE_LEFT=10;*/


    private DatabaseReference mUserDatabase;
    public ConversationViewAdapter( Context mcontext, List<ConversationActivity_Data_Model> mMessageList, String mChatUser,String mCurrentUserId) {
        this.mcontext = mcontext;
        this.mMessageList = mMessageList;
        this.mChatUser=mChatUser;
        this.mCurrentUserId=mCurrentUserId;
        //System.out.println("============="+mcontext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {

            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_text_right, parent, false);
            return new MessageViewHolder(view);
        }
        else if(viewType==MSG_TYPE_LEFT){
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_text_left, parent, false);
            return new MessageViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {

        final ConversationActivity_Data_Model c = (ConversationActivity_Data_Model) mMessageList.get(i);
        final ConversationActivity_Data_Model d = (ConversationActivity_Data_Model) mMessageList.get(mMessageList.size()-1);
        boolean isSeen = c.isIsSeen();
        //System.out.println("1111111111111111111111"+isSeen);

   /*   TextViewHolder holder = (TextViewHolder)viewHolder;
      holder.textView.setVisibility(View.VISIBLE);
      holder.textView.setText(c.getMessage());*/



        if(viewHolder instanceof MessageViewHolder)
        {
            //Toast.makeText(context,"called",Toast.LENGTH_LONG).show();
            String from_user = c.getFrom();
            String to_user = c.getFrom();
            String message_type = c.getType();
            final String pushId=c.getId();

            //  final String pushId2=d.getId();

       if (pushId!=null)
            msgSentStatus(pushId,isSeen);
            ((MessageViewHolder) viewHolder).chat_text.setText(c.getMessage());


            long ms = c.getTime();
            Date date = new Date(ms);
            SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            ((MessageViewHolder) viewHolder).time_text_layout.setText("" + dateformat.format(date));
            ((MessageViewHolder) viewHolder).single_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    deleteSingleChat(pushId,c);

                    return true;
                }
            });

            //   if (i==mMessageList.size()-1){

            if(pushId!=null){
                seenSynchronise(pushId,((MessageViewHolder) viewHolder).seen);
            }

            // }
          /* else {
           ((MessageViewHolder) viewHolder).seen.setText("Sending");
         }*/
        }
    }

    private void seenSynchronise(String pushId, final ImageView seen) {

        final DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("messages").child(mChatUser).child(mCurrentUserId).child(pushId).child("isSeen");
    /*final DatabaseReference ref1=FirebaseDatabase.getInstance().getReference()
      .child("messages").child(mCurrentUserId).child(mChatUser).child(pushId2).child("message");
    */
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                Boolean isSeenF=snapshot.getValue(Boolean.class);
                //System.out.println("mmmmmmmmmmm"+isSeenF);
                // Toast.makeText(mcontext,""+isSeenF,Toast.LENGTH_LONG).show();

                if (isSeenF!=null && isSeenF.equals(true)){
                    seen.setImageResource(R.drawable.seen_tick);


                }
                else if(isSeenF!=null && isSeenF.equals(false)) {
                    seen.setImageResource(R.drawable.sent_tick);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    //  ((VideoViewHolder) viewHolder).videoView.start();

    // ((VideoViewHolder) viewHolder).videoView.start();



    private void msgSentStatus(String pushId, boolean isSeen) {

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference()
                .child("messages").child(mCurrentUserId).child(mChatUser).child(pushId).child("isSeen");
        ref.setValue(true);

    }

    private boolean deleteSingleChat(String pushId, final ConversationActivity_Data_Model c) {
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference()
                .child("messages").child(mCurrentUserId).child(mChatUser).child(pushId);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mcontext);
        alertDialog.setTitle("Do you want to delete task?");
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ref.removeValue();
                        mMessageList.remove(c);
                        notifyDataSetChanged();
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

        return true;
    }

     /*private void showAllItems(ImageView imageView, String cPushid, String from, String myid){

        final ArrayList<Chat_multiple> datas=new ArrayList<>();
        final ArrayList<String>before=new ArrayList<>();

      //

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("messages").child(myid).child(from);
        reference.child(cPushid).child("All_messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                List<Chat_multiple> all_messages = new ArrayList<>();
                Chat_multiple  chat_multiple=dataSnapshot.getValue(Chat_multiple.class);



                 *//*   String type = dataSnapshot1.child("type").getValue().toString();
                    String message=dataSnapshot1.child("message").getValue().toString();
                    String pushid=dataSnapshot1.child("pushid").getValue().toString();
                    String time =dataSnapshot1.child("time").getValue().toString();

                    before.add(time);
                    before.add(type);
                    before.add(message);
                    before.add(pushid);

                    datas.add(before);*//*
                   for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                       Chat_multiple user = postSnapshot.getValue(Chat_multiple.class);
                       if(!all_messages.contains(user)){
                           all_messages.add(user);
                       }
                   }
                for(int i=0;i<all_messages.size();i++){

                   // Log.i("Pritish", "onDataChange: "+contactList.get(i));
                }
               // datas.add(chat_multiple);
                Bundle bundle=new Bundle();
               // bundle.putParcelableArrayList(all_messages);
                Intent intent=new Intent(context,MultipleChatShow.class);
                intent.putExtra("links", (Serializable) all_messages);
                context.startActivity(intent);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/

    /*public class ImageViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
          //  imageView=itemView.findViewById(R.id.chat_item_image11);

        }
    }*/
   /* public  class ImagePreviewHolder extends RecyclerView.ViewHolder
    {        private ProgressBar photoUploadProgress;
             ImageView chat_item_image11;

        public ImagePreviewHolder(@NonNull View itemView) {
            super(itemView);
         // photoUploadProgress=itemView.findViewById(R.id.photoUploadProgress);
         //   chat_item_image11=itemView.findViewById(R.id.chat_item_image11);



        }
    }
    public class DocViewHolder extends RecyclerView.ViewHolder
    {
         TextView textView;
        public DocViewHolder(@NonNull View itemView) {
            super(itemView);
         //   textView=itemView.findViewById(R.id.docs_desc);
        }
    }*/


    public class MessageViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout single_layout_right,single_layout;
        public TextView time_text_layout,chat_text;
        public CircleImageView profileImage;
        public TextView displayName;
        public ImageView messageImage,seen;

        public MessageViewHolder(View view) {
            super(view);

            //messageText = (TextView) view.findViewById(R.id.message_text_layout);
//            displayName = (TextView) view.findViewById(R.id.name_text_layout);
            // messageImage = (ImageView) view.findViewById(R.id.message_image_layout);
            seen=view.findViewById(R.id.seen);
            time_text_layout = view.findViewById(R.id.Time);
            chat_text=(TextView) itemView.findViewById(R.id.chat_text);
            single_layout=view.findViewById(R.id.layout_RT);
            //  System.out.println("--------"+single_layout);
        }



    }
   /* class VideoViewHolder extends RecyclerView.ViewHolder
    {
      // ScalableVideoView videoView;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
          //  videoView= itemView.findViewById(R.id.message_video);
        }
    }

    class AudioViewHolder extends RecyclerView.ViewHolder
    {
        TextView songName;
        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);
          //  songName=itemView.findViewById(R.id.songName);
        }
    }
*/

//  public class TextViewHolder extends RecyclerView.ViewHolder {
//    public TextView textView;
//    public LinearLayout timeText;
//    public TextViewHolder(View v) {
//      super(v);
//      timeText = (LinearLayout) v.findViewById(R.id.timeTextLayout);
//      textView = (TextView) v.findViewById(R.id.textView);
//    }
//  }



    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


    @Override
    public int getItemViewType(int position) {

        System.out.println("/////////"+mMessageList.size());


        if(mMessageList.size()>0 && mMessageList.get(position).getType()!=null) {
            if (mMessageList.get(position).getFrom().equals(mCurrentUserId) && mMessageList.get(position).getType().equals("text")) {
                return MSG_TYPE_RIGHT;
            } else if (!mMessageList.get(position).getFrom().equals(mCurrentUserId) && mMessageList.get(position).getType().equals("text")) {
                return MSG_TYPE_LEFT;
            }
        }
        /*else if(mMessageList.get(position).getFrom().equals(FirebaseAuth.getInstance().getUid())&& mMessageList.get(position).getType().equals("image") )
        {

            return IMAGE_TYPE_RIGHT;
        }
        else if(!mMessageList.get(position).getFrom().equals(FirebaseAuth.getInstance().getUid())&& mMessageList.get(position).getType().equals("image") )
        {

            return IMAGE_TYPE_LEFT;
        }
        else if(!mMessageList.get(position).getFrom().equals(FirebaseAuth.getInstance().getUid())&& mMessageList.get(position).getType().equals("docs") )
        {
            return DOC_TYPE_LEFT;
        }
        else if(mMessageList.get(position).getFrom().equals(FirebaseAuth.getInstance().getUid())&& mMessageList.get(position).getType().equals("docs") )
        {
            return DOC_TYPE_RIGHT;
        }
       else if(mMessageList.get(position).getFrom().equals(FirebaseAuth.getInstance().getUid()) && mMessageList.get(position).getType().equals("video")){
            return  VIDEO_TYPE_RIGHT;

        }
        else if(!mMessageList.get(position).getFrom().equals(FirebaseAuth.getInstance().getUid())&& mMessageList.get(position).getType().equals("video") )
        {return VIDEO_TYPE_LEFT;}

        else if(mMessageList.get(position).getFrom().equals(FirebaseAuth.getInstance().getUid()) && mMessageList.get(position).getType().equals("audio")){
            return  AUDIO_TYPE_RIGHT;

        }
        else if(!mMessageList.get(position).getFrom().equals(FirebaseAuth.getInstance().getUid())&& mMessageList.get(position).getType().equals("audio") )
        {return AUDIO_TYPE_LEFT;}*/

      /*  else if (dateChanged.equals(true)){
          return msg_type_division;
        }
*/
            return 0;
        }


    public void setItemcount(){
        mMessageList.clear();
        notifyDataSetChanged();
    }
}

