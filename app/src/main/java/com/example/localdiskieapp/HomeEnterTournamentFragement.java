package com.example.localdiskieapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeEnterTournamentFragement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeEnterTournamentFragement extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView username,date,des;
    private CircularImageView profile;
    private Button req;
    private ImageView poster,moreVert,sendbtn;
    private String UID,teamname,name,teamlogo="NONE";
    private EditText comment;
    LoadingBar bar;
    addTournamentHelper add;

    //
    RecyclerView recyclerView;
    DatabaseReference database;
    commentAdapter myAdapter;
    ArrayList<commentHelper> list;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    public HomeEnterTournamentFragement() {
        // Required empty public constructor
    }
    public HomeEnterTournamentFragement( addTournamentHelper add) {
        this.add=add;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeEnterTournamentFragement.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeEnterTournamentFragement newInstance(String param1, String param2) {
        HomeEnterTournamentFragement fragment = new HomeEnterTournamentFragement();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home_enter_tournament_fragement, container, false);
        profile=(CircularImageView)view.findViewById(R.id.hosterProfile);
        poster=(ImageView)view.findViewById(R.id.enter_photo);
        sendbtn=(ImageView)view.findViewById(R.id.sendbtn);
        moreVert=(ImageView)view.findViewById(R.id.moreRequest);
        recyclerView=view.findViewById(R.id.commentsection);
        comment=(EditText)view.findViewById(R.id.comment);
        username=(TextView)view.findViewById(R.id.hosterName);
        date=(TextView)view.findViewById(R.id.hosterPostdate);
        des=(TextView)view.findViewById(R.id.hosterdesc);
        req=(Button)view.findViewById(R.id.btnReqeust);

        UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        bar=new LoadingBar(getActivity());

        getTeamName();
        getUser();
        getMyRequest();
        geTteamLogo();
        // loadComments();
        if(!add.getPicture().isEmpty())
        {
            Picasso.with(getActivity())
                    .load(add.getPicture())
                    .placeholder(R.drawable.localdiskielogoe)
                    .into(poster);
        }
        if(!add.getDp().equals("NONE"))
        {
            Picasso.with(getActivity())
                    .load(add.getDp())
                    .placeholder(R.drawable.userprofile)
                    .into(profile);
        }
        username.setText(add.getName());
        date.setText(add.getPostdate());
        des.setText(add.getDescription());


        ((localDiskieHome)getActivity()).hideFloating();//hiding floating button
        database= FirebaseDatabase.getInstance().getReference(add.getTournamentname().trim()+"comments");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();
        myAdapter=new commentAdapter(getContext(),list);
        recyclerView.setAdapter(myAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    commentHelper comment=dataSnapshot.getValue(commentHelper.class);
                    list.add(comment);
                }
                Collections.reverse(list);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String button=req.getText().toString();
                if(button.equals("Request to Enter")) {
                    bar.startLoading();
                    if (teamname.equals("")) {
                        bar.stopLoading();
                        Toast.makeText(getActivity(), "You are not part of a team", Toast.LENGTH_LONG).show();

                    } else {
                        sendRequest();
                        myRequests();
                    }
                }else{
                    Toast.makeText(getActivity(), "Are you sure you want to delete request", Toast.LENGTH_LONG).show();
                }
            }
        });

        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                sendComment(comment.getText().toString());
                sendbtn.requestFocus();
                sendbtn.setFocusableInTouchMode(true);
                comment.setText("");
            }
        });
        moreVert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "menu", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }
    private void myRequests(){
        FirebaseDatabase rootNode;
        DatabaseReference reference;
        rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference("myrequests"+UID);
        reference.child(add.getTournamentname());
    }

    public void sendComment(String commenttext)
    {

        FirebaseDatabase rootNode;
        DatabaseReference reference;
        rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference(add.getTournamentname().trim()+"comments");
        commentHelper comment=new commentHelper(commenttext,name,UID,new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()),add.getDp());

        //

        // String root=Integer.toString(pre.nextInt(Integer.MAX_VALUE/2))+name+Integer.toString(post.nextInt(Integer.MAX_VALUE/2));
        reference.child(System.currentTimeMillis()+"").setValue(comment);
    }
    private void sendRequest(){
        FirebaseDatabase rootNode;
        DatabaseReference reference;
        rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference(add.getTeamname().trim()+"request");
        sendRequestHelper send=new sendRequestHelper(teamname,name,teamlogo,add.getTournamentname());
        reference.child(teamname+add.getTournamentname()).setValue(send);
        bar.stopLoading();
        req.setText("Cancel Request");
        Toast.makeText(getActivity(), "Request send!", Toast.LENGTH_LONG).show();
    }
    public void getTeamName()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        final Query checkTeamName=reference.orderByChild("uid").equalTo(UID);
        checkTeamName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String tn=snapshot.child(UID).child("teamname").getValue(String.class);
                    if(!tn.equals("NONE"))
                    {
                        teamname=tn;
                    }
                    else
                        teamname="";

                    name=snapshot.child(UID).child("name").getValue(String.class);
                    //Toast.makeText(getActivity(),teamname,Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void geTteamLogo()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("teams");
        final Query checkTeamName=reference.orderByChild("uid").equalTo(UID);
        checkTeamName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    teamlogo=snapshot.child(teamname).child("picture").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void getMyRequest()
    {

        bar.startLoading();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference(add.getTeamname().trim()+"request");
        //Toast.makeText(getActivity(),teamname,Toast.LENGTH_LONG).show();
        final Query checkTeamName=reference.orderByChild("teamname").equalTo(teamname);//.equalTo(teamname);
        checkTeamName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String tour=snapshot.child(teamname).child("teamname").getValue(String.class);
                    req.setText("Cancel Request");
                    // Toast.makeText(getActivity(),tour,Toast.)
                    if(teamname.equals(tour))
                    {

                        //bar.stopLoading();
                    }

                }
                bar.stopLoading();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getUser()
    {
        if(UID.equals(add.getUid()))
            req.setVisibility(View.GONE);
    }
}