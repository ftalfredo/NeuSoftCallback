package client.oddc.fla.com.restclient;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import client.oddc.fla.com.model.ODDCJob;


public class MainActivity extends AppCompatActivity {
    public static Context mContext;
    public static final String DATABASE_NAME = "oddc.db";

    public static int frameRate = 2; // for NeuSoft set value somewhere for getFrameRate()
    public static int getFrameRate(){return frameRate;}


    public static File mVideoFolder; // for NeuSoft set value
    public static String baseUrl;

    ODDCclass oddc;
    public static boolean oddcOK = false;


    static NeusoftHandler nsc = new NeusoftHandler(); // TESTING ONLY
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

        mContext = getApplicationContext();
        createVideoFolder(); // for NeuSoft is this needed?

        oddc = new ODDCclass(baseUrl,mContext,mVideoFolder);
        nsc.setListener(oddc);
        oddc.setListener(nsc);

        oddcOK = oddc.ok2Startup();
    }

    //public static String getBaseUrl(){return baseUrl;}
    //public static File getVideoFolder(){return mVideoFolder;}
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
    public void onPBlist(View view) {
        ArrayList<PlaybackList> pbl = oddc.getPlaybackList();
        if (pbl != null) {
            if (pbl.size() > 0) {
                for (int i = 0; i < pbl.size(); i++) {
                    Log.d("ODDC ONPBLIST", "onPBlist " + pbl.get(i).MediaURI + " " + pbl.get(i).GShockEvent + " " + pbl.get(i).FCWEvent + " " + pbl.get(i).LDWEvent);
                }
            }
        }
    }


    // TESTING ONLY
    public void onCheckFS(View view) {
        File[] vFiles = mVideoFolder.listFiles();
        if (vFiles != null) {
            fsCount.setText(String.valueOf(vFiles.length)+" files");
        }
    }

    public void onCopy(View view){
        File currentDB = mContext.getDatabasePath(DATABASE_NAME);
        Log.d("ODDC ONCOPY","currentDB="+currentDB.toString());

        boolean copyIN = ( view.getId() == R.id.btnCopyIN ) ? true : false;


        Log.d("ODDC ONCOPY","copyIN="+copyIN);

        File md = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        Log.d("ODDC ONCOPY","MainActivity getExternalStoragePublicDirectory.DIRECTORY_MOVIES="+md.toString()+" canWrite="+md.canWrite());

        File backupDB = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),"backup.db");

        try {
            FileChannel src;
            FileChannel dst;
            if (copyIN){
                src = new FileInputStream(backupDB).getChannel();
                dst = new FileOutputStream(currentDB).getChannel();
            }
            else {
                src = new FileInputStream(currentDB).getChannel();
                dst = new FileOutputStream(backupDB).getChannel();
            }
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
            Log.d("ODDC ONCOPY","transferFrom ");
        }
        catch (FileNotFoundException fnfe){
            Log.e("ODDC ERR","FileNotFoundException"+fnfe.toString());
        }
        catch (IOException ioe){
            Log.e("ODDC ERR","IOException"+ioe.toString());
        }
    }

    // TESTING ONLY
    public void onViewFS(View view){
        File[] vFiles = mVideoFolder.listFiles();
        if (vFiles != null) {
            ArrayList<String> vidFiles = new ArrayList<String>();
            for (File f : vFiles) {
                vidFiles.add(f.getPath());
                Log.d("ODDC VIEWFILES","f="+f.toString());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.fslistview_layout, vidFiles);
            ListView fsview = (ListView) findViewById(R.id.fslistView);
            fsview.setAdapter(adapter);

            oddc.checkFileSpace();
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
                DBschema.M_D,
                DBschema.M_U,
                DBschema.D_U,
                DBschema.M_URI,
                DBschema.GS_E,
                DBschema.FCW_E,
                DBschema.LDW_E
        };
        int[] caTo = new int[]{
                R.id.rowVal,
                R.id.gpsTSval,
                R.id.mdVal,
                R.id.muVal,
                R.id.duVal,
                R.id.mediaURI,
                R.id.gsEventVal,
                R.id.fcwEventVal,
                R.id.ldwEventVal
        };
        String omsg = oddc == null ? "oddc=NULL" : "oddc=NOT NULL";
        Log.d("ODDC","onViewDB "+omsg);
        if (oddc.db != null){

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

            Log.d("ODDC","onViewDB rowCount="+cursor.getCount());
            //cursor.close();
        }
    }


    // If NeuSoft creates folder, please set mVideoFolder to location
    private void createVideoFolder() {
        mVideoFolder = mContext.getDir("oddc", Context.MODE_PRIVATE); //Creating an internal dir;
        Log.d("ODDC CREATEVIDFILDER","mVideoFolder="+mVideoFolder.toString());
    }

}
