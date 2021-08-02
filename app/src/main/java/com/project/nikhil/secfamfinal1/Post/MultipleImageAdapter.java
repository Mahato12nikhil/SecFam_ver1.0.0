package com.project.nikhil.secfamfinal1.Post;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.nikhil.secfamfinal1.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MultipleImageAdapter extends RecyclerView.Adapter<MultipleImageAdapter.ViewHolder> {

    ArrayList<String> mainModels;
    Context context;
    boolean isPostAvailable;
    public MultipleImageAdapter(Context context, ArrayList<String> mainModels, boolean postAvailable){
        this.context=context;
        this.mainModels= mainModels;
        this.isPostAvailable = postAvailable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent , int viewType) {
        View view= LayoutInflater.from ( parent.getContext () )
                .inflate ( R.layout.row_item ,parent,false);

        return new ViewHolder ( view );
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder , int position) {
        //   Toast.makeText(context,""+mainModels.get(position),Toast.LENGTH_LONG).show();

/*        Transformation blurTransformation = new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {
                Bitmap blurred = Blur.fastblur(context, source, 10);
                source.recycle();
                return blurred;
            }

            @Override
            public String key() {
                return "blur()";
            }
        };*/
        Glide.with(context).load(mainModels.get(position)).into(holder.imageView);


        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ShowAlIPostmages.class);
                intent.putExtra("links",mainModels);

                if (isPostAvailable) {
                    context.startActivity(intent);
                }else {
                    Toast.makeText(context, "This post is no longer available, deleted by the post owner.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return mainModels.size ();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super ( itemView );

            imageView =itemView.findViewById ( R.id.image_view );
        }
    }

    @Override
    public long getItemId(int position) {
        return mainModels.get(position).hashCode();
    }
}

