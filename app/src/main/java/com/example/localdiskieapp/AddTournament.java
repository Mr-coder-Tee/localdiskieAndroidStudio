package com.example.localdiskieapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTournament#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTournament extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri image;
    private StorageTask mUploadTask;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String top, UID;
    private int tournSize;
    private Spinner size;
    private ProgressBar progressline;
    private ImageView selectposter;
    private StorageReference mStorageRef;
    private EditText name, description;
    //private DatabaseReference reference;
    private String imageuri,dp,teamsname;
    LoadingBar bar;
    String names;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddTournament() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddTournament.
     */
    // TODO: Rename and change types and number of parameters
    public static AddTournament newInstance(String param1, String param2) {
        AddTournament fragment = new AddTournament();
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
        View view = inflater.inflate(R.layout.fragment_add_tournament, container, false);
        progressline = view.findViewById(R.id.progressline);
        selectposter = view.findViewById(R.id.selectposter);
        bar = new LoadingBar(getActivity());
        UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getTeamName();
        name = view.findViewById(R.id.addTourName);
        description = view.findViewById(R.id.addDescription);
        Button pick = view.findViewById(R.id.addPick);
        Button post = view.findViewById(R.id.addPost);
        size = (Spinner) view.findViewById(R.id.spinner_tournamentsize);
        getDP();
        mStorageRef = FirebaseStorage.getInstance().getReference("posters");


        ArrayList<String> list = new ArrayList<>();
        list.add("---Select tournament size---");
        list.add("Top 4");
        list.add("Top 8");
        list.add("Top 16");
        list.add("Top 32");
        int[] saver = {0, 4, 8, 16, 32};

        size.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, list));
        size.setSelection(0);
        size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    top = list.get(position);
                    // Toast.makeText(getActivity(), Integer.toString(position), Toast.LENGTH_LONG).show();
                    tournSize = saver[position];
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bar.startLoading();
                if (image != null) {
                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        Toast.makeText(getActivity(), "Upload in progress", Toast.LENGTH_LONG).show();
                    } else {
                        uploadfile();
                    }
                }else{
                    createTournament(name.getText().toString().trim(), description.getText().toString(), tournSize, false, "");
                }

            }
        });
        selectposter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image=null;
                selectposter.setImageDrawable(null);
            }
        });
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallery = new Intent();
                openGallery.setType("image/");
                openGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(openGallery, PICK_IMAGE_REQUEST);
            }
        });
        return view;
    }

    public void createTournament(String tournamentname, String des, int size, boolean figture, String pic) {
        FirebaseDatabase rootNode;
        DatabaseReference reference;
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("tournaments");
        //
        FirebaseDatabase rootNode1;
        DatabaseReference reference1;
        rootNode1 = FirebaseDatabase.getInstance();
        reference1 = rootNode1.getReference("tournaments" + UID);
        //
        addTournamentHelper helper = new addTournamentHelper(tournamentname, UID, des, pic, new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Calendar.getInstance().getTime()), size, figture, names,dp,teamsname);
        reference.child(tournamentname).setValue(helper);
        reference1.child(tournamentname).setValue(helper);
//        bar.stopLoading();
//        done.startLoading();
        load();


    }

    public void getDP()
    {
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        final Query checkTeamName=reference.orderByChild("uid").equalTo(UID);
        checkTeamName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    dp=snapshot.child(UID).child("picture").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void uploadfile() {
        final String picture_name = System.currentTimeMillis() + "." + getFileExtension(image);
        StorageReference fileref = mStorageRef.child(picture_name);
        mUploadTask=fileref.putFile(image)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressline.setProgress(0);

                            }
                        }, 500);
                        progressline.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "upload successful", Toast.LENGTH_SHORT).show();

                        Task<Uri> result=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                imageuri=uri.toString();
                                createTournament(name.getText().toString().trim(), description.getText().toString(), tournSize, false, imageuri);

                            }
                        });
                        //imageuri = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        progressline.setVisibility(View.VISIBLE);
                        double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressline.setProgress((int) progress);
                    }
                });
    }

    public void load() {
        AddTournament home = new AddTournament();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.homeframe, home);
        transaction.commit();
        // done.stopLoading();
    }

    public void getTeamName() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        final Query checkTeamName = reference.orderByChild("uid").equalTo(UID);
        checkTeamName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    names = snapshot.child(UID).child("name").getValue(String.class);
                    teamsname= snapshot.child(UID).child("teamname").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null) {
            image = data.getData();
            Picasso.with(getContext()).load(image).into(selectposter);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}