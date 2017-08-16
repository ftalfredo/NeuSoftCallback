/**
 * Created by yzharchuk on 8/2/2017.
 */

package client.oddc.fla.com.model;

import java.sql.Time;

public class SelectiveUploadTask extends ODDCTask
{
    private boolean mediaUpload;
    private boolean msrdDataUpload;
    private Time startTime;
    private Time endTime;

    public boolean isMediaUpload()
    {
        return mediaUpload;
    }
    public void setMediaUpload(boolean mediaUpload)
    {
        this.mediaUpload = mediaUpload;
    }
    public boolean isMsrdDataUpload()
    {
        return msrdDataUpload;
    }
    public void setMsrdDataUpload(boolean msrdDataUpload)
    {
        this.msrdDataUpload = msrdDataUpload;
    }
    public Time getStartTime()
    {
        return startTime;
    }
    public void setStartTime(Time startTime)
    {
        this.startTime = startTime;
    }
    public Time getEndTime()
    {
        return endTime;
    }
    public void setEndTime(Time endTime)
    {
        this.endTime = endTime;
    }
}
