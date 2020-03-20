package com.example.wah;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public interface NotifyPumpsFetched {

    public void notifyPumpsFetched(ArrayList<LatLng> x);
}
