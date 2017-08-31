/**
 * Created by yzharchuk on 8/1/2017.
 */

package client.oddc.fla.com.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import client.oddc.fla.com.utilities.Utilities;

public class ContinuousData
{

    @JsonProperty("vehicleID")
    public String vehicleID;
    @JsonProperty("id")
    public String id;
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
    public int ldwEvent;
    @JsonProperty("mediaURI")
    public String mediaURI;

    //yz I did not touch them
    @JsonIgnore
    public boolean mediaDeleted;
    @JsonIgnore
    public boolean mediaUploaded;
    @JsonIgnore
    public boolean dataUploaded;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getGpsTimeStamp() {
        return gpsTimeStamp;
    }

    public void setGpsTimeStamp(Timestamp gpsTimeStamp) {
        this.gpsTimeStamp = gpsTimeStamp;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getSpeedDetectionType() {
        return speedDetectionType;
    }

    public void setSpeedDetectionType(int speedDetectionType) {
        this.speedDetectionType = speedDetectionType;
    }

    public Timestamp getAccelerationTimeStamp() {
        return accelerationTimeStamp;
    }

    public void setAccelerationTimeStamp(Timestamp accelerationTimeStamp) {
        this.accelerationTimeStamp = accelerationTimeStamp;
    }

    public double getAccelerationX() {
        return accelerationX;
    }

    public void setAccelerationX(double accelerationX) {
        this.accelerationX = accelerationX;
    }

    public double getAccelerationY() {
        return accelerationY;
    }

    public void setAccelerationY(double accelerationY) {
        this.accelerationY = accelerationY;
    }

    public double getAccelerationZ() {
        return accelerationZ;
    }

    public void setAccelerationZ(double accelerationZ) {
        this.accelerationZ = accelerationZ;
    }

    public Timestamp getgShockTimeStamp() {
        return gShockTimeStamp;
    }

    public void setgShockTimeStamp(Timestamp gShockTimeStamp) {
        this.gShockTimeStamp = gShockTimeStamp;
    }

    public boolean isgShockEvent() {
        return gShockEvent;
    }

    public void setgShockEvent(boolean gShockEvent) {
        this.gShockEvent = gShockEvent;
    }

    public double getgShockEventThreshold() {
        return gShockEventThreshold;
    }

    public void setgShockEventThreshold(double gShockEventThreshold) {
        this.gShockEventThreshold = gShockEventThreshold;
    }

    public Timestamp getFcwTimeStamp() {
        return fcwTimeStamp;
    }

    public void setFcwTimeStamp(Timestamp fcwTimeStamp) {
        this.fcwTimeStamp = fcwTimeStamp;
    }

    public boolean isFcwExistFV() {
        return fcwExistFV;
    }

    public void setFcwExistFV(boolean fcwExistFV) {
        this.fcwExistFV = fcwExistFV;
    }

    public boolean isFcwCutIn() {
        return fcwCutIn;
    }

    public void setFcwCutIn(boolean fcwCutIn) {
        this.fcwCutIn = fcwCutIn;
    }

    public double getFcwDistanceToFV() {
        return fcwDistanceToFV;
    }

    public void setFcwDistanceToFV(double fcwDistanceToFV) {
        this.fcwDistanceToFV = fcwDistanceToFV;
    }

    public double getFcwRelativeSpeedToFV() {
        return fcwRelativeSpeedToFV;
    }

    public void setFcwRelativeSpeedToFV(double fcwRelativeSpeedToFV) {
        this.fcwRelativeSpeedToFV = fcwRelativeSpeedToFV;
    }

    public boolean isFcwEvent() {
        return fcwEvent;
    }

    public void setFcwEvent(boolean fcwEvent) {
        this.fcwEvent = fcwEvent;
    }

    public double getFcwEventThreshold() {
        return fcwEventThreshold;
    }

    public void setFcwEventThreshold(double fcwEventThreshold) {
        this.fcwEventThreshold = fcwEventThreshold;
    }

    public Timestamp getLdwTimeStamp() {
        return ldwTimeStamp;
    }

    public void setLdwTimeStamp(Timestamp ldwTimeStamp) {
        this.ldwTimeStamp = ldwTimeStamp;
    }

    public double getLdwDistanceToLeftLane() {
        return ldwDistanceToLeftLane;
    }

    public void setLdwDistanceToLeftLane(double ldwDistanceToLeftLane) {
        this.ldwDistanceToLeftLane = ldwDistanceToLeftLane;
    }

    public double getLdwDistanceToRightLane() {
        return ldwDistanceToRightLane;
    }

    public void setLdwDistanceToRightLane(double ldwDistanceToRightLane) {
        this.ldwDistanceToRightLane = ldwDistanceToRightLane;
    }

    public int isLdwEvent() {
        return ldwEvent;
    }

    public void setLdwEvent(int ldwEvent) {
        this.ldwEvent = ldwEvent;
    }

    public String getMediaURI() {
        return mediaURI;
    }

    public void setMediaURI(String mediaURI) {
        this.mediaURI = mediaURI;
    }
}
