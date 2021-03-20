package com.project.nikhil.secfamfinal.Post;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.nikhil.secfamfinal.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AllPostAdapter extends RecyclerView.Adapter<AllPostAdapter.ViewHolder> {

    ArrayList<String> mainModels;
    Context context;

    public AllPostAdapter(Context context,ArrayList<String> mainModels){
        this.context=context;
        this.mainModels= mainModels;
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

        Glide.with(context).load(mainModels.get(position)).into(holder.imageView);



    }


    @Override
    public int getItemCount() {
        return mainModels.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super ( itemView );

            imageView =itemView.findViewById ( R.id.image_view_all );

        }
    }


}

