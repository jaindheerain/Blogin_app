package com.jaypee.dheerain.blogin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaypee.dheerain.blogin.Activities.OpenedActivity;
import com.jaypee.dheerain.blogin.Model.SavedPost;
import com.jaypee.dheerain.blogin.R;
import com.squareup.picasso.Picasso;

import io.realm.RealmResults;

/**
 * Created by Dheerain on 14-07-2017.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    RealmResults<SavedPost> list;
    Context context;

    public BookmarkAdapter(RealmResults<SavedPost> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(context).inflate(R.layout.post_layout,parent,false);
        ViewHolder viewHolder=new ViewHolder(v);
        int a=12;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BookmarkAdapter.ViewHolder holder, final int position) {

        holder.title.setText(list.get(position).getTitle());
        holder.discription.setText(list.get(position).getDiscription());
        holder.date.setText(list.get(position).getDate());
        Picasso.with(context).load(list.get(position).getBlogImage()).resize(500,500).centerCrop().into(holder.blogImage);
        holder.blogImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openact=new Intent(context, OpenedActivity.class);
                openact.setAction("offline");
                openact.putExtra("title",list.get(position).getTitle());
                openact.putExtra("discription",list.get(position).getDiscription());
                openact.putExtra("date",list.get(position).getDate());
                openact.putExtra("dp",list.get(position).getUserDp());
                openact.putExtra("name",list.get(position).getUserName());
                openact.putExtra("blogimage",list.get(position).getBlogImage());

                context.startActivity(openact);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,discription,date;
        ImageView blogImage,like;

        public ViewHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.post_title);
            discription= (TextView) itemView.findViewById(R.id.post_disp);
            date= (TextView) itemView.findViewById(R.id.date);
            blogImage= (ImageView) itemView.findViewById(R.id.post_image);
            like= (ImageView) itemView.findViewById(R.id.like);
        }
    }
}
