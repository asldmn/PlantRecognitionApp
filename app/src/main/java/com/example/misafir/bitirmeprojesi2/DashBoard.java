package com.example.misafir.bitirmeprojesi2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.misafir.bitirmeprojesi2.R.string.action_settings;

public class DashBoard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ViewDatabase";

    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;

    //FIREBASE DATABASE FIELDS
    DatabaseReference mUserDatabase;
    DatabaseReference db;
    StorageReference mStorageRef;

    //IMAGE HOLD URI
    Uri imageHoldUri = null;
    String resultUri;

    //PROGRESS DIALOG
    ProgressDialog mProgress;
    String userID;

    private ListView mListView;
    CircleImageView profilePhoto;
    CircleImageView pp;
    TextView textname;
    Button button2;

    ActionBarDrawerToggle mToggle;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        //Navigation menü
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/

        //Firebase auth özelliği sayesinde sadece yetkilendirilmiş kişiler ( kullanıcı yada admin) verileri okuyabilir.
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        userID = user.getUid();
        // Read from the database
      mFirebaseDatabase.getReference("userInformation").getRef().child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(dataSnapshot.getValue()!=null){
                    showData(dataSnapshot);
                }
                else
                {
                    Toast.makeText(DashBoard.this, "Profilinize bilgi eklemek için 'Bilgilerimi düzenle' bölümüne gidin! ", Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Adding Add Value Event Listener to databaseReference.
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView =  navigationView.getHeaderView(0);
        pp = (CircleImageView) hView.findViewById(R.id.icon3);
        textname = (TextView) hView.findViewById(R.id.textname);
        textname.setText(user.getDisplayName());


        TextView profile_name = (TextView)findViewById(R.id.profile_name) ;
        profilePhoto = (CircleImageView) findViewById(R.id.icon2);
        mListView = (ListView) findViewById(R.id.listview);
        button2 = (Button) findViewById(R.id.button2);


        //Session check
        if(mAuth.getCurrentUser() != null){

            profile_name.setText(mAuth.getCurrentUser().getDisplayName());
        }

        //profil fotosunu veritabanındana çekme
       String photoUrl = "User_Photo/"+ userID + ".jpg";
       FirebaseStorage storage = FirebaseStorage.getInstance();
       StorageReference storageRef = storage.getReferenceFromUrl("gs://bitirmeprojesi2-a4e89.appspot.com").child(photoUrl);


        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profilePhoto.setImageBitmap(bitmap);
                    pp.setImageBitmap(bitmap);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Drawable drawable =getResources().getDrawable(R.drawable.asdasd);
                    profilePhoto.setImageDrawable(drawable);
                    pp.setImageDrawable(drawable);
            }
            });
        } catch (IOException e ) {
            int a=0;
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //LOGIC CHECK USER
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    finish();
                    Intent moveToHome = new Intent(DashBoard.this, LoginActivity.class);
                    moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(moveToHome);

                }

            }
        };

        //PROGRESS DIALOG
        mProgress = new ProgressDialog(this);

        //FIREBASE DATABASE INSTANCE
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("ProfilePhoto").child(mAuth.getCurrentUser().getUid());
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //ONCLICK SAVE PROFILE BTN
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //LOGIC FOR SAVING USER PROFILE
                saveUserProfile();


            }
        });

        //USER IMAGEVIEW ONCLICK LISTENER
        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //LOGIC FOR PROFILE PICTURE
                profilePicSelection();

            }
        });


    }

    public void onClickUserIcon(MenuItem item){
        Intent i = new Intent(DashBoard.this, AddUserInformation.class);
        startActivity(i);
    }

    public void onClickPasswordIcon(MenuItem item){
        Intent a = new Intent(DashBoard.this, ChangePassword.class);
        startActivity(a);
    }

    private void saveUserProfile() {

            if( imageHoldUri != null )
            {

                mProgress.setTitle("Profil kaydediliyor");
                mProgress.setMessage("Lütfen bekleyiniz....");
                mProgress.show();
                String photoName = userID + ".jpg";
                StorageReference mChildStorage = mStorageRef.child("User_Photo").child(photoName);
                String profilePicUrl = imageHoldUri.getLastPathSegment();
                Glide.with(DashBoard.this).load(profilePicUrl).into(profilePhoto);
                profilePhoto.setVisibility(View.VISIBLE);

                mChildStorage.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        final Uri imageUrl = taskSnapshot.getDownloadUrl();


                        mUserDatabase.child("userid").setValue(mAuth.getCurrentUser().getUid());
                        mUserDatabase.child("imageurl").setValue(imageUrl.toString());


                        mProgress.dismiss();

                        Uri downloadUri = taskSnapshot.getDownloadUrl();

                        Picasso.with(DashBoard.this).load(downloadUri).fit().centerCrop().into(profilePhoto);

                        Toast.makeText(DashBoard.this, "Yükleme tamamlandı", Toast.LENGTH_LONG).show();



                    }
                });
            }else
            {

                Toast.makeText(DashBoard.this, "Lütfen profil fotoğrafınızı seçmek için resmi tıklayın", Toast.LENGTH_LONG).show();

            }

    }

    private void profilePicSelection() {


        //DISPLAY DIALOG TO CHOOSE CAMERA OR GALLERY

        final CharSequence[] items = {"Fotoğraf çek", "Kütüphaneden seç",
                "İptal"};
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this);
        builder.setTitle("Fotoğraf ekle!");

        //SET ITEMS AND THERE LISTENERS
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Fotoğraf çek")) {
                    cameraIntent();
                } else if (items[item].equals("Kütüphaneden seç")) {
                    galleryIntent();
                } else if (items[item].equals("İptal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    private void cameraIntent() {

        //CHOOSE CAMERA
        Log.d("gola", "entered here");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {

        //CHOOSE IMAGE FROM GALLERY
        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }
//Sonucu al
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //SAVE URI FROM GALLERY
        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setAspectRatio(1,1)
                    .start(this);

        }else if ( requestCode == REQUEST_CAMERA && resultCode == RESULT_OK ){
            //SAVE URI FROM CAMERA

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setAspectRatio(1,1)
                    .start(this);

        }


        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();

                profilePhoto.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }

    //listviewde kullanici bilgilerini listeliyo
    private void showData(DataSnapshot ds){

        String name =  ds.child("name").getValue().toString();
        String age =  ds.child("age").getValue().toString();
        String country = ds.child("country").getValue().toString();
        String phone = ds.child("phone").getValue().toString();
        String birthday = ds.child("birthday").getValue().toString();
    /*   for(DataSnapshot ds : dataSnapshot.getChildren()){

          String s =  ds.child("name").getValue().toString();

            UserInformation uInfo = new UserInformation();
            uInfo.setName((ds.getValue(UserInformation.class)).getName()); //set the name
            uInfo.setAge(ds.child(userID).getValue(UserInformation.class).getAge()); //set the email
            uInfo.setCountry(ds.child(userID).getValue(UserInformation.class).getCountry()); //set the phone_num
            uInfo.setPhone(ds.child(userID).getValue(UserInformation.class).getPhone()); //set the phone_num
            uInfo.setBirthday(ds.child(userID).getValue(UserInformation.class).getBirthday()); //set the phone_num*/


            ArrayList<String> array  = new ArrayList<>();
            array.add("Ad Soyad       :  " + name);
            array.add("Yaş                 :  " +age);
            array.add("Şehir               :  " +country);
            array.add("Telefon No     :  " +phone);
            array.add("Doğum tarihi :  " +birthday);
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);

        mListView.setAdapter(adapter);

    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

       if (id== action_settings) {
            return true;

        }

        if (mToggle.onOptionsItemSelected(item)) {
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //here is the main place where we need to work on.
        int id=item.getItemId();


        switch (id) {

            case R.id.home_page:
                Intent h = new Intent(DashBoard.this, MainActivity.class);
                startActivity(h);
                break;
            case R.id.profil:
                if (mAuth.getCurrentUser() == null){
                    Intent i = new Intent(DashBoard.this, LoginActivity.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(DashBoard.this, DashBoard.class);
                    startActivity(i);
                }
                break;

            case R.id.using:
                Intent i = new Intent(DashBoard.this, HowToUse.class);
                startActivity(i);
                break;
            case R.id.logout:
                mAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.paylas:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND); //Share intentini oluşturuyoruz
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mesaj Konu");//share mesaj konusu
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "mobilhanem.com tarafından gönderildi");//share mesaj içeriği
                startActivity(Intent.createChooser(sharingIntent, "Paylaşmak İçin Seçiniz"));//Share intentini başlığı ile birlikte başlatıyoruz
                break;
            case R.id.mesaj:
               /* Intent i = new Intent(MainActivity.this, UsersActivity.class);
                startActivity(i);*/
                sendEmail();

                break;


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @SuppressLint("LongLogTag")
    protected void sendEmail() {
        Log.i("Send email", "");

        String[] TO = {"senakilic3@gmail.com"};
        String[] CC = {"asliduman0@gmail.com"};


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Konu");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Mesajınız..");

        try {
            startActivity(Intent.createChooser(emailIntent, "Maili Gönder..."));
            finish();
            Log.i("Mail başarıyla gönderildi...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DashBoard.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
