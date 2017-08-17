package client.oddc.fla.com.restclient;

import android.content.Context;
import android.database.Cursor;
import android.os.Process;
import android.util.Log;
import android.icu.util.TimeZone;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Arrays;

import android.content.ContentValues;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.DatabaseUtils;
import android.widget.TextView;

import client.oddc.fla.com.model.ContinuousDataCollection;
import client.oddc.fla.com.restclient.ODDCinterface;

import client.oddc.fla.com.restclient.DBschema;
import client.oddc.fla.com.model.ContinuousData;



public class ODDCclass implements ODDCinterface {
    RESTController controller;


    String DATABASE_NAME = "oddc.db";

    int MIN_AVAIL_FS = 1024 * 1024 * 1024;
    int SAMPLE_FREQ = 30; // ALFREDO parameter from SLA?
    int FRAMES_PER_MIN;
    int SENDCOUNT;
    //int frameRate = 3; // fixed rate since NeuSoft can't provide
    //int getFrameRate(){return frameRate;}

    //Context mContext;


    ODDCdbHelper dbh = null;
    SQLiteDatabase db = null;
    TimeZone tz;

    private NeuSoftSimulator listener;
    private int loopCount = 0;

    public ODDCclass(){

        //mContext = MainActivity.mContext;
        FRAMES_PER_MIN = MainActivity.getFrameRate() * 60;
        SENDCOUNT = FRAMES_PER_MIN / SAMPLE_FREQ;

        Log.d("ALFREDO THREAD ","ODDCclass TID="+String.valueOf(Process.myTid()));
    }



    // for NeuSoft, need to know name of NeuSoft listener class
    public void setListener(NeuSoftSimulator listener){
        this.listener = listener;
    }


    // return value of false indicates some condition exists which should prevent startup
    // otherwise true indicate OK to startup
    public boolean ok2Startup(){

        controller = new RESTController(MainActivity.getBaseUrl());

        tz = TimeZone.getDefault();
        boolean isDST = tz.observesDaylightTime();
        int dstMillisec = tz.getDSTSavings();
        int ros = tz.getRawOffset();


        // code above is for testing, code below is for production
        Log.d("ALFREDO","ok2Startup mContext="+MainActivity.mContext);
        dbh = new ODDCdbHelper(MainActivity.mContext);
        Log.d("ALFREDO","ok2Startup dbh="+dbh);
        db = dbh.getWritableDatabase();
        Log.d("ALFREDO","ODDCclass.ok2Startup db = dbh.getWritableDatabase");


        long fsStat = checkFileSpace();
        return fsStat == -1 ? false : true;
    }

    // NeuSoft waits for boolean return value from reqShutdown before shutdown
    public boolean reqShutdown(){return true;} // FIXME TBD


    // TESTING ONLY
    public int getRowCount(){
        long cnt  = DatabaseUtils.queryNumEntries(db, DBschema.TABLE_NAME);
        return (int)cnt;
    }


    private class ODDCdbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 3;


