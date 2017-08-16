package client.oddc.fla.com.dal;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

import client.oddc.fla.com.model.EventType;
import client.oddc.fla.com.model.JobStatus;
import client.oddc.fla.com.model.ODDCJob;
import client.oddc.fla.com.model.ODDCTask;
import client.oddc.fla.com.model.TaskType;
import client.oddc.fla.com.restclient.ODDCApplication;
import client.oddc.fla.com.restclient.R;

/**
 * Created by yzharchuk on 8/8/2017.
 */

public class DBHelper extends SQLiteOpenHelper
{
    private static String DATABASE_NAME = "ODDC";
    private static int DATABASE_VERSION = 1;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
        Resources resources = ODDCApplication.getResourcesStatic();
        DATABASE_NAME = resources.getString(R.string.database_name);
        DATABASE_VERSION = resources.getInteger(R.integer.database_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
//        //TODO: Add unit test
//        db.execSQL(
//                "CREATE TABLE IF NOT EXISTS Jobs (" +
//                    "Id char(30) PRIMARY KEY," +
//                    "SessionId char(30) NOT NULL," +
//                    "JobTimeStamp timestamp NOT NULL,"+
//                    "TransportTrigger char(30) NOT NULL,"+
//                    "ActivationTrigger char(30) NOT NULL,"+
//                    "Status int NOT NULL,"+
//                    "FOREIGN KEY(Status) REFERENCES JobStatusTypes(id)" +
//                ");"
//        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
       // db.execSQL("DROP TABLE IF EXISTS Jobs");
    }

    public void insertJobs(ArrayList<ODDCJob> jobs) throws SQLException
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // this transaction will include 4 cascaded inserts
        // into related tables: TaskEvents, TaskCameras, Tasks, Jobs
        db.beginTransaction();

