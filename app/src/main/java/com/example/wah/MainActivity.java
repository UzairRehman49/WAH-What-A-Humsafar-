package com.example.wah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button btnLogin,test;
    EditText EmailId,Password;
    FirebaseAuth mFirebaseAuth;
    LoginButton btnLoginFb;
    CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callbackManager = CallbackManager.Factory.create();
        final String EMAIL = "email";
        // FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
        mFirebaseAuth= FirebaseAuth.getInstance();
        EmailId= findViewById(R.id.email);
        Password= findViewById(R.id.password);
        btnLogin=findViewById(R.id.login_btn);
        btnLoginFb= (LoginButton)findViewById(R.id.loginfb);
        btnLoginFb.setReadPermissions(Arrays.asList(EMAIL));
        textView=findViewById(R.id.SignUp);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,SignUp.class);
                startActivity(intent);
///                Toast.makeText(MainActivity.this, "Sign Up", Toast.LENGTH_SHORT).show();
            }
        });
        final FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mAuthStateListener= new FirebaseAuth.AuthStateListener() {
            @Override

            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if (mFirebaseUser != null)
                {
                    Toast.makeText(MainActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent i =new Intent(MainActivity.this,DashBoard.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }

            }
        };
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = EmailId.getText().toString();
                String pwd = Password.getText().toString();
                if (email.isEmpty() && !(pwd.isEmpty())) {
                    EmailId.setError("Please Enter a valid email address");
                    EmailId.requestFocus();
                } else if (pwd.isEmpty() && !(email.isEmpty())) {
                    Password.setError("Please Enter a valid Password");
                    Password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields are left empty", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                if(mFirebaseUser.isEmailVerified());
                                Intent ToHome=new Intent(MainActivity.this,DashBoard.class);
                                startActivity(ToHome);
                            }
                            else if(!mFirebaseUser.isEmailVerified())
                            Toast.makeText(MainActivity.this, "Pleasee verify your email address ", Toast.LENGTH_LONG).show();
                            else
                            {
                                Toast.makeText(MainActivity.this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                } else
                {
                    Toast.makeText(MainActivity.this,"Unknown Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                        if(isLoggedIn) {
                            Intent i =new Intent(MainActivity.this,DashBoard.class);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        {

        }

        test=findViewById(R.id.buttonTEST);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Weather.class);
                startActivity(i);
            }
        });
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

    }


}
