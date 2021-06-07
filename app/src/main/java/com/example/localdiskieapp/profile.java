package com.example.localdiskieapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
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

public class profile extends AppCompatActivity {
    private  static  final int PICK_IMAGE_REQUEST_USERLOGO=1;
    private Uri imageUserLOGO;
    private CircularImageView pic;
    UserDatails userDatails;
    private String UID;
    private TextView teamname,usernames;

    private StorageTask mUploadTask;
    private StorageReference mStorageRef;
    private TabLayout tabLayout;
    private ViewPager2 pager2;
    private FragementAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //
        Button logout=(Button)findViewById(R.id.btnlogout);
        teamname=(TextView)findViewById(R.id.teamname);
        usernames=(TextView)findViewById(R.id.username);
        UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        mStorageRef = FirebaseStorage.getInstance().getReference("profilepicture");
        pic= (CircularImageView)findViewById(R.id.picture);
        userDatails=new UserDatails();
        //
        tabLayout=(TabLayout)findViewById(R.id.tab_layout);
        pager2=(ViewPager2)findViewById(R.id.view_pager2);
        //

        FragmentManager fm=getSupportFragmentManager();
        adapter=new FragementAdapter(fm,getLifecycle());
        pager2.setAdapter(adapter);
        tabLayout.addTab(tabLayout.newTab().setText("My Posts"));
        tabLayout.addTab(tabLayout.newTab().setText("About Team"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                set profile picture for user
                 */

                openGallary();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
            }
        });

        getTeamName();


    }
    private void openGallary(){
        Intent openGallery=new Intent();
        openGallery.setType("image/");
        openGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(openGallery,PICK_IMAGE_REQUEST_USERLOGO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE_REQUEST_USERLOGO && resultCode==RESULT_OK
                && data!=null && data.getData()!=null)
        {
            imageUserLOGO=data.getData();
            Picasso.with(this).load(imageUserLOGO).into(pic);
            uploadProfile();
        }
    }

    private void uploadProfile() {

        final String picture_name = System.currentTimeMillis() + "." + getFileExtension(imageUserLOGO);
        StorageReference fileref = mStorageRef.child(picture_name);
        mUploadTask=fileref.putFile(imageUserLOGO)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(profile.this,"upload successful.",Toast.LENGTH_LONG).show();
                        Task<Uri> result=taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                update(uri.toString());
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(profile.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        Toast.makeText(profile.this,"please wait...",Toast.LENGTH_LONG).show();
                    }
                });
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cr =getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
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
                    String dp=snapshot.child(UID).child("picture").getValue(String.class);
                    if(!tn.equals("NONE"))
                        teamname.setText(tn);
                    else
                        teamname.setText("register team");

                    if(!dp.equals("NONE")){
                        Picasso.with(profile.this)
                                .load(dp)
                                .placeholder(R.drawable.userprofile)
                                .into(pic);
                    }
                    usernames.setText(snapshot.child(UID).child("name").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void update(String uri){
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference ref=database.getReference();
        ref.child("users").child(UID).child("picture").setValue(uri);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userDatails=new UserDatails();
    }
}