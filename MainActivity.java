package com.uisys.firebasechatapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    EditText messageET;
    Button  sendBtn;
    DatabaseReference databaseReference;
    ArrayList chatMsgList = new ArrayList();
    GoogleSignInOptions gso;
    GoogleApiClient mGoogleApiClient;
    int RC_SIGN_IN = 1;
    String TAG="@uniSys";
    String userName;
    String uid;
    Button signInBtn;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    TextView textView1;
    TextView textView2;
    FirebaseListAdapter<ChatMessage> firebaseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        signInBtn = (Button) findViewById(R.id.signIn);
        listView= (ListView) findViewById(R.id.listView);
        messageET= (EditText) findViewById(R.id.sendMessage);
        sendBtn= (Button) findViewById(R.id.sendMessageButton);

         mAuth = FirebaseAuth.getInstance();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    signIn();

            }
        });


                sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText   =messageET.getText().toString();
                ChatMessage message = new ChatMessage(userName,messageText);
                databaseReference.push().setValue(message);


            }
        });


    }


    void chatFirebaseListAdatper()

    {

        firebaseListAdapter = new FirebaseListAdapter<ChatMessage>
                (this,
                        ChatMessage.class,android.R.layout.simple_list_item_2,
                        databaseReference.limitToLast(5))
        {
            @Override
            protected void populateView(View v, ChatMessage chatMessage, int position) {
                textView1 = (TextView) v.findViewById(android.R.id.text1);
                textView2=  (TextView) v.findViewById(android.R.id.text2);

                textView1.setTextColor(Color.BLUE);
                textView1.setText(chatMessage.getName());
                textView2.setText(chatMessage.getMessage());

            }
        };

        listView.setAdapter(firebaseListAdapter);


    }

    private void signIn() {
        Log.i(TAG,"signIn");
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

         mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        userName=mAuth.getCurrentUser().getDisplayName();
                        uid=mAuth.getCurrentUser().getUid().toString();
                        Log.i(TAG,"user Name : " +userName);
                        Log.i(TAG,"uid : " +uid);
                        String groupId = "Group001";
                        databaseReference= FirebaseDatabase.getInstance().getReference("/ChatApp/Messages"+"/"+
                                groupId);

                        signInBtn.setText("signOut");
                        chatFirebaseListAdatper();



                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                        // ...
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();

    }
}
