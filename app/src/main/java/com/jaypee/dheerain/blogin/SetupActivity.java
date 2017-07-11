package com.jaypee.dheerain.blogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class SetupActivity extends AppCompatActivity {
    FirebaseAuth mauth;

    ImageButton mDp;
    EditText user_name;
    Button submit;

    ProgressDialog progressDialog;

    private Uri post_image;

    static Uri downloadurl;

    private static final int galler=1;

    private StorageReference mstorage;

    private DatabaseReference mref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mauth=FirebaseAuth.getInstance();

        mref= FirebaseDatabase.getInstance().getReference().child("USER");
        mstorage= FirebaseStorage.getInstance().getReference();
        mDp= (ImageButton) findViewById(R.id.dp_image);
        user_name= (EditText) findViewById(R.id.user_name);
        submit= (Button) findViewById(R.id.submit);

        progressDialog=new ProgressDialog(this);

mDp.setOnClickListener(new View.OnClickListener() {
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
            startPosting();
        }
    });
    }

    private void startPosting() {
        progressDialog.setMessage("Posting to the Blog....");
        progressDialog.show();
        final String name=user_name.getText().toString().trim();
        if(!TextUtils.isEmpty(name ) &&  post_image !=null)
        /*if these condititons sattisfy then onlky one should be allowed to post*/
        {
            StorageReference filepath=mstorage.child("profile_pic").child(post_image.getLastPathSegment());
            filepath.putFile(post_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.d("Postactivity","HERER");
                    downloadurl= taskSnapshot.getDownloadUrl();
                    progressDialog.dismiss();
                    DatabaseReference newpost=mref.child(mauth.getCurrentUser().getUid().toString());
                    newpost.child("name").setValue(name);
                    newpost.child("image").setValue(String.valueOf(downloadurl));

                    Intent main_intent=new Intent(SetupActivity.this,MainActivity.class);
                    main_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(main_intent);
                }
            });

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==galler && resultCode==RESULT_OK)
        {
            Uri image=data.getData();/*here we get the selected image*/
            Toast.makeText(this, "i am here", Toast.LENGTH_SHORT).show();
            CropImage.activity(image)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(100,100)
                    .start(SetupActivity.this);/*we satrt this cativity to crop the image*/
            Toast.makeText(this, "cleraed this", Toast.LENGTH_SHORT).show();

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();/*this piece of code gets the cropped image uri*/
                post_image=resultUri;
                Toast.makeText(this, "Onto the posting job", Toast.LENGTH_SHORT).show();
                mDp.setImageURI(resultUri);/*here i set the image uri to the imagevieeew*/
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
