package com.project.nikhil.secfamfinal.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal.R;

public class Edit_Profile_Activity extends AppCompatActivity {

RadioGroup sex_edit;
String name,bio,details,sex;
RadioButton radioButton;
TextView personal_details;
EditText nameProfile_edit,bio_edit,details_edit;
RelativeLayout back,edit_done;
FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile_);

        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.bg)); //status bar or the time bar at the top

        }

        personal_details=findViewById(R.id.personal_details);
        back=findViewById(R.id.back);
        nameProfile_edit=findViewById(R.id.nameProfile_edit);
        bio_edit=findViewById(R.id.bio_edit);
        details_edit=findViewById(R.id.details_edit);
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
                if(sex!=null)
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
                editUpload();
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

    private void editUpload() {
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("uploading");
        progressDialog.show();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Users")
                .child(mCurrentUser.getUid());

        if(!bio_edit.getText().toString().isEmpty())
            reference.child("bio").setValue(bio_edit.getText().toString());
        else
            reference.child("bio").setValue("");

        if (!nameProfile_edit.getText().toString().isEmpty()) {

            reference.child("name").setValue(nameProfile_edit.getText().toString());
         FirebaseDatabase.getInstance().getReference().child("Search").child("Users").child(mCurrentUser.getUid())
                 .child("name").setValue(nameProfile_edit.getText().toString());
        } else {
            reference.child("name").setValue("");
            FirebaseDatabase.getInstance().getReference().child("Search").child("Users").child(mCurrentUser.getUid())
                    .child("name").setValue("");
        }

        if(!details_edit.getText().toString().isEmpty())
            reference.child("details").setValue(details_edit.getText().toString());
        else
            reference.child("details").setValue("");

        int selectedId = sex_edit.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioButton = (RadioButton) findViewById(selectedId);
       ;
        if( !radioButton.getText().toString().isEmpty())
        {
            reference.child("sex").setValue(radioButton.getText().toString());
        }

        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(),"Updated successfully",Toast.LENGTH_SHORT).show();
        finish();



    }
}