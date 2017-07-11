package com.jaypee.dheerain.blogin;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Date;

public class PostActivity extends AppCompatActivity {
    ImageButton minsert;
    EditText title,discription;
    Button submit;
    private Uri post_image;
    static Uri downloadurl;
    private static final int galler=1;
    private StorageReference mstorage;
    private DatabaseReference mref;
    private DatabaseReference mref_user;
    FirebaseUser muser;
    FirebaseAuth mauth;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        minsert= (ImageButton) findViewById(R.id.image);

        mstorage= FirebaseStorage.getInstance().getReference();

        mref=FirebaseDatabase.getInstance().getReference().child("BLOG");
        mref_user=FirebaseDatabase.getInstance().getReference().child("USER");
        mauth=FirebaseAuth.getInstance();
        muser=mauth.getCurrentUser();

        title= (EditText) findViewById(R.id.posttitle);
        progressDialog=new ProgressDialog(this);
        discription= (EditText) findViewById(R.id.postdesp);
        submit= (Button) findViewById(R.id.submit);

        minsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_PICK);
                i.setType("image/*");/*this open the galler window  and sset the types to image sso w ccan only access images */
                startActivityForResult(i,galler);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startposting();/*we , make a  method to start the posting process */
            }
        });
    }

    private void startposting() {
        progressDialog.setMessage("Posting to the Blog....");
        progressDialog.show();
        final String title_val=title.getText().toString().trim();
        final String discription_val=discription.getText().toString().trim();
        if(!TextUtils.isEmpty(title_val ) && !TextUtils.isEmpty(discription_val) && post_image !=null)
        /*if these condititons sattisfy then onlky one should be allowed to post*/
        {
            StorageReference filepath=mstorage.child("blogginfpic").child(post_image.getLastPathSegment());
            filepath.putFile(post_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Postactivity","HERER");
                final Uri downlaoduri= taskSnapshot.getDownloadUrl();

                    mref_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DatabaseReference newpost=mref.push();
                            newpost.child("title").setValue(title_val);
                            newpost.child("discription").setValue(discription_val);
                            newpost.child("image").setValue(String.valueOf(downlaoduri));
                            newpost.child("user_name").setValue(dataSnapshot.child(muser.getUid()).child("name").getValue());
                            newpost.child("date").setValue(setdate());
                            String img= dataSnapshot.child(mauth.getCurrentUser().getUid()).child("image").getValue(String.class);
                            newpost.child("u_image").setValue(img);
                            newpost.child("blog_id").setValue(newpost.getKey());
                                    //Toast.makeText(MainActivity.this,img, Toast.LENGTH_SHORT).show();


                            finish();
                            Intent main_activity=new Intent(PostActivity.this,MainActivity.class);
                            startActivity(main_activity);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            });
            progressDialog.dismiss();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==galler && resultCode==RESULT_OK)
        {
            Uri image=data.getData();/*here we get the selected image*/
            Toast.makeText(this, "i am here", Toast.LENGTH_SHORT).show();
                   CropImage.activity(image)
                    .setGuidelines(com.theartofdev.edmodo.cropper.CropImageView.Guidelines.ON)
                           .setAspectRatio(100,100)
                    .start(PostActivity.this);/*we satrt this cativity to crop the image*/
            Toast.makeText(this, "cleraed this", Toast.LENGTH_SHORT).show();

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();/*this piece of code gets the cropped image uri*/
                post_image=resultUri;
                Toast.makeText(this, "Onto the posting job", Toast.LENGTH_SHORT).show();
                minsert.setImageURI(resultUri);/*here i set the image uri to the imagevieeew*/
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String setdate()
    {
        Calendar c = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            c = Calendar.getInstance();

            SimpleDateFormat df = null;

            df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            String formattedDate = df.format(c.getTime());

            return formattedDate;
        }

        return DateFormat.getDateTimeInstance().format(new Date());

    }
}
