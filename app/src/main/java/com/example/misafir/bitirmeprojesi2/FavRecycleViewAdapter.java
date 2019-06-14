package com.example.misafir.bitirmeprojesi2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.Random;


public class FavRecycleViewAdapter extends RecyclerView.Adapter<FavRecycleViewAdapter.MyHoder> implements Filterable {
    ArrayList<FavModel> favList,filterList;
    Context context;
    CustomFilterFav filter;


    FirebaseAuth mAuth;
    private String users;
    private String usersId;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Boolean favladi = false;


    public  FavRecycleViewAdapter(ArrayList<FavModel> favList,  Context context){
        this.favList =favList;
        this.context=context;
        this.filterList=favList;

    }



    @Override
    public FavRecycleViewAdapter.MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.add_cards,parent,false);
        MyHoder myHoder = new MyHoder(view);



        mAuth =  FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        return myHoder;

    }


    //arayÃ¼z
    @Override
    public void onBindViewHolder(final MyHoder holder, final int position) {


        //buradaki list aslÄ±nda listelenen itemler, mylist ile seÃ§ili item demek oluyo
        final FavModel mylist = favList.get(position);

        // initializeViews(mylist, holder,position);

        holder.apiData.setText(mylist.getvisionApiData());


        Glide.with(context).load(mylist.getImageurl()).into(holder.image);
        holder.imageButton.setImageResource(R.drawable.ic_favorite_black_24dp);


        holder.txtOptionDigit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Display option menu

                PopupMenu popupMenu = new PopupMenu(context, holder.txtOptionDigit);
                popupMenu.inflate(R.menu.popup_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.delete:
                                //Delete
                                favList.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Arama sonucu silindi", Toast.LENGTH_LONG).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }

                });


                popupMenu.show();
            }
        });

       holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                favladi = true;

              /*  myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(favladi){
                            final String post_key = favList.get(position).getShopID();
                            if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(post_key)) {
                                myRef.child(mAuth.getCurrentUser().getUid()).child(post_key);
                                favList.remove(position);
                                favladi = false;
                                holder.imageButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);

                            } else
                            {
                                FavModel fav = new FavModel(mylist.getImageurl(), mylist.getvisionApiData(), mylist.getShopID());
                                // myRef.child()
                                myRef.setValue(fav);
                                favladi=false;
                                holder.imageButton.setImageResource(R.drawable.ic_favorite_black_24dp);


                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/


            }
        });

    }


    private void initializeViews(FavModel mylist, MyHoder holder, int position) {
    }


    @Override
    public int getItemCount() {
        int arr =0;

        try {
            if (favList.size()==0){
                arr=0;
            }
            else{
                arr=favList.size();
            }
        }catch (Exception e){

        }
        return arr;

    }



    class MyHoder extends RecyclerView.ViewHolder {
        TextView apiData,txtOptionDigit;
        ImageView image, imageButton;

        public MyHoder(View itemView) {
            super(itemView);
            apiData = (TextView) itemView.findViewById(R.id.apiData);
            image = (ImageView) itemView.findViewById(R.id.image);
            imageButton = (ImageView) itemView.findViewById(R.id.imageButton);
            txtOptionDigit = (TextView) itemView.findViewById(R.id.txtOptionDigit);


        }
    }


    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilterFav(filterList,this);        }

        return filter;
    }

}

/*    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Konum");
    Query ref = databaseReference.orderByChild("uid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
    final FirebaseAuth auth;
    final FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<Konumlar, RecyclerAdapter.MyHoder>(
            Konumlar.cass,
            R.layout.add_cards,
            RecyclerAdapter.MyHoder.class,
            ref
    ) {
        @Override
        protected void populateViewHolder(final RecyclerAdapter.MyHoder viewHolder, final Konumlar model, final int position) {
            final Konumlar mylist = list.get(position);


            viewHolder.header.setText(model.getHeader());
            viewHolder.username.setText(model.getUsername());
            viewHolder.location.setText(model.getLocation());



            Glide.with(context).load(mylist.getImageurl()).into(viewHolder.image);

            viewHolder.txtOptionDigit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    //Display option menu

                    PopupMenu popupMenu = new PopupMenu(context, viewHolder.txtOptionDigit);
                    popupMenu.inflate(R.menu.popup_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.message:

                                    //  FirebaseUser kullanici = mAuth.getCurrentUser();
                                    //    users= kullanici.getUid();
                                    //  usersId = mylist.getUid();

                                    Intent intent = new Intent(v.getContext(), ChatActivity.class);
                                    //   intent.putExtra("user",users);
                                    //  intent.putExtra("userId",usersId);
                                    v.getContext().startActivity(intent);

                                    break;

                                case R.id.favorite:
                                    //  Toast.makeText(context, "Favorilere kaydedildi", Toast.LENGTH_LONG).show();
                                    break;
                                case R.id.delete:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage(model.getHeader() + "baÅŸlÄ±klÄ± fotoÄŸrafÄ± silmek istiyor musunuz")

                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // continue with delete

                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // do nothing
                                                }
                                            }) .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();


                                    //Delete
                                    list.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "FotoÄŸraf silindi", Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    break;
                            }
                            return false;
                        }

                    });


                    popupMenu.show();
                }
            });



        }
    };
*/
