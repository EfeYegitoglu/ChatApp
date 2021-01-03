package com.efeyegitoglu.sohbet_uygulamasi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;


public class BildirimFragment extends Fragment {

    View view;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;

    String userId;

    List<String> friend_req_key_list;
    RecyclerView friend_req_recycler;
    Friend_Req_Adapter friend_req_adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_bildirim, container, false);

        tanimla();
        istekler();


        return view;
    }

    void tanimla() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Arkadaslik_Istek");

        friend_req_key_list = new ArrayList<>();
        friend_req_recycler = view.findViewById(R.id.friend_req_recycler);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
        friend_req_recycler.setLayoutManager(layoutManager);
        friend_req_adapter = new Friend_Req_Adapter(friend_req_key_list, getContext(), getActivity());
        friend_req_recycler.setAdapter(friend_req_adapter);
    }


    void istekler() {

        reference.child(userId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    String kontrol = dataSnapshot.child("tip").getValue().toString();
                    if (kontrol.equals("aldi")) {
                        friend_req_key_list.add(dataSnapshot.getKey());
                        friend_req_adapter.notifyDataSetChanged();
                    }



            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                friend_req_key_list.remove(dataSnapshot.getKey());
                friend_req_adapter.notifyDataSetChanged();

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
