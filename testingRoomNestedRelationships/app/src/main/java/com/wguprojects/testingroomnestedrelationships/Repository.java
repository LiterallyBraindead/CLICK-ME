package com.wguprojects.testingroomnestedrelationships;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.loader.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import kotlinx.coroutines.flow.LintKt;
import kotlinx.coroutines.internal.OnUndeliveredElementKt;

public class Repository {
    private AllDao.ScheduleDao scheduleDao;
    private AllDao.TermDao termDao;
    private AllDao.CourseDao courseDao;
    private AllDao.InstructorDao instructorDao;
    private AllDao.AssessmentDao assessmentDao;

    private LiveData<List<Entities.Schedule>> allSchedules;
    private LiveData<List<Entities.Term>> allTerms;
    private LiveData<List<Entities.Course>> allCourses;
    private LiveData<List<Entities.Instructor>> allInstructor;
    private LiveData<List<Entities.Assessment>> allAssessments;

    private AllDao.RelationalDao relationalDao;
    private LiveData<List<RelationalEntities.TermWithCourses>> allTermWithCourses;
    private LiveData<List<RelationalEntities.CourseWithAssessments>> allCourseWithAssessments;


    public static int termID;
    public static int courseId;


    public static void setTermID(int id){
        termID = id;
        System.out.println("The term identity set in repo is: " + termID);
    }

    public static void setCourseID(int id){
        courseId = id;
        System.out.println("The course id set in repo is : " + courseId);
    }

    public Repository(Application application){
        ProjectDatabase database = ProjectDatabase.getInstance(application);
        scheduleDao = database.scheduleDao();
        termDao = database.termDao();
        courseDao = database.courseDao();
        instructorDao = database.instructorDao();
        assessmentDao = database.assessmentDao();

        allSchedules = scheduleDao.getAllSchedule();
        allTerms = termDao.getAllTerms();
        allCourses = courseDao.getAllCourses();
        allInstructor = instructorDao.getAllInstructors();
        allAssessments = assessmentDao.getAllAssessment();

        relationalDao = database.relationalDao();
        allTermWithCourses = relationalDao.getTermsWithCourses(termID);
        allCourseWithAssessments = relationalDao.getCourseWithAssessments(courseId);

    }

    public LiveData<List<Entities.Schedule>> getAllSchedules() {return allSchedules;}
    public void insertSchedule(Entities.Schedule schedule){
        new InsertScheduleAsyncTask(scheduleDao).execute(schedule);
    }
    public void updateSchedule(Entities.Schedule schedule){
        new UpdateScheduleAsyncTask(scheduleDao).execute(schedule);
    }
    public void deleteSchedule(Entities.Schedule schedule){
        new DeleteScheduleAsyncTask(scheduleDao).execute(schedule);
    }


    private static class InsertScheduleAsyncTask extends AsyncTask<Entities.Schedule, Void, Void>{
        private AllDao.ScheduleDao scheduleDao;
        private InsertScheduleAsyncTask(AllDao.ScheduleDao scheduleDao){
            this.scheduleDao = scheduleDao;
        }
        @Override
        protected Void doInBackground(Entities.Schedule... schedules) {
            scheduleDao.insert(schedules[0]);
            return null;
        }
    }
    private static class UpdateScheduleAsyncTask extends AsyncTask<Entities.Schedule, Void, Void>{
        private AllDao.ScheduleDao scheduleDao;
        private UpdateScheduleAsyncTask(AllDao.ScheduleDao scheduleDao){
            this.scheduleDao = scheduleDao;
        }
        @Override
        protected Void doInBackground(Entities.Schedule... schedules) {
            scheduleDao.update(schedules[0]);
            return null;
        }
    }
    private static class DeleteScheduleAsyncTask extends AsyncTask<Entities.Schedule, Void, Void>{
        private AllDao.ScheduleDao scheduleDao;
        private DeleteScheduleAsyncTask(AllDao.ScheduleDao scheduleDao){
            this.scheduleDao = scheduleDao;
        }
        @Override
        protected Void doInBackground(Entities.Schedule... schedules) {
            scheduleDao.delete(schedules[0]);
            return null;
        }
    }


    public LiveData<List<Entities.Term>> getAllTerms(){
        return allTerms;
    }

    public void insertTerm(Entities.Term term){
        new InsertTermAsyncTask(termDao).execute(term);
    }
    public void updateTerm(Entities.Term term){
        new UpdateTermAsyncTask(termDao).execute(term);
    }
    public void deleteTerm(Entities.Term term){
        new DeleteTermAsyncTask(termDao).execute(term);
    }

    private static class InsertTermAsyncTask extends AsyncTask<Entities.Term, Void, Void>{
        private AllDao.TermDao termDao;
        private InsertTermAsyncTask(AllDao.TermDao termDao){
            this.termDao = termDao;
        }
        @Override
        protected Void doInBackground(Entities.Term... terms) {
            termDao.insert(terms[0]);
            return null;
        }
    }
    private static class UpdateTermAsyncTask extends AsyncTask<Entities.Term, Void, Void>{
        private AllDao.TermDao termDao;
        private UpdateTermAsyncTask(AllDao.TermDao termDao){
            this.termDao = termDao;
        }
        @Override
        protected Void doInBackground(Entities.Term... terms) {
            termDao.update(terms[0]);
            return null;
        }
    }
    private static class DeleteTermAsyncTask extends AsyncTask<Entities.Term, Void, Void>{
        private AllDao.TermDao termDao;
        private DeleteTermAsyncTask(AllDao.TermDao termDao){
            this.termDao = termDao;
        }
        @Override
        protected Void doInBackground(Entities.Term... terms) {
            termDao.delete(terms[0]);
            return null;
        }
    }



