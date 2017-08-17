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
import client.oddc.fla.com.restclient.ODDCclass;
import client.oddc.fla.com.restclient.NeuSoftSimulator;


public class MainActivity extends AppCompatActivity {
    public static Context mContext;
    public static final String DATABASE_NAME = "oddc.db";

    public static int frameRate = 30; // for NeuSoft set value somewhere for getFrameRate()
    public static int getFrameRate(){return frameRate;}


    public static File mVideoFolder; // for NeuSoft set value
    public static String baseUrl;

    ODDCclass oddc;
    public static boolean oddcOK = false;


    NeuSoftSimulator nsc = new NeuSoftSimulator(); // TESTING ONLY
    static boolean cTimerRunning = false; // TESTING ONLY
    TextView msgView; // TESTING ONLY
    public static TextView dbCount; // TESTING ONLY
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

    public static String getBaseUrl(){return baseUrl;}
    public static File getVideoFolder(){return mVideoFolder;}
    public static boolean isCTimerRunning(){return cTimerRunning;} // TESTING ONLY


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
    public void onDropTable(View view){oddc.dropTable();} // TESTING ONLY



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


    // If NeuSoft creates folder, please set mVideoFolder to location
    private void createVideoFolder() {
        mVideoFolder = mContext.getDir("FLA", Context.MODE_PRIVATE); //Creating an internal dir;
    }

}
