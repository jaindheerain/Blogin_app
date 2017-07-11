package com.jaypee.dheerain.blogin.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaypee.dheerain.blogin.R;

public class RegisterActivity extends AppCompatActivity {
    EditText name,email,password;
    Button signUp;
    FirebaseAuth mauth;/*basic  auth and database refrences required*/
    DatabaseReference db,dbBlog;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name=(EditText) findViewById(R.id.nameField);
        email=(EditText) findViewById(R.id.emailField);
        password=(EditText) findViewById(R.id.passwordField);
        signUp=(Button)findViewById(R.id.signUP);

        mauth =FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance().getReference().child("USER");
        db.keepSynced(true);
        dbBlog=FirebaseDatabase.getInstance().getReference().child("BLOG");
        /*getting the rot refrence to the users beacuse here we wanna add user info like name and dp in the
        * database*/

        progress=new ProgressDialog(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            signupUser();
            }
        });
    }

    private void signupUser() {
        progress.setMessage("Signing you Up");
        progress.show();
 /*creating a new user int eh auth part of the firebase */
        mauth.createUserWithEmailAndPassword(email.getText().toString().trim(),
                password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            /*adding and on complete to do task after completion*/
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("REgister","SucesSSSSSSSSSSSSSSSSSS");
                String user_id=mauth.getCurrentUser().getUid();
                DatabaseReference userdb=db.child(user_id);/*creating a child with the unique user id to get*/
                userdb.child("name").setValue(name.getText().toString());
                userdb.child("image").setValue("Defualt");
                        /*adding user info to the DB*/

                        progress.dismiss();

                Intent i=new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(i);
                        /*String the Main Blog activity bcacuse the user has regitered*/

            }
        });
    }


}
