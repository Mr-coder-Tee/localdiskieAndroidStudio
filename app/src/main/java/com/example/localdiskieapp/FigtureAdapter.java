package com.example.localdiskieapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;

public class FigtureAdapter extends RecyclerView.Adapter<FigtureAdapter.MyViewHolder>{
    Context context;
    ArrayList<addTournamentHelper> list;
    boolean FigtureIsSet;

    public FigtureAdapter(Context context, ArrayList<addTournamentHelper> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.figure_listview,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        addTournamentHelper req=list.get(position);

        holder.tour.setText(req.getTournamentname());
        holder.date.setText(req.getPostdate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSet(req.getTournamentname());
                if(!FigtureIsSet){

                    Intent intent=new Intent(context, SetFicture.class);
                    context.startActivity(intent);
                }else{
                    Intent intent=new Intent(context, UpdateScore.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tour,date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tour=itemView.findViewById(R.id.figture_touramentname);
            date=itemView.findViewById(R.id.figture_date);
        }
    }
    private void isSet(String name){

//        String UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("tournaments");
//        final Query checkTeamName=reference.orderByChild("uid").equalTo(UID);
//        checkTeamName.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists())
//                {
//                    FigtureIsSet=snapshot.child(name).child("figtureset").getValue(Boolean.class);
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

}
