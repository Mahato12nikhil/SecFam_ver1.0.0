package com.project.nikhil.secfamfinal.Profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal.Post.Post;
import com.project.nikhil.secfamfinal.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProfileMedia extends Fragment {

    private List<Post> links;
    RecyclerView recyclerView;
    ProfileMediaAdapter adapter;
     private String iD;

    private FirebaseUser mCurrentUser;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_media, container, false);

        iD=((ProfilePostActivity) getActivity()).getId();
        mCurrentUser= FirebaseAuth.getInstance().getCurrentUser();
        recyclerView=view.findViewById ( R.id.rcv );
        GridLayoutManager gridLayoutManager = new GridLayoutManager (getContext (),3 );
        recyclerView.setLayoutManager ( gridLayoutManager );
        mCurrentUser=FirebaseAuth.getInstance().getCurrentUser();


        recyclerView.setHasFixedSize ( true );

        links=new ArrayList<>();

        adapter = new ProfileMediaAdapter (links,getContext());
        recyclerView.setAdapter ( adapter );
        myPhotos();

        return view;

    }

    private void getNrPosts(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("PersonalPosts").child(iD);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    //if (post.getPublisher().equals(profileid)){
                    i++;
                    // }
                }
                //posts.setText(""+i);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void myPhotos(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PersonalPosts")
                .child(iD);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(links.size()>0)
                    links.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Post post = null;
                    try {
                        post = snapshot.getValue(Post.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                  if(post!=null && post.getType()!=null) {
                      if (post.getType().equals("image") || post.getType().equals("video"))
                          links.add(post);
                  }

                }
                Collections.reverse(links);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
