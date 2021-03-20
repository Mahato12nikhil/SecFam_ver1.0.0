package com.project.nikhil.secfamfinal.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.project.nikhil.secfamfinal.MainActivity;
import com.project.nikhil.secfamfinal.Post.PostActivity;
import com.project.nikhil.secfamfinal.R;

import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String mVerificationId;
    private String name,phone,otp;
    private Button otpVerify;
    private TextInputEditText otpText;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.bg)); //status bar or the time bar at the top
        }

        otpVerify=findViewById(R.id.otpVerify);
        otpText=findViewById(R.id.otpText);

        ;

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

                if( otp!=null) {
                   Toast.makeText(getApplicationContext(),""+otp,Toast.LENGTH_SHORT).show();
                   PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                    signInWithPhoneAuthCredential(credential);
                }
                else {

                }
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
                            Toast.makeText(OtpActivity.this,"OTP VERIFIED!!",Toast.LENGTH_LONG).show();


                            doNext();

                        } else {
                            // Sign in failed, display a message and update the UI

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OtpActivity.this,"Enter a number",Toast.LENGTH_LONG).show();}
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
        Intent public_reg=new Intent(OtpActivity.this, MainActivity.class);
        startActivity(public_reg);

    }

    private void getOtp(String phoneNumber) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.orderByChild("phone").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Toast.makeText(getApplicationContext(), "Phone number is already registered ,Please log in", Toast.LENGTH_SHORT).show();




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

            }
        });


        mcallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);


            }



            @Override
            public void onVerificationFailed(FirebaseException e){
                Toast.makeText(OtpActivity.this,"There is some problem",Toast.LENGTH_LONG).show();




            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                 mVerificationId= verificationId;

            }
        };
    }



}