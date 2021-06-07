package com.example.localdiskieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.MyViewHolder> {


    private String logo;
    Context context;
    ArrayList<sendRequestHelper> list;
    String myTeam;

    public RequestsAdapter(Context context, ArrayList<sendRequestHelper> list) {
        this.context = context;
        this.list = list;
        setHasStableIds(true);
        getTeamName();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.request_listview,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        sendRequestHelper req=list.get(position);

        Picasso.with(context)
                .load(req.getLogo())
                .placeholder(R.drawable.userprofile)
                .into(holder.imgProfile);
        holder.txtName.setText(req.getTeamname());
        holder.txtTour.setText(req.getTournamentname());
        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLogo();
                confirm(req.getTeamname(),req.getUsername(),req.getLogo(),req.getTournamentname());
                Toast.makeText(context,"Done",Toast.LENGTH_SHORT).show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String teamname,String tourName
                delete(req.getTeamname(),req.getTournamentname());
                Toast.makeText(context,"deleted",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtName,txtTour;
        ImageView imgProfile;
        Button confirm,delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName=(TextView)itemView.findViewById(R.id.requester_username);
            txtTour=(TextView)itemView.findViewById(R.id.requester_tourname);
            imgProfile=(ImageView) itemView.findViewById(R.id.requester_profile);
            confirm=(Button) itemView.findViewById(R.id.confirmRequest);
            delete=(Button) itemView.findViewById(R.id.deleteRequest);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void confirm(String teamname,String name,String teamlogo,String tourName){
//        FirebaseDatabase rootNode;
//        DatabaseReference reference;
//        rootNode=FirebaseDatabase.getInstance();
//        reference=rootNode.getReference(tourName.trim()+"accepted");
//        sendRequestHelper send=new sendRequestHelper(teamname,name,teamlogo,tourName);
//        reference.child(teamname).setValue(send);
//        //remove from requests
//        sendAnswer(teamname,tourName);
//        delete(teamname,tourName);




//        DatabaseReference deleteRef=FirebaseDatabase.getInstance().getReference(myTeam.trim()+"request").child(teamname.trim()+tourName);
//        deleteRef.removeValue();
    }
    public void sendAnswer(String teamname,String tourName)
    {
        //String senderuid, String from, String logo, String tournamentname, boolean isread, boolean isjointeam

//        FirebaseDatabase rootNode;
//        DatabaseReference reference;
//        rootNode=FirebaseDatabase.getInstance();
//        reference=rootNode.getReference(teamname.trim()+"notifications");
//        sendNotificationHelper notif=new sendNotificationHelper( FirebaseAuth.getInstance().getCurrentUser().getUid(),myTeam,logo,tourName,false,false);
//        reference.child(System.currentTimeMillis()+"").setValue(notif);
    }
    public void delete(String teamname,String tourName){
//        DatabaseReference deleteRef=FirebaseDatabase.getInstance().getReference(myTeam.trim()+"request").child(teamname.trim()+tourName);
//        deleteRef.removeValue();
    }
    public void getTeamName()
    {
//        String  UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
//        final Query checkTeamName=reference.orderByChild("uid").equalTo(UID);
//        checkTeamName.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists())
//                {
//                    myTeam=snapshot.child(UID).child("teamname").getValue(String.class);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
    public void getMyLogo(){
//        String  UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
//        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("teams");
//        final Query checkTeamName=reference.orderByChild("uid").equalTo(UID);
//        checkTeamName.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists())
//                {
//                    logo=snapshot.child(myTeam).child("picture").getValue(String.class);
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
