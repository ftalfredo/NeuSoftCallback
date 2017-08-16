/**
 * Created by yzharchuk on 8/1/2017.
 */

package client.oddc.fla.com.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.Date;

public class ContinuousData
{
    @JsonProperty("messageID")
    public int messageID;
    @JsonProperty("sessionID")
    public String sessionID;
    @JsonProperty("vehicleID")
    public String vehicleID;
    @JsonProperty("driverID")
    public String driverID;
    @JsonProperty("submitterID")
    public String submitterID;

    @JsonProperty("gpsTimestamp")
    public Timestamp gpsTimestamp;
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
    //public double gShockEventThreshold; // might be a parameter from FLA

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
}
