package com.efeyegitoglu.sohbet_uygulamasi;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Friend_Req_Adapter extends RecyclerView.Adapter<Friend_Req_Adapter.ViewHolder> {

    List<String> userKeyList;
    Context context;
    Activity activity;

    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseUser user;
    String userId;

    public Friend_Req_Adapter(List<String> userKeyList, Context context, Activity activity) {
        this.userKeyList = userKeyList;
        this.context = context;
        this.activity = activity;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();
        userId=user.getUid();
    }

    //layout tanımlaması yapılacak
    @Override
    public Friend_Req_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.friend_req_layout, parent, false);
        return new Friend_Req_Adapter.ViewHolder(view);
    }


    //view set işlemleri
    @Override
    public void onBindViewHolder(@NonNull final Friend_Req_Adapter.ViewHolder holder, final int position) {
        //holder.usernameTextView.setText(userKeyList.get(position));

        reference.child("Kullanicilar").child(userKeyList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KullaniciBilgiler kullaniciBilgiler = dataSnapshot.getValue(KullaniciBilgiler.class);
                if (!kullaniciBilgiler.getIsim().equals("null")) {
                    Picasso.get().load(kullaniciBilgiler.getResim()).into(holder.friend_req_image);
                    holder.friend_req_text.setText(kullaniciBilgiler.getIsim());

                    holder.friend_req_kabulet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            kabulEt(userId,userKeyList.get(position));
                        }


                    });

                    holder.friend_req_reddet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            reddet(userId,userKeyList.get(position));
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

        CircleImageView friend_req_image;
        TextView friend_req_text;
        Button friend_req_kabulet,friend_req_reddet;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            friend_req_image = itemView.findViewById(R.id.friend_req_image);
            friend_req_text = itemView.findViewById(R.id.friend_req_text);
            friend_req_reddet=itemView.findViewById(R.id.friend_req_reddet);
            friend_req_kabulet=itemView.findViewById(R.id.friend_req_kabulet);



        }
    }

    void kabulEt(final String userId, final String otherId){

        DateFormat df=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date today= Calendar.getInstance().getTime();

        final  String reportDate=df.format(today);


        reference.child("Arkadaslar").child(userId).child(otherId).child("tarih").setValue(reportDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
               reference.child("Arkadaslar").child(otherId).child(userId).child("tarih").setValue(reportDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                      if (task.isSuccessful()){
                          Toast.makeText(context,"İstek Kabul Edildi",Toast.LENGTH_LONG).show();

                          reference.child("Arkadaslik_Istek").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                              @Override
                              public void onSuccess(Void aVoid) {
                                  reference.child("Arkadaslik_Istek").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                      @Override
                                      public void onSuccess(Void aVoid) {

                                          notifyDataSetChanged();

                                      }
                                  });

                              }
                          });

                      }
                   }
               });
            }
        });

    }

    void reddet(final String userId, final String otherId){

        reference.child("Arkadaslik_Istek").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child("Arkadaslik_Istek").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context,"İstek Red Edildi",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}
