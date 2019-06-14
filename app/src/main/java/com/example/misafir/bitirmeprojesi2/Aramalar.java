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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Aramalar extends  Fragment {
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<ApiModel> list;
    List<FavModel> favList;
    RecyclerView recycle;
    Button view;
    RecyclerAdapter recyclerAdapter = null;
    RecyclerView.LayoutManager recyce = null;

    MenuItem searchItem;
    SearchView searchView;

    public Aramalar(){
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.aramalar, container, false);
        recycle = (RecyclerView) view.findViewById(R.id.recycle);
        database = FirebaseDatabase.getInstance();


        recycle.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recycle.setItemAnimator(new DefaultItemAnimator());



        myRef = database.getReference("visionApiData");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                list = new ArrayList<ApiModel>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                    ApiModel value = dataSnapshot1.getValue(ApiModel.class);
                    ApiModel fire = new ApiModel();
                    String image = value.getImageurl();
                    String visionApiData = value.getvisionApiData();
                    String id = value.getShopID();

                    fire.setImageurl(image);
                    fire.setVisionApiData(visionApiData);
                    fire.setShopID(id);
                    list.add(fire);
                }

                recyclerAdapter = new RecyclerAdapter((ArrayList<ApiModel>) list, getActivity());
                recyce = new GridLayoutManager(getActivity(), 1);

                recycle.setLayoutManager(recyce);
                recycle.setItemAnimator(new DefaultItemAnimator());
                recycle.setAdapter(recyclerAdapter);

                recycle.setAdapter(recyclerAdapter);




            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Failed to read value.", error.toException());
            }

        });

        return view;
    }


    @Override
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




}
