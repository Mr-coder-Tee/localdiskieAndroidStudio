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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyTournaments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyTournaments extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recyclerView;
    DatabaseReference database;
    FigtureAdapter myAdapter;
    ArrayList<addTournamentHelper> list;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyTournaments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyTournaments.
     */
    // TODO: Rename and change types and number of parameters
    public static MyTournaments newInstance(String param1, String param2) {
        MyTournaments fragment = new MyTournaments();
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
        View view= inflater.inflate(R.layout.fragment_my_tournaments, container, false);
        recyclerView=view.findViewById(R.id.mytournamelist);
        String UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        database= FirebaseDatabase.getInstance().getReference("tournaments");
        final Query query=database.orderByChild("uid").equalTo(UID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        list=new ArrayList<>();
        Collections.reverse(list);
        myAdapter=new FigtureAdapter(getContext(),list);
        recyclerView.setAdapter(myAdapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    addTournamentHelper req=dataSnapshot.getValue(addTournamentHelper.class);
                    list.add(req);
                }
                Collections.reverse(list);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        there will be list of user's created tournamets then when the user clicks and that tournament
        ficture has not been set it must take user to set ficture otherwise it must take user to update
        score
         */
        return view;
    }
}