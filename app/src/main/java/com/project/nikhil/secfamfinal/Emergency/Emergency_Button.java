package com.project.nikhil.secfamfinal.Emergency;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.project.nikhil.secfamfinal.LiveVideo.LiveVideoBroadcast;
import com.project.nikhil.secfamfinal.Location.Location_get;
import com.project.nikhil.secfamfinal.Location.Mylocationshare;
import com.project.nikhil.secfamfinal.Notification.APIService;
import com.project.nikhil.secfamfinal.Notification.*;
import com.project.nikhil.secfamfinal.Notification.Client;
import com.project.nikhil.secfamfinal.Notification.Data;
import com.project.nikhil.secfamfinal.Notification.MyResponse;
import com.project.nikhil.secfamfinal.Notification.Sender;
import com.project.nikhil.secfamfinal.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;


public class Emergency_Button extends Fragment implements AdapterView.OnItemSelectedListener {
    String pushId;
    //Later added
    CircleImageView button;
    ProgressBar progressBar;
    int progressStatus=20;
    int touch=0;
    String name;
    private  static CountDownTimer countDownTimer;
    PopupWindow popUp;
    LinearLayout layout;
    CardView crdd;
    private AlertDialog dialog;
    ListView listView;
    private TextToSpeech myTTS;
    private StorageTask uploadTask;
    private  String miUrlOk = "";
    private Uri videoUri;
    ArrayAdapter<String> MessageAdapter;
    private DatabaseReference reference,chatref1;
    private FirebaseDatabase database,chatdata;
    FirebaseAuth fileauth;
    private TextView text_view_id;
    ProgressBar progressbr23;
    private ListView msgList;
    private TextView myLat,myLong;
    ArrayList<Double> array;
    FirebaseAuth distanceAuth;
    private RadioGroup radioGroup;
    private RadioButton radioButton,radioBoth;
    private SpeechRecognizer myspeakrecognizer;
    String key,myUid;
    String id,data,data1;
    private Double  myLatitude,myLongitude;
    Double  Latitude,Longitude,userlat,userlang;
    DatabaseReference Policeref;
    DatabaseReference mRootRef;
    private Map<String,Double> map;
    private Map<String,Double> Users;
    private Button findnearuser,emergency_users,map_to_show_all;
    Location_get location_get;
    APIService apiService;
    private Spinner msgSpinner;

    ProgressDialog progressDialog;
    FloatingActionButton fab;
    boolean notify = false;
    private StorageReference storageRef;
    private TypedArray photosmm;
    private String[] messagesmm;
    //   private  List<MessageEmergencyModel> rowItems;

    public static Fragment newInstance() {
        return new Emergency_Button();
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_emergency__button, container, false);

        //Later added

