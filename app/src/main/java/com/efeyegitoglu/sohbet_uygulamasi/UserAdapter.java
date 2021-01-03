package com.efeyegitoglu.sohbet_uygulamasi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<String> userKeyList;
    Context context;
    Activity activity;

    FirebaseDatabase database;
    DatabaseReference reference;

    public UserAdapter(List<String> userKeyList, Context context, Activity activity) {
        this.userKeyList = userKeyList;
        this.context = context;
        this.activity = activity;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
    }

    //layout tanımlaması yapılacak
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.userlayout, parent, false);
        return new ViewHolder(view);
    }


    //view set işlemleri
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        //holder.usernameTextView.setText(userKeyList.get(position));

        reference.child("Kullanicilar").child(userKeyList.get(position).toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KullaniciBilgiler kullaniciBilgiler = dataSnapshot.getValue(KullaniciBilgiler.class);
                if (!kullaniciBilgiler.getIsim().equals("null")) {
                    Picasso.get().load(kullaniciBilgiler.getResim()).into(holder.userimage);
                    holder.usernameTextView.setText(kullaniciBilgiler.getIsim());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.userAnaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(context);
                changeFragment.changeWithParameter(new OtherProfileFragment(), userKeyList.get(position));

            }
        });

    }


    //list.size döndürür
    @Override
    public int getItemCount() {
        return userKeyList.size();
    }


    //Ana class tanumlamaları
    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userimage;
        TextView usernameTextView;
        LinearLayout userAnaLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userimage = itemView.findViewById(R.id.userimage);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            userAnaLayout = itemView.findViewById(R.id.userAnaLayout);


        }
    }
}
