/**
 * Created by yzharchuk on 8/2/2017.
 */

package client.oddc.fla.com.model;

import java.util.ArrayList;
import java.util.HashMap;

public class ODDCTask
{
    private String id;
    private TaskType type;
    private int msrdReportFrequency; //interval in seconds
    private double gshockEventThreshold;

    private ArrayList<EventType> eventsToReport;
    private ArrayList<String> targetedCameras;

    private HashMap<String, Object> parameters;

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

    public double getgshockEventThreshold()
    {
        return gshockEventThreshold;
    }
    public void setgshockEventThreshold(double gshockEventThreshold)
    {
        this.gshockEventThreshold = gshockEventThreshold;
    }

    public HashMap<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, Object> parameters) {
        this.parameters = parameters;
    }
}