package com.example.localdiskieapp;

import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ScoreFragmentAdapter extends FragmentStateAdapter {
    public ScoreFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==1){
            return new lineup();
        }else if(position==2){
            return new stats();
        }else if(position==3){
            return new comments();
        }

        return new matches();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
