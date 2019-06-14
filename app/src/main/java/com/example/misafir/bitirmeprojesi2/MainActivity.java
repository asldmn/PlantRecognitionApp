package com.example.misafir.bitirmeprojesi2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_CAMERA = 3;
    private static final int SELECT_FILE = 2;


    private TabLayout tabLayout;
    private ViewPager viewPager;
    String userID;
    String userName;
    FirebaseAuth mAuth;
    Uri imageHoldUri = null;
    Uri imageUrl;
    Uri uri;
    String imageurll;
    DatabaseReference mDatabase;


    CircleImageView pp;
    TextView textname;

    private Bitmap bitmap;

    private Sonuc sonuc;
    private Aramalar aramalar;
    private Favoriler favoriler;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("visionApiData").push();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhotoSelection();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    public void onClickUserIcon(MenuItem item){
        if (mAuth.getCurrentUser() == null){
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
        else {
            Intent i = new Intent(MainActivity.this, DashBoard.class);
            startActivity(i);
        }
    }

    //kamera için

    private void addPhotoSelection() {

        final CharSequence[] items = {"Fotoğraf çek", "Kütüphaneden seç",
                "İptal"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {

            case R.id.home_page:
                Intent h = new Intent(MainActivity.this, MainActivity.class);
                startActivity(h);
                break;
            case R.id.profil:
                if (mAuth.getCurrentUser() == null){
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(MainActivity.this, DashBoard.class);
                    startActivity(i);
                }
                break;

            case R.id.using:
                Intent i = new Intent(MainActivity.this, HowToUse.class);
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
    public void setupViewPager(ViewPager upViewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        aramalar = new Aramalar();
        favoriler = new Favoriler();
        sonuc= new Sonuc();
        adapter.addFragment(aramalar,"Aramalar");
        adapter.addFragment(sonuc, "Sonuç");
        adapter.addFragment(favoriler, "Favoriler");

        viewPager.setAdapter(adapter);
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
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            bitmap = (Bitmap) data.getExtras().get("data");
            sonuc.GetBitmap(bitmap, imageUri);
        }


        //image crop library code
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageHoldUri = result.getUri();
                sonuc.imageView.setImageURI(imageHoldUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        else if (requestCode == SELECT_FILE && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            sonuc.GetUri(imageUri);

        }
    }

    public void onClick(View view) {
        if (sonuc.visionApiData != null){
            saveUserInformation();
        }else {
            Toast.makeText(this,"Henüz kaydedilcek sonuç bulunamadı!",Toast.LENGTH_LONG).show();
        }

    }

    private void saveUserInformation() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://bitirmeprojesi2-a4e89.appspot.com").child("Add_Photo").child(imageHoldUri.getLastPathSegment());
        storageRef.putFile(imageHoldUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                imageUrl = taskSnapshot.getDownloadUrl();
                imageurll= imageUrl.getPath().toString();
                mDatabase.child("imageurl").setValue(imageUrl.toString());


            }
        });
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = firebaseDatabase.getReference().child("visionApiData").push();
        String id = myRef.getKey();
        String visionApi = sonuc.visionApiData.getText().toString().trim();

        ApiModel userInformation=new ApiModel(  imageurll,  visionApi, id);

        mDatabase.setValue(userInformation);
        Toast.makeText(this,"Kaydedildi",Toast.LENGTH_LONG).show();
        tabLayout.getTabAt(0).select();
    }


}