        try
        {
            for (ODDCJob job : jobs)
            {
                // nested inserts into related tables
                insertTasks(job.getTasks(), job.getId(), db);
                contentValues.put("Id", job.getId());
                contentValues.put("SessionId", job.getSessionId());
                contentValues.put("JobTimeStamp", " time('now') ");
                contentValues.put("TransportTrigger", job.getTransportTrigger());
                contentValues.put("ActivationTrigger", job.getActivationTrigger());
                contentValues.put("Status", job.getStatus().getValue());
            }
            // 3-rd operation in SQLiteDatabase transaction
            db.insert("Jobs", null, contentValues);

            db.setTransactionSuccessful();

            String path = db.getPath();
            String cwd = System.getProperty("user.dir");

            String currentDirectory;
            File file = new File("torba1.txt");
            currentDirectory = file.getAbsolutePath();
            currentDirectory = "";

        }
        catch (Exception e)
        {
            Log.e("Error inserting jobs.",e.getMessage());
            throw new SQLException("Error inserting into Jobs.", e);
        }
        finally
        {
            db.endTransaction();
        }
    }

    private void insertTasks(ArrayList<ODDCTask> tasks, String jobId, SQLiteDatabase db) throws SQLException
    {
        ContentValues contentValues = new ContentValues();

        try
        {
            for (ODDCTask task : tasks)
            {
                // nested inserts into related tables
                insertEventsToReport(task.getEventsToReport(), task.getId(), db);
                insertTargetedCameras(task.getTargetedCameras(), task.getId(), db);
                contentValues.put("Id", task.getId());
                contentValues.put("JobId", jobId);
                contentValues.put("MsrdReportFrequency", task.getMsrdReportFrequency());
                contentValues.put("TaskType", task.getType().getValue());
            }
            // 3-rd operation in SQLiteDatabase transaction
            db.insert("Tasks", null, contentValues);
        }
        catch (SQLException e)
        {
            Log.e("Err insert Tasks", e.getMessage());
            throw new SQLException("Error inserting Tasks.", e);
        }
    }

    private void insertTargetedCameras(ArrayList<String> targetedCameras, String taskId, SQLiteDatabase db)
    {
        ContentValues contentValues = new ContentValues();

        try
        {
            for (String cameraId : targetedCameras)
            {
                contentValues.put("TaskId", taskId);
                contentValues.put("CameraId", cameraId);
            }
            // 2-nd operation in SQLiteDatabase transaction
            db.insert("TaskCameras", null, contentValues);
        }
        catch (SQLException e)
        {
            Log.e("Err insert TaskCameras.", e.getMessage());
            throw new SQLException("Error inserting TaskCameras.", e);
        }
    }

    private void insertEventsToReport(ArrayList<EventType> eventTypes, String taskId, SQLiteDatabase db)
    {
        ContentValues contentValues = new ContentValues();

        try
        {
            for (EventType eventType : eventTypes)
            {
                contentValues.put("TaskId", taskId);
                contentValues.put("EventType", eventType.getValue());
            }
            // 1-st operation in SQLiteDatabase transaction
            db.insert("TaskEvents", null, contentValues);
        }
        catch (SQLException e)
        {
            Log.e("Err insert TaskEvents", e.getMessage());
            throw new SQLException("Error inserting into TaskEvents.", e);
        }
    }

    // lazy loading. Tasks not loaded.
    // TODO: implement cascaded data loading from tables: TaskEvents, TaskCameras, Tasks
    public ArrayList<ODDCJob> getJobs(ArrayList<String> jobIDs)
    {
        // compose criteria
        String sql = "";
        for (String jobId : jobIDs)
            sql += jobId + ",";

        //remove trailing comma
        sql = sql.substring(0, sql.length() - 1);
        // prepare statement
        sql = "select * from Jobs where id in (" + sql + ");";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<ODDCJob> jobs = new ArrayList<ODDCJob>();
        db.beginTransaction();
        Cursor cursor = null;

        try
        {
            cursor = db.rawQuery(sql , null);

            if (cursor.moveToFirst())
            {
                ODDCJob job = new ODDCJob();
                do
                {
                    String id = cursor.getString(cursor.getColumnIndex("Id"));
                    job.setId(id);
                    String sessionId = cursor.getString(cursor.getColumnIndex("SessionId"));
                    job.setSessionId(sessionId);
                    Timestamp jobTimeStamp = Timestamp.valueOf(cursor.getString(cursor.getColumnIndex("JobTimeStamp")));
                    job.setJobTimeStamp(jobTimeStamp);
                    String transportTrigger = cursor.getString(cursor.getColumnIndex("TransportTrigger"));
                    job.setTransportTrigger(transportTrigger);
                    String activationTrigger = cursor.getString(cursor.getColumnIndex("ActivationTrigger"));
                    job.setActivationTrigger(activationTrigger);
                    int status = cursor.getInt(cursor.getColumnIndex("Status"));
                    job.setStatus(JobStatus.valueOf(status));
                    ArrayList <ODDCTask> tasks = getTasks(id, db);
                    job.setTasks(tasks);
                    jobs.add(job);
                }
                while (cursor.moveToNext());
            }
            db.setTransactionSuccessful();
        }
        catch (SQLException e)
        {
            Log.e("Error in getJobs().",e.getMessage());
        }
        finally
        {
            if(cursor != null && !cursor.isClosed())
                cursor.close();

            db.endTransaction();
        }
        return jobs;
    }

    public ODDCJob getJob(String jobId)
    {
        ArrayList<String> jobIDs = new ArrayList<String>();
        jobIDs.add(jobId);
        ArrayList<ODDCJob> jobs = getJobs(jobIDs);
        return (jobs != null && jobs.isEmpty() && jobs.size() > 0) ? jobs.get(0) : null;
    }

    //TODO: Add tasks update if needed
    public boolean updateJob(
            String id,
            String sessionId,
            Timestamp jobTimeStamp,
            String transportTrigger,
            String activationTrigger,
            int status)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("SessionId", sessionId);
        contentValues.put("JobTimeStamp", " time('now') ");
        contentValues.put("TransportTrigger", transportTrigger);
        contentValues.put("ActivationTrigger", activationTrigger);
        contentValues.put("Status", status);
        db.update("Jobs", contentValues, "id = ? ", new String[]
                {
                        id
                } );
        return true;
    }

    public Integer deleteJob (String jobId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Jobs", "id = ? ",  new String[]
                {
                        jobId
                });
    }

    //TODO: Implement this
    public ArrayList<ODDCTask> getTasks(String jobId, SQLiteDatabase db)
    {
        ArrayList<ODDCTask> tasks = new ArrayList<ODDCTask>();
        String sql = "select * from Tasks where JobID='" + jobId + "';";
        Cursor cursor = null;

        try
        {
            cursor = db.rawQuery(sql , null);

            if (cursor.moveToFirst())
            {
                ODDCTask task = new ODDCTask();
                do
                {
                    task.setId(jobId);
                    String id = cursor.getString(cursor.getColumnIndex("Id"));
                    task.setId(id);
                    int taskType = cursor.getInt(cursor.getColumnIndex("TaskType"));
                    task.setType(TaskType.valueOf(taskType));
                    int msrdReportFrequency = cursor.getInt(cursor.getColumnIndex("MsrdReportFrequency"));
                    task.setMsrdReportFrequency(msrdReportFrequency);
                    ArrayList<EventType> eventsToReport = getTaskEvents(id, db);
                    task.setEventsToReport(eventsToReport);
                    ArrayList<String> targetedCameras = getTargetedCameras(id, db);
                    task.setTargetedCameras(targetedCameras);
                    tasks.add(task);
                }
                while (cursor.moveToNext());
            }
        }
        catch (SQLException e)
        {
            Log.e("Error in getTasks().",e.getMessage());
            throw new SQLException("Error in getTasks().", e);
        }
        finally
        {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return tasks;
    }

    private ArrayList<String> getTargetedCameras(String taskId, SQLiteDatabase db)
    {
        ArrayList<String> cameraIDs = new ArrayList<String>();
        String sql = "select CameraId from TaskCameras where TaskId='" + taskId + "';";
        Cursor cursor = null;

        try
        {
            cursor = db.rawQuery(sql , null);

            if (cursor.moveToFirst())
            {
                do
                {
                    String cameraId = cursor.getString(cursor.getColumnIndex("CameraId"));
                    cameraIDs.add(cameraId);
                }
                while (cursor.moveToNext());
            }
        }
        catch (SQLException e)
        {
            Log.e("Er getTargetedCameras()",e.getMessage());
            throw new SQLException("Error in getTargetedCameras().", e);
        }
        finally
        {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return cameraIDs;
    }

    private ArrayList<EventType> getTaskEvents(String taskId, SQLiteDatabase db)
    {
        ArrayList<EventType> taskEvents = new ArrayList<EventType>();
        String sql = "select EventType from TaskEvents where TaskId='" + taskId + "';";
        Cursor cursor = null;

        try
        {
            cursor = db.rawQuery(sql , null);

            if (cursor.moveToFirst())
            {
                do
                {
                    int eventType = cursor.getInt(cursor.getColumnIndex("EventType"));
                    taskEvents.add(EventType.valueOf(eventType));
                }
                while (cursor.moveToNext());
            }
        }
        catch (SQLException e)
        {
            Log.e("Err in getTaskEvents().",e.getMessage());
            throw new SQLException("Error in getTaskEvents().", e);
        }
        finally
        {
            if(cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return taskEvents;
    }

    public ArrayList<ODDCTask> getTasks(String jobId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return getTasks(jobId, db);
    }
}
