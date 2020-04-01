package com.ellen_savude_mpd.s1638842_ellen_savude.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ellen_savude_mpd.s1638842_ellen_savude.Model.InflatorModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.MappingModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.Controller.ChannelPersister;
import com.ellen_savude_mpd.s1638842_ellen_savude.R;

// Name: Ellen Savude Ngatia
// Student ID: S1638842
//This class shows Information of an individual class and how it is mapped on google maps

public class InfoActivity extends AppCompatActivity {

    private TextView title, description, link,pubDate,category,lat,lon,mag, dep,origDate, loc;
    private Button backButton;
    private ChannelPersister p;
    private InflatorModel c;
    private Intent in;
    private int selectedID;
    private MappingModel i;
    private Button mapView;
    private Intent mapIn;
    private InfoActivity.MyReceiver rec;
    private IntentFilter ifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        link = findViewById(R.id.link);
        pubDate = findViewById(R.id.pubDate);
        category = findViewById(R.id.category);
        lat = findViewById(R.id.lat);
        lon = findViewById(R.id.lon);
        backButton = findViewById(R.id.back);
        mapView = findViewById(R.id.mapView);
        mag = findViewById(R.id.mag);
        dep = findViewById(R.id.dep);
        origDate = findViewById(R.id.origDate);
        loc = findViewById(R.id.loc);

        in = getIntent();
        p = in.getParcelableExtra("persister");
        selectedID = in.getIntExtra("ItemID",0);
        in = new Intent(this, MainActivity.class);
        mapIn = new Intent(this, MapActivity.class);
        c = p.getChannel();
        i = c.getItems().get(selectedID);
        title.setText(getResources().getString(R.string.title,i.getTitle()));
        description.setText(getResources().getString(R.string.desc,i.getDescription()));
        link.setText(getResources().getString(R.string.link,i.getLink()));
        pubDate.setText(getResources().getString(R.string.publish, i.getPubDate().toString()));
        category.setText(getResources().getString(R.string.category, i.getCategory()));
        lat.setText(getResources().getString(R.string.lat, i.getLat()));
        lon.setText(getResources().getString(R.string.lon, i.getLon()));
        mag.setText(getResources().getString(R.string.magnitude, i.getMagnitude()));
        dep.setText(getResources().getString(R.string.depth, i.getDepth(), "km"));
        origDate.setText(getResources().getString(R.string.orig, i.getOriginDate().toString()));
        loc.setText(getResources().getString(R.string.location, i.getLocation()));

        backButton.setOnClickListener(new View.OnClickListener() {          //Programmatically add the onclick listener to pack persister to the intent
            @Override
            public void onClick(View v) {
                in.putExtra("persister",p);
                startActivity(in);
            }
        });

        mapView.setOnClickListener(new View.OnClickListener() {            //As above
            @Override
            public void onClick(View v) {
                mapIn.putExtra("persister",p);
                mapIn.putExtra("ItemID",selectedID);
                startActivity(mapIn);
            }
        });
        rec = new InfoActivity.MyReceiver();
        ifi = new IntentFilter("");
        registerReceiver(rec,ifi);
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
            Toast.makeText(InfoActivity.this,"Refreshing",Toast.LENGTH_SHORT).show();
        }
    }
}
