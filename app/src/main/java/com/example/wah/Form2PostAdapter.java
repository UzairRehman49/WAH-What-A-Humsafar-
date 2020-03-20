package com.example.wah;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Form2PostAdapter extends RecyclerView.Adapter<Form2PostAdapter.Form2PostViewHolder> {
   // private String [] data;
    private ArrayList<ProfilePosts2>  postData;
    String source;
    String finalDest;
    int numOfStops;


    public Form2PostAdapter(ArrayList<ProfilePosts2> postData, int numOfStops, String source, String finalDest) {
        this.postData = postData;
        this.numOfStops= numOfStops;
        this.source= source;
        this.finalDest= finalDest;

    }

    @NonNull
    @Override
    public Form2PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());



        View view = inflater.inflate(R.layout.post_form2_row2, parent, false);


        return new Form2PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Form2PostViewHolder holder, int position) {
        //String title = data[position];
        String mydest_name = postData.get(position).getDestination_name();
        String mytransport = postData.get(position).getTransport();
        String mystay = postData.get(position).getStay();
        String myprice = postData.get(position).getPrice();
        int dum= postData.get(position).getStopNum();
        String stop= Integer.toString(dum);
        // holder.imageView.setImageAlpha(myimg);
        // holder.imageView.setImageDrawable(R.drawable.babusar);
    //    holder.dest_name.setText(mydest_name);
 //       holder.trans.setText(mytransport);
 //       holder.stay.setText(mystay);
 //       holder.price.setText(myprice);
        holder.stopNum.setText(stop);
        // holder.textView.setText(title);
    }
    @Override
    public int getItemCount() {
        // return data.length;
        return postData.size();
    }
    public class Form2PostViewHolder extends RecyclerView.ViewHolder{
        //TextView textView;
        EditText dest_name, trans, stay, price;
        TextView stopNum;

        public Form2PostViewHolder (View itemView)
        {
            super(itemView);
//            imageView = itemView.findViewById(R.id.imgIcon);
//            textView = itemView.findViewById(R.id.txtTitle);

            dest_name = itemView.findViewById(R.id.dest_name);
            trans = itemView.findViewById(R.id.transport_txtview);
            stay = itemView.findViewById(R.id.stay_txtview);
            price = itemView.findViewById(R.id.price_txtview);
            stopNum= itemView.findViewById(R.id.stopNum);

        }
    }

}
