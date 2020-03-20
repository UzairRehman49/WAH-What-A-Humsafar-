package com.example.wah;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TripPlan

        extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance)
    {
        View view =  inflater.inflate(R.layout.fragment_trip_plan,container,false);
        Button getPlan = view.findViewById(R.id.btnGetPlan);
        getPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),budget_plan.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
