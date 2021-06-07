package com.example.localdiskieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class phoneVarification extends AppCompatActivity {
    private  String verificationId;
    private FirebaseAuth mAuth;
    private EditText phonenumber,names;
    private String num , name;
    private Button submit;
    private TextInputLayout hint,hint1;
    LoadingBar bar;

    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_varification);
        submit=(Button)findViewById(R.id.phoneSubmit);
        hint=(TextInputLayout)findViewById(R.id.hint);
        hint1=(TextInputLayout)findViewById(R.id.hint1);
        phonenumber=(EditText)findViewById(R.id.phonenumber);
        names=(EditText)findViewById(R.id.names);
        mAuth=FirebaseAuth.getInstance();
        //FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting();
        broadcastReceiver=new Connection();
        //registerNetworkBroadcast();
        bar=new LoadingBar(phoneVarification.this);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str=submit.getText().toString();

//                Intent intent=new Intent(phoneVarification.this,localDiskieHome.class);
//                startActivity(intent);
                register(str);
            }
        });
    }
    private void register(String str)
    {
        if(str.equals("Get OTC")){

            num=phonenumber.getText().toString();
            name=names.getText().toString();
                    /*
                    check if its empty and is phone number
                     */
            sendVerificationCode(num);
            phonenumber.setText("");
            hint.setHint("Enter OTP");
            submit.setText("Confirm");
            names.setVisibility(View.GONE);
            hint1.setVisibility(View.GONE);
        }else{
                    /*
                    check if its empty
                     */
            String code=phonenumber.getText().toString();

            verifyCode(code);

        }
    }
    private void signInWithCredential(PhoneAuthCredential credintial)
    {


        mAuth.signInWithCredential(credintial)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            StoreUser user=new StoreUser(FirebaseAuth.getInstance().getCurrentUser().getUid(),name);
                            userExists();


                        }else {
                            bar.stopLoading();
                            Toast.makeText(phoneVarification.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private  void sendVerificationCode(String number)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                phoneVarification.this,
                mCallBack
        );
    }
    private  void verifyCode(String code)

    {
        PhoneAuthCredential credintial=PhoneAuthProvider.getCredential(verificationId,code);
        signInWithCredential(credintial);

    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {

            super.onCodeSent(s, forceResendingToken);

            verificationId=s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String Code=phoneAuthCredential.getSmsCode();
//            if(Code!=null)
//            {
//                phonenumber.setText(Code);
//                  verifyCode(Code);
//            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(phoneVarification.this,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    };
    protected  void registerNetworkBroadcast(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter((ConnectivityManager.CONNECTIVITY_ACTION)));
        }
    }
    protected void unregisterNetwork(){
        try{
            unregisterReceiver(broadcastReceiver);
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterNetwork();
    }
    private void createUser(){
        FirebaseDatabase rootNode;
        DatabaseReference reference;
        rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference("users");
        Users users=new Users(FirebaseAuth.getInstance().getCurrentUser().getUid(),name,"NONE","NONE");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(users);
    }
    public void userExists()
    {

        bar.startLoading();
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
                bar.stopLoading();
                Intent intent=new Intent(phoneVarification.this,localDiskieHome.class);

                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}