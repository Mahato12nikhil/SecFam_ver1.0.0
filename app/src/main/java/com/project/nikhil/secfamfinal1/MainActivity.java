package com.project.nikhil.secfamfinal1;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
import com.project.nikhil.secfamfinal1.Authentication.RegisterActivity;
import com.project.nikhil.secfamfinal1.Post.PostActivity;

import java.util.concurrent.TimeUnit;

public class MainActivity extends BaseActivity {

    private static final int CREDENTIAL_PICKER_REQUEST = 1;
    private TextView btRegister;
   private Button loginButton;
    private TextView tvLogin, tvChangeMobile, tvTimer, reGenerateOTP;
    private TextInputEditText phone_1;
//    private TextInputEditText password;
//    private TextInputLayout password_layout;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    private String mVerificationId;
    private FrameLayout regenerateOTPLayout;

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

        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.bg)); //status bar or the time bar at the top

        }
        setContentView(R.layout.activity_main);
        disableAutofill();
        Log.i("!!!Activity", "MainActivity");
        /*FirebaseDatabase.getInstance().getReference("Hello").child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String s = snapshot.getValue(String.class);
                Log.i("!!!!!Hello",s);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("!!!!!REEor",error.toString());
            }
        });*/
        btRegister = findViewById(R.id.btRegister);
        tvLogin = findViewById(R.id.tvLogin);
        btRegister.setOnClickListener((View.OnClickListener) this);
        phone_1 = findViewById(R.id.phone_1);
        //password = findViewById(R.id.logInPassword);
       // password_layout = findViewById(R.id.password);
        tvChangeMobile = findViewById(R.id.tvChangeMobile);
        tvTimer = findViewById(R.id.tvTimer);
        reGenerateOTP = findViewById(R.id.reGenerateOTP);
        regenerateOTPLayout = findViewById(R.id.regenerateOTPLayout);
        tvChangeMobile.setOnClickListener(this);
        reGenerateOTP.setOnClickListener(this);

        loginButton = findViewById(R.id.loginButton);

        mAuth = FirebaseAuth.getInstance();

       // password_layout.setVisibility(View.GONE);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(phone_1.getText().toString()) || phone_1.getText().toString().length() < 10) {
                    phone_1.setError("Please enter a valid 10 digit mobile no.");
                    phone_1.requestFocus();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                } else {
                    //password_layout.setVisibility(View.VISIBLE);
                    getOtp(phone_1.getText().toString());
                    //Toast.makeText(MainActivity.this, "Please wait..", Toast.LENGTH_SHORT).show();

                }
            }

        });

       /* phone_1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    phoneNumberPicker();

                }
                return false;
            }
        });*/

    }
    @TargetApi(Build.VERSION_CODES.O)
    private void disableAutofill() {
        getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);
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
                    phone_1.setEnabled(false);
                    phone_1.setClickable(false);
                    loginButton.setVisibility(View.GONE);
                    regenerateOTPLayout.setVisibility(View.VISIBLE);

                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber("+91" + phoneNumber)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(MainActivity.this)                 // Activity (for callback binding)
                                    .setCallbacks(mcallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);


                } else {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), "Phone number is not registered", Toast.LENGTH_SHORT).show();
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
                // Toast.makeText(MainActivity.this,"id=success"+phoneAuthCredential.getSmsCode(),Toast.LENGTH_LONG).show();
                //password.setText(phoneAuthCredential.getSmsCode());

            }


            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(MainActivity.this, "There is some problem", Toast.LENGTH_LONG).show();
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

    private void phoneNumberPicker() {
        HintRequest hintRequest = new HintRequest.Builder()
                .setPhoneNumberIdentifierSupported(true)
                .build();


        PendingIntent intent = Credentials.getClient(MainActivity.this).getHintPickerIntent(hintRequest);
        try {
            startIntentSenderForResult(intent.getIntentSender(), CREDENTIAL_PICKER_REQUEST, null, 0, 0, 0, new Bundle());
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onClick(View v) {
        if (v == btRegister) {
            Intent intent = new Intent ( MainActivity.this , RegisterActivity.class );
            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View, String> ( tvLogin , "tvLogin" );
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation ( MainActivity.this , pairs );
            startActivity ( intent , activityOptions.toBundle () );
        }else if (v == tvChangeMobile){
            timer.cancel();
            //password_layout.setVisibility(View.GONE);
            phone_1.setEnabled(true);
            phone_1.setClickable(true);
            regenerateOTPLayout.setVisibility(View.GONE);
            reGenerateOTP.setVisibility(View.GONE);
            tvTimer.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }else if (v == reGenerateOTP){
            reGenerateOTP.setVisibility(View.GONE);
            getOtp(phone_1.getText().toString());

        }
    }

    private boolean validateUsername() {
        String usernameInput = phone_1.getText().toString().trim();
        if (usernameInput.isEmpty()) {
            phone_1.setError("Field can't be empty");
            return false;
        } else {
            phone_1.setError(null);
            return true;
        }
    }

   /* private boolean validatePassword() {
        String passwordInput = password.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }*/
/*
    public void login(View v) {

        if (!validateUsername() | !validatePassword()) {
            return;
        }
        String input = "Username: " + phone_1.getText().toString();
        input += "\n";
        input += "Password: " + password.getText().toString();
        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == RESULT_OK) {
            // Obtain the phone number from the result
            Credential credentials = data.getParcelableExtra(Credential.EXTRA_KEY);
            phone_1.setText(credentials.getId().substring(3));//get the selected phone number
//Do what ever you want to do with your selected phone number here


        } else if (requestCode == CREDENTIAL_PICKER_REQUEST && resultCode == CredentialsApi.ACTIVITY_RESULT_NO_HINTS_AVAILABLE) {
            // *** No phone numbers available ***
            Toast.makeText(MainActivity.this, "No phone numbers found", Toast.LENGTH_LONG).show();
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
                            Toast.makeText(MainActivity.this,"Login Successful",Toast.LENGTH_LONG).show();

                           Intent public_reg=new Intent(MainActivity.this, PostActivity.class);
                            startActivity(public_reg);
                            finish();

                        } else {
                            // Sign in failed, display a message and update the UI
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(MainActivity.this,"Enter a number",Toast.LENGTH_LONG).show();}
                        }
                    }
                });
    }

}