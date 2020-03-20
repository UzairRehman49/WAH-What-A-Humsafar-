package com.example.wah;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {

    EditText userName,EmailId,Password;
    Button btnSignUp;
    FirebaseAuth mFirebaseAuth;
    data_User data_user = data_User.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mFirebaseAuth= FirebaseAuth.getInstance();
        EmailId= findViewById(R.id.email);
        userName=findViewById(R.id.UserName);
        Password=findViewById(R.id.password);
        btnSignUp=findViewById(R.id.login_btn);
        data_user.setUserName(userName.getText().toString());
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = EmailId.getText().toString();
                String pwd = Password.getText().toString();
                data_user.setEmail(email);
                data_user.setPassword(pwd);
                if (email.isEmpty() && !(pwd.isEmpty())) {
                    EmailId.setError("Please Enter a valid email address");
                    EmailId.requestFocus();
                } else if (pwd.isEmpty() && !(email.isEmpty())) {
                    Password.setError("Please Enter a valid Password");
                    Password.requestFocus();
                } else if (email.isEmpty() && pwd.isEmpty()) {
                    Toast.makeText(SignUp.this, "Fields are left empty", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty() && pwd.isEmpty())) {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignUp.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();

                            } else {
                                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("Message", "Email sent.");
                                                }
                                            }
                                        });
                                if (user != null) {
                                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder().setDisplayName(userName.getText().toString().trim()).build();
                                    user.updateProfile(profileUpdate)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                        Log.d("Display Name", userName.getText().toString());

                                                }
                                            });
                                }
                                if (user.isEmailVerified())
                                    startActivity(new Intent(SignUp.this, DashBoard.class));
                                else {
                                    mFirebaseAuth.signOut();
                                    Toast.makeText(SignUp.this,"Please veryify your email first",Toast.LENGTH_LONG);
                                    startActivity(new Intent(SignUp.this, MainActivity.class));

                                }
                            }
                        }
                    });
                } else
                {
                    Toast.makeText(SignUp.this,"Unknown Error",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
