package com.project.nikhil.secfamfinal.Upload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.project.nikhil.secfamfinal.Post.PostActivity;
import com.project.nikhil.secfamfinal.Post.PostAdapter;
import com.project.nikhil.secfamfinal.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class Upload_Video extends AppCompatActivity {
    ImageView playerView;
    SimpleExoPlayer exoPlayer;
    private String miUrlOk = "";
    private String thumbUrl = "";
    private String urlXy;
    Bitmap BittMap;
    int s=1;

    ProgressDialog progressDoalog;
    private TextView post_button;
    Bitmap bitmap;
    EditText editText;
    private StorageTask uploadTask;
    private  String userThumb,userName;
    private  String myUid;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_upload__video );


        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor( ContextCompat.getColor(this,R.color.bg_light)); //status bar or the time bar at the top
        }

        playerView = findViewById ( R.id.upload_video_view );
        editText=findViewById(R.id.editText);
        post_button=findViewById(R.id.post_button);
        myUid=FirebaseAuth.getInstance().getUid();
        String upload_video = getIntent ().getStringExtra ( "Upload_Video_view" );


        Glide.with(this).asBitmap().load(upload_video).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                BittMap=resource;
            }
            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        });


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(BittMap!=null)
                {
                    playerView.setImageBitmap(BittMap);
                }
                else {
                    handler.postDelayed(this,100);
                }
                //Do something after 100ms
            }
        }, 100);




        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDoalog = new ProgressDialog(Upload_Video.this);
                progressDoalog.setMessage("Loading....");
                progressDoalog.show();
                uploadVideo(upload_video,BittMap);

            }
        });

    }




    private void uploadVideo(String upload_video, Bitmap bittMap) {
        Map<String,String> linkMap=new HashMap<>();
        final StorageReference path = FirebaseStorage.getInstance().getReference().child("PostMedia").child(myUid).child(System.currentTimeMillis()
                + "." +"mp4");
        uploadTask = path.putFile(Uri.parse(upload_video));
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                //Toast.makeText(getApplicationContext(),""+path.getDownloadUrl(),Toast.LENGTH_SHORT).show();
                return path.getDownloadUrl();

            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    miUrlOk = downloadUri.toString();

                    //links.add(miUrlOk);
                    linkMap.put("Video_"+(s),miUrlOk);
                    generateThumbnail(bittMap,miUrlOk);
                    s++;

                    //   pd.dismiss();


                } else {
                    Toast.makeText(Upload_Video.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Upload_Video.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





    private void generateThumbnail(Bitmap bittMap, String miUrlOk) {
        Map<String,String> linkMap=new HashMap<>();
        final StorageReference path = FirebaseStorage.getInstance().getReference().child("PostThumbnail").child(myUid).child(System.currentTimeMillis()
                + "." + "thumb");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bittMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        uploadTask = path.putBytes(data);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                //Toast.makeText(getApplicationContext(),""+path.getDownloadUrl(),Toast.LENGTH_SHORT).show();
                return path.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    thumbUrl = downloadUri.toString();

                    //links.add(miUrlOk);
                    linkMap.put("image_"+(s), Upload_Video.this.thumbUrl);
                    s++;
                    uploadData(miUrlOk,thumbUrl);


                    //   pd.dismiss();


                } else {
                    Toast.makeText(Upload_Video.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Upload_Video.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        startActivity(new Intent(Upload_Video.this,PostActivity.class));
        progressDoalog.dismiss();
    }







    private void uploadData(String miUrlOk, String thumbUrl) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts")
                ;
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("PersonalPosts").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                ;

        final String postid = reference.push().getKey();
        final Handler handler=new Handler();
        final int delay=1000;
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("postid", postid);
        hashMap.put("site", miUrlOk);
        hashMap.put("description", editText.getText().toString());
        hashMap.put("time", ServerValue.TIMESTAMP);
        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("type", "video");
        hashMap.put("thumb",thumbUrl);


        reference.child(postid).setValue(hashMap);
        reference2.child(postid).setValue(hashMap);



    }






/*    private void uploadData(@NotNull final Uri uri) {




        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("PersonalPosts")
                                             .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        final String postid = reference.push().getKey();



        //int data=getSaltString();

            HashMap<String, Object> hashMap = new HashMap<>();

            hashMap.put("postid", postid);
            hashMap.put("site", "");
            hashMap.put("description", "hello");
            hashMap.put("time", ServerValue.TIMESTAMP);
            hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
            hashMap.put("type", "video");
            hashMap.put("thumb",userThumb);


            reference.child(postid).setValue(hashMap);
            reference2.child(postid).setValue(hashMap);


        final Uri file =uri;
        final StorageReference path = FirebaseStorage.getInstance().getReference().child("PostMedia").child(myUid).child(System.currentTimeMillis()
                + "." +"mp4");


          *//*  path.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(ImageActivity.this,"Success", Toast.LENGTH_LONG).show();

                }
            });*//*

        uploadTask = path.putFile(file);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return path.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    miUrlOk = downloadUri.toString();

                    final String link_id = reference.child(postid).child("site").push().getKey();

                    reference.child(postid).child("thumb").setValue(miUrlOk);


                    Bitmap bitmap = null;
                    bitmap = ThumbnailUtils.createVideoThumbnail(String.valueOf(uri), MediaStore.Video.Thumbnails.MINI_KIND);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] dash = baos.toByteArray();
                    final StorageReference thumbPath = FirebaseStorage.getInstance().getReference().child("PostThumb").child(getFileExtension(file));

                    UploadTask uploadTask1 = thumbPath.putBytes(dash);

                    Toast.makeText(getApplicationContext(),""+dash,Toast.LENGTH_LONG).show();



                    uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return thumbPath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                urlXy = downloadUri.toString();

                                final String link_id = reference.child(postid).child("site").push().getKey();

                                reference.child(postid).child("site").setValue(urlXy);
                                reference2.child(postid).child("site").setValue(urlXy);


                            } else {
                                Toast.makeText(Upload_Video.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Upload_Video.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            long negtime= (long) dataSnapshot.child(postid).child("time").getValue();
                            Toast.makeText(getApplicationContext()," Negetive"+negtime,Toast.LENGTH_LONG).show();
                            long revtime=-1*negtime;
                            reference.child(postid).child("invtime").setValue(revtime);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    startActivity(new Intent(Upload_Video.this, PostActivity.class));
                    finish();

                } else {
                    Toast.makeText(Upload_Video.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Upload_Video.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }*/



   /* private String getFileExtension(Uri uri) {
        String result=null;

        if(uri.getScheme().equals("content")){
            Cursor cursor= null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                cursor = getContentResolver().query(null,null,null,null);
            }
            try{
                if(cursor!=null && cursor.moveToFirst())
                {
                    result=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }

            }
            finally {
                cursor.close();
            }
        }
        if(result==null) {
            result = uri.getPath();
            int cut = result.lastIndexOf("/");
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }*/

   /* private int getSaltString() {
        String SALTCHARS = "1234567892";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 9) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        ;
        return   Integer.parseInt(saltStr);

    }
*/


}