package com.example.localdiskieapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link alert#newInstance} factory method to
 * create an instance of this fragment.
 */
public class alert extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int num=0;

    private String myteam;
    int i=0;
    //request things
    RecyclerView recyclerView,recyclerView2,recyclerView3;
    DatabaseReference database;
    RequestsAdapter myAdapter;
    ArrayList<sendRequestHelper> list;
    //notifications
    DatabaseReference database2;
    notificationsAdapter myAdapter2;
    ArrayList<sendNotificationHelper>list2;
    private String UID;


    public alert() {
        // Required empty public constructor
    }
    public alert(String teamname) {
        this.myteam=teamname;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment alert.
     */
    // TODO: Rename and change types and number of parameters
    public static alert newInstance(String param1, String param2) {
        alert fragment = new alert();
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
        View view= inflater.inflate(R.layout.fragment_alert, container, false);
        /*
        in alert we see alert of both users hosted tournament and joined tournament
        the notification can be deleted by long holding or after tthe end of the tournament

        user can press accept to allow a team to join or click on the team to find out more abt the
        team and the button will be there too


           alerts to join team
         */
        UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView=view.findViewById(R.id.alertlistview);
        recyclerView2=view.findViewById(R.id.alertlistview2);
        // request to join tournament
        database= FirebaseDatabase.getInstance().getReference(myteam.trim()+"request");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();
        myAdapter=new RequestsAdapter(getContext(),list);
        recyclerView.setAdapter(myAdapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    sendRequestHelper req=dataSnapshot.getValue(sendRequestHelper.class);
                    //String uids=add.getUid();
                    list.add(req);


                }
                Collections.reverse(list);

                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //notification
        database2= FirebaseDatabase.getInstance().getReference(myteam.trim()+"notifications");
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        list2=new ArrayList<>();
        myAdapter2=new notificationsAdapter(getContext(),list2);
        recyclerView2.setAdapter(myAdapter2);
        database2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list2.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    sendNotificationHelper receive=dataSnapshot.getValue(sendNotificationHelper.class);
                    //String uids=add.getUid();
                    list2.add(receive);


                }
                Collections.reverse(list2);

                myAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
    }
}