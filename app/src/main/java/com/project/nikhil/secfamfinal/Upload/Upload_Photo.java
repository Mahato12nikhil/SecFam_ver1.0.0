package com.project.nikhil.secfamfinal.Upload;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.project.nikhil.secfamfinal.Post.DotsIndicatorDecoration;
import com.project.nikhil.secfamfinal.Post.PostActivity;
import com.project.nikhil.secfamfinal.Post.PostAdapter;
import com.project.nikhil.secfamfinal.R;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Upload_Photo extends AppCompatActivity {
    EditText editText;
    private RecyclerView staggeredRv;
    private String name,image;
    int s=1;
    Uri uri;
    //PostAdapter postAdapter;
    String Imagesingle;
    private StaggeredAdapter adapter ;
    private LinearLayoutManager manager;
    private StorageTask uploadTask;
    ImageView single_image;
    private TextView post_button;
    private String miUrlOk = "";
    private  List<Uri> list;
    private String userName,userThumb;
    private  String myUid;
    ProgressDialog progressDoalog,progressDoalog2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_upload__photo );


        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.bg_light)); //status bar or the time bar at the top
        }

        myUid=FirebaseAuth.getInstance().getUid();
        post_button=findViewById(R.id.post_button);
        editText=findViewById(R.id.editText);
       list= new ArrayList<Uri> ();
        list = (ArrayList<Uri>)getIntent().getSerializableExtra("ImagesList");
        Imagesingle = getIntent().getStringExtra ("ImageSingle");
        //name=getIntent().getStringExtra("name");
        //image=getIntent().getStringExtra("ImagesList");




     //   Toast.makeText(getApplicationContext(),""+name,Toast.LENGTH_SHORT).show();
        
        post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list!=null)
                {
                    progressDoalog = new ProgressDialog(Upload_Photo.this);
                    progressDoalog.setMessage("Loading....");
                    progressDoalog.show();
                    uploadData(list);

                }else if(Imagesingle!=null){
                    uri= Uri.parse(Imagesingle);
                    progressDoalog2 = new ProgressDialog(Upload_Photo.this);
                   progressDoalog2.setMessage("Loading....");
                   progressDoalog2.show();
                    uploadSingleData(uri);

                }

            }
        });

        if(list!=null){


            staggeredRv = findViewById(R.id.staggered_rv);
            manager = new LinearLayoutManager(Upload_Photo.this,LinearLayoutManager.HORIZONTAL,false);
            staggeredRv.setLayoutManager(manager);

            adapter = new StaggeredAdapter (this,list);
            staggeredRv.setAdapter(adapter);

            final int color = ContextCompat.getColor( this,R.color.lightcolor);
            final int color_1 = ContextCompat.getColor( this,R.color.bg);


            staggeredRv.addItemDecoration(new DotsIndicatorDecoration(8, 10, 20, color, color_1));


            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(staggeredRv);

            Toast.makeText ( getApplicationContext () , "OK multiple : " + list.size () , Toast.LENGTH_LONG ).show ();

        }
        else if(Imagesingle!=null){
            single_image=findViewById ( R.id.single_img_view );



            single_image.setVisibility ( View.VISIBLE );
            single_image.setImageURI ( Uri.parse ( Imagesingle ) );

            Toast.makeText ( getApplicationContext () , "OK single : " + Imagesingle , Toast.LENGTH_LONG ).show ();

        }


        // ArrayList<Uri> list_1 = new ArrayList<Uri> ();

    }

    private void uploadSingleData(Uri uri) {
        Map<String,String> linkMap=new HashMap<>();
        final StorageReference path = FirebaseStorage.getInstance().getReference().child("PostMedia").child(myUid).child(System.currentTimeMillis()
                + "." + getFileExtension(uri));

        uploadTask = path.putFile(uri);
      //  System.out.println("cholcheyyyyyyyyyyyy"+uri.toString());
        //uploadTask = path.putFile(uri);
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
                    linkMap.put("image_"+(s),miUrlOk);
                    s++;

                    //   pd.dismiss();


                } else {
                    Toast.makeText(Upload_Photo.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Upload_Photo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts")
                ;
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("PersonalPosts").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                ;

        final String postid = reference.push().getKey();



       /* DatabaseReference reg=FirebaseDatabase.getInstance().getReference()
                .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());*/
        final Handler handler=new Handler();
        final int delay=1000;
        handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {


                                    if (linkMap.size() ==1 || linkMap.equals(miUrlOk)) {
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("postid", postid);
                                        hashMap.put("site",linkMap.get("image_1"));
                                        hashMap.put("description", editText.getText().toString());
                                        hashMap.put("time", ServerValue.TIMESTAMP);
                                        hashMap.put("publisher", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                                        hashMap.put("type", "image");
                                        hashMap.put("links", linkMap);


                                        reference.child(postid).setValue(hashMap);
                                        reference2.child(postid).setValue(hashMap);
                                        handler.removeCallbacks(this);
                                    }
                                    else {

                                        handler.postDelayed(this,delay);

                                    }
                                }
                            },delay
        );

        startActivity(new Intent(Upload_Photo.this,PostActivity.class));
        progressDoalog2.dismiss();
    }


    //function for uploading images in firebase storage

    private void uploadData(@NonNull @NotNull List<Uri> mAlbumFiles) {
        Map<String,String> linkMap=new HashMap<>();
        List<String> links=new ArrayList<>();

        //final String postid2=reference2.push().getKey();


       // int data=getSaltString();


        //assert postid != null;




        for(int i=0;i<mAlbumFiles.size();i++) {
            //Toast.makeText(getApplicationContext(),mAlbumFiles.size(),Toast.LENGTH_SHORT).show();
           // uri[i] = Uri.parse("file://"+mAlbumFiles.get(i).getPath());
            //Uri file = Uri.fromFile(new File(mAlbumFiles.get(i).getPath()));
            final StorageReference path = FirebaseStorage.getInstance().getReference().child("PostMedia").child(myUid).child(System.currentTimeMillis()
                    + "." + getFileExtension(mAlbumFiles.get(i)));

            uploadTask = path.putFile(mAlbumFiles.get(i));
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
                        linkMap.put("image_"+(s),miUrlOk);
                        s++;

                        //   pd.dismiss();


                    } else {
                        Toast.makeText(Upload_Photo.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Upload_Photo.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts")
                ;
        final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("PersonalPosts").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                ;

        final String postid = reference.push().getKey();



       /* DatabaseReference reg=FirebaseDatabase.getInstance().getReference()
                .child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());*/
        final Handler handler=new Handler();
        final int delay=1000;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                if (linkMap.size() == mAlbumFiles.size() || linkMap.equals(miUrlOk)) {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("postid", postid);
                    hashMap.put("site",linkMap.get("image_1"));
                    hashMap.put("description", editText.getText().toString());
                    hashMap.put("time", ServerValue.TIMESTAMP);
                    hashMap.put("publisher", Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                    hashMap.put("type", "image");
                    hashMap.put("links", linkMap);


                    reference.child(postid).setValue(hashMap);
                    reference2.child(postid).setValue(hashMap);
                    handler.removeCallbacks(this);
                }
                else {

                    handler.postDelayed(this,delay);

                }
            }
            },delay
     );

        startActivity(new Intent(Upload_Photo.this,PostActivity.class));
        progressDoalog.dismiss();

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}