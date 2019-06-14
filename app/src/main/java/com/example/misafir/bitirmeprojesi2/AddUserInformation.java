package com.example.misafir.bitirmeprojesi2;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * Created by User on 2/16/2017.
 */

public class AddUserInformation extends AppCompatActivity {

    private static final String TAG = "AddToDatabase";


    Button save_edit_button,buttonSec;
    EditText profile_name,profile_age, profile_country,profile_phone,profile_birth;

    private String userID;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_inf);


        //View
        buttonSec = (Button)findViewById(R.id.buttonSec);
        save_edit_button = (Button)findViewById(R.id.save_edit_button);
        profile_name = (EditText)findViewById(R.id.profile_name);
        profile_age = (EditText)findViewById(R.id.profile_age);
        profile_country = (EditText)findViewById(R.id.profile_country);
        profile_phone = (EditText) findViewById(R.id.profile_phone);
        profile_birth = (EditText) findViewById(R.id.profile_birth);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();



        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d(TAG, "onDataChange: Added information to database: \n" +
                        dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

//tarih seçme
        buttonSec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int year = mcurrentTime.get(Calendar.YEAR);
                int month = mcurrentTime.get(Calendar.MONTH);
                int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePicker;
                datePicker = new DatePickerDialog(AddUserInformation.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {

                        profile_birth.setText( dayOfMonth + "/" + monthOfYear+ "/"+year);

                    }
                },year,month,day);//baÅŸlarken set edilcek deÄŸerlerimizi atÄ±yoruz
                datePicker.setTitle("Tarih Seçiniz");
                datePicker.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ayarla", datePicker);
                datePicker.setButton(DatePickerDialog.BUTTON_NEGATIVE, "İptal", datePicker);

                datePicker.show();

            }
        });


        save_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String  name = profile_name.getText().toString();
              String  age = profile_age.getText().toString();
              String  country = profile_country.getText().toString();
              String  phone = profile_phone.getText().toString();
              String  birth = profile_birth.getText().toString();


                if (!name.equals("") && !age.equals("") && !country.equals("")  && !birth.equals("")) {
                    UserInformation userInformation = new UserInformation(name, age, country, phone, birth);
                    myRef.child("userInformation").child(userID).setValue(userInformation);
                    toastMessage("Kullanıcı bilgileri kaydedildi.");

                    profile_name.setText("");
                    profile_age.setText("");
                    profile_country.setText("");
                    profile_phone.setText("");
                    profile_birth.setText("");

                }else{
                    toastMessage("Lütfen yıldızlı alanları doldurunuz !");
                }
            }
        });
    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
