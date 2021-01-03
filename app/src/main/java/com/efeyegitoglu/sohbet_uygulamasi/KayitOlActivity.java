package com.efeyegitoglu.sohbet_uygulamasi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class KayitOlActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    EditText input_email, input_password;
    Button registerButton;
    FirebaseAuth auth;
    TextView girisYapText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);
        tanimla();

    }

    void tanimla() {

        auth = FirebaseAuth.getInstance();
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit_mail = input_email.getText().toString();
                String edit_pass = input_password.getText().toString();
                if (!edit_mail.equals("") && !edit_pass.equals("")) {
                    kayitOl(edit_mail, edit_pass);
                } else {
                    Toast.makeText(getApplicationContext(), "Bilgileri Kontrol Ediniz", Toast.LENGTH_LONG).show();
                }
            }
        });
        girisYapText = findViewById(R.id.girisYapText);
        girisYapText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GirisActivity.class);
                startActivity(intent);
            }
        });

    }

    void kayitOl(String email, String pass) {
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    database=FirebaseDatabase.getInstance();
                    reference = database.getReference().child("Kullanicilar").child(auth.getUid());
                    Map map = new HashMap();
                    map.put("resim", "null");
                    map.put("isim", "null");
                    map.put("egitim", "null");
                    map.put("dogumtarihi", "null");
                    map.put("hakkimda", "null");
                    reference.setValue(map);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Bir Sorun Var", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
