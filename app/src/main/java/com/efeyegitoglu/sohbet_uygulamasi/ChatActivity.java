package com.efeyegitoglu.sohbet_uygulamasi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    TextView chat_user_name_text;
    FirebaseUser user;
    DatabaseReference reference;
    ImageView send_message_button,turn_back_button;
    EditText message_edit_text;

    List<MessageModel> messageModelList;
    RecyclerView chat_recyclerView;
    MessageAdapter messageAdapter;
    List<String> userKeyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tanimla();
        action();
        loadMessage();

    }

    void tanimla(){

        gelenIsmiAl();
        chat_user_name_text=findViewById(R.id.chat_user_name_text);
        chat_user_name_text.setText(gelenIsmiAl());

        reference=FirebaseDatabase.getInstance().getReference();
        user= FirebaseAuth.getInstance().getCurrentUser();

        send_message_button=findViewById(R.id.send_message_button);
        message_edit_text=findViewById(R.id.message_edit_text);

        userKeyList=new ArrayList<>();
        messageModelList=new ArrayList<>();
        chat_recyclerView=findViewById(R.id.chat_recyclerView);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager(getApplicationContext(),1);
        chat_recyclerView.setLayoutManager(layoutManager);
        messageAdapter=new MessageAdapter(userKeyList,getApplicationContext(),ChatActivity.this,messageModelList);
        chat_recyclerView.setAdapter(messageAdapter);

        turn_back_button=findViewById(R.id.turn_back_button);
        turn_back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }

    String gelenIsmiAl(){

        //Bundle ile alım
        Bundle bundle=getIntent().getExtras();
        String userName=bundle.getString("userName");
        return userName;
    }

    String getId(){

        //Intent alımı
        String otherId=getIntent().getExtras().getString("otherId");
        return otherId;
    }

    void sendMessage(final String userId, final String otherId, String textType, String date, Boolean seen, String messageText){

        final String messageId=reference.child("Mesajlar").child(userId).child(otherId).push().getKey();
        final Map messageMap=new HashMap();
        messageMap.put("type",textType);
        messageMap.put("seen",seen);
        messageMap.put("time",date);
        messageMap.put("text",messageText);
        messageMap.put("from",userId);

        reference.child("Mesajlar").child(userId).child(otherId).child(messageId).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                reference.child("Mesajlar").child(otherId).child(userId).child(messageId).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });


            }
        });

    }

    String getDate(){
        DateFormat df=new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date today= Calendar.getInstance().getTime();

        final  String reportDate=df.format(today);
        return reportDate;
    }

    void action(){

        send_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message=message_edit_text.getText().toString();
                if (!message.equals("")) {
                    sendMessage(user.getUid(),getId(),"text",getDate(),false,message);
                    message_edit_text.setText("");
                }else {
                    Toast.makeText(getApplicationContext(),"Mesaj Giriniz",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    void loadMessage(){
        reference.child("Mesajlar").child(user.getUid()).child(getId()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                MessageModel messageModel=dataSnapshot.getValue(MessageModel.class);
                messageModelList.add(messageModel);
                messageAdapter.notifyDataSetChanged();
                userKeyList.add(dataSnapshot.getKey());
                chat_recyclerView.scrollToPosition(messageModelList.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

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
