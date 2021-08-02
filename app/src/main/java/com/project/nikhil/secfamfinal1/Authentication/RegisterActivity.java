package com.project.nikhil.secfamfinal1.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.project.nikhil.secfamfinal1.Model.User;
import com.project.nikhil.secfamfinal1.R;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends BaseActivity {

    private static final int CREDENTIAL_PICKER_REQUEST =2 ;
    private Animation mAnimation;
   private RelativeLayout rlayout;
   private TextInputEditText signUpOtp,signUpName,signUpPhone;
   private Button SignUpNextButton;
    private FrameLayout regenerateOTPLayout;
    private TextView tvChangeMobile, tvTimer, reGenerateOTP;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    private String mVerificationId;
    String phoneNumber="";//TODO Delete number

    private final int countDownInterval = 1000;
    private final int totalTime = 60000;
    private final CountDownTimer timer = new CountDownTimer(totalTime,
            countDownInterval) {
        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        @Override
        public void onTick(long l) {
            tvTimer.setText("00." + String.format("%02d", l / countDownInterval));
        }

        @Override
        public void onFinish() {
            tvTimer.setVisibility(View.GONE);
            reGenerateOTP.setVisibility(View.VISIBLE);
            hideProgressDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        disableAutofill();
        Log.i("!!!Activity", "RegisterActivity");

        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.bg)); //status bar or the time bar at the top
        }
        tvChangeMobile = findViewById(R.id.tvChangeMobile);
        tvTimer = findViewById(R.id.tvTimer);
        reGenerateOTP = findViewById(R.id.reGenerateOTP);
        regenerateOTPLayout = findViewById(R.id.regenerateOTPLayout);
        tvChangeMobile.setOnClickListener(this);
        reGenerateOTP.setOnClickListener(this);
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

        /* signUpPhone.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View view, MotionEvent motionEvent) {
                 if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                     phoneNumberPicker();
                 }
                 return false;
             }
         });*/
         SignUpNextButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 if(isFormValid()) {
                     //Toast.makeText(getApplicationContext(),""+phoneNumber,Toast.LENGTH_SHORT).show();
                    /* Intent intent = new Intent(RegisterActivity.this, OtpActivity.class);
                     intent.putExtra("phone",signUpPhone.getText().toString() );
                     intent.putExtra("name", signUpName.getText().toString());
                     startActivity(intent);
                     finish();*/
                     getOtp(signUpPhone.getText().toString());


                 }
             }
         });

    }
    @TargetApi(Build.VERSION_CODES.O)
    private void disableAutofill() {
        getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
    }
    public void onClick(View v) {
        if (v == tvChangeMobile){
            timer.cancel();
            //password_layout.setVisibility(View.GONE);
            signUpName.setEnabled(true);
            signUpName.setClickable(true);
            signUpPhone.setEnabled(true);
            signUpPhone.setClickable(true);
            regenerateOTPLayout.setVisibility(View.GONE);
            reGenerateOTP.setVisibility(View.GONE);
            tvTimer.setVisibility(View.GONE);
            SignUpNextButton.setVisibility(View.VISIBLE);
        }else if (v == reGenerateOTP){
            reGenerateOTP.setVisibility(View.GONE);
            getOtp(signUpPhone.getText().toString());
        }
    }
    private boolean isFormValid() {
        if (TextUtils.isEmpty(signUpName.getText().toString())){
            signUpName.setError("Please enter your Name.");
            signUpName.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return false;
        }else if (TextUtils.isEmpty(signUpPhone.getText().toString()) || signUpPhone.getText().toString().length()<10){
            signUpPhone.setError("Please enter a valid 10 digit mobile no.");
            signUpPhone.requestFocus();
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            return false;
        }
        return true;
    }

    private void getOtp(String phoneNumber) {
        if (isInternetAvailable()){
            showProgressDialog();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
            ref.keepSynced(true);
            ref.orderByChild("phone").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    //   Toast.makeText(getApplicationContext(), "enteres ", Toast.LENGTH_SHORT).show();
                    if (snapshot.getValue() != null) {
                        hideProgressDialog();
                        Toast.makeText(getApplicationContext(), "Phone number is already registered, Please login.", Toast.LENGTH_SHORT).show();

                    } else {
                        signUpName.setEnabled(false);
                        signUpName.setClickable(false);
                        signUpPhone.setEnabled(false);
                        signUpPhone.setClickable(false);
                        SignUpNextButton.setVisibility(View.GONE);
                        regenerateOTPLayout.setVisibility(View.VISIBLE);
                        PhoneAuthOptions options =
                                PhoneAuthOptions.newBuilder(mAuth)
                                        .setPhoneNumber("+91" + phoneNumber)       // Phone number to verify
                                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                        .setActivity(RegisterActivity.this)                 // Activity (for callback binding)
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


            mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                    // Toast.makeText(RegisterActivity.this,"id=success"+phoneAuthCredential.getSmsCode(),Toast.LENGTH_LONG).show();
                    //password.setText(phoneAuthCredential.getSmsCode());

                }


                @Override
                public void onVerificationFailed(FirebaseException e) {
                    Toast.makeText(RegisterActivity.this, "There is some problem", Toast.LENGTH_LONG).show();
                    hideProgressDialog();
                }

                @Override
                public void onCodeSent(String verificationId,
                                       PhoneAuthProvider.ForceResendingToken token) {
                    hideProgressDialog();
                    showProgressDialog("Please wait... OTP will be read automatically");
                    tvTimer.setVisibility(View.VISIBLE);
                    timer.start();
                    // The SMS verification code has been sent to the provided phone number, we
                    // now need to ask the user to enter the code and then construct a credential
                    // by combining the code with a verification ID.


                    // Save verification ID and resending token so we can use them later
                    mVerificationId = verificationId;
                    mResendToken = token;
                    // phoneprog.setVisibility(View.INVISIBLE);
                    //codelayout.setVisibility(View.VISIBLE);
                    //btnType=1;
                    //vericbut.setEnabled(true);

                    // ...
                }
            };
        }

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideProgressDialog();
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(RegisterActivity.this,"Registration Successful! Please Login.",Toast.LENGTH_LONG).show();
                            doNext();

                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(RegisterActivity.this,"Enter a number",Toast.LENGTH_LONG).show();}
                        }
                    }
                });
    }

    private void doNext() {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        User user = new User();
        user.setId(mAuth.getCurrentUser().getUid());
        user.setName(signUpName.getText().toString());
        user.setPhone(signUpPhone.getText().toString());
        reference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                FirebaseDatabase.getInstance().getReference().child("Search").child("Users").child(mAuth.getCurrentUser().getUid())
                        .child("name").setValue(signUpName.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("Follow").child(mAuth.getCurrentUser().getUid());
                        ref.child("followers").child(mAuth.getCurrentUser().getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                ref.child("following").child(mAuth.getCurrentUser().getUid()).setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        FirebaseAuth.getInstance().signOut();
                                        Intent intent=new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                    }
                });
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