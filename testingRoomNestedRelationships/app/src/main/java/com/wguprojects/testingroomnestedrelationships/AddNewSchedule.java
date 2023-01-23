package com.wguprojects.testingroomnestedrelationships;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class AddNewSchedule extends AppCompatActivity {

    private TermViewModel termViewModel;
    private ArrayList<Entities.Term> addedTermsList = new ArrayList<>();
    Button saveButton;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_schedule);

        saveButton = findViewById(R.id.save_term_button);
        editText = findViewById(R.id.edit_text_title);

        RecyclerView recyclerView = findViewById(R.id.terms_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        TermAdapter adapter = new TermAdapter();
        recyclerView.setAdapter(adapter);

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

        termViewModel.getAllTerms().observe(this, allTerms -> {

            if (allTerms != null){
                adapter.setTerms(allTerms);
            }
        });

        RecyclerView addRecyclerView = findViewById(R.id.added_terms_recycler_view);
        addRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addRecyclerView.setHasFixedSize(true);
        TermAdapter addAdapter = new TermAdapter();
        addRecyclerView.setAdapter(addAdapter);

        addAdapter.setTerms(addedTermsList);

        adapter.setOnItemClickListener(new TermAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Entities.Term term) {

                for (int i = 0; i < addedTermsList.size(); i++){
                    if(addedTermsList.get(i).getTerm_id() == term.getTerm_id()){
                        Toast.makeText(AddNewSchedule.this, "That Term has already been added to the schedule!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                addedTermsList.add(term);
                addAdapter.notifyDataSetChanged();

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> termsId = new ArrayList<>();
                String title = null;

                try{
                    for (int i = 0; i < addedTermsList.size(); i++) {
                        termsId.add(addedTermsList.get(i).getTerm_id());
                    }

                    if (addedTermsList.isEmpty()) {
                        Toast.makeText(AddNewSchedule.this, "Schedules require at least one term!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    title = editText.getText().toString();
                    if (title.matches("")) {
                        Toast.makeText(AddNewSchedule.this, "Please add a title!", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    Entities.Schedule schedule = new Entities.Schedule(title,termsId);
                    termViewModel.insertSchedule(schedule);
                }catch (NullPointerException e){
                    Toast.makeText(AddNewSchedule.this,"A Schedule should contain at least one term and a title", Toast.LENGTH_SHORT).show();
                }


                finish();
            }
        });


    }
}