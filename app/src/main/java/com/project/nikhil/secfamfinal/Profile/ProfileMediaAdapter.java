package com.project.nikhil.secfamfinal.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.project.nikhil.secfamfinal.Post.Post;
import com.project.nikhil.secfamfinal.R;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileMediaAdapter  extends RecyclerView.Adapter<ProfileMediaAdapter.MyViewHolder> {


    int []arr;

    List<Post> postList;
    Context context;
    Map<String,String> links;

    public ProfileMediaAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context=context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {

        View view = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.profile_media_image_item, parent,false);
        MyViewHolder myViewHolder = new MyViewHolder ( view );
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder , int position) {

        Post nPosts=postList.get(position);

        Glide.with(context).load(nPosts.getSite()).into(holder.imageView);

    }


    @Override
    public int getItemCount() {

        return postList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super ( itemView );
            imageView=itemView.findViewById ( R.id.row_img );
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
