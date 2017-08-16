/**
 * Created by yzharchuk on 8/2/2017.
 */

package client.oddc.fla.com.model;

import java.util.ArrayList;

public class ODDCTask
{
    private String id;
    private TaskType type;

    public String getId()
    {
        return id;
    }
    public void setId(String id)
    {
        this.id = id;
    }
    public TaskType getType()
    {
        return type;
    }
    public void setType(TaskType type)
    {
        this.type = type;
    }


    //TODO: this is ugly temp solution for polymorphic JSON serialization

    private int msrdReportFrequency; //interval in seconds
    private ArrayList<EventType> eventsToReport;
    private ArrayList<String> targetedCameras;

    public ArrayList<String> getTargetedCameras()
    {
        return targetedCameras;
    }
    public void setTargetedCameras(ArrayList<String> targetedCameras)
    {
        this.targetedCameras = targetedCameras;
    }
    public int getMsrdReportFrequency()
    {
        return msrdReportFrequency;
    }
    public void setMsrdReportFrequency(int msrdReportFrequency)
    {
        this.msrdReportFrequency = msrdReportFrequency;
    }
    public ArrayList <EventType> getEventsToReport()
    {
        return eventsToReport;
    }
    public void setEventsToReport(ArrayList <EventType> eventsToReport)
    {
        this.eventsToReport = eventsToReport;
    }
}