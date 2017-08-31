package client.oddc.fla.com.restclient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.TimeZone;
import android.os.Process;
import android.util.Log;

import org.springframework.http.HttpStatus;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import client.oddc.fla.com.model.ContinuousData;
import client.oddc.fla.com.model.DataPackage;
import client.oddc.fla.com.model.DataPackageType;
import client.oddc.fla.com.model.ODDCJob;
import client.oddc.fla.com.model.Video;



public class ODDCclass implements ODDCinterface {
    private Context mContext;
    private File mVideoFolder; // for NeuSoft set value
    private String baseUrl;


    RESTController controller;
    ArrayList<ODDCJob> jobList;


    String DATABASE_NAME = "oddc.db";

    int MIN_AVAIL_FS = 1024 * 1024 * 1024;
    int SAMPLE_FREQ = 30; // ALFREDO parameter from SLA?
    int FRAMES_PER_MIN;
    int SENDCOUNT;

    ArrayList<String> eventList;
    String currentVideoFile;


    ODDCdbHelper dbh = null;
    SQLiteDatabase db = null;
    TimeZone tz;

    private NeuSoftSimulator listener;
    private int loopCount = 0;

    public ODDCclass(String url, Context context, File folder){
        this.mContext = context;
        this.mVideoFolder = folder;
        this.baseUrl = url;

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

        controller = new RESTController(baseUrl);

        tz = TimeZone.getDefault();
        boolean isDST = tz.observesDaylightTime();
        int dstMillisec = tz.getDSTSavings();
        int ros = tz.getRawOffset();


        // code above is for testing, code below is for production
        Log.d("ALFREDO","ok2Startup mContext="+this.mContext);
        dbh = new ODDCdbHelper(this.mContext);
        Log.d("ALFREDO","ok2Startup dbh="+dbh);
        db = dbh.getWritableDatabase();
        Log.d("ALFREDO","ODDCclass.ok2Startup db = dbh.getWritableDatabase");

        jobList = controller.getJobList();

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


    public ArrayList<PlaybackList> getPlaybackList(){
        Cursor c = db.rawQuery("select MediaURI,sum(GShockEvent),sum(FCWEvent),sum(LDWEvent),MediaDeleted from oddc where MediaURI not in ( select MediaURI from oddc where  MediaDeleted = 1 ) group by MediaURI having ( sum(GShockEvent) > 0 or sum(FCWEvent) > 0 or sum(LDWEvent) > 0 )",null);
        int nrows = c.getCount();
        Log.d("ALFREDO GETPBLIST", "Cursor.nrows="+nrows);
        if (nrows > 0) {
            ArrayList<PlaybackList> pbList = new ArrayList<PlaybackList>();
            while (c.moveToNext()) {
                PlaybackList pb = new PlaybackList();
                pb.MediaURI = c.getString(0);
                pb.GShockEvent = c.getInt(1);
                pb.FCWEvent = c.getInt(2);
                pb.LDWEvent = c.getInt(3);
                pb.MediaDeleted = c.getInt(4);
                pbList.add(pb);
            }
            c.close();
            return pbList;
        }
        c.close();
        return null;
    }


    public boolean onContinuousData(ContinuousData data){
        // Fujitsu processing of continuous data received from NeuSoft

        insertSQLite(data);

        if (loopCount > FRAMES_PER_MIN){
            SendToFLA fla = new SendToFLA(DataPackageType.CONTINUOUS);
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
                    DBschema.LDW_E    + " INT," +

                    DBschema.M_URI    + " VARCHAR(32)," +
                    DBschema.M_U      + " BOOLEAN," +
                    DBschema.M_D      + " BOOLEAN," +
                    DBschema.D_U      + " BOOLEAN )";

    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + DBschema.TABLE_NAME;


    public boolean insertSQLite(ContinuousData data){
        ContentValues values = new ContentValues();
        values.put(DBschema.VIN, data.vehicleID);
        //values.put(DBschema.TZ, tz.getRawOffset()); /* milliseconds */

        values.put(DBschema.GPS_TS, String.valueOf(data.gpsTimeStamp));
        values.put(DBschema.GPS_LON, data.longitude);
        values.put(DBschema.GPS_LAT, data.latitude);

        values.put(DBschema.SPEED, data.speed);
        values.put(DBschema.SPEED_DT, data.speedDetectionType);

        values.put(DBschema.ACC_TS, String.valueOf(data.accelerationTimeStamp));
        values.put(DBschema.ACC_X, data.accelerationX);
        values.put(DBschema.ACC_Y, data.accelerationY);
        values.put(DBschema.ACC_Z, data.accelerationZ);

        values.put(DBschema.GS_TS, String.valueOf(data.gShockTimeStamp));
        values.put(DBschema.GS_E, data.gShockEvent);
        values.put(DBschema.GS_ET, data.gShockEventThreshold);

        values.put(DBschema.FCW_TS, String.valueOf(data.fcwTimeStamp));
        values.put(DBschema.FCW_EFV, data.fcwExistFV);
        values.put(DBschema.FCW_CI, data.fcwCutIn);
        //values.put(DBschema.FCW_TTC, data.fcwTimeToCollision);
        values.put(DBschema.FCW_DFV, data.fcwDistanceToFV);
        //values.put(DBschema.FCW_RSFV, data.fcwRelativeSpeedToFV);
        values.put(DBschema.FCW_E, data.fcwEvent);
        values.put(DBschema.FCW_ET, data.fcwEventThreshold);

        values.put(DBschema.LDW_TS, String.valueOf(data.ldwTimeStamp));
        values.put(DBschema.LDW_DLL, data.ldwDistanceToLeftLane);
        values.put(DBschema.LDW_DRL, data.ldwDistanceToRightLane);
        values.put(DBschema.LDW_E, data.ldwEvent);

        values.put(DBschema.M_URI, data.mediaURI);
        values.put(DBschema.M_D, false);
        values.put(DBschema.M_U, false);
        values.put(DBschema.D_U, false);

        long rid = db.insert(DBschema.TABLE_NAME, null, values);
        Log.d("ALFREDO INSERTSQL","mediaURI="+values.get(DBschema.M_URI));

        if (currentVideoFile != data.mediaURI){
            SendToFLA fla = new SendToFLA(DataPackageType.EVENT);
            fla.start();
        }
        currentVideoFile = data.mediaURI;

        if (data.gShockEvent || data.fcwEvent || (data.ldwEvent > 0)){
            if (! eventList.contains(data.mediaURI)) eventList.add(data.mediaURI);
        }

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
        long availSpace = mVideoFolder.getUsableSpace();

        Log.d("ALFREDO CHECKFILESPACE","MIN_AVAIL_FS="+String.valueOf(MIN_AVAIL_FS)+" availSpace="+String.valueOf(availSpace));

        if (availSpace > MIN_AVAIL_FS) return availSpace;
        else  {
            String[] columns = new String[]{DBschema.GPS_TS,DBschema.GS_E,DBschema.FCW_E,DBschema.FCW_CI,DBschema.LDW_E,DBschema.M_URI,DBschema.M_D,DBschema.M_U};
            String selection = new String("MediaURI NOT IN ( select MediaURI from oddc where GShockEvent = 1 or FCWEvent = 1 or LDWEvent = 1 or MediaDeleted = 1)");
            String limit = new String("2");
            Cursor c = db.query (true,
                    DBschema.TABLE_NAME,
                    columns,
                    selection,
                    null,
                    null,
                    null,
                    null,
                    limit);

            int nrows = c.getCount();
            Log.d("ALFREDO CHECKFILESPACE", "Cursor.nrows="+nrows);
            if (nrows > 0) {
                int i = 0;
                while (c.moveToNext()) {
                    String fname = c.getString(5);
                    File f = new File(mVideoFolder.getAbsolutePath() + File.separatorChar + fname);
                    Log.d("ALFREDO CHECKFILESPACE","f="+f.toString()+" f.exists="+f.exists());
                    if (f.exists()) {
                        f.delete();

                        Log.d("ALFREDO CHECKFILESPACE","file DELETED "+f.toString());
                        String sqlStmt = "update oddc set MediaDeleted = 1 where MediaURI = \'"+fname+"\' ";
                        db.execSQL(sqlStmt);
                    }
                    Log.d("ALFREDO CHECKFILESPACE", c.getString(0) + " GS_E="+c.getInt(1)+" FCW_E="+c.getInt(2)+" FCW_CI="+c.getInt(3)+" LDW_E="+c.getInt(4)+" MediaURI=" + c.getString(5) + " MediaDeleted=" + c.getInt(6) + " MediaUploaded=" + c.getInt(7));
                }
                c.close();
            }
            return -1; // delete some files
        }
    }


    public class SendToFLA extends Thread {
        DataPackageType ptype;
        public SendToFLA(DataPackageType pt){
            super ("SendToFLA");
            ptype = pt;
        }
        public void run(){
            String selection;
            String[] selectionArgs;
            HttpStatus status = HttpStatus.I_AM_A_TEAPOT;
            String[] columns = new String[]{DBschema.FCW_DFV,DBschema.GPS_TS,DBschema.GPS_LON,DBschema.GPS_LAT,
                    DBschema.SPEED,DBschema.SPEED_DT,
                    DBschema.ACC_TS,DBschema.ACC_X,DBschema.ACC_Y,DBschema.ACC_Z,
                    DBschema.GS_TS,DBschema.GS_E,DBschema.GS_ET,
                    DBschema.FCW_TS,DBschema.FCW_EFV,DBschema.FCW_CI,DBschema.FCW_DFV,DBschema.FCW_E,DBschema.FCW_ET,
                    DBschema.LDW_TS,DBschema.LDW_DLL,DBschema.LDW_DRL,DBschema.LDW_E,
                    DBschema.M_URI,DBschema.M_D,DBschema.M_U,DBschema.D_U};
            if (ptype == DataPackageType.CONTINUOUS) {
                selection = new String("rowid in ( select rowid from oddc where rowid % ? = 0 and DataUploaded = 0 limit ? )");
                selectionArgs = new String[]{String.valueOf(SAMPLE_FREQ), String.valueOf(SENDCOUNT)};
            }
            else {
                selection = new String("MediaURI =  ? )");
                selectionArgs = new String[]{ eventList.get(0) };
            }

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
                    cd.gpsTimeStamp = Timestamp.valueOf(c.getString(1));
                    cd.longitude = c.getFloat(2);
                    cd.latitude = c.getFloat(3);
                    cd.speed = c.getFloat(4);
                    cd.speedDetectionType = c.getInt(5);
                    cd.accelerationTimeStamp = Timestamp.valueOf(c.getString(6));
                    cd.accelerationX = c.getFloat(7);
                    cd.accelerationX = c.getFloat(8);
                    cd.accelerationX = c.getFloat(9);
                    cd.gShockTimeStamp = Timestamp.valueOf(c.getString(10));
                    cd.gShockEvent = ( c.getInt(11) != 0 );
                    cd.gShockEventThreshold = c.getDouble(12);
                    cd.fcwTimeStamp = Timestamp.valueOf(c.getString(13));
                    cd.fcwEvent = ( c.getInt(14) != 0 );
                    cd.fcwCutIn = ( c.getInt(15) != 0 );
                    cd.fcwDistanceToFV = c.getFloat(16);
                    cd.fcwEvent = ( c.getInt(17) != 0 );
                    cd.fcwEventThreshold = c.getFloat(18);
                    cd.ldwTimeStamp = Timestamp.valueOf(c.getString(19));
                    cd.ldwDistanceToLeftLane = c.getFloat(20);
                    cd.ldwDistanceToRightLane = c.getFloat(21);
                    cd.ldwEvent = c.getInt(22);
                    cd.mediaURI = c.getString(23);
                    cd.mediaDeleted = ( c.getInt(24) != 0 );
                    cd.mediaUploaded = ( c.getInt(25) != 0 );
                    cd.dataUploaded = ( c.getInt(26) != 0 );
                    dataCollection.add(cd);
                }
                c.close();

                DataPackage dataPackage = new DataPackage(); //yz
                dataPackage.setContinuousData(dataCollection); //yz
                dataPackage.setPackageType(ptype);
                if (ptype == DataPackageType.EVENT) {
                    File evFile = new File(eventList.get(0));
                    try {
                        byte[] vData = FileUtils.readFileToByteArray(evFile);
                        ArrayList<Video> videos = new ArrayList<Video>();
                        videos.add(Video.createDummyVideo(vData));
                        dataPackage.setVideos(videos);
                        Log.d("ALFREDO","SendToFLA EVENT upload "+eventList.get(0));
                    }
                    catch (IOException ioe){
                        Log.e("ALFREDO","IOException FileUtils.readFileToByteArray "+eventList.get(0));
                        return;
                    }
                }
                status = controller.postDataPackage(dataPackage); //yz

                if (status == null) MainActivity.nsc.sentToFLA(-1);
                else {
                    if (status != HttpStatus.OK) {
                        MainActivity.nsc.sentToFLA(-1);
                        Log.e("ALFREDO ERR","SendToFLA HttpStatus NOT OK");
                    }
                    else {
                        String sqlStmt = "update oddc set DataUploaded = 1 where rowid in ( select rowid from oddc where rowid % "+String.valueOf(SAMPLE_FREQ)+" = 0 and DataUploaded = 0 limit "+String.valueOf(SENDCOUNT)+" )";
                        db.execSQL(sqlStmt);
                        if (ptype == DataPackageType.EVENT) eventList.remove(0);
                        Log.d("ALFREDO","SendToFLA DataPackageType="+ptype);
                	}
                }
            }
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
