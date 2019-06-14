package com.example.misafir.bitirmeprojesi2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Favoriler extends  Fragment {
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<FavModel> favList;
    RecyclerView recycle;
    Button view;
    FavRecycleViewAdapter recyclerAdapter = null;
    RecyclerView.LayoutManager recyce = null;

    MenuItem searchItem;
    SearchView searchView;

    public Favoriler(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.favoriler, container, false);
        recycle = (RecyclerView) view.findViewById(R.id.recycle);

        mAuth = FirebaseAuth.getInstance();


        if(mAuth.getCurrentUser() != null) {

            recycle.setLayoutManager(new LinearLayoutManager(this.getContext()));
            recycle.setItemAnimator(new DefaultItemAnimator());

            database = FirebaseDatabase.getInstance();
            mAuth = FirebaseAuth.getInstance();
            myRef = database.getReference("Favorilerim").child(mAuth.getCurrentUser().getUid());


            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    favList = new ArrayList<FavModel>();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        String imageurl = dataSnapshot1.child("imageurl").getValue(String.class);
                        String visionApiData = dataSnapshot1.child("visionApiData").getValue(String.class);
                        String shopID = dataSnapshot1.child("shopID").getValue(String.class);
                        boolean favori = dataSnapshot1.child("favori").getValue(boolean.class);
                        Log.d("TAG", imageurl + " / " + visionApiData + " / " + shopID + " / " + favori);

                        FavModel fire = new FavModel();
                        fire.setImageurl(imageurl);
                        fire.setVisionApiData(visionApiData);
                        fire.setShopID(shopID);
                        fire.setFavori(favori);
                        favList.add(fire);

                        recyclerAdapter = new FavRecycleViewAdapter((ArrayList<FavModel>) favList, getActivity());
                        recyce = new GridLayoutManager(getActivity(), 1);

                        recycle.setLayoutManager(recyce);
                        recycle.setItemAnimator(new DefaultItemAnimator());
                        recycle.setAdapter(recyclerAdapter);

                        recycle.setAdapter(recyclerAdapter);
                    }


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("Failed to read value.", error.toException());
                }

            });
        }

        else
        {
       //     Toast.makeText(view.getContext(),
                 //   "Favorilere eklemek istiyorsanız lütfen giriş yapınız.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }


 /*   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {

        inflater.inflate(R.menu.search_menu, menu);

        searchItem = menu.findItem(R.id.search_menu);


        searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                //FILTER AS YOU TYPE
                recyclerAdapter.getFilter().filter(query);
                return false;
            }
        });


        return;
    }
*/



}