        public ODDCdbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            Log.d("ALFREDO","ODDCdbHelper.ODDCdbHelper");
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_DROP_TABLE);
            db.execSQL(SQL_CREATE_TABLE);
            Log.d("ALFREDO","ODDCdbHelper.onCreate SQL_CREATE_TABLE="+SQL_CREATE_TABLE);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DROP_TABLE);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

    }

    public void dropTable(){
        db.execSQL(SQL_DROP_TABLE);
        db.execSQL(SQL_CREATE_TABLE);
    }

    public List<String> getContinuousLog(){return Arrays.asList("TBD");}
    public List<String> getEventLog(){return Arrays.asList("TBD");}
    public List<String> getOnDemandLog(){return Arrays.asList("TBD");}


    public boolean onContinuousData(ContinuousData data){
        // Fujitsu processing of continuous data received from NeuSoft

        insertSQLite(data);

        if (loopCount > FRAMES_PER_MIN){
            SendToFLA fla = new SendToFLA();
            fla.start();

            loopCount = 0;
        }
        else loopCount++;

        long fsStat = checkFileSpace();

        return fsStat == -1 ? false : true;
    }


    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + DBschema.TABLE_NAME + " (" +
                    DBschema._ID      + " INTEGER PRIMARY KEY," +
                    DBschema.VIN      + " CHAR(18)," +

                    DBschema.GPS_TS   + " TIMESTAMP," +
                    DBschema.GPS_LON  + " FLOAT(10,6)," +
                    DBschema.GPS_LAT  + " FLOAT(10,6)," +

                    DBschema.SPEED    + " FLOAT(5,2)," +
                    DBschema.SPEED_DT + " INT," +

                    DBschema.ACC_TS   + " TIMESTAMP," +
                    DBschema.ACC_X    + " FLOAT(10,6)," +
                    DBschema.ACC_Y    + " FLOAT(10,6)," +
                    DBschema.ACC_Z    + " FLOAT(10,6)," +

                    DBschema.GS_TS    + " TIMESTAMP," +
                    DBschema.GS_E     + " BOOLEAN," +
                    DBschema.GS_ET    + " FLOAT(10,6)," +

                    DBschema.FCW_TS   + " TIMESTAMP," +
                    DBschema.FCW_EFV  + " BOOLEAN," +
                    DBschema.FCW_CI   + " BOOLEAN," +
                    DBschema.FCW_TTC  + " INT," +
                    DBschema.FCW_DFV  + " FLOAT(5,2)," +
                    //DBschema.FCW_RSFV + " FLOAT(5,2)," +
                    DBschema.FCW_E    + " BOOLEAN," +
                    DBschema.FCW_ET   + " FLOAT(5,2)," +

                    DBschema.LDW_TS   + " TIMESTAMP," +
                    DBschema.LDW_DLL  + " FLOAT(5,2)," +
                    DBschema.LDW_DRL  + " FLOAT(5,2)," +
                    DBschema.LDW_E    + " BOOLEAN," +

                    DBschema.M_URI    + " VARCHAR(32)," +
                    DBschema.M_U      + " BOOLEAN," +
                    DBschema.M_D      + " BOOLEAN," +
                    DBschema.D_U      + " BOOLEAN )";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + DBschema.TABLE_NAME;


    public boolean insertSQLite(ContinuousData data){
        ContentValues values = new ContentValues();
        values.put(DBschema.VIN, data.vehicleID);
        //values.put(DBschema.TZ, tz.getRawOffset()); /* milliseconds */

        values.put(DBschema.GPS_TS, getTimestamp());
        values.put(DBschema.GPS_LON, data.longitude);
        values.put(DBschema.GPS_LAT, data.latitude);

        values.put(DBschema.SPEED, data.speed);
        values.put(DBschema.SPEED_DT, data.speedDetectionType);

        values.put(DBschema.ACC_TS, getTimestamp());
        values.put(DBschema.ACC_X, data.accelerationX);
        values.put(DBschema.ACC_Y, data.accelerationY);
        values.put(DBschema.ACC_Z, data.accelerationZ);

        values.put(DBschema.GS_TS, getTimestamp());
        values.put(DBschema.GS_E, data.gShockEvent);
        values.put(DBschema.GS_ET, data.gShockEventThreshold);

        values.put(DBschema.FCW_TS, getTimestamp());
        values.put(DBschema.FCW_EFV, data.fcwExistFV);
        values.put(DBschema.FCW_CI, data.fcwCutIn);
        //values.put(DBschema.FCW_TTC, data.fcwTimeToCollision);
        values.put(DBschema.FCW_DFV, data.fcwDistanceToFV);
        //values.put(DBschema.FCW_RSFV, data.fcwRelativeSpeedToFV);
        values.put(DBschema.FCW_E, data.fcwEvent);
        values.put(DBschema.FCW_ET, data.fcwEventThreshold);

        values.put(DBschema.LDW_TS, getTimestamp());
        values.put(DBschema.LDW_DLL, data.ldwDistanceToLeftLane);
        values.put(DBschema.LDW_DRL, data.ldwDistanceToRightLane);
        values.put(DBschema.LDW_E, data.ldwEvent);

        values.put(DBschema.M_URI, data.mediaURI);
        values.put(DBschema.M_D, false);
        values.put(DBschema.M_U, false);
        values.put(DBschema.D_U, false);

        long rid = db.insert(DBschema.TABLE_NAME, null, values);

        // TESTING, take out for PRODUCTION
        MainActivity.dbCount.post(new Runnable() {
            public void run() {
                final int cnt = getRowCount();
                //Log.d("ALFREDO","MainActivity.insertSQLite "+String.valueOf(cnt));
                String dbt = "DB rowCount="+String.valueOf(cnt);
                MainActivity.dbCount.setText(dbt);
            }
        });

        return  true;
    }


    // YYMMDD_HHMMSS.mp4
    public long checkFileSpace(){
        long availSpace = MainActivity.getVideoFolder().getUsableSpace();

        //Log.d("ALFREDO","checkFileSpace MIN_AVAIL_FS="+String.valueOf(MIN_AVAIL_FS)+" availSpace="+String.valueOf(availSpace));

        if (availSpace > MIN_AVAIL_FS) return availSpace;
        else                           return -1; // delete some files
    }


    public class SendToFLA extends Thread {
        public SendToFLA(){super ("SendToFLA");}
        public void run(){
            String[] columns = new String[]{"vehicleID","longitude","latitude"};
            String selection = new String("rowid in ( select rowid from oddc where rowid % ? = 0 and DataUploaded = 0 limit ? )");
            String[] selectionArgs = new String[]{String.valueOf(SAMPLE_FREQ),String.valueOf(SENDCOUNT)};

            Cursor c = db.query (DBschema.TABLE_NAME,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null,
                    null);

            int nrows = c.getCount();
            if (nrows > 0)
            {
                int i = 0;
                ArrayList<ContinuousData> dataCollection = new ArrayList<ContinuousData>();
                while (c.moveToNext()){
                    ContinuousData cd = new ContinuousData();
                    cd.vehicleID = c.getString(0);
                    cd.longitude = c.getFloat(1);
                    cd.latitude = c.getFloat(2);
                    dataCollection.add(cd);
                }
                c.close();

                ContinuousDataCollection wrapper = new ContinuousDataCollection();
                wrapper.setContinuousData(dataCollection);
                controller.postContinuousData(wrapper);
            }

            String sqlStmt = "update oddc set DataUploaded = 1 where rowid in ( select rowid from oddc where rowid % "+String.valueOf(SAMPLE_FREQ)+" = 0 and DataUploaded = 0 limit "+String.valueOf(SENDCOUNT)+" )";
            db.execSQL(sqlStmt);

            Log.d("ALFREDO","SendToFLA tid="+Thread.currentThread().getId()+" Cursor.nrows="+nrows);
        }
    }

    private static String getTimestamp(){
        //SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS" );
        //SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-mm-dd hh:mm:ss.fffffffff" );
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
        Date date = new Date();

        return dateFormat.format(date);
    }

}
