package com.example.localdiskieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.MyViewHolder> {
    Context context;
    ArrayList<commentHelper> list;

    public commentAdapter(Context context, ArrayList<commentHelper> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.comment_section_listview,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        commentHelper comment=list.get(position);

        if(!comment.getPicuture().equals("NONE"))
        {
            Picasso.with(context)
                    .load(comment.getPicuture())
                    .placeholder(R.drawable.userprofile)
                    .into(holder.comment_profile);
        }

         holder.comment_username.setText(comment.getName());
         holder.userComment.setText(comment.getComment());


         holder.itemView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
//                 if(!comment.getUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
//                 {
//                     Toast.makeText(context,"DO you want to delete",Toast.LENGTH_LONG).show();
//                 }
             }
         });
    }

    @Override
    public int getItemCount() {
      return  list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        CircularImageView comment_profile;
        TextView comment_username;
        TextView userComment;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_profile=itemView.findViewById(R.id.comment_profile);
            comment_username=itemView.findViewById(R.id.comment_username);
            userComment=itemView.findViewById(R.id.userComment);
        }
    }
}
