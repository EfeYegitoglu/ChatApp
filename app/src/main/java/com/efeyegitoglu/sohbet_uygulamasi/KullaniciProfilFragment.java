package com.efeyegitoglu.sohbet_uygulamasi;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class KullaniciProfilFragment extends Fragment {


    View view;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference, reference2;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    StorageReference urlReference;

    EditText kullaniciIsmi, dogum_tarihi_input, egitim_input, hakkimda_input;
    Button bilgi_guncelle_button, bilgi_arkadas_button, bilgi_istek_button;
    CircleImageView profile_image;
    ChangeFragment changeFragment;

    Uri imageData;
    Bitmap selectedImage;
    String downloadUrl;


    TextView arkadasSayiText,BegeniSayiText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_kullanici_profil, container, false);

        tanimla();
        bilgileriGetir();

        arkadasSayisi();
        getBegeniSayisi();


        return view;
    }

    void tanimla() {

        arkadasSayiText=view.findViewById(R.id.arkadasSayiText);
        BegeniSayiText=view.findViewById(R.id.BegeniSayiText);


        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Kullanicilar").child(user.getUid());

        kullaniciIsmi = view.findViewById(R.id.kullaniciIsmi);
        dogum_tarihi_input = view.findViewById(R.id.dogum_tarihi_input);
        egitim_input = view.findViewById(R.id.egitim_input);
        hakkimda_input = view.findViewById(R.id.hakkimda_input);
        bilgi_guncelle_button = view.findViewById(R.id.bilgi_guncelle_button);
        profile_image = view.findViewById(R.id.profile_image);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        bilgi_arkadas_button = view.findViewById(R.id.bilgi_arkadas_button);

        bilgi_arkadas_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.change(new ArkadasFragment());
            }
        });

        bilgi_istek_button = view.findViewById(R.id.bilgi_istek_button);
        bilgi_istek_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeFragment changeFragment = new ChangeFragment(getContext());
                changeFragment.change(new BildirimFragment());
            }
        });


        reference2 = FirebaseDatabase.getInstance().getReference();


        bilgi_guncelle_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guncelle();


            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tikaProfilResmine();


            }


        });
    }


    void bilgileriGetir() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                KullaniciBilgiler kullaniciBilgiler = dataSnapshot.getValue(KullaniciBilgiler.class);

                kullaniciIsmi.setText(kullaniciBilgiler.getIsim());
                dogum_tarihi_input.setText(kullaniciBilgiler.getDogumtarihi());
                egitim_input.setText(kullaniciBilgiler.getEgitim());
                hakkimda_input.setText(kullaniciBilgiler.getHakkimda());
                if (!kullaniciBilgiler.getResim().equals("")) {

                    Picasso.get().load(kullaniciBilgiler.getResim()).into(profile_image);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void guncelle() {


        if (imageData != null) {


            final String mainChild = "images/" + user.getUid() + "/" + "image.jpg";

            storageReference.child(mainChild).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    urlReference = FirebaseStorage.getInstance().getReference(mainChild);
                    urlReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl = uri.toString();

                            String isim = kullaniciIsmi.getText().toString();
                            String dogum = dogum_tarihi_input.getText().toString();
                            String egitim = egitim_input.getText().toString();
                            String hakkimda = hakkimda_input.getText().toString();

                            database = FirebaseDatabase.getInstance();
                            reference = database.getReference().child("Kullanicilar").child(auth.getUid());


                            Map map = new HashMap();
                            map.put("isim", isim);
                            map.put("egitim", egitim);
                            map.put("dogumtarihi", dogum);
                            map.put("hakkimda", hakkimda);
                            map.put("resim", downloadUrl);


                            reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {


                                    /*changeFragment = new ChangeFragment(getContext());
                                    changeFragment.change(new KullaniciProfilFragment());*/
                                        Toast.makeText(getContext(), "Bilgiler başarıyla güncellendi", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getContext(), "Bir sorun meydana geldi", Toast.LENGTH_LONG).show();
                                    }


                                }
                            });

                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    changeFragment = new ChangeFragment(getContext());
                    changeFragment.change(new KullaniciProfilFragment());
                    Toast.makeText(getContext(), "Bir sorun meydana geldi", Toast.LENGTH_LONG).show();


                }
            });
        } else {
            Toast.makeText(getContext(), "resim seçmediniz", Toast.LENGTH_LONG).show();
        }
    }


    void tikaProfilResmine() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentGallery, 2);


        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGallery, 2);

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageData = data.getData();

            try {

                if (Build.VERSION.SDK_INT >= 28) {
                    ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    profile_image.setImageBitmap(selectedImage);
                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageData);
                    profile_image.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }



     /*void galeriAc() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 5);
    }*/

     /*if (imageData != null) {

            uuid=UUID.randomUUID();
             imageName="images/"+user.getUid()+"/"+uuid+".jpeg";

            storageReference.child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Download url
                    //storageReference=FirebaseStorage.getInstance().getReference(imageName);
                   storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            downloadUrl=uri.toString();

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }


        String isim = kullaniciIsmi.getText().toString();
        String dogum = dogum_tarihi_input.getText().toString();
        String egitim = egitim_input.getText().toString();
        String hakkimda = hakkimda_input.getText().toString();




        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Kullanicilar").child(auth.getUid());



        Map map = new HashMap();
        map.put("isim", isim);
        map.put("egitim", egitim);
        map.put("dogumtarihi", dogum);
        map.put("hakkimda", hakkimda);





        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    changeFragment = new ChangeFragment(getContext());
                    changeFragment.change(new KullaniciProfilFragment());
                    Toast.makeText(getContext(), "Bilgiler başarıyla güncellendi", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Bir sorun meydana geldi", Toast.LENGTH_LONG).show();
                }


            }
        });



       */

     void arkadasSayisi(){
         final List<String> arkadasList = new ArrayList<>();

         reference2.child("Arkadaslar").child(user.getUid()).addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                 arkadasList.add(dataSnapshot.getKey());
                 arkadasSayiText.setText(String.valueOf(arkadasList.size()));

             }

             @Override
             public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


             }

             @Override
             public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                 arkadasList.remove(dataSnapshot.getKey());
                 arkadasSayiText.setText(String.valueOf(arkadasList.size()));

             }

             @Override
             public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

     }


     void getBegeniSayisi(){

         final List<String> begeniList = new ArrayList<>();

         reference2.child("Begeniler").child(user.getUid()).addChildEventListener(new ChildEventListener() {
             @Override
             public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                 begeniList.add(dataSnapshot.getKey());
                 BegeniSayiText.setText(String.valueOf(begeniList.size()));

             }

             @Override
             public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


             }

             @Override
             public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                 begeniList.remove(dataSnapshot.getKey());
                 BegeniSayiText.setText(String.valueOf(begeniList.size()));

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
