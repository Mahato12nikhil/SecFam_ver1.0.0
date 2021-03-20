package com.project.nikhil.secfamfinal.Profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.project.nikhil.secfamfinal.Chat.ConversationActivity;
import com.project.nikhil.secfamfinal.Model.User;
import com.project.nikhil.secfamfinal.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity   {

    TextView  textView, photo_count, follow_count,following,profileName,profile_bio,profile_details;
    ImageView plus, settings,  image, checkFollow,pf_image,message,profile_gender;
    DatabaseReference mUsersDatabase;
    Uri uri;
    UploadTask uploadTask,uploadTask2;
    String profileid;
    ByteArrayOutputStream bytearrayoutputstream;
    byte[] BYTE;
    FirebaseUser firebaseUser;
    int current_img;
    StorageReference storageRef,thumbRef;
    LinearLayout profile_follower,profile_following;
    RelativeLayout edit,back,camera;
    LinearLayout gallery;
    //int[] images = {R.drawable.follow_prof, R.drawable.followed_prof};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.bg)); //status bar or the time bar at the top

        }


       // plus=findViewById ( R.id.plus );
        gallery = findViewById ( R.id.gallery );
       settings = findViewById ( R.id.settings );
        photo_count = findViewById ( R.id.photo_count );
        camera= findViewById ( R.id.camera );
        image = findViewById ( R.id.pf_image );
        follow_count = findViewById ( R.id.follow_count );
        following =findViewById ( R.id.following );
        checkFollow=findViewById(R.id.checkFollow);
        message=findViewById(R.id.mssg);
         edit = findViewById ( R.id.edit );
         back=findViewById(R.id.back_1);
         profile_bio=findViewById(R.id.profile_bio);
         profile_details=findViewById(R.id.profile_details);
         profile_gender=findViewById(R.id.profile_gender);
         profile_follower=findViewById(R.id.profile_follower);
         profile_following=findViewById(R.id.profile_following);

        profileName=findViewById(R.id.profileName);
        String fllwer = prettyCount ( 5165161 );
        String fllwing = prettyCount ( 55161 );
        String photo = prettyCount ( 1500 );
        photo_count.setText(photo);
        follow_count.setText (fllwer  );
        following.setText ( fllwing );
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("Profile_Images");
        thumbRef=FirebaseStorage.getInstance().getReference("Profile_thumb");

        profileid=getIntent().getStringExtra("publisherid");
        if(profileid==null)
        {profileid=firebaseUser.getUid();
        }

        if(firebaseUser.getUid().equals(profileid))
        {


        }
        else {
            settings.setVisibility(View.VISIBLE);
            camera.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
            checkFollow.setVisibility(View.VISIBLE);
            message.setVisibility(View.VISIBLE);
            isFollowing(profileid,checkFollow);

        }
        edit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent ( ProfileActivity.this , Edit_Profile_Activity.class );
                startActivity ( intent );
            }
        } );

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        userInfo();

        getFollowers();
        getPostsCount();

        profile_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,FollowingActivity.class));
            }
        });
        profile_follower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,FollowerActivity.class));

            }
        });
        checkFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (checkFollow.getTag().equals("follow")) {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                                .child("following").child(profileid).setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                                .child("followers").child(firebaseUser.getUid()).setValue(true);

                        addNotification(profileid);
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                                .child("following").child(profileid).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                                .child("followers").child(firebaseUser.getUid()).removeValue();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        gallery.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ProfileActivity.this , ProfilePostActivity.class );
               if(profileid!=null) {
                   intent.putExtra("id", profileid);
                   startActivity(intent);
               }
            }
        } );


        camera.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity ( ProfileActivity.this );
            }
        } );

        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(profileid);

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsersDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        final String name=snapshot.child("name").getValue(String.class);
                        String image=snapshot.child("image").getValue(String.class);
                        Intent intent=new Intent(ProfileActivity.this, ConversationActivity.class);
                        intent.putExtra("id",profileid);
                        intent.putExtra("user_name",name);
                        intent.putExtra("dp",image);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation =AnimationUtils.loadAnimation(getApplicationContext(),R.anim.profile_setting_rotate);
                settings.startAnimation(animation);
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(ProfileActivity.this, settings);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.profile_popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId ()) {
                            case R.id.option_1:
                                Toast.makeText ( ProfileActivity.this,"Item1 selected",Toast.LENGTH_SHORT ).show ();
                                return true;

                            case R.id.option_2:
                                Toast.makeText (ProfileActivity.this,"Item2 selected", Toast.LENGTH_SHORT ).show ();
                                return true;
                            default:return false;
                        }
                    }

                });


                popup.show();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode , int resultCode , @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            Uri imageuri = CropImage.getPickImageResultUri(this, data);
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                uri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                startCrop(imageuri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                image.setImageURI(result.getUri());
                uploadImage(result.getUri());
            }
        }

    }

    private void startCrop(Uri imageuri) {
        CropImage.activity (imageuri)
                .setGuidelines ( CropImageView.Guidelines.ON )
                .setMultiTouchEnabled ( true )
                .start ( this );
    }

    private void getPostsCount() {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("PersonalPosts").child(profileid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                photo_count.setText(""+snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }







    public String prettyCount(Number number) {
        char[] suffix = {' ', 'k', 'M', 'B', 'T', 'P', 'E'};
        long numValue = number.longValue();
        int value = (int) Math.floor(Math.log10(numValue));
        int base = value / 3;
        if (value >= 3 && base < suffix.length) {
            return new DecimalFormat("#0.00").format(numValue / Math.pow(10, base * 3)) + suffix[base];
        } else {
            return new DecimalFormat().format(numValue);
        }

    }

    private void getFollowers(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Follow").child(profileid).child("followers");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                follow_count.setText(""+(dataSnapshot.getChildrenCount()-1));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Follow").child(profileid).child("following");
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                following.setText(""+(dataSnapshot.getChildrenCount()-1));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void checkFollow(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(profileid).exists()){
                   // edit_profile.setText("following");
                    checkFollow.setImageDrawable( getResources().getDrawable(R.drawable.followed));
                } else{
                    checkFollow.setImageDrawable( getResources().getDrawable(R.drawable.follow));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void userInfo(){


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (getApplicationContext() == null){

                    return;
                }

                else {


                    User user = null;
                    try {
                        user = dataSnapshot.getValue(User.class);
                        if(user.getId().equals(firebaseUser.getUid()))
                        {
                            checkFollow.setVisibility(View.GONE);
                            message.setVisibility(View.GONE);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        if(user.getBio()!=null){
                            profile_bio.setText(user.getBio());
                        }
                        if(user.getDetails()!=null){
                            profile_details.setText(user.getDetails());
                        }
                        if(user.getSex()!=null)
                        {
                            if(user.getSex().equals("Male"))
                            {
                             profile_gender.setImageDrawable(getDrawable(R.drawable.male));
                            }
                            else {
                                profile_gender.setImageDrawable(getDrawable(R.drawable.female));

                            }
                        }
                        Glide.with(getApplicationContext()).load(user.getImage()).into(image);
                        profileName.setText(user.getName());
                    } catch (Exception e) {
                        Glide.with(getApplicationContext()).load(R.drawable.man_placeholder).into(image);
                        e.printStackTrace();
                    }

                }

                // bio.setText(user.getBio());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void uploadImage(Uri uri)  {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        //getting thumbnail of that dp
        try {
            BYTE=null;
            bytearrayoutputstream = new ByteArrayOutputStream();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,bytearrayoutputstream);
            BYTE = bytearrayoutputstream.toByteArray();

            final StorageReference fileReference = thumbRef.child(firebaseUser.getUid()).child(""+System.currentTimeMillis() + "." +"jpg");
                   ;
            uploadTask2=fileReference.putBytes(BYTE);

             uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String, Object> map1 = new HashMap<>();
                        map1.put("thumb", ""+downloadUri);
                        reference.updateChildren(map1);
                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Search").child("Users").child(firebaseUser.getUid());

                        reference2.updateChildren(map1);

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });



        } catch (IOException e) {
            e.printStackTrace();
        }

        if (uri != null){
            final StorageReference fileReference = storageRef.child(firebaseUser.getUid()).child("Secfam_"+System.currentTimeMillis()
                    + "." + "jpg");

            uploadTask = fileReference.putFile(uri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String miUrlOk = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        HashMap<String, Object> map1 = new HashMap<>();
                        map1.put("image", ""+miUrlOk);
                        reference.updateChildren(map1);
                        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference().child("Search").child("Users").child(firebaseUser.getUid());

                        reference2.updateChildren(map1);

                        pd.dismiss();

                    } else {
                        Toast.makeText(ProfileActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(ProfileActivity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void isFollowing(final String userid, final ImageView button){

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot.child(userid).exists()){
                        button.setImageDrawable(getApplicationContext().getDrawable(R.drawable.followed));
                        button.setTag("followed");
                    } else{
                        button.setImageDrawable(getApplicationContext().getDrawable(R.drawable.follow));
                        button.setTag("follow");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void addNotification(String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userid);

        String pushID = reference.push().getKey();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("userid", firebaseUser.getUid());
        hashMap.put("text", "started following you");
        hashMap.put("postid", "");
        hashMap.put("notificationID", pushID);
        hashMap.put("type","follow");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);
    }


}
