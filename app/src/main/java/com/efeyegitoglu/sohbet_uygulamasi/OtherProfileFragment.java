package com.efeyegitoglu.sohbet_uygulamasi;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import de.hdodenhof.circleimageview.CircleImageView;


public class OtherProfileFragment extends Fragment {


    View view;
    String otherId, userId, kontrol = "", begeniKontrol = "";
    TextView adText, dogumText, egitimText, hakkimdaText, arkadasText, begeniText;
    ImageView arkadasImage, mesajImage, begenImage;

    FirebaseDatabase database;
    DatabaseReference reference, reference_arkadaslık;
    FirebaseAuth auth;
    FirebaseUser user;

    CircleImageView ProfileImage;
    String userName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        tanimla();
        action();

        getBegeniText();
        getArkadasText();


        return view;
    }

    void tanimla() {

        otherId = getArguments().getString("userId");

        adText = view.findViewById(R.id.adText);
        dogumText = view.findViewById(R.id.dogumText);
        egitimText = view.findViewById(R.id.egitimText);
        hakkimdaText = view.findViewById(R.id.hakkimdaText);
        begeniText = view.findViewById(R.id.begeniText);
        arkadasText = view.findViewById(R.id.arkadasText);
        arkadasImage = view.findViewById(R.id.arkadasImage);
        begenImage = view.findViewById(R.id.begenImage);
        mesajImage = view.findViewById(R.id.mesajImage);
        ProfileImage = view.findViewById(R.id.ProfileImage);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        reference_arkadaslık = database.getReference().child("Arkadaslik_Istek");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();


        reference_arkadaslık.child(otherId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(userId)) {
                    kontrol = "istek";
                    arkadasImage.setImageResource(R.drawable.unf);
                } else {
                    arkadasImage.setImageResource(R.drawable.friend);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Arkadaslar").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(otherId)) {
                    kontrol = "arkadas";
                    arkadasImage.setImageResource(R.drawable.delete_user);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.child("Begeniler").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(otherId)) {
                    begeniKontrol = "begendi";
                    begenImage.setImageResource(R.drawable.red_heart);
                } else {
                    begenImage.setImageResource(R.drawable.white_heart);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    void action() {
        reference.child("Kullanicilar").child(otherId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KullaniciBilgiler kullaniciBilgiler = dataSnapshot.getValue(KullaniciBilgiler.class);
                userName = kullaniciBilgiler.getIsim();
                adText.setText("AD:  " + kullaniciBilgiler.getIsim());
                dogumText.setText("DOĞUM TARİHİ:  " + kullaniciBilgiler.getDogumtarihi());
                egitimText.setText("EĞİTİM:  " + kullaniciBilgiler.getEgitim());
                hakkimdaText.setText("HAKKIMDA:  " + kullaniciBilgiler.getHakkimda());
                if (!kullaniciBilgiler.getResim().equals("null")) {
                    Picasso.get().load(kullaniciBilgiler.getResim()).into(ProfileImage);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        arkadasImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kontrol.equals("istek")) {

                    arkadasIptalEt(otherId, userId);

                } else if (kontrol.equals("arkadas")) {

                    arkadasTablosundanCıkar(otherId, userId);

                } else {

                    arkadasEkle(otherId, userId);
                }

            }
        });

        begenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (begeniKontrol.equals("begendi")) {
                    begeniIptal(userId, otherId);
                } else {
                    begen(userId, otherId);
                }
            }
        });

        mesajImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("userName", userName);
                intent.putExtra("otherId", otherId);

                startActivity(intent);
            }
        });


    }

    void arkadasEkle(final String otherId, final String userId) {

        reference_arkadaslık.child(userId).child(otherId).child("tip").setValue("gonderdi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    reference_arkadaslık.child(otherId).child(userId).child("tip").setValue("aldi").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                kontrol = "istek";
                                Toast.makeText(getContext(), "İstek Gönderildi", Toast.LENGTH_LONG).show();
                                arkadasImage.setImageResource(R.drawable.unf);


                            } else {
                                Toast.makeText(getContext(), "Hata Meydana Geldi", Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Hata Meydana Geldi", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    void arkadasIptalEt(final String otherId, final String userId) {

        reference_arkadaslık.child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference_arkadaslık.child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol = "";
                        arkadasImage.setImageResource(R.drawable.friend);
                        Toast.makeText(getContext(), "İstek İptal Edildi", Toast.LENGTH_LONG).show();


                    }
                });
            }
        });

    }

    void arkadasTablosundanCıkar(final String otherId, final String userId) {
        reference.child("Arkadaslar").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                reference.child("Arkadaslar").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol = "";
                        arkadasImage.setImageResource(R.drawable.friend);
                        Toast.makeText(getContext(), "Arkadaşlıktan Çıkarıldı", Toast.LENGTH_LONG).show();
                        getArkadasText();

                    }
                });
            }
        });

    }

    void begen(String userId, String otherId) {

        reference.child("Begeniler").child(otherId).child(userId).child("tip").setValue("begendi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Profili begendiniz", Toast.LENGTH_LONG).show();
                    begenImage.setImageResource(R.drawable.red_heart);
                    begeniKontrol = "begendi";
                    getBegeniText();

                }

            }
        });

    }

    void begeniIptal(String userId, String otherId) {

        reference.child("Begeniler").child(otherId).child(userId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    begeniKontrol = "";
                    begenImage.setImageResource(R.drawable.white_heart);
                    Toast.makeText(getContext(), "Begeniyi İptal Ettiniz", Toast.LENGTH_LONG).show();
                    getBegeniText();

                }


            }
        });
    }

    void getBegeniText() {

        final List<String> begeniList = new ArrayList<>();

        reference.child("Begeniler").child(otherId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                begeniList.add(dataSnapshot.getKey());
                begeniText.setText(String.valueOf(begeniList.size()));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                begeniList.remove(dataSnapshot.getKey());
                begeniText.setText(String.valueOf(begeniList.size()));

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void getArkadasText() {

        final List<String> arkadasList = new ArrayList<>();

        reference.child("Arkadaslar").child(otherId).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                arkadasList.add(dataSnapshot.getKey());
                arkadasText.setText(String.valueOf(arkadasList.size()));

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                arkadasList.remove(dataSnapshot.getKey());
                arkadasText.setText(String.valueOf(arkadasList.size()));

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
