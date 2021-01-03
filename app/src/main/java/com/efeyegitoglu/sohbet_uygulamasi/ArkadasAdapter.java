package com.efeyegitoglu.sohbet_uygulamasi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArkadasAdapter extends RecyclerView.Adapter<ArkadasAdapter.ViewHolder> {


    List<String> userKeyList;
    Context context;
    Activity activity;

    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseAuth auth;
    FirebaseUser user;
    String userId;

    public ArkadasAdapter(List<String> userKeyList, Context context, Activity activity) {
        this.userKeyList = userKeyList;
        this.context = context;
        this.activity = activity;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        auth=FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.arkadas_layout, parent, false);
        return new ArkadasAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        reference.child("Kullanicilar").child(userKeyList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                KullaniciBilgiler k1 = dataSnapshot.getValue(KullaniciBilgiler.class);

                //String userName=dataSnapshot.child("isim").getValue().toString();
                // String userImage=dataSnapshot.child("resim").getValue().toString();
                if (!k1.getResim().equals("null")) {

                    Picasso.get().load(k1.getResim()).into(holder.arkadas_image);
                    holder.arkadas_text.setText(k1.getIsim());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return userKeyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        CircleImageView arkadas_image, arkadas_durum_image;
        TextView arkadas_text;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            arkadas_image = itemView.findViewById(R.id.arkadas_image);
            arkadas_text = itemView.findViewById(R.id.arkadas_text);


        }
    }
}
