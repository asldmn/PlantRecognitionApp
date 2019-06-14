package com.example.misafir.bitirmeprojesi2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignUp extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database;
    DatabaseReference myRef;



    public static final String User_id ="user_id";
    public static final String User_name="user_name";
    private static final String TAG = "asd";

    Button btnSignup;
    TextView btnLogin,btnForgotPass;
    EditText input_email,input_pass, input_username;
    RelativeLayout activity_sign_up;

    FirebaseAuth auth;
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);



        FirebaseDatabase database = FirebaseDatabase.getInstance();

        myRef = database.getReference();


        //View
        btnSignup = (Button)findViewById(R.id.signup_btn_register);
        btnLogin = (TextView)findViewById(R.id.signup_btn_login);
        input_username = (EditText)findViewById(R.id.signup_username);
        input_email = (EditText)findViewById(R.id.signup_email);
        input_pass = (EditText)findViewById(R.id.signup_password);
        activity_sign_up = (RelativeLayout)findViewById(R.id.activity_sign_up);

        btnSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        //Init Firebase
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.signup_btn_login){
            startActivity(new Intent(SignUp.this,LoginActivity.class));
            finish();
        }


        else if(view.getId() == R.id.signup_btn_register){
            signUpUser(input_email.getText().toString(),input_pass.getText().toString());

        }

    }

    private void signUpUser(final String email, final String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {if(password.length() < 6)
                        {
                            Toast.makeText(SignUp.this,"Şifre 6 haneden uzun olmalı!", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(SignUp.this,"Bu mail adresi zaten kayıtlı ya da hatalı. Lütfen başka bir mail adresi giriniz!", Toast.LENGTH_SHORT).show();
                        }
                            //  snackbar = Snackbar.make(activity_sign_up,"Hata: "+task.getException(),Snackbar.LENGTH_SHORT);
                            //  snackbar.show();
                        }
                        else{
                            userProfile();

                            //  myRef.child("users")
                            //   .child(auth.getCurrentUser().getUid()).setValue(email);

                            Toast.makeText(SignUp.this,"Kayıt başarılı", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignUp.this, MainActivity.class));
                            finish();
                        }
                    }
                });


    }


    //Set UserDisplay Name
    private void userProfile()
    {
        FirebaseUser user = auth.getCurrentUser();
        if(user!= null)
        {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(input_username.getText().toString().trim())
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
        }
    }

}
