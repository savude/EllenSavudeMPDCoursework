package com.ellen_savude_mpd.s1638842_ellen_savude.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ellen_savude_mpd.s1638842_ellen_savude.Controller.ChannelPersister;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.MappingModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.R;

// Name: Ellen Savude Ngatia
// Student ID: S1638842
// This class is responsible for connecting google maps coodinates and those being parsed from the xml

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Intent in,  i;
    private ChannelPersister p;
    private int id;
    private MappingModel item;
    private Button infoButton;
    LatLng area;
    LatLng origarea;
    SupportMapFragment mapFragment;
    private MyReceiver rec;
    private IntentFilter ifi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        in = getIntent();
        i = new Intent(this, InfoActivity.class);
        p = in.getParcelableExtra("persister");
        id = in.getIntExtra("ItemID",0);
        item = p.getChannel().getItems().get(id);
        infoButton = findViewById(R.id.back);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i.putExtra("persister",p);
                i.putExtra("ItemID",id);
                startActivity(i);
            }
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        rec = new MyReceiver();
        ifi = new IntentFilter("");
        registerReceiver(rec,ifi);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        origarea = new LatLng(item.getLat(),item.getLon());
        double radius;
        mMap.addMarker(new MarkerOptions().position(origarea).title("Earthquake on: " + item.getPubDate().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(Float.parseFloat("12.0")));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(origarea));
        for(MappingModel i:p.getChannel().getItems()) {
            area = new LatLng(i.getLat(),i.getLon());
            double mag = i.getMagnitude();
            radius = mag * 1200;
            if(radius < 0) {
                radius = radius * -1;
            }
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(rec);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(rec,ifi);
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(MapActivity.this,"Refreshing",Toast.LENGTH_SHORT).show();


        }
    }
}
