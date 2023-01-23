package com.wguprojects.testingroomnestedrelationships;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Entities {

    @Entity (tableName = "schedule_table")
    public static class Schedule{

        @PrimaryKey (autoGenerate = true)
        private int schedule_id;
        private String schedule_title;
        private ArrayList<Integer> associated_terms;


        public Schedule(String schedule_title, ArrayList<Integer> associated_terms){
            this.schedule_title = schedule_title;
            this.associated_terms = associated_terms;
        }

        public void setSchedule_id(int schedule_id) {
            this.schedule_id = schedule_id;
        }

        public int getSchedule_id() {
            return schedule_id;
        }

        public String getSchedule_title() {
            return schedule_title;
        }

        public ArrayList<Integer> getAssociated_terms() {
            return associated_terms;
        }
    }

    @Entity (tableName = "term_table")
    public static class Term{

        @PrimaryKey (autoGenerate = true)
        private int term_id;
        private String term_title;
        private LocalDateTime term_start_date;
        private LocalDateTime term_end_date;

        public Term(String term_title, LocalDateTime term_start_date, LocalDateTime term_end_date) {
            this.term_title = term_title;
            this.term_start_date = term_start_date;
            this.term_end_date = term_end_date;
        }

        @Ignore
        public Term(int term_id, String term_title, LocalDateTime term_start_date, LocalDateTime term_end_date) {
            this.term_id = term_id;
            this.term_title = term_title;
            this.term_start_date = term_start_date;
            this.term_end_date = term_end_date;
        }

        public void setTerm_id(int term_id) {
            this.term_id = term_id;
        }

        public int getTerm_id() {
            return term_id;
        }

        public String getTerm_title() {
            return term_title;
        }

        public LocalDateTime getTerm_start_date() {
            return term_start_date;
        }

        public LocalDateTime getTerm_end_date() {
            return term_end_date;
        }
    }

    @Entity (tableName = "course_table")
    public static class Course{

        @PrimaryKey (autoGenerate = true)
        private int course_id;

        private int associated_term_id;

        private String course_title;
        private LocalDateTime start_date;
        private LocalDateTime end_date;
        private String status;
        private String optional_note;

        private int associated_instructor_id;

        public Course(String course_title, LocalDateTime start_date, LocalDateTime end_date, String status, String optional_note, int associated_term_id, int associated_instructor_id) {
            this.course_title = course_title;
            this.start_date = start_date;
            this.end_date = end_date;
            this.status = status;
            this.optional_note = optional_note;
            this.associated_term_id = associated_term_id;
            this.associated_instructor_id = associated_instructor_id;
        }
        @Ignore
        public Course(int course_id, String course_title, LocalDateTime start_date, LocalDateTime end_date, String status, String optional_note, int associated_term_id, int associated_instructor_id) {
            this.course_id = course_id;
            this.course_title = course_title;
            this.start_date = start_date;
            this.end_date = end_date;
            this.status = status;
            this.optional_note = optional_note;
            this.associated_term_id = associated_term_id;
            this.associated_instructor_id = associated_instructor_id;
        }

        public void setCourse_id(int course_id) {
            this.course_id = course_id;
        }

        public int getCourse_id() {
            return course_id;
        }

        public String getCourse_title() {
            return course_title;
        }

        public LocalDateTime getStart_date() {
            return start_date;
        }

        public LocalDateTime getEnd_date() {
            return end_date;
        }

        public String getStatus() {
            return status;
        }

        public String getOptional_note() {
            return optional_note;
        }

        public int getAssociated_term_id(){
            return associated_term_id;
        }

        public int getAssociated_instructor_id() {
            return associated_instructor_id;
        }
    }

    @Entity (tableName = "instructor_table")
    public static class Instructor{

        @PrimaryKey (autoGenerate = true)
        private int instructor_id;


        private String instructor_name;
        private String instructor_phone;
        private String instructor_email;

        public Instructor(String instructor_name, String instructor_phone, String instructor_email) {
            this.instructor_name = instructor_name;
            this.instructor_phone = instructor_phone;
            this.instructor_email = instructor_email;
        }

        public void setInstructor_id(int instructor_id) {
            this.instructor_id = instructor_id;
        }

        public int getInstructor_id() {
            return instructor_id;
        }

        public String getInstructor_name() {
            return instructor_name;
        }

        public String getInstructor_phone() {
            return instructor_phone;
        }

        public String getInstructor_email() {
            return instructor_email;
        }

        @Override
        public String toString(){
            return instructor_name;
        }

    }

    @Entity (tableName = "assessment_table")
    public static class Assessment{

        @PrimaryKey (autoGenerate = true)
        private int assessment_id;

        private int associated_course_id;

        private String assessment_type;
        private String assessment_title;
        private LocalDateTime assessment_start_date;
        private LocalDateTime assessment_end_date;

        public Assessment(String assessment_title, String assessment_type, LocalDateTime assessment_start_date, LocalDateTime assessment_end_date, int associated_course_id) {

            this.assessment_title = assessment_title;
            this.assessment_type = assessment_type;
            this.assessment_start_date = assessment_start_date;
            this.assessment_end_date = assessment_end_date;
            this.associated_course_id = associated_course_id;
        }

        public void setAssessment_id(int assessment_id) {
            this.assessment_id = assessment_id;
        }

        public int getAssessment_id() {
            return assessment_id;
        }

        public String getAssessment_type() {
            return assessment_type;
        }

        public String getAssessment_title() {
            return assessment_title;
        }

        public LocalDateTime getAssessment_start_date() {
            return assessment_start_date;
        }

        public LocalDateTime getAssessment_end_date() {
            return assessment_end_date;
        }

        public int getAssociated_course_id(){
            return associated_course_id;
        }
    }

    static class Converters {

        @TypeConverter
        public String fromLocalDateTime (LocalDateTime localDateTime){
            if (localDateTime == null){
                return null;
            }

            return localDateTime.toString();
        }
        @TypeConverter
        public LocalDateTime toLocalDateTime (String localDateTime){
            if (localDateTime == null){
                return null;
            }
            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(localDateTime);
        }

        @TypeConverter
        public ArrayList<Integer> fromString(String i){
            if (i == null || i.isEmpty()){
                return null;
            }
            ArrayList<Integer> list = new ArrayList<>();
            String[] parsedString = i.split(",");
            for (int j = 0; j < parsedString.length; j++){
                list.add(Integer.parseInt(parsedString[j]));
            }
            return list;
        }

        @TypeConverter
        public String fromArrayList(ArrayList<Integer> list){
            if (list == null){
                return null;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < list.size(); i ++){
                int num = list.get(i);
                if (i < list.size() - 1){
                    sb.append(num + ",");
                }
                else {
                    sb.append(num);
                }
            }
            String listAsString = sb.toString();
            return listAsString;
        }
    }


}
