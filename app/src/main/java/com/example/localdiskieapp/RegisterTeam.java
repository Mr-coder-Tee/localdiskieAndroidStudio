package com.example.localdiskieapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterTeam#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterTeam extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //
    private  static  final int PICK_IMAGE_REQUEST_TEAMLOGO=1;
    private EditText teamname;
    private  EditText location;
    private CircularImageView teamlogo;
    private Uri imageTeamLOGO;
    private StorageTask mUploadTask;
    private StorageReference mStorageRef;
    private String logouri="";
    LoadingBar bar;
    String UID;
    LinearLayout teamnameLayout;
    //
    UserDatails userDatails;

    public RegisterTeam() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterTeam.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterTeam newInstance(String param1, String param2) {
        RegisterTeam fragment = new RegisterTeam();
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
        View view= inflater.inflate(R.layout.fragment_register_team, container, false);
        teamlogo=(CircularImageView)view.findViewById(R.id.teamlogo);
        UID= FirebaseAuth.getInstance().getCurrentUser().getUid();
        bar=new LoadingBar(getActivity());
        Button cancel=(Button)view.findViewById(R.id.btnteamCancel);
        teamnameLayout=(LinearLayout)view.findViewById(R.id.teamnameLayout);
        Button submit=(Button)view.findViewById(R.id.btnteamSubmit);
        teamname=(EditText)view.findViewById(R.id.registerteamname);
        location=(EditText)view.findViewById(R.id.location);
        mStorageRef = FirebaseStorage.getInstance().getReference("teamlogo");
        userDatails=new UserDatails();

        teamlogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallary();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bar.startLoading();
                exists(teamname.getText().toString().trim());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load();
            }
        });
        getTeamName();
        return view;
    }
    public void teamsReg()
    {
        FirebaseDatabase rootNode;
        DatabaseReference reference;
        rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference("teams");
        String name=teamname.getText().toString().trim();
        String loc=location.getText().toString();
        CreateTeam team=new CreateTeam(name,UID,logouri,loc);
        reference.child(name).setValue(team);
        makeAdmin(name,"Adim",UID);
        update(name);
        teamnameLayout.setVisibility(View.GONE);
    }
    public void makeAdmin(String name,String type,String uid)
    {
        FirebaseDatabase rootNode;
        DatabaseReference reference;
        rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference(name);
        TeamMemberType membertype=new TeamMemberType(type,uid);
        reference.child(uid).setValue(membertype);
        Intent intent=new Intent(getActivity(),profile.class);
        startActivity(intent);
    }
    /*
    public void load(){
        MyPosts posts=new MyPosts();
        FragmentTransaction transaction=getFragmentManager().beginTransaction();
        transaction.replace(R.id.myPostsAndRegistration,posts);
        transaction.commit();
    }*/
    public void exists(String name){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("teams");
        final Query checkTeamName=reference.orderByChild("teamname").equalTo(name);
        checkTeamName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    upload();
                }else{
                    bar.stopLoading();
                    teamname.setError("Team already exit");

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void upload(){
        final String picture_name = System.currentTimeMillis() + "." + getFileExtension(imageTeamLOGO);
        StorageReference fileref = mStorageRef.child(picture_name);
        mUploadTask=fileref.putFile(imageTeamLOGO)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getActivity(),"Upload successfull",Toast.LENGTH_SHORT).show();
                        Task<Uri> result=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                logouri=uri.toString();
                                teamsReg();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        Toast.makeText(getActivity(),"please wait...",Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void update(String name){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference();
        ref.child("users").child(UID).child("teamname").setValue(name);
        bar.stopLoading();
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
                        teamnameLayout.setVisibility(View.VISIBLE);
                    else
                        teamnameLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openGallary(){
        Intent openGallery=new Intent();
        openGallery.setType("image/");
        openGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(openGallery,PICK_IMAGE_REQUEST_TEAMLOGO);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST_TEAMLOGO && resultCode==getActivity().RESULT_OK
                && data!=null && data.getData()!=null)
        {
            imageTeamLOGO=data.getData();
            Picasso.with(getActivity()).load(imageTeamLOGO).into(teamlogo);
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cr = getActivity().getApplicationContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

}