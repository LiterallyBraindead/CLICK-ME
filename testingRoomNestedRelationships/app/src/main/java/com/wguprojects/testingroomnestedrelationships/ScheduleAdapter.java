package com.wguprojects.testingroomnestedrelationships;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleHolder> {

    private List<Entities.Schedule> allSchedules = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ScheduleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        return new ScheduleHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleHolder holder, int position) {
        Entities.Schedule currentSchedule = allSchedules.get(position);

        Random random = new Random();
        holder.itemView.setBackgroundColor(Color.argb(255, random.nextInt(256),
                random.nextInt(256), random.nextInt(256)));

        try{
            if (currentSchedule.getSchedule_title().isEmpty() || currentSchedule.getSchedule_title() == null){
                holder.title.setText("");
            }else {
                holder.title.setText(currentSchedule.getSchedule_title());
            }
            if (currentSchedule.getAssociated_terms().isEmpty() || currentSchedule.getAssociated_terms() == null){
                holder.termCount.setText("");
            }else {
                holder.termCount.setText(String.valueOf(currentSchedule.getAssociated_terms().size()));
            }
        }catch (NullPointerException e){
            //
        }


    }


    @Override
    public int getItemCount() {
        return allSchedules.size();
    }

    public void setAllSchedules(List<Entities.Schedule> allSchedules){
        this.allSchedules = allSchedules;
        notifyDataSetChanged();
    }

    class ScheduleHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView termCount;

        public ScheduleHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            termCount = itemView.findViewById(R.id.term_total);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(allSchedules.get(position));
                    }
                }
            });
        }
    }
    public interface OnItemClickListener{
        void onItemClick(Entities.Schedule schedule);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public Entities.Schedule getScheduleAt(int position){
        return allSchedules.get(position);
    }
}
