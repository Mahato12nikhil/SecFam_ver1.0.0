package com.project.nikhil.secfamfinal1.Post;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.github.piasy.biv.view.BigImageView;
import com.google.android.material.appbar.MaterialToolbar;
import com.project.nikhil.secfamfinal1.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllPostAdapter extends RecyclerView.Adapter<AllPostAdapter.ViewHolder> {

    ArrayList<String> mainModels;
    Context context;
    MaterialToolbar toolbar;
    boolean flag = true;
    public AllPostAdapter(Context context,ArrayList<String> mainModels, MaterialToolbar toolbar){
        this.context=context;
        this.mainModels= mainModels;
        BigImageViewer.initialize(GlideImageLoader.with(context));
        this.toolbar = toolbar;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        View view= LayoutInflater.from ( parent.getContext () )
                .inflate ( R.layout.allpost ,parent,false);

        return new ViewHolder ( view );
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder , int position) {
        //   Toast.makeText(context,""+mainModels.get(position),Toast.LENGTH_LONG).show();

        //Glide.with(context).load(mainModels.get(position)).into(holder.imageView);

        holder.imageView.showImage(Uri.parse(mainModels.get(position)));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag){
                    flag = false;
                    Animation slideUp = AnimationUtils.loadAnimation(context,R.anim.top_slide_up);
                    toolbar.startAnimation(slideUp);
                    toolbar.setVisibility(View.GONE);

                }else {
                    flag = true;
                    Animation slideDown = AnimationUtils.loadAnimation(context,R.anim.top_slide_down);
                    toolbar.startAnimation(slideDown);
                    toolbar.setVisibility(View.VISIBLE);

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mainModels.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        BigImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super ( itemView );

            imageView =itemView.findViewById ( R.id.image_view_all );

        }
    }


}

