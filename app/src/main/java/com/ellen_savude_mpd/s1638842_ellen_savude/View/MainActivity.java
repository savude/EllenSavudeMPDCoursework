package com.ellen_savude_mpd.s1638842_ellen_savude.View;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ellen_savude_mpd.s1638842_ellen_savude.Controller.ChannelPersister;
import com.ellen_savude_mpd.s1638842_ellen_savude.Controller.RssFeeder;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.DataLoader;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.InflatorModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Name: Ellen Savude Ngatia
// Student ID: S1638842
// This class shows a list of buttons for the user to choose from to access data

public class MainActivity extends AppCompatActivity {

    private static InflatorModel channel;
    private Intent in;
    private Button search,recent,duration;
    private int id = 0;
    private ChannelPersister p;
    private Intent globalIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recent = findViewById(R.id.recent);
        duration = findViewById(R.id.duration);
        search = findViewById(R.id.searchb);

        in = getIntent();
        p = in.getParcelableExtra("persister");
        channel = p.getChannel();
        id = 0;



        recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Loading...",Toast.LENGTH_SHORT).show();
                DataLoader dl = new DataLoader();
                channel = dl.getLoadedChannel();
                p = new ChannelPersister(channel);
             Intent   i = new Intent(MainActivity.this, RecentEarthQuakeActivity.class);
                i.putExtra("persister",p);
                startActivity(i);
            }
        });

        duration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"Loading...",Toast.LENGTH_SHORT).show();
                DataLoader dl = new DataLoader();
                channel = dl.getLoadedChannel();
                p = new ChannelPersister(channel);
                Intent   i = new Intent(MainActivity.this, DurationActivity.class);
                i.putExtra("persister",p);
                startActivity(i);
            }
        });



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                SearchFragment searchFrag = SearchFragment.newInstance(getResources().getString(R.string.searchtitle),p);
                searchFrag.show(fm, "search_fragment");
            }
        });

        in = new Intent(this, InfoActivity.class);



        globalIn = new Intent(this, RssFeeder.class);

        if(!RssFeeder.isInstanceCreated()) {
        Thread t = new Thread() {
                public void run() {
                    startService(globalIn);
                }
            };
            t.start();
            Toast.makeText(MainActivity.this,"Loading Feeds...",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("E, dd MMM yyyy").parse(dateString); //Parse date object in the provided format.
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InflatorModel getChannel() {
        return channel;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        SpannableString titleformatted = new SpannableString(getResources().getString(R.string.selectid) + String.valueOf(v.getId()));
        titleformatted.setSpan(new TextAppearanceSpan(this, R.style.ContextFont), 0, (getResources().getString(R.string.selectid) + String.valueOf(v.getId())).length(),0);
        menu.setHeaderTitle(titleformatted);
        menu.add(0, v.getId(), 0, getResources().getString(R.string.home));
        menu.add(0, v.getId(), 0, getResources().getString(R.string.locate));
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().toString().equals("Info Screen")) {
            in = new Intent(this,InfoActivity.class);
            in.putExtra("ItemID",item.getItemId());
            in.putExtra("persister",p);
            startActivity(in);
        } else if(item.getTitle().toString().equals("View on Map")) {
            in = new Intent(this, MapActivity.class);
            in.putExtra("ItemID",item.getItemId());
            in.putExtra("persister",p);
            startActivity(in);
        }
        return true;
    }
}
