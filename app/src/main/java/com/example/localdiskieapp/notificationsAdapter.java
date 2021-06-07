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

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class notificationsAdapter extends RecyclerView.Adapter<notificationsAdapter.MyViewHolder>{


    Context context;
    ArrayList<sendNotificationHelper> list;

    public notificationsAdapter(Context context, ArrayList<sendNotificationHelper> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.notifications_listview,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        sendNotificationHelper send=list.get(position);
        boolean isJoin=send.isIsjointeam();
        Picasso.with(context)
                .load(send.getLogo())
                .placeholder(R.drawable.userprofile)
                .into(holder.imgProfile);

        if(isJoin){
            String m=send.getFrom()+" requested to join your team";
            holder.message.setText(m);
        }else{
            holder.confirm.setVisibility(View.GONE);
            holder.delete.setVisibility(View.GONE);
            String message=send.getFrom()+" accepted you into "+send.getTournamentname();
            holder.message.setText(message);
        }

        holder.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"accept",Toast.LENGTH_SHORT).show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"delete",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView message;
        CircularImageView imgProfile;
        Button confirm,delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            confirm=(Button) itemView.findViewById(R.id.acceptRequest);
            delete=(Button) itemView.findViewById(R.id.denyRequest);
            message=(TextView)itemView.findViewById(R.id.message);
            imgProfile=(CircularImageView) itemView.findViewById(R.id.sender_profile);
        }
    }

}
