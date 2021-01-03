package com.efeyegitoglu.sohbet_uygulamasi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class AnaSayfaFragment extends Fragment {

    View view;

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    List<String> userKeyList;
    RecyclerView userListRecylerView;
    UserAdapter userAdapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ana_sayfa, container, false);

        tanimla();
        kullaniciGetir();


        return view;
    }


    void tanimla(){
         database=FirebaseDatabase.getInstance();
         reference=database.getReference();

         auth=FirebaseAuth.getInstance();
         user=auth.getCurrentUser();

         userKeyList=new ArrayList<>();
         userListRecylerView=view.findViewById(R.id.userListRecylerView);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),1);
        userListRecylerView.setLayoutManager(layoutManager);
        userAdapter=new UserAdapter(userKeyList,getContext(),getActivity());
        userListRecylerView.setAdapter(userAdapter);
    }


    void kullaniciGetir(){
        reference.child("Kullanicilar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                reference.child("Kullanicilar").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        KullaniciBilgiler kullaniciBilgiler = dataSnapshot.getValue(KullaniciBilgiler.class);
                        if (!kullaniciBilgiler.getIsim().equals("null") && !dataSnapshot.getKey().equals(user.getUid())){
                            userKeyList.add(dataSnapshot.getKey());
                            userAdapter.notifyDataSetChanged();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
