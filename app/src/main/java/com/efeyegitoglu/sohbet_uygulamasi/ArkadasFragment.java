package com.efeyegitoglu.sohbet_uygulamasi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class ArkadasFragment extends Fragment {

    View view;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;

    RecyclerView arkadas_RecyclerView;
    ArkadasAdapter arkadasAdapter;
    List<String> keyList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_arkadas, container, false);

        tanimla();
        getArkadasList();


        return view;
    }

    void tanimla(){
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();

        database=FirebaseDatabase.getInstance();
        reference=database.getReference().child("Arkadaslar");

        keyList=new ArrayList<>();
        arkadas_RecyclerView=view.findViewById(R.id.arkadas_RecyclerView);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getContext(),1);
        arkadas_RecyclerView.setLayoutManager(layoutManager);

        arkadasAdapter=new ArkadasAdapter(keyList,getContext(),getActivity());
        arkadas_RecyclerView.setAdapter(arkadasAdapter);





    }

    void getArkadasList(){
        reference.child(user.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                keyList.add(dataSnapshot.getKey());
                arkadasAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                keyList.remove(dataSnapshot.getKey());
                arkadasAdapter.notifyDataSetChanged();

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
