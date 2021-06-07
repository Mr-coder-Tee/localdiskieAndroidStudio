package com.example.localdiskieapp;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> implements PopupMenu.OnMenuItemClickListener {

    Context context;
    ArrayList<addTournamentHelper>list;
    //String name;
    homefragment home;
    int menuPosition;
    int numcomments=0;

    public HomeAdapter(Context context, ArrayList<addTournamentHelper> list, homefragment home) {
        this.context = context;
        this.list = list;
        this.home = home;
    }

    public HomeAdapter(Context context, ArrayList<addTournamentHelper> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.tournamet_listview,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            addTournamentHelper add=list.get(position);

                setNumcomments(add.getTournamentname());


                if(!add.getDp().equals("NONE"))
                {
                    Picasso.with(context)
                            .load(add.getDp())
                            .placeholder(R.drawable.userprofile)
                            .into(holder.profile);
                }

                String pictureuri=add.getPicture();
                if(!pictureuri.isEmpty()){
                    Picasso.with(context)
                            .load(add.getPicture())
                            .placeholder(R.drawable.localdiskielogoe)//
                            .into(holder.poster_pic);
                }//.centerCrop()
        holder.description.setText(add.getDescription());
        holder.postdate.setText(add.getPostdate());
        holder.username.setText(add.getName());

//        DatabaseReference database=FirebaseDatabase.getInstance().getReference(add.getTournamentname()+"comments");
//        Query query=database;
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    int c=(int)snapshot.getChildrenCount();
//                    String a=Integer.toString(c);
//                    holder.comment.setText(a+" comments");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


        holder.morevert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuPosition=position;
                popup(v);
                //Toast.makeText(context,"more",Toast.LENGTH_LONG).show();
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppCompatActivity activity=(AppCompatActivity)v.getContext();
//                HomeEnterTournamentFragement frag=new HomeEnterTournamentFragement(add);
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.homeframe,frag).addToBackStack(null).commit();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id=item.getItemId();
        addTournamentHelper menu=list.get(menuPosition);
        if(id==R.id.sendorcancel){
            Toast.makeText(context,"send"+menu.getTournamentname(),Toast.LENGTH_LONG).show();
            return true;
        }else if(id==R.id.share)
        {
            Toast.makeText(context,"shared",Toast.LENGTH_LONG).show();
            return true;
        }else if(id==R.id.interest)
        {
            Toast.makeText(context,"not interested",Toast.LENGTH_LONG).show();
            return true;
        }

        return false;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CircularImageView profile;
        TextView username,postdate,description,comment;
        ImageView poster_pic,morevert;
        CardView card;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            morevert=itemView.findViewById(R.id.morevert);
            card=itemView.findViewById(R.id.card);
            profile=itemView.findViewById(R.id.home_profile);
            username=itemView.findViewById(R.id.home_username);
            postdate=itemView.findViewById(R.id.homePostDate);
            description=itemView.findViewById(R.id.home_tournameDes);
            poster_pic=itemView.findViewById(R.id.home_tournamentPic);
            comment=itemView.findViewById(R.id.home_commentsection);

        }
    }
    private void popup(View view)
    {
        PopupMenu popupMenu=new PopupMenu(view.getContext(),view);
        popupMenu.inflate(R.menu.send_or_cancel_request_menu);

        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
    }
    private void setNumcomments(String name){
//        DatabaseReference database=FirebaseDatabase.getInstance().getReference(name+"comments");
//        Query query=database;
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    numcomments=(int)snapshot.getChildrenCount();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

}
