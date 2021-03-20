package com.project.nikhil.secfamfinal.Post;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.project.nikhil.secfamfinal.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MultipleImageAdapter extends RecyclerView.Adapter<MultipleImageAdapter.ViewHolder> {

    ArrayList<String> mainModels;
    Context context;

    public MultipleImageAdapter(Context context,ArrayList<String> mainModels){
        this.context=context;
        this.mainModels= mainModels;
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

                context.startActivity(intent);

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

