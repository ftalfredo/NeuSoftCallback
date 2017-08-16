/**
 * Created by yzharchuk on 8/2/2017.
 */

package client.oddc.fla.com.model;

import java.util.ArrayList;

public class UploadTask extends ODDCTask
{
    private int msrdReportFrequency; //interval in seconds
    private ArrayList <EventType> eventsToReport;
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
    public ArrayList<EventType> getEventsToReport()
    {
        return eventsToReport;
    }
    public void setEventsToReport(ArrayList <EventType> eventsToReport)
    {
        this.eventsToReport = eventsToReport;
    }
}

