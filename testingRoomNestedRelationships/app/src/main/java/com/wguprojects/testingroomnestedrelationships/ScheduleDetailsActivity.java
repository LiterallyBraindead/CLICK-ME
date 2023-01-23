package com.wguprojects.testingroomnestedrelationships;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;

public class ScheduleDetailsActivity extends AppCompatActivity {

    TermViewModel termViewModel;
    TextView detailsText;
    TextView scheduleTitle;
    ArrayList<Integer> associatedTermIds = new ArrayList<>();
    StringBuilder termDetails = new StringBuilder();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_details);

        scheduleTitle = findViewById(R.id.schedule_title);
        detailsText = findViewById(R.id.schedule_details);

        Intent intent = getIntent();
        int scheduleId = intent.getIntExtra("schedule_id", 0);

        termViewModel = new ViewModelProvider(this).get(TermViewModel.class);

       try{
           termViewModel.getAllSchedules().observe(ScheduleDetailsActivity.this, allSchedules ->{
               for(int i = 0; i < allSchedules.size(); i++){
                   if (allSchedules.get(i).getSchedule_id() == scheduleId){
                       scheduleTitle.setText(allSchedules.get(i).getSchedule_title());
                       associatedTermIds = allSchedules.get(i).getAssociated_terms();
                   }
               }

               termViewModel.getAllTerms().observe(ScheduleDetailsActivity.this, allTerms ->{
                   for (int n = 0; n < allTerms.size(); n++){
                       DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                       if(associatedTermIds.contains(allTerms.get(n).getTerm_id())){
                           termDetails.append("Term Title: " + allTerms.get(n).getTerm_title()
                                   + "\nStart Date: " + allTerms.get(n).getTerm_start_date().format(formatter)
                                   + "\nEnd Date:   " + allTerms.get(n).getTerm_end_date().format(formatter)
                                   + "\n\n");

                       }
                   }
                   String details = termDetails.toString();
                   if(details.isEmpty()){
                       detailsText.setText("");
                   }
                   else{
                       detailsText.setText(details);
                   }

               });
           });
       }catch (NullPointerException e){
           System.out.println("There was an error with displaying the schedule details");
       }

    }
}