/**
 * Created by yzharchuk on 8/1/2017.
 */

package client.oddc.fla.com.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import client.oddc.fla.com.utilities.Utilities;

public class ContinuousData
{
    @JsonProperty("id")
    public String id;
    @JsonProperty("sessionID")
    public String sessionID;
    @JsonProperty("vehicleID")
    public String vehicleID;
    @JsonProperty("driverID")
    public String driverID;
    @JsonProperty("submitterID")
    public String submitterID;

    @JsonProperty("gpsTimeStamp")
    public Timestamp gpsTimeStamp;
    @JsonProperty("longitude")
    public double longitude;
    @JsonProperty("latitude")
    public double latitude;
    @JsonProperty("speed")
    public double speed;
    @JsonProperty("speedDetectionType")
    public int speedDetectionType;

    @JsonProperty("accelerationTimeStamp")
    public Timestamp accelerationTimeStamp;
    @JsonProperty("accelerationX")
    public double accelerationX;
    @JsonProperty("accelerationY")
    public double accelerationY;
    @JsonProperty("accelerationZ")
    public double accelerationZ;

    @JsonProperty("gShockTimeStamp")
    public Timestamp gShockTimeStamp;
    @JsonProperty("gShockEvent")
    public boolean gShockEvent;
    @JsonProperty("gShockEventThreshold")
    public double gShockEventThreshold;

    @JsonProperty("fcwTimeStamp")
    public Timestamp fcwTimeStamp;
    @JsonProperty("fcwExistFV")
    public boolean fcwExistFV;
    @JsonProperty("fcwCutIn")
    public boolean fcwCutIn;
    @JsonProperty("fcwDistanceToFV")
    public double fcwDistanceToFV;
    @JsonProperty("fcwRelativeSpeedToFV")
    public double fcwRelativeSpeedToFV;
    @JsonProperty("fcwEvent")
    public boolean fcwEvent;
    @JsonProperty("fcwEventThreshold")
    public double fcwEventThreshold;

    @JsonProperty("ldwTimeStamp")
    public Timestamp ldwTimeStamp;
    @JsonProperty("ldwDistanceToLeftLane")
    public double ldwDistanceToLeftLane;
    @JsonProperty("ldwDistanceToRightLane")
    public double ldwDistanceToRightLane;
    @JsonProperty("ldwEvent")
    public boolean ldwEvent;
    @JsonProperty("mediaURI")
    public String mediaURI;

    public static ContinuousData createDummyContinuousData(String session, String vehicle, String driver, String submitter)
    {
        ContinuousData data = new ContinuousData();
        data.id = Utilities.generateUUIDString();
        data.sessionID = session;
        data.vehicleID = vehicle;
        data.driverID = driver;
        data.submitterID = submitter;

        data.gpsTimeStamp = new Timestamp(new Date().getTime());
        data.longitude = Math.random() * Math.PI * 2;
        data.latitude = Math.acos(Math.random() * 2 - 1);
        data.speed = (double) (Math.random() * (50)) + 50;
        data.speedDetectionType = 4;

        data.accelerationTimeStamp = new Timestamp(new Date().getTime());
        data.accelerationX = (int) (Math.random() * 10) + 1;
        data.accelerationY = (int) (Math.random() * 10) + 1;
        data.accelerationZ = (int) (Math.random() * 10) + 1;

        data.gShockTimeStamp = new Timestamp(new Date().getTime());
        data.gShockEvent = Math.random() < 0.5;
        data.gShockEventThreshold = (int) (Math.random() * 10) + 1;
        data.fcwTimeStamp = new Timestamp(new Date().getTime());
        data.fcwExistFV = Math.random() < 0.5;
        data.fcwCutIn = Math.random() < 0.5;
        data.fcwDistanceToFV = (double) new Random().nextInt(2) + 3;
        data.fcwRelativeSpeedToFV = (double) new Random().nextInt(2) + 3;
        data.fcwEvent = Math.random() < 0.5;
        data.fcwEventThreshold = (double) (Math.random() * (50)) + 50;

        data.ldwTimeStamp = new Timestamp(new Date().getTime());
        data.ldwDistanceToLeftLane = (double) new Random().nextInt(2) + 3;
        data.ldwDistanceToRightLane = (double) new Random().nextInt(2) + 3;
        data.ldwEvent = Math.random() < 0.5;
        data.mediaURI = "some/media/uri";
        return data;
    }
}
