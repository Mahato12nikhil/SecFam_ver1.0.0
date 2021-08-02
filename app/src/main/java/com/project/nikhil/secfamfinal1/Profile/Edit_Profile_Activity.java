package com.project.nikhil.secfamfinal1.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.R;

import java.util.HashMap;
import java.util.Map;

public class Edit_Profile_Activity extends BaseActivity {

RadioGroup sex_edit;
String name,bio,details,sex,location;
RadioButton radioButton;
TextView personal_details;
EditText nameProfile_edit,bio_edit,details_edit,location_edit;
RelativeLayout back,edit_done;
FirebaseUser mCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile_);
        Log.i("!!!Activity", "Edit_Profile_Activity");

        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.bg)); //status bar or the time bar at the top

        }

        personal_details=findViewById(R.id.personal_details);
        back=findViewById(R.id.back);
        nameProfile_edit=findViewById(R.id.nameProfile_edit);
        bio_edit=findViewById(R.id.bio_edit);
        details_edit=findViewById(R.id.details_edit);
        location_edit=findViewById(R.id.location_edit);
        sex_edit=findViewById(R.id.sex_edit);
        edit_done=findViewById(R.id.edit_done);



        mCurrentUser=FirebaseAuth.getInstance().getCurrentUser();



        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users")
                .child(mCurrentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    name=snapshot.child("name").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    bio=snapshot.child("bio").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    details=snapshot.child("details").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    location=snapshot.child("location").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    sex=snapshot.child("sex").getValue().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(name!=null){
                    nameProfile_edit.setText(name);
                }
                if(bio!=null)
                {
                    bio_edit.setText(bio);
                }
                if(details!=null)
                {
                    details_edit.setText(details);
                }
                if(location!=null)
                {
                    location_edit.setText(location);
                }
                if(sex!=null && !sex.equals(""))
                {
                    if(sex.equals("Male"))
                    {
                        ((RadioButton)sex_edit.getChildAt(0)).setChecked(true);
                    }
                    else {
                        ((RadioButton)sex_edit.getChildAt(1)).setChecked(true);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        edit_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValid()) {
                    editUpload();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        personal_details.setPaintFlags(personal_details.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private boolean isValid() {
        if (nameProfile_edit.getText().toString().trim().equals("")){
            nameProfile_edit.setError("Please enter your Name");
            nameProfile_edit.requestFocus();
            return false;
        }
        return true;
    }

    private void editUpload() {
        showProgressDialog();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(mCurrentUser.getUid());
        Map<String,Object> taskMap = new HashMap<>();
        taskMap.put("bio", bio_edit.getText().toString());
        taskMap.put("name", nameProfile_edit.getText().toString());
        taskMap.put("details", details_edit.getText().toString());
        taskMap.put("location", location_edit.getText().toString());
        int selectedId = sex_edit.getCheckedRadioButtonId();
        // find the radiobutton by returned id
        radioButton = (RadioButton) findViewById(selectedId);
        if (radioButton!=null && !radioButton.getText().toString().isEmpty()) {
            taskMap.put("sex",radioButton.getText().toString());
        }
        reference.updateChildren(taskMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgressDialog();
            }
        });





        /*if (snap != null) {
            snap.child("bio").getRef().setValue(bio_edit.getText().toString());
            snap.child("name").getRef().setValue(nameProfile_edit.getText().toString());
            snap.child("details").getRef().setValue(details_edit.getText().toString());
            snap.child("location").getRef().setValue(location_edit.getText().toString());

            int selectedId = sex_edit.getCheckedRadioButtonId();
            // find the radiobutton by returned id
            radioButton = (RadioButton) findViewById(selectedId);
            if (!radioButton.getText().toString().isEmpty()) {
                snap.child("sex").getRef().setValue(radioButton.getText().toString());
            }

            reference.setValue(snap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    FirebaseDatabase.getInstance().getReference().child("Search").child("Users").child(mCurrentUser.getUid())
                            .child("name").getRef().setValue(nameProfile_edit.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            editUpload();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Edit_Profile_Activity.this, "Failed to update", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
            finish();
        }*/


    }
}