package com.wguprojects.testingroomnestedrelationships;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AssessmentActivity extends AppCompatActivity {

    private TermViewModel termViewModel;
    public static final String EXTRA_ID = "com.wguprojects.testingroomnestedrelationships.EXTRA_ID";

    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        floatingActionButton = findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                AssessmentAddFragment assessmentAddFragment = new AssessmentAddFragment();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.assessment_scroll, assessmentAddFragment)
                .addToBackStack(null).commit();
            }
        });

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.assessment_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        AssessmentAdapter adapter = new AssessmentAdapter();
        recyclerView.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            termViewModel.getAllCourseWithAssessments().observe(this, courseWithAssessments -> {
                if (courseWithAssessments.size() <= 0)
                    finish();
                System.out.println("The selected course has " + courseWithAssessments.size() + " assessments!");
                adapter.setAllAssessment(courseWithAssessments);
            });
        }

        adapter.setOnLongClickListener(new AssessmentAdapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(Entities.Assessment assessment) {

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder builder = new AlertDialog.Builder(recyclerView.getContext());
                builder.setTitle(adapter.getAssessmentAt(viewHolder.getAdapterPosition()).getAssessment_title());
                builder.setMessage("Are you sure you want to delete the selected assignment?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        termViewModel.deleteAssessment(adapter.getAssessmentAt(viewHolder.getAdapterPosition()));
                    }
                });
                builder.setNegativeButton("No", null);
                AlertDialog deleteDialog = builder.create();
                deleteDialog.show();
                adapter.notifyDataSetChanged();

            }
        }).attachToRecyclerView(recyclerView);

    }




}