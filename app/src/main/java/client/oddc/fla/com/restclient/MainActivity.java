package client.oddc.fla.com.restclient;

import android.icu.util.TimeZone;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import android.os.Environment;
import android.content.Context;

import android.provider.BaseColumns;
import android.content.ContentValues;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.DatabaseUtils;


import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import android.widget.TextView;

import client.oddc.fla.com.model.ContinuousData;
import client.oddc.fla.com.model.ContinuousDataCollection;
import client.oddc.fla.com.model.ODDCJob;


public class MainActivity extends AppCompatActivity {
    public static Context mContext;
    public static final String DATABASE_NAME = "oddc.db";

    public static int frameRate = 30; // for NeuSoft set value somewhere for getFrameRate()
    public static int getFrameRate(){return frameRate;}

    public String currentFilename = "";  // TESTING ONLY
    public File mVideoFolder; // for NeuSoft set value
    public String baseUrl;

    ODDCclass oddc;
    boolean oddcOK = false;


    final NeusoftSimulator nsc = new NeusoftSimulator(); // TESTING ONLY
    boolean cTimerRunning = false; // TESTING ONLY
    TextView msgView; // TESTING ONLY
    TextView dbCount; // TESTING ONLY
    TextView fsCount; // TESTING ONLY


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baseUrl = getString(R.string.base_url);

        dbCount = (TextView) findViewById(R.id.dbCount); // TESTING ONLY
        msgView = (TextView) findViewById(R.id.msgView); // TESTing ONLY
        fsCount = (TextView) findViewById(R.id.fsCount); // TESTing ONLY


