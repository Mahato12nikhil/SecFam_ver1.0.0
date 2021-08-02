package com.project.nikhil.secfamfinal1.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.project.nikhil.secfamfinal1.BaseActivity;
import com.project.nikhil.secfamfinal1.MainActivity;
import com.project.nikhil.secfamfinal1.R;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private String mVerificationId;
    private String name,phone,otp;
    private Button otpVerify;
    private TextInputEditText otpText;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    TextView tvTimer,reGenerateOTP;

    private final int countDownInterval = 1000;
    private final int totalTime = 60000;
    private final CountDownTimer timer  = new CountDownTimer(totalTime,
            countDownInterval) {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void onTick(long l) {
            tvTimer.setText("00." + String.format("%02d",l / countDownInterval));
        }

        @Override
        public void onFinish() {
            tvTimer.setVisibility(View.INVISIBLE);
            reGenerateOTP.setVisibility(View.VISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        Log.i("!!!Activity", "OtpActivity");
        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.bg)); //status bar or the time bar at the top
        }

        otpVerify = findViewById(R.id.otpVerify);
        otpText = findViewById(R.id.otpText);
        tvTimer = findViewById(R.id.tvTimer);
        reGenerateOTP = findViewById(R.id.reGenerateOTP);


        name=getIntent().getStringExtra("name");
        //=getIntent().getStringExtra("verification_id");
         phone=getIntent().getStringExtra("phone");

        if(phone!=null)
        {                  //  Toast.makeText(getApplicationContext(),""+phone,Toast.LENGTH_SHORT).show();

            getOtp(phone);
          //  Toast.makeText(getApplicationContext(),""+phone,Toast.LENGTH_SHORT).show();

        }

        mAuth=FirebaseAuth.getInstance();

        otpVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp=otpText.getText().toString();
                if(!otp.equals("")) {
                   Toast.makeText(getApplicationContext(),""+otp,Toast.LENGTH_SHORT).show();
                   PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                }
                else {
                    otpText.setError("Please enter valid OTP");
                    otpText.requestFocus();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                }
            }
        });
        reGenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reGenerateOTP.setVisibility(View.INVISIBLE);
                getOtp(phone);
            }
        });

    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(OtpActivity.this,"Registration Successful! Please Login.",Toast.LENGTH_LONG).show();
                            doNext();
                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                otpText.setError("Please enter valid OTP");
                                otpText.requestFocus();
                                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                            }
                        }
                    }

                });
    }

    private void doNext() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        reference.child("name").setValue(name);
        reference.child("phone").setValue(phone);
        reference.child("id").setValue(mAuth.getCurrentUser().getUid());

        FirebaseDatabase.getInstance().getReference().child("Search").child("Users").child(mAuth.getCurrentUser().getUid())
                .child("name").setValue(name);
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Follow").child(mAuth.getCurrentUser().getUid());
        ref.child("followers").child(mAuth.getCurrentUser().getUid()).setValue(true);
        ref.child("following").child(mAuth.getCurrentUser().getUid()).setValue(true);


        FirebaseAuth.getInstance().signOut();
      /*  Intent public_reg=new Intent(OtpActivity.this, MainActivity.class);
        startActivity(public_reg);*/
        finish();

    }

    private void getOtp(String phoneNumber) {
        showProgressDialog();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.orderByChild("phone").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), "Phone number is already registered ,Please log in", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber("+91"+phoneNumber)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(OtpActivity.this)                 // Activity (for callback binding)
                                    .setCallbacks(mcallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideProgressDialog();
            }
        });


        mcallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                hideProgressDialog();
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }



            @Override
            public void onVerificationFailed(FirebaseException e){
                hideProgressDialog();
                Toast.makeText(OtpActivity.this,"There is some problem",Toast.LENGTH_LONG).show();
                Log.d("OTP",e.toString());
                reGenerateOTP.setVisibility(View.VISIBLE);
            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                hideProgressDialog();
                tvTimer.setVisibility(View.VISIBLE);
                timer.start();
                 mVerificationId= verificationId;

            }
        };
    }



}