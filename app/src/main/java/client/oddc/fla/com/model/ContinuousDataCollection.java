package client.oddc.fla.com.model;

import java.util.ArrayList;

/**
 * Created by yzharchuk on 8/10/2017.
 */

public class ContinuousDataCollection
{
    private ArrayList<ContinuousData> continuousData;
    private String mediaUri;


    public ContinuousDataCollection()
    {
    }

    public ContinuousDataCollection(ArrayList<ContinuousData> continuousData, String mediaUri)
    {
        this.continuousData = continuousData;
        this.mediaUri = mediaUri;
    }

    public ArrayList<ContinuousData> getContinuousData()
    {
        return continuousData;
    }

    public String getMediaUri()
    {
        return mediaUri;
    }

    public void setMediaUri(String mediaUri)
    {
        this.mediaUri = mediaUri;
    }

    public void setContinuousData(ArrayList<ContinuousData> continuousData)
    {
        this.continuousData = continuousData;
    }
}
