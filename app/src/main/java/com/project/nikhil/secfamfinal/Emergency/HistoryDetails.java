package com.project.nikhil.secfamfinal.Emergency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryDetails extends AppCompatActivity {

    private DatabaseReference reference;
    private String mAuth;
    private String pushId;
    private List<String> idsList;
    private RecyclerView allSendRecyclerview;
    private HistoryDetailsAdapter historyDetailsAdapter;
    private RelativeLayout back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);

        allSendRecyclerview=findViewById(R.id.allSend);
        back=findViewById(R.id.history_details_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        idsList=new ArrayList<>();
        allSendRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        historyDetailsAdapter = new HistoryDetailsAdapter(this, idsList);
        //adapter.setClickListener(this);
        allSendRecyclerview.setAdapter(historyDetailsAdapter);
        pushId=getIntent().getStringExtra("pushId");

        mAuth= FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference= FirebaseDatabase.getInstance().getReference().child("EmergencyUsersChat").child(mAuth).child("sent").
                child(pushId).child("ids");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.hasChildren())
                {
                    for(DataSnapshot sn :snapshot.getChildren())
                    {
                       idsList.add(sn.getKey().toString());
                    }
                }

                historyDetailsAdapter.notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(),"Details::->"+idsList.size(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}