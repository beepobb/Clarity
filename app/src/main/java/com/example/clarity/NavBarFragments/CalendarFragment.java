package com.example.clarity.NavBarFragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.example.clarity.MainActivity;
import com.example.clarity.R;
import com.example.clarity.adapters.CalendarEventAdapter;
import com.example.clarity.model.PreferenceUtils;
import com.example.clarity.model.data.Post;
import com.example.clarity.model.repository.RestRepo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import kotlin.jvm.functions.Function1;

/**
 * CalendarFragment handles all logic for Monthly view, which is the default view
 */
public class CalendarFragment extends Fragment {
    private static final String TAG = "CalendarFragment";

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1"; private static final String ARG_PARAM2 = "param2";

    private String mParam1; private String mParam2;
    private TextView aabbcc;
    private RecyclerView monthlyRecyclerView;
    private RecyclerView agendaRecyclerView;
    private CalendarEventAdapter adapterMonthly;
    private CalendarEventAdapter adapterAgenda;
    private com.applandeo.materialcalendarview.CalendarView calendarView;
    private TextView monthLabelTextView;
    private MutableLiveData<List<Post>> savedEventsList;
    private Calendar selectedDate;
    private RestRepo db;
    public enum CalendarDisplayState {MONTHLY_VIEW, AGENDA_VIEW}
    private CalendarDisplayState calendarDisplayState;
    private ImageView displayToggle; // to toggle between monthly view and agenda view
    private PreferenceUtils prefUtils;
    private String[] intToMonth = {"JANUARY", "FEBRUARY", "MARCH", "APRIL",
            "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"}; // Convert integer representation of month to String

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

        Log.i(TAG, "onCreate run");

        // Fetch database (RestRepo instance)
        Activity activity = getActivity();
        if (activity != null) {
            // Example: Accessing activity's method
            db = ((MainActivity) activity).database;
        }

        savedEventsList = new MutableLiveData<>(new ArrayList<>());
        prefUtils = PreferenceUtils.getInstance(getActivity());
        calendarDisplayState = CalendarDisplayState.MONTHLY_VIEW;
        selectedDate = Calendar.getInstance(); // get current date

        // INITIALIZE //
        // ONLY FOR TESTING: Sample code to (temporarily) save events to userPrefs local storage
        prefUtils.addToCalendar(13);
        prefUtils.addToCalendar(4);
        prefUtils.addToCalendar(1);
        // without prefUtils.commitCalendarUpdates(), these changes are not saved to local file

        // Fetch saved events from local storage (sharedPrefs):
        db.getPostsRequest(new ArrayList<Integer>(prefUtils.getCalendarPostIds()), new RestRepo.RepositoryCallback<ArrayList<Post>>() {
            @Override
            public void onComplete(ArrayList<Post> result) {

                // Update savedEventsList (Mutable Live Data containing Array List of Post objects)
                if (result != null) {
                    // TODO: Perhaps sort result (by start date) before storing?
                    savedEventsList.postValue(result); // postValue used as this will be executed on worker thread
                }
                // Observer will be notified - RecyclerView will update accordingly
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // get reference to all UI elements
        calendarView = view.findViewById(R.id.calendarView);
        aabbcc = view.findViewById(R.id.AABBCC);
        displayToggle = view.findViewById(R.id.displayToggle);
        monthlyRecyclerView = view.findViewById(R.id.monthlyRecycler);
        agendaRecyclerView = view.findViewById(R.id.agendaRecycler);
        monthLabelTextView = view.findViewById(R.id.monthLabel);

        // Set up RecyclerViews (Monthly and Agenda)
        monthlyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Linear Scroll
        adapterMonthly = new CalendarEventAdapter(getActivity(), calendarDisplayState);
        monthlyRecyclerView.setAdapter(adapterMonthly);

        // Agenda View: RecyclerView and adapter
        agendaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Linear Scroll
        adapterAgenda = new CalendarEventAdapter(getActivity(), CalendarDisplayState.AGENDA_VIEW);
        agendaRecyclerView.setAdapter(adapterAgenda);

        // Initialize month label
        monthLabelTextView.setText(intToMonth[selectedDate.get(Calendar.MONTH)] + " " + selectedDate.get(Calendar.YEAR));

        // Set up observer for savedEventsList (must be placed here because view objects are touched)
        savedEventsList.observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                // TODO: Update UI (RecyclerView) when data source is updated
                Log.d(TAG, "onChanged: Observer called (savedEventsList updated)");
                updateMonthlyRecycler();
                updateAgendaRecycler();
            }
        });

