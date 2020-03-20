package com.example.wah;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
        private CardView swatCard, ayubiaCard, chitralCard;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance)
    {

        View view= inflater.inflate(R.layout.fragment_home, container, false);


        swatCard= (CardView) view.findViewById(R.id.swatCard);
        ayubiaCard= (CardView) view.findViewById(R.id.ayubiaCard);
        chitralCard= (CardView) view.findViewById(R.id.chitralCard);


        swatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Intent i= new Intent(getActivity(), place1.class);
                startActivity(i);
            }
        });

        ayubiaCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getActivity(), place2.class);
                startActivity(i);
            }
        });

        chitralCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getActivity(), place3.class);
                startActivity(i);
            }
        });

//        swatCard.setOnClickListener(this);
//        ayubiaCard.setOnClickListener(this);
//        chitralCard.setOnClickListener(this);

     //   Toast.makeText(getActivity(), "ON Createeeee", Toast.LENGTH_LONG).show();

        return view;


    }
//
//    @Override
//    public void onClick(View v) {
//        Intent i;
//
//        Toast.makeText(getActivity(), "ON CLICKKKKK", Toast.LENGTH_LONG).show();
//        switch (v.getId())
//        {
//
//            case R.id.swatCard : i= new Intent(getActivity(), place1.class);
//            startActivity(i);
//            break;
//
//            case R.id.ayubiaCard : i= new Intent(getActivity(), place2.class);
//            startActivity(i);
//            break;
//
//            case R.id.chitralCard : i= new Intent(getActivity(), place3.class);
//            startActivity(i);
//            break;
//
//            default:break;
//
//        }
//
//
//    }
}
