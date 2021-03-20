package com.project.nikhil.secfamfinal;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsApi;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.nikhil.secfamfinal.Authentication.RegisterActivity;
import com.project.nikhil.secfamfinal.Post.PostActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CREDENTIAL_PICKER_REQUEST = 1;
    private ImageButton btRegister;
    private Button loginButton;
    private TextView tvLogin,getOTP;
    private TextInputEditText phone_1;
    private TextInputEditText password;
    private  TextInputLayout password_layout;
    private  PhoneAuthProvider.ForceResendingToken mResendToken;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    private String mVerificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            // getWindow().setNavigationBarColor( ContextCompat.getColor(this, R.color.bg_light)); // Navigation bar the soft bottom of some phones like nexus and some Samsung note series
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.bg)); //status bar or the time bar at the top

        }
        setContentView(R.layout.activity_main);

        btRegister = findViewById(R.id.btRegister);
        tvLogin = findViewById(R.id.tvLogin);
        btRegister.setOnClickListener((View.OnClickListener) this);
        phone_1 = findViewById(R.id.phone_1);
        password = findViewById(R.id.logInPassword);
        password_layout=findViewById(R.id.password);
        loginButton=findViewById(R.id.loginButton);

        getOTP=findViewById(R.id.tvOTP);

        mAuth=FirebaseAuth.getInstance();

        getOTP.setPaintFlags(getOTP.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        password_layout.setVisibility(View.GONE);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp =password.getText().toString();
                if(mVerificationId!=null && otp !=null)
                {PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, otp);
                signInWithPhoneAuthCredential(credential);
                }
            }
        });


        getOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password_layout.setVisibility(View.VISIBLE);
                getOtp(phone_1.getText().toString());
                Toast.makeText(MainActivity.this,"Please wait..",Toast.LENGTH_SHORT).show();
                getOTP.setClickable(false);



            }

        });

        phone_1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    phoneNumberPicker();

                }
                return false;
            }
        });


    }

    private void getOtp(String phoneNumber) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref.orderByChild("phone").equalTo(phoneNumber).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

             //   Toast.makeText(getApplicationContext(), "enteres ", Toast.LENGTH_SHORT).show();

                if (snapshot.getValue() != null) {

                    PhoneAuthOptions options =
                            PhoneAuthOptions.newBuilder(mAuth)
                                    .setPhoneNumber("+91"+phoneNumber)       // Phone number to verify
                                    .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                    .setActivity(MainActivity.this)                 // Activity (for callback binding)
                                    .setCallbacks(mcallbacks)          // OnVerificationStateChangedCallbacks
                                    .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);


                } else {
                    Toast.makeText(getApplicationContext(), "Phone number is not registered", Toast.LENGTH_SHORT).show();


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
               // Toast.makeText(MainActivity.this,"id=success"+phoneAuthCredential.getSmsCode(),Toast.LENGTH_LONG).show();
                //password.setText(phoneAuthCredential.getSmsCode());

            }



            @Override
            public void onVerificationFailed(FirebaseException e){
                Toast.makeText(MainActivity.this,"There is some problem",Toast.LENGTH_LONG).show();




            }
            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.


                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // phoneprog.setVisibility(View.INVISIBLE);
                //codelayout.setVisibility(View.VISIBLE);
                loginButton.setEnabled(true);
                loginButton.setText("Verify code");
                //btnType=1;
                //vericbut.setEnabled(true);

                // ...
            }
        };
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

    private boolean validatePassword() {
        String passwordInput = password.getText().toString().trim();
        if (passwordInput.isEmpty()) {
            password.setError("Field can't be empty");
            return false;
        } else {
            password.setError(null);
            return true;
        }
    }

    public void login(View v) {

        if (!validateUsername() | !validatePassword()) {
            return;
        }
        String input = "Username: " + phone_1.getText().toString();
        input += "\n";
        input += "Password: " + password.getText().toString();
        Toast.makeText(this, input, Toast.LENGTH_SHORT).show();
    }

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
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this,"Success",Toast.LENGTH_LONG).show();


                           Intent public_reg=new Intent(MainActivity.this, PostActivity.class);
                            startActivity(public_reg);

                        } else {
                            // Sign in failed, display a message and update the UI

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(MainActivity.this,"Enter a number",Toast.LENGTH_LONG).show();}
                        }
                    }

                });
    }

}