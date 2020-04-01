package com.ellen_savude_mpd.s1638842_ellen_savude.View;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.TextView;

import com.ellen_savude_mpd.s1638842_ellen_savude.Controller.ChannelPersister;
import com.ellen_savude_mpd.s1638842_ellen_savude.Controller.SearchLoadData;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.InflatorModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.Model.MappingModel;
import com.ellen_savude_mpd.s1638842_ellen_savude.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

// Name: Ellen Savude Ngatia
// Student ID: S1638842
//Search Fragment that shows parameters that the user is to enter so as to get the information

public class SearchFragment extends DialogFragment {

    private EditText origDateFrom,origDateTo, magnitude, location,edittitle, depth;
    private Button searchButton,backButton;
    private Calendar cal;
    private InflatorModel channel, filchannel;
    private Intent in;
    private Spinner category;
    private ChannelPersister p;

    public SearchFragment() {

    }

    public static SearchFragment newInstance(String title, ChannelPersister persister) {
        SearchFragment myfragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putSerializable("channel",persister.getChannel());
        args.putParcelable("persister",persister);
        myfragment.setArguments(args);
        return myfragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setLayoutParams(new LinearLayout.LayoutParams(500,500));

        String title = getArguments().getString("title", "Enter Search: ");
        channel = (InflatorModel) getArguments().getSerializable("channel");
        p = getArguments().getParcelable("persister");

        searchButton = view.findViewById(R.id.dialogButtonSearch);
        backButton = view.findViewById(R.id.dialogButtonBack);
        category = view.findViewById(R.id.editCat);
        magnitude = view.findViewById(R.id.editMag);
        location = view.findViewById(R.id.editLocation);
        depth = view.findViewById(R.id.editDepth);
        edittitle = view.findViewById(R.id.editTitle);

        List<String> array = new ArrayList<>();
        for(MappingModel i : channel.getItems()) {

            if(!array.contains(i.getCategory())) {
                array.add(i.getCategory());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(super.getContext(),R.layout.select, array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        backButton.setOnClickListener(new View.OnClickListener() { //On back button click.
            @Override
            public void onClick(View v) {
                SearchFragment.super.dismiss();
            }
        });

        cal = Calendar.getInstance();
        origDateFrom = view.findViewById(R.id.editOrigFrom);

        final DatePickerDialog.OnDateSetListener adf = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFormFrom();
            }
        };

        origDateFrom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog dp = new DatePickerDialog(getActivity(),adf,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });

        origDateTo = view.findViewById(R.id.editOrigTo);
        final DatePickerDialog.OnDateSetListener adt = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, month);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateFormTo();
            }
        };

        origDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dp = new DatePickerDialog(getActivity(),adt,cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
                dp.show();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filchannel = pipeFilter();
                if (filchannel.getItems().isEmpty()) {
                    AlertDialog memo = new AlertDialog.Builder(getActivity(),R.style.OtherDialog)
                            .setTitle("No Data")
                            .setMessage("No activity...")
                            .setNeutralButton("Cotinue",
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
                } else {
                    in = new Intent(getActivity(), SearchLoadData.class);
                    in.putExtra("filtered",filchannel);
                    in.putExtra("persister",p);
                    startActivity(in);
                }
            }
        });

        getDialog().setTitle(title);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        Window window = getDialog().getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
    }
    public void updateFormFrom() {
        String myFormat = "E, dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        origDateFrom.setText(sdf.format(cal.getTime()));
    }
    public void updateFormTo() {
        String myFormat = "E, dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        origDateTo.setText(sdf.format(cal.getTime()));
    }
    public InflatorModel pipeFilter() {
        InflatorModel filterc = new InflatorModel();
        filterc.setItems((LinkedList<MappingModel>) channel.getItems().clone());

        LinkedList<MappingModel> toRemove = new LinkedList<>();
        for(MappingModel i : filterc.getItems()) {
            boolean check = true;
            if(!category.getSelectedItem().toString().isEmpty()) {
                if (!i.getCategory().contains(category.getSelectedItem().toString())) {
                    check = false;
                }
            }

            if(!origDateFrom.getText().toString().isEmpty() && !origDateTo.getText().toString().isEmpty()) {
                if(i.getOriginDate().before(parseDate(origDateFrom.getText().toString() + " 00:00:00")) || i.getOriginDate().after(parseDate(origDateTo.getText().toString() + " 23:59:59"))) {
                    check = false;
                }
            } else if(!origDateFrom.getText().toString().isEmpty()) {
                if(i.getOriginDate().before(parseDate(origDateFrom.getText().toString() + " 00:00:00"))) {
                    check = false;
                }
            } else if(!origDateTo.getText().toString().isEmpty()) {
                if(i.getOriginDate().after(parseDate(origDateTo.getText().toString() + " 23:59:59"))) {
                    check = false;
                }
            }

            if(!magnitude.getText().toString().isEmpty()) {
                if(i.getMagnitude() != Double.parseDouble(magnitude.getText().toString())) {
                    check = false;
                }
            }

            if(!edittitle.getText().toString().isEmpty()) {
                if(!i.getTitle().toUpperCase().contains(edittitle.getText().toString().toUpperCase())) {
                    check = false;
                }
            }

            if(!depth.getText().toString().isEmpty()) {
                if(i.getDepth() != Integer.parseInt(depth.getText().toString())) {
                    check = false;
                }
            }

            if(!location.getText().toString().isEmpty()) {
                if(!i.getLocation().toUpperCase().contains(location.getText().toString().toUpperCase())) {
                    check = false;
                }
            }
            if(check) {
                toRemove.add(i);
            }
        }

        filterc.setItems(toRemove);
        return filterc;
    }
    private Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
