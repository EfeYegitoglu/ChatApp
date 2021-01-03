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
import com.google.firebase.auth.FirebaseUser;

public class GirisActivity extends AppCompatActivity {

    FirebaseUser user;
    FirebaseAuth auth;
    EditText sign_email,sign_password;
    Button signButton;
    TextView kayitOlText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        tanimla();
    }
    void tanimla(){
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        sign_email=findViewById(R.id.sign_email);
        sign_password=findViewById(R.id.sign_password);
        signButton=findViewById(R.id.signButton);
        signButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=sign_email.getText().toString();
                String pass=sign_password.getText().toString();
                if (!mail.equals("")&&!pass.equals("")){
                    sign_in(mail,pass);
                }else {
                    Toast.makeText(getApplicationContext(),"Bilgileri Kontrol Ediniz",Toast.LENGTH_LONG).show();
                }

            }
        });
        kayitOlText=findViewById(R.id.kayitOlText);
        kayitOlText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),KayitOlActivity.class);
                startActivity(intent);
            }
        });


    }

    void sign_in(final String mail, final String pass){

        auth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(),"Bilgileri Kontrol Ediniz",Toast.LENGTH_LONG).show();
                        }

                    }
        });



    }
}
