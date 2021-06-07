package com.example.localdiskieapp;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserDatails {
   private String UID,name,teamname,picture,teampicture,location;

    public UserDatails() {
        users();
        UID=FirebaseAuth.getInstance().getCurrentUser().getUid();


    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeamname(String teamname) {
        this.teamname = teamname;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setTeampicture(String teampicture) {
        this.teampicture = teampicture;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUID() {
        return UID;
    }

    public String getName() {
        return name;
    }

    public String getTeamname() {
        return teamname;
    }

    public String getPicture() {
        return picture;
    }

    public String getTeampicture() {
        return teampicture;
    }

    public String getLocation() {
        return location;
    }
    public void users()
    {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        final Query checkTeamName=reference.orderByChild("uid").equalTo(UID);
        checkTeamName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {

                    setName(snapshot.child(UID).child("name").getValue(String.class));
                    setTeamname(snapshot.child(UID).child("teamname").getValue(String.class));
                    setPicture(snapshot.child(UID).child("picture").getValue(String.class));
                    teamDetails();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void teamDetails()
    {

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("teams");
        final Query checkTeamName=reference.orderByChild("uid").equalTo(UID);
        checkTeamName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    teampicture=snapshot.child(teamname).child("picture").getValue(String.class);
                    location=snapshot.child(teamname).child("location").getValue(String.class);//location
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
