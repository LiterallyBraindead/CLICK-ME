package com.wguprojects.testingroomnestedrelationships;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private TermViewModel termViewModel;
    private FloatingActionButton fab;
    BottomNavigationView bottomNavigationView;

    Boolean addEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannel();
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.term_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        TermAdapter adapter = new TermAdapter();
        recyclerView.setAdapter(adapter);

        fab = findViewById(R.id.main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addEdit = true;

               Bundle bundle = new Bundle();
               bundle.putBoolean("addEdit", addEdit);

               AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
               AddEditTermFragment addEditTermFragment = new AddEditTermFragment();
               addEditTermFragment.setArguments(bundle);
               appCompatActivity.getSupportFragmentManager().beginTransaction()
                       .replace(R.id.main_scroll_container, addEditTermFragment)
                       .addToBackStack(null).commit();
            }
        });


        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        termViewModel.getAllTerms().observe(this, terms -> {
            if(terms != null) {
                adapter.setTerms(terms);

                System.out.println("Main Activity Term Size: " + terms.size());
            }
        });

        //NOTIFICATIONS FOR COURSES AND ASSESSMENTS
        termViewModel.getAllCourses().observe(this, allCourses ->{
            if (allCourses != null){

                Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                for (int i = 0; i< allCourses.size(); i++){

                    LocalDateTime startTime = allCourses.get(i).getStart_date().minusMinutes(5);
                    ZonedDateTime startZonedDateTime = ZonedDateTime.of(startTime, ZoneId.systemDefault());
                    long startDate = startZonedDateTime.toInstant().toEpochMilli();
                    System.out.println(startDate + " " + (LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()));
                    if (startZonedDateTime.isAfter(ZonedDateTime.now())){
                        alarmManager.set(AlarmManager.RTC_WAKEUP, startDate, pendingIntent);
                    }

                    LocalDateTime endTime = allCourses.get(i).getEnd_date().minusMinutes(5);
                    ZonedDateTime endZonedDateTime = ZonedDateTime.of(endTime, ZoneId.systemDefault());
                    long endDate = endZonedDateTime.toInstant().toEpochMilli();
                    if (endZonedDateTime.isAfter(ZonedDateTime.now())){
                        alarmManager.set(AlarmManager.RTC_WAKEUP, endDate, pendingIntent);
                    }

                }


            }
        });
        termViewModel.getAllAssessments().observe(MainActivity.this, allAssessment ->{
            if (allAssessment != null) {

                Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                for (int i = 0; i< allAssessment.size(); i++){

                    LocalDateTime startTime = allAssessment.get(i).getAssessment_start_date().minusMinutes(5);
                    ZonedDateTime startZonedDateTime = ZonedDateTime.of(startTime, ZoneId.systemDefault());
                    long startDate = startZonedDateTime.toInstant().toEpochMilli();
                    alarmManager.set(AlarmManager.RTC_WAKEUP,
                            startDate,
                            pendingIntent);

                    LocalDateTime endTime = allAssessment.get(i).getAssessment_end_date().minusMinutes(5);
                    ZonedDateTime endZonedDateTime = ZonedDateTime.of(endTime, ZoneId.systemDefault());
                    long endDate = endZonedDateTime.toInstant().toEpochMilli();
                    alarmManager.set(AlarmManager.RTC_WAKEUP, endDate, pendingIntent);

                }
            }
        });



        adapter.setOnItemClickListener(new TermAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Entities.Term term) {
                Intent intent = new Intent(MainActivity.this, CoursesActivity.class);
                Repository.setTermID(term.getTerm_id());
                intent.putExtra(CoursesActivity.EXTRA_ID, term.getTerm_id());


                startActivity(intent);
            }
        });

        adapter.setOnLongItemClickListener(new TermAdapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(Entities.Term term) {

                Repository.setTermID(term.getTerm_id());
                int id = term.getTerm_id();
                addEdit = false;
                Bundle editBundle = new Bundle();
                editBundle.putBoolean("addEdit", addEdit);
                editBundle.putInt("term_id", id);

                System.out.println("term id is: " + id);
                System.out.println("addEdit is: " + addEdit);

                AddEditTermFragment addEditTermFragment = new AddEditTermFragment();
                addEditTermFragment.setArguments(editBundle);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int tempPosition = adapter.getTermAt(viewHolder.getAdapterPosition()).getTerm_id();
                Entities.Term term = adapter.getTermAt(viewHolder.getAdapterPosition());
                termViewModel.getAllCourses().observe(MainActivity.this, allCourses ->{
                    for (int i = 0; i < allCourses.size(); i++){
                        if (allCourses.get(i).getAssociated_term_id() == tempPosition){
                            Toast.makeText(getApplicationContext(), "Terms that have associated courses cannot be deleted!", LENGTH_SHORT).show();
                            return;
                        }
                    }
                    termViewModel.deleteTerm(term);
                });
                adapter.notifyDataSetChanged();
            }
        }).attachToRecyclerView(recyclerView);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.ic_schedule:
                        Intent intent = new Intent(MainActivity.this, Scheduler.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "CourseReminderChannel";
            String description = "Channel for Course Reminder";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("courseChannel", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }
}