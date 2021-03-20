package com.project.nikhil.secfamfinal.Emergency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class HistoryDetailsAdapter extends RecyclerView.Adapter<HistoryDetailsAdapter.ViewHolder> {

    List<String> idsList;
    Context mContext;
    public HistoryDetailsAdapter(Context mContext, List<String> idsList){
        this.mContext=mContext;
        this.idsList=idsList;
    }

    @NonNull
    @Override
    public HistoryDetailsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_details_sent_lists,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryDetailsAdapter.ViewHolder holder, int position) {

        String id=idsList.get(position);
        Toast.makeText(mContext,"HEY::"+id,Toast.LENGTH_LONG).show();

        showUsers(holder.history_sendName,holder.history_sendImage,id);

    }

    @Override
    public int getItemCount() {
        return idsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView history_sendImage;
        TextView history_sendName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            history_sendImage=itemView.findViewById(R.id.history_sendImage);
            history_sendName=itemView.findViewById(R.id.history_sendName);
        }
    }

    private void showUsers(TextView nameView, CircleImageView imageView, String id)
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users").child(id);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChildren())
                {
                    String name= null;
                    try {
                        name = snapshot.child("name").getValue().toString();
                        nameView.setText(name);
                    } catch (Exception e) {
                        nameView.setText("Unknown");
                        e.printStackTrace();
                    }
                    String image= null;
                    try {
                        image = snapshot.child("thumb").getValue().toString();
                        Glide.with(mContext).load(image).into(imageView);
                    } catch (Exception e) {
                        Glide.with(mContext).load(R.drawable.man_placeholder)
                                .into(imageView);
                        e.printStackTrace();
                    }





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
