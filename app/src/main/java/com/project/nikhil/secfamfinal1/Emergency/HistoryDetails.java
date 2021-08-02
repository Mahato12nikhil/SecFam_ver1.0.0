package com.project.nikhil.secfamfinal1.Emergency;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryDetails extends BaseActivity {

    private DatabaseReference reference;
    private String mAuth;
    private String pushId;
    private List<String> idsList;
    private RecyclerView allSendRecyclerview;
    private HistoryDetailsAdapter historyDetailsAdapter;
    private RelativeLayout back;
    private TextView full_location;
    Double latitude,longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        Log.i("!!!Activity", "HistoryDetails");

        allSendRecyclerview=findViewById(R.id.allSend);
        back=findViewById(R.id.history_details_back);
        full_location=findViewById(R.id.full_location);

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
        latitude=getIntent().getDoubleExtra("latitude",0.0);
        longitude=getIntent().getDoubleExtra("longitude",0.0);
        getAddress(latitude,longitude);
        mAuth= FirebaseAuth.getInstance().getCurrentUser().getUid();

        reference= FirebaseDatabase.getInstance().getReference().child("Emergency").child(mAuth).child("sent").
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
    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            //add = add + "\n" + obj.getSubThoroughfare();
            add = add + "\n" + obj.getSubAdminArea();
            //add = add + "\n" + obj.getLocality();
            //add = add + "\n" + obj.getPostalCode();
            //add = add + "\n" + obj.getCountryName();
            //add = add + "\n" + obj.getCountryCode();
            //add = add + "\n" + obj.getAdminArea();
            full_location.setText(add);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}