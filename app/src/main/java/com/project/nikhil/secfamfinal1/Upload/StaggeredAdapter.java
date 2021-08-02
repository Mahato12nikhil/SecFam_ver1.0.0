package com.project.nikhil.secfamfinal1.Upload;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.request.RequestOptions;
import com.project.nikhil.secfamfinal1.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StaggeredAdapter extends RecyclerView.Adapter<StaggeredAdapter.ImageViewHolder> {


    Context mContext;
    List<Uri> mdata;
    RequestOptions requestOptions ;

    public StaggeredAdapter(Context mContext, List<Uri> horizontalList) {
        this.mContext = mContext;
        this.mdata =horizontalList;
        requestOptions = new RequestOptions().fitCenter();
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.staggered_item,viewGroup,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {

        //Bitmap myBitmap = BitmapFactory.decodeFile(mdata.get(i).getPath());

        imageViewHolder.img.setImageURI (mdata.get ( i ));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView img;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.uploadImages);
        }
    }
}

