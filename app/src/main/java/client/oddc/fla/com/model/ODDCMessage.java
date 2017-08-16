/**
 * Created by yzharchuk on 8/2/2017.
 */

package client.oddc.fla.com.model;

import java.sql.Timestamp;

public class ODDCMessage
{
    private int messageID;
    private Envelope envelope;
    private MSRDData msrdData;

    private Timestamp accelerationTimeStamp;
    private double accelerationX;
    private double accelerationY;
    private double accelerationZ;

    private Timestamp gShockTimeStamp;
    private boolean gShockEvent;
    private float gShockEventThreshold;

    private Timestamp fcwTimeStamp;
    private boolean fcwExistFV;
    private boolean fcwCutIn;
    private double fcwDistanceToFV;
    private double fcwRelativeSpeedToFV;
    private boolean fcwEvent;
    private double fcwTEventThreshold;

    private Timestamp ldwTimeStamp;
    private double ldwDistanceToLeftLane;
    private double ldwDistanceToRightLane;
    private boolean ldwEvent;

    private String mediaURI;
    private boolean mediaProtected;
    private boolean mediaUploaded;


    public int getMessageID()
    {
        return messageID;
    }
    public void setMessageID(int messageID)
    {
        this.messageID = messageID;
    }
    public Timestamp getAccelerationTimeStamp()
    {
        return accelerationTimeStamp;
    }
    public void setAccelerationTimeStamp(Timestamp accelerationTimeStamp)
    {
        this.accelerationTimeStamp = accelerationTimeStamp;
    }
    public double getAccelerationX()
    {
        return accelerationX;
    }
    public void setAccelerationX(double accelerationX)
    {
        this.accelerationX = accelerationX;
    }
    public double getAccelerationY()
    {
        return accelerationY;
    }
    public void setAccelerationY(double accelerationY)
    {
        this.accelerationY = accelerationY;
    }
    public double getAccelerationZ()
    {
        return accelerationZ;
    }
    public void setAccelerationZ(double accelerationZ)
    {
        this.accelerationZ = accelerationZ;
    }
    public Timestamp getgShockTimeStamp()
    {
        return gShockTimeStamp;
    }
    public void setgShockTimeStamp(Timestamp gShockTimeStamp)
    {
        this.gShockTimeStamp = gShockTimeStamp;
    }
    public boolean isgShockEvent()
    {
        return gShockEvent;
    }
    public void setgShockEvent(boolean gShockEvent)
    {
        this.gShockEvent = gShockEvent;
    }
    public float getgShockEventThreshold()
    {
        return gShockEventThreshold;
    }
    public void setgShockEventThreshold(float gShockEventThreshold)
    {
        this.gShockEventThreshold = gShockEventThreshold;
    }
    public Timestamp getFcwTimeStamp()
    {
        return fcwTimeStamp;
    }
    public void setFcwTimeStamp(Timestamp fcwTimeStamp)
    {
        this.fcwTimeStamp = fcwTimeStamp;
    }
    public boolean isFcwExistFV()
    {
        return fcwExistFV;
    }
    public void setFcwExistFV(boolean fcwExistFV)
    {
        this.fcwExistFV = fcwExistFV;
    }
    public boolean isFcwCutIn()
    {
        return fcwCutIn;
    }
    public void setFcwCutIn(boolean fcwCutIn)
    {
        this.fcwCutIn = fcwCutIn;
    }
    public double getFcwDistanceToFV()
    {
        return fcwDistanceToFV;
    }
    public void setFcwDistanceToFV(double fcwDistanceToFV)
    {
        this.fcwDistanceToFV = fcwDistanceToFV;
    }
    public double getFcwRelativeSpeedToFV()
    {
        return fcwRelativeSpeedToFV;
    }
    public void setFcwRelativeSpeedToFV(double fcwRelativeSpeedToFV)
    {
        this.fcwRelativeSpeedToFV = fcwRelativeSpeedToFV;
    }
    public boolean isFcwEvent()
    {
        return fcwEvent;
    }
    public void setFcwEvent(boolean fcwEvent)
    {
        this.fcwEvent = fcwEvent;
    }
    public double getFcwTEventThreshold()
    {
        return fcwTEventThreshold;
    }
    public void setFcwTEventThreshold(double fcwTEventThreshold)
    {
        this.fcwTEventThreshold = fcwTEventThreshold;
    }
    public Timestamp getLdwTimeStamp()
    {
        return ldwTimeStamp;
    }
    public void setLdwTimeStamp(Timestamp ldwTimeStamp)
    {
        this.ldwTimeStamp = ldwTimeStamp;
    }
    public double getLdwDistanceToLeftLane()
    {
        return ldwDistanceToLeftLane;
    }
    public void setLdwDistanceToLeftLane(double ldwDistanceToLeftLane)
    {
        this.ldwDistanceToLeftLane = ldwDistanceToLeftLane;
    }
    public double getLdwDistanceToRightLane()
    {
        return ldwDistanceToRightLane;
    }
    public void setLdwDistanceToRightLane(double ldwDistanceToRightLane)
    {
        this.ldwDistanceToRightLane = ldwDistanceToRightLane;
    }
    public boolean isLdwEvent()
    {
        return ldwEvent;
    }
    public void setLdwEvent(boolean ldwEvent)
    {
        this.ldwEvent = ldwEvent;
    }
    public String getMediaURI()
    {
        return mediaURI;
    }
    public void setMediaURI(String mediaURI)
    {
        this.mediaURI = mediaURI;
    }
    public boolean isMediaProtected()
    {
        return mediaProtected;
    }
    public void setMediaProtected(boolean mediaProtected)
    {
        this.mediaProtected = mediaProtected;
    }
    public boolean isMediaUploaded()
    {
        return mediaUploaded;
    }
    public void setMediaUploaded(boolean mediaUploaded)
    {
        this.mediaUploaded = mediaUploaded;
    }
}
