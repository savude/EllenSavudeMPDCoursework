package com.ellen_savude_mpd.s1638842_ellen_savude.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.ellen_savude_mpd.s1638842_ellen_savude.View.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.InflatorModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.MappingModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.R;


import java.util.ArrayList;
import java.util.List;

// Name: Ellen Savude Ngatia
// Student ID: S1638842
// Searches the loaded parameters to the google map

public class SearchLoadData extends AppCompatActivity implements OnMapReadyCallback {

    private Intent in;
    private SearchLoadData.MyReceiver rec;
    private IntentFilter ifi;
    private Spinner selectRecord;
    private InflatorModel channel;
    SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private LatLng origarea;
    private MappingModel selectedItem;
    private Button backButton;
    private boolean mapready;
    private ChannelPersister p;
    private LinearLayout info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        selectRecord = findViewById(R.id.selectRecord);
        info = findViewById(R.id.showInfo);
        mapready = false;

        in = getIntent();
        channel = (InflatorModel)in.getSerializableExtra("filtered");
        p = in.getParcelableExtra("persister");
        List<String> array = new ArrayList<>();
        for(MappingModel i : channel.getItems()) {
            array.add(i.getOriginDate() + " - " + i.getLocation());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.select, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectRecord.setAdapter(adapter);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapInfo);
        mapFragment.getMapAsync(this);

        selectRecord.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                selectedItem = channel.getItems().get(position);
                origarea = new LatLng(selectedItem.getLat(),selectedItem.getLon());

                Thread thread = new Thread() {
                    public void run() {
                        while(!mapready) {
                            try { Thread. sleep(2000); }
                            catch (InterruptedException e) {

                            }
                        }
                        runOnUiThread(new Runnable() {

                            /**
                             * Run method of runnable runs as the first point of the thread.
                             */
                            @Override
                            public void run() {
                                mMap.clear();
                                mMap.addMarker(new MarkerOptions().position(origarea).title("Earthquake on: " + selectedItem.getPubDate().toString()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                mMap.moveCamera(CameraUpdateFactory.zoomTo(Float.parseFloat("12.0")));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(origarea));
                                mMap.getUiSettings().setZoomControlsEnabled(true);
                                mMap.getUiSettings().setScrollGesturesEnabled(false);
                            }
                        });
                    }
                };

                thread.start();

                TextView title = findViewById(R.id.title);
                title.setText(getResources().getString(R.string.title,selectedItem.getTitle()));
                title.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                title.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

                TextView link = findViewById(R.id.link);
                link.setText(getResources().getString(R.string.link,selectedItem.getLink()));
                link.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                link.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

                TextView cat = findViewById(R.id.cat);
                cat.setText(getResources().getString(R.string.category, selectedItem.getCategory()));
                cat.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                cat.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

                TextView lat = findViewById(R.id.lat);
                lat.setText(getResources().getString(R.string.lat, selectedItem.getLat()));
                lat.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                lat.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

                TextView lon = findViewById(R.id.lon);
                lon.setText(getResources().getString(R.string.lon, selectedItem.getLon()));
                lon.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                lon.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

                TextView origin = findViewById(R.id.origin);
                origin.setText(getResources().getString(R.string.orig, selectedItem.getOriginDate().toString()));
                origin.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                origin.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

                TextView loc = findViewById(R.id.loc);
                loc.setText(getResources().getString(R.string.location, selectedItem.getLocation()));
                loc.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                loc.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

                TextView mag = findViewById(R.id.mag);
                mag.setText(getResources().getString(R.string.magnitude,selectedItem.getMagnitude()));
                mag.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                mag.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);

                TextView dep = findViewById(R.id.dep);
                dep.setText(getResources().getString(R.string.depth, selectedItem.getDepth(),"km"));
                dep.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                dep.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            /**
             * The onclicklistener interface defines that the onclick event must be defined. This is done
             * anonymously so is used to define what happens when the above view is change.
             * @param v The view from which the on click event was called. Not necessary as this is
             *          anonymous and only relevant to the view above.
             */
            @Override
            public void onClick(View v) {
                in = new Intent(SearchLoadData.this, MainActivity.class);
                in.putExtra("persister",p);
                startActivity(in);
            }
        });

        rec = new SearchLoadData.MyReceiver();
        ifi = new IntentFilter("Refresh");
        registerReceiver(rec,ifi);
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(rec);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mapready = true;

    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(rec,ifi);
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            AlertDialog memo = new AlertDialog.Builder(SearchLoadData.this,R.style.OtherDialog)
                    .setTitle("Refresh")
                    .setMessage(getResources().getString(R.string.refresh))
                    .setNeutralButton("Ok",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
            TextView dialogMessage = memo.findViewById(android.R.id.message);
            dialogMessage.setTextSize(TypedValue.COMPLEX_UNIT_DIP,20);
            Button neut = memo.getButton(AlertDialog.BUTTON_NEUTRAL);
            LinearLayout lp = (LinearLayout) neut.getParent();
            Space space = (Space) lp.getChildAt(1);
            space.setVisibility(View.GONE);
            lp.setGravity(Gravity.CENTER);
        }
    }
}
