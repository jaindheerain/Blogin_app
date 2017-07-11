package com.jaypee.dheerain.blogin.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaypee.dheerain.blogin.Adapter.BlogViewHolder;
import com.jaypee.dheerain.blogin.R;
import com.jaypee.dheerain.blogin.Model.blog;

public class MainActivity extends AppCompatActivity {
    RecyclerView bloglist;

    DatabaseReference mref;

    DatabaseReference mrefuser;

    String blog_id;

    FirebaseAuth mauth;/*this to mmale the authentication*/

    FirebaseAuth.AuthStateListener mauthstate;

    /*this is to see weather sa user is already loged in or not  */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //            picasso();
//        menu_inflator a=new menu_inflator();
        bloglist = (RecyclerView) findViewById(R.id.blog_list);
        bloglist.setHasFixedSize(true);
        bloglist.setLayoutManager(new LinearLayoutManager(this));

        mref = FirebaseDatabase.getInstance().getReference().child("BLOG");/*we want to read the blog child and not he rot
                directory so we need to point at the chld to get the data */
        mref.keepSynced(true);
        mrefuser = FirebaseDatabase.getInstance().getReference().child("USER");
        mrefuser.keepSynced(true);

        mauth = FirebaseAuth.getInstance();
        mauthstate = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    //  i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                } else {
                    checkUserExist();
                }
            }
        };



    }


    @Override
    protected void onStart() {
        super.onStart();
        mauth = FirebaseAuth.getInstance();
        mauth.addAuthStateListener(mauthstate);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);//this to infalte the menu we generate
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(MainActivity.this, PostActivity.class));//satrt a new activit as we click on the + sign
        }
        if (item.getItemId() == R.id.action_logout) {
            mauth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }


    private void checkUserExist() {
        mrefuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(mauth.getCurrentUser().getUid())) {
                    /*here we get the snapshot of the data */
                    /*so in the snapshot we look weather there is child with current user id*/
                    /*if yes then opne the main activity*/
                    Toast.makeText(MainActivity.this, "Your havent completed your profile ", Toast.LENGTH_SHORT).show();
                    Intent setup = new Intent(MainActivity.this, SetupActivity.class);
                    setup.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setup);
                }
                else {

                    FirebaseRecyclerAdapter<blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<blog, BlogViewHolder>(
                            blog.class,/*the object calss of the data*/
                            R.layout.post_layout,/*the layout ew want to imposr*/
                            BlogViewHolder.class,/*the view holder */
                            mref/*the data base refrence */

                    ) {
                        @Override
                        protected void populateViewHolder(final BlogViewHolder viewHolder, final blog model, int position) {

                            viewHolder.settitle(model.getTitle());
                            viewHolder.setdisp(model.getDiscription());
                            viewHolder.setimage(getApplicationContext(), model.getImage());
                       //     viewHolder.setname(model.getName());
                            viewHolder.setdate(model.getDate());
                            blog_id = model.getBlog_id();
                         //   viewHolder.setdp(getApplication(), model.getDp());
                            viewHolder.onclick(MainActivity.this, mref, blog_id);
                            viewHolder.like(blog_id, FirebaseDatabase.getInstance().getReference());

                /*picaso reqires the context so the ocntext is passed because this si astatic class and we need to pass the context
                * */
                        }
                    };
                    bloglist.setAdapter(firebaseRecyclerAdapter);/*dont forget to attach the adapter to the recycler list*/
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
