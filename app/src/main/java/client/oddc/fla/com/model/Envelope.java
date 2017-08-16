/**
 * Created by yzharchuk on 8/2/2017.
 */

package client.oddc.fla.com.model;

import java.sql.Timestamp;

public class Envelope
{
    private Timestamp envelopeTimeStamp;
    private String sessionID;
    private String vehicleID;
    private String driverID;
    private String submitterID;

    public Timestamp getEnvelopeTimeStamp()
    {
        return envelopeTimeStamp;
    }
    public void setEnvelopeTimeStamp(Timestamp envelopeTimeStamp)
    {
        this.envelopeTimeStamp = envelopeTimeStamp;
    }
    public String getSessionID()
    {
        return sessionID;
    }
    public void setSessionID(String sessionID)
    {
        this.sessionID = sessionID;
    }
    public String getVehicleID()
    {
        return vehicleID;
    }
    public void setVehicleID(String vehicleID)
    {
        this.vehicleID = vehicleID;
    }
    public String getDriverID()
    {
        return driverID;
    }
    public void setDriverID(String driverID)
    {
        this.driverID = driverID;
    }
    public String getSubmitterID()
    {
        return submitterID;
    }
    public void setSubmitterID(String submitterID)
    {
        this.submitterID = submitterID;
    }
}



