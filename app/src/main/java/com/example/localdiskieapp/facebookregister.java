package com.example.localdiskieapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class facebookregister extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebookregister);
        mAuth = FirebaseAuth.getInstance();
        createFacebook();
    }
    private void createFacebook(){
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        LoadingBar bar=new LoadingBar(facebookregister.this);
        bar.startLoading();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            bar.stopLoading();
                            StoreUser u=new StoreUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),name);
                            userExists();

                        }else{
                            bar.stopLoading();
                            Toast.makeText(facebookregister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    AccessTokenTracker tokenTracker=new AccessTokenTracker(){
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken!=null){
                loaduserprofile(currentAccessToken);
            }
            else
            {
                name="";
            }
        }
    };

    private void loaduserprofile(AccessToken currentAccessToken) {
        GraphRequest request=GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String lastname=object.getString("last_name");
                    String firstname=object.getString("first_name");
                    name=firstname+" "+lastname;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle parameters=new Bundle();
        parameters.putString("fields","first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }
    private void createUser(){
        FirebaseDatabase rootNode;
        DatabaseReference reference;
        rootNode= FirebaseDatabase.getInstance();
        reference=rootNode.getReference("users");
        Users users=new Users(FirebaseAuth.getInstance().getCurrentUser().getUid(),name,"NONE","NONE");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(users);
    }
    public void userExists()
    {

        String UID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("users");
        final Query checkTeamName=reference.orderByChild("uid").equalTo(UID);
        checkTeamName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    createUser();
                }
                Intent intent =new Intent(facebookregister.this,localDiskieHome.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}