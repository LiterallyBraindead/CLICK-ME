package com.wguprojects.testingroomnestedrelationships;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Collections;
import java.util.Comparator;

public class Scheduler extends AppCompatActivity {
    TermViewModel termViewModel;
    Button buttonNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);

        RecyclerView recyclerView = findViewById(R.id.schedule_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        ScheduleAdapter adapter = new ScheduleAdapter();
        recyclerView.setAdapter(adapter);

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        termViewModel.getAllSchedules().observe(Scheduler.this, allSchedules ->{
            adapter.setAllSchedules(allSchedules);
        });

        adapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Entities.Schedule schedule) {
                Intent intent = new Intent(Scheduler.this, ScheduleDetailsActivity.class);
                intent.putExtra("schedule_id", schedule.getSchedule_id());
                startActivity(intent);
            }
        });

        buttonNew = findViewById(R.id.new_button);
        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Scheduler.this, AddNewSchedule.class);
                startActivity(intent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int positionId = adapter.getScheduleAt(viewHolder.getAdapterPosition()).getSchedule_id();
                Entities.Schedule schedule = adapter.getScheduleAt(viewHolder.getAdapterPosition());
                termViewModel.getAllSchedules().observe(Scheduler.this, allSchedules ->{
                    for(int i = 0; i < allSchedules.size(); i++){
                        if(allSchedules.get(i).getSchedule_id() == positionId){
                            termViewModel.deleteSchedule(allSchedules.get(i));
                        }
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);
    }
}