package com.jaypee.dheerain.blogin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaypee.dheerain.blogin.Model.SavedPost;
import com.jaypee.dheerain.blogin.Model.blog;
import com.jaypee.dheerain.blogin.R;
import com.jaypee.dheerain.blogin.Realm.RealmController;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class OpenedActivity extends AppCompatActivity {

    DatabaseReference mrefuser;
    blog data;
    String blog_id;
    ImageView bookMark;
    SavedPost savedPost;
    //data task;

    private Realm realm;

    TextView userName, title/*, date*/, disc;
    ImageView userDp, blogImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_opened);

        setRealmConfigration();

        this.realm = RealmController.with(this).getRealm(); // getting the insatnce of the realm database

        blog_id = getIntent().getStringExtra("blog_id");
        //userName = (TextView) findViewById(R.id.user_name);
        title = (TextView) findViewById(R.id.title);
        // date = (TextView) findViewById(R.id.date);
        disc = (TextView) findViewById(R.id.description);
        //userDp = (ImageView) findViewById(R.id.user_dp);
        blogImage = (ImageView) findViewById(R.id.blog_image);
        bookMark= (ImageView) findViewById(R.id.bookmark);


        Intent decider=getIntent();
        if(decider.getAction()== "online")
        {
            setViewByFirebase();// here we set the opened activity using firebase if online acton is called using the intent
        }
        else
        {
            setViewByRealm(decider);// here we open the view using the realm database if the action called is offline
        }

        bookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();   // here we beinthe
                realm.copyToRealm(savedPost);
                realm.commitTransaction();
            }
        });


    }

    private void setViewByRealm(Intent decider) {

        title.setText(decider.getStringExtra("title"));
        disc.setText(decider.getStringExtra("discription"));
        setblogImage(decider.getStringExtra("blogimage"));

    }


    void setViewByFirebase()
    {
        mrefuser = FirebaseDatabase.getInstance().getReference().child("BLOG").child(blog_id);

        mrefuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data = dataSnapshot.getValue(blog.class);
                //     setall(data);
                //Toast.makeText(OpenedActivity.this, ""+ data.getDate()+data.getName(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(OpenedActivity.this,data.getDate(), Toast.LENGTH_SHORT).show();
                title.setText(data.getTitle());
                //  userName.setText(data.getName());
                // date.setText(data.getDate());
                disc.setText(data.getDiscription());
                //setDp(data.getDp());
                setblogImage(data.getImage());
                savedPost=new SavedPost(data.getTitle()
                        ,data.getDate()
                        ,data.getDiscription()
                        ,data.getDp()
                        ,data.getName()
                        ,data.getImage());  ///creating object for the saved post object to be saved in th realm database
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        // Toast.makeText(this, ""+blog_id + mrefuser.child("discription").getKey(), Toast.LENGTH_SHORT).show();

    }


    private void setblogImage(final String image) {
        Picasso.with(OpenedActivity.this)
                .load(image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .centerCrop()
                .resize(500,500)
                .into(blogImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(OpenedActivity.this)
                                .load(image)
                                .centerCrop()
                                .resize(500,500)
                                .into(blogImage);
                    }
                });
    }


    private void setDp(final String image) {
        Picasso.with(OpenedActivity.this)
                .load(image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .centerCrop()
                .resize(40, 40)
                .into(userDp, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(OpenedActivity.this)
                                .load(image)
                                .centerCrop()
                                .resize(40, 40)
                                .into(userDp);
                    }
                });
    }

    void setRealmConfigration()
    {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}