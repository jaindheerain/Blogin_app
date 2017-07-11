package com.jaypee.dheerain.blogin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.jaypee.dheerain.blogin.Activities.OpenedActivity;
import com.jaypee.dheerain.blogin.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * Created by Dheerain on 04-07-2017.
 */

public class BlogViewHolder extends RecyclerView.ViewHolder{
    View mview;
    FirebaseAuth mauth;
    public BlogViewHolder(View itemView) {
        super(itemView);
        mview=itemView;
        mauth=FirebaseAuth.getInstance();

    }
    public  void settitle(String title)
    {
        TextView tit= (TextView) mview.findViewById(R.id.post_title);
        Log.d("title",title);
        tit.setText(title);
    }
    public void setdisp(String disp)
    {
        Log.d("discription",disp);
        TextView discrip= (TextView) mview.findViewById(R.id.post_disp);
        discrip.setText(disp);
    }
    public void setimage(final Context context, final String image)
    {
        final ImageView img= (ImageView) mview.findViewById(R.id.post_image);
            /*load the uri and into the view in whihc you wna tot showo */

            Picasso.with(context)
                .load(image)
                .centerCrop()
                .resize(500,500)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(image)
                                .centerCrop()
                                .resize(500,200)
                                .into(img);
                    }
                });

    }
    /*public void setdp(final Context context,final String image)
    {
        final ImageView img= (ImageView) mview.findViewById(R.id.user_id);

            *//*load the uri and into the view in whihc you wna tot showo *//*
        Picasso.with(context)
                .load(image)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .resize(40,40)
                .centerCrop()
                .into(img, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(image)
                                .resize(40,40)
                                .centerCrop()
                                .into(img);
                    }
                });
    }*/
    public void onclick(Context c, final DatabaseReference mref, final String blog_id)

    {
        final Context co=c;
        mview.findViewById(R.id.post_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openActivity=new Intent(co,OpenedActivity.class);
                Toast.makeText(co, ""+blog_id, Toast.LENGTH_SHORT).show();
                openActivity.putExtra("blog_id",blog_id);
                co.startActivity(openActivity);
            }
        });
    }
    /*public void setname(String name)
    {
        ((TextView)mview.findViewById(R.id.user_name)).setText(name);
    }*/
    public void setdate(String name)
    {
        ((TextView)mview.findViewById(R.id.date)).setText(name);
    }


    public void like(final String blog_id, final DatabaseReference mref)
    {
        Log.e("qwerty", String.valueOf(mauth.getCurrentUser()));

        if(mauth!=null){
            Log.e("qwerty", String.valueOf(mauth.getCurrentUser()));

        final ImageView likeIcon= (ImageView) mview.findViewById(R.id.like);


        mref.child("Likes").child(blog_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                {
                    if(dataSnapshot.hasChild(mauth.getCurrentUser().getUid()))
                    {
                        //mref.child("Likes").child(blog_id).child(user_id).removeValue();
                        likeIcon.setImageResource(R.mipmap.liked);
                        //liked[0] =0;
                    }
                    else
                    {
                //        mref.child("Likes").child(blog_id).child(user_id).setValue("hello");
                        likeIcon.setImageResource(R.mipmap.disliked);
                  //      liked[0] =0;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mview.findViewById(R.id.like).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] liked = {1};

                mref.child("Likes").child(blog_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(liked[0] ==1)
                        {
                            if(dataSnapshot.hasChild(mauth.getCurrentUser().getUid()))
                            {
                                mref.child("Likes").child(blog_id).child(mauth.getCurrentUser().getUid()).removeValue();
                                likeIcon.setImageResource(R.mipmap.disliked);
                                liked[0] =0;
                            }
                            else
                            {
                                mref.child("Likes").child(blog_id).child(mauth.getCurrentUser().getUid()).setValue("hello");
                                likeIcon.setImageResource(R.mipmap.liked);
                                liked[0] =0;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }}

}