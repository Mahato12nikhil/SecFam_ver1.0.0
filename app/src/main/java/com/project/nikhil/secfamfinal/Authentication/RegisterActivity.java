package com.project.nikhil.secfamfinal.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.RegexValidator;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.project.nikhil.secfamfinal.MainActivity;
import com.project.nikhil.secfamfinal.Post.PostActivity;
import com.project.nikhil.secfamfinal.R;

public class RegisterActivity extends AppCompatActivity {

    private static final int CREDENTIAL_PICKER_REQUEST =2 ;
    private Animation mAnimation;
   private RelativeLayout rlayout;
   private TextInputEditText signUpOtp,signUpName,signUpPhone;
   private Button SignUpNextButton;
    private FirebaseAuth mAuth;
    private String mVerificationId;
    String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.bg)); //status bar or the time bar at the top
        }

        rlayout=findViewById(R.id.rlayout);
        signUpName=findViewById(R.id.signUpName);
        signUpOtp=findViewById(R.id.SignUpOtp);
        signUpPhone=findViewById(R.id.SignUpPhone);
        SignUpNextButton=findViewById(R.id.SignUpNextButton);
        Toolbar toolbar = findViewById(R.id.bgHeader);


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

         mAnimation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
         rlayout.setAnimation(mAnimation);


         mAuth=FirebaseAuth.getInstance();
         String phone=signUpPhone.getText().toString();

         signUpPhone.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View view, MotionEvent motionEvent) {
                 if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                     phoneNumberPicker();
                 }
                 return false;
             }
         });
         SignUpNextButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 if(phoneNumber!=null && signUpName.getText().toString()!=null) {
                     //Toast.makeText(getApplicationContext(),""+phoneNumber,Toast.LENGTH_SHORT).show();

                     Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
                     intent.putExtra("phone",phoneNumber );
                     intent.putExtra("name", signUpName.getText().toString());
                     startActivity(intent);
                 }


             }
         });

    }
    private void phoneNumberPicker() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();


        PendingIntent intent = Credentials.getClient(RegisterActivity.this).getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, new Bundle());
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK) {
            // Obtain the phone number from the result
            Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);
            signUpPhone.setText(credentials.getId().substring(3));//get the selected phone number
//Do what ever you want to do with your selected phone number here
            phoneNumber=signUpPhone.getText().toString();


        } else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
            // *** No phone numbers available ***
            Toast.makeText(RegisterActivity.this, "No phone numbers found", Toast.LENGTH_LONG).show();
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}