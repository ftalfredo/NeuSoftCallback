package client.oddc.fla.com.restclient;

import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import client.oddc.fla.com.model.ContinuousData;


public class NeusoftHandler implements NeuSoftInterface {
    private ODDCclass listener;


    Timer ctimer; // TESTING ONLY
    Timer vtimer; // TESTING ONLY

    public String currentFilename = "";  // TESTING ONLY


    public void setListener(ODDCclass listener){
        this.listener = listener;
    }  // for NeuSoft


    public void sentToFLA(int param){}
    public void onFLAparam(int param){} // for NeuSoft, example only at this time, param(s) TBD

    public NeusoftHandler() {

        Log.d("ODDC THREAD ","NeusoftSimulator TID="+String.valueOf(Process.myTid()));

        currentFilename = mkFileName();

        int fRate = 1000 / MainActivity.getFrameRate(); // TESTING ONLY
        int vRate = 60 * 1000; // TESTING ONLY
        ctimer = new Timer();
        ctimer.scheduleAtFixedRate(new TimerTask(){
            public void run(){
                //Log.d("ODDC","NeusoftSimulator.ctimer");
                if ( MainActivity.isCTimerRunning()) {
                    MainActivity.oddcOK = listener.onContinuousData(mkContinuousData()); // for NeuSoft to send ContinuousData to ODDC
                }
            }
        }, 1000, fRate);

        vtimer = new Timer(); // for testing only
        vtimer.scheduleAtFixedRate(new TimerTask(){ /* for testing only */
            public void run(){
                boolean ok;
                //Log.d("ODDC","NeusoftSimulator.vtimer oddcOK="+oddcOK);
                if (! MainActivity.isCTimerRunning()) return;
                //if (! MainActivity.oddcOK) return; // fileSysCheck not OK
                try {
                    currentFilename = mkFileName();
                    String fname = MainActivity.mVideoFolder.getPath() + File.separator + currentFilename;

                    File f = new File(fname);
                    try {
                        ok = f.createNewFile();
                        Log.d("ODDC NEWFILE","NeusoftSimulator.createNewFile "+ok+" "+f.toString());
                    }
                    catch(IOException ioe){Log.d("ODDC","NeusoftSimulator.vtimer IOException");}
                }
                catch (NullPointerException npe){Log.d("ODDC","NeusoftSimulator.vtimer NullPointerException");}
            }
        }, 1000,2000);    /*vRate, vRate);*/
    }


    // TESTING ONLY   //yz there is static ContinuousData.createDummyContinuousData(). Feel free to change it for your needs.
    public ContinuousData mkContinuousData()
    {
        Timestamp dateTime = new Timestamp(new Date().getTime());

        // NeuSoft prepares data for transfer somewhere in their code
        ContinuousData cd = new ContinuousData();

        //Create dummy data
        cd.vehicleID = "2JOHN41JXMN109186"; // VIN
        //cd.timezone = 0;

        cd.gpsTimeStamp = dateTime; // from OS not GPS
        cd.longitude = -118.15;
        cd.latitude = 34.03;
        cd.speed = getRandomFloat();
        cd.speedDetectionType = 0;

        cd.accelerationTimeStamp = dateTime; /* yyyy-MM-dd HH:mm:ss.SSS for SQLite */
        cd.accelerationX = getRandomFloat();
        cd.accelerationY = getRandomFloat();
        cd.accelerationZ = getRandomFloat();

        cd.gShockTimeStamp = dateTime;
        cd.gShockEvent = getRandomBoolean();
        cd.gShockEventThreshold = getRandomFloat(); /* might be a parameter from FLA */

        cd.fcwTimeStamp = dateTime;
        cd.fcwExistFV = getRandomBoolean();
        cd.fcwCutIn = getRandomBoolean();
        //cd.fcwTimeToCollision = 0;
        cd.fcwDistanceToFV = getRandomFloat();
        cd.fcwEvent = getRandomBoolean();
        cd.fcwEventThreshold = getRandomFloat();

        cd.ldwTimeStamp = dateTime;
        cd.ldwDistanceToLeftLane = getRandomFloat();
        cd.ldwDistanceToRightLane = getRandomFloat();
        cd.ldwEvent = getRandomInt() % 3;
        cd.mediaURI = currentFilename;
Log.d("ODDC MKCONTDATA","currentFilename="+currentFilename+" vehicleID="+cd.vehicleID);
        return cd;
    }

    // TESTING ONLY
    private int getRandomInt()
    {
        int randomNumber = 0 + (int)(Math.random() * 10);
        return randomNumber;
    }
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
    private String mkFileName(){
        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyMMdd_HHmmss" );
        Date date = new Date();
        return dateFormat.format(date)+".mp4";
    }
}


