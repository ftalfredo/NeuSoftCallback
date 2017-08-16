/**
 * Created by yzharchuk on 8/2/2017.
 */

package client.oddc.fla.com.model;

import java.sql.Timestamp;
public class MSRDData
{
    private Timestamp msrdTimeStamp;
    private double longitude;
    private double latitude;
    private double speed;
    private SpeedDetectionType speedDetectionType;

    public Timestamp getMsrdTimeStamp()
    {
        return msrdTimeStamp;
    }
    public void setMsrdTimeStamp(Timestamp msrdTimeStamp)
    {
        this.msrdTimeStamp = msrdTimeStamp;
    }
    public double getLongitude()
    {
        return longitude;
    }
    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }
    public double getLatitude()
    {
        return latitude;
    }
    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }
    public double getSpeed()
    {
        return speed;
    }
    public void setSpeed(double speed)
    {
        this.speed = speed;
    }
    public SpeedDetectionType getSpeedDetectionType()
    {
        return speedDetectionType;
    }
    public void setSpeedDetectionType(SpeedDetectionType speedDetectionType)
    {
        this.speedDetectionType = speedDetectionType;
    }
}
