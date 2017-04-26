package com.uisys.firebasechatapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.firebase.auth.*;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    EditText messageET;
    Button  sendBtn;
    DatabaseReference databaseReference;
    ArrayList<BktappUserInfo> userList = new ArrayList();
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
    ArrayAdapter userListAdater;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        //signInBtn = (Button) findViewById(R.id.signIn);
        listView= (ListView) findViewById(R.id.listView);
        messageET= (EditText) findViewById(R.id.sendMessage);
        sendBtn= (Button) findViewById(R.id.sendMessageButton);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.filmNews)
                {
                    Intent intent = new Intent(MainActivity.this,ViewNewsActivity.class);
                    startActivity(intent);


                }

                if(item.getItemId() == R.id.publishNews)
                {
                    Intent intent = new Intent(MainActivity.this,AddNewsActivity.class);
                    startActivity(intent);


                }
                if(item.getItemId() == R.id.signIn)
                {
                    if(item.getTitle().equals("signIn")) {


                        signIn();

                    }

                    else
                    {
                        FirebaseAuth.getInstance().signOut();
                        item.setTitle("signIn");

                        userList= new ArrayList();
                    }


                }

                return true;
            }
        });

         mAuth = FirebaseAuth.getInstance();
         userListAdater= new ChatArrayAdapter(this,android.R.layout.simple_list_item_2,userList);


 /*       signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });*/


                sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText   =messageET.getText().toString();
                ChatMessage message = new ChatMessage(userName,messageText);
                databaseReference.push().setValue(message);
                messageET.setText("");


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

    class CustomerCareUserEventListener implements ChildEventListener
    {

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Log.i(TAG, "onChild added" + dataSnapshot.getValue());
                String uid=dataSnapshot.getKey();
                getUserInfo(uid);
               //add value listner for new message event
                FirebaseDatabase.getInstance().getReference("ChatApp/Messages/"+uid).
                        addValueEventListener(new MessageEventListener(MainActivity.this,uid,userName));

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
        private void getUserInfo( String uid)
        {

            FirebaseDatabase.getInstance().getReference("/AppUsersUid/"+uid).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String uid =dataSnapshot.getKey();
                            Log.i(TAG,"user uid "+dataSnapshot.getKey());
                            Log.i(TAG,"user details "+dataSnapshot.getValue());

                            BktappUserInfo bktappUserInfo=dataSnapshot.getValue(BktappUserInfo.class);
                            Log.i(TAG,bktappUserInfo.getName());
                            Log.i(TAG,bktappUserInfo.getEmailId());
                            bktappUserInfo.setUid(uid);
                            userList.add(bktappUserInfo);
                            userListAdater.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });



        }




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
       // super.onActivityResult(requestCode, resultCode, data);
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
                        Query query=FirebaseDatabase.getInstance().getReference("CustomerCareUids/"+uid);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.i(TAG,dataSnapshot.getKey());
                                //Log.i(TAG,dataSnapshot.getValue().toString());
                                if(dataSnapshot.getValue() != null)
                                {
                                    HashMap hashMap = (HashMap) dataSnapshot.getValue();
                                    userName= (String) hashMap.get("name");
                                    Log.i(TAG,"customer care uid" +userName);
                                    toolbar.getMenu().getItem(1).setTitle(userName);
                                    messageET.setVisibility(View.INVISIBLE);
                                    sendBtn.setVisibility(View.INVISIBLE);
                                    databaseReference= FirebaseDatabase.getInstance().getReference("/ChatApp/Messages");
                                    databaseReference.addChildEventListener(new CustomerCareUserEventListener());

                                    listView.setAdapter(userListAdater);
                                    customerCareUserListItemClickListner();

                                }
                                else
                                {
                                    Log.i(TAG,"not a customer care uid");
                                    toolbar.getMenu().getItem(1).setTitle(userName);
                                    databaseReference = FirebaseDatabase.getInstance().
                                            getReference("/ChatApp/Messages" + "/" +
                                                    uid);
                                    chatFirebaseListAdatper();
                                    messageET.setVisibility(View.VISIBLE);
                                    sendBtn.setVisibility(View.VISIBLE);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



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

    public void customerCareUserListItemClickListner()
    {
          listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                  TextView  uidT = (TextView) view.findViewById(R.id.uid);
                  intent.putExtra("uid", uidT.getText().toString());
                  intent.putExtra("userName",userName);
                  MainActivity.this.startActivity(intent);


              }
          });



    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();

    }
}