        oddc = new ODDCclass();
        mContext = getApplicationContext();
        nsc.setListener(oddc);
        oddc.setListener(nsc);
        createVideoFolder(); // for NeuSoft is this needed?
        oddcOK = oddc.ok2Startup();
    }


    // TESTING ONLY
    public void getJobList(View view){
        RESTController controller = new RESTController(baseUrl);
        ArrayList<ODDCJob> jlist = controller.getJobList();
        if (jlist != null) {
            msgView.setText("\nODDCJob sessionId="+jlist.get(0).getSessionId());
        }
    }

    // TESTING ONLY
    public void onCheckFS(View view) {
        File[] vFiles = mVideoFolder.listFiles();
        if (vFiles != null) {
            fsCount.setText(String.valueOf(vFiles.length));
        }
    }



    // TESTING ONLY
    public void onViewFS(View view){
        File[] vFiles = mVideoFolder.listFiles();
        if (vFiles != null) {
            ArrayList<String> vidFiles = new ArrayList<String>();
            for (File f : vFiles) {
                vidFiles.add(f.getPath());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.fslistview_layout, vidFiles);
            ListView fsview = (ListView) findViewById(R.id.fslistView);
            fsview.setAdapter(adapter);
        }
    }

    // TESTING ONLY
    public void onDelete(View view){
        //MediaDeleted

        //String sqlStmt = new String("update oddc set DataUploaded = 1 where rowid % "+SAMPLE_FREQ+" = 0 and DataUploaded = 0");
        //db.execSQL(sqlStmt);
    }


    public void onStart(View view){cTimerRunning = true;oddcOK = true;}  // TESTING ONLY
    public void onStop(View view){cTimerRunning = false;oddcOK = false;}  // TESTING ONLY
    public void onDropTable(View view){oddc.dbh.dropTable();} // TESTING ONLY



    // TESTING ONLY
    public void onViewDB(View view){
        SimpleCursorAdapter dataAdapter;

        msgView.setText("");
        String[] caFrom = {
                DBschema._ID,
                DBschema.GPS_TS,
                DBschema.D_U,
                DBschema.M_D,
                DBschema.M_URI
        };
        int[] caTo = new int[]{
                R.id.rowVal,
                R.id.gpsTSval,
                R.id.duVal,
                R.id.mdVal,
                R.id.mediaURI
        };
        String omsg = oddc == null ? "oddc=NULL" : "oddc=NOT NULL";
        Log.d("ALFREDO","onViewDB "+omsg);
        if (oddc.db != null){
            String dmsg = oddc.db == null ? "oddc.db=NULL" : "oddc.db=NOT NULL";
            Log.d("ALFREDO","onViewDB oddc="+omsg+" "+dmsg);
            Cursor cursor = oddc.db.query(
                    DBschema.TABLE_NAME,
                    caFrom,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            dataAdapter = new SimpleCursorAdapter(
                    this, R.layout.dblistview_layout,
                    cursor,
                    caFrom,
                    caTo,
                    0);
            ListView listview = (ListView) findViewById(R.id.dblistView);
            listview.setAdapter(dataAdapter);
            //cursor.close();
        }
    }






    // NeuSoft callbacks which ODDC invokes to send data to NeuSoft
    public interface NeuSoftInterface {
        public void onFLAparam(int param); // TBD
    }

    // If NeuSoft creates folder, please set mVideoFolder to location
    private void createVideoFolder() {
        mVideoFolder = mContext.getDir("FLA", Context.MODE_PRIVATE); //Creating an internal dir;
    }


    // TESTING, however this class implements NeuSoftInterface
    // Some NeuSoft class would implement NeuSoftInterface as does this class,
    // in order to call oddcOK = listener.onContinuousData(data);
    public class NeusoftSimulator extends Thread implements NeuSoftInterface {
        private ODDCclass listener;



        Timer ctimer; // TESTING ONLY
        Timer vtimer; // TESTING ONLY


        public void setListener(ODDCclass listener){
        this.listener = listener;
    }  // for NeuSoft

        public void onFLAparam(int param){} // for NeuSoft, example only at this time, param(s) TBD

        public NeusoftSimulator() {

            int fRate = 1000 / getFrameRate(); // TESTING ONLY
            int vRate = 60 * 1000; // TESTING ONLY
            ctimer = new Timer();
            ctimer.scheduleAtFixedRate(new TimerTask(){
                public void run(){
                    //Log.d("ALFREDO","NeusoftSimulator.ctimer");
                    if (cTimerRunning) {
                        oddcOK = listener.onContinuousData(mkContinuousData()); // for NeuSoft to send ContinuousData to ODDC
                    }
                }
            }, 1000, fRate);

            vtimer = new Timer(); // for testing only
            vtimer.scheduleAtFixedRate(new TimerTask(){ /* for testing only */
                public void run(){
                    boolean ok;
                    //Log.d("ALFREDO","NeusoftSimulator.vtimer oddcOK="+oddcOK);
                    if (! cTimerRunning) return;
                    if (! oddcOK) return; // fileSysCheck not OK
                    try {
                        currentFilename = mkFileName();
                        String fname = mVideoFolder.getPath() + File.separator + currentFilename;

                        File f = new File(fname);
                        try {
                            ok = f.createNewFile();
                            //Log.d("ALFREDO","NeusoftSimulator.vtimer "+ok+" fname="+fname);
                        }
                        catch(IOException ioe){Log.d("ALFREDO","NeusoftSimulator.vtimer IOException");}
                    }
                    catch (NullPointerException npe){Log.d("ALFREDO","NeusoftSimulator.vtimer NullPointerException");}
                }
            }, 1000,1000);    /*vRate, vRate);*/
        }


    // TESTING ONLY
    public ContinuousData mkContinuousData()
    {
        Timestamp dateTime = new Timestamp(new Date().getTime());

        // NeuSoft prepares data for transfer somewhere in their code
        ContinuousData cd = new ContinuousData();

        //Create dummy data
        cd.vehicleID = "2JOHN41JXMN109186"; // VIN
        //cd.timezone = 0;

        cd.gpsTimestamp = dateTime; // from OS not GPS
        cd.longitude = getRandomFloat();
        cd.latitude = getRandomFloat();
        cd.speed = getRandomFloat();
        cd.speedDetectionType = 0;

        cd.accelerationTimeStamp = dateTime; /* yyyy-MM-dd HH:mm:ss.SSS for SQLite */
        cd.accelerationX = getRandomFloat();
        cd.accelerationY = getRandomFloat();
        cd.accelerationZ = getRandomFloat();

        cd.gShockTimeStamp = dateTime;
        cd.gShockEvent = false;
        //cd.gShockEventThreshold = getRandomFloat(); /* might be a parameter from FLA */

        cd.fcwTimeStamp = dateTime;
        cd.fcwExistFV = getRandomBoolean();
        //cd.fcwTimeToCollision = 0;
        cd.fcwDistanceToFV = getRandomFloat();

        cd.fcwEvent = getRandomBoolean();
        cd.fcwEventThreshold = getRandomFloat();

        cd.ldwTimeStamp = dateTime;
        cd.ldwDistanceToLeftLane = getRandomFloat();
        cd.ldwDistanceToRightLane = getRandomFloat();
        cd.ldwEvent = getRandomBoolean();
        cd.mediaURI = getCurrentFilename();

        return cd;
    }

    // TESTING ONLY
    private float getRandomFloat()
    {
        float randomNumber = 0 + (float)(Math.random() * 100);
        return randomNumber;
    }

    // TESTING ONLY
    private Boolean getRandomBoolean() {
        int randomNumber = 0 + (int) (Math.random() * 100);
        if (randomNumber < 65) {
            return true;
        }
        return false;
    }

    // TESTING ONLY
    public String getCurrentFilename()
    {
        return currentFilename;
    }

    // TESTING ONLY
    private String mkFileName(){
            SimpleDateFormat dateFormat = new SimpleDateFormat( "yyMMdd_HHmmss" );
            Date date = new Date();
            return dateFormat.format(date)+".mp4";
        }
    }













    // ODDC below here



    // ODDC callbacks which NeuSoft invokes to send data to ODDC
    public interface ODDCinterface {
        public boolean onContinuousData(ContinuousData data);
        public List<String> getContinuousLog();
        public List<String> getEventLog();
        public List<String> getOnDemandLog();
        public boolean ok2Startup();
        public boolean reqShutdown();
    }

    // Fujitsu supplied class containing callback functions to receive data from NeuSoft
    public class ODDCclass extends Thread implements ODDCinterface {
        //File dataDir;
        RESTController controller;

        Timestamp t0,t1;

		int MIN_AVAIL_FS = 1024 * 1024 * 1024;
		int SAMPLE_FREQ = 60; // ALFREDO parameter from SLA?
        int FRAMES_PER_MIN;
		int SENDCOUNT;


        public ODDCdbHelper dbh;
        public SQLiteDatabase db = null;
        TimeZone tz;

        private NeusoftSimulator listener;
        private int loopCount = 0;

        public ODDCclass(){
            FRAMES_PER_MIN = getFrameRate() * 60;
            SENDCOUNT = getFrameRate() * 60 / SAMPLE_FREQ;
            t0 = null;
            t1 = null;
        }

        // for NeuSoft, need to know name of NeuSoft listener class
        public void setListener(NeusoftSimulator listener){
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
			dbh = new ODDCdbHelper(mContext);
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
	        public void dropTable(){
                db.execSQL(SQL_DROP_TABLE);
                onCreate(db);
            }
	    }

        public List<String> getContinuousLog(){return Arrays.asList("TBD");}
        public List<String> getEventLog(){return Arrays.asList("TBD");}
        public List<String> getOnDemandLog(){return Arrays.asList("TBD");}


        public boolean onContinuousData(final ContinuousData data){
            // Fujitsu processing of continuous data received from NeuSoft
            long fmRate = 0;
            long t0ms = 0;
            long t1ms = 0;

            if (t0 == null){
                t0 = data.fcwTimeStamp;
                t1 = t0;
            }
            else {
                t0 = t1;
                t1 = data.fcwTimeStamp;
                t0ms = t0.getTime();
                t1ms = t1.getTime();
                frameRate = (int)(1000 / ( t1ms - t0ms));
                FRAMES_PER_MIN = frameRate * 60;
                SENDCOUNT = frameRate * 60 / SAMPLE_FREQ;
            }

Log.d("ALFREDO","onContinuousData() "+String.valueOf(FRAMES_PER_MIN)+" "+String.valueOf(loopCount)+" fmRate="+String.valueOf(frameRate)+" t0="+String.valueOf(t0ms)+" t1="+String.valueOf(t1ms));
            insertSQLite(data);

            if (loopCount > FRAMES_PER_MIN){
                SendToFLA fla = new SendToFLA();
                fla.start();
                //sendToFLA();
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
            //values.put(DBschema.GS_ET, data.gShockEventThreshold);

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
            dbCount.post(new Runnable() {
                public void run() {
                    final int cnt = getRowCount();
                    //Log.d("ALFREDO","MainActivity.insertSQLite "+String.valueOf(cnt));
                    String dbt = "DB rowCount="+String.valueOf(cnt);
                    dbCount.setText(dbt);
                }
            });

            return  true;
        }


        // YYMMDD_HHMMSS.mp4
        public long checkFileSpace(){
            long availSpace = mVideoFolder.getUsableSpace();

            //Log.d("ALFREDO","checkFileSpace MIN_AVAIL_FS="+String.valueOf(MIN_AVAIL_FS)+" availSpace="+String.valueOf(availSpace));

            if (availSpace > MIN_AVAIL_FS) return availSpace;
            else                           return -1; // delete some files
        }


        public void sendToFLAAAA(){

            //String[] columns = new String[]{"vehicleID","gpsTimestamp","longitude","latitude"};
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

            Log.d("ALFREDO","SendToFLA Cursor.nrows="+nrows);
		}
    }

    public class SendToFLA extends Thread {
        public SendToFLA(){super ("SendToFLA");}
        public void run(){
            String[] columns = new String[]{"vehicleID","longitude","latitude"};
            String selection = new String("rowid in ( select rowid from oddc where rowid % ? = 0 and DataUploaded = 0 limit ? )");
            String[] selectionArgs = new String[]{String.valueOf(oddc.SAMPLE_FREQ),String.valueOf(oddc.SENDCOUNT)};

            Cursor c = oddc.db.query (DBschema.TABLE_NAME,
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
                oddc.controller.postContinuousData(wrapper);
            }

            String sqlStmt = "update oddc set DataUploaded = 1 where rowid in ( select rowid from oddc where rowid % "+String.valueOf(oddc.SAMPLE_FREQ)+" = 0 and DataUploaded = 0 limit "+String.valueOf(oddc.SENDCOUNT)+" )";
            oddc.db.execSQL(sqlStmt);

            Log.d("ALFREDO","SendToFLA Cursor.nrows="+nrows);
        }
    }

    public class DBschema implements BaseColumns {
        public static final String TABLE_NAME = "oddc";
        public static final String VIN = "VehicleID";
        public static final String TZ = "TimeZone";

        public static final String GPS_TS = "GPStimeStamp";
        public static final String GPS_LON = "longitude";
        public static final String GPS_LAT = "latitude";

        public static final String SPEED = "Speed";
        public static final String SPEED_DT = "SpeedDetectionType";

        public static final String ACC_TS = "AccelerationTimeStamp";
        public static final String ACC_X = "AccelerationX";
        public static final String ACC_Y = "AccelerationY";
        public static final String ACC_Z = "AccelerationZ";

        public static final String GS_TS = "GShockTimeStamp";
        public static final String GS_E = "GShockEvent";
        public static final String GS_ET = "GShockEventThreshold";

        public static final String FCW_TS = "FCWTimeStamp";
        public static final String FCW_EFV = "FCWExistFV";
        public static final String FCW_CI = "FCWCutIn";
        public static final String FCW_TTC = "FCWTimeToCollision";
        public static final String FCW_DFV = "FCWDistanceToFV";
        //public static final String FCW_RSFV = "FCWRelativeSpeedToFV";
        public static final String FCW_E = "FCWEvent";
        public static final String FCW_ET = "FCWTEventThreshold";

        public static final String LDW_TS = "LDWTimeStamp";
        public static final String LDW_DLL = "LDWDistanceToLeftLane";
        public static final String LDW_DRL = "LDWDistanceToRightLane";
        public static final String LDW_E = "LDWEvent";

        public static final String M_URI = "MediaURI";
        public static final String M_D = "MediaDeleted";
        public static final String M_U = "MediaUploaded";
        public static final String D_U = "DataUploaded";
    }




    private static String getTimestamp(){
        //SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS" );
        //SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-mm-dd hh:mm:ss.fffffffff" );
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );
        Date date = new Date();

        return dateFormat.format(date);
    }
}
