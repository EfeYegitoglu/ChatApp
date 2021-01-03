package com.efeyegitoglu.sohbet_uygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth auth;
    FirebaseUser user;
    Button anasayfa_button,bildirim_button,profil_button;
    ChangeFragment changeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tanimla();
        control();
        buttonClick();

        changeFragment=new ChangeFragment(MainActivity.this);
        changeFragment.change(new AnaSayfaFragment());
    }

    void tanimla() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        anasayfa_button=findViewById(R.id.anasayfa_button);
        bildirim_button=findViewById(R.id.bildirim_button);
        profil_button=findViewById(R.id.profil_button);
    }

    void control(){
        if (user==null){
            Intent intent=new Intent(getApplicationContext(),GirisActivity.class);
            startActivity(intent);
            finish();
        }
    }

    void buttonClick(){
        anasayfa_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment.change(new AnaSayfaFragment());
            }
        });

        profil_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment.change(new KullaniciProfilFragment());
            }
        });

        bildirim_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFragment.change(new BildirimFragment());

            }
        });

    }


























    //ÇIKIŞ İŞLEMLERİ

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.sign_out_menu) {
            auth.signOut();
            Intent intent = new Intent(getApplicationContext(), GirisActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
