package com.project.nikhil.secfamfinal1.Emergency;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.Notification.Token;
import com.project.nikhil.secfamfinal1.R;

import java.util.ArrayList;


public class Emergency_History extends Fragment {

    private DatabaseReference mConvDatabase;
    private RecyclerView policeconv_list;

    private DatabaseReference mMessageDatabase;
    private DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;
    Emergency_data emergency_data;
    private String userid;
    private Emergency_History_Adapter  police_chat_adapter;
    private String mCurrent_user_id;
    private ArrayList<Emergency_data> Userid;
    private ArrayList<String> lastMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_emergency__history, container, false);
        policeconv_list=view.findViewById(R.id.policeconv_list);
        mCurrent_user_id=FirebaseAuth.getInstance().getCurrentUser().getUid();
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);

        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Emergency").child(mCurrent_user_id);
        mConvDatabase.keepSynced(true);
        Userid=new ArrayList<>();
        police_chat_adapter=new Emergency_History_Adapter(getContext(),Userid);
        policeconv_list .setAdapter(police_chat_adapter);
        lastMessage=new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        policeconv_list.setHasFixedSize(true);
        policeconv_list.setLayoutManager(linearLayoutManager);
        //  updateToken(FirebaseInstanceId.getInstance().getToken());

        //  Toast.makeText(getContext(),"This  ua",Toast.LENGTH_LONG).show();

        mConvDatabase.child("sent").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Userid.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    try {
                        emergency_data=ds.getValue(Emergency_data.class);
                        Userid.add(emergency_data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //Toast.makeText(getContext(),""+Userid.size(),Toast.LENGTH_LONG).show();
                police_chat_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return  view;
    }

    private void updateToken(String token) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("tokenid");
        Token token1 = new Token(token);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token1);}


    @Override
    public void onStart() {
        super.onStart();

    }
}