        progressBar=view.findViewById(R.id.prog1);
        button=view.findViewById(R.id.button);

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circularexample);
        progressBar.setMax(100);
        progressBar.setProgress(0);   // Main Progress
        //progressBar.setSecondaryProgress(100); // Secondary Progress
        // progressBar.setMax(100); // Maximum Progress
        progressBar.setProgressDrawable(drawable);
        myUid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        Users=new HashMap<>();

        // long then = 0;
        button.setOnTouchListener(new View.OnTouchListener() {

            long then=0;

            Thread thread;
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                touch++;
                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    then=System.currentTimeMillis();
                    //  TimeLeftInMillis = millisUntilFinished;
                    //   updateCountDownText(); //  Updating CountDown_Tv

                    //
                    countDownTimer = new CountDownTimer(5000, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                            // textViewTime.setText(hmsTimeFormatter(millisUntilFinished));
                            long progress = (int) (millisUntilFinished/60);

                            progressBar.setProgress((int) (progressBar.getMax()-progress));

                        }

                        @Override
                        public void onFinish() {
                            final Map<String,Double> map=new HashMap<>();
                            DatabaseReference userlocation=FirebaseDatabase.getInstance().getReference().child("Users");

                            userlocation.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot sn : dataSnapshot.getChildren())
                                    {
                                        userlat=sn.child("Latitude").getValue(Double.class);
                                        userlang=sn.child("Longitude").getValue(Double.class);
                                        String keys= sn.getKey();
                                        double distances;
                                        if(userlang==null && userlat==null) {
                                            distances= 150;
                                        }
                                        else {
                                            distances=0;
                                        }
                                        try {
                                            if(userlang!=null && userlat!=null) {
                                                distances = KmeterDistanceBetweenPoints(userlat, userlang);
                                            }
                                            // Toast.makeText(getContext(),""+distances,Toast.LENGTH_LONG).show();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        try {
                                            if(distances<=2 && keys.contentEquals(distanceAuth.getUid())==false)

                                            {
                                                Users.put(keys,distances);

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            final Handler handler=new Handler();
                            final int delay=1000;
                            handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        if(!Users.isEmpty())
                                                        {

                                                            sentEmergency(Users);
                                                        }

                                                        else {

                                                            handler.postDelayed(this,delay);
                                                        }


                                                    }
                                                },delay
                            );
                            // Toast.makeText(getContext(),"hiiii",Toast.LENGTH_LONG).show();



                        }

                    }.start();

                }

                else if(event.getAction() == MotionEvent.ACTION_UP){
                    countDownTimer.cancel();
                    progressBar.setProgress(0);
                    if(((Long) System.currentTimeMillis() - then) > 5000){

                        //    Toast.makeText(getContext(),"Hello",Toast.LENGTH_LONG).show();


                        return true;
                    }
                    else
                    {
                        progressBar.setProgress(0);

                        return  true;
                    }
                }
                else
                {

                }
                return false;
            }
        });

        final View v=getActivity().findViewById(android.R.id.content);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);


        distanceAuth=FirebaseAuth.getInstance();
        myUid=FirebaseAuth.getInstance().getCurrentUser().getUid();


        array=new ArrayList<>();

        map = new HashMap<>();
        Users=new HashMap<>();

        location_get=new Location_get(myLatitude,myLongitude);

        //to fetch all the users of firebase Auth app
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Policeref = rootRef.child("Police");


        final DatabaseReference Polref= rootRef.child("Police") ;
        Polref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference myrefe = rootRef.child("Users").child(distanceAuth.getUid());
        chatdata = FirebaseDatabase.getInstance();
        myrefe.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myLatitude = dataSnapshot.child("Latitude").getValue(Double.class);
                myLongitude = dataSnapshot.child("Longitude").getValue(Double.class);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference userRef=FirebaseDatabase.getInstance().getReference().child("Users");



        return view;
    }

    private Map findUsers() {


        return map;
    }

    private void startLiveVideo(String pushId) {
        Intent inttn=new Intent(getContext(), LiveVideoBroadcast.class);
        inttn.putExtra("pushId",pushId);
        startActivity(inttn);
    }


    /*   @Override
       public void onPause() {
           super.onPause();
           myTTS.shutdown();
       }
   */
    @Override
    public void onStart() {
        super.onStart();





    }

    private void sendNotifiaction(final String key, final String name, final String s) {

//        Toast.makeText(getActivity(), "call", Toast.LENGTH_SHORT).show();

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("tokenid");
        Query query = tokens.orderByKey().equalTo(key);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    final Token token = snapshot.getValue(Token.class);
                    Data data = new Data(myUid, R.mipmap.ic_launcher, s, ""+name,
                            key,"emergency");

                    final Sender sender = new Sender(data, token.getTokenid());
                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){

                                            //  Toast.makeText(getActivity(), "sender="+token.getTokenid(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private double KmeterDistanceBetweenPoints( double lat_b, double lng_b) {

        try {
            double theta = myLongitude - lng_b;
            double dist = Math.sin(Math.toRadians(myLatitude)) * Math.sin(Math.toRadians(lat_b)) + Math.cos(Math.toRadians(myLatitude)) * Math.cos(Math.toRadians(lat_b)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515;
            double distinkm = dist * 1.609344;

            return (distinkm);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==400 && resultCode==RESULT_OK)
        {
            videoUri=data.getData();
            DatabaseReference gy=FirebaseDatabase.getInstance().getReference().child("EmergencyVideo").child("share").child(myUid).child("video");

            // Toast.makeText(getContext(),""+videoUri,Toast.LENGTH_LONG).show();
          /*  Intent inten=new Intent(getActivity(),TestActivityVideo.class);
            inten.putExtra("videouri",videoUri);
            // startActivity(inten);
            uploadvideo(videoUri);*/
        }
    }
    private void uploadvideo(final Uri videoURI) {


        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Posting");

        pd.show();
        pd.setCanceledOnTouchOutside(false);
        if (videoURI != null){


            storageRef= FirebaseStorage.getInstance().getReference().child("EmergencyVideos").child(myUid);
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(videoURI));

            uploadTask = fileReference.putFile(videoURI);
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
                        miUrlOk = downloadUri.toString();


                        //   final String postid = reference.push().getKey();

                        DatabaseReference gy=FirebaseDatabase.getInstance().getReference().child("EmergencyVideo").child("share").child(myUid).child("video");

                        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(miUrlOk, MediaStore.Video.Thumbnails.MINI_KIND);




                        gy.setValue(miUrlOk);
                        pd.dismiss();

                    } else {

                        // Toast.makeText(VideoPost.this, "Failed", Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Toast.makeText(VideoPost.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            //  Toast.makeText(VideoPost.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri videoURI) {

        ContentResolver cR = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(videoURI));

    }

    @Override
    public void onResume() {
        super.onResume();

//        radioBoth.setChecked(true);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // dialog.dismiss();
    }

    @Override
    public void onPause() {
        super.onPause();
//radioBoth.setChecked(false);
        // dialog.dismiss();
    }

    public void ShowDialog2(View view)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(getContext());
        builder.setCancelable(true);
        // builder.setPositiveButton("ok",null);
        builder.setView(listView);
        dialog=builder.create();
        dialog.show();
        newInstance();

    }
    public void sentEmergency(Map<String, Double> User)
    {
        mRootRef = FirebaseDatabase.getInstance().getReference();

        final String message="Help me";
        progressBar.setProgress(progressBar.getMax());

        //();
       getActivity().startService(new Intent(getActivity(), Mylocationshare.class));
        DatabaseReference gta = FirebaseDatabase.getInstance().getReference().child("locationshare")
                .child(myUid);

        gta.child("isShare").setValue("true");
        final String sentPushId=mRootRef.child("EmergencyUsersChat").child(myUid).push().getKey();

        final String receivedPush=mRootRef.child("EmergencyUsersChat").child(myUid).child("received").push().getKey();

        //String path=sentPushId;

        mRootRef = FirebaseDatabase.getInstance().getReference();


        DatabaseReference helper=mRootRef.child("Helper").child("emergencySent").child(myUid);


        mRootRef = FirebaseDatabase.getInstance().getReference();
        // String emergencyLivePush=mRootRef.child("EmergencyLive").push().getKey();
        Emergency_data emergency_data=new Emergency_data(sentPushId,"sent",userlat,userlang,0,null,null);
        mRootRef.child("EmergencyUsersChat").child(myUid).child("sent").child(sentPushId).setValue(emergency_data);
        mRootRef.child("EmergencyUsersChat").child(myUid).child("sent").child(sentPushId).child("time").setValue(ServerValue.TIMESTAMP);


        for (final Map.Entry<String, Double> entry : User.entrySet()) {






            mRootRef.child("EmergencyUsersChat").child(myUid).child("sent").child(sentPushId).child("ids").child(entry.getKey()).setValue(true);


            //   mRootRef.child("EmergencyUsersChat").child(entry.getKey()).child(sentPushId).child("ids").child(myUid).child("seen").setValue(false);

            Emergency_data emergency_data1=new Emergency_data(sentPushId,"receive",userlat,userlang,0,myUid,null);

            mRootRef.child("EmergencyUsersChat").child(entry.getKey()).child("receive").child(sentPushId).setValue(emergency_data1);
            mRootRef.child("EmergencyUsersChat").child(entry.getKey()).child("receive").child(sentPushId).child("time").setValue(ServerValue.TIMESTAMP);

        }
        //   mRootRef.child("EmergencyLive").child(emergencyLivePush).child("broadcastId").setValue("");



        progressBar.setProgress(0);
        startLiveVideo(sentPushId);

        //startActivity(new Intent(getContext(),LiveVideoBroadcast.class));
    }
}


