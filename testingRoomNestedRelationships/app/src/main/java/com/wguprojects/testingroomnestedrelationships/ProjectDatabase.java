package com.wguprojects.testingroomnestedrelationships;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.time.LocalDateTime;


@Database(entities = {Entities.Term.class, Entities.Course.class, Entities.Instructor.class, Entities.Assessment.class, Entities.Schedule.class}, version = 4, exportSchema = false)
@TypeConverters(Entities.Converters.class)
public abstract class ProjectDatabase extends RoomDatabase{

    private static ProjectDatabase instance;

    public abstract AllDao.ScheduleDao scheduleDao();
    public abstract AllDao.TermDao termDao();
    public abstract AllDao.CourseDao courseDao();
    public abstract AllDao.InstructorDao instructorDao();
    public abstract AllDao.AssessmentDao assessmentDao();

    public abstract AllDao.RelationalDao relationalDao();

    public static synchronized ProjectDatabase getInstance(Context context){

        System.out.println("Getting database instance");
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ProjectDatabase.class,"project_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
            System.out.println("New instance of Project Database created!");
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsAsyncTask(instance).execute();
        }
    };



    private static class PopulateDbAsAsyncTask extends AsyncTask<Void, Void, Void> {

        private AllDao.TermDao termDao;

        private PopulateDbAsAsyncTask(ProjectDatabase database){

            termDao = database.termDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {


            return null;
        }


    }
}