    public LiveData<List<Entities.Course>> getAllCourses(){
        return allCourses;
    }


    public void insertCourse(Entities.Course course){
        new InsertCourseAsyncTask(courseDao).execute(course);
    }
    public void updateCourse(Entities.Course course){
        new UpdateCourseAsyncTask(courseDao).execute(course);
    }
    public void deleteCourse(Entities.Course course){
        new DeleteCourseAsyncTask(courseDao).execute(course);
    }

    public static class InsertCourseAsyncTask extends AsyncTask<Entities.Course, Void, Void>{
        private AllDao.CourseDao courseDao;
        private InsertCourseAsyncTask(AllDao.CourseDao courseDao){
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Entities.Course... courses) {
            courseDao.insertCourse(courses[0]);
            return null;
        }
    }
    public static class UpdateCourseAsyncTask extends AsyncTask<Entities.Course, Void, Void>{
        private AllDao.CourseDao courseDao;
        private UpdateCourseAsyncTask(AllDao.CourseDao courseDao){
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Entities.Course... courses) {
            courseDao.updateCourse(courses[0]);
            return null;
        }
    }

    public static class DeleteCourseAsyncTask extends AsyncTask<Entities.Course, Void, Void>{
        private AllDao.CourseDao courseDao;
        private DeleteCourseAsyncTask(AllDao.CourseDao courseDao){
            this.courseDao = courseDao;
        }

        @Override
        protected Void doInBackground(Entities.Course... courses) {
            courseDao.deleteCourse(courses[0]);
            return null;
        }
    }



    public LiveData<List<Entities.Instructor>> getAllInstructors(){
        return allInstructor;
    }

    public void insertInstructor(Entities.Instructor instructor){
        new InsertInstructorAsyncTask(instructorDao).execute(instructor);
    }
    public void updateInstructor(Entities.Instructor instructor){
        new UpdateInstructorAsyncTask(instructorDao).execute(instructor);
    }
    public void deleteInstructor(Entities.Instructor instructor){
        new DeleteInstructorAsyncTask(instructorDao).execute(instructor);
    }

    public static class InsertInstructorAsyncTask extends AsyncTask<Entities.Instructor, Void, Void>{
        private AllDao.InstructorDao instructorDao;
        private InsertInstructorAsyncTask(AllDao.InstructorDao instructorDao){
            this.instructorDao = instructorDao;
        }
        @Override
        protected Void doInBackground(Entities.Instructor... instructors) {
            instructorDao.insertInstructor(instructors[0]);
            return null;
        }
    }
    public static class UpdateInstructorAsyncTask extends AsyncTask<Entities.Instructor, Void, Void>{
        private AllDao.InstructorDao instructorDao;
        private UpdateInstructorAsyncTask(AllDao.InstructorDao instructorDao){
            this.instructorDao = instructorDao;
        }
        @Override
        protected Void doInBackground(Entities.Instructor... instructors) {
            instructorDao.updateInstructor(instructors[0]);
            return null;
        }
    }
    public static class  DeleteInstructorAsyncTask extends AsyncTask<Entities.Instructor, Void, Void>{
        private AllDao.InstructorDao instructorDao;
        private DeleteInstructorAsyncTask(AllDao.InstructorDao instructorDao){
            this.instructorDao = instructorDao;
        }
        @Override
        protected Void doInBackground(Entities.Instructor... instructors) {
            instructorDao.deleteInstructor(instructors[0]);
            return null;
        }
    }


    public LiveData<List<Entities.Assessment>> getAllAssessments(){
        return allAssessments;
    }
    
    public void insertAssessment(Entities.Assessment assessment){
        new InsertAssessmentAsyncTask(assessmentDao).execute(assessment);
    }
    public void updateAssessment(Entities.Assessment assessment){
        new UpdateAssessmentAsyncTask(assessmentDao).execute(assessment);
    }
    public void deleteAssessment(Entities.Assessment assessment){
        new DeleteAssessmentAsyncTask(assessmentDao).execute(assessment);
    }
    public static class InsertAssessmentAsyncTask extends AsyncTask<Entities.Assessment, Void, Void>{
        private AllDao.AssessmentDao assessmentDao;
        private InsertAssessmentAsyncTask(AllDao.AssessmentDao assessmentDao){
            this.assessmentDao = assessmentDao;
        }
        @Override
        protected Void doInBackground(Entities.Assessment... assessments) {
            assessmentDao.insertAssessment(assessments[0]);
            return null;
        }
    }
    public static class UpdateAssessmentAsyncTask extends AsyncTask<Entities.Assessment, Void, Void>{
        private AllDao.AssessmentDao assessmentDao;
        private UpdateAssessmentAsyncTask(AllDao.AssessmentDao assessmentDao){
            this.assessmentDao = assessmentDao;
        }
        @Override
        protected Void doInBackground(Entities.Assessment... assessments) {
            assessmentDao.updateAssessment(assessments[0]);
            return null;
        }
    }
    public static class DeleteAssessmentAsyncTask extends AsyncTask<Entities.Assessment, Void, Void>{
        private AllDao.AssessmentDao assessmentDao;
        private DeleteAssessmentAsyncTask(AllDao.AssessmentDao assessmentDao){
            this.assessmentDao = assessmentDao;
        }
        @Override
        protected Void doInBackground(Entities.Assessment... assessments) {
            assessmentDao.deleteAssessment(assessments[0]);
            return null;
        }
    }


    public LiveData<List<RelationalEntities.TermWithCourses>> getAllTermWithCourses(){
        return allTermWithCourses;
    }

    public LiveData<List<RelationalEntities.CourseWithAssessments>> getAllCourseWithAssessments(){
        return allCourseWithAssessments;
    }

}
