package com.project.nikhil.secfamfinal.Upload;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.project.nikhil.secfamfinal.Post.PostActivity;
import com.project.nikhil.secfamfinal.R;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class UploadBottom_Sheet extends BottomSheetDialogFragment {

    LinearLayout bottom,upload_photo,upload_video,upload_text;
    int SELECT_PHOTO =1, SELECT_VIDEO=2;
    Uri uri;
    private ArrayList<Uri> photoPathList;


    public static UploadBottom_Sheet newInstance() {

        return new UploadBottom_Sheet ();}
    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate ( R.layout.fragment_upload_bottom__sheet , container , false );


        upload_photo=view.findViewById ( R.id.bottomsheet_photo );
        upload_video=view.findViewById ( R.id.bottomsheet_video );
        upload_text=view.findViewById ( R.id.bottomsheet_text );


        upload_photo.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                photoPathList = new ArrayList <Uri>();

                Intent intent = new Intent (Intent.ACTION_PICK);
                intent.setType ( "image/*" );
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                 //startActivityForResult ( intent,SELECT_PHOTO );
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), 1);



            }
        } );

        upload_video.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (Intent.ACTION_PICK);
                intent.setType ( "video/*" );
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult ( intent,SELECT_VIDEO );

            }
        } );

        upload_text.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent (getContext (),Upload_Text.class);
                startActivity ( intent );
            }
        } );
        return view;
    }



    @Override
    public void onActivityResult(int requestCode , int resultCode , Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("data11111111111111"+data);


        if(requestCode==1 && resultCode==RESULT_OK && data!=null) {
            if (data.getClipData () != null) {
                ClipData mClipData = data.getClipData ();

                for (int i = 0; i < mClipData.getItemCount (); i++) {
                    ClipData.Item item = mClipData.getItemAt ( i );
                    Uri uri = item.getUri ();
                    // display your images

                    photoPathList.add ( i,item.getUri () );
                    System.out.println("photoPathList11111111111111"+photoPathList);
                }


                Intent intent = new Intent (getContext (),Upload_Photo.class);
                intent.putExtra ( "ImagesList",photoPathList );

                startActivity ( intent );
            } else if (data.getData () != null) {
                Uri uri = data.getData ();

                Intent intent = new Intent (getContext (),Upload_Photo.class);
                intent.putExtra ( "ImageSingle",uri.toString());
                startActivity ( intent );

            }

        }
        else if (requestCode==SELECT_VIDEO && resultCode==RESULT_OK && data!=null){
            Uri uri = data.getData ();
           // uri.getPath();

            Intent intent = new Intent (getContext (),Upload_Video.class);
            assert uri != null;
            intent.putExtra ( "Upload_Video_view",uri.toString());
            startActivity ( intent );
        }

    }



}
