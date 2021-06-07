package com.example.localdiskieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularimageview.CircularImageView;

public class localDiskieHome extends AppCompatActivity {

    int num=0;
    Live live;
    private EnteredTournament enteredTournament;
    private  alert alert;
    private homefragment home;
    private AddTournament add;
    private MyTournaments tour;
    private String myteam;

    private BottomNavigationView nav;
    private FloatingActionButton floatbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_diskie_home);
        //
        CircularImageView profile=(CircularImageView)findViewById(R.id.userprofile1);
        nav= (BottomNavigationView)findViewById(R.id.bottom_nav);
        floatbtn=(FloatingActionButton)findViewById(R.id.floating);
        //
        home=new homefragment();
        setFrag(home);
        //
        floatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add=new AddTournament();
                setFrag(add);
                floatbtn.setVisibility(View.GONE);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),profile.class);
                startActivity(intent);
            }
        });


        nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.home){
                    floatbtn.setVisibility(View.VISIBLE);
                    setFrag(home);
                    return true;
                }else if(id==R.id.createTour){

                    floatbtn.setVisibility(View.VISIBLE);
                    tour=new MyTournaments();
                    setFrag(tour);
                    return true;
                }else if(id==R.id.alert){
                    floatbtn.setVisibility(View.VISIBLE);
                    alert=new alert(myteam);
                    setFrag(alert);
                    return true;
                }else if(id==R.id.myTour){
                    floatbtn.setVisibility(View.VISIBLE);
                    enteredTournament=new EnteredTournament();
                    setFrag(enteredTournament);
                    return true;
                }else if(id==R.id.live){
                    floatbtn.setVisibility(View.VISIBLE);
                    live=new Live();
                    setFrag(live);
                    return true;
                }
                floatbtn.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }
    private void setFrag(Fragment fragment)
    {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.homeframe,fragment);
        fragmentTransaction.commit();
    }
    public void showFloating()
    {
        floatbtn.setVisibility(View.VISIBLE);
    }
    public void hideFloating()
    {
        floatbtn.setVisibility(View.GONE);
    }

/*
    private void getteamname()
    {

        String UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        final Query checkTeamName=reference.orderByChild("uid").equalTo(UID);
        checkTeamName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    myteam=snapshot.child(UID).child("teamname").getValue(String.class);
                    //Toast.makeText(localDiskieHome.this,myteam,Toast.LENGTH_SHORT).show();
                    getnumbers();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getnumbers()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference(myteam.trim()+"notifications");
        final Query checkTeamName=reference;
        checkTeamName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    num=num+(int)snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reference2= FirebaseDatabase.getInstance().getReference(myteam.trim()+"request");
        final Query checkTeamName2=reference2;
        checkTeamName2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    num=num+(int)snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        draw();
        Toast.makeText(localDiskieHome.this,Integer.toString(num),Toast.LENGTH_SHORT).show();
    }
    private void draw()
    {
        if(num>0) {
            BadgeDrawable badgeDrawable = nav.getOrCreateBadge(R.id.alert);
            badgeDrawable.setNumber(num);
            badgeDrawable.setVisible(true);
        }
    }

 */
}