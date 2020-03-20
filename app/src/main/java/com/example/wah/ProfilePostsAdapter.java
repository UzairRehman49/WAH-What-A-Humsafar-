package com.example.wah;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProfilePostsAdapter extends RecyclerView.Adapter<ProfilePostsAdapter.MyViewHolder> {
    private String [] data;
    private ArrayList<ProfilePosts>  postData;


    public ProfilePostsAdapter(ArrayList<ProfilePosts> postData) {
        this.postData = postData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.user_profile_post_row, parent, false);


        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //String title = data[position];
        int myimg = postData.get(position).getImage();
        String mydest = postData.get(position).getDestination();
        String mydays = postData.get(position).getDays();
        String mylabel = postData.get(position).getLabel();
        String mysrc = postData.get(position).getSource();
        String mydate = postData.get(position).getDate();

       // holder.imageView.setImageAlpha(myimg);
       // holder.imageView.setImageDrawable(R.drawable.babusar);
        holder.imageView.setImageResource(myimg);
        holder.dest.setText(mydest);
        holder.days.setText(mydays);
        holder.label.setText(mylabel);
        holder.src.setText(mysrc);
        holder.date.setText(mydate);

       // holder.textView.setText(title);

    }

    @Override
    public int getItemCount() {
       // return data.length;
        return postData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        //TextView textView;
        TextView dest, days, src, date, label;
        public MyViewHolder (View itemView)
        {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView4);
//            imageView = itemView.findViewById(R.id.imgIcon);
//            textView = itemView.findViewById(R.id.txtTitle);

            dest = itemView.findViewById(R.id.destination4);
            days = itemView.findViewById(R.id.textViewDays4);
            label = itemView.findViewById(R.id.textViewConst4);
            src = itemView.findViewById(R.id.textViewSrc4);
            date = itemView.findViewById(R.id.textViewDate4);

        }
    }

}
