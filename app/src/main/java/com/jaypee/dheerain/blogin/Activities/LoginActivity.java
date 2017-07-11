package com.jaypee.dheerain.blogin.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaypee.dheerain.blogin.R;

public class LoginActivity extends AppCompatActivity {
    EditText email,password;
    TextView heading;
    ImageView mGoogleSignin;
    Button login,signup;
    FirebaseAuth mauth;
    LinearLayout linearLayout;
    DatabaseReference mref;
    GoogleApiClient mGoogleApiClient;
    String TAG="Login_activity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=(EditText) findViewById(R.id.emailField);
        heading= (TextView) findViewById(R.id.heading);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/orange.ttf");
        linearLayout=(LinearLayout)findViewById(R.id.linear);
        heading.setTypeface(custom_font);
        password=(EditText) findViewById(R.id.passwordField);
        signup= (Button) findViewById(R.id.signup);
        login= (Button) findViewById(R.id.login);

        ((LinearLayout)findViewById(R.id.linear)).getBackground().setAlpha(125);

        mGoogleSignin= (ImageView) findViewById(R.id.google);


        mauth=FirebaseAuth.getInstance();
        mref= FirebaseDatabase.getInstance().getReference().child("USER");
        /*getting the User refrence adn not root beacuse here we wanna chck weather the user is in the databse or now
         * and also when user signup using fb or google they arent added in db just authentication
          * but we need the dp and name*/
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
signup.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)/*take it to the regiser acitvity*/;
        startActivity(i);
    }


});

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(this).
                enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

mGoogleSignin.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        signIn();
    }
});
    }

    private void loginUser() {
        String emailadd,pass;
        emailadd=email.getText().toString().trim();
        pass=password.getText().toString().trim();
        /*sign in using mauth , email and pass*/
        mauth.signInWithEmailAndPassword(emailadd,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(mauth.getCurrentUser().getUid()!=null)
                checkUserExist();/*check if user has a name and dp in firebase db*/

            }
        });
    }

    private void checkUserExist() {
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(mauth.getCurrentUser().getUid()))
                {
                    /*here we get the snapshot of the data */
                    /*so in the snapshot we look weather there is child with current user id*/
                    /*if yes then opne the main activity*/
                    Intent i=new Intent(LoginActivity.this,MainActivity.class);
                     i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
                else
                {/*if no then data is incomplete dont let himm procede*/
                    Toast.makeText(LoginActivity.this, "Your havent completed your profile ", Toast.LENGTH_SHORT).show();
                    Intent setup=new Intent(LoginActivity.this,SetupActivity.class);
                    setup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setup);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 1) {
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
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            checkUserExist();
                            FirebaseUser user = mauth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });}

}
