package com.example.clarity.NavBarFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.clarity.R;
import com.example.clarity.adapters.CalendarEventAdapter;
import com.example.clarity.model.Event; // PLACEHOLDER for data source

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * CalendarFragment handles all logic for Monthly view, which is the default view
 */
public class CalendarFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1"; private static final String ARG_PARAM2 = "param2";

    private String mParam1; private String mParam2;
    private Calendar calendar;
    private CalendarView calendarView;
    private TextView aabbcc;
    private TextView textView2;
    private RecyclerView recyclerView;
    private List<Event> eventList = new ArrayList<>(); // placeholder Event list for data source
    enum CalendarDisplayState {MONTHLY_VIEW, AGENDA_VIEW}
    private CalendarDisplayState calendarDisplayState;
    private ImageView displayToggle; // to toggle between monthly view and agenda view

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Calendar.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // stores arguments passed to Fragment,
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Log.d("CalendarFragment", "onCreate run");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // get reference to all UI elements
        calendarView = view.findViewById(R.id.calendarView);
        aabbcc = view.findViewById(R.id.AABBCC);
        recyclerView = view.findViewById(R.id.recyclerView);
        calendar = Calendar.getInstance(); // calendar is like datetime in python
        displayToggle = view.findViewById(R.id.displayToggle);
        textView2 = view.findViewById(R.id.textView2);

        // other variables
        calendarDisplayState = CalendarDisplayState.MONTHLY_VIEW;

        return view; // Inflate the layout for this fragment
    }

    /**
     * view lookups and attaching view listeners done here
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // onViewCreated is executed after onCreateView
        super.onViewCreated(view, savedInstanceState);
        Log.i("CalendarFragment", "onViewCreate");

        setDate(1,1,2023);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                aabbcc.setText(String.valueOf(dayOfMonth+"/"+ (month + 1) +"/"+year));
                Log.d("CalendarFragment", "date set");
            }
        });

        displayToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("CalendarFragment", "displayToggle");

                switch (calendarDisplayState) {
                    case AGENDA_VIEW:
                        displayToggle.setImageResource(R.drawable.monthly_view);
                        calendarDisplayState = CalendarDisplayState.MONTHLY_VIEW;
                        break;
                    case MONTHLY_VIEW:
                        displayToggle.setImageResource(R.drawable.agenda_view);
                        calendarDisplayState = CalendarDisplayState.AGENDA_VIEW;

                        showAgendaView();
                        break;
                }

                Log.d("displayToggle", "value is: " + calendarDisplayState);
            }
        });
        // RECYCLER VIEW SET-UP
        // Placeholder for data source
        eventList.add(new Event("UPOP", "1300-1500", "Think Tank 3"));
        eventList.add(new Event("ML Workshop", "1100-1900", "Classroom 1"));
        eventList.add(new Event("Career Fair", "1500-2000", "Student Centre"));
        eventList.add(new Event("Lame Event", "1200-1300", "Somewhere"));
        eventList.add(new Event("Toilet Break", "1500-1501", "Toilet"));

        // Set up RecyclerView and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Linear Scroll
        CalendarEventAdapter adapter = new CalendarEventAdapter(getActivity(), eventList);
        recyclerView.setAdapter(adapter);

    }

    // Helper functions:
    public void setDate(int day, int month, int year) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // JANUARY is 0
        calendar.set(Calendar.DAY_OF_MONTH, day);
        long milli = calendar.getTimeInMillis();
        calendarView.setDate(milli);
        Log.d("CalendarFragment.setDate", calendar.toString());
    }
    public void getDate() {
        long date = calendarView.getDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
        calendar.setTimeInMillis(date);
        String selected_date = simpleDateFormat.format(calendar.getTime());
        Toast.makeText(getActivity(), selected_date, Toast.LENGTH_SHORT).show();
    }

    // TODO: create UI for Agenda view and method to hide it
    /**
     * Hides Agenda view UI then display Monthly view UI
     */
    public void showMonthlyView() {
        // hide Agenda view UI

        // show Monthly view UI


    }

    /**
     * Hides Monthly view UI then display Agenda view UI
     */
    public void showAgendaView() {
        // hide Monthly view UI
        calendarView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        aabbcc.setVisibility(View.GONE);

        // show Agenda view UI
        textView2.setVisibility(View.VISIBLE);
    }
}

