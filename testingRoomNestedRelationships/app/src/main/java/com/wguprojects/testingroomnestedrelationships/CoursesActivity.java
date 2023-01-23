package com.wguprojects.testingroomnestedrelationships;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.Comparator;

public class CoursesActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "com.wguprojects.testingroomnestedrelationships.EXTRA_ID";
    private TermViewModel termViewModel;

    private FloatingActionButton floatingActionButton;

    Boolean addOrEditCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.course_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        CourseAdapter adapter = new CourseAdapter();
        recyclerView.setAdapter(adapter);


        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            termViewModel.getAllTermWithCourses().observe(this, termWithCourses -> {
                System.out.println("termWithCourses Size is: " + termWithCourses.size());

                Collections.sort(termWithCourses.get(0).courses, Comparator.comparing(Entities.Course:: getStart_date));
                adapter.setAllCourses(termWithCourses);

            });
        }


        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Entities.Course course) {
                Intent intentAssessment = new Intent(CoursesActivity.this, AssessmentActivity.class);
                Repository.setCourseID(course.getCourse_id());
                System.out.println("Course Id: " + course.getCourse_id());
                intentAssessment.putExtra(CoursesActivity.EXTRA_ID, course.getCourse_id());

                startActivity(intentAssessment);
            }
        });

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addOrEditCourse = true;

                Bundle bundle = new Bundle();
                bundle.putBoolean("addEdit", addOrEditCourse);

                AppCompatActivity activity= (AppCompatActivity) view.getContext();
                AddEditCourseFragment addEditCourseFragment = new AddEditCourseFragment();
                addEditCourseFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.scroll_container, addEditCourseFragment)
                        .addToBackStack(null).commit();
            }
        });

        adapter.setOnLongItemClickListener(new CourseAdapter.OnLongItemClickListener() {
            @Override
            public void onLongItemClick(Entities.Course course) {
                addOrEditCourse = false;
                Bundle bundle = new Bundle();
                bundle.putBoolean("addEdit", addOrEditCourse);
                bundle.putInt("course_id", course.getCourse_id());

                AddEditCourseFragment addEditCourseFragment = new AddEditCourseFragment();
                addEditCourseFragment.setArguments(bundle);

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
                builder.setTitle(adapter.getCourseAt(viewHolder.getAdapterPosition()).getCourse_title());
                builder.setMessage("Are you sure you want to delete the selected course?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        termViewModel.deleteCourse(adapter.getCourseAt(viewHolder.getAdapterPosition()));
                        termViewModel.getAllAssessments().observe(CoursesActivity.this, allAssessment ->{
                            for(int j = 0; j < allAssessment.size(); j ++){
                                if (allAssessment.get(j).getAssociated_course_id() == adapter.getCourseAt(viewHolder.getAdapterPosition()).getCourse_id()){
                                    termViewModel.deleteAssessment(allAssessment.get(j));
                                }
                            }
                        });
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