package com.ellen_savude_mpd.s1638842_ellen_savude.View;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.ellen_savude_mpd.s1638842_ellen_savude.Controller.ChannelPersister;
import com.ellen_savude_mpd.s1638842_ellen_savude.Controller.RssFeeder;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.InflatorModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.MappingModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;


// Name: Ellen Savude Ngatia
// Student ID: S1638842
//This class shows the view for the range of earthquakes being searched


public class DurationActivity extends AppCompatActivity {

    LinearLayout hold, todatelay;
    private static InflatorModel channel;
    private Intent intent;
    private Button search;
    private EditText fromDate, toDate;
    private Calendar myCal;
    private Button calculateButton;
    private int northid,southid,westid,eastid,largemagid,deepid,shallowid,id;
    private TextView northernly, southernly,westernly,easternly,shallow;
    private DatePickerDialog.OnDateSetListener dateFrom, dateTo;
    private TextView deep, largemag;
    ChannelPersister p;
    private Intent globalIntent;
    private MyUpdateReceiver uec;
    private IntentFilter ifi, ufi;
    private Thread t;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration);

        intent = getIntent();
      ChannelPersister  p = intent.getParcelableExtra("persister");
        channel = p.getChannel();
        id = 0;
        pb = findViewById(R.id.myProg);

        hold = findViewById(R.id.holdhide);
        todatelay= findViewById(R.id.todatelayout);
        toDate = findViewById(R.id.toDate);
        calculateButton = findViewById(R.id.calculateButton);

        hold.setVisibility(View.INVISIBLE);
        todatelay.setVisibility(View.INVISIBLE);
        calculateButton.setVisibility(View.INVISIBLE);




        myCal = Calendar.getInstance();
        dateFrom = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCal.set(Calendar.YEAR, year);
                myCal.set(Calendar.MONTH, month);
                myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCal.set(Calendar.HOUR,0);
                myCal.set(Calendar.MINUTE,0);
                myCal.set(Calendar.SECOND,0);
                updateLabelFrom();
            }
        };

        dateTo = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCal.set(Calendar.YEAR, year);
                myCal.set(Calendar.MONTH, month);
                myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                myCal.set(Calendar.HOUR,23);
                myCal.set(Calendar.MINUTE,59);
                myCal.set(Calendar.SECOND,59);
                updateLabelTo();
            }
        };

        fromDate = findViewById(R.id.fromDate);
        fromDate.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                todatelay.setVisibility(View.VISIBLE);

                DatePickerDialog dp = new DatePickerDialog(DurationActivity.this,dateFrom,myCal.get(Calendar.YEAR),myCal.get(Calendar.MONTH),myCal.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });


        toDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                calculateButton.setVisibility(View.VISIBLE);

                DatePickerDialog dp = new DatePickerDialog(DurationActivity.this,dateTo,myCal.get(Calendar.YEAR),myCal.get(Calendar.MONTH),myCal.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });


        intent = new Intent(this, InfoActivity.class);


        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hold.setVisibility(View.VISIBLE);
                runCalculations("fromButton");
            }
        });

        northernly = findViewById(R.id.northernly);
        southernly = findViewById(R.id.southernly);
        westernly = findViewById(R.id.westernly);
        easternly = findViewById(R.id.easternly);
        deep = findViewById(R.id.greatDepth);
        shallow = findViewById(R.id.minDepth);
        largemag = findViewById(R.id.greatMag);

        globalIntent = new Intent(this, RssFeeder.class);
        uec = new MyUpdateReceiver();
        ifi = new IntentFilter("");
        ufi = new IntentFilter("");
        registerReceiver(uec,ufi);
        if(!RssFeeder.isInstanceCreated()) {
        Thread t = new Thread() {
                public void run() {
                    startService(globalIntent);
                }
            };
            t.start();
            AlertDialog memo = new AlertDialog.Builder(this,R.style.OtherDialog)
                    .setTitle("Service Starting")
                    .setMessage("Service Starting...")
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

    @Override
    public void onResume() {
        super.onResume();
        runCalculations("fromResume");

    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void updateLabelFrom() {
        String myFormat = "E, dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        Log.e("Date",myCal.getTime().toString()); //Used to find out that it's using current time.
        fromDate.setText(sdf.format(myCal.getTime()));
    }

    private void updateLabelTo() {
        String myFormat = "E, dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        Log.e("Date",myCal.getTime().toString()); //Used to find out that it's using the current time.
        toDate.setText(sdf.format(myCal.getTime()));
    }

    private void runCalculations(String calling) {

        if((fromDate.getText().length() == 0 || toDate.getText().length() == 0) && calling.equals("fromButton")) {
            AlertDialog memo = new AlertDialog.Builder(DurationActivity.this,R.style.OtherDialog)
                    .setTitle("Date Error")
                    .setMessage(getResources().getString(R.string.dateentry))
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
            return;
        }


        if((fromDate.getText().length() == 0 || toDate.getText().length() == 0) && calling.equals("fromResume")) {

            return;
        }

        int counter = 0;
        double toplat = 0.0;
        double toplon = 0.0;
        double minlat = 0.0;
        double minlon = 0.0;
        double greatmag = 0.0;
        int deepest = 0;
        int shallowest = 0;

        LinkedList<MappingModel> filteredList = new LinkedList<>();

        for(MappingModel i : channel.getItems()) {
            if((i.getPubDate().after(parseDate(fromDate.getText().toString()))) && (i.getPubDate().before(parseDate(toDate.getText().toString())))) {
                filteredList.add(i);
            }
        }

        if(filteredList.size() == 0) {

            Toast.makeText(DurationActivity.this,getResources().getString(R.string.empty),Toast.LENGTH_SHORT).show();

            return;
        }
        for(MappingModel i : filteredList) {

            if(counter == 0) {
                toplat = i.getLat();
                greatmag = i.getMagnitude();
                toplon = i.getLon();
                minlat = i.getLat();
                minlon = i.getLon();
                deepest = i.getDepth();
                shallowest = i.getDepth();
            }
            if(i.getLat() > toplat) {
                northid = counter;
                toplat = i.getLat();
            }
            if(i.getLat() < minlat) {
                southid = counter;
                minlat = i.getLat();
            }
            if(i.getLon() > toplon) {
                eastid = counter;
                toplon = i.getLon();
            }
            if(i.getLon() < minlon) {
                westid = counter;
                minlon = i.getLon();
            }
            if(greatmag < i.getMagnitude()) {
                largemagid = counter;
                greatmag = i.getMagnitude();
            }
            if(deepest < i.getDepth()) {
                deepid = counter;
                deepest = i.getDepth();
            }
            if(shallowest > i.getDepth()) {
                shallowid = counter;
                shallowest = i.getDepth();
            }
            counter++;
        }

        northernly.setText(getResources().getString(R.string.northernmost,toplat,filteredList.get(northid).getTitle()));
        southernly.setText(getResources().getString(R.string.southernmost, minlat, filteredList.get(southid).getTitle()));
        westernly.setText(getResources().getString(R.string.westernmost,toplon, filteredList.get(westid).getTitle()));
        easternly.setText(getResources().getString(R.string.easternmost,minlon,filteredList.get(eastid).getTitle()));
        deep.setText(getResources().getString(R.string.deepest, deepest , filteredList.get(deepid).getTitle()));
        shallow.setText(getResources().getString(R.string.shallow,shallowest,filteredList.get(shallowid).getTitle()));
        largemag.setText(getResources().getString(R.string.largemag, greatmag,filteredList.get(largemagid).getTitle()));
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
            intent = new Intent(this,InfoActivity.class);
            intent.putExtra("ItemID",item.getItemId());
            intent.putExtra("persister",p);
            startActivity(intent);
        } else if(item.getTitle().toString().equals("View on Map")) {
            intent = new Intent(this, MapActivity.class);
            intent.putExtra("ItemID",item.getItemId());
            intent.putExtra("persister",p);
            startActivity(intent);
        }
        return true;
    }
}
