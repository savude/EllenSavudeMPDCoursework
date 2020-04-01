package com.ellen_savude_mpd.s1638842_ellen_savude.View;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ellen_savude_mpd.s1638842_ellen_savude.Controller.ChannelPersister;
import com.ellen_savude_mpd.s1638842_ellen_savude.Controller.RssFeeder;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.InflatorModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.MappingModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.DataLoader;
import com.ellen_savude_mpd.s1638842_ellen_savude.R;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
// Name: Ellen Savude Ngatia
// Student ID: S1638842
//This class shows all the recent earthquakes within the past 100 days

public class RecentEarthQuakeActivity extends AppCompatActivity {

    private LinearLayout holder;
    private static InflatorModel channel;
    private Intent in;
    private int id = 0;
    private ChannelPersister p;
    private Intent globalIn;
    private MyReceiver rec;
    private MyUpdateReceiver uec;
    private IntentFilter ifi;
    private IntentFilter ufi;
    private ProgressBar pb;
    private Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        holder = findViewById(R.id.selectionDisplay);
        in = getIntent();
        p = in.getParcelableExtra("persister");
        channel = p.getChannel();
        id = 0;
        pb = findViewById(R.id.myProg);
        for(MappingModel i:channel.getItems()) {
            Button b = new Button(this);
            b.setText(i.getDescription());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.setMargins(8,5,8,5);
            b.setLayoutParams(param);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            b.setId(id);
            b.setOnClickListener(new View.OnClickListener() {   //Set the listener anonymously for each button.

                @Override
                public void onClick(View v) {
                    DataLoader dl = new DataLoader();
                    channel = dl.getLoadedChannel();
                    p = new ChannelPersister(channel);
                    in.putExtra("ItemID",v.getId());
                    in.putExtra("persister",p);
                    startActivity(in);
                }
            });
            registerForContextMenu(b);
            holder.addView(b);
            id++;
        }

        if(holder.getChildCount() == 0) {
            Button b = new Button(this);
            b.setText("No Data found, check connection and click here to force update.");
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.setMargins(8,5,8,5);
            b.setLayoutParams(param);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            b.setBackground(getDrawable(R.drawable.button));
            b.setId(id);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataLoader dl = new DataLoader();
                    channel = dl.getLoadedChannel();
                    p = new ChannelPersister(channel);
                    in.putExtra("persister",p);
                    updateData(in);
                }
            });
            holder.addView(b);
        }




        in = new Intent(this, InfoActivity.class);



        globalIn = new Intent(this, RssFeeder.class);
        rec = new MyReceiver();
        uec = new MyUpdateReceiver();
        ifi = new IntentFilter("");
        ufi = new IntentFilter("");
        registerReceiver(rec,ifi);
        registerReceiver(uec,ufi);

        if(!RssFeeder.isInstanceCreated()) {
            Thread t = new Thread() {
                public void run() {
                    startService(globalIn);
                }
            };
            t.start();
            Toast.makeText(RecentEarthQuakeActivity.this,"Loading...",Toast.LENGTH_SHORT).show();
        }
    }


    private void updateData(Intent intent) {
        p = intent.getParcelableExtra("persister");
        channel = p.getChannel();
        holder.removeAllViews();
        id = 0;
        for(MappingModel i:channel.getItems()) {
            Button b = new Button(this);
            b.setText(i.getDescription());
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.setMargins(8,5,8,5);
            b.setLayoutParams(param);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            b.setId(id);
            b.setBackground(getDrawable(R.drawable.button));
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    in.putExtra("ItemID",v.getId());
                    in.putExtra("persister",p);
                    startActivity(in);
                }
            });
            registerForContextMenu(b);
            holder.addView(b);
            id++;
        }

        if(holder.getChildCount() == 0) {
            Button b = new Button(this);
            b.setText("No Data found, check connection and click here to force update.");
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            param.setMargins(8,5,8,5);
            b.setLayoutParams(param);
            b.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            b.setBackground(getDrawable(R.drawable.button));
            b.setId(id);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataLoader dl = new DataLoader();
                    channel = dl.getLoadedChannel();
                    p = new ChannelPersister(channel);
                    in.putExtra("persister",p);
                    updateData(in);
                }
            });
            holder.addView(b);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(rec,ifi);
        registerReceiver(uec,ufi);
    }
    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(rec);
        unregisterReceiver(uec);
    }
    private Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("E, dd MMM yyyy").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static InflatorModel getChannel() {
        return channel;
    }

    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(RecentEarthQuakeActivity.this,"Refreshing...",Toast.LENGTH_SHORT).show();

            updateData(intent);
        }
    }

    public class MyUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String temp = intent.getStringExtra("method");
            int tempnum = 0;
            try {
                tempnum = Integer.parseInt(temp);
            } catch (NumberFormatException e) {
                Log.e("MyTag","DoNothing");
            }
            if(temp.equals("start")) {
                pb.setProgress(0);
                pb.setVisibility(View.VISIBLE);
            } else {
                pb.setProgress(Integer.parseInt(temp));
            }
            if(tempnum == 100) {
                pb.setVisibility(View.GONE);
            }
        }
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
