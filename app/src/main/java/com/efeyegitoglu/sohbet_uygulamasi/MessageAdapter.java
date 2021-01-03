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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<String> userKeyList;
    Context context;
    Activity activity;

    FirebaseDatabase database;
    DatabaseReference reference;

    FirebaseAuth auth;
    FirebaseUser user;
    String userId;

    List<MessageModel> messageModelList;
    Boolean state;
    int viewType_send=1,viewType_recieved=2;

    public MessageAdapter(List<String> userKeyList, Context context, Activity activity, List<MessageModel> messageModelList) {
        this.userKeyList = userKeyList;
        this.context = context;
        this.activity = activity;
        this.messageModelList = messageModelList;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userId = user.getUid();
        state = false;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType==viewType_send){

            view = LayoutInflater.from(context).inflate(R.layout.send_layout, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.recieved_layout, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.messageText.setText(messageModelList.get(position).getText());




    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageText;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            if (state==true){
                messageText=itemView.findViewById(R.id.sendText);

            }else {
                messageText =itemView.findViewById(R.id.receivedText);
            }
        }


    }

    @Override
    public int getItemViewType(int position) {
        if (messageModelList.get(position).getFrom().equals(userId)) {

            state = true;
            return viewType_send;
        } else {
            state = false;
            return viewType_recieved;
        }

    }
}
