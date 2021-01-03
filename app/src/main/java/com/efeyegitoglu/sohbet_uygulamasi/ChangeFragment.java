package com.efeyegitoglu.sohbet_uygulamasi;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

public class ChangeFragment {
    private Context context;

    public ChangeFragment(Context context) {
        this.context = context;

    }

    public void change(Fragment fragment) {

        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivityFragment,fragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_ENTER_MASK)
                .commit();

    }


    public void changeWithParameter(Fragment fragment,String userId){

        Bundle bundle=new Bundle();
        bundle.putString("userId",userId);
        fragment.setArguments(bundle);


        ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainActivityFragment,fragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_ENTER_MASK)
                .commit();

    }

}
