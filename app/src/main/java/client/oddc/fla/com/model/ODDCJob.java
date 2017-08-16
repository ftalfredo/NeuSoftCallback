/**
 * Created by yzharchuk on 8/2/2017.
 */

package client.oddc.fla.com.model;

import java.sql.Timestamp;
import java.util.ArrayList;

public class ODDCJob
{
    public ODDCJob() {}
    private String id;
    private String sessionId;
    private Timestamp jobTimeStamp;
    private String transportTrigger;
    private String activationTrigger;
    private ArrayList <ODDCTask> tasks;
    private JobStatus status;

    public String getSessionId()
    {
        return sessionId;
    }
    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }
    public Timestamp getJobTimestamp()
    {
        return jobTimeStamp;
    }
    public void setJobTimeStamp(Timestamp jobTimeStamp)
    {
        this.jobTimeStamp = jobTimeStamp;
    }
    public JobStatus getStatus()
    {
        return status;
    }
    public void setStatus(JobStatus status)
    {
        this.status = status;
    }
    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public String getTransportTrigger()
    {
        return transportTrigger;
    }
    public void setTransportTrigger(String transportTrigger)
    {
        this.transportTrigger = transportTrigger;
    }
    public String getActivationTrigger()
    {
        return activationTrigger;
    }
    public void setActivationTrigger(String activationTrigger)
    {
        this.activationTrigger = activationTrigger;
    }
    public ArrayList<ODDCTask> getTasks()
    {
        return tasks;
    }
    public void setTasks(ArrayList<ODDCTask> tasks)
    {
        this.tasks = tasks;
    }
}