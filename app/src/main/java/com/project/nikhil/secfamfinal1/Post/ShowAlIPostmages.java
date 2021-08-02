package com.project.nikhil.secfamfinal1.Post;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.R;

import java.util.ArrayList;

public class ShowAlIPostmages extends BaseActivity {

    RecyclerView recyclerView;
    RelativeLayout back;
   private ArrayList<String> list;
   private DatabaseReference databaseReference;
    MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
             //getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.trans)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            //getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.trans)); //status bar or the time bar at the top

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_show_al_i_postmages);
        Log.i("!!!Activity", "ShowAlIPostmages");

        toolbar = findViewById(R.id.topAppBar);
        back=findViewById(R.id.finshL);
        recyclerView=findViewById(R.id.postShow);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager ( linearLayoutManager );
        recyclerView.setItemAnimator ( new DefaultItemAnimator() );

        SnapHelper spacePagerSnapHelper = new PagerSnapHelper();
        spacePagerSnapHelper.attachToRecyclerView(recyclerView);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
      /*  back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/

        String postId=getIntent().getStringExtra("postid");

          list = (ArrayList<String>) getIntent().getSerializableExtra("links");
           if(list!=null){
               final AllPostAdapter mainAdapter = new AllPostAdapter (ShowAlIPostmages.this,list, toolbar);
               recyclerView.setAdapter(mainAdapter);
               //System.out.println("ooooooooooooooooooooooooooo");
               mainAdapter.notifyDataSetChanged();
           }
           else {

               list=new ArrayList<>();

               databaseReference= FirebaseDatabase.getInstance().getReference().child("Posts").child(postId).child("links");
               databaseReference.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {

                      // Toast.makeText(getApplicationContext(),"Testing:::"+snapshot.getChildrenCount(),Toast.LENGTH_LONG).show();

                       for(DataSnapshot sp: snapshot.getChildren()){
                           list.add(sp.getValue().toString());

                       }

                       final AllPostAdapter mainAdapter = new AllPostAdapter (getApplicationContext(),list, toolbar );
                       recyclerView.setAdapter (  mainAdapter);
                       mainAdapter.notifyDataSetChanged();
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
           }
    }
}