        // Precaution in case DB loads (savedEventsList updated) before observer is assigned.
        updateMonthlyRecycler();
        updateAgendaRecycler();

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
        Log.i(TAG, "onViewCreate");

        // set default view (monthly)
        showMonthlyView();

        calendarView.setOnCalendarDayClickListener(new OnCalendarDayClickListener() {
            @Override
            public void onClick(@NonNull CalendarDay calendarDay) {
                selectedDate = calendarDay.getCalendar();
                updateMonthlyRecycler(); // Only display events on selected day
            }
        });

        // Update month label in purple header bar on calendar page change
        calendarView.setOnForwardPageChangeListener(new calendarPageChangeListener());
        calendarView.setOnPreviousPageChangeListener(new calendarPageChangeListener());

        displayToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "displayToggle");

                // Switch between Monthly view and Agenda view (Toggles visibility of View objects)
                switch (calendarDisplayState) {
                    case AGENDA_VIEW: // Switch from agenda to monthly view
                        calendarDisplayState = CalendarDisplayState.MONTHLY_VIEW;
                        showMonthlyView();
                        break;
                    case MONTHLY_VIEW: // Switch from monthly to agenda view
                        calendarDisplayState = CalendarDisplayState.AGENDA_VIEW;
                        showAgendaView();
                        break;
                }

                Log.d("displayToggle", "Switched to: " + calendarDisplayState);
            }
        });
    }

    //***Helper functions***//

    // Update the Monthly Recycle view when there is new data or new selected date
    private void updateMonthlyRecycler() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH); // January is 0 (not 1)
        int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);
        aabbcc.setText(String.valueOf(dayOfMonth+"/"+ (month+1) +"/"+year));

        // Only display Events on selected date
        List<Post> eventList = new ArrayList<>();
        for (Post p: Objects.requireNonNull(savedEventsList.getValue())) {
            Calendar c = p.getEventStart();
            if (year == c.get(Calendar.YEAR) &&
                    month == c.get(Calendar.MONTH) &&
                    dayOfMonth == c.get(Calendar.DAY_OF_MONTH)) {
                eventList.add(p);
            }
        }
        adapterMonthly.updateEventList(eventList);
    }

    private void updateAgendaRecycler() {
        // TODO: sort by start date
        adapterAgenda.updateEventList(Objects.requireNonNull(savedEventsList.getValue()));
    }

    // sets date for Calendar object
    public static Calendar setDate(Calendar date, int day, int month, int year) {
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month - 1); // JANUARY is 0
        date.set(Calendar.DAY_OF_MONTH, day);
        Log.d("setDate", year+String.valueOf(date.get(Calendar.YEAR)));
        // seconds and milliseconds don't matter
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }
//    public void getDate() {
//        long date = calendarView.getDate();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy", Locale.getDefault());
//        calendar.setTimeInMillis(date);
//        String selected_date = simpleDateFormat.format(calendar.getTime());
//        Toast.makeText(getActivity(), selected_date, Toast.LENGTH_SHORT).show();
//    }

    /**
     * Hides Agenda view UI then display Monthly view UI
     */
    private void showMonthlyView() {
        // hide Agenda view UI
        agendaRecyclerView.setVisibility(View.GONE);

        // show Monthly view UI
        displayToggle.setImageResource(R.drawable.monthly_view);
        calendarView.setVisibility(View.VISIBLE);
        monthlyRecyclerView.setVisibility(View.VISIBLE);
        monthLabelTextView.setVisibility(View.VISIBLE);
        aabbcc.setVisibility(View.VISIBLE);
    }

    /**
     * Hides Monthly view UI then display Agenda view UI
     */
    private void showAgendaView() {
        // hide Monthly view UI
        calendarView.setVisibility(View.GONE);
        monthlyRecyclerView.setVisibility(View.GONE);
        aabbcc.setVisibility(View.GONE);
        monthLabelTextView.setVisibility(View.GONE);

        // show Agenda view UI
        displayToggle.setImageResource(R.drawable.agenda_view);
        agendaRecyclerView.setVisibility(View.VISIBLE);
    }

    // Listener for Calendar Page change (changing between months
    private class calendarPageChangeListener implements OnCalendarPageChangeListener {
        @Override
        public void onChange() {
            Calendar c = calendarView.getCurrentPageDate();
            monthLabelTextView.setText(intToMonth[c.get(Calendar.MONTH)] + " " + c.get(Calendar.YEAR));
        }
    }
}




