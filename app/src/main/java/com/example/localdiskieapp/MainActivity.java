package com.example.localdiskieapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loaduser();
    }
    private void loaduser()
    {
        FirebaseAuth mfire=FirebaseAuth.getInstance();
        FirebaseUser mUser=mfire.getCurrentUser();
        if(mUser!=null)
        {
            Intent intent=new Intent(this,localDiskieHome.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent=new Intent(this,Register.class);
            startActivity(intent);
            finish();
        }
    }
}