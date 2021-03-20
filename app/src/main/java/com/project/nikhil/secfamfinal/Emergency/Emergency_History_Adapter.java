package com.project.nikhil.secfamfinal.Emergency;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class Emergency_History_Adapter extends RecyclerView.Adapter<Emergency_History_Adapter.ChatHolder>  {
    ArrayList<Emergency_data> Userid;
    DatabaseReference mUsersDatabase;
    ArrayList<String> lastMessage;
    Context context;

    public Emergency_History_Adapter(Context context, ArrayList<Emergency_data> Userid) {

        this.context=context;
        this.Userid = Userid;


    }

    @NonNull
    @Override
    public Emergency_History_Adapter.ChatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.emergency_history_adapter,viewGroup,false);
        return new ChatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Emergency_History_Adapter.ChatHolder chatHolder, final int position) {




        final Emergency_data emergency_data=Userid.get(position);


        Toast.makeText(context,""+Userid.get(position),Toast.LENGTH_LONG).show();
        chatHolder.statusEmer.setTextColor(context.getResources().getColor(R.color.blue));
        chatHolder.statusEmer.setText("sent");




        SimpleDateFormat format=new SimpleDateFormat("EEE, d MMM yyyy 'at' HH:mm:ss "  );
        String dateString = format.format(new Date(emergency_data.getTime()));
        chatHolder.timeSup.setText(dateString);
        //  chatHolder.card_history_notification.setCardElevation(0 );

        chatHolder.card_history_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Boolean> lists;
                ArrayList<String> list;
                lists=emergency_data.getIds();
               // list=new ArrayList<>(lists.values());
                Toast.makeText(context,"Entered",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(context,HistoryDetails.class);
                intent.putExtra("pushId",emergency_data.getPushId());
                context.startActivity(intent);
            }
        });

    }


    private void findTime(final String findId, final TextView timeSup, final TextView sup) {
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("EmergencyUsersChat").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(findId);

        mUsersDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String  time=dataSnapshot.child("time").getValue().toString();
                String status=dataSnapshot.child("status").getValue().toString();


                if(status.equals("sent"))
                {
                    sup.setText("sent");
                }
                else
                    sup.setText("received");
                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                String dateString = formatter.format(new Date(Long.parseLong(time)));
                timeSup.setText(dateString);
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

    public class ChatHolder extends RecyclerView.ViewHolder{

        RelativeLayout historyView;
        TextView timeSup,statusEmer;
        CardView card_history_notification;
        Calendar cal;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);
            //  historyView=itemView.findViewById(R.id.historyView);
            timeSup=itemView.findViewById(R.id.timeSup);
            card_history_notification=itemView.findViewById(R.id.historyView);
            statusEmer=itemView.findViewById(R.id.statusEmer);


            cal = Calendar.getInstance(Locale.ENGLISH);


        }
    }

}